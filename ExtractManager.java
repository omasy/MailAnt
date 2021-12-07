package system.soft.processor;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang.ArrayUtils;

public class ExtractManager {
	// HERE WE SET PUBLIC VARIABLES
	private String Keywords;
	public String TARGETONURL;
	public String[][] EXTRACTED = {};
	public int TOTALPARSEDURL = 0;
	public int TOTALFOUNTURL = 0;
	SetBase Setting;
	// HERE WE CONSTRUCT CLASS
	public ExtractManager(SetBase setting){
		Setting = setting;
	}
	
	
	/************* HERE WE CONSTRUCT HELPER METHOD *****************/
	public void keyword(String arg){
		Keywords = arg;
	}
	
	
	/****************** HERE WE CONSTRUCT THE MANAGER **************/
	public void manager(ExtractManager manager){
		// HERE WE START CONSTRUCTING THE SNDER PROCESS
		int NUM_THREADS=Integer.parseInt(Setting.get("maxThread"));
		String[] foundURL = {}; // Found URL to work with
		int limit=0;
		String[] parseURL = {};
		int rate=0;
		int istype=0;
		int start=0;
		// HERE PROCESSING BEGINS
		foundURL = findURLS(); // Here we get urls
		if(foundURL!=null && foundURL.length>0){
			// Here we loop mails to store send mails
			TOTALFOUNTURL=foundURL.length;
			rate=foundURL.length; // setting rate
			if((rate%2)==1){
				limit=(rate-1)/NUM_THREADS;
			}
			else{
				limit=rate/NUM_THREADS;
			}
			// Now lets check amount of mails
			if(foundURL.length<100){
				limit=foundURL.length;
				NUM_THREADS=1;
				istype=1;
			}
			// Here we set the mails length
			parseURL=new String[limit];
			// HERE WE START THREAD POOL
			ExecutorService es = Executors.newFixedThreadPool(NUM_THREADS);
			List<Future<String[][]>> futures = new ArrayList<>(NUM_THREADS);
					
			// Submit task to every thread:
			for (int i = 0; i < NUM_THREADS; i++) {
			// Here we loop to get mails
				if(start<foundURL.length){
					for(int k=start, j=0; j<foundURL.length; k++, j++){
						if(j<=(limit-1)){
							// Here we assign new values to email array
							if(k<foundURL.length && j<parseURL.length){
								parseURL[j]=foundURL[k];
							}
						}
						else{
							start+=limit;
							break;
						}
					}
					// Here we thread task
					TOTALPARSEDURL += parseURL.length;
					futures.add(i, es.submit((Callable<String[][]>) new Manager(parseURL, manager)));
					try {
						if(EXTRACTED!=null){
							if(EXTRACTED.length>0){
								EXTRACTED = (String[][])ArrayUtils.addAll(EXTRACTED, futures.get(i).get());
							}
							else{
								EXTRACTED = futures.get(i).get();
							}
						}
						else{
							EXTRACTED = futures.get(i).get();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // change send mails
				}
				else{
					istype=1;
					break;
				}
						
				// Checking thread type to prevent multiple run
				if(istype==1){
					break;
				}
			} // end of loop
		        
			// Shutdown thread pool
			es.shutdown();
		}
	}
	
	
	/************* HERE WE CONSTRUCT THE PAGE FOUNDER *************/
	private String[] findURLS(){
		// HERE WE START CONSTRUCTING URL FINDER PROCESS
		WebMaster master = new WebMaster(Setting);
		int limit = Integer.parseInt(Setting.get("extRate"));
		List<String> urls = new ArrayList<String>();
		String[] URLs = {};
		String[] foundURL = {};
		String[] Terms = {};
		// NOW LETS START PROCESSING
		if(Keywords!=null){
			if(Keywords.length()>0){
				if(Keywords.indexOf(",")>=0){
					Terms = Keywords.split(",");
				}
				else{
					Terms = new String[]{Keywords};
				}
				
				// NOW LETS GET OUR URL
				if(Terms.length>0){
					master.term(Terms);
					foundURL = master.crawler();
					// Here we check if found url is above limit
					if(foundURL!=null && foundURL.length>0){
						if(foundURL.length > limit){
							for(int i=0; i<limit; i++){
								urls.add(foundURL[i]);
							}
							// Now lets convert to array
							if(urls.size()>0){
								URLs = urls.toArray(new String[urls.size()]);
							}
						}
						else{
							URLs = foundURL;
						}
					}
				}
			}
		}
		// Here we return
		return URLs;
	}
	
	
	/************* HERE WE CONSTRUCT THE EXTRACTOR ****************/
	private String[][] extract(String[] foundURL){
		// HERE WE CONSTRUCT THE EXTRATION PROCESS
		MailExtractor extractor = new MailExtractor(Setting);
		String[][] foundMails = {};
		// NOW LETS START PROCESSING
		if(foundURL!=null){
			if(foundURL.length>0){
				try { 
					extractor.mailExtract(foundURL);
					TARGETONURL = extractor.TARGETONURL;
					foundMails = extractor.EXTRACTEDMAIL;
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// Here we return
		return foundMails;
	}
	
	
	/**************** HERE WE CONSTRUCT MANAGE CLASS **************/
	public static final class Manager implements Callable<String[][]>{
		private String[] FoundURL;
		ExtractManager Manage;
		
		public Manager(String[] foundURL, ExtractManager manager){
			FoundURL = foundURL;
			Manage = manager;
		}
		
		/*********** HERE WE CALL THE CALLABLE ***********/
		@Override
        public String[][] call() throws Exception {
			try {
	            return Manage.extract(FoundURL);
	        } catch (Throwable t) {
	            t.printStackTrace();
	            throw new RuntimeException(t);
	        }
        }
		
		// END OF INNER CLASS
	}
	
	// END OF MAIN CLASS
}
