package contentAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class SentimentLexicon {
	
	
	HashMap<String, String> lexicon;
	
	public SentimentLexicon(String fileName) throws IOException{
		this.lexicon = new HashMap<String, String>();
		readFromFile(fileName);
	}
	
	private void readFromFile(String fileName) throws IOException{
		BufferedReader reader = new BufferedReader (new FileReader (new File(fileName)));
		String line = reader.readLine();
		while (line != null){
			String[] entry = line.split("\t");
			this.lexicon.put(entry[0].trim().toLowerCase(), entry[1].trim());
			line = reader.readLine();
		}
	}

	/**
	 * retourne le score du sentiment d'un mot
	 * si le mot n'existe pas dans la liste alors retourne 0
	 * @param word
	 * @return
	 */
	public String getScore(String word){
		if (this.lexicon.containsKey(word.toLowerCase())){
			return this.lexicon.get(word.toLowerCase());
		}
		return "NN";
	}
	
}
