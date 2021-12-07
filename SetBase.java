package system.soft.processor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import system.source.DjadeUtil;
import system.source.SetHandle;
import system.source.XmlReader;


public class SetBase {
	// HERE WE SET PUBLIC VARIABLES FOR CLASS
	private String EngineUrl;
	private String UserAgent;
	private String EngineMeta;
	private String Expression;
	private String MaximumSearch;
	private String Timeout;
	private String PageSize;
	private String SendDir;
	private String SentDir;
	private String SentLog;
	private String MaximumThread;
	private String SendRate;
	private String Host;
	private String Port;
	private String From;
	private String Username;
	private String Password;
	private String ServiceProtocol;
	private String TransferProtocol;
	private String Validate;
	private String ProcessTimeout;
	private String Template;
	private String MailDirectory;
	private String ExtractRate;
	private String Parse;
	private String Parsed;
	private String ParsedLog;
	private String Company;
	private String TemplateLog;
	private String Filter;
	private String Retry;
	private String ExtractionLog;
	private String EnableImport;
	private String EnableExport;
	//HashMap<String, Integer> Settings;
	private String FeederRate;
	private String FeederQuality;
	private String MidRate;
	private String AdvanceRate;
	private String AdvanceQuality;
	
	// HERE WE CONSTRUCT CLASS
	public SetBase(){};
	
	
	/*************** HERE WE SET METHOD THAT RETURN SETTINGS ******************/
	private void engineUrl(String arg){
		// Setting Engine
		EngineUrl=arg;
	}


	private void userAgent(String Agent){
		// Setting values
		UserAgent=Agent;
	}


	private void engineMeta(String meta){
		// Setting values
		EngineMeta=meta.replace("%ua", UserAgent);
	}


	private void maximumSearch(String args){
		// Setting values
		MaximumSearch=args;
	}
	
	
	private void timeout(String args){
		// Setting values
		Timeout=args;
	}
	
	
	private void ptimeout(String args){
		// Setting values
		ProcessTimeout=args;
	}
	
	
	private void validate(String args){
		// Setting values
		Validate=args;
	}
	
	
	private void template(String args){
		// Setting values
		Template=args;
	}
	
	
	private void pageSize(String args){
		// Setting values
		PageSize=args;
	}
	
	
	private void sendDir(String args){
		// Setting values
		SendDir=args;
	}
	
	
	private void sentDir(String args){
		// Setting values
		SentDir=args;
	}
	
	
	private void maxThread(String args){
		// Setting values
		MaximumThread=args;
	}
	
	
	private void sentLog(String args){
		// Setting values
		SentLog=args;
	}
	
	
	private void sendRate(String args){
		// Setting values
		SendRate=args;
	}
	
	
	private void host(String args){
		// Setting values
		Host=args;
	}
	
	
	private void from(String args){
		// Setting values
		From=args;
	}
	
	
	private void port(String args){
		// Setting values
		Port=args;
	}
	

	private void username(String args){
		// Setting values
		Username=args;
	}


	private void password(String num){
		// Setting values
		Password=num;
	}


	private void expression(String arg){
		// Setting values
		Expression=arg;
	}
	
	
	private void sp(String arg){
		// Setting values
		ServiceProtocol=arg;
	}
	
	
	private void tp(String arg){
		// Setting values
		TransferProtocol=arg;
	}
	
	
	private void mailDir(String arg){
		MailDirectory=arg;
	}
	
	
	private void extRate(String arg){
		ExtractRate=arg;
	}
	
	
	private void parse(String arg){
		Parse=arg;
	}
	
	
	private void parsed(String arg){
		Parsed=arg;
	}
	
	
	private void parsedLog(String arg){
		ParsedLog=arg;
	}
	
	
	private void company(String arg){
		Company=arg;
	}
	
	
	private void tmplLog(String arg){
		TemplateLog=arg;
	}
	
	
	private void filter(String arg){
		Filter=arg;
	}
	
	
	private void retry(String arg){
		Retry=arg;
	}
	
	
	private void extLog(String arg){
		ExtractionLog=arg;
	}
	
	
	private void eimport(String arg){
		EnableImport=arg;
	}
	
	
	private void eexport(String arg){
		EnableExport=arg;
	}


