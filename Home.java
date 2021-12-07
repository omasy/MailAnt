package system.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.ArrayUtils;

import system.soft.processor.ExtractManager;
import system.soft.processor.FileManager;
import system.soft.processor.SetBase;

@SuppressWarnings("serial")
public class Home {
	// HERE WE SET PUBLIC VARIABLES
	private JPanel HomePanel = new JPanel(new BorderLayout());
	private boolean Start = false;
	private boolean FirstRun = false;
	private Control control;
	private String Queries;
	private int TotalFoundURL;
	private int TotalParseURL;
	private String[][] FoundITEMS;
	private String[] NewData;
	private String BuildPath;
	// Setting other variables
	DefaultTableModel Model;
	JTextArea QueryBox;
	JTextField Fitem;
	JTextField Furl;
	UIHelper helper;
	SetBase Setting;
	JLabel Status;
	JTable Table;
	JButton StartBut;
	JButton ClearBut;
	JButton StopBut;
	// HERE WE CONSTRUCT MENU CLASS
	public Home(Control cntrl) {
		this.control = cntrl;
		Setting = new SetBase();
		// Here we call the wizard
		try {
			wizard();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	  
	  
	/********* CONSTRUCTING DESIGN WIZARD METHOD * @throws IOException **********/
	private void wizard() throws IOException{
		// HERE WE START CONSTRUCTING THE PANEL DESIGN PROCESS
		helper = new UIHelper();
		
		// HERE WE START PROCESSING
		JToolBar toolBar = new JToolBar("Still draggable");
		JPanel mainPane = new JPanel(new BorderLayout());
		JPanel headPane = new JPanel(new BorderLayout());
		JPanel bodyPane = new JPanel(new BorderLayout());
		JPanel statusPane = new JPanel(new FlowLayout(FlowLayout.RIGHT,  5, 5));
		    
		JPanel menuPane = new JPanel();
		
		// Setting Background for footpane
		JPanel footPane = new JPanel(new BorderLayout()){
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				int w = getWidth();
				int h = getHeight();
				Color color1 = Color.decode("#7ec0ee");
				Color color2 = Color.decode("#87cefa");
				GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
				g2d.setPaint(gp);
				g2d.fillRect(0, 0, w, h);
			}
		};
		
		JPanel boxPane = new JPanel(new BorderLayout()){
		    @Override
		    protected void paintComponent(Graphics g) {
		    	super.paintComponent(g);
		        Graphics2D g2d = (Graphics2D) g;
		        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		        int w = getWidth();
		        int h = getHeight();
		        Color color1 = Color.decode("#7ec0ee");
		        Color color2 = Color.decode("#87cefa");
		        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
		        g2d.setPaint(gp);
		        g2d.fillRect(0, 0, w, h);
		    }
		};
		 
		// Here we set the menu panel
		menuPane = new JPanel(new FlowLayout(FlowLayout.LEFT,  5, 5)){
		    @Override
		    protected void paintComponent(Graphics g) {
		    	super.paintComponent(g);
		        Graphics2D g2d = (Graphics2D) g;
		        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		        int w = getWidth();
		        int h = getHeight();
		        Color color1 = Color.decode("#7ec0ee");
		        Color color2 = Color.decode("#87cefa");
		        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
		        g2d.setPaint(gp);
		        g2d.fillRect(0, 0, w, h);
		    }
		};
		
		boxPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		footPane.setBorder(new EmptyBorder(4, 4, 4, 4));
		statusPane.setOpaque(false);
		
		Status = new JLabel("");
		JLabel foundUrl = new JLabel("Found Urls ");
		JLabel foundItems = new JLabel("Found Items ");
		    
		Furl = helper.textField("", 3);
		Fitem = helper.textField("", 3);
		    
		statusPane.add(foundUrl);
		statusPane.add(Furl);
		statusPane.add(foundItems);
		statusPane.add(Fitem);
		    
		Dimension d = Status.getPreferredSize();
		Status.setPreferredSize(new Dimension(d.width+100,d.height));
		Status.setFont(Status.getFont().deriveFont(Font.PLAIN));
		foundUrl.setFont(foundUrl.getFont().deriveFont(Font.PLAIN));
		foundItems.setFont(foundItems.getFont().deriveFont(Font.PLAIN));
		foundItems.setBorder(new EmptyBorder(0, 5, 0, 0));
		    
		JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, Status, statusPane);
		jsp.setOpaque(false);
		jsp.setBorder(null);
		jsp.setResizeWeight(.80d);
		jsp.setDividerSize(0);
		footPane.add(jsp);
		    
		StartBut = helper.buttonMake("Start", "data/img/start.png");
		StartBut.setActionCommand("START");
		StartBut.addActionListener(new ActionTaken());
		
		StopBut = helper.buttonMake("Stop", "data/img/Stop.png");
		StopBut.setActionCommand("STOP");
		StopBut.addActionListener(new ActionTaken());
		StopBut.setEnabled(false);
		
		ClearBut = helper.buttonMake("Clear", "data/img/clear.png");
		ClearBut.addActionListener(new SubAction());
		ClearBut.setEnabled(false);
		    
		    
		toolBar.setBorderPainted(false);
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		toolBar.setOpaque(false);
		    
		toolBar.add(StartBut);
		toolBar.add(helper.separator("#87cefa", 1));
		toolBar.add(StopBut);
		toolBar.add(helper.separator("#87cefa", 1));
		toolBar.add(ClearBut);
		    
		menuPane.add(toolBar);
		    
		JLabel searchLabel= helper.label("Search Keywords", 0, 4);
		    
		QueryBox = helper.textArea("");
		JScrollPane sp1 = new JScrollPane(QueryBox);
		sp1.setSize(new Dimension(200,100));
		sp1.setPreferredSize(new Dimension(200,100));
		    
		boxPane.add(searchLabel, BorderLayout.NORTH);
		boxPane.add(sp1, BorderLayout.CENTER);

		String[] titles = {"#", "Item", "Source", "Search Engine" };
		String[][] data = {};
		    
		Model = new DefaultTableModel(data, titles);
		Table = helper.table(Model, "#7ec0ee", "Extracted Emails");
		    
		bodyPane.add(new JScrollPane(Table), BorderLayout.CENTER);

		// JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp1, rightPane);
		headPane.add(menuPane, BorderLayout.NORTH);
		headPane.add(boxPane, BorderLayout.CENTER);

		// Here we add the main pane
		mainPane.add(headPane, BorderLayout.NORTH);
		mainPane.add(bodyPane, BorderLayout.CENTER);
		mainPane.add(footPane, BorderLayout.SOUTH);
		    
		// Now lets add to the frame
		HomePanel.add(mainPane, BorderLayout.CENTER);
	}
	  
	
	/****************** HERE WE RETURN HOME PANEL METHOD *********************/
	public JPanel getHome() {
		// Here we return home panel
		return HomePanel;
	}
	
	
	/******************* CONSTRUCTING THE EMAIL SENDER METHOD *****************/
	private void extractor(){
		// HERE WE START CONSTRUCTING THE EXTRACTOR PROCESS
		ExtractManager manager = new ExtractManager(Setting);
		// Create a new Thread to do the counting
  	  	Thread t = new Thread() {
  	  		@Override
  	  		public void run() { 
  	  			// Here we construct all our major processes
  	  			// Here we set to base on user option
  	  			for(int i=0; i<1; i++){
  	  				if(!Start){
  	  					break; // We break if start is false
  	  				}
  	  				
  	  				// Now lets get our search Query
  	  				Queries = QueryBox.getText();
  	  				// If found mail is null we break
  	  				if(Queries!=null && Queries.length()>0){
  	  					// Here we set display variable
  	  					Status.setText("Waiting for Google...");
  	  					Furl.setText("0");
  	  					Fitem.setText("0");
  	  					
  	  					// Here lets validate Emails
  	  					Status.setText("Parsing found URL (please wait...)");
  	  					// Now we start here we find url and parse
  	  					manager.keyword(Queries);
  	  					manager.manager(manager);
  	  					TotalFoundURL = manager.TOTALFOUNTURL;
  	  					TotalParseURL = manager.TOTALPARSEDURL;
  	  					FoundITEMS = manager.EXTRACTED;
  	  					// Now lets get other parameters
  	  					if(TotalFoundURL>0){
  	  						Furl.setText(String.valueOf(TotalFoundURL));
  	  						// Now lets get found Emails
  	  						if(FoundITEMS!=null){
  	  							if(FoundITEMS.length>0){
  	  								// Here we update table with new records
  	  								for(int t=0; t<FoundITEMS.length; t++){
  	  									NewData = (String[])ArrayUtils.addAll(new String[]{String.valueOf((t+1))}, FoundITEMS[t]);
  	  									Model.addRow(NewData);
  	  								}
  	  								Fitem.setText(String.valueOf(FoundITEMS.length));
  	  								Status.setText("Parsing: "+manager.TARGETONURL);
  	  								// HERE WE CHECK IF TASK IS COMPLETE
  	  								if(TotalParseURL >= TotalFoundURL){
  	  									// HERE WE COMPILE THE EXTRACTED EMAIL IN ONE FILE
  	  									try {
  	  										// Here we check if we have connected before
  	  										if(FirstRun){
  	  											JFrame current = control.getFrame();
  	  											control = new Control(current);
  	  										}
  	  										// Here we build document
											BuildPath = control.build();
											if(BuildPath!=null){
	  	  										if(BuildPath.indexOf(".csv")>=0){
	  	  											Start = false;
	  	  											Status.setText("Email Extraction completed");
	  	  											control.alert().warningAlart("Extraction Manager", "Extraction Ended - Emails @ "+BuildPath);
	  	  											StartBut.setEnabled(true);
	  	  											StopBut.setEnabled(false);
	  	  											ClearBut.setEnabled(true);
	  	  										}
	  	  									}
										} catch (URISyntaxException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
  	  								}
  	  							}
  	  						}
  	  					}
  	  				}
  	  				else{
  	  					break; // Here we break process
  	  				}
  	  				// Also provide the necessary delay.
  	  				try {
  	  					sleep(100);
  	  				} catch (InterruptedException ex) {}
  	  			} // end of loop
  	  		}
  	  	};
  	  	t.start();
	}
	
	
	/****************** CONSTRUCTING THE CLEAR METHOD *************************/
	private void clear(){
		// HERE WE START PROCESSING THE CLEAR FUNCTIONALITY
		FileManager manager = new FileManager(Setting);
		String logPath = Setting.get("extLog");
		File logFile = new File(logPath);
		// NOW LETS CLEAR TABLES AND DATAS
		Model.setRowCount(0);
		Status.setText("");
		Furl.setText("0");
		Fitem.setText("0");
		// Now lets clear log
		if(logFile.exists()){
			manager.clear(logPath);
		}
	}
	
	
	/****************** CONSTRUCTING THE ACTION EVEN CLASS ********************/
	class ActionTaken implements ActionListener {
	      public void actionPerformed(ActionEvent e) {
	    	  String command = e.getActionCommand();
	    	  // Here we check command to set start appropriately
	    	  switch(command){
	    	  case "START":
	    		  // Here we start thread running
	    		  Start = true;
	    		  StopBut.setEnabled(true);
	    		  ClearBut.setEnabled(true);
	    		  StartBut.setEnabled(false);
	    		  break;
	    	  case "STOP":
	    		  // Here we stop thread running
	    		  Start = false;
	    		  StartBut.setEnabled(true);
	    		  StopBut.setEnabled(false);
	    	  }
	    	  
	    	  // HERE WE START OUR SEND COMMAND GO ON
	    	  // When send is clicked it keeps on sending till
	    	  // Stop button is click
	    	  if (Start){
	    		  // lets start sending
	    		  if(control.connection()){
	    			  extractor(); // calling our extractor
	    			  // Setting the first run
	    			  if(!FirstRun){
	    				  FirstRun = true;
	    			  }
	    		  }
	    		  else{
	    			  // Here display message
	    			  StartBut.setEnabled(true);
	    			  StopBut.setEnabled(false);
	    			  ClearBut.setEnabled(false);
	    			  control.alert().errorAlart("Internet Connection", "There's no internet connection!");
	    		  }
	    	  }
	    	  else{
	    		  control.alert().warningAlart("Extraction Manager", "Extraction Ended");
	    	  }
	      }
	      
	      // END OF ACTION CLASS
	}

	
	/****************** CONSTRUCTING THE SUBACTION CLASS *********************/
	class SubAction implements ActionListener {
	      public void actionPerformed(ActionEvent e) {
	    	  clear(); // calling the clear method
	    	  ClearBut.setEnabled(false);
	      }
	}
	
	// END OF MAIN CLASS
}
