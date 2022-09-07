package DnfHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TxtLog {
	
	private static FileWriter fw = null; 
	private static PrintWriter out = null;  
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static {
		try {
			File file = new File("D:/dnfLog");
			if(!file.exists()) {
				file.mkdirs();
			}
			File list[] = file.listFiles();
			file = new File("D:/dnfLog/dnfLog_"+(list.length+1)+".txt");
			if(file.exists() || file.createNewFile()){
				fw = new FileWriter(file); 
				out = new PrintWriter(fw);
			}
		} catch (Exception e) {
			// nothing
		}
	}
	
	public static void closeLog() {
		try {
			fw.close();
			out.close();
		} catch (IOException e) {
			// nothing
		}
	}
	
	public static void log(String content) {
		if(out != null) {
			out.println(df.format(new Date())+"    "+content);
			out.flush();
		}
	}
}