	private void rate(String args){
		// HERE WE PROCESS THE RATING TO SET ALL SORTS OF RATINGS
		String[] eachRate;
		
		// LETS START PROCESSING
		if(args!=null && args.length()>0){
			if(args.indexOf("->")>=0){
				eachRate=args.split("\\->");
				if(eachRate.length>=5){
					// Now lets set to appropriate variable
					FeederRate=eachRate[0];
					FeederQuality=eachRate[1];
					MidRate=eachRate[2];
					AdvanceRate=eachRate[3];
					AdvanceQuality=eachRate[4];
				} // End of rate array check
			} // End of string check
		} // End of arg check
	}
	
	
	/****************** HERE WE CONSTRUCT THE SET BASE SETTER METHOD ********************/
	private void setBase(){
		// LETS PROCESS THE SEARCH SETTING PROCEDURE
		SetHandle settings=new SetHandle();
		// Now lets set the settings
		engineUrl(settings.getSet("engine", "val-engine"));
		userAgent(settings.getSet("agent", "val-agent"));
		engineMeta(settings.getSet("meta", "val-meta"));
		maximumSearch(settings.getSet("num", "val-num"));
		timeout(settings.getSet("timeout", "val-timeout"));
		pageSize(settings.getSet("pageSize", "val-pageSize"));
		sendDir(settings.getSet("sendDir", "val-sendDir"));
		sentDir(settings.getSet("sentDir", "val-sentDir"));
		sentLog(settings.getSet("sentLog", "val-sentLog"));
		maxThread(settings.getSet("thread", "val-thread"));
		sendRate(settings.getSet("sendRate", "val-sendRate"));
		expression(settings.getSet("expression", "val-expression"));
		host(settings.getSet("mshost", "val-mshost"));
		from(settings.getSet("msfrom", "val-msfrom"));
		port(settings.getSet("msport", "val-msport"));
		username(settings.getSet("msuser", "val-msuser"));
		password(settings.getSet("mspass", "val-mspass"));
		rate(settings.getSet("rate", "val-rate"));
		sp(settings.getSet("sp", "val-sp"));
		tp(settings.getSet("tp", "val-tp"));
		ptimeout(settings.getSet("ptimeout", "val-ptimeout"));
		validate(settings.getSet("validate", "val-validate"));
		template(settings.getSet("template", "val-template"));
		mailDir(settings.getSet("mailDir", "val-mailDir"));
		extRate(settings.getSet("extRate", "val-extRate"));
		parse(settings.getSet("webParse", "val-webParse"));
		parsed(settings.getSet("webParsed", "val-webParsed"));
		parsedLog(settings.getSet("parsedLog", "val-parsedLog"));
		company(settings.getSet("company", "val-company"));
		tmplLog(settings.getSet("tmplLog", "val-tmplLog"));
		filter(settings.getSet("filter", "val-filter"));
		retry(settings.getSet("retry", "val-retry"));
		extLog(settings.getSet("extLog", "val-extLog"));
		eimport(settings.getSet("eimport", "val-eimport"));
		eexport(settings.getSet("eexport", "val-eexport"));
	}
	
	
	/***************** HERE WE CONSTRUCT THE SETTING GET METHOD ***********************/
	public String get(String setName){
		// HERE WE CONSTRUCT THE SETTING METHOD
		HashMap<String, String> setRows=new HashMap<String, String>();
		String returnedSetting="";
		setBase();
		// HERE WE GET SET REQUEST
		// LETS START PROCESSING RateCtrlMain
		setRows.put("engine", EngineUrl);
		setRows.put("agent", UserAgent);
		setRows.put("meta", EngineMeta);
		setRows.put("num", MaximumSearch);
		setRows.put("pageSize", PageSize);
		setRows.put("timeout", Timeout);
		setRows.put("fr", FeederRate);
		setRows.put("fq", FeederQuality);
		setRows.put("mr", MidRate);
		setRows.put("ar", AdvanceRate);
		setRows.put("aq", AdvanceQuality);
		setRows.put("sendDir", SendDir);
		setRows.put("sentDir", SentDir);
		setRows.put("sentLog", SentLog);
		setRows.put("maxThread", MaximumThread);
		setRows.put("sendRate", SendRate);
		setRows.put("expression", Expression);
		setRows.put("host", Host);
		setRows.put("from", From);
		setRows.put("port", Port);
		setRows.put("username", Username);
		setRows.put("password", Password);
		setRows.put("sp", ServiceProtocol);
		setRows.put("tp", TransferProtocol);
		setRows.put("validate", Validate);
		setRows.put("pt", ProcessTimeout);
		setRows.put("template", Template);
		setRows.put("mailDir", MailDirectory);
		setRows.put("extRate", ExtractRate);
		setRows.put("parseDir", Parse);
		setRows.put("parsedDir", Parsed);
		setRows.put("parsedLog", ParsedLog);
		setRows.put("company", Company);
		setRows.put("tmplLog", TemplateLog);
		setRows.put("filter", Filter);
		setRows.put("try", Retry);
		setRows.put("extLog", ExtractionLog);
		setRows.put("import", EnableImport);
		setRows.put("export", EnableExport);
		// LETS ASSIGN SET TO RETURNER
		if(!setRows.isEmpty()){
			if(setName!=null && setRows.get(setName).length()>0){
				returnedSetting=setRows.get(setName);
			}
		}
		// Here we return
		return returnedSetting;
	}
	
	
	/******************* CONSTRUCTING METHOD TO GET SET DESCRIPTION ********************/
	public String getDesc(String setName){
		// LETS PROCESS THE SEARCH SETTING PROCEDURE
		String description="";
		SetHandle settings=new SetHandle();
		// HERE WE START PROCESSING
		if(setName!=null && setName.length()>0){
			description=settings.getSet(setName, setName);
		}
		
		// Here we return description
		return description;
	}
	
	
	/****** HERE WE CONSTRUCT THE SETTING SET METHOD * @throws URISyntaxException ******/
	public int set(String name, String value) throws URISyntaxException{
		// HERE WE START PROCESSING THE SET PROCESSES
		int trueValue=0;
		SetHandle settings;
		String syntax="";
		int id=0;
		// HERE WE START PROCESSING
		if(name!=null && value!=null){
			id=id(name);
			syntax="setting->"+name+"("+id+")~val-"+name+":"+value;
			settings=new SetHandle(syntax, "x");
			trueValue=settings.writeCheck();
		}
		// Here we return true or false
		return trueValue;
	}
	
	
	public int setAll(String[] names, String[] values) throws URISyntaxException{
		// HERE WE START PROCESSING THE SET PROCESSES
		int trueValue=0;
		SetHandle settings;
		String syntax="";
		int id=0;
		// HERE WE START PROCESSING
		if(names!=null && values!=null){
			if(names.length==values.length){
				for(int i=0; i<names.length; i++){
					id=id(names[i]);
					if(syntax.length()>0){
						syntax+=";"+"setting->"+names[i]+"("+id+")~val-"+names[i]+":"+values[i];
					}
					else{
						syntax+="setting->"+names[i]+"("+id+")~val-"+names[i]+":"+values[i];
					}
				} // end of loop
				
				// Now lets run the syntax
				settings=new SetHandle(syntax, "x");
				trueValue=settings.writeCheck();
			}
		}
		// Here we return int
		return trueValue;
	}
	
	
	
