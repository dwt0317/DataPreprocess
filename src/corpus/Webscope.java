package corpus;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Webscope {
	public static void main(String[] args) throws IOException{
		getSnippet();
	}
	
	//提取部分文件
	private static void getSnippet() throws IOException{
		File src = new File("E:\\Exchange\\computing_ad\\data\\Webscope_A32\\ydata-ysm-keyphrase-bid-imp-click-v1_0");
		BufferedReader br;
		BufferedWriter bw;
		br = new BufferedReader(new FileReader(src));
		bw = new BufferedWriter(new FileWriter("E:\\Exchange\\computing_ad\\data\\Webscope_A32\\ydata_snippet"));
		String line = "";
		int count = 0;
		while((line = br.readLine())!=null){
			bw.append(line);
			bw.newLine();
			if(count++ == 10000) break;
		}
		br.close();
		bw.close();
	}
}
