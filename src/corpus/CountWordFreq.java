package corpus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;


/**
 * @author dwt
 * 统计KDD训练语料词频并选出前100
 */
public class CountWordFreq {

	
	public static void main(String[] args){
		new CountWordFreq().countWordFreq();
	}
	
	class CompareWord implements Comparable<CompareWord>{
		String val;
		int freq;
		@Override
		public int compareTo(CompareWord o) {
			return freq - o.freq;
		}
		
		public CompareWord(String val, int freq){
			this.val = val;
			this.freq = freq;
		}
	}
	
	public HashSet<String> countWordFreq(){
		HashSet<String> highFreqWords = new HashSet<String>(100);
		String filePath = Constants.srcDirectory + "sample\\mapping\\queryMapping";
		File file = new File(filePath);
		Map<String, Integer> wordFreq = new HashMap<String, Integer>(50000);
		int k = 100;
		PriorityQueue<CompareWord> pq = new PriorityQueue<CompareWord>(k);
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null){
				String words = line.split("\t")[1];
				for (String word: words.split("\\|")){
					if(wordFreq.containsKey(word)){
						wordFreq.put(word, wordFreq.get(word)+1);
					}else
						wordFreq.put(word, 1);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int count = 0;
		for (Map.Entry<String, Integer> entry : wordFreq.entrySet()){
			Integer i = entry.getValue();
			if (count < k){
				pq.add(new CompareWord(entry.getKey(),i));
				count++;
			}else{
				CompareWord v = pq.peek();
				if (i > v.freq){
					pq.poll();
					pq.add(new CompareWord(entry.getKey(),i));
				}
			}
		}
		System.out.println("words size" + wordFreq.size());
		count = 0;
		while (!pq.isEmpty()){
			highFreqWords.add(pq.poll().val);
			if (count++ % 10 == 0) 
				System.out.println();
		}	
		return highFreqWords;
	}
}