	/***** HERE WE CONSTRUCT THE ID METHOD TO GET ID * @throws URISyntaxException *****/
	public int id(String fieldName) throws URISyntaxException{
		// HERE WE START CONSTRUCTING THE ID GETTER PROCESS
		Iterator<Map.Entry<String, String[]>> mappedHarvest;
		String setFile="data/setting/config.set";
		String parentName="setting";
		int fieldID=0;
		XmlReader read;
		DjadeUtil util = new DjadeUtil();
		// HERE WE START PROCESSING
		if(fieldName!=null){
			if(fieldName.length()>0){
				// Setting document values
				try {
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			        DocumentBuilder dBuilder;
					dBuilder = dbFactory.newDocumentBuilder();
					// LETS GET THE DOCUMENT WE ARE TO READ
					try {
						Document doc;
						doc = dBuilder.parse(util.antResource(setFile));
						doc.getDocumentElement().normalize();
						// Now lets get syntax
						read=new XmlReader(setFile);
						mappedHarvest=read.xmlHarvest(parentName).entrySet().iterator();
						while(mappedHarvest.hasNext()){
							Entry<String, String[]> next=mappedHarvest.next();
							if(fieldName.equals(next.getKey())){
								for(String attr:next.getValue()){
									// Here we get element attribute
									NodeList pList=doc.getElementsByTagName(fieldName);
									Node pElement=pList.item(0);
									Element element=(Element) pElement;
									// Now lets get id and break
									fieldID=Integer.parseInt(element.getAttribute(attr));
									break;
								}
								
								// Here we break
								break;
							}
						}
						
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} // end of check
		}
		// Here we return id
		return fieldID;
	}
	
	// END OF CLASS
}
