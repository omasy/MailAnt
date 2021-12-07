package system.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import system.soft.processor.MailTransporter;
import system.soft.processor.SetBase;
import system.soft.processor.WebMaster;

@SuppressWarnings("serial")
public class Message {
	// HERE WE SET PUBLIC VARIABLES
	private JPanel MessagePanel = new JPanel(new BorderLayout());
	private static int SentMessages;
	private boolean Start = false;
	private boolean Enable = true;
	private String[] FoundMails;
	private String[] Filtered;
	private int ProcessCount = 0;
	private boolean Condition;
	private Control control;
	private int Process;
	UIHelper helper;
	SetBase Setting;
	// Setting other variables
	JLabel Status;
	JTextField To;
	JTextField Furl;
	JButton EnableTo;
	JTextField Fitem;
	JTextField Subject;
	JTextArea MessageBox;
	JButton SendBut;
	JButton StopBut;
	JButton TemplateBut;
	// HERE WE CONSTRUCT MENU CLASS
	public Message(Control cntrl) {
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
	
	/************ CONSTRUCTING DESIGN WIZARD METHOD * @throws IOException *************/
	private void wizard() throws IOException{
		// HERE WE START CONSTRUCTING THE PANEL DESIGN PROCESS
		helper = new UIHelper();
		
		// HERE WE START PROCESSING
		JToolBar toolBar = new JToolBar("Still draggable");
		JPanel mainPane = new JPanel(new BorderLayout());
		JPanel headPane = new JPanel(new BorderLayout());
		JPanel menuPane = new JPanel();
		JPanel statusPane = new JPanel(new FlowLayout(FlowLayout.RIGHT,  5, 5));
		JPanel toPane = new JPanel(new BorderLayout());
		JPanel enablePane = new JPanel(new BorderLayout());
		JPanel subjectPane = new JPanel(new BorderLayout());
		
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
		
		// Setting body pane backgroung
		JPanel bodyPane = new JPanel(new BorderLayout()){
		    @Override
		    protected void paintComponent(Graphics g) {
		    	super.paintComponent(g);
		        Graphics2D g2d = (Graphics2D) g;
		        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		        int w = getWidth();
		        int h = getHeight();
		        Color color1 = Color.decode("#87cefa");
		        Color color2 = Color.decode("#7ec0ee");
		        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
		        g2d.setPaint(gp);
		        g2d.fillRect(0, 0, w, h);
		    }
		};
		 
		// Setting box pane background
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
		bodyPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		footPane.setBorder(new EmptyBorder(4, 4, 4, 4));
		statusPane.setOpaque(false);
		
		toPane.setOpaque(false);
		enablePane.setOpaque(false);
		subjectPane.setOpaque(false);
		
		subjectPane.setBorder(new EmptyBorder(10, 0, 0, 0));
		
		Status = new JLabel();
		JLabel foundUrl = new JLabel("Total");
		JLabel foundItems = new JLabel("Sent");
		    
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
		    
		SendBut = helper.buttonMake("Send", "data/img/send.png");
		SendBut.setActionCommand("SEND");
		SendBut.addActionListener(new ActionTaken());
		
		StopBut = helper.buttonMake("Stop", "data/img/Stop.png");
		StopBut.setActionCommand("STOP");
		StopBut.addActionListener(new ActionTaken());
		StopBut.setEnabled(false);
		
		TemplateBut = helper.buttonMake("Template", "data/img/template.png");
		TemplateBut.setActionCommand("TEMPLATE");
		TemplateBut.addActionListener(new SubAction());
		    
		    
		toolBar.setBorderPainted(false);
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		toolBar.setOpaque(false);
		    
		toolBar.add(SendBut);
		toolBar.add(helper.separator("#87cefa", 1));
		toolBar.add(StopBut);
		toolBar.add(helper.separator("#87cefa", 1));
		toolBar.add(TemplateBut);
		    
		menuPane.add(toolBar);
		  
		// Setting the from and subject Text Field
		EnableTo = new JButton("Enable TO");
		EnableTo.setBackground(Color.ORANGE);
		EnableTo.setActionCommand("ENABLE");
		EnableTo.addActionListener(new SubAction());
        // these next two lines do the magic..
		EnableTo.setContentAreaFilled(false);
		EnableTo.setOpaque(true);
		
		JLabel descLabel = helper.label("Emails will be fetch from the mail bank by default; to change this enable the field Message TO", 4, 4);
		descLabel.setForeground(Color.decode("#666666"));
		
		JLabel toLabel = helper.label("Message TO", 0, 4);
		JLabel subjectLabel = helper.label("Message Subject", 0, 4);
		    
		To = helper.textField("", 0);
		Subject = helper.textField("", 0);
		
		// Lets add the to panel up
		To.setEditable(false);
		toPane.add(toLabel, BorderLayout.NORTH);
		toPane.add(To, BorderLayout.CENTER);
		
		enablePane.add(descLabel, BorderLayout.WEST);
		enablePane.add(EnableTo, BorderLayout.EAST);
		
		// Lets add up the subject pane
		subjectPane.add(subjectLabel, BorderLayout.NORTH);
		subjectPane.add(Subject, BorderLayout.CENTER);
		
		// Setting message layer
		JLabel messageLabel= helper.label("Write Message", 0, 4);
		
		MessageBox = helper.textArea("");
		JScrollPane sp2 = new JScrollPane(MessageBox);
		sp2.setSize(new Dimension(200,600));
		sp2.setPreferredSize(new Dimension(200,500));
		
		boxPane.add(enablePane, BorderLayout.NORTH);
		boxPane.add(toPane, BorderLayout.CENTER);
		boxPane.add(subjectPane, BorderLayout.SOUTH);

		// Here we add message box to body pane 
		bodyPane.add(messageLabel, BorderLayout.NORTH);
		bodyPane.add(sp2, BorderLayout.CENTER);

		// Adding Header Level Pane
		headPane.add(menuPane, BorderLayout.NORTH);
		headPane.add(boxPane, BorderLayout.CENTER);

		// Here we add the main pane
		mainPane.add(headPane, BorderLayout.NORTH);
		mainPane.add(bodyPane, BorderLayout.CENTER);
		mainPane.add(footPane, BorderLayout.SOUTH);
		    
		// Now lets add to the frame
		MessagePanel.add(mainPane, BorderLayout.CENTER);
	}
	 
	
	/******************* CONSTRUCTING THE EMAIL SENDER METHOD ******************/
	private void sender(){
		// HERE WE START CONSTRUCTING THE SENDER PROCESSES
		MailTransporter transport = new MailTransporter(Setting);
		WebMaster master = new WebMaster(Setting);
		int Process = Integer.parseInt(Setting.get("pt"));
		// Create a new Thread to do the counting
  	  	Thread t = new Thread() {
  	  		@Override
  	  		public void run() { 
  	  			// Here we construct all our major processes
  	  			// Here we set to base on user option
  	  			for(int i=0; i<Process; i++){
  	  				if(!Start){
  	  					break; // We break if start is false
  	  				}
  	  				
  	  				// Here we fetch our email data
  	  				if(To.isEditable()){
  	  					if(To.getText().length()>0 && To.getText().indexOf("@")>=0){
  	  						FoundMails = getTo(To);
  	  						Condition = false;
  	  					}
  	  					else{
  	  						FoundMails = master.get("email");
  	  						Condition = true;
  	  					}
  	  				}
  	  				else{
  	  					FoundMails =  master.get("email");
  	  					Condition = true;
  	  				}
  	  				
  	  				// If found mail is null we break
  	  				if(FoundMails!=null){
  	  					if(FoundMails.length>0){
  	  						// Here we set display variable
  	  	  					Status.setText("Initiating email sending...");
  	  	  					Furl.setText("0");
  	  	  					Fitem.setText("0");
  	  	  					
  	  	  					// Here lets validate Emails
  	  	  					Status.setText("Validating Email Addresses...");
  	  	  					Filtered = helper.mailFilter(FoundMails);
  	  	  					if(Filtered!=null){
  	  	  						if(Filtered.length>0){
  	  	  							// Now we start sending emails
  	  	  							Furl.setText(String.valueOf(Filtered.length));
  	  	    	  					transport.subject(Subject.getText());
  	  	    	  					transport.message(MessageBox.getText());
  	  	    	  					transport.condition(Condition);
  	  	    	  					transport.send(Filtered, transport); // send message
  	  	    	  					// Now lets get email datas
  	  	    	  					SentMessages=transport.SENTMESSAGES;
  	  	    	  					Fitem.setText(String.valueOf(SentMessages));
  	  	    	  					Status.setText("Sent "+SentMessages+" of "+Filtered.length+" emails");
  	  	    	  					if(SentMessages>=Filtered.length){
  	  	    	  						Start = false;
  	  	    	  						Status.setText("Email Batch "+(i+1)+" Sending Completed....");
  	  	    	  						ProcessCount+=i;
  	  	    	  						SendBut.setEnabled(true);
  	  	    	  						StopBut.setEnabled(false);
  	  	    	  					}
  	  	  						}
  	  	  					}
  	  					}
  	  					else{
  	  						control.alert().warningAlart("Sending Message", "Sorry no email was found in the mail Bank; please import your own emails to send.");
  	  						break; // Here we break process
  	  					}
  	  				}
  	  				else{
  	  					control.alert().warningAlart("Sending Message", "Sorry no email was found in the mail Bank; please import your own emails to send.");
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
	
	
	private String[] getTo(JTextField to){
		// HERE WE START CONSTRUCTING THE GET TO PROCESS
		String[] sendTo={};
		String toString="";
		if(to.isEditable()){
			toString=to.getText();
			if(toString.length()>0 && toString.indexOf("@")>=0){
				if(toString.indexOf(" ")>=0){
					sendTo=toString.split(" ");
				}
				else if(toString.indexOf(",")>=0){
					sendTo=toString.split(",");
				}
				else{
					sendTo=new String[]{toString};
				}
			}
		}
		// Here we return
		return sendTo;
		
	}
	
	
	/****************** CONSTRUCTING THE ENABLE METHOD ************************/
	private void enable(){
		if(Enable){
			Enable = false;
			To.setEditable(true);
			EnableTo.setText("Disable TO");
		}
		else{
			Enable = true;
			To.setEditable(false);
			EnableTo.setText("Enable TO");
		}
	}
	
	
	/****************** HERE WE RETURN HOME PANEL METHOD *********************/
	public JPanel getMessage() {
		// Here we return home panel
		return MessagePanel;
	}
	
	
	/****************** CONSTRUCTING THE ACTION EVEN CLASS ********************/
	class ActionTaken implements ActionListener {
	      public void actionPerformed(ActionEvent e) {
	    	  String command = e.getActionCommand();
	    	  // Here we check command to set start appropriately
	    	  switch(command){
	    	  case "SEND":
	    		  // Here we start thread running
	    		  Start = true;
	    		  StopBut.setEnabled(true);
	    		  SendBut.setEnabled(false);
	    		  break;
	    	  case "STOP":
	    		  // Here we stop thread running
	    		  Start = false;
	    		  SendBut.setEnabled(true);
	    		  StopBut.setEnabled(false);
	    	  }
	    	  
	    	  // HERE WE START OUR SEND COMMAND GO ON
	    	  // When send is clicked it keeps on sending till
	    	  // Stop button is click
	    	  if (Start){
	    		  // lets start sending
	    		  if(control.connection()){
	    			  sender();
	    		  }
	    		  else{
	    			  // Here display message
	    			  SendBut.setEnabled(true);
	    			  StopBut.setEnabled(false);
	    			  control.alert().errorAlart("Internet Connection", "There's no internet connection!");
	    		  }
	    	  }
	    	  else{
	    		  if(ProcessCount==Process){
	    			  control.alert().warningAlart("Sending Message", "Process Ended");
	    		  }
	    	  }
	      }
	}
	
	
	/****************** CONSTRUCTING THE SUBACTION CLASS *********************/
	class SubAction implements ActionListener {
	      public void actionPerformed(ActionEvent e) {
	    	  String command = e.getActionCommand();
	    	  switch(command){
	    	  case "TEMPLATE":
	    		  // Here we call the template processor
	    		  try {
					control.template();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		  break;
	    	  case "ENABLE":
	    		  // Here we enable the to field
	    		  enable();
	    	  }
	      }
	}
	
	// END OF CLASS
}
