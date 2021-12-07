package system.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import system.soft.processor.FileManager;
import system.soft.processor.SetBase;
import system.source.DjadeUtil;

public class UIHelper {
	// HERE WE SET PUBLIC VARIABLES
	DjadeUtil util = new DjadeUtil();
	// HERE WE CONSTRUCT CLASS
	public UIHelper(){};
	
	
	/*********** HERE WE CONSTRUCT THE BUTTION MAKER * @throws IOException ************/
	public JButton buttonMake(String name, String iconPath) throws IOException{
		// HERE WE START CONSTRUCTING
		Image img = ImageIO.read(util.getResourceStream(this.getClass(), iconPath));
		ImageIcon jIcon = new ImageIcon(img);
		JButton jButton = new JButton(name, jIcon);
		    
		jButton.setFont(jButton.getFont().deriveFont(Font.PLAIN));
		jButton.setMargin(new Insets(3,3,3,3));
		jButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		jButton.setHorizontalTextPosition(SwingConstants.CENTER);
		    
		jButton.setBorderPainted(false); 
		jButton.setContentAreaFilled(false); 
		jButton.setFocusPainted(false); 
		jButton.setOpaque(false);
		    
		jButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				jButton.setBackground(Color.ORANGE);
				jButton.setBorderPainted(false); 
				jButton.setContentAreaFilled(true); 
				jButton.setFocusPainted(false); 
				jButton.setOpaque(true);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				jButton.setBorderPainted(false); 
				jButton.setContentAreaFilled(false); 
				jButton.setFocusPainted(false); 
				jButton.setOpaque(false);
			}
		});
		  
		// Here we return button
		return jButton;
	}
	  
	  
	/**************** CONSTRUCTING THE TABLE MAKER METHOD *******************/
	public JTable table(DefaultTableModel model, String colorCode, String desc){
		// HERE WE START CONSTRUCTING
		JTable table = new JTable(model);
		table.getAccessibleContext().setAccessibleDescription(desc);
		table.setOpaque(true);
		table.setFillsViewportHeight(true);
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.decode(colorCode));
		header.setForeground(Color.BLACK);
		table.setBackground(Color.WHITE);
		table.setDefaultRenderer(Object.class, new BorderTableCellRenderer());
		
		// Setting Size
		table.setRowHeight(20);
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(5);
		columnModel.getColumn(1).setPreferredWidth(175);
		columnModel.getColumn(2).setPreferredWidth(250);
		columnModel.getColumn(3).setPreferredWidth(100);
	         
		  
		// Here we return table
		return table;
	}
	  
	  
	/****************** CONSTRUCTING SEPARTOR METHOD ****************************/
	public JSeparator separator(String color, int num){
		JSeparator separator = new JSeparator(num);
		separator.setBackground(Color.decode(color));
		  
		// Here we return
		return separator;
	}
	
	
	/******************* CONSTRUCTING THE MENU HELPER ****************************/
	public JMenu menu(String name){
		JMenu jMenu = new JMenu(name);
		jMenu.setFont(jMenu.getFont().deriveFont(Font.PLAIN));
		jMenu.setBorder(new EmptyBorder(0, 4, 0, 4));
		
		// Here we return
		return jMenu;
	}
	
	
	/************* HERE WE CREATE MENU ITEM CUSTOMIZED ***************/
	public JMenuItem createMenuItem (String text, KeyStroke keyStroke) {
		// setIcon(new ImageIcon("icon.png"));
        JMenuItem menuItem = new JMenuItem (text);
        if (keyStroke != null) menuItem.setAccelerator (keyStroke);
        menuItem.setIconTextGap (40);
        return menuItem;
    }
	
	
	/************* CREATING DYNAMIC JLABEL METHOD ********************/
	public JLabel label(String value, int top, int bottom){
		JLabel label = new JLabel(value);
		label.setFont(label.getFont().deriveFont(Font.PLAIN));
		label.setBorder(new EmptyBorder(top, 0, 4, bottom));
		// Here we return
		return label;
	}
	
	
	/************* CREATING DYNAMIC JTextField METHOD *****************/
	public JTextField textField(String value, int col){
		JTextField field = new JTextField(value);
		field.setFont(field.getFont().deriveFont(Font.PLAIN));
		field.setBorder(new EmptyBorder(2, 2, 2, 2));
		// Checking if col value is set
		if(col>0){
			field.setColumns(col);
		}
		// Here we return
		return field;
	}
	
	
	
	/************* CREATING DYNAMIC JTextField METHOD *****************/
	public JTextArea textArea(String value){
		JTextArea area = new JTextArea(value);
		area.setFont(area.getFont().deriveFont(Font.PLAIN));
		area.setBorder(new EmptyBorder(4, 4, 4, 4));
		// Here we return
		return area;
	}
	
	
	/************** SETTING SPLIT PANE UTILITY *************************/
	public void setDividerLocation(final JSplitPane splitPane, final double location)
		{
		    splitPane.setDividerLocation(location);
		    splitPane.validate();
		}
	
	
	/************** SETTING JEDITOR PANE METHOD ************************/
	public JEditorPane editor(String texts, String type){
		JEditorPane myPane = new JEditorPane();    
        myPane.setContentType(type);    
        myPane.setText(texts);
        
        // Here we return
        return myPane;
	}
	
	
	/************** SETTING CODE EDITOR METHOD ************************/
	public RTextScrollPane codePane(String texts, String style, int c, int r){
		// Style : SyntaxConstants.SYNTAX_STYLE_JAVA
		RSyntaxTextArea textArea = new RSyntaxTextArea(c, r);
		textArea.setSyntaxEditingStyle(style);
		textArea.setCodeFoldingEnabled(true);
		RTextScrollPane sp = new RTextScrollPane(textArea);
		
		// Here we return
		return sp;
	}
	
	
	/************** SETTING THE EMAIL FILTER METHOS *********************/
	public String[] mailFilter(String[] emails){
		// METHOD TO IMPLEMENT FILTER FOR EMAIL
		SetBase setting = new SetBase();
		FileManager manager = new FileManager(setting);
		FileManager.Filter filter = manager.filter();
		String[] accepted = {};
		// NOW LETS START PROCESSING
		if(emails!=null){
			if(emails.length>0){
				if(setting.get("filter").equals("yes")){
					accepted = filter.nameFilter(occuredManager(emails));
					// Now lets check
					if(accepted!=null && accepted.length>0){
						if(setting.get("validate").equals("yes")){
							accepted = filter.validFilter(accepted);
						}
					}
				}
				else{
					// Setting filter setting is no
					accepted = occuredManager(emails);
				}
			}
		}
		// Here we return
		return accepted;
	}
	
	
	/***************** CONSTRUCTING REPEAT FILTER METHOD ***********/
	private String[] occuredManager(String[] data){
		// HERE WE START CONSTRUCTING THE OCCURED MANAGER PROCESS
		List<String> dataSet = new ArrayList<String>();
		String[] content = {};
		// HERE WE START PROCESSING
		if(data!=null){
			if(data.length>0){
				// Now lets loop
				for(int i=0; i<data.length; i++){
					if(!dataSet.contains(data[i])){
						dataSet.add(data[i]);
					}
				} // end of loop
				
				// Here lets convert our list to array
				content = dataSet.toArray(new String[dataSet.size()]);
			}
		}
		// Here we return
		return content;
	}
	
	// END OF CLASS
}


/*************** CREATING TABLE PADDING CLASS IMPLEMENTER ***********/
	@SuppressWarnings("serial")
class BorderTableCellRenderer extends JLabel implements TableCellRenderer{
		public BorderTableCellRenderer()
		{
			super();
			this.setBorder( BorderFactory.createEmptyBorder( 0, 5, 0, 5 ) );
		}
	 
		public Component getTableCellRendererComponent( JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column )
		{
			setForeground( table.getForeground() );
			setFont( table.getFont() );
			setValue( value );
			return this;
		}
	 
		public void updateUI()
		{
			super.updateUI();
			setForeground( null );
		}
	 
		public void validate() { }
	 
		public void revalidate() { }
	 
		public void repaint( long tm, int x, int y, int width, int height ) { }
	 
		public void repaint( Rectangle r ) { }
	 
		public void firePropertyChange( String propertyName, boolean oldValue, boolean newValue ) { }
	 
		protected void setValue( Object value )
		{
			setText( ( value == null ) ? "" : value.toString() );
		}
	 
		protected void firePropertyChange( String propertyName, Object oldValue, Object newValue )
		{
			// Strings get interned...
			if ( propertyName == "text" )
			{
				super.firePropertyChange( propertyName, oldValue, newValue );
			}
		}
	}