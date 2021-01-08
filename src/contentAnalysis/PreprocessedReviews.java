package contentAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class PreprocessedReviews {

	/**
	 * Liste des phrases POS-tagged
	 */
	public Vector<TaggedSentence> taggedSentences;
	/**
	 * c'est une Hashmap contient tous les mots et leurs fréquences dans le corpus
	 * key: noun
	 * value: frequency
	 */
	HashMap<String, Integer> nounFrequencies;


	public PreprocessedReviews(String fileName) throws IOException{

		taggedSentences = new Vector<TaggedSentence>();
		nounFrequencies = new HashMap<String, Integer>();
		this.preprocessReviews(fileName);
	}
	
	/**
	 * retourne la fréquence d'une nom données dans le corpus
	 * @param noun
	 * @return frequency
	 */
	public int getFrequencyForNoun(String noun){
		if (this.nounFrequencies.get(noun) != null){
			return this.nounFrequencies.get(noun);
		}
		else {
			return 0;
		}	
	}
	
	

	/**
	 * retourne true si le POS tag est un type de nom
	 * @param posTag
	 * @return
	 */
	private boolean isNoun(String posTag){
		if( posTag.equalsIgnoreCase("NN") || posTag.equalsIgnoreCase("NNP") || posTag.equalsIgnoreCase("NNPS") || posTag.equalsIgnoreCase("NNS")){
			return true;
		}
		return false;
	}
	
	
	/**
	 * appliquer les outils NLP de Stanford Core au contenu d'un fichier
	 * écrit les traitement dans un nouveau fichier
	 * @param fileName
	 * @throws IOException
	 */
	private void preprocessReviews(String fileName) throws IOException{

		
		/**
		 * STANFORD CORE NLP pipeline
		 */
	    StanfordCoreNLP pipeline;

		
		
	    Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma");
	    pipeline = new StanfordCoreNLP(props);

	    
	    
	    // read data ça veut dire notre fichier
	    BufferedReader reader = new BufferedReader(new FileReader (new File(fileName)));
	    String text = "";
	    String line = reader.readLine();
	    while (line != null){
	    	text = text.concat(line + "\n");
	    	line = reader.readLine();
	    }

	    // creer une annotation vide pour un texte donné
	    Annotation document = new Annotation(text);
	    
	    // démarrer les annotations sur ce fichier
	    pipeline.annotate(document);
	    
	    // ce sont toutes les phrases 
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    

	    /*
	     * boucle sur toutes les sentences 
	     */
	   
	    for(CoreMap sentence: sentences) {
	    	
	    
	    	// une tableau de sentences 
	    	String[] words = new String[sentence.get(TokensAnnotation.class).size()];
	    	String[] posTags = new String[sentence.get(TokensAnnotation.class).size()];
	    	int index = 0;


	    	/*
	    	 * boucle sur tous les tokens in sentence, 
	    	 * mettre les information dans deux tableaux (un pour les mots, un pour les POS tag)
	    	 */
	    	for (CoreLabel token: sentence.get(TokensAnnotation.class)) {

	    		//  texte du token
	    		String word = token.get(TextAnnotation.class); 
	    		// POS tag du token 
	    		String posTag = token.get(PartOfSpeechAnnotation.class); 
	    		
	    		
	    		// les occurences de férquence por chaque mot
	    		if (isNoun(posTag)){
	    			if (this.nounFrequencies.containsKey(word)){
	    				this.nounFrequencies.put(word, this.nounFrequencies.get(word) + 1);
	    			}
	    			else {
	    				this.nounFrequencies.put(word, 1);
	    			}
	    		}
	    		
	    		
	    		// mettre les mots et les POS tag dnas des tableaux 
	    		words[index] = word;
	    		posTags[index] = posTag;
	    		index ++;
	    	}	      
	    	
	    	//creer une phrase avec des POS tag et mettre la dans une liste 
	    	taggedSentences.add(new TaggedSentence(words, posTags));
	    	//extractInformation(words, posTags);
	    }

	}
}
