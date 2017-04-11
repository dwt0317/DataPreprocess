package corpus;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import corpus.Constants;

/**
 * @author dwt
 * 处理word2vec训练语料
 */
public class ConstructAdCorpus {
	
	private Map<Long, String> descMap;
	private Map<Long, String> titleMap;
//	private Map<Long, String> queryMap;
	private String[] queryMap;
	public static void main(String[] args) throws IOException{
		ConstructAdCorpus cac = new ConstructAdCorpus();
//		cac.loadTDMap();
//		cac.constructTDCorpus();
//		cac.constructQueryCorpus();
		cac.mergeCorpus();
	}
	
	
	private void loadTDMap(){
		File titleFile = new File(Constants.srcDirectory + "titleid_tokensid.txt");
		File descFile = new File(Constants.srcDirectory + "descriptionid_tokensid.txt");
		
		titleMap = new HashMap<Long, String>(4051440, 1f);
		descMap = new HashMap<Long, String>(3171829, 1f);
		
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	//query的量过大不能与其他两个同时construct
	private void constructQueryCorpus(){
		File queryFile = new File(Constants.srcDirectory + "queryid_tokensid.txt");
//		queryMap = new HashMap<Long, String>(26243604, 1f);
		queryMap = new String[26243604+2];
		BufferedReader br;
		try {
			//load query map
			br = new BufferedReader(new FileReader(queryFile));
			String line = null;
			int i = 0;
			while ((line = br.readLine()) != null){
				String[] tuple2 = line.split("	");
//				queryMap.put(Long.parseLong(tuple2[0]), tuple2[1]);
				queryMap[Integer.parseInt(tuple2[0])] = tuple2[1];
				if (i++ % 100000 == 0)
					System.out.println(i);
			}
			br.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		System.out.println("Start construct corpus");
		File totalPartFile = new File(Constants.srcDirectory + "sample//total.part");
		File corpusFile = new File(Constants.srcDirectory + "sample//trainning_corpus//ad_query_corpus.part");
		BufferedWriter bw;
		try {
			br = new BufferedReader(new FileReader(totalPartFile));
			bw = new BufferedWriter(new FileWriter(corpusFile));
			String line = null;
			int i = 0;
			while ((line = br.readLine()) != null){
				String[] tuples = line.split("\t");
				int queryid = Integer.parseInt(tuples[7]);
				int click = Integer.parseInt(tuples[0]); 
				//跳过没有点击的语料
				if (click <= 0) continue;
				bw.append(queryMap[queryid]);
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
	
	
	
	
	/**
	 * 构建ad title和description语料
	 */
	private void constructTDCorpus(){
		System.out.println("Start construct corpus");
		File totalPartFile = new File(Constants.srcDirectory + "sample//total.part");
		File corpusFile = new File(Constants.srcDirectory + "sample//trainning_corpus//ad_t_d_corpus.part");
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
				int click = Integer.parseInt(tuples[0]); 
				if (click <= 0 ) continue;
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
	
	//拼接query, title, description
	private void mergeCorpus(){
		File tdCorpusFile = new File(Constants.srcDirectory + "sample//trainning_corpus//ad_t_d_corpus.part");
		File queryCorpusFile = new File(Constants.srcDirectory + "sample//trainning_corpus//ad_query_corpus.part");
		File corpusFile = new File(Constants.srcDirectory + "sample//trainning_corpus//ad_corpus.part");
		BufferedReader brtd;
		BufferedReader brq;
		BufferedWriter bw;
		try {
			brtd = new BufferedReader(new FileReader(tdCorpusFile));
			brq = new BufferedReader(new FileReader(queryCorpusFile));
			bw = new BufferedWriter(new FileWriter(corpusFile));
			String linetd = null, lineq = null;
			int i = 0;
			while ((linetd = brtd.readLine()) != null){
				lineq = brq.readLine();
				bw.append(lineq + "|" + linetd);
				bw.newLine();
				if (i++ % 100000 == 0)
					System.out.println(i);
			}
			brtd.close();
			brq.close();
			bw.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
