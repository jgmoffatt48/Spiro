
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import javax.swing.ImageIcon;


public class FileUtils {

	private String inDirPath;
	private String inFileName;
	private int DEBUG = 0;

	public File readInFile(String logFilePath) {
		File inFile = new File(logFilePath);
		return inFile;
	}

	public File getGZAsFile(String path){
		//BufferedReader gzipReader = new BufferedReader(new FileReader(path));


		try {
			GZIPInputStream  gzip = new GZIPInputStream(new FileInputStream(path));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String getAsString(File file){

		StringBuffer contents = new StringBuffer(); 
		BufferedReader reader = null; 

			try {
				reader = new BufferedReader(new FileReader(file));   
				String text = null;    
				int cnt = 0;
				String sep = System.getProperty("line.separator");
				while ((text = reader.readLine()) != null)             
				{                 
					contents.append(text).append(sep);             
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}        

			return contents.toString();
	}
	
	Vector<Vector<String>> allLogs = new Vector<Vector<String>>();
	public Vector<String> getLineByLineVector(File inFile) {

		Vector<String> lines = new Vector<String>();
		try{
			BufferedReader br  = getBufferReader(inFile);

			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				//System.err.println(strLine);
			//	allLogs.add(splitLine(strLine, "\t"));
				lines.add(strLine);
			}
			//Close the reader
			br.close();
			br = null;
		}catch (Exception e){
			//Catch exception if any
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		
		//System.out.println("Size " + lines.size());
		return lines;
	}

	public String getLineNumbr(File inFile,int num) {
		String strLine = "";
		try{
			BufferedReader br  = getBufferReader(inFile);
			//Read File Line By Line
			int cnt = 1;
			while ((strLine = br.readLine()) != null)   {
				if(cnt++ == num)
					return strLine;
			}
			//Close the reader
			br.close();
			br = null;
		}catch (Exception e){
			//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
		return strLine;
	}
	
	
	public Vector getAllLogsVector(File inFile) {

		//Vector<String> allLogs = new Vector<String>();
		Vector<Vector<String>> allLogs = new Vector<Vector<String>>();
		try{
			BufferedReader br  = getBufferReader(inFile);

			String strLine;
			//Read File Line By Line
			int cnt = 0;
			while ((strLine = br.readLine()) != null)   {
				//System.err.println(strLine);
				allLogs.add(splitLine(strLine, "\t",true));
				//lines.add(strLine);
			}
			//Close the reader
			br.close();
			br = null;
		}catch (Exception e){
			//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
		System.out.println("Size " + allLogs.size());
		return allLogs;
	}
	
	
	Hashtable<String,StringTokenizer>tokenizer = new Hashtable<String,StringTokenizer>();
	public Vector splitLine(String line,String delineator){
		Vector v = new Vector();
		StringTokenizer tokenizer = new StringTokenizer(line,delineator);
		while (tokenizer.hasMoreElements()) {
			String str = (String) tokenizer.nextToken();
		//	System.out.println(object.toString());
			v.add(str);
		}
		return v;
	}
	
	
	public Vector splitLine(String line,String delineator,boolean showToken){
		Vector v = new Vector();
	//	System.err.println(line);
		line = line.replaceAll(delineator , delineator + " ");
		

	//	System.err.println(line);
		StringTokenizer tokenizer = new StringTokenizer(line,delineator,false);
		int cnt = 0;
		while (tokenizer.hasMoreElements()) {
			String str = (String) tokenizer.nextToken();
			
		//	System.out.println(object.toString());
			//if(!str.equals(delineator) || (cnt % 2 == 0))
				v.add(str);
			
			cnt++;
		}
		return v;
	}

	
	
	
	public void writeToFile(Vector<String> lines, String path) {
		BufferedWriter bw = null;

		try {
			//Construct the BufferedWriter object
			bw = new BufferedWriter(new FileWriter(path));

			for (int i = 0; i < lines.size(); i++) {
				if(DEBUG > 0)
					System.out.println(lines.elementAt(i));
				
				bw.write((String)lines.elementAt(i));
				bw.newLine();
			}

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			//Close the BufferedWriter
			try {
				if (bw != null) {
					bw.flush();
					bw.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	
	private BufferedReader getBufferReader(File inFile){
		BufferedReader br = null;
		
		try {
			inDirPath = inFile.getParentFile().getPath();
			inFileName = inFile.getName();
			
			if(inFileName.indexOf(".") > 0)
				inFileName = inFileName.substring(0, inFileName.indexOf("."));
			
			
			br  = new BufferedReader(new FileReader(inFile));
		}catch (Exception e){
			//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return br;
	}
	
	public static void main(String[] args) {
		String logFilePath = "C:\\Program Files\\cognos\\C84GA\\logszzz\\cogserver.log";
		FileUtils utils = new FileUtils();
		
		long start = System.currentTimeMillis();
		System.err.println();
		
		File f =  new File(logFilePath);
		Vector test  =  new Vector();
		
/*		test.add(utils.getLineByLineVector(f));
		System.err.println(test.capacity());
		test.add(utils.getLineByLineVector(f));
		System.err.println(test.capacity());
		test.add(utils.getLineByLineVector(f));*/
		Vector test1  =  utils.getLineByLineVector(f);
		Vector test2  =  utils.getLineByLineVector(f);
	//	Vector test3  =  utils.getLineByLineVector(f);
		utils.getLineByLineVector(f);
		utils.getLineByLineVector(f);
		utils.getLineByLineVector(f);
		utils.getLineByLineVector(f);
		utils.getLineByLineVector(f);
		utils.getLineByLineVector(f);		
		
		
		//System.out.println("lines.size()" + lines.size());
		long end = System.currentTimeMillis();
		
		System.out.println("Took -- " + (end - start)/1000.00 + " seconds");
	}

	int debugCnt = 0;
	public Vector<String> getStringTokens(String line0, String del) {

		
		Vector<String> tokens = new Vector<String>();
		try {
			//	debugCnt++;
				// Break it up
				int atIndex = 0;
				int cnt = 0;
				int dIndex = -1;
				int dqIndex = -1;
				int nextDel = -1;
				String token = "";
				
			//	tokens = new Vector<String>();
				
				//System.err.println("ADD line --- " + debugCnt + " " + line0);
				// TODO single quotes
				String dqStr = '"' + "";
				
				
				if(debugCnt == 16){
					System.out.println("debugCnt " + debugCnt);
					//debugCnt = 100;
					//return tokens;
				}
				
				if(line0.endsWith(del))
					line0 = line0 + " ";
				
				while(atIndex < line0.length()){
				
					dIndex = line0.indexOf(del,atIndex);
					
					if(dIndex < 0)
						dIndex = line0.length();
					
					token = line0.substring(atIndex,dIndex);
					if(cnt++ > 100){
						break;
					}
	
					
					if((token.indexOf(dqStr)) > -1){
						// Index of the next quote
						dqIndex = line0.indexOf(dqStr, atIndex + 1);
						nextDel =  line0.indexOf(del,dqIndex);
						
						
						if(dqIndex < 0){
							tokens.add(token);
						}
						else{
							nextDel = line0.indexOf(del,nextDel);
							
							if(nextDel > line0.length()){
								nextDel = line0.length();
							}
							if(atIndex > line0.length()){
								atIndex = line0.length();
							}
							
							if(debugCnt == 10){
								System.out.println("line0 = " + line0);
								System.out.println("token = " + token);
								System.out.println("atIndex = " + atIndex);
								System.out.println("nextDel = " + nextDel);
								
								
							}
							
							token =  line0.substring(atIndex,nextDel);
							tokens.add(token);
							//System.err.println("ADD TOKEN --- " + t1);
							atIndex = atIndex + del.length() + token.length();
						}
					}
					else{
					//	t1 =  line0.substring(atIndex,line0.indexOf(del,dqIndex));
						tokens.add(token);
						atIndex = atIndex + del.length() + token.length();
					//	System.err.println("ADD TOKEN --- " + t1);
					}
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		System.err.println(":::::::::::::::::::::::::::::: tokens.size() -- " + tokens.size());
		return tokens;
	}
	
	public ImageIcon getImageIcon(String imgName) {

		File iconFile = new File("./cussupp/viewer/images/" + imgName);
		ImageIcon ii = null;
		if (iconFile.exists()) {
			ii = new ImageIcon("./cussupp/viewer/images/" + imgName);
		} else {
			ii = new ImageIcon("./bin/cussupp/viewer/images/" + imgName);
		}

		return ii;
	}
	
}