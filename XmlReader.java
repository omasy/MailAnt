package system.source;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class XmlReader {
// SETTING INSTANCE VARIABLES
private String Syntax;
private String FilePath;
private String[] RecordSet;
DjadeUtil util=new DjadeUtil();
/****************************** HERE WE CONSTRUCT CLASS ********************************/
public XmlReader(String syntax, String xmlFile, String process){
	// Setting values
	Syntax=syntax;
	FilePath=xmlFile;
	// HERE THE RETRIEVAL LOGIC WILL BE CALLED
	if(process!=null && process.equals("rawMode")){
		try {
			embedderLogic();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	else{
		try {
			retrievalLogic();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


public XmlReader(String xmlFile){
	// Setting values
	FilePath=xmlFile;
}



/********** LETS CONSTRUCT MAIN METHOD TO TEST * @throws URISyntaxException **************/
public static void main(String[] args) throws URISyntaxException{
	// NOW LETS SET VARIABLES
	String syntax="status@val;respond~from@val,position@val;date@val";
	String xmlFile="cores/core/testData/705813151.xml";
	String xmlFile2="cores/core/log/Query/engine/engineQuery.xml";
	String process="rawMode";
	// Now lets call class
	XmlReader reader=new XmlReader(syntax, xmlFile, process);
	XmlReader reader2=new XmlReader(xmlFile2);
	// Now lets display
	HashMap<String, HashMap<String, String>> rows=reader.emberFetch();
	Iterator<Map.Entry<String, String[]>> mappedHarvest = reader2.xmlHarvest("engine-query").entrySet().iterator();
	System.out.println(rows.get("respond").get("from"));
	System.out.println("Generated Syntax: "+reader2.syntaxBuilder("engine-query"));
	
	while(mappedHarvest.hasNext()){
		System.out.println(mappedHarvest.next());
	}
	// System.out.println("Harvested Xml: "+reader2.xmlHarvest("engine-query").size()+"--"+reader2.xmlHarvest("engine-query"));
	// System.out.println("Harvested Xml: "+Arrays.toString(reader2.xmlHarvest("engine-query").get("similar-synonyms")));
	// HashMap<String, String> fetch=reader.fetch();
	// System.out.println(fetch.get("from"));
	// System.out.println(reader.get("status"));
	// System.out.println(Arrays.toString(reader.rawData()));
}



/******** CONSTRUCTING THE SYNTAX RETRIEVEAL LOGIC * @throws URISyntaxException **********/
private void retrievalLogic() throws URISyntaxException{
	// NOW LETS START PROCESSING THE RETRIEVAL LOGIC TERMINOLOGY
	String[] returnData={};
	String[] syntaxDatas={};
	String[] SyntaxPaths;
	String[] refTagging;
	String attr="";
	String parentTag="";
	String childTag="";
	boolean tagRef=false;
	boolean containVal=false;
	
	// NOW LETS START PROCESSING
	syntaxDatas=util.syntaxInterpreter(Syntax);
	// LETS CHECK THE CONTENTS OF THE SYNTAX DATA
	if(syntaxDatas.length>0){
		// NOW WE CAN GET EACH INDIVIDUAL VALUES OF XML USING THE DOM PARSER
		returnData=new String[syntaxDatas.length];
		// Now lets try parsing documents
		try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
		dBuilder = dbFactory.newDocumentBuilder();
        try {
        	// LETS GET THE DOCUMENT WE ARE TO READ
			Document doc = dBuilder.parse(util.antResource(FilePath));
			doc.getDocumentElement().normalize();
			// NOW LETS GET INDIVIDUAL ELEMNTS AND STORE WITH THERE VALUES AND NAMES
			for(int i=0; i<syntaxDatas.length; i++){
				// CHECKING IF TAG REFRENCE ANOTHER AND SETTING A FEED
				if(syntaxDatas[i].indexOf("->")>=0){
					// Syntax contains tag reference
					tagRef=true;
				}
				
				// Now lets loop through individual syntax and get values
				if(syntaxDatas[i].indexOf("~")>=0){
					// HERE TAG NAME IS SPECIFIED AND IT CONTAINS ATTRIBUTE
					SyntaxPaths=syntaxDatas[i].split("\\~");
					// Here we select tag name so we check
					if(tagRef==true){
						// We set the parent and child tag
						refTagging=SyntaxPaths[0].split("\\->");
						parentTag=refTagging[0];
						// We check if child tag has value
						if(refTagging[1].indexOf("@val")>=0){
							childTag=refTagging[1].substring(0, SyntaxPaths[0].indexOf("@val"));
							containVal=true;
						}
						else{
							childTag=refTagging[1];
							containVal=false;
						}
					}
					else{
						// Here we just set child tag
						if(SyntaxPaths[0].indexOf("@val")>=0){
							childTag=SyntaxPaths[0].substring(0, SyntaxPaths[0].indexOf("@val"));
							containVal=true;
						}
						else{
							childTag=SyntaxPaths[0];
							containVal=false;
						}
					}
					
					// Setting the attribute part
					attr=SyntaxPaths[1];
					// NOW LETS GET THE ELEMENT BY NODE NAME
					NodeList nList=null;
					if(tagRef==false){
						nList = doc.getElementsByTagName(childTag);
					}
					else{
						NodeList pList=doc.getElementsByTagName(parentTag);
						Node pElement=pList.item(0);
						Element elem=(Element) pElement;
						nList=elem.getElementsByTagName(childTag);
					}
					
					// LETS LOOP ALL NODES
					Node element=nList.item(0);
					Element eElement = (Element) element;
					// Now lets get attribute value
					if(attr.indexOf("@val")>=0){
						// Here we are getting attributes with values only
						if(containVal==true){
							returnData[i]=attr.substring(0, attr.indexOf("@val"))+"-->"+eElement.getAttribute(attr.substring(0, attr.indexOf("@val")));
						}
					}
					
				}
				else{
					// TAG LINE DOES NOT CONTAIN ATTRIBUTE
					if(syntaxDatas[i].indexOf("@val")>=0){
						// Here we are getting tags with values only
						// Here we select tag name so we check
						if(tagRef==true){
							// We set the parent and child tag
							refTagging=syntaxDatas[i].split("\\->");
							parentTag=refTagging[0];
							// We check if child tag has value
							if(refTagging[1].indexOf("@val")>=0){
								childTag=refTagging[1].substring(0, syntaxDatas[i].indexOf("@val"));
							}
							else{
								childTag=refTagging[1];
							}
						}
						else{
							// Here we just set child tag
							if(syntaxDatas[i].indexOf("@val")>=0){
								childTag=syntaxDatas[i].substring(0, syntaxDatas[i].indexOf("@val"));
							}
							else{
								childTag=syntaxDatas[i];
							}
						}
						
						// Setting the attribute part
						NodeList nList=null;
						if(tagRef==false){
							nList = doc.getElementsByTagName(childTag);
						}
						else{
							NodeList pList=doc.getElementsByTagName(parentTag);
							Node pElement=pList.item(0);
							Element elem=(Element) pElement;
							nList=elem.getElementsByTagName(childTag);
						}
						
						// LETS LOOP ALL NODES
						Node element=nList.item(0);
						Element eElement = (Element) element;
						// Now lets set values
						returnData[i]=syntaxDatas[i].substring(0, syntaxDatas[i].indexOf("@val"))+"-->"+eElement.getTextContent();
					}
				}
			} // End of loop
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		// HERE WE MAKE SURE WE DONT RETURN NULL VALUES
		if(returnData.length>0){
		// NOW LETS REMOVE ANY NULL IN THE ARRAY
		// HERE WE TRY TO COMPOSE ARRAY TO REMOVE NULL
		List<String> list=new ArrayList<String>();
		for(String s:returnData){
			if(s!=null && s.length()>0){
				list.add(s);
			}
		} // End of loop
				
		// Now we change data of array
		returnData=list.toArray(new String[list.size()]);
		// NOW LETS SET THE RECORD SET
		RecordSet=returnData;
		}
		else{
			// WE SET THE RECORDSET TO NULL
			RecordSet=null;
		}
		
	} // End of content check

	// End of method
}



/*********** CONSTRUCTING THE EMBEDDERlOGIC * @throws URISyntaxException ****************/
private void embedderLogic() throws URISyntaxException{
	// NOW LETS START CONSTRUCTING THE EMBEDDED LOGIC TERMINOLOGY
	String[] returnData={};
	String[] syntaxEach;
	String[] SyntaxPaths;
	String[] attrArray;
	String[] refTagging;
	String datas="";
	String parentTag="";
	String childTag="";
	String attr="";
	boolean tagRef=false;
	boolean containVal=false;
	
	// NOW LETS START PROCESSING
	if(Syntax!=null && Syntax.length()>0){
		syntaxEach=Syntax.split("\\;");
		// Now lets loop each syntax to get values
		if(syntaxEach.length>0){
			// NOW WE CAN GET EACH INDIVIDUAL VALUES OF XML USING THE DOM PARSER
			returnData=new String[syntaxEach.length];
			// SETTING XML INSTANCES
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder;
				dBuilder = dbFactory.newDocumentBuilder();
				
				// LETS GET THE DOCUMENT WE ARE TO READ
				try {
					Document doc;
					doc = dBuilder.parse(util.antResource(FilePath));
					doc.getDocumentElement().normalize();
					// NOW LETS LOOP EACH SYNTAX
					for(int i=0; i<syntaxEach.length; i++){
						// CHECKING IF TAG REFRENCE ANOTHER AND SETTING A FEED
						if(syntaxEach[i].indexOf("->")>=0){
							// Syntax contains tag reference
							tagRef=true;
						}
						
						// Now lets loop through individual syntax and get values
						if(syntaxEach[i].indexOf("~")>=0){
							// HERE TAG NAME IS SPECIFIED AND IT CONTAINS ATTRIBUTE @val
							SyntaxPaths=syntaxEach[i].split("\\~");
							// Here we select tag name so we check
							if(tagRef==true){
								// We set the parent and child tag
								refTagging=SyntaxPaths[0].split("\\->");
								parentTag=refTagging[0];
								// We check if child tag has value
								if(refTagging[1].indexOf("@val")>=0){
									childTag=refTagging[1].substring(0, SyntaxPaths[0].indexOf("@val"));
									containVal=true;
								}
								else{
									childTag=refTagging[1];
									containVal=false;
								}
							}
							else{
								// Here we just set child tag
								if(SyntaxPaths[0].indexOf("@val")>=0){
									childTag=SyntaxPaths[0].substring(0, SyntaxPaths[0].indexOf("@val"));
									containVal=true;
								}
								else{
									childTag=SyntaxPaths[0];
									containVal=false;
								}
							}
							
							// Setting the attribute part
							attr=SyntaxPaths[1];
							// Lets check contents of attributes
							if(attr.indexOf(",")>=0){
								// HERE WE HAVE MULTIPLE ATTRIBUTE
								attrArray=attr.split(",");
								// Now lets loop to find individual attribute
								for(int j=0; j<attrArray.length; j++){
									// NOW LETS GET EACH ATTRIBUTES
									// NOW LETS GET THE ELEMENT BY NODE NAME
									NodeList nList=null;
									if(tagRef==false){
										nList = doc.getElementsByTagName(childTag);
									}
									else{
										NodeList pList=doc.getElementsByTagName(parentTag);
										Node pElement=pList.item(0);
										Element elem=(Element) pElement;
										nList=elem.getElementsByTagName(childTag);
									}
					
									Node element=nList.item(0);
									Element eElement = (Element) element;
									// Now lets get attribute value
									if(attrArray[j].indexOf("@val")>=0){
										// Here we are getting attributes with values only
										String tagValue=element.getTextContent();
										String attrName=attrArray[j].substring(0, attrArray[j].indexOf("@val"));
										// NOW LETS GET EACH ATTRIBUTE AND STORE
										if(datas.length()>0){
											// Set a connector
											datas+="<dje>"+attrName+"=="+eElement.getAttribute(attrName);
										}
										else{
											// Set with no connector
											datas+=attrName+"=="+eElement.getAttribute(attrName);
										}
										// NOW LETS STORE TO OUR ARRAY
										// WE CHECK IF TAG VALUE IS RETURNED
										// System.out.println(hasChildElements(eElement));
										if(containVal==true){
											returnData[i]=childTag+"=="+tagValue+"-->"+datas;
										}
										else{
											returnData[i]=childTag+"-->"+datas;
										}
									}
								} // End of loop
							}
							else{
								// WE HAVE SINGLE ATTRIBUTE
								// NOW LETS GET THE ELEMENT BY NODE NAME
								NodeList nList=null;
								if(tagRef==false){
									nList = doc.getElementsByTagName(childTag);
								}
								else{
									NodeList pList=doc.getElementsByTagName(parentTag);
									Node pElement=pList.item(0);
									Element elem=(Element) pElement;
									nList=elem.getElementsByTagName(childTag);
								}
								
								// LETS GET ELEMENT DATAS
								Node element=nList.item(0);
								Element eElement = (Element) element;
								// Now lets get attribute value
								if(attr.indexOf("@val")>=0){
									// Here we are getting attributes with values only
									// Here we are getting attributes with values only
									String tagValue=element.getTextContent();
									String attrName=attr.substring(0, attr.indexOf("@val"));
									// WE CHECK IF TAG VALUE IS RETURNED
									if(containVal==true){
										returnData[i]=childTag+"=="+tagValue+"-->"+attrName+"=="+eElement.getAttribute(attrName);
									}
									else{
										returnData[i]=childTag+"-->"+attrName+"=="+eElement.getAttribute(attrName);
									}
								}
							}
						}
						else{
							// HERE TAG NAME IS SPECIFIED AND IT CONATINAS NO ATTRIBUTE
							if(syntaxEach[i].indexOf("@val")>=0){
								// Here we are getting tags with values only
								// Here we select tag name so we check
								if(tagRef==true){
									// We set the parent and child tag
									refTagging=syntaxEach[i].split("\\->");
									parentTag=refTagging[0];
									// We check if child tag has value
									if(refTagging[1].indexOf("@val")>=0){
										childTag=refTagging[1].substring(0, syntaxEach[i].indexOf("@val"));
									}
									else{
										childTag=refTagging[1];
									}
								}
								else{
									// Here we just set child tag
									childTag=syntaxEach[i].substring(0, syntaxEach[i].indexOf("@val"));
								}
								
								// Now lets set our elements
								NodeList nList=null;
								if(tagRef==false){
									nList = doc.getElementsByTagName(childTag);
								}
								else{
									NodeList pList=doc.getElementsByTagName(parentTag);
									Node pElement=pList.item(0);
									Element elem=(Element) pElement;
									nList=elem.getElementsByTagName(childTag);
								}
								
								// LETS LOOP ALL NODES
								Node element=nList.item(0);
								Element eElement = (Element) element;
								// Now lets set values
								if(!hasChildElements(eElement)){
									returnData[i]=childTag+"=="+eElement.getTextContent();
								}
								else{
									returnData[i]=childTag+"==null";
								}
							}
						}
					} // End of loop
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
		} // End of each check
		
		// HERE WE MAKE SURE WE DONT RETURN NULL VALUES
		if(returnData.length>0){
			// NOW LETS REMOVE ANY NULL IN THE ARRAY
			// HERE WE TRY TO COMPOSE ARRAY TO REMOVE NULL
			List<String> list=new ArrayList<String>();
			for(String s:returnData){
				if(s!=null && s.length()>0){
					list.add(s);
				}
			} // End of loop
						
			// Now we change data of array
			returnData=list.toArray(new String[list.size()]);
			// NOW LETS SET THE RECORD SET
			RecordSet=returnData;
		}
		else{
			// WE SET THE RECORDSET TO NULL
			RecordSet=null;
		}
				
	} // End of syntax check
	
	// End of method
}



/*********************** CONSTRUCTING THE GET METHOD *************************************/
public String get(String name){
	// NOW LETS CONSTRUCT THE GET METHOD TERMINOLOGY
	String returnValue="";
	String[] recordParts;
	// LETS START PROCESSING
	if(RecordSet!=null && RecordSet.length>0){
		// NOW WE LOOP TO PROCESS INDIVIDUAL RECORD
		for(int i=0; i<RecordSet.length; i++){
			// Now lets get parts of each record as all has
			recordParts=RecordSet[i].split("\\-->");
			if(recordParts.length>0){
				// Now lets check if name matches the particular record
				if(recordParts[0].equals(name) || recordParts[1].equals(name)){
					// We set return value and break
					returnValue=recordParts[1];
					break;
				}
			} // End of part check
		} // End of loop
	} // End of record set check
	
	// NOW LETS RETURN VALUE
	return returnValue;
}



/********************** CONSTRUCTING THE FETCH METHOD ************************************/
public HashMap<String, String> fetch(){
	// LETS CONSTRUCT THE FETCH METHOD TERMINOLOGY
	HashMap<String, String> Map=new HashMap<String, String>();
	String[] recordParts;
	
	// LETS START PROCESSING
		if(RecordSet!=null && RecordSet.length>0){
			// NOW WE LOOP TO PROCESS INDIVIDUAL RECORD
			for(int i=0; i<RecordSet.length; i++){
				// Now lets get parts of each record as all has
				recordParts=RecordSet[i].split("\\-->");
				if(recordParts.length>0){
					// Now lets check if name matches the particular record
					// Lets add map contents
					Map.put(recordParts[0], recordParts[1]);
				} // End of part check
			} // End of loop
		} // End of record set check
	
	// NOW LETS RETURN OUR RECORDS GOTTEN	
	return Map;
}




/********************** CONSTRUCTING THE EMBERMAP METHOD **********************************/
public HashMap<String, HashMap<String, String>> emberFetch(){
	// LETS CONSTRUCT THE FETCH METHOD TERMINOLOGY
	HashMap<String, HashMap<String, String>> Map=new HashMap<String, HashMap<String, String>>();
	HashMap<String, String> inMap=new HashMap<String, String>();
	String[] recordParts;
	String[] tagParts;
	String[] attrParts;
	String[] attrEach;
	String tagName="";
	String tagValue="";
	
	// LETS START PROCESSING
	if(RecordSet!=null && RecordSet.length>0){
		// NOW WE LOOP TO PROCESS INDIVIDUAL RECORD
		// System.out.println(Arrays.toString(RecordSet));
		for(int i=0; i<RecordSet.length; i++){
			// Now lets get parts of each record as all has
			if(RecordSet[i].indexOf("-->")>=0){
				recordParts=RecordSet[i].split("\\-->");
				if(recordParts.length>0){
					// Now lets check if name matches the particular record
					// Lets check if record contain values
					if(recordParts[0].indexOf("==")>=0){
						tagParts=recordParts[0].split("\\==");
						tagName=tagParts[0];
						tagValue=tagParts[1];
						// Lets add map contents
						Map.put(tagName, inMap);
						Map.get(tagName).put(tagName, tagValue);
						// NOW LETS GET OUR ATTRIBUTES AND VALES
						if(recordParts[1].indexOf("<dje>")>=0){
							// HERE WE HAVE MULTIPLE ATTRIBUTES
							attrParts=recordParts[1].split("<dje>");
							// Now lets loop
							for(int j=0; j<attrParts.length; j++){
								attrEach=attrParts[j].split("\\==");
								// Now lets log it to our map
								Map.get(tagName).put(attrEach[0], attrEach[1]);
							} // End of loop
						}
						else{
							// HERE WE HAVE SINGLE ATTRIBUTE
							attrEach=recordParts[1].split("\\==");
							// System.out.println(tagName+": "+attrEach[0]+"="+attrEach[1]);
							// Now lets log it to our map
							Map.get(tagName).put(attrEach[0], attrEach[1]);
						}
					} // End of value check
					else{
						// HERE WE KNOW TAG CONTAINS NO VALUE
						tagName=recordParts[0];
						// Lets add map contents
						Map.put(tagName, inMap);
						Map.get(tagName).put(tagName, "noval");
						// NOW LETS GET OUR ATTRIBUTES AND VALES
						if(recordParts[1].indexOf("<dje>")>=0){
							// HERE WE HAVE MULTIPLE ATTRIBUTES
							attrParts=recordParts[1].split("<dje>");
							// Now lets loop
							for(int j=0; j<attrParts.length; j++){
								attrEach=attrParts[j].split("\\==");
								// Now lets log it to our map
								Map.get(tagName).put(attrEach[0], attrEach[1]);
							} // End of loop
						}
						else{
							// HERE WE HAVE SINGLE ATTRIBUTE
							attrEach=recordParts[1].split("\\==");
							// Now lets log it to our map
							Map.get(tagName).put(attrEach[0], attrEach[1]);
						}
					}
				}
			} // End of part check
			else{
				// HERE DATA DONT CONTAIN ATTRIBUTE ALONG
				// Now lets check if name matches the particular record
				// Lets check if record contain values
				if(RecordSet[i].indexOf("==")>=0){
					tagParts=RecordSet[i].split("\\==");
					tagName=tagParts[0];
					tagValue=tagParts[1];
					// Lets add map contents
					Map.put(tagName, inMap);
					Map.get(tagName).put(tagName, tagValue);
				} // End of value check
				else{
					// RECORD DATA DONT CONTAIN ANY VALUE
					tagName=RecordSet[i];
					// Lets add map contents
					Map.put(tagName, inMap);
					Map.get(tagName).put(tagName, "noval");
				}
			}
		} // End of loop
	} // End of record set check
		
	// NOW LETS RETURN OUR RECORDS GOTTEN	
	return Map;
}



/************************ CONSTRUCTING THE RAW OUTPUT METHOD ******************************/
public String[] rawData(){
	// LETS RETURN THE RAW OUTPUT
	return RecordSet;
}



/********** CONSTRUCTING THE HARVESTER METHOD * @throws URISyntaxException **************/
public HashMap<String, String[]> xmlHarvest(String base) throws URISyntaxException{
	// LETS START CONSTRUCTING THE XMLHARVEST TERMINOLOGY
	HashMap<String, String[]> harvestMap=new HashMap<String, String[]>();
	String[] harvested={};
	String[] harvestData={};
	
	// Here lets check if request is setted
	if(base.length()>0){
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			
			// LETS GET THE DOCUMENT WE ARE TO READ
			try {
				Document doc;
				doc = dBuilder.parse(util.antResource(FilePath));
				doc.getDocumentElement().normalize();
				// HERE WE START PROCESSING FETCH AND HARVEST OF XML TAGS
				NodeList parentNode = doc.getElementsByTagName(base);
				Element baseElmnt_gold = (Element) parentNode.item(0);
				NodeList entries = baseElmnt_gold.getChildNodes();
				Element nodeElement = null;
				int num = entries.getLength();
				harvested=new String[num];
				for (int i=0; i<num; i++) {
					if (entries.item(i) instanceof Element)
				    {
						nodeElement = (Element) entries.item(i);
						harvested[i]=getAllAttributes(nodeElement);
				    }
				}

				// HERE WE PROCESS THE HARVESTED TAGS
				// Here we remove all null in array
				// HERE WE TRY TO COMPOSE ARRAY TO REMOVE NULL
				List<String> list=new ArrayList<String>();
				for(String s:harvested){
					if(s!=null && s.length()>0){
						list.add(s);
					}
				} // End of loop
						
				// Now we change data of array
				harvested=list.toArray(new String[list.size()]);
				for(String data:harvested){
					// Now lets start processing
					harvestData=data.split("\\>");
					String tagname=harvestData[0];
					String attributes=harvestData[1];
					// Check attribute
					if(attributes.equals("null")){
						// Here we set
						harvestMap.put(tagname, null);
					}
					else{
						// We process attribute as array
						if(attributes.indexOf(",")>=0){
							// Many attributes dictated
							String[] attrArray=attributes.split(",");
							harvestMap.put(tagname, attrArray);
						}
						else{
							// Only one attribute dictated
							String[] attrArray={attributes};
							harvestMap.put(tagname, attrArray);
						}
					}
				} // End of foreach loop
				
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// Lets return
	return harvestMap;
}




/********* CONSTRUCTING THE DYNAMIC SYNTAX BUILDER * @throws URISyntaxException *********/
public String syntaxBuilder(String base) throws URISyntaxException{
	// LETS START CONSTRUCTING THE SYNTAX BUILDER
	String builtSyntax = "";
	String[] harvested={};
	String[] harvestData={};
	
	// Here lets check if request is setted
	if(base.length()>0){
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			
			// LETS GET THE DOCUMENT WE ARE TO READ
			try {
				Document doc;
				doc = dBuilder.parse(util.antResource(FilePath));
				doc.getDocumentElement().normalize();
				// HERE WE START PROCESSING FETCH AND HARVEST OF XML TAGS
				NodeList parentNode = doc.getElementsByTagName(base);
				Element baseElmnt_gold = (Element) parentNode.item(0);
				NodeList entries = baseElmnt_gold.getChildNodes();
				Element nodeElement = null;
				int num = entries.getLength();
				harvested=new String[num];
				for (int i=0; i<num; i++) {
					if (entries.item(i) instanceof Element)
				    {
						nodeElement = (Element) entries.item(i);
						harvested[i]=getAllAttributes(nodeElement);
				    }
				}
				
				// HERE WE START INITIATING SYNTAX BUILDING
				// Here we remove all null in array
				// NOW LETS REMOVE ANY NULL IN THE ARRAY
				// HERE WE TRY TO COMPOSE ARRAY TO REMOVE NULL
				List<String> list=new ArrayList<String>();
				for(String s:harvested){
					if(s!=null && s.length()>0){
						list.add(s);
					}
				} // End of loop
						
				// Now we change data of array
				harvested=list.toArray(new String[list.size()]);
				// status@val;respond~from@val,position@val;date@val
				for(String data:harvested){
					// Now lets start building synatx
					harvestData=data.split("\\>");
					String tagname=harvestData[0];
					String attributes=harvestData[1];
					NodeList pList=doc.getElementsByTagName(tagname);
					Node pElement=pList.item(0);
					Element elem=(Element) pElement;
					// Check attribute
					if(attributes.equals("null")){
						// Here we set
						if(builtSyntax.length()>0){
							if(!hasChildElements(elem)){
								builtSyntax+=";"+tagname+"@val";
							}
							else{
								builtSyntax+=";"+tagname;
							}
						}
						else{
							if(!hasChildElements(elem)){
								builtSyntax+=tagname+"@val";
							}
							else{
								builtSyntax+=tagname;
							}
						}
					}
					else{
						// We process attribute as array
						if(attributes.indexOf(",")>=0){
							// Many attributes dictated
							String[] attrArray=attributes.split(",");
							String attributeSyntax="";
							// Here we build attribute synatx
							for(String d:attrArray){
								if(attributeSyntax.length()>0){
									attributeSyntax+=","+d+"@val";
								}
								else{
									attributeSyntax+=d+"@val";
								}
							} // end of loop
							// Now lets add tag to the synatx
							// If tag contains text value we add or no
							if(builtSyntax.length()>0){
								// Here theres something on the build syntax
								if(!hasChildElements(elem)){
									builtSyntax+=";"+tagname+"@val~"+attributeSyntax;
								}
								else{
									builtSyntax+=";"+tagname+"~"+attributeSyntax;
								}
							}
							else{
								// Here theres nothing on the build syntax
								if(!hasChildElements(elem)){
									builtSyntax+=tagname+"@val~"+attributeSyntax;
								}
								else{
									builtSyntax+=tagname+"~"+attributeSyntax;
								}
							}
						}
						else{
							// Only one attribute dictated
							if(builtSyntax.length()>0){
								// Here there is something on the built syntax
								if(!hasChildElements(elem)){
									builtSyntax+=";"+tagname+"@val~"+attributes+"@val";
								}
								else{
									builtSyntax+=";"+tagname+"~"+attributes+"@val";
								}
							}
							else{
								// Here nothing is on the built syntax
								if(!hasChildElements(elem)){
									builtSyntax+=tagname+"@val~"+attributes+"@val";
								}
								else{
									builtSyntax+=tagname+"~"+attributes+"@val";
								}
							}
							
						}
					}
				} // end of loop
				
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// Lets return
	return builtSyntax;
}




/************************* CONSTRUCTING THE ATTRIBUTE GETTER *********************************/
public String getAllAttributes(Element element) {
	// LETS START THE PROCESSING
	String contents="";
    String elementName=element.getNodeName();
    String attributeDatas="";

    // get a map containing the attributes of this node
    NamedNodeMap attributes = element.getAttributes();

    // get the number of nodes in this map
    int numAttrs = attributes.getLength();

    for (int i = 0; i < numAttrs; i++) {
        Attr attr = (Attr) attributes.item(i);
        String attrName = attr.getNodeName();
        // We check if attribute data has been set
        if(attributeDatas.length()>0){
        	attributeDatas+=","+attrName;
        }
        else{
        	attributeDatas+=attrName;
        }
    }
    // Now lets add the element name to its attributes
    if(attributeDatas.length()>0){
    	contents=elementName+">"+attributeDatas;
    }
    else{
    	contents=elementName+">null";
    }
    // Here we return
    return contents;
}




/*********************** CONSTRUCTING THE HAS CHILDELEMENTS METHOD ************************/
public static boolean hasChildElements(Element el){
	NodeList children=el.getChildNodes();
	for(int i=0; i<children.getLength(); i++){
		if(children.item(0).getNodeType()==Node.ELEMENT_NODE){
			return true;
		}
	}
	return false;
}

// END OF CLASS	
}
