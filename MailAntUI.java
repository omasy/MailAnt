package system.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import system.soft.processor.SetBase;
import system.source.DjadeUtil;

public class MailAntUI {
	// HERE WE SET PUBLIC VARIABLES
	UIHelper helper = new UIHelper();
	JPanel mainPanel = new JPanel();
	JMenuBar menuBar = new JMenuBar();
	// Setting other objects
	SetBase Setting = new SetBase();
	Control control;
	Home home;
	Message message;
	Settings setting;
	About about;
	
	private  void createAndShowUI() throws IOException {
		// Initiating the components
		JFrame frame = new JFrame("Mail Ant");
		DjadeUtil util = new DjadeUtil();
		control = new Control(frame);
		home = new Home(control);
		message = new Message(control);
		setting = new Settings(control);
		about = new About(control);
	    // Here we set main panel prop
	    int gap=0;
	    Image img = ImageIO.read(util.getResourceStream(this.getClass(), "data/img/MyIcon.jpg"));
	    ImageIcon Jimg = new ImageIcon(img);
	    mainPanel.setBorder(BorderFactory.createEmptyBorder(gap, gap, gap, gap));
	    mainPanel.setLayout(new BorderLayout(gap, gap));
	    mainPanel.setPreferredSize(new Dimension(800, 600));
	    mainPanel.add(home.getHome(), BorderLayout.CENTER);
	    
	    // Here lets initiate the menu
	    initMenu();
	    
	    // create the GUI to display the view
	    frame.setIconImage(Jimg.getImage());
	    frame.getContentPane().add(mainPanel);
	    frame.setJMenuBar(menuBar);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.pack();
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}

	// call Swing code in a thread-safe manner per the tutorials
	public static void main(String[] args) {
		// HERE WE SET LOOK AND FEEL
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }

		// HERE WE START RUNNING APP
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				MailAntUI ui = new MailAntUI();
				try {
					ui.createAndShowUI();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	
	/****************** HERE WE SET OTHER USEFUL METHODS *************************/
	private void initMenu() {
		// HERE WE INITIATE MENU FOR THE APPLICATION
		//create menus
	    JMenu fileMenu = helper.menu("File");
	    JMenu homeMenu = helper.menu("Home");
	    homeMenu.addMenuListener(new MenuAction(home.getHome()));
	    JMenu messageMenu = helper.menu("Message");
	    messageMenu.addMenuListener(new MenuAction(message.getMessage()));
	    JMenu settingMenu = helper.menu("Settings");
	    settingMenu.addMenuListener(new MenuAction(setting.getSettings()));
	    JMenu aboutMenu = helper.menu("About");
	    aboutMenu.addMenuListener(new MenuAction(about.getAbout()));
	    
	    //create menu items
	    JMenuItem importMenuItem = helper.createMenuItem("Import", KeyStroke.getKeyStroke (KeyEvent.VK_I, ActionEvent.ALT_MASK));
	    importMenuItem.setActionCommand("Import");
	    importMenuItem.addActionListener(new MenuItemListener());

	    JMenuItem exportMenuItem = helper.createMenuItem("Export", KeyStroke.getKeyStroke (KeyEvent.VK_E, ActionEvent.ALT_MASK));
	    exportMenuItem.setActionCommand("Export");
	    exportMenuItem.addActionListener(new MenuItemListener());

	    JMenuItem exitMenuItem = helper.createMenuItem("Exit", KeyStroke.getKeyStroke (KeyEvent.VK_ESCAPE, 0));
	    exitMenuItem.setActionCommand("Exit");
	    exitMenuItem.addActionListener(new MenuItemListener());
	      
	    // HERE WE ADD ALL MENU ITEMS
	    //add menu items to menus
	    fileMenu.add(importMenuItem);
	    fileMenu.add(exportMenuItem);
	    fileMenu.addSeparator();
	    fileMenu.add(exitMenuItem);
	     
	    //add menu to menu bar
	    menuBar.add(fileMenu);
	    menuBar.add(homeMenu);
	    menuBar.add(messageMenu);
	    menuBar.add(settingMenu);
	    menuBar.add(aboutMenu);
	}

	
	private void changePanel(JPanel panel) {
		mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
	}
	
	
	/****************** HERE WE CREATE OUR ACTION CLASS **************************/
	// HERE WE CREATE ACTION LISTENER FOR MENU
	class MenuAction implements MenuListener {
		private JPanel panel;
	    private MenuAction(JPanel pnl) {
	        this.panel = pnl;
	    }
	    
	    @Override
	    public void menuSelected(MenuEvent e) {            
	    	changePanel(panel);
	    }
	    
	    @Override
	    public void menuDeselected(MenuEvent e) {
	    	changePanel(panel);
	    }

	    @Override
	    public void menuCanceled(MenuEvent e) {
	    	changePanel(panel);
	    }
	}
	
	// HERE WE CREATE ACTION LISTENER FOR ITEM
	class MenuItemListener implements ActionListener {
	      public void actionPerformed(ActionEvent e) {
	    	  String command = e.getActionCommand();
	    	  // Lets check which we called
	    	  switch(command){
	    	  case "Import":
	    		  // Here we call import control
	    		  if(Setting.get("import").equals("yes")){
	    			  try {
						if(control.importer("bank", "import")==1){
							  control.alert().warningAlart("Import Manager", "File imported successfully!");
						  }
	    			  } catch (URISyntaxException e1) {
	    				  // TODO Auto-generated catch block
	    				  e1.printStackTrace();
	    			  }
	    		  }
	    		  else{
	    			  control.alert().warningAlart("Import Manager", "Not allowed to import");
	    		  }
	    		  break;
	    	  case "Export":
	    		  // Here we call export control
	    		  if(Setting.get("export").equals("yes")){
	    			  try {
						if(control.exporter("bank")==1){
							  control.alert().warningAlart("Export Manager", "Email banks exported successfully!");
						  }
	    			  } catch (URISyntaxException e1) {
	    				  // TODO Auto-generated catch block
	    				  e1.printStackTrace();
	    			  }
	    		  }
	    		  else{
	    			  control.alert().warningAlart("Export Manager", "Not allowed to export");
	    		  }
	    		  break;
	    	  case "Exit":
	    		  // Here we call exit control
	    		  control.exit();
	    	  }
	      }    
	}
}
