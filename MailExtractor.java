package system.soft.processor;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import system.source.DjadeUtil;

public class MailExtractor {
	// HERE WE SET PUBLIC VARIABLE FOR CLASS
	public String[][] EXTRACTEDMAIL;
	public String TARGETONURL;
	private SetBase Settings;
	private DjadeUtil Util;
	FileManager Manage;
	// HERE WE CONSTRUCT CLASS
	public MailExtractor(SetBase settings){
		Settings=settings;
		Util=new DjadeUtil();
		Manage=new FileManager(Settings);
	}
	
	
	
	/*** CONSTRUCTING THE MAIL EXTRACTOR METHOD * @throws MalformedURLException **/
	public int mailExtract(String[] urls) throws MalformedURLException{
		// HERE WE START CONSTRUCTING THE MAIL EXTRACTED PROCESS
		String pattern="[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
		List<String> emails = new ArrayList<String>();
		FileManager.Filter filter;
		String[][] mailData = {};
		String[][] newData = {};
		String[] parts = {};
		int trueValue = 0;
		// HERE WE START PROCESSING
		if(urls!=null && urls.length>0){
			// Now lets extract mails
			Pattern pat = Pattern.compile(pattern);
			filter = Manage.filter();
			// Now lets loop
			emails=parse(urls, pat);
			if(emails.size()>0){
				// Setting mail data size and processing
				mailData=new String[emails.size()][3];
				for(int i=0; i<emails.size(); i++){
					if(emails.get(i)!=null && emails.get(i).indexOf("--->")>=0){
						// System.out.println(emails.get(i));
						parts=emails.get(i).split("\\--->");
						// Here we loop to assign
						if(parts.length>0){
							for(int t=0; t<parts.length; t++){
								mailData[i][t]=parts[t];
							}
						}
					}
				} // end of loop
			}
			
			// Here we call the CSV saver to log our csv to send directory
			// Now lets log Email Data to CSV
			if(mailData.length>0){
				// Here we filter email before logging
				newData = filterWiz(mailData, filter);
				if(newData != null){
					if(newData.length > 0){
						try {
							Manage.save(newData, "extract");
							EXTRACTEDMAIL=newData;
							trueValue=1;
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		// Here we return
		return trueValue;
	}
	
	
	
	/*** HERE WE START CONSTRUCTING THE PARSE FUNCTIONS * @throws MalformedURLException ***/
	public List<String> parse(String[] urls, Pattern pat) throws MalformedURLException{
		// HERE WE CONSTRUCT THE EMAIL PARSER PROCESS
		List<String> emailAddresses = new ArrayList<String>();
		String engine = new URL(Settings.get("engine")).getHost();
		boolean found=false;
		String[] contents={};
		// HERE WE START PROCESSING
		if(urls!=null && urls.length>0){
			contents=urlContent(urls); // extracting content
			if(contents!=null && contents.length>0){
				// Here we loop contents to get the emails
				for(int i=0; i<contents.length; i++){
					if(contents[i]!=null){
						//Matches contents against the given Email Address Pattern
						Matcher match = pat.matcher(contents[i]);
						found=match.find();
						//If match found, append to emailAddresses
						while(found) {
							emailAddresses.add(match.group()+"--->"+urls[i]+"--->"+engine); 
							found = match.find(); 
						}// end of while loop
					}
				}
			}
		}
		
		// Here we return
		return emailAddresses;
	}
	
	
	/********* HERE WE START CONSTRUCTING THE CONTENT EXTRACTION METHOD **********/
	private String[] urlContent(String[] urls){
		// HERE WE CONSTRUCT THE URL CONTENT RETURNER
		List<String> content = new ArrayList<String>();
		String[] contents={};
		String pageData = "";
		// HERE WE START PROCESSING THE CONTENT FETCHING
		if(urls!=null && urls.length>0){
			// NOW LETS START LOOPING
			for(int i=0; i<urls.length; i++){
				if(urls[i].startsWith("http")){
					TARGETONURL = urls[i]; // we set current url
					pageData = Util.getHtml(urls[i], Integer.parseInt(Settings.get("timeout")), Integer.parseInt(Settings.get("try")));
					if(pageData !=null){
						content.add(pageData);	
					}
				}
			} // end of loop
			
			// NOW LETS TURN LIST TO ARRAY
			contents = content.toArray(new String[content.size()]);
		} // end of check
		
		// Here we return
		return contents;
	}
	
	
	/****************** HERE WE CONSTRUCT THE FILTER WIZARD *********************/
	private String[][] filterWiz(String[][] data, FileManager.Filter filter){
		// METHOD TO IMPLEMENT FILTER FOR EMAIL
		SetBase setting = new SetBase();
		String[][] emailDatas = {};
		String[] mailAddress = {};
		String[] accepted = {};
		// NOW LETS START PROCESSING
		if(data!=null){
			if(data.length>0){
				// Now let get mail address
				mailAddress = Manage.getOneRows(data);
				// Now lets check mail address to proceed
				if(mailAddress != null && mailAddress.length>0){
					if(setting.get("filter").equals("yes")){
						accepted = filter.nameFilter(mailAddress);
						// Here we turn accepted to multi-dimensional array
						// Here let us prepare our mail datas
						if(accepted!=null && accepted.length>0){
							emailDatas = new String[accepted.length][3];
							for(int j=0; j<accepted.length; j++){
								for(int k=0; k<data.length; k++){
									if(accepted[j].equals(data[k][0].toLowerCase())){
										if(accepted[j].indexOf("@")>=0){
											emailDatas[j][0] = data[k][0];
											emailDatas[j][1] = data[k][1];
											emailDatas[j][2] = data[k][2];
											// Here we break
											break;
										}
										else{
											break;
										}
									}
								}
							}
						}
						else{
							// Setting filter setting is no
							emailDatas = data;
						}
					}
					else{
						// Setting filter setting is no
						emailDatas = data;
					}	
				}
			}
		}
		// Here we return
		return emailDatas;
	}
	
	// END OF CLASS
}
