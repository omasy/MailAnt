package system.ui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import system.soft.control.AntResource;
import system.source.DjadeUtil;

@SuppressWarnings("serial")
public class About {
	// HERE WE SET PUBLIC VARIABLES
	private JPanel AboutPanel = new JPanel(new BorderLayout());
	private Control control;
	// HERE WE CONSTRUCT MENU CLASS
	public About(Control cntrl) {
		this.control = cntrl;
		// Here we call the wizard
		try {
			try {
				wizard();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** CONSTRUCTING DESIGN WIZARD METHOD @throws FileNotFoundException @throws IOException 
	 * @throws URISyntaxException **/
	private void wizard() throws IOException, URISyntaxException{
		// HERE WE START CONSTRUCTING THE PANEL DESIGN PROCESS
		String aboutFile = "log/about/about.txt";
		UIHelper helper = new UIHelper();
		DjadeUtil util = new DjadeUtil();
		AntResource resource = new AntResource();
		
		// HERE WE START PROCESSING
		JToolBar toolBar = new JToolBar("Still draggable");
		JPanel mainPane = new JPanel(new BorderLayout());
		JPanel headPane = new JPanel(new BorderLayout());
		JPanel aboutPanel = new JPanel(new BorderLayout());
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
		aboutPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		// Here we set the menu button    
		JButton saveBut = helper.buttonMake("Download Info", "data/img/download.png");
		saveBut.addActionListener(new DownloadAction());
		    
		toolBar.setBorderPainted(false);
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		toolBar.setOpaque(false);
		    
		toolBar.add(saveBut);
		menuPane.add(toolBar);

		// Adding Header Level Pane
		headPane.add(menuPane, BorderLayout.NORTH);
		
		// LETS CREATE LABEL TO DISPLAY OUR ABOUT
		String aboutData = util.readBuffer(resource.get(aboutFile), "ant");
		JLabel aboutDoc;
		if(aboutData!=null && aboutData.length()>0){
			aboutDoc = new JLabel(aboutData);
		}
		else{
			aboutDoc = new JLabel("<b>About Document Not Found<b>");
		}
		
		// Lets add to the about panel then to body
		aboutDoc.setFont(aboutDoc.getFont().deriveFont(Font.PLAIN));
		aboutPanel.add(aboutDoc, BorderLayout.NORTH);
		aboutPanel.setBackground(Color.WHITE);
		bodyPane.add(aboutPanel);
		
		// Here we add the main pane
		mainPane.add(headPane, BorderLayout.NORTH);
		mainPane.add(bodyPane, BorderLayout.CENTER);
		    
		// Now lets add to the frame
		AboutPanel.add(mainPane, BorderLayout.CENTER);
	}
	  
	
	/****************** HERE WE RETURN HOME PANEL METHOD *********************/
	public JPanel getAbout() {
		// Here we return home panel
		return AboutPanel;
	}
	
	
	/****************** CONSTRUCTING THE SUBACTION CLASS *********************/
	class DownloadAction implements ActionListener {
	      public void actionPerformed(ActionEvent e) {
	    	  int check=0;
	    	  try {
				check=control.exporter("info");
				if(check==1){
					control.alert().warningAlart("Notification", "Download completed!");
				}
	    	  } catch (URISyntaxException e1) {
	    		  // TODO Auto-generated catch block
	    		  e1.printStackTrace();
	    	  }
	      }
	}
}
