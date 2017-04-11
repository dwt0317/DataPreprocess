import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConstructAdCorpus {
	public static String srcDirectory = "G:\\Exchange\\searchAD\\grad_project\\data\\KDD Cup 2012 track2\\";
	private Map<Long, String> descMap;
	private Map<Long, String> titleMap;
	private Map<Long, String> queryMap;
	
	public static void main(String[] args) throws IOException{
		ConstructAdCorpus cac = new ConstructAdCorpus();
		cac.loadMap();
		cac.constructCorpus();
	}
	
	
	private void loadMap(){
		File titleFile = new File(srcDirectory + "titleid_tokensid.txt");
		File descFile = new File(srcDirectory + "descriptionid_tokensid.txt");
		File queryFile = new File(srcDirectory + "queryid_tokensid.txt");
		titleMap = new HashMap<Long, String>(4051440, 1f);
		descMap = new HashMap<Long, String>(3171829, 1f);
//		queryMap = new HashMap<Long, String>(26243604, 1f);
		
		BufferedReader br;
		try {
			//load title map
			br = new BufferedReader(new FileReader(titleFile));
			String line = null;
			int i = 0;
			while ((line = br.readLine()) != null){
				String[] tuple2 = line.split("	");
				titleMap.put(Long.parseLong(tuple2[0]), tuple2[1]);
				if (i++ % 100000 == 0)
					System.out.println(i);
			}
			br.close();
			
			//load description map
			br = new BufferedReader(new FileReader(descFile));
			line = null;
			i = 0;
			while ((line = br.readLine()) != null){
				String[] tuple2 = line.split("	");
				descMap.put(Long.parseLong(tuple2[0]), tuple2[1]);
				if (i++ % 100000 == 0)
					System.out.println(i);
			}
			br.close();
			
			//load query map
//			br = new BufferedReader(new FileReader(queryFile));
//			line = null;
//			i = 0;
//			while ((line = br.readLine()) != null){
//				String[] tuple2 = line.split("	");
//				queryMap.put(Long.parseLong(tuple2[0]), tuple2[1]);
//				if (i++ % 100000 == 0)
//					System.out.println(i);
//			}
//			br.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	private void constructCorpus(){
		System.out.println("Start construct corpus");
		File totalPartFile = new File(srcDirectory + "sample//total.part");
		File corpusFile = new File(srcDirectory + "sample//ad_corpus.part");
		BufferedReader br;
		BufferedWriter bw;
		try {
			br = new BufferedReader(new FileReader(totalPartFile));
			bw = new BufferedWriter(new FileWriter(corpusFile));
			String line = null;
			int i = 0;
			while ((line = br.readLine()) != null){
				String[] tuples = line.split("\t");
				long titleid = Long.parseLong(tuples[9]);
				long descid = Long.parseLong(tuples[10]);
				long queryid = Long.parseLong(tuples[7]);
				bw.append(titleMap.get(titleid)+"|"+descMap.get(descid));
				bw.newLine();
				if (i++ % 100000 == 0)
					System.out.println(i);
			}
			br.close();
			bw.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}
