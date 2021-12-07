package system.soft.processor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.commons.lang.ArrayUtils;

import system.soft.control.AntResource;
import system.source.DjadeCompressor;
import system.source.DjadeUtil;

public class FileManager {
	// HERE WE CONSTRUCT CLASS
	AntResource resource;
	SetBase Setting;
	DjadeUtil Util;
	
	public FileManager(SetBase setting){
		Setting=setting;
		Util=new DjadeUtil();
		resource = new AntResource();
	};
	
	
	/********* HERE WE INVOKE THE EMAIL VALIDATOR ***********/
	public Filter filter(){
		return new Filter();
	}
	
	
	/** HERE WE CONSTRUCT METHODS * @throws URISyntaxException **/
	public String choose(String command) throws URISyntaxException{
		return new MailProcess().choose(command);
	}
	
	
	public String[][] quickData(String path, String command){
		return new MailProcess().getTinyContent(path, command);
	}
	
	
	public String uniWriter(String outPath, String[][] data, Object[] header){
		return new MailProcess().uniWriter(outPath, data, header);
	}
	
	
	public int uniEditor(String logPath, String[][] logData){
		return new MailProcess().uniEditor(logPath, logData);
	}
	
	
	public String[] getOneRows(String[][] data){
		return new MailProcess().getOneRow(data);
	}
	
	
	public String importer(String extFilePath, String dest) throws URISyntaxException{
		// HERE WE START CONSTRUCTING THE IMPORTER PROCESSES
		String destination=resource.get(Setting.get("sendDir"));
		String DataLog="";
		String filePath="";
		// HERE WE START PROCESSING
		if(extFilePath.length()>0 && extFilePath.indexOf("/")>=0){
			// Lets set the destination
			if(dest!=null){
				destination=dest;
			}
			
			// HERE WE TRY OUR CODE
			try {
				DataLog=Util.saveFile(extFilePath, destination);
				if(DataLog.indexOf("[")>=0 && DataLog.indexOf("=")>=0){
					// Here we imported multiple files
					filePath=destination;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Here we return
		return filePath;
	}
	
	
	public String exporter(String intDir, String extDir, String filename) throws URISyntaxException{
		DjadeCompressor compress;
		String path="";
		
		// HERE WE START PROCESSING
		if(extDir!=null && extDir.length()>0){
			if(intDir!=null && intDir.length()>0){
				compress=new DjadeCompressor();
				compress.zipDir(extDir);
				compress.fIn(resource.get(intDir));
				// Lets check file name
				if(filename!=null && filename.length()>0){
					path=compress.zip(filename);
				}
				else{
					path=compress.zip(null);
				}
			}
		}
		
		// Here we return
		return path;
	}
	
	
	public int moveMail(String bankPath) throws URISyntaxException{
		return new Transport().moveMails(bankPath);
	}
	
	
	public int delete(String path){
		return new Transport().delete(path);
	}
	
	
	public String compile(String fIn, String dOut, String command){
		return new Transport().compile(fIn, dOut, command);
	}
	
	
	public int save(String[][] mailData, String command) throws URISyntaxException{
		// HERE WE START CONSTRUCTING THE MAIL SAVING PROCESS
		String logPath = resource.get(Setting.get("extLog"));
		File logFile = new File(logPath);
		String[] data = {};
		int trueValue=0;
		int check = 0;
		// HERE WE START PROCESSING
		if(mailData!=null && mailData.length>0){
			if(command!=null && command.length()>0){
				// Here we check the log path
				if(logFile.exists() && logPath.indexOf(".csv")>=0){
					// Here Log file exists so we edit
					data = getOneRows(mailData);
					if(data!=null && data.length>0){
						try {
							check = editor(logPath, data);
							if(check>0){
								trueValue = 1;
							}
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				else{
					// Here Log file don't exist so we create
					check = log("Google.com", mailData, command);
					if(check>0){
						trueValue = 1;
					}
				}
			}
		}
		
		// Here we return bank path
		return trueValue;
	}

	
	public int editor(String logPath, String[] Logdata) throws MalformedURLException{
		// HERE WE CONSTRUCT THE EDITOR PROCESSES
		MailProcess process=new MailProcess();
		CSVUtils csvUtil=new CSVUtils();
		String[][] newData={};
		String[][] logData={};
		String[][] newLog={};
		Object[] header={};
		String[] fields={};
		String comingPath="";
		String basePath="";
		String xpath="";
		int trueValue=0;
		int pCount=0;
		// HERE WE START PROCESSING
		if(logPath!=null && Logdata!=null){
			if(logPath.length()>0 && Logdata.length>0){
				if(new File(logPath).exists()){
					// Here we set field
					fields=new String[]{"Coming", "Going", "Base", "Count"};
					header=new Object[]{"Coming", "Going", "Base", "Count"};
						
					// NOW WE HAVE FIELD LETS SELECT
					CSVReader reader=csvUtil.reader();
					CSVWriter writer=csvUtil.writer();
					reader.header(fields);
					reader.fIn(logPath);
					logData=reader.readCSV(fields);
					if(logData!=null){
						if(logData.length>0){
							// Now lets set new array max and write again
							comingPath=logData[logData.length-1][0];
							basePath=logData[logData.length-1][2];
							pCount=Integer.parseInt(logData[logData.length-1][3]);
							// Now lets log new data and return path
							xpath=process.tinyWriter(basePath, Logdata, header);
							if(xpath!=null && xpath.indexOf(".csv")>=0){
								// Here we set new log
								newLog=new String[1][4];
								newLog[0][0]=comingPath;
								newLog[0][1]=xpath;
								newLog[0][2]=basePath;
								newLog[0][3]=String.valueOf((pCount+1));
								// Now lets merge old and new content and edit
								newData=(String[][])ArrayUtils.addAll(logData, newLog);
								if(newData!=null && newData.length>0){
									writer.header(header);
									writer.fOut(logPath);
									writer.writeCSV(newData);
									trueValue=1;
								}
							} // end of xpath check
						}
					}
				}
			} // end of check two
		} // end of check one
		
		// Here we return
		return trueValue;
	}
	
	
	public int clear(String logPath){
		// HERE WE CONSTRUCT THE CLEAR PROCESSES
		String[] fields={"Coming", "Going", "Base", "Count"};
		Transport transport=new Transport();
		CSVUtils csvUtil=new CSVUtils();
		String[][] logData={};
		String comingPath="";
		String basePath="";
		int trueValue=0;
		int check=0;
		// HERE WE START PROCESSING
		if(logPath!=null){
			if(logPath.length()>0){
				if(new File(logPath).exists()){
					// HERE WE READ LOG TO FIND RELATED FILES
					CSVReader reader=csvUtil.reader();
					reader.header(fields);
					reader.fIn(logPath);
					logData=reader.readCSV(fields);
					if(logData!=null && logData.length>0){
						// Now lets get coming and going paths
						for(int i=0; i<logData.length; i++){
							comingPath=logData[i][0];
							basePath=logData[i][2];
							// Now lets delete files
							check+=transport.delete(comingPath);
							check+=transport.delete(basePath);
							if(check>1){
								check=transport.delete(logPath);
								if(check>0){
									trueValue=1;
								}
							}
						}
					}
				} // end of is file check
			} // end of second check
		} // end of first check
		
		// Here we return
		return trueValue;
	}
	
	
	public String[] stack(HashMap<String, String[][]> mapData, String command) throws URISyntaxException{
		// HERE WE START CONSTRUCTING THE STACK
		Iterator<Map.Entry<String, String[][]>> mappedHarvest=null;
		String[][] dummie = {{"...", "...", "..."}};
		String[][] logData={};
		String[] contents={};
		String bankPath="";
		int logged=0;
		// HERE WE START PROCESSING
		if(!mapData.isEmpty()){
			// Here we log content back
			mappedHarvest=mapData.entrySet().iterator();
			Entry<String, String[][]> next = mappedHarvest.next();
			bankPath=next.getKey();
			logData=next.getValue();
			// Now lets log data
			logged=log(bankPath, dummie, command);
			if(logged>0){
				// Here we get our inputs
				contents=new String[logData.length];
				for(int i=0; i<logData.length; i++){
					contents[i]=logData[i][0];
				} // end of loop
				
			} // end of data check
		}
		// Here we return
		return contents;
	}
	
	
	public int log(String bankPath, String[][] LogDatas, String command) throws URISyntaxException{
		// HERE WE CONSTRUCT THE LOG PROCESSES
		String filename="bank_"+new Random().nextInt(999999999)+".csv";
		String logname="log_"+new Random().nextInt(999999999)+".csv";
		String newPath="tmp_"+new Random().nextInt(9999999)+"/";
		Object[] header={"Coming", "Going", "Base", "Count"};
		String[][] logDatas=new String[1][4];
		CSVUtils csvUtil=new CSVUtils();
		Object[] header2=null;
		String logPath2="";
		String logPath="";
		String filePath="";
		String filePath2="";
		String newField="";
		int trueValue=0;
		int check=0;
		// HERE WE START PROCESSING
		if(bankPath!=null){
			if(LogDatas!=null && LogDatas.length>0){
				if(command!=null && command.length()>0){
					switch(command){
					case "email":
						newField="sentLog";
						logPath=resource.get(Setting.get(newField));
						logPath2=resource.get(Setting.get("sentDir"));
						header2=new Object[]{"Email", "Source", "Engine"};
						break;
					case "url":
						newField="parsedLog";
						logPath=resource.get(Setting.get(newField));
						logPath2=resource.get(Setting.get("parsedDir"));
						header2=new Object[]{"URL", "Term", "Engine"};
						break;
					case "extract":
						newField="extLog";
						logPath=resource.get(Setting.get(newField));
						logPath2=resource.get(Setting.get("sendDir"));
						header2=new Object[]{"Email", "Source", "Engine"};
					}
					
					// NOW LETS LOG THE DATA TO IT LOG FOLDER
					filePath=logPath.substring(0, (logPath.lastIndexOf("/")+1))+logname;
					filePath2=logPath2+newPath+filename;
					// Here lets create folder if it doesnt exist
					File dir = new File(filePath2.substring(0, filePath2.lastIndexOf("/")+1));
					if(!dir.exists()){
						dir.setReadable(true, false);
						dir.setWritable(true, false);
						dir.mkdir();
					}
					// Now lets save file
					CSVWriter write=csvUtil.writer();
					write.header(header2);
					write.fOut(filePath2);
					// Now lets log file
					check=write.writeCSV(LogDatas);
					if(check>0){
						// Here we are going to log the file
						logDatas[0][0]=bankPath;
						logDatas[0][1]=filePath2;
						logDatas[0][2]=filePath2.substring(0, (filePath2.lastIndexOf("/")+1));
						logDatas[0][3]=String.valueOf(LogDatas.length);
						// Now lets log
						write.header(header);
						write.fOut(filePath);
						// Now lets log file
						check+=write.writeCSV(logDatas);
						if(check>1){
							// Here we log the log file data to set
							check+=Setting.set(newField, filePath);
							if(check>2){
								trueValue=1;
							}
						}
					}
					
				} // end of command check
			} // end of data check
		} // end of bank check
		
		// Here we return log path
		return trueValue;
	}
	


	public String[] getContent(String logPath, String command) throws URISyntaxException{
		// CALL THE MAIL INTERPRETER AND CHOOSE
		HashMap<String, String[][]> mapContents=new HashMap<String, String[][]>();
		List<String> selected=new ArrayList<String>();
		Transport transport=new Transport();
		MailProcess process=new MailProcess();
		CSVUtils csvUtil=new CSVUtils();
		String[][] CSVDatas={};
		String[][] CSVComing={};
		String[][] CSVGoing={};
		String[] oneComing={};
		String[] contents={};
		String[] fields={};
		String compilePath="";
		String comingPath="";
		String feedPath="";
		String basePath="";
		CSVReader reader;
		int processed=0;
		int deleted=0;
		int Limit=0;
		int start=0;
		// HERE WE START PROCESSING
		if(logPath!=null && logPath.indexOf("/")>=0){
			// Here we set the command
			if(command!=null && command.length()>0){
				switch(command){
				case "email":
					// Here we set field
					fields=new String[]{"Email", "Source", "Engine"};
					Limit=Integer.parseInt(Setting.get("sendRate"));
					feedPath=resource.get(Setting.get("sentDir"));
					break;
				case "url":
					// Here we set field
					fields=new String[]{"URL", "Term", "Engine"};
					Limit=Integer.parseInt(Setting.get("extRate"));
					feedPath=resource.get(Setting.get("parsedDir"));
				}
				
				// HERE WE CHECK IF LOGPATH EXISTS SO WE HAVE TO SELECT APPROPRIATELY
				if(new File(logPath).exists()){
					// Here we start reading
					reader=csvUtil.reader(); // set the reader object
					CSVDatas=process.getTinyContent(logPath, "log");
					if(CSVDatas!=null && CSVDatas.length>0){
						// Here let us get the coming and going path
						comingPath=CSVDatas[CSVDatas.length-1][0];
						basePath=CSVDatas[CSVDatas.length-1][2];
						processed=Integer.parseInt(CSVDatas[CSVDatas.length-1][3]);
						if(processed>0){
							// NOW WE GET THE CONTENTS
							if(comingPath!=null){
								if(comingPath.indexOf("/")>=0){
									CSVComing=process.getTinyContent(comingPath, command);
								}
							}
							
							// HERE WE GET GOING PATH
							if(basePath!=null){
								if(basePath.indexOf("/")>=0){
									CSVGoing=process.readAll(basePath, command);
								}
							}
							
							// NOW LETS CHECK THE COMING AND THE GOING TO GET CONTENT
							// OR WE DELETE AND CALL THE CHOOSE
							if(CSVComing!=null && CSVGoing!=null){
								if(CSVComing.length>0 && CSVGoing.length>0){
									oneComing = process.getOneFiltered(CSVComing);
									if(oneComing!=null){
										if(oneComing.length>0){
											if(CSVGoing.length < oneComing.length){
												for(int i=start; i<oneComing.length; i++){
													if(!Arrays.asList(process.getOneRow(CSVGoing)).contains(oneComing[i])){
														if(selected.size() < Limit){
															selected.add(oneComing[i]);
														}
														else{
															// break to stop getting contents
															start += CSVGoing.length;
															break;
														}
													}
												} // end of loop
											}
										}
									}
									
									// NOW LETS CHECK LIST TO KNOW WHETHER TO SELECT NEW BANK
									if(selected.size()>0){
										// Here we convert selected list to array
										contents=selected.toArray(new String[selected.size()]);
									}
									else{
										// Here we delete the coming path and select new
										// HERE WE DELETE FOLDER AND FILES
										compilePath=transport.compile(basePath, feedPath, command);
										if(compilePath!=null && compilePath.indexOf("/")>=0){
											// We check to delete compiled
											if(command.equals("url")){
												transport.delete(compilePath);
											}
											
											// Here we have to edit log
											deleted=transport.delete(logPath);
											if(deleted>0){
												deleted+=transport.delete(comingPath);
												deleted+=transport.delete(basePath);
												// Now lets choose
												if(deleted>2){
													mapContents=process.rdChoose(reader, fields, command);
													if(mapContents!=null){
														if(mapContents.size()>0){
															// Here we set content
															contents=stack(mapContents, command);
														} // end of map content check
													}
												}
											} // end of delete check
										} // end of compile Path check
									}
								} // end csv content length check
							} // end of csv content null check
						}
					}
					
				}
				else{
					// Here we choose
					mapContents=process.rdChoose(csvUtil.reader(), fields, command);
					if(mapContents!=null){
						if(mapContents.size()>0){
							// Here we set content
							contents=stack(mapContents, command);
						} // end of map content check
					}
				}
				
			} // end of command check
		} // end of log path check
		// Here we return
		return contents;
	}
	
	
	
	/************ CREATING INNER CLASS ONE ******************/
	private class MailProcess{
		public String choose(String command) throws URISyntaxException{
			// HERE WE START CONSTRUCTING THE CHOOSE PROCESS
			String sendMailPath="";
			String mailBank=null;
			File[] fileList={};
			File[] allFiles={};
			int key=0;
			// HERE WE START PROCESSING
			if(command!=null && command.length()>0){
				// Lets get the appropriate bank directory
				switch(command){
				case "email":
					sendMailPath=resource.get(Setting.get("sendDir"));
					break;
				case "url":
					sendMailPath=resource.get(Setting.get("parseDir"));
				}
				
				// Now lets get our bank
				if(new File(sendMailPath).isDirectory()){
					fileList=new File(sendMailPath).listFiles();
					allFiles=Util.fileFilter(Util.getFiles(fileList));
					// Now lets check file
					if(allFiles!=null){
						if(allFiles.length>0){
							// Here lets select a random index
							key=new Random().nextInt(allFiles.length);
							mailBank=allFiles[key].toString().replace("\\", "/");
						}
					}
				}
			}
			// Here we return
			return mailBank;
		}
		
		
		public HashMap<String, String[][]> rdChoose(CSVReader reader, String[] fields, String command) throws URISyntaxException{
			// HERE WE START CONSTRUCTING READER
			HashMap<String, String[][]> contents=new HashMap<String, String[][]>();
			String[][] CSVTmp={};
			String[][] CSVComing={};
			String comingPath="";
			int Limit=0;
			// HERE WE START PROCESSING
			if(fields!=null && command!=null){
				// Lets get the appropriate bank directory
				switch(command){
				case "email":
					// Here we set limit
					Limit=Integer.parseInt(Setting.get("sendRate"));
					break;
				case "url":
					// Here we set limit
					Limit=Integer.parseInt(Setting.get("extRate"));
				}
				
				// Now lets choose
				comingPath=choose(command);
				if(comingPath!=null){
					// Now lets read CSV File
					if(new File(comingPath).exists()){
						CSVComing=new String[Limit][3];
						CSVTmp=getTinyContent(comingPath, command);
						if(CSVTmp!=null){
							if(CSVTmp.length>0){
								// LETS GET ONLY DATA WE REQUIRE AT A TIME
								if(CSVTmp.length<Limit){
									CSVComing=new String[CSVTmp.length][3];
								}
								// Here we loop to assign value
								for(int i=0; i<CSVTmp.length; i++){
									if(i<Limit){
										CSVComing[i][0]=CSVTmp[i][0];
										CSVComing[i][1]=CSVTmp[i][1];
										CSVComing[i][2]=CSVTmp[i][2];
									}
									else{
										// Here we break to stop getting content
										break;
									}
								} // end of loop
								
								// NOW LETS LOG TO MAP
								contents.put(comingPath, CSVComing);
							}
						}
					}
				}
			} // end of check
			
			// Here we return
			return contents;
		}
		
		public String[][] readAll(String basePath, String command){
			// HERE WE START CONSTRUCTING THE READ ALL PROCESS
			String[][] contents = {};
			File[] listFiles = {};
			String[][] datas = {};
			File baseFile = null;
			// HERE WE START PROCESSING
			if(basePath != null && command != null){
				if(basePath.length() > 0 && command.length() > 0){
					baseFile = new File(basePath);
					if(baseFile.exists()){
						if(baseFile.isDirectory()){
							listFiles = Util.fileFilter(Util.getFiles(baseFile.listFiles()));
							if(listFiles != null){
								if(listFiles.length > 0){
									for(int i=0; i<listFiles.length; i++){
										datas = getTinyContent(listFiles[i].toString().replace("\\", "/"), command);
										if(datas != null){
											if(datas.length > 0){
												contents = (String[][])ArrayUtils.addAll(contents, datas);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			// Here we return
			return contents;
		}
		
		public String[][] getTinyContent(String bankPath, String command){
			CSVUtils csvUtil=new CSVUtils();
			String[][] CSVData={};
			String[] fields={};
			// HERE WE START PROCESSING
			if(bankPath!=null && bankPath.indexOf("/")>=0){
				// Here we set the command
				if(command!=null && command.length()>0){
					switch(command){
					case "email":
						// Here we set field
						fields=new String[]{"Email", "Source", "Engine"};
						break;
					case "url":
						// Here we set field
						fields=new String[]{"URL", "Term", "Engine"};
						break;
					case "log":
						// Here we set field
						fields=new String[]{"Coming", "Going", "Base", "Count"};
						break;
					case "template":
						// Here we set field
						fields=new String[]{"Name", "Path"};
					}
					
					// NOW LETS READ CONTENT FROM CSV
					CSVReader reader=csvUtil.reader();
					reader.header(fields);
					reader.fIn(bankPath);
					CSVData=reader.readCSV(fields);
					
				} // end of command check
			} // end of check
			
			// Here we return
			return CSVData;
		}
		
		
		public String tinyWriter(String goingPath, String[] tinyData, Object[] header) throws MalformedURLException{
			// HERE WE START CONSTRUCTING THE TINY WRITER
			String filename="bank_"+new Random().nextInt(999999999)+".csv";
			String engine=new URL(Setting.get("engine")).getHost();
			CSVUtils csvUtil=new CSVUtils();
			String returnPath="";
			String[][] logData={};
			String filePath="";
			int check=0;
			// HERE WE START PROCESSING
			if(goingPath!=null && goingPath.indexOf("/")>=0){
				if(tinyData!=null && header!=null){
					if(tinyData.length>0 && header.length>0){
						// HERE LETS PREPARE DATA TO LOG
						logData=new String[tinyData.length][3];
						for(int i=0; i<tinyData.length; i++){
							logData[i][0]=tinyData[i];
							logData[i][1]="...";
							logData[i][2]=engine;
						} // end of loop
						
						// HERE WE CHECK LOG DATA
						filePath=goingPath+filename;
						// Now lets save file
						CSVWriter write=csvUtil.writer();
						write.header(header);
						write.fOut(filePath);
						check=write.writeCSV(logData);
						if(check>0){
							returnPath=filePath;
						}
					} // end of second check
				} // end of first check
			} // end of main check
			
			// Here we return
			return returnPath;
		}
		
		public String uniWriter(String outPath, String[][] tinyData, Object[] header){
			// HERE WE START CONSTRUCTING THE TINY WRITER
			String filename="log_"+new Random().nextInt(999999999)+".csv";
			CSVUtils csvUtil=new CSVUtils();
			String filePath="";
			String returnPath = "";
			int check=0;
			// HERE WE START PROCESSING
			if(outPath!=null && outPath.indexOf("/")>=0){
				if(tinyData!=null && header!=null){
					if(tinyData.length>0 && header.length>0){
						// HERE LETS PREPARE DATA TO LOG
						filePath=outPath+filename;
						// Now lets save file
						CSVWriter write=csvUtil.writer();
						write.header(header);
						write.fOut(filePath);
						check=write.writeCSV(tinyData);
						if(check>0){
							returnPath = filePath;
						}
					} // end of second check
				} // end of first check
			} // end of main check
						
			// Here we return
			return returnPath;
		}
		
		public int uniEditor(String logPath, String[][] Logdata){
			// HERE WE START CONSTRUCTING THE UNI EIDTOR SUB PROCESS
			CSVUtils csvUtil=new CSVUtils();
			String[][] newData={};
			String[][] logData={};
			Object[] header={};
			String[] fields={};
			int trueValue=0;
			// HERE WE START PROCESSING
			if(logPath!=null && Logdata!=null){
				if(logPath.length()>0 && Logdata.length>0){
					// Here we set field
					fields=new String[]{"Name", "Path"};
					header=new Object[]{"Name", "Path"};
						
					// NOW WE HAVE FIELD LETS SELECT
					CSVReader reader=csvUtil.reader();
					CSVWriter writer=csvUtil.writer();
					reader.header(fields);
					reader.fIn(logPath);
					logData=reader.readCSV(fields);
					if(logData!=null){
						if(logData.length>0){
							// Now lets merge old and new content and edit
							newData=(String[][])ArrayUtils.addAll(logData, Logdata);
							if(newData!=null && newData.length>0){
								writer.header(header);
								writer.fOut(logPath);
								writer.writeCSV(newData);
								trueValue=1;
								}
						}
					}
				} // end of check two
			} // end of check one
			
			// Here we return
			return trueValue;
		}
		
		public String[] getOneFiltered(String[][] multi){
			// HERE WE START PROCESSING
			Filter filter = new Filter();
			String[] content = {};
			if(multi!=null){
				if(multi.length>0){
					content = filter.nameFilter(getOneRow(multi));
				}
			}
			// Here we return
			return content;
		}
		
		public String[] getOneRow(String[][] multi){
			// HERE WE START PROCESSTING
			String[] content = {};
			if(multi!=null){
				if(multi.length>0){
					content = new String[multi.length];
					for(int i=0; i<multi.length; i++){
						content[i] = multi[i][0];
					}
				}
			}
			// Here we return
			return content;
		}
		
		// END OF CLASS
	}
	
	
	/************ CREATING INNER CLASS TWO ******************/
	private class Transport{
		public int moveMails(String bankPath) throws URISyntaxException{
			// HERE WE START CONSTRUCTING THE MOVE MAIL PROCESS
			String destination=resource.get(Setting.get("sentDir"));
			String DataLog="";
			int trueValue=0;
			// HERE WE START PROCESSING
			if(bankPath!=null && bankPath.length()>0){
				try {
					DataLog=Util.saveFile(bankPath, destination);
					if(DataLog.indexOf("[")>=0 && DataLog.indexOf("=")>=0){
						trueValue=1;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// Here we return
			return trueValue;
		}
		
		public int delete(String path){
			// HERE WE START CONSTRUCTING THE DELETE
			int trueValue=0;
			File Dir=new File(path);
			// HERE WE START PROCESSING
			// Lets get the files of the directories
			if(Dir.exists()){
				if(Dir.isDirectory()){
					File[] DirFiles=Dir.listFiles();
					if(DirFiles.length>0){ // Here we know we have files i the folder
						// Now lets delete individual files in the temporal directory
						for(File f:DirFiles){
							f.delete();
						} // End of loop
						Dir.delete(); // We delete folder
						trueValue=1;
					}
					else{ // Here we know file is not in the folder
						Dir.delete(); // We delete folder
						trueValue=1;
					}
				}
				else{
					// Here we know file is not in the folder
					Dir.delete(); // We delete folder
					trueValue=1;
				}
			}
			// Here we return
			return trueValue;
		}
		
		public String compile(String fin, String dout, String command){
			// HERE WE START CONSTRUCTING THE COMPILE PROCESSES
			String filename="bank_"+new Random().nextInt(999999999)+".csv";
			List<String> added = new ArrayList<String>();
			CSVUtils csvUtil=new CSVUtils();
			String[][][] superContents={};
			String[][] LogData={};
			File[] listFiles=null;
			String filePath="";
			Object[] header={};
			String[] field={};
			File outDir=null;
			File inDir=null;
			// NOW WE START PROCESSING
			if(fin!=null && dout!=null){
				if(fin.length()>0 && dout.length()>0){
					if(command!=null){
						inDir=new File(fin);
						outDir=new File(dout);
						if(inDir.isDirectory() && outDir.isDirectory()){
							// Now we list files in the in directory to reread
							listFiles=Util.getFiles(inDir.listFiles());
							if(listFiles!=null){
								if(listFiles.length>0){
									superContents=new String[listFiles.length][][];
									CSVWriter writer=csvUtil.writer();
									CSVReader reader=csvUtil.reader();
									// Here we set the field and header
									switch(command){
									case "email":
										// Here code reside
										header=new Object[]{"Email", "Source", "Engine"};
										field=new String[]{"Email", "Source", "Engine"};
										break;
									case "url":
										header=new Object[]{"URL", "Term", "Engine"};
										field=new String[]{"Email", "Source", "Engine"};
									}
									// Now lets set the headers
									filePath=dout+filename;
									reader.header(field);
									// Here we loop to get content
									for(int i=0; i<listFiles.length; i++){
										if(listFiles[i].toString().indexOf(".csv")>=0){
											if(!added.contains(listFiles[i].toString())){
												reader.fIn(listFiles[i].toString().replace("\\", "/"));
												superContents[i]=reader.readCSV(field);
												added.add(listFiles[i].toString());
											}
										}
									} // end of loop
									
									// NOW LETS CHECK THE SUPER CONTENT AND PROCESS
									if(superContents!=null){
										for(int k=0; k<superContents.length; k++){
											// Now lets set the count
											LogData = (String[][])ArrayUtils.addAll(LogData, superContents[k]);
										} // end of loop
										
										// NOW WE CAN LOG DATA TO APPROPRAITE PATH
										writer.header(header);
										writer.fOut(filePath);
										writer.writeCSV(LogData);
									}
								}
							}
						}
					} // end of command check
				} // end of second check
			} // end of first check
			
			// Here we return
			return filePath;
		}
		
		// END OF SUBCLASS
	}
	
	
	/**************** HERE WE CONSTRUCT THE EMAIL FILTER SUB CLASS *****************/
	public class Filter{
		public String[] nameFilter(String[] emails){
			// HERE WE START PROCESSING THE NAME AND TYPE OF FILTER
			List<String> filtered = new ArrayList<String>();
			String[] returnData = {};
			String[] rejected = {};
			String[] filter = {"bot", "spider", "craw", 
					"scrap", "engine", ".png", ".jpg", "jpeg",  
					".gif", ".mp4", ".mp3", ".audio", 
					"feeder", "@google", "@quora", "@ecosy",
					"@reddit", "@facebook", "@twitter", "support",
					"info", "no-reply", "-agent"};
			
			// NOW LETS START PROCESSING
			if(emails!=null){
				if(emails.length>0){
					rejected = rejected(emails, filter);
					// Now lets check rejected
					if(rejected!=null && rejected.length>0){
						for(int i=0; i<emails.length; i++){
						// Now lets do filter
							if(!Arrays.asList(rejected).contains(emails[i])){
								// Here we check if email already exist
								filtered.add(emails[i].toLowerCase());
							}
						} // end of loop
					}
				}
			}
			
			// NOW LETS CHECK FILTERED BEFORE WE RETURN
			if(filtered.size()>0){
				returnData = filtered.toArray(new String[filtered.size()]);
			}
			else{
				returnData = emails;
			}
			
			// Here we return
			return returnData;
		}
		
		
		public String[] rejected(String[] emails, String[] filter){
			List<String> rejects = new ArrayList<String>();
			String[] returnData = {};
			// HERE WE START PROCESSING
			if(emails!=null){
				if(emails.length>0){
					for(int i=0; i<emails.length; i++){
						// Now lets do filter
						for(int j=0; j<filter.length; j++){
							if(emails[i].indexOf(filter[j])>=0){
								rejects.add(emails[i]);
							}
						} // end of loop
					} // end of loop
				}
			}
			
			// NOW LETS CHECK FILTERED BEFORE WE RETURN
			if(rejects.size()>0){
				returnData = rejects.toArray(new String[rejects.size()]);
			}
			else{
				returnData = null;
			}
						
			// Here we return
			return returnData;
		}
		
		
		public String[] validFilter(String[] emails){
			// HERE WE CHECK IF EMAIL EXISTS
			List<String> validated = new ArrayList<String>();
			ValidateEmail valid = new ValidateEmail();
			String[] returnData = {};
			// HERE WE START PROCESSING
			if(emails!=null){
				if(emails.length>0){
					for(int i=0; i<emails.length; i++){
						if(valid.checkValid(emails[i])){
							validated.add(emails[i].toLowerCase());
						}
					}
				}
			}
			
			// NOW LETS CHECK FILTERED BEFORE WE RETURN
			if(validated.size()>0){
				returnData = validated.toArray(new String[validated.size()]);
			}
			else{
				returnData = null;
			}
						
			// Here we return
			return returnData;
		}
	}
	
	// END OF OUTER CLASS
}



/******************* CONSTRUCTING THE EMAIL VALIDATOR CLASS ************************/
class ValidateEmail {
	// HERE WE CONSTRUCT CLASS
	public ValidateEmail(){};
	
	
	/*********** HERE WE CONSTRUCT THE TESTER METHODS *****************/
	private static int hear( BufferedReader in ) throws IOException {
		String line = null;
		int res = 0;
		while ( (line = in.readLine()) != null ) {
			String pfx = line.substring( 0, 3 );
			try {
				res = Integer.parseInt( pfx );
			} 
			catch (Exception ex) {
				res = -1;
			}
			if ( line.charAt( 3 ) != '-' ) break;
		}
		return res;
     }
	
	private static void say( BufferedWriter wr, String text ) throws IOException {
		wr.write( text + "\r\n" );
		wr.flush();
		return;
     }
	
	/*********** CONSTRUCTING THE DNS CHECKER METHOD ***************/
	@SuppressWarnings({ "unchecked", "rawtypes" })
   	private static ArrayList getMX( String hostName )throws NamingException {
		// Perform a DNS lookup for MX records in the domain
		Hashtable env = new Hashtable();
		env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
		DirContext ictx = new InitialDirContext( env );
		javax.naming.directory.Attributes attrs = ictx.getAttributes( hostName, new String[] { "MX" });
		javax.naming.directory.Attribute attr = attrs.get( "MX" );
		// if we don't have an MX record, try the machine itself
		if (( attr == null ) || ( attr.size() == 0 )) {
			attrs = ictx.getAttributes( hostName, new String[] { "A" });
			attr = attrs.get( "A" );
			if( attr == null ) 
				throw new NamingException( "No match for name '" + hostName + "'" );
		}
		// Huzzah! we have machines to try. Return them as an array list
		// NOTE: We SHOULD take the preference into account to be absolutely
		// correct. This is left as an exercise for anyone who cares.
		ArrayList res = new ArrayList();
		NamingEnumeration en = attr.getAll();
		while ( en.hasMore() ) {
			String x = (String) en.next();
			String f[] = x.split( " " );
			if ( f[1].endsWith( "." ) ) 
				f[1] = f[1].substring( 0, (f[1].length() - 1));
				res.add( f[1] );
		}
		return res;
    }
	
	/************ CONSTRUCTING THE MAIL CHECKER METHOD ************/
	@SuppressWarnings({ "rawtypes", "resource" })
	public static boolean isAddressValid( String address ) {
		// Find the separator for the domain name
		int pos = address.indexOf( '@' );
		// If the address does not contain an '@', it's not valid
		if ( pos == -1 ) return false;
		// Isolate the domain/machine name and get a list of mail exchangers
		String domain = address.substring( ++pos );
		ArrayList mxList = null;
		try {
			mxList = getMX( domain );
		} 
		catch (NamingException ex) {
			return false;
		}
		// Just because we can send mail to the domain, doesn't mean that the
		// address is valid, but if we can't, it's a sure sign that it isn't
		if ( mxList.size() == 0 ) return false;
		// Now, do the SMTP validation, try each mail exchanger until we get
		// a positive acceptance. It *MAY* be possible for one MX to allow
		// a message [store and forwarder for example] and another [like
		// the actual mail server] to reject it. This is why we REALLY ought
		// to take the preference into account.
		for ( int mx = 0 ; mx < mxList.size() ; mx++ ) {
			boolean valid = false;
			try {
				int res;
				Socket skt = new Socket( (String) mxList.get( mx ), 25 );
				BufferedReader rdr = new BufferedReader( new InputStreamReader( skt.getInputStream() ) );
				BufferedWriter wtr = new BufferedWriter( new OutputStreamWriter( skt.getOutputStream() ) );
				res = hear( rdr );
				if ( res != 220 ) throw new Exception( "Invalid header" );
				say( wtr, "EHLO orbaker.com" );
				res = hear( rdr );
				if ( res != 250 ) throw new Exception( "Not ESMTP" );
				// validate the sender address  
				say( wtr, "MAIL FROM: <tim@orbaker.com>" );
				res = hear( rdr );
				if ( res != 250 ) throw new Exception( "Sender rejected" );
				say( wtr, "RCPT TO: <" + address + ">" );
				res = hear( rdr );
				// be polite
				say( wtr, "RSET" ); hear( rdr );
				say( wtr, "QUIT" ); hear( rdr );
				if ( res != 250 ) throw new Exception( "Address is not valid!" );
				valid = true;
				rdr.close();
				wtr.close();
				skt.close();
			} 
			catch (Exception ex) {
				// Do nothing but try next host
			} 
			finally {
				if ( valid ) return true;
			}
		}
		return false;
     }
   
	
	/************ HERE WE CONSTRUCT THE RETURNER METHOD **************/
   	public  boolean checkValid( String email ) {
   		String testData[] = {email};
   		boolean isValid=false;
   		for ( int ctr = 0 ; ctr < testData.length ; ctr++ ) {
   			isValid=isAddressValid( testData[ ctr ] );
   		}
   		return isValid;
   	}
   	
   	// END OF CLASS
}