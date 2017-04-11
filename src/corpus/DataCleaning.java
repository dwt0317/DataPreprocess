package corpus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.HashSet;

public class DataCleaning {
	private HashSet<String> highFreqWords;
	public static void main(String[] args){
		DataCleaning dc = new DataCleaning();
		dc.rmHighFreqWords();
	}
	
	private void rmHighFreqWords(){
		highFreqWords = new CountWordFreq().countWordFreq();
		File corpusFile = new File(Constants.srcDirectory + "sample//trainning_corpus//ad_corpus.part");
		File cleanCorpusFile = new File(Constants.srcDirectory + "sample//trainning_corpus//ad_corpus_clean.part");
		BufferedReader bro;
		BufferedWriter bw;
		try {
			bro = new BufferedReader(new FileReader(corpusFile));
			bw = new BufferedWriter(new FileWriter(cleanCorpusFile));
			String line = null;
			int i = 0;
			while ((line = bro.readLine()) != null){
				String[] words = line.split("\\|");
				String newLine = "";
				for (String word: words){
					if (!highFreqWords.contains(word))
						newLine += word + "|";
				}
				newLine = newLine.substring(0, newLine.length()-1);
				bw.append(newLine);
				bw.newLine();
				if (i++ % 100000 == 0)
					System.out.println(i);
			}
			bro.close();
			bw.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
