package system.soft.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import org.apache.commons.lang.ArrayUtils;

import system.source.DjadeUtil;
import system.source.FeederBot;

public class WebMaster {
	// HERE WE CREATE PUBLIC VARIABLE FOR CLASS
	private String[] Terms;
	private File TermFile;
	private int Type;
	SetBase Setting;
	// HERE WE CONSTRUCT PUBLIC CLASS
	public WebMaster(SetBase setting){
		Setting=setting;
	}
	
	
	/***************** HERE WE CREATE HELPER METHODS **********************/
	public void term(String args){
		Terms=new String[]{args};
		Type=1;
	}
	
	
	public void term(String[] args){
		Terms=args;
		Type=1;
	}
	
	
	public void term(File args){
		TermFile=args;
		Type=2;
	}
	
	
	/********* HERE WE CONSTRUCT THE CRAWLER * @throws MalformedURLException *********/
	public String[] crawler(){
		// HERE WE CONSTRUCT THE CRAWLER TO DOWNLOAD URL FOR USE
		String[] contents={};
		String[] crawedURL={};
		String[] termData={};
		String fileData="";
		DjadeUtil util;
		FeederBot bot;
		// HERE WE START PROCESSING
		if(Terms!=null || TermFile!=null){
			// HERE WE CHECK TYPE TO GET TERMS
			if(Type==1){
				termData=Terms;
			}
			else if(Type==2){
				// Here we read file to get terms
				if(TermFile.toString().length()>4 && TermFile.toString().indexOf(".")>=0){
					// Here we have file to read
					util=new DjadeUtil();
					try {
						fileData=util.readByScanner(TermFile.toString());
						if(fileData.length()>0){
							if(fileData.indexOf(",")>=0){
								// Here we break file data to array
								termData=fileData.split(",");
							}
							else{
								// We assign like that
								termData=new String[]{fileData};
							}
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
			// HERE WE START PROCESSING TO GET URL
			if(termData.length>0){
				for(int i=0; i<termData.length; i++){
					// Here we craw web to get content
					bot=new FeederBot(termData[i], Setting, "url");
					crawedURL=bot.getRows();
					// NOW LETS CHECK IF WE GOT PAGE
					if(crawedURL!=null && crawedURL.length>0){
						// NOW LETS STORE DATA TO PARENT ARRAY
						if(contents!=null){
							if(contents.length>0){
								contents = (String[])ArrayUtils.addAll(contents, crawedURL);
							}
							else{
								contents = crawedURL;
							}
						}
						else{
							contents = crawedURL;
						}
					} // end of url check
				} // end of loop
			} // end of data check
		}
		// Here we return
		return contents;
	}
	
	
	
	/**************** CONSTRUCTING THE WEB MASTER CHOOSER METHOD ******************/
	public String[] get(String command){
		// HERE WE CONSTRUCT THE GET URL PROCESS
		FileManager manage=new FileManager(Setting);
		String[] contents={};
		String Log="";
		// NOW WE START PROCESSING
		switch(command){
		case "email":
			// Here we set log for mail
			Log=Setting.get("sentLog");
			break;
		case "url":
			// Here we set log for url
			Log=Setting.get("parsedLog");
		}
		// NOW LETS GET OUR CONTENTS
		if(Log!=null && Log.indexOf("/")>=0){
			try {
				contents=manage.getContent(Log, command);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Here we return
		return contents;
	}
	
	// END OF CLASS
}
