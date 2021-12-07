package system.source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import system.soft.processor.SetBase;

public class FeederBot {
// INITIATING INSTANCE VARIABLES
private String Terms;
private String[] Rows;
private String ReturnType="url";
private SetBase SETDATA;
/************************** LETS CONSTRUCTING THE CLASS *********************************/
public FeederBot(String searchTerm, SetBase setRows, String returnType){
	// Now lets set values
	Terms=searchTerm;
	SETDATA=setRows;
	// Calling the search method
	search(returnType);
}




/************************ SETTING THE MAIN METHOD FOR TEST *********************************
public static void main(String[] args){
	// NOW LETS TRY TO RUN CLASS
	String Terms="Java Tutorials full pdf";
	String engineUrl="https://www.google.com.ng/search?q=";
	String userAgent="Mozilla/5.0(compactible;Googlebot/2.1;+http://www.google.com/bot.html)";
	String engineMeta="title:h3.r>a(text)-url:h3.r>a(attr~href)-desc:span[class=st](text),"+userAgent+",+";
	// Now lets instantiate class
	FeederBot bot=new FeederBot(Terms, engineUrl, engineMeta, 20);
	Lets loop to display
	for(String rows:bot.getRows()){
	System.out.println(rows);
	 }
}
****************************/


/************************* CONSTRUCTING THE SEARCH METHOD *******************************/
private void search(String returnType){
	// NOW LET PROCESS THE SEARCH AND DEPLOY CRAWLER
	String[][] searchResults=crawler();
	String[] Websites;
	// Lets set the return type
	if(returnType!=null){
		ReturnType=returnType;
	}
	
	// Now lets check result
	if(searchResults.length>0){
		Websites=keySearch(searchResults);
		// Now lets check again
		if(Websites.length>0){
			Rows=Websites;
		}
	}
}



/************************* CONSTRUCTING THE CRAWLER METHOD ******************************/
private String[][] crawler(){
	// NOW LETS START PROCESSING THE CRAWLING MECHANISM
	String[][] contents={};
	String[] metas;
	String[] selectors;
	String selector="";
	String userAgent="";
	String kbSymbol="";
	// Setting Jsoup Variables
	Document doc;
	String QueryPaths="";
	int key=0;
	
	// NOW LETS START THE STRING ON TERMS
	if(SETDATA.get("meta")!=null){ // Meta data must be set
		// NOW LETS PROCESS THE META DATA
		metas=SETDATA.get("meta").split(",");
		selector=metas[0];
		userAgent=metas[1];
		kbSymbol=metas[2];
		
		// NOW LETS PROCESS SELECTORS
		selectors=selector.split("-");
		
		/************** NOW LETS START PROCESSING JSOUP ***************/
		// Preparing url
		QueryPaths=Terms.replaceAll(" ", kbSymbol)+"&num="+SETDATA.get("num");
		// Lets try connection and processing
		try {
			// Now lets connect and retrieve data
			doc=Jsoup.connect(SETDATA.get("engine")+QueryPaths).timeout(Integer.parseInt(SETDATA.get("timeout"))).ignoreHttpErrors(true).userAgent(userAgent).get();
			// Now lets get the document parts
			Elements results=doc.select("div[class=g]");
			// Lets set the contents array size
			contents=new String[results.size()][3];
			// Setting in variables
			String[] metaDirectives;
			String[] Title;
			String[] Url;
			String[] Desc;
			String[] urlParts;
			// NOW LETS LOOP TO GET INDIVIDUAL SEARCH
			for(Element result:results){
				// PROCESSING THE META DIRECTIVES
				for(int i=0; i<selectors.length; i++){
					// Now lets get directives
					metaDirectives=selectors[i].split("\\:");
					// Now lets check selector type
					if(metaDirectives[0].equals("title")){
						// We are processing title selector
						Title=metaDirectives[1].split("\\(");
						// Now lets check the fetch directive
						if(Title[1].substring(0, Title[1].length()-1).equals("text")){
							// Lets set the content for title
							contents[key][0]=result.select(Title[0]).text();
						}
					}
					else if(metaDirectives[0].equals("url")){
						// We are processing title selector
						Url=metaDirectives[1].split("\\(");
						// Now lets check the fetch directive
						if(Url[1].indexOf("attr")>=0){
							urlParts=Url[1].split("\\~");
							contents[key][1]=result.select(Url[0]).attr(urlParts[1].substring(0, urlParts[1].length()-1));
						}
					}
					else if(metaDirectives[0].equals("desc")){
						// We are processing Desc selector
						Desc=metaDirectives[1].split("\\(");
						// Now lets check the fetch directive
						if(Desc[1].substring(0, Desc[1].length()-1).equals("text")){
							// Lets set the content for title
							contents[key][2]=result.select(Desc[0]).text();
						}
					}
				} // End of static loop
				
				// Increment key
				key++;
			} // End of loop
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	else{
		// We set the contents to null
		contents=null;
	}
	
	return contents;
}



/************************* CONSTRUCTING THE KEYSEARCH METHOD ****************************/
private String[] keySearch(String[][] searchResult){
	// LETS START PROCESSING THE KEY SEARCH PROCESSES
	String[] contents=new String[searchResult.length];
	String[] Titles=new String[searchResult.length];
	String[] Urls=new String[searchResult.length];
	String[] Descs=new String[searchResult.length];
	String[] keyWords=Terms.split(" ");
	KeywordHandle handle;
	DjadeUtil util=new DjadeUtil();
	/****** Setting the relevance rate has to come from set file
	** But lets set any default value to work with
	******/
	int Rate=Integer.parseInt(SETDATA.get("fr"));
	// Setting other static variables
	String descHits="";
	String titleHits="";
	int[][] rTitles;
	int[][] rDescs;
	int[] thitMax={};
	int[] dhitMax={};
	int titleMax=0;
	int descMax=0;
	
	// HERE WE START MAJOR PROCESSES
	for(int i=0; i<searchResult.length; i++){
		// NOW LETS SEARCH INDIVIDUAL DATA BASED ON DESC AND TITLE TO GET RELENACE
		Titles[i]=searchResult[i][0];
		Urls[i]=searchResult[i][1].substring(7, searchResult[i][1].indexOf("&"));
		Descs[i]=searchResult[i][2];
	} // End of loop
	
	/************** NOW LETS START SEARCHING FOR KEYWORDS ****************/
	// TITLE KEYWORD SEARCHES
	handle=new KeywordHandle(keyWords, Titles, "ih", Rate);
	titleHits=handle.scoreHits();
	
	// DESC KEYWORD SEARCHES
	handle=new KeywordHandle(keyWords, Descs, "ih", Rate);
	descHits=handle.scoreHits();
	
	
	/********* What we do to select the most relevant search based on the
	 * Keyword search on both the title and description is to find the id
	 * that occurred in both search and select first then select all from
	 * the title search 
	 */
	rTitles=util.hitsProcessor(titleHits);
	rDescs=util.hitsProcessor(descHits);
	
	// NOW LETS LOOP INDEVIDUALS HITS TO GET ALL SUM HITS
	thitMax=new int[rTitles.length];
	for(int j=0; j<rTitles.length; j++){
		thitMax[j]=rTitles[j][1];
	} // End of loop
	
	dhitMax=new int[rDescs.length];
	for(int k=0; k<rDescs.length; k++){
		dhitMax[k]=rDescs[k][1];
	} // End of loop
	
	// NOW LETS START SELECTING BASED ON PERCENTAGES
	titleMax=util.max(thitMax);
	descMax=util.max(dhitMax);
	
	/*************** LETS GET INDIVIDUAL RELEVANCE RATE *******************/
	for(int f=0; f<thitMax.length; f++){ // Make a check
		if(((thitMax[f]*100)/titleMax)>=Integer.parseInt(SETDATA.get("fq"))){
			// Now lets add to the index
			if(contents.length>0){
				// We first have to check if url already exists
				if(!Arrays.asList(contents).contains(Urls[rTitles[f][0]])){
					// Here we set value
					if(ReturnType.equals("url")){
						contents[f]=Urls[rTitles[f][0]];
					}
					else if(ReturnType.equals("desc")){
						contents[f]="("+Descs[rTitles[f][0]]+"-->>"+Titles[rTitles[f][0]]+"-->>"+Urls[rTitles[f][0]]+"-->>"+thitMax[f]+")";
					}
					else{
						contents[f]=Titles[rTitles[f][0]];
					}
				}
			}
			else{
				// Here we set value
				if(ReturnType.equals("url")){
					contents[f]=Urls[rTitles[f][0]];
				}
				else if(ReturnType.equals("desc")){
					contents[f]="("+Descs[rTitles[f][0]]+"-->>"+Titles[rTitles[f][0]]+"-->>"+Urls[rTitles[f][0]]+"-->>"+thitMax[f]+")";
				}
				else{
					contents[f]=Titles[rTitles[f][0]];
				}
			}
		}
	} // End of loop
	
	// HANDLING FOR DESCRIPTION
	for(int e=0; e<dhitMax.length; e++){ // Make a check
		if(((dhitMax[e]*100)/descMax)>=Integer.parseInt(SETDATA.get("mr"))){
			// Now lets add to the index
			if(contents.length>0){
				// We first have to check if url already exists
				if(!Arrays.asList(contents).contains(Urls[rDescs[e][0]])){
					// Here we set value
					if(ReturnType.equals("url")){
						contents[e]=Urls[rDescs[e][0]];
					}
					else if(ReturnType.equals("desc")){
						contents[e]="("+Descs[rDescs[e][0]]+"-->>"+Titles[rDescs[e][0]]+"-->>"+Urls[rDescs[e][0]]+"-->>"+dhitMax[e]+")";
					}
					else{
						contents[e]=Titles[rDescs[e][0]];
					}
				}
			}
			else{
				// Here we set value
				if(ReturnType.equals("url")){
					contents[e]=Urls[rDescs[e][0]];
				}
				else if(ReturnType.equals("desc")){
					contents[e]="("+Descs[rDescs[e][0]]+"-->>"+Titles[rDescs[e][0]]+"-->>"+Urls[rDescs[e][0]]+"-->>"+dhitMax[e]+")";
				}
				else{
					contents[e]=Titles[rDescs[e][0]];
				}
			}
		}
	} // End of loop
	
	// NOW LETS REMOVE ANY NULL IN THE ARRAY
	// HERE WE TRY TO COMPOSE ARRAY TO REMOVE NULL
	List<String> list=new ArrayList<String>();
	for(String s:contents){
		if(s!=null && s.length()>0){
			list.add(s);
		}
	} // End of loop
			
	// Now we change data of array
	contents=list.toArray(new String[list.size()]);
	
	// NOW LETS RETURN
	return contents;
}




/************************* CONSTRUCTING THE GET ROWS METHOD ******************************/
public String[] getRows(){
	// LETS RETURN ROWS
	return Rows;
}

// END OF CLASS
}
