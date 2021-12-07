package system.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import system.soft.processor.SetBase;

@SuppressWarnings("serial")
public class Settings {
	// HERE WE SET PUBLIC VARIABLES
	private JPanel SettingsPanel = new JPanel(new BorderLayout());
	List<JTextField> TextFieldLists;
	List<String> DataBase;
	private Control control;
	private String[] RSets;
	// HERE WE CONSTRUCT MENU CLASS
	public Settings(Control cntrl) {
		this.control = cntrl;
		// Here we call the wizard
		try {
			wizard();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	/*********** CONSTRUCTING DESIGN WIZARD METHOD * @throws IOException ************/
	private void wizard() throws IOException{
		// HERE WE START CONSTRUCTING THE PANEL DESIGN PROCESS
		String[] sets = {"company", "host", "port", "from", "username", "password", "sp", "tp", "sendRate", "extRate", "num", "timeout", "pt", "pageSize", "validate", "filter"};
		RSets = new String[]{"company", "mshost", "msport", "msfrom", "msuser", "mspass", "sp", "tp", "sendRate", "extRate", "num", "timeout", "ptimeout", "pageSize", "validate", "filter"};
		List<JLabel> labelLists = new ArrayList<JLabel>();
		TextFieldLists = new ArrayList<JTextField>();
		DataBase = new ArrayList<String>();
		UIHelper helper = new UIHelper();
		SetBase setting = new SetBase();
		// HERE WE START PROCESSING
		
		GridLayout formLayout = new GridLayout(0,2);
		JToolBar toolBar = new JToolBar("Still draggable");
		JPanel mainPane = new JPanel(new BorderLayout());
		JPanel headPane = new JPanel(new BorderLayout());
		JPanel formPane = new JPanel(formLayout);
		JPanel menuPane = new JPanel();
		
		// Setting body pane backgroung
		JPanel bodyPane = new JPanel(new BorderLayout()){
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
		
		bodyPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		formPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		formLayout.setHgap(10);
		formLayout.setVgap(5);
		
		// Here we set the description label
		String description = "Change made to default settings can alter the default functionalities so make sure to set properly.";
		JLabel setDesc = helper.label(description, 5, 10);
		setDesc.setBorder(new EmptyBorder(5, 5, 10, 0));
		
		// Here we set the menu button    
		JButton saveBut = helper.buttonMake("Save Settings", "data/img/save.png");
		saveBut.addActionListener(new SetCommand());
		    
		toolBar.setBorderPainted(false);
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		toolBar.setOpaque(false);
		    
		toolBar.add(saveBut);
		menuPane.add(toolBar);

		// Adding Header Level Pane
		headPane.add(menuPane, BorderLayout.NORTH);
		
		// HERE LETS CREATE OUR JlABELS AND TEXTFIELDS DYNAMICALLY
		for(int i=0; i<sets.length; i++){
			labelLists.add(helper.label(setting.getDesc(RSets[i]), 8, 4));
			TextFieldLists.add(helper.textField(setting.get(sets[i]), 24));
			DataBase.add(setting.get(sets[i]));
		} // end of loop

		// NOW LETS LOG EACH FIELD TO ITS APPROPRIATE PANELS
		formPane.setOpaque(false);
		formPane.removeAll();
		// Now lets loop to assign
		for(int j=0; j<labelLists.size(); j++){
			// Here we add to left pane
			JPanel small = new JPanel(new BorderLayout());
			small.setOpaque(false);
			small.add(labelLists.get(j), BorderLayout.NORTH);
			small.add(TextFieldLists.get(j), BorderLayout.CENTER);
			formPane.add(small);
		} // end of second loop
		
		// NOW LETS SET THE SPLIT PANE
		formPane.revalidate();
		formPane.repaint();
		// Here we add to the body
		bodyPane.add(setDesc, BorderLayout.NORTH);
		bodyPane.add(formPane, BorderLayout.CENTER);
		
		// Here we add the main pane
		mainPane.add(headPane, BorderLayout.NORTH);
		mainPane.add(bodyPane, BorderLayout.CENTER);
		    
		// Now lets add to the frame
		SettingsPanel.add(mainPane, BorderLayout.CENTER);
	}
	  
	
	/****************** HERE WE RETURN HOME PANEL METHOD *********************/
	public JPanel getSettings() {
		// Here we return home panel
		return SettingsPanel;
	}
	
	
	/****************** CONSTRUCTING THE SUBACTION CLASS *********************/
	class SetCommand implements ActionListener {
	      public void actionPerformed(ActionEvent e) {
	    	  int confirm = 0;
	    	  int setted = 0;
	    	  // Lets confirm that user wants to save settings
	    	  confirm = control.alert().confirmAlart("Are you sure you want to commit change to setting?");
	    	  if(confirm==1){
	    		  setted = control.saveSetting(TextFieldLists, DataBase, RSets);
	    		  if(setted==1){
	    			  control.alert().warningAlart("Save Settings", "Setting saved successfully!");
	    		  }
	    	  }
	      }
	}
}
