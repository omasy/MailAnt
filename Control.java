package system.ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.filechooser.FileFilter;

import system.soft.control.AntResource;
import system.soft.processor.FileManager;
import system.soft.processor.SetBase;

public class Control {
	// HERE WE SET PUBLIC VARIABLES
	public String TemplatePath;
	AntResource resource;
	FileManager Manager;
	SetBase Setting;
	JFrame Frame;
	// HERE WE CONSTRUCT THE CONTROL CLASS
	public Control(JFrame frame){
		Frame=frame;
		Setting = new SetBase();
		Manager = new FileManager(Setting);
		resource = new AntResource();
	}
	
	
	/**************** SETTING THE EXIT METHOD ******************************/
	public void exit(){
		new SystemProcess().exit();
	}
	
	
	/**************** RETURING THE JFRAME FOR REINITIATE ******************/
	public JFrame getFrame(){
		return Frame;
	}
	
	
	/** SETTING THE IMPORTER AND EXPORTER METHOD * @throws URISyntaxException **/
	public int importer(String command, String mode) throws URISyntaxException{
		return new SystemProcess().importer(command, mode);
	}
	
	
	public int exporter(String command) throws URISyntaxException{
		return new SystemProcess().exporter(command);
	}
	
	
	/**************** SETTING OTHER FUNCTIONALITIES ************************/
	public int saveSetting(List<JTextField> inputs, List<String> db, String[] setFields){
		return new SystemProcess().saveSetting(inputs, db, setFields);
	}
	
	
	public void template() throws URISyntaxException{
		new SystemProcess().template();
	}
	
	
	public boolean connection(){
		return new SystemProcess().connection();
	}
	
	
	public OptionDialogs alert(){
		return new ModalProcess().message();
	}
	
	
	public String build() throws URISyntaxException{
		return new SystemProcess().buildMail();
	}
	
	/************** CONSTRUCTING THE MODAL PROCESS SUBCLASS ****************/
	public class ModalProcess{
		public OptionDialogs message(){
			return new OptionDialogs();
		}
		
		public String fileDialog(String command){
			// HERE WE START CONSTRUCTING THE OPEN DIALOG
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Choose file");
			String filePath="";
			File fileToOpen;
			// Here lets set some variables on file
			fileChooser.setAcceptAllFileFilterUsed(false);
			if(command.equals("export")){
				fileChooser.addChoosableFileFilter(new OpenFileFilter("zip","Compressed Zip Folder"));
			}
			else if(command.equals("import")){
				fileChooser.addChoosableFileFilter(new OpenFileFilter("csv","Comma Separated Value") );
			}
			else if(command.equals("import-t")){
				fileChooser.addChoosableFileFilter(new OpenFileFilter("html","Web Page") );
			}
			// Demonstrate "Open" dialog:
			int rVal = fileChooser.showOpenDialog(Frame);
			// HERE WE CAN SAFELY GET FILE
		    if (rVal == JFileChooser.APPROVE_OPTION) {
		    	fileToOpen = fileChooser.getSelectedFile();
		    	filePath = fileToOpen.getAbsolutePath().replace("\\", "/");
		    }
		    
			// Here we return string
			return filePath;
		}
		
		public int chooseTemplate(){
			// HERE WE START CONSTRUCTING THE CHOOSE DIALOG
			
			// Here we return
			return 0;
		}
		
		public int uploadTemplate(){
			
			// Here we return
			return 0;
		}
		
		public void progress(){
			
		}
	}
	
	
	/************** CONSTRUCTING THE SYSTEM PROCESS SUBCLASS ****************/
	public class SystemProcess{
		public void exit(){
			// HERE WE CONSTRUCT THE SYSTEM EXIT
			System.exit(0);
		}
		
