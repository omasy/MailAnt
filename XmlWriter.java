package system.source;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import system.soft.control.AntResource;

public class XmlWriter {
// SETTING INSTANCE VARIABLES
private String Syntax;
private String Mode="w";
private String FileOutPath;
private String FileInputPath;
private String Filename;
AntResource resource = new AntResource();

/****************************** HERE WE CONSTRUCT CLASS ********************************/
public XmlWriter(String syntax, String fname){
	// Setting values
	Syntax=syntax;
	Filename=fname;
}



/***************************** SETTING THE TESTING METHOD ******************************/
public static void main(String[] args){
	// NOW LETS SET ALL VARIABLES
	String syntax="header->status:accepted;header->resolved:yes;header->date:5/10/2017;responses->respond(8.item^type!michael)~from:djade,position:1";
	// LETS INSTANTIATE THE CLASS
	XmlWriter writer=new XmlWriter(syntax, null);
	writer.fIn("cores/core/log/structure/rtrend/struct_rtrend.xml");
	writer.fOut("cores/core/testData/");
	// Now lets display the output
	try {
		System.out.println(writer.xmlEditor());
	} catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}



/***************************** CONSTRUCTING THE MODE GETTING METHOD ********************/
public void mode(String mode){
	// LETS SET MODE
	Mode=mode;
}


/*************************** CONSTRUCTING THE OUTPUT PATH METHOD ***********************/
public void fOut(String FilePath){
	// LETS SET THE FOUT
	if(Filename!=null){
		FileOutPath=FilePath+Filename+".xml";
	}
	else{
		FileOutPath=FilePath+new Random().nextInt(999999999)+".xml";
	}
}


/************************** CONSTRUCTING THE STRUCTURE FILE IN *************************/
public void fIn(String FilePath){
	// LETS SET THE STRUTF
	FileInputPath=FilePath;
}




/**** NOW LETS START CONSTRUCTING THE PROCESSOR METHOD * @throws URISyntaxException ****/
public int xmlEditor() throws URISyntaxException{
	// NOW LETS START PROCESSING XML TO EDIT
	int trueValue=0;
	String[] syntaxDatas={};
	String strutData="";
	int editFeed=0;
	DjadeUtil util=new DjadeUtil();
	
	// CREATING WORKABLE VARIABLES
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder;
	
	
	// HERE WE START THE INNER BODY PROCESSING
	// LETS PROCESS THE XML FILE FOR EDITING CONTENTS
	syntaxDatas=util.syntaxInterpreter(Syntax);
	
	// NOW LETS CHECK DATA PROPERTIES
	if(Syntax!=null && FileInputPath.length()>0){
		// NOW LETS CHECK MODE TO KNOW HOW WE PROCESS
		if(Mode.equals("w")){
			try {
				// Now lets get the file data
				strutData=util.readBuffer(resource.get(FileInputPath), "ant");
				try {
					dBuilder = dbFactory.newDocumentBuilder();
					// NOW LETS CONVERT TO READABLE XML STREAM
					InputSource is=new InputSource(new StringReader(strutData));
					// LETS GET THE DOCUMENT WE ARE TO READ
					try {
						Document doc;
						doc = dBuilder.parse(is);
						doc.getDocumentElement().normalize();
						// NOW LETS EDIT OUR DOCUMENT TO SAVE FINISHED OUTPUT TO FILE
						editFeed=editWiz(doc, syntaxDatas);
						// NOW LETS CHECK THE EDIT FEED BACK
						if(editFeed>0){
							// HERE WE TRY TO SAVE FILE TO NEW PATH
							TransformerFactory transformerFactory=TransformerFactory.newInstance();
							try {
								Transformer transformer=transformerFactory.newTransformer();
								DOMSource source=new DOMSource(doc);
								StreamResult result=new StreamResult(util.antResource(FileOutPath));
								try {
									transformer.transform(source, result);
									// NOW LETS SET TRUE VALUE
									trueValue=1;
								} catch (TransformerException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							} catch (TransformerConfigurationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(Mode.equals("a")){
			// HERE WE ARE TO EDIT EXISTING XML FILE
			try {
				dBuilder = dbFactory.newDocumentBuilder();
				// LETS GET THE DOCUMENT WE ARE TO READ
				Document doc;
				try {
					doc = dBuilder.parse(util.antResource(FileInputPath));
					doc.getDocumentElement().normalize();
					// NOW LETS EDIT OUR DOCUMENT TO SAVE FINISHED OUTPUT TO FILE
					editFeed=editWiz(doc, syntaxDatas);
					// NOW LETS CHECK THE EDIT FEED BACK
					if(editFeed>0){
						// HERE WE TRY TO SAVE FILE TO NEW PATH
						TransformerFactory transformerFactory=TransformerFactory.newInstance();
						try {
							Transformer transformer=transformerFactory.newTransformer();
							DOMSource source=new DOMSource(doc);
							StreamResult result=new StreamResult(util.antResource(FileInputPath));
							try {
								transformer.transform(source, result);
								// NOW LETS SET TRUE VALUE
								trueValue=1;
							} catch (TransformerException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						} catch (TransformerConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
		}
	} // End of data check
	
	
	// LETS RETURN VALUE
	return trueValue;
	
	// End of method
}



/************************** CONSTRUCTING THE EDITWIZARD METHOD ****************************/
private int editWiz(Document doc, String[] syntax){
	// NOW LETS START PROCESSING THE EDIT WIZARD TERMINOLOGY
	int trueValue=0;
	String[] SyntaxPaths;
	String[] refTagging;
	String[] splitChild;
	String[] indexData;
	String attr="";
	String tag="";
	String tagVal="";
	String parentTag="";
	String childTag="";
	String indexString="";
	String inSyntax="";
	int indexKey=0;
	boolean tagRef=false;
	boolean create=false;
	boolean specific=false;
	boolean hasSyntax=false;
	
	// NOW LETS START PROCESSING
	if(syntax!=null && syntax.length>0){
		// NOW LETS GET INDIVIDUAL ELEMNTS AND STORE WITH THERE VALUES AND NAMES
		for(int i=0; i<syntax.length; i++){
			// CHECKING IF TAG REFRENCE ANOTHER AND SETTING A FEED
			if(syntax[i].indexOf("->")>=0){
				// Syntax contains tag reference
				tagRef=true;
			}
			else{
				tagRef=false;
			}
			
			// NOW LETS LOOP THROUGH INDIVIDUAL SYNTAX AND GET VALUES
			if(syntax[i].indexOf("~")>=0){
				// HERE TAG NAME IS SPECIFIED AND IT CONTAINS ATTRIBUTE
				SyntaxPaths=syntax[i].split("\\~");
				// Here we select tag name so we check
				if(tagRef==true){
					// We set the parent and child tag
					refTagging=SyntaxPaths[0].split("\\->");
					parentTag=refTagging[0];
					// We check if child tag has value
					if(refTagging[1].indexOf(":")>=0){
						// Lets further check other directives
						if(refTagging[1].indexOf("c:")>=0){
							// Here we have create directive
							create=true;
							childTag=refTagging[1].substring(refTagging[1].indexOf("c:")+1, refTagging[1].indexOf(":")-1);
						}
						else{
							// WE HAVE TO ADD THE INDEX KEY FUNCTION HERE
							// Lets further check for index number
							if(refTagging[1].indexOf("(")>=0 && refTagging[1].indexOf(")")>=0){
								// Here we have index specific
								splitChild=refTagging[1].split("\\(");
								childTag=splitChild[0];
								indexString=splitChild[1].substring(0, splitChild[1].indexOf("):")-1);
								if(indexString.indexOf(".")>=0 && indexString.indexOf("!")>=0){
									indexData=inInterpreter(indexString);
									indexKey=Integer.parseInt(indexData[0]);
									inSyntax=indexData[1];
									hasSyntax=true;
								}
								else{
									indexKey=Integer.parseInt(indexString);
								}
								// Setting the specific
								specific=true;
							}
							else{
								// We have no index specific
								childTag=refTagging[1].substring(0, SyntaxPaths[0].indexOf(":")-1);
								specific=false;
							}
						}
					}
					else{
						// HERE WE DO NOT HAVE VALUE FOR ELEMENT
						// Lets further check other directives
						if(refTagging[1].indexOf("c:")>=0){
							// Here we have create directive
							create=true;
							childTag=refTagging[1].substring(refTagging[1].indexOf("c:")+1, refTagging[1].length());
						}
						else{
							// We do not have create directive
							if(refTagging[1].indexOf("(")>=0 && refTagging[1].indexOf(")")>=0){
								// Here we have index specific
								splitChild=refTagging[1].split("\\(");
								childTag=splitChild[0];
								indexString=splitChild[1].substring(0, splitChild[1].length()-1);
								if(indexString.indexOf(".")>=0 && indexString.indexOf("!")>=0){
									indexData=inInterpreter(indexString);
									indexKey=Integer.parseInt(indexData[0]);
									inSyntax=indexData[1];
									hasSyntax=true;
								}
								else{
									indexKey=Integer.parseInt(indexString);
								}
								// Setting the specific
								specific=true;
							}
							else{
								childTag=refTagging[1];
								specific=false;
							}
							
						}
					}
				}
				else{
					// HERE PARENT TAG IS NOT SPECIFIED
					// Here we just set child tag
					if(SyntaxPaths[0].indexOf(":")>=0){
						// Lets further check other directives
						if(SyntaxPaths[0].indexOf("c:")>=0){
							// Here we have create directive
							create=true;
							childTag=SyntaxPaths[0].substring(SyntaxPaths[0].indexOf("c:")+1, SyntaxPaths[0].indexOf(":")-1);
						}
						else{
							// We do not have create directive
							// Lets further check for index number
							if(SyntaxPaths[0].indexOf("(")>=0 && SyntaxPaths[0].indexOf(")")>=0){
								// Here we have index specific
								splitChild=SyntaxPaths[0].split("\\(");
								childTag=splitChild[0];
								indexString=splitChild[1].substring(0, splitChild[1].indexOf("):")-1);
								if(indexString.indexOf(".")>=0 && indexString.indexOf("!")>=0){
									indexData=inInterpreter(indexString);
									indexKey=Integer.parseInt(indexData[0]);
									inSyntax=indexData[1];
									hasSyntax=true;
								}
								else{
									indexKey=Integer.parseInt(indexString);
								}
								// Setting the specific
								specific=true;
							}
							else{
								// We have no index specific
								childTag=SyntaxPaths[0].substring(0, SyntaxPaths[0].indexOf(":")-1);
								specific=false;
							}
						}
					}
					else{
						// HERE TAG DOES NOT HAVE VALUE
						if(SyntaxPaths[0].indexOf("c:")>=0){
							// Here we have create directive
							create=true;
							// Lets further check for index number
							if(SyntaxPaths[0].indexOf("(")>=0 && SyntaxPaths[0].indexOf(")")>=0){
								// Here we have index specific
								splitChild=SyntaxPaths[0].split("\\(");
								childTag=splitChild[0].substring(splitChild[0].indexOf("c:")+1, splitChild[0].length());
								indexString=splitChild[1].substring(0, splitChild[1].length()-1);
								if(indexString.indexOf(".")>=0 && indexString.indexOf("!")>=0){
									indexData=inInterpreter(indexString);
									indexKey=Integer.parseInt(indexData[0]);
									inSyntax=indexData[1];
									hasSyntax=true;
								}
								else{
									indexKey=Integer.parseInt(indexString);
								}
								// Setting the specific
								specific=true;
							}
							else{
								childTag=SyntaxPaths[0].substring(SyntaxPaths[0].indexOf("c:")+1, SyntaxPaths[0].length());
								specific=false;
							}
						}
						else{
							// We do not have create directive
							// Lets further check for index number
							if(SyntaxPaths[0].indexOf("(")>=0 && SyntaxPaths[0].indexOf(")")>=0){
								// Here we have index specific
								splitChild=SyntaxPaths[0].split("\\(");
								childTag=splitChild[0];
								indexString=splitChild[1].substring(0, splitChild[1].length()-1);
								if(indexString.indexOf(".")>=0 && indexString.indexOf("!")>=0){
									indexData=inInterpreter(indexString);
									indexKey=Integer.parseInt(indexData[0]);
									inSyntax=indexData[1];
									hasSyntax=true;
								}
								else{
									indexKey=Integer.parseInt(indexString);
								}
								// Setting the specific
								specific=true;
							}
							else{
								childTag=SyntaxPaths[0];
								specific=false;
							}
							
						}
					}
				}
				
				// Setting the attribute part
				attr=SyntaxPaths[1];
				// NOW LETS GET THE ELEMENT BY NODE NAME
				NodeList nList=null;
				Node pElement=null;
				Element elem=null;
				if(tagRef==false){
					nList = doc.getElementsByTagName(childTag);
				}
				else{
					NodeList pList=doc.getElementsByTagName(parentTag);
					// LETS CHECK THE INDEX
					if(specific==false){
						// We set index to zero
						pElement=pList.item(0);
					}
					else{
						// We set index to the specified index
						// pElement=pList.item(indexKey);
						XPathFactory xPathfactory = XPathFactory.newInstance();
						XPath xpath = xPathfactory.newXPath();
						try {
							XPathExpression expr = xpath.compile("//"+childTag+"[@id="+indexKey+"]");
							nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
						} catch (XPathExpressionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					// Converting to element
					pElement=pList.item(0);
					elem=(Element) pElement;
					//nList=elem.getElementsByTagName(childTag);
				}
				
				
				/****************** HERE WE MAKE DECISSION TO CREATE OR UPDATE *******************/
				if(create==true){
					// HERE WE CREATE AND APPEND TAG
					// NOW LETS CREATE ELEMENTS
					// We have check if tag exists
					NodeList docTag=doc.getElementsByTagName(childTag);
					Element target=null;
					if(docTag.getLength()<1){
						target=doc.createElement(childTag);
						// NOW WE CAN ADD ATTRIBUTES
						Attr attribute=doc.createAttribute(attr.substring(0, attr.indexOf(":")+1));
						attribute.setValue(attr.substring(attr.indexOf(":")+1, attr.length()));
						target.setAttributeNode(attribute);
						elem.appendChild(target);
					}
					else{
						// NOW WE CAN ADD ATTRIBUTES
						// Node rElement=nList.item(0);
						// target=(Element) rElement;
						Node rElement=doc.getElementsByTagName(childTag).item(0);
						target=(Element) rElement;
						Attr attribute=doc.createAttribute(attr.substring(0, attr.indexOf(":")+1));
						attribute.setValue(attr.substring(attr.indexOf(":")+1, attr.length()));
						target.setAttributeNode(attribute);
						elem.appendChild(target);
					}
					
				}
				
				if(create==false){
					// HERE WE EDIT EXISTING TAG AND ATTRIBUTE WITH VALUES	
					// LETS LOOP ALL NODES
					Node element=nList.item(0);
					//Element eElement = (Element) element;
					// Now lets get attribute value
					if(attr.indexOf(":")>=0){
						// Here we are getting attributes with values only
						tag=attr.substring(0, attr.indexOf(":"));
						tagVal=attr.substring(attr.indexOf(":")+1, attr.length());
						NamedNodeMap attrMap=element.getAttributes();
						Node nodeAttr=attrMap.getNamedItem(tag);
						nodeAttr.setTextContent(tagVal);
					}
					
					// NOW LETS LOG THE CONTENT OF THE INNER ELEMENT
					if(hasSyntax==true){
						// Here we log content of data
						inWriter((Element) element, inSyntax);
					}
					
					// Incrementing true value
					trueValue++;
				}			
			}
			else{
				// TAG LINE DOES NOT CONTAIN ATTRIBUTE
				if(syntax[i].indexOf(":")>=0){
					// Here we are getting tags with values only
					tag=syntax[i].substring(syntax[i].indexOf("->")+2, syntax[i].indexOf(":"));
					tagVal=syntax[i].substring(syntax[i].indexOf(":")+1, syntax[i].length());
					NodeList nList=doc.getElementsByTagName(tag);
					// LETS LOOP ALL NODES
					Node element=nList.item(0);
					element.setTextContent(tagVal);
					
					// NOW LETS LOG THE CONTENT OF THE INNER ELEMENT
					if(hasSyntax==true){
						// Here we log content of data
						inWriter((Element) element, inSyntax);
					}
					
					// Incrementing true value
					trueValue++;
				}
			}
		} // End of loop
	} // End of syntax check
	
	// LETS RETURN TRUE VALUE
	return trueValue;
	
	// End of method
}



/*********************** CONSTRUCTING THE INTAG INTERPRETER METHOD **************************/
private String[] inInterpreter(String Ins){
	// LETS START PROCESSING THE SYNTAX SEPARATOR
	String[] returnDatas=new String[2];
	String[] parts;
	
	// LETS START PROCESSING
	if(Ins.indexOf(".")>=0 && (Ins.indexOf("!")>=0 || Ins.indexOf("^")>=0)){	
		// Now lets break data
		parts=Ins.split("\\.");
		// Lets check parts
		if(parts.length>0){
			returnDatas[0]=parts[0];
			returnDatas[1]=parts[1];
		} // End of part part check
	}
	
	return returnDatas;
}



private void inWriter(Element parent, String syntax){
	// NOW LETS START PROCESSING THE SYNTAX WRITER
	String[] parts;
	String[] bparts;
	String[] aparts;
	String tagparts="";
	String attrparts="";
	String tagName="";
	String tagVal="";
	String attrName="";
	String attrVal="";
	// Setting creating instruction values
	boolean hasVal=false;
	boolean hasAttr=false;
	// Setting child elements
	NodeList cList=null;
	Node elem=null;
	// NOW LETS START PROCESSING
	if(syntax.length()>0 && syntax!=null){
		// NOW LETS CHECK IF SYNTAX TAG CONTAINS VALUES
		if(syntax.indexOf("^")>=0){
			// Here syntax tag contains attribute
			hasAttr=true; // Setting has attribute to true
			parts=syntax.split("\\^");
			tagparts=parts[0];
			attrparts=parts[1];
			// Lets check tag parts for value
			if(tagparts.indexOf("!")>=0){
				// Here tag contains value
				bparts=tagparts.split("\\!");
				tagName=bparts[0];
				tagVal=bparts[1];
				// We get attributes values
				aparts=attrparts.split("\\!");
				attrName=aparts[0];
				attrVal=aparts[1];
				hasVal=true;
				hasAttr=true;
			}
			else{
				// Tag contains no value
				tagName=tagparts;
				// We get attributes values
				aparts=attrparts.split("\\!");
				attrName=aparts[0];
				attrVal=aparts[1];
				hasVal=true;
				hasAttr=true;
			}
		}
		else{
			// Here syntax tag contains no attribute
			bparts=syntax.split("\\!");
			tagName=bparts[0];
			tagVal=bparts[1];
		}
	} // End of synatx check
	
	/****************** NOW LETS WRITE ELEMENT TO FILE *******************/
	// Tag has values
	cList=parent.getElementsByTagName(tagName);
	elem=cList.item(0);
	Element element=(Element) elem;
	// NOW LETS START SETTING VALUES
	if(hasAttr==true){
		// We log attribute values and tag same
		if(hasVal==true){
			// Lets set values and attributes
			element.setTextContent(tagVal);
			NamedNodeMap attrMap=element.getAttributes();
			Node nodeAttr=attrMap.getNamedItem(attrName);
			nodeAttr.setTextContent(attrVal);
			
		}
		else{
			// Tag has no values
			// Lets set values and attributes
			NamedNodeMap attrMap=element.getAttributes();
			Node nodeAttr=attrMap.getNamedItem(attrName);
			nodeAttr.setTextContent(attrVal);
		}
	}
	else{
		// We log only tag values
		// Lets set values and attributes
		element.setTextContent(tagVal);
	}
	
	// End of method
}

// END OF CLASS
}
