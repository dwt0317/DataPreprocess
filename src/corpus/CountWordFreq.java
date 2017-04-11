package corpus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class CountWordFreq {

	
	public static void main(String[] args){
		countWordFreq();
		
	}
	

	
	public static void countWordFreq(){
		String filePath = "E:\\Exchange\\computing_ad\\data\\kdd cup 2012 track2\\sample\\mapping\\queryMapping";
		File file = new File(filePath);
		Map<String, Integer> wordFreq = new HashMap<String, Integer>(50000);
		int k = 50;
		PriorityQueue<Integer> pq = new PriorityQueue<Integer>(k);
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
				pq.add(i);
				count++;
			}else{
				int v = pq.peek();
				if (i > v){
					pq.poll();
					pq.add(i);
				}
			}
		}
		System.out.println("words size" + wordFreq.size());
		count = 0;
		while (!pq.isEmpty()){
			System.out.print(pq.poll() + " ");
			if (count++ % 10 == 0) 
				System.out.println();
		}	
	}
}