		public int importer(String command, String mode) throws URISyntaxException{
			// HERE WE START CONSTRUCTING THE DOWNLOAD INFO PROCESS
			String filePath = new ModalProcess().fileDialog(mode);
			String[] extensions = {};
			String newPath = "sub_"+new Random().nextInt(999999999)+"/";
			String filename="bank_"+new Random().nextInt(999999999);
			String extension = "";
			String intDir = "";
			int uploaded = 0;
			String confirm = "";
			// NOW LETS START PROCESSING
			if(filePath.length()>0 && filePath.indexOf("/")>=0){
				// Here lets select file source
				switch(command){
				case "bank":
					// Setting internal directory
					intDir=resource.get(Setting.get("sendDir"));
					extensions = new String[]{"csv", "xls"};
					break;
				case "template":
					// Setting internal directory
					intDir=resource.get(Setting.get("template"));
					intDir=intDir.substring(0, intDir.lastIndexOf("/")+1);
					extensions = new String[]{"html"};
				}
				// Here lets save file
				intDir+=newPath;
				extension=filePath.substring(filePath.lastIndexOf(".")+1, filePath.length());
				filename+="."+extension;
				File dir = new File(intDir);
				if(!dir.exists()){
					dir.setReadable(true, false);
					dir.setWritable(true, false);
					dir.mkdir();
				}
				intDir+=filename;
				// Now lets start saving file
				if(Arrays.asList(extensions).contains(extension)){
					confirm = Manager.importer(filePath, intDir);
					if(confirm!=null && confirm.indexOf("/")>=0){
						uploaded = 1;
						TemplatePath = intDir;
					}
				}
				else{
					new OptionDialogs().errorAlart("Error Notification", "File not supported!");
				}
			}
			else{
				new OptionDialogs().errorAlart("Error Notification", "Unknown error occured while trying to download File");
			}
			// Here we return
			return uploaded;
		}
		
		public int exporter(String command) throws URISyntaxException{
			// HERE WE START CONSTRUCTING THE DOWNLOAD INFO PROCESS
			String filePath = new ModalProcess().fileDialog("export");
			String intDir = "";
			String filename = "";
			int downloaded = 0;
			String confirm = "";
			// NOW LETS START PROCESSING
			if(filePath.length()>0 && filePath.indexOf("/")>=0){
				// Here lets select file source
				switch(command){
				case "bank":
					// Setting internal directory
					intDir=resource.get(Setting.get("mailDir"));
					break;
				case "info":
					// Setting internal directory
					intDir=resource.get("log/about/");
				}
				// Here lets save file
				filename=filePath.substring(filePath.lastIndexOf("/"), filePath.length());
				filePath=filePath.substring(0, filePath.lastIndexOf("/"));
				confirm = Manager.exporter(intDir, filePath, filename);
				if(confirm!=null && confirm.indexOf("/")>=0){
					downloaded = 1;
				}
			}
			else{
				new OptionDialogs().errorAlart("Error Notification", "Unknown error occured while trying to download File");
			}
			// Here we return
			return downloaded;
		}
		
		public void template() throws URISyntaxException{
			// HERE WE CONSTRUCT THE SYSTEM THAT LOGS THE CHOOSEN TEMPLATE
			String Title = "Template Action";
			String Message = "Choose Template Action!";
			String[] option = {"Choose Template", "Upload Template"};
			String icon = "data/img/template.png";
			int mode = new OptionDialogs().optionAlart(Title, Message, option, icon);
			// Here more variable resides
			String name = "";
			String input = "";
			String output = "";
			String[][] LogData = {};
			Object[] header = {"Name", "Path"};
			String logPath = resource.get(Setting.get("tmplLog"));
			boolean isFileCheck = false;
			int uploadCheck = 0;
			int check = 0;
			// NOW LETS START PROCESSING
			if(mode>=0){
				if(mode==0){
					// Here we call choose modal
					chooseDialog upload = new chooseDialog(Frame, "Choose Template", "Choose template from the dropdown");
					upload.setVisible(true);
				}
				else if(mode==1){
					// Here we call upload modal
					uploadCheck = importer("template", "import-t");
					// Now lets check the path
					if(uploadCheck > 0){
						input = new OptionDialogs().inputAlart("Template Name");  
						// Here we check input to make sure we got data
						if(input!=null && input.length()>0){
							// Here we first save template
							name = input;
						}
						else{
							// Here we set a default name
							name = "Template_"+new Random().nextInt(999999999);
						}
						
						// Here let us now save our files
						if(TemplatePath!=null){
							if(TemplatePath.indexOf(".html")>=0){
								// Now we can save
								LogData = new String[1][2];
								LogData[0][0]=name;
								LogData[0][1]=TemplatePath;
								
								// Now Lets Log Template
								if(new File(logPath).isFile()){
									if(new File(logPath).exists()){
										isFileCheck = true;
									}
								}
								
								// Here we check to know what to do
								if(isFileCheck){
									// Here we edit
									check = Manager.uniEditor(logPath, LogData);
									if(check>0){
										Setting.set("tmplLog", logPath);
										new OptionDialogs().warningAlart("Template Manager", "Template uploaded sucessfully!");
									}
								}
								else{
									// Here we log
									output = Manager.uniWriter(logPath, LogData, header);
									if(output!=null && output.indexOf(".csv")>=0){
										Setting.set("tmplLog", output);
										new OptionDialogs().warningAlart("Template Manager", "Template uploaded sucessfully!");
									}
								}
							}
						}
					}
				}
			}
			else{
				new OptionDialogs().errorAlart("Error Notification", "Operation Cancelled");
			}
		}
		
