package corpus;
import java.util.List;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;



/**
 * @author dwt
 * Data preprocessor for dictionary of query rewriting
 */
public class Processor {
	
	
	
	public static void main(String[] args) throws IOException{
		trimNews();
	}
	
	//分词新闻语料
	public static void splitNews() throws IOException{
		File srcDir = new File("G:\\Exchange\\searchAD\\grad_project\\corpus\\SougouCA_txt");
		File desDir = new File("G:\\Exchange\\searchAD\\grad_project\\corpus\\SougouCA_txt2");
		BufferedReader br;
		BufferedWriter bw;
		File[] files = srcDir.listFiles();
		for(File file:files){
			br = new BufferedReader(new FileReader(file));
			File toFile = new File(desDir+"\\"+file.getName());
			bw = new BufferedWriter(new FileWriter(toFile));
			String line;
			int i=0;
			while((line=br.readLine()) != null){
				String rs = ToDBC(line);
				bw.write(rs);
				bw.newLine();
			}  
			br.close();
			bw.close();
		}		
	}
	
	//清理格式
	public static void trimNews() throws IOException{
		File srcDir = new File("G:\\Exchange\\searchAD\\grad_project\\corpus\\SougouCA_seg");
		File desDir = new File("G:\\Exchange\\searchAD\\grad_project\\corpus\\SougouCA_seg2");
		BufferedReader br;
		BufferedWriter bw;
		File[] files = srcDir.listFiles();
		for(File file:files){
			System.out.println(file.getName());
			br = new BufferedReader(new FileReader(file));
			File toFile = new File(desDir+"\\"+file.getName());
			bw = new BufferedWriter(new FileWriter(toFile));
			String line;
			int i=0;
			while((line=br.readLine()) != null){
				String newline="";
				String[] strs = line.split(" ");
				for(String str:strs){
					newline+=str+"/";
				}
				newline.substring(0, newline.length()-1);
				newline.trim();
				if(i++%10000==0)
					System.out.println(i);
				bw.write(newline);
				bw.newLine();
			}  
			br.close();
			bw.close();
		}	
	}
	
	//解析新闻语料
	public static void parseXml() throws IOException{
//		File dir = new File("G:\\Exchange\\searchAD\\广告\\corpus\\SogouCA");
		File dir = new File("G:\\Exchange\\searchAD\\广告\\corpus\\news_tensite_xml.full");

		File toDir = new File("G:\\Exchange\\searchAD\\广告\\corpus\\SougouCA_txt");
		BufferedReader br;
		BufferedWriter bw = null;		
		
		int chunk = 21;
		int lines=50000;
		File newFile=null;
		File[] files = dir.listFiles();
		for(File f:files){			
			String line;
			String title;
			String content;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "GBK"));
			while((line=br.readLine())!=null){
				if(line.startsWith("<url>")){
					if(line.contains("pic")||line.contains("photo")||line.contains("tuku")||line.contains("odds")){
						int i=5;
						while(i-->0)   //skip picture news
							br.readLine();
						continue;
					}
				}
				if(line.startsWith("<contentt")){  //<contenttitle>
					title=line.substring("<contenttitle>".length(),line.indexOf("</con"));
					line=br.readLine();
					if(title==null||title.equals("")||title.contains("图")||title.contains("视频"))
						continue;					
					if(lines%50000==0){
						if(bw!=null) bw.close();
						newFile = new File(toDir+"\\news_txt_"+chunk);
						bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile),"UTF-8"));
						chunk++;
						System.out.println(chunk);
					}	
					if(title.length()>10){
						bw.append(title);
						bw.newLine();
					}
					content = line.substring("<content>".length(),line.indexOf("</content>"));
					if(content.length()>10&&content!=null&&!content.equals("")){
						bw.append(content);	
						bw.newLine();
					}
					lines++;					
				}			
			}
			br.close();

		}
		bw.close();

	}
	
	
	//分词微博
	public static void splitWeibo() throws IOException{
		BufferedReader br;
		BufferedWriter bw = null;
		String weiboPath = "G:\\Exchange\\searchAD\\广告\\corpus\\weibo\\weibo.txt";
		String line;
		File f = new File(weiboPath);
		br = new BufferedReader(new FileReader(f));
		int chunk = 0;
		int lines=100000;
		File newFile=null;
		String rootPath = "G:\\Exchange\\searchAD\\广告\\corpus\\weibo\\";
		while((line=br.readLine())!=null){
			if(lines%100000==0){
				if(bw!=null) bw.close();
				newFile = new File(rootPath+"weibo_"+chunk);
				bw = new BufferedWriter(new FileWriter(newFile));
				chunk++;
				System.out.println(chunk);
			}		
			bw.append(line);
			bw.newLine();
			lines++;
		}
		bw.close();
		br.close();		
	}
	
	//合并分词词库
	public static void mergeAll() throws IOException{
		BufferedReader br;
		BufferedWriter bw = null;
		HashSet<String> hs = new HashSet<String>();
		String root = "G:\\Exchange\\searchAD\\广告\\wordlib\\use";
		String toFile = "G:\\Exchange\\searchAD\\广告\\wordlib\\use\\words_part2.dict";
		bw = new BufferedWriter(new FileWriter(toFile));
		File[] dirs = new File(root).listFiles();
		for(File f:dirs){
			try {
				String line;
				br = new BufferedReader(new FileReader(f));
				while((line=br.readLine())!=null){
					hs.add(line);
				}
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			for(String s:hs){
				bw.append(s);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//去除词库中重复的词
	public static void removeDuplication(){
		HashSet<String> hs = new HashSet<String>();
		BufferedReader br;
		BufferedWriter bw;
		String root = "G:\\Exchange\\searchAD\\广告\\wordlib\\use";
		File[] dirs = new File(root).listFiles();
		for(File dir:dirs){
			System.out.println(dir.getName());
			File[] files = dir.listFiles();
			for(File f:files){
				try {
					String line;
					br = new BufferedReader(new FileReader(f));
					while((line=br.readLine())!=null){
						hs.add(line);
					}
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			File dataFile = new File(dir.getAbsolutePath()+"//data");
			try {
				bw = new BufferedWriter(new FileWriter(dataFile));
				for(String s:hs){
					bw.append(s);
					bw.newLine();
				}
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			hs.clear();
		}
	}
	
	public static void extractData(){
		String dir="G:\\Exchange\\searchAD\\广告\\wordlib\\data";
		File d = new File(dir);
		File[] files = d.listFiles();
		String desDir = "G:\\Exchange\\searchAD\\广告\\wordlib\\use\\影视\\";
		int i=0;
		for(File f:files){
			if(f.getName().contains("电影")||f.getName().contains("动漫")){
				File fd = new File(desDir+f.getName());
				try {
					Files.copy(f.toPath(),fd.toPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(i++%1000==0)
				System.out.println(i);
		}	
	}

    /**
     * 全角转半角
     * @param input
     * @return
     */
    public static String ToDBC(String input) {

        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
          if (c[i] == '\u3000') {
            c[i] = ' ';
          } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
            c[i] = (char) (c[i] - 65248);
          }
        }
        String returnString = new String(c);
   
        return returnString;
    }

}
