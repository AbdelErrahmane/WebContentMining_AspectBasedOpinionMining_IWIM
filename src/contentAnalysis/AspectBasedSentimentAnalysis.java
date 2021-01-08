package contentAnalysis;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AspectBasedSentimentAnalysis {


	/**
	 * ecrit les informations extraites dans un csv 
	 */
	BufferedWriter resultWriter;
	/**
	 * contient les edjectifs et leurs score de sentiments
	 */
	SentimentLexicon sentimentLexicon;
	/**
	 * contient les reviews pr�trait�s: phrase, ses mots et ses POS-tags
	 */
	PreprocessedReviews reviews;		

	
	
	public AspectBasedSentimentAnalysis() throws IOException{
		reviews = new PreprocessedReviews("data/reviews.txt");
		resultWriter = new BufferedWriter ( new FileWriter (new File("data/output.csv")));
		sentimentLexicon = new SentimentLexicon("data/taboadaAdjectiveSentimentLexicon.txt");
	}

	
	/*
	 *   MAIN 
	 */
	public static void main(String[] args) throws IOException{

		AspectBasedSentimentAnalysis absa = new AspectBasedSentimentAnalysis();
		absa.startExtraction();

	}
	
	/** 
	 * it�re sur toutes les phrases dans les reviews et
	 * commence l'extraction des informations sur chaque phrase
	 * @throws IOException
	 */
	public void startExtraction() throws IOException{
		/*
		 * it�re sur toutes les phrases
		 */
		for (TaggedSentence sentence : this.reviews.taggedSentences){
			extractInformation(sentence);
		}
		
		this.resultWriter.close();

	}

	
	/*
	 * ici se passe les choses int�ressantes 	
	 */
	/**
	 * Extrait les informations des phrases 
	 * le contenu de chaque phrase est donn� en deux tableaux
	 *  mot -> contient tous les tokens des phrase
	 * posTags -> contient les POS tag des tokens
	 * @throws IOException 
	 */
	private void extractInformation(TaggedSentence s) throws IOException{

		String[] words = s.getWords(); // contient tous les mots d'une phrase dans l'ordre
		String[] posTags = s.getPosTags(); //contient tous POS tags des mots d'une phrase dans l'ordre

		/*
		 * T�che 1) 
		 *  Jetez un oeil sur le contenu des deux tableaux:
         * Imprimez chaque mot et son posTag (mot -> posTag)
         * En bouclant sur les indices
         *
		 */
		for (int i = 0; i < words.length; i++) {
			System.out.println("le mot est : "+ words[i]+" , son Tag est : "+posTags[i]);

		}

		/*
		 * Task 2) 
		 * sous t�ches sont dans la boucle ci dessous
		 */
		HashMap<String, Integer> noun = new HashMap<String, Integer>();
		int[] indicesMot = new int[1000];
		int j=0;
		int k=0;
		for (int i = 0; i < words.length; i ++) {

			String posTag; // POS-tag for word i  
			int nounFrequency = 0; // frequency of the current word
			String closestAdjective; // closest adjective for the current word
			String sentiment; // sentiment of the current word

			/*
			 * T�che 2.0)
			 * Assigner � la variable posTag le POs tag du mot � la position i
			 */
			posTag = posTags[i];
			/*
			 * T�che 2.1)
			 * S�lectionner tous les noms ici!
			 * Si le nom du mot est un nom(noon):
			 * - attribuez le � la variable noun
			 * - imprimez-la sur la ligne de commande.
			 */
			if (isNoun(posTag)) {
				System.out.println("le nom  ajout� est : "+words[i]);

			}				/*
				 *  T�che 2.2)
				* V�rifiez combien le nom est fr�quent
                * Affecter le a nounFrequency.
                * Imprimez la fr�quence(nounFrequency)  sur la ligne de commande.
				 */
			if (noun.containsKey((words[i]))) {
				noun.put((words[i]), noun.get(words[i]) + 1);
			} else if (isNoun(posTag)) {
				indicesMot[j]=i;
				j++;
				noun.put(words[i], 1);		}	}

		for (String name: noun.keySet()){
			String key = name.toString();
			String value = noun.get(name).toString();
			System.out.println("Le nom est : "+key + " et la fr�quence de  " + value);
		}

		/*
				 * T�che 2.3)
				 * Si le nom est assez fr�quent (au-dessus du seuil de fr�quence):
                  * - recherche de son adjectif le plus proche
				  */
				int frequencyThreshold = 2;

		for (String name: noun.keySet()){
			String key = name.toString();
			if(noun.get(name)>frequencyThreshold) {
				int indexOfAdjProch=	findIndexOfClosestAdjective(posTags ,k ,3);

				System.out.println("le mot "+ words[k]+" est index� dans la position "+ k+" a un adjectif dans la position "+ indexOfAdjProch);
				if (indexOfAdjProch != -1) {
					String polarite= getPolarityForAdjective(words[indexOfAdjProch]);
					System.out.println("l'adjectif: "+ words[indexOfAdjProch]+" et sa polarit�: "+polarite);
				writeExtractedInformationToFile( words[k],  noun.get(name),  words[indexOfAdjProch],  polarite, s.sentence);
			}}
			k++;

		}			/*
					 *  Task 2.4)
					 * - v�rifier si un adjectif est trouv� (autrement, l'indice est -1)
                     * - Imprimer le noun et son adjectif
					 */
		/**cette tache est d�ja effectuer dans la question pr�cidente */
						/*
						 *  T�che 2.5)
						* - d�river la polarit� de l'adjectif et affecter la � la variable sentiment 
						 */
						/**cette tache est d�ja effectuer dans la question effectu� dans la tache  2.3)* */
						
						/*
						 *  T�che 2.6)
						* - �crire les informations extraites dans le fichier r�sultat
                        * Pour une meilleure interpr�tation des r�sultats, nous �crivons �galement
                        * La phrase originale, les informations qui ont �t� extraites de cette phrase
						 */
						/** Cette tache a �t� effectu� dans la tache  2.3)*/
					}
//

	/*
	 * TOUT CI-DESSOUS CETTE LIGNE EST JUSTE POUR L'UTILISATION,
     * VOUS N'AVEZ PAS NECESSAIREMENT � SOIN DES D�TAILS DE MISE EN OEUVRE 
	 */
	
	/**
	 * Renvoie true si posTag fait r�f�rence � tout type de nom, false sinon
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
	 * Renvoie true si posTag fait r�f�rence � tout type d'adjectifs, false sinon
	 * @param posTag
	 * @return
	 */
	private boolean isAdjective(String posTag){
		if( posTag.equalsIgnoreCase("JJ") || posTag.equalsIgnoreCase("JJR") || posTag.equalsIgnoreCase("JJS") ){
			return true;
		}
		return false;
	}

	/**
	 * ecrire les information extraites dans un fichier csv
	 * @param
	 * @throws IOException
	 */
	private void writeExtractedInformationToFile(String noun, int frequency, String adjective, String sentiment, String sentence) throws IOException{
		this.resultWriter.write(noun + "," + frequency + ","  + adjective + ","  + sentiment + "," + sentence.replace(",", "-comma-") +  "\n");
	}

	/**
	 * Prend l'indice d'un nom et renvoie l'index de l'adjectif le plus proche
     * Retourne -1 si aucun adjectif n'est trouv�
	 * @param index
	 * @return
	 */
	private int findIndexOfClosestAdjective(String[] posTags, int index, int searchWindow){

		// chercher a droite et � gauche du mot
		int leftNeighbor = index - 1; 
		int rightNeighbor = index + 1;
		int window = 0;
		System.out.println("Index of noun: " + index);

		// Continuer la recherche dans les deux sens jusqu'� la fin du tableau && dans une fen�tre de trois mots
		while (!(leftNeighbor < 0 && rightNeighbor > posTags.length) && (window <= searchWindow) ){
			try{
				//V�rifier le bon voisin pour l'adjectif
				if (isAdjective(posTags[rightNeighbor])){
					System.out.println("Found right: \t" + rightNeighbor);
					return rightNeighbor; 
				}
				//V�rifier le voisin gauche pour l'adjectif
				if (isAdjective(posTags[leftNeighbor])){
					System.out.println("Found left: \t" + leftNeighbor);
					return leftNeighbor; 
				}

			} catch (Exception e){
				// Ne vous inqui�tez pas, nous avons juste couru hors du tableau sur un c�t�
				//e.printStackTrace ();

			}
			leftNeighbor --;
			rightNeighbor ++;
			window ++;
		}

		// aucun adjectif :(
		return -1;
	}

	/**
	 * Retourne le score de sentiment pour l'adjectif (comme String!)
	 * @param adjective
	 * @return
	 */
	private String getPolarityForAdjective(String adjective){
		return this.sentimentLexicon.getScore(adjective);
	}

	/**
	 * Renvoie la fr�quence du nom dans tout le corpus
	 * @param noun
	 */
	private int getNounFrequency(String noun){
		return this.reviews.getFrequencyForNoun(noun);
	}


}