		public int saveSetting(List<JTextField> inputs, List<String> db, String[] setFields){
			// HERE WE START CONSTRUCTING SETTING SAVE PROCESS
			List<String> fieldValues = new ArrayList<String>();
			String[] values={};
			int setted = 0;
			// HERE WE START PROCESSING
			if(!inputs.isEmpty() && !db.isEmpty()){
				if(setFields!=null && setFields.length>0){
					// Here we get texts from the textfield to ensure its same
					// with the database list to avoid heavy work
					for(int i=0; i<inputs.size(); i++){
						fieldValues.add(inputs.get(i).getText());
					} // end of loop
				
					// Now lets check
					if(fieldValues.equals(db)){
						new OptionDialogs().warningAlart("Setting Control", "No change made to setting!");
					}
					else{
						// Here user has changed the content of setting
						values=fieldValues.toArray(new String[fieldValues.size()]);
						try {
							setted=Setting.setAll(setFields, values);
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			
			// Here we return
			return setted;
		}
		
		public String buildMail() throws URISyntaxException{
			// HERE WE CONSTRUCT THE COMPILE PROCESS
			String logPath = resource.get(Setting.get("extLog"));
			String sendDir = resource.get(Setting.get("sendDir"));
			File logFile = new File(logPath);
			String bankPath = null;
			String basePath = null;
			String[][] data = {};
			int check = 0;
			// HERE WE START PROCESSING
			if(logFile.exists()){
				data = Manager.quickData(logPath, "log");
				if(data!=null && data.length>0){
					basePath = data[data.length-1][2];
					if(basePath.length()>=0 && basePath.indexOf("/")>=0){
						if(new File(basePath).exists()){
							bankPath = Manager.compile(basePath, sendDir, "email");
							if(bankPath!=null && bankPath.length()>0){
								if(bankPath.indexOf(".csv")>=0){
									check = Manager.delete(basePath);
									if(check>0){
										check = Manager.delete(logPath);
									}
								}
							}
						}
					}
				}
			}
			// Here we return
			return bankPath;
		}
		
		public boolean connection(){
			try {
                //make a URL to a known source
                URL url = new URL("http://www.google.com");

                //open a connection to that source
                HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();

                //trying to retrieve data from the source. If there
                //is no connection, this line will fail
                urlConnect.getContent();

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
            return true;
		}
		
		// END OF INNER CLASS
	}
	
	
	
	/****************** CONSTRUCTING OPTION PANE INNER CLASS *******************/
	class OptionDialogs{
		public int confirmAlart(String message){
			// HERE WE CONSTRUCT THE CONFIRM BUTTON
			// feedback yes: 1, no: 2, cancel: -1; 
			int feedback=0;
			int a=JOptionPane.showConfirmDialog(Frame, message); 
			// Here we set the feedback
			if(a==JOptionPane.YES_OPTION){  
				feedback=1;
			}
			else if(a==JOptionPane.NO_OPTION){
				feedback=2;
			}
			else if(a==JOptionPane.CANCEL_OPTION){
				feedback=-1;
			}
			// Here we return int
			return feedback;
		}
		
		public int optionAlart(String title, String message, String[] options, String iconPath){
			// HERE WE CONSTRUCT THE OPTION ALART
			ImageIcon icon = new ImageIcon(iconPath);
	        int feedBack = JOptionPane.showOptionDialog(Frame, message, title, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, icon, options, options[0]);
			
	        // Here we return int
			return feedBack;
		}
		
		public void warningAlart(String title, String message){
			// HERE WE START CONSTRUCTING THE MESSAGE PROCESS
			int messageType = JOptionPane.WARNING_MESSAGE;
			JOptionPane.showMessageDialog(Frame, message, title, messageType);
		}
		
		public void errorAlart(String title, String message){
			int messageType = JOptionPane.ERROR_MESSAGE;
			JOptionPane.showMessageDialog(Frame, message, title, messageType);
		}
		
		public String inputAlart(String message){
			String input = JOptionPane.showInputDialog(Frame, message);
			// Here we return
			return input;
		}
	}
	
	
	
	/****************** HERE WE CONSTRUCT THE FILTER CLASS ************************/
	class OpenFileFilter extends FileFilter {

	    String description = "";
	    String fileExt = "";

	    public OpenFileFilter(String extension) {
	        fileExt = extension;
	    }

	    public OpenFileFilter(String extension, String typeDescription) {
	        fileExt = extension;
	        this.description = typeDescription;
	    }

	    @Override
	    public boolean accept(File f) {
	        if (f.isDirectory())
	            return true;
	        return (f.getName().toLowerCase().endsWith(fileExt));
	    }

	    @Override
	    public String getDescription() {
	        return description;
	    }
	}
	
	
	
	/******************* CONSTRUCTION THE UPLOAD DIALOG ***********************/
	class chooseDialog extends Dialog {
		private static final long serialVersionUID = 1L;
		String LogPath;
		boolean isChoosable = false;
		@SuppressWarnings("rawtypes")
		JComboBox options;
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public chooseDialog(JFrame parent, String title, String message) {
			super(parent, title);
			// set the position of the window
			try {
				LogPath = resource.get(Setting.get("tmplLog"));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Point p = new Point(550, 200);
			setLocation(p.x, p.y);

			// Create a message
			JPanel messagePane = new JPanel();
			// Here we create combo box
			if(new File(LogPath).exists()){
				if(LogPath.indexOf("template")>=0 && LogPath.indexOf(".csv")>=0){
					isChoosable = true;
					options = new JComboBox(getData());
					options.setBounds(50, 100,90,20);
					messagePane.add(new JLabel(message), BorderLayout.NORTH);
					messagePane.add(options, BorderLayout.SOUTH);
				}
				else{
					messagePane.add(new JLabel("Sorry there's no template to choose!"), BorderLayout.NORTH);
				}
			}
			else{
				messagePane.add(new JLabel("Sorry there's no template to choose!"), BorderLayout.NORTH);
			}
			// get content pane, which is usually the
			// Container of all the dialog's components.
			add(messagePane);

			// Create a button
			JPanel buttonPane = new JPanel();
			JButton button = new JButton("Choose");
			buttonPane.add(button);
			// set action listener on the button
			button.addActionListener(new MyActionListener());
			add(buttonPane, BorderLayout.PAGE_END);
			pack();
			setSize(300, 150);
			
			// Here we set closing operation
			addWindowListener(new WindowAdapter() {
	            public void windowClosing(WindowEvent windowEvent){
	               dispose();
	            }
	         });
		}

		// override the createRootPane inherited by the JDialog, to create the rootPane.
		// create functionality to close the window when "Escape" button is pressed
		public JRootPane createRootPane() {
			JRootPane rootPane = new JRootPane();
			KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
			Action action = new AbstractAction() {
				
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					dispose();
				}
			};
			InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
			inputMap.put(stroke, "ESCAPE");
			rootPane.getActionMap().put("ESCAPE", action);
			return rootPane;
		}
		
		
		// Constructing the data fetcher and sum
		public String[] getData(){
			String[] contents = {};
			String[][] csvData = Manager.quickData(LogPath, "template");
			if(csvData!=null && csvData.length>0){
				contents = new String[csvData.length];
				for(int i=0; i<contents.length; i++){
					contents[i] = csvData[i][0]+" --> "+i;
				}
			}
			
			// Here we return content
			return contents;
		}
		
		// an action listener to be used when an action is performed
		// (e.g. button is pressed)
		class MyActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				// Here we run basic functionalities to save
				// Settings to the xml file
				int index = 0;
				String path = "";
				String[][] data = {};
				String[] itempart = {};
				if(isChoosable){
					String item = options.getItemAt(options.getSelectedIndex()).toString();
					if(item!=null && item.indexOf("-->")>=0){
						itempart=item.split("\\-->");
						index=Integer.parseInt(itempart[1].replace(" ", ""));
						// Now lets get the template log
						data=Manager.quickData(LogPath, "template");
						if(data!=null && data.length>0){
							path=data[index][1];
							if(path!=null && path.indexOf(".htm")>=0){
								try {
									Setting.set("template", path);
									new OptionDialogs().warningAlart("Template Manager", "Template loaded successfully!");
									// Now lets close
									setVisible(false);
									dispose();
								} catch (URISyntaxException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	      
	}
	
	// END OF MAIN CLASS
}
