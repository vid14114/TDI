package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class TDIDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = -1221054153637014114L;
	private String[] plugin;
	private JPanel main = new JPanel();
	private JPanel header = new JPanel();
	private JPanel content = new JPanel();
	private JPanel options = new JPanel();
	
	//Creating buttons
	private JButton pluginButton = new JButton("Plugins");
	private JButton restoreButton = new JButton("Restore");
	private JButton helpButton = new JButton("!S.O.S!");
	private JButton connectButton = new JButton("Connect");
	
	//creating textbox for ip-insert
	private JTextField ip1=new JTextField();
	private JTextField ip2=new JTextField();
	private JTextField ip3=new JTextField();
	private JTextField ip4=new JTextField();
	
	//Panel used for onConnect ip insertion
	private JPanel ipPanel=new JPanel();
	
	//background colors
	Color colorContent=new Color(100);
	Color colorHeader=new Color(200);
	
	
	public TDIDialog(String[] plugin){
        super();
        setTitle("Test"); // Must be changed
        setSize(600, 600);
        setLocation(400, 200);
        setContentPane(onWelcome());
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
	}
/**/
	public JPanel onWelcome() {
		main.setLayout(new BorderLayout());
		
		//Changing options of the panels
		header.setSize(600,50);
		header.setLocation(0,0);
		header.setBackground(colorHeader);
		content.setSize(480,550);
		content.setLocation(0,50);
		content.setBackground(colorContent);
		options.setSize(120,550);
		options.setLocation(480,50);
		
		//Placing buttons statically
		pluginButton.setPreferredSize(new Dimension(90,30));
		restoreButton.setPreferredSize(new Dimension(90,30));
		helpButton.setPreferredSize(new Dimension(90,30));
		connectButton.setPreferredSize(new Dimension(90,30));
		
		pluginButton.addActionListener(this);
		restoreButton.addActionListener(this);
		helpButton.addActionListener(this);
		connectButton.addActionListener(this);
		
		//Buttons added to options panel
		options.add(pluginButton);
		options.add(helpButton);
		options.add(restoreButton);
		options.add(connectButton);
		
		//Panels added to main panel
		main.add(content);
		main.add(options);
		main.add(header);
		return main;
	}

	/**
	 * Shows help-information
	 * */
	public void onHelp() { //Maria
		
		// clear panel
		content.removeAll();
		
		JPanel helpe=new JPanel();
		
		//create TextArea
		JTextArea help=new JTextArea();
		
		//set parameters
		help.setText("This is some information don't care about it, its useless anyway - The End");
		help.setEditable(false);
		help.setBackground(colorContent);
		help.setBounds(new Rectangle(new Dimension(500,20)));
		help.setForeground(new Color(-50));
		help.setBorder(null);
		help.setLocation(50,50);
		
		//add everything
		helpe.add(help);
		content.add(helpe);
	}

	/**
	 * Initializes 4 input Fields separated by dots to enter the users IP and a Button to start the connection
	 */
	public void onConnect() { //Maria
		
		// clear panel
		content.removeAll();
		
		//posititon of elements
		int dot1X=183;
		int dot2X=dot1X+45;
		int dot3X=dot2X+45;
		int dotY=240;
		
		int ipX=-37;
		int ipY=225;
		
		//creating none editable text area for dots x.x.x.x
		JTextField dot1=new JTextField();
		JTextField dot2=new JTextField();
		JTextField dot3=new JTextField();
		
		//creating none editable text area for info
		JTextField info=new JTextField();
		
		Rectangle ipSize = new Rectangle(new Dimension(34,20));
		
		//setting parameters
		dot1.setText(".");
		dot1.setBounds(5, 5, 5, 5);
		dot1.setEditable(false);
		dot1.setLocation(dot1X, dotY);
		
		dot2.setText(".");
		dot2.setBounds(5, 5, 5, 5);
		dot2.setEditable(false);
		dot2.setLocation(dot2X, dotY);
		
		dot3.setText(".");
		dot3.setBounds(5, 5, 5, 5);
		dot3.setEditable(false);
		dot3.setLocation(dot3X, dotY);
		
		ip1.setBounds(ipSize);
		ip1.setText("0000");
		ip1.setLocation(ipX+dot1X, ipY);
		ip1.setBackground(colorContent);
		ip1.setForeground(new Color(-50));
		ip1.setBorder(null);
		
		ip2.setBounds(ipSize);
		ip2.setText("0000");
		ip2.setLocation(ipX+dot2X, ipY);
		ip2.setBackground(colorContent);
		ip2.setForeground(new Color(-50));
		ip2.setBorder(null);
		
		ip3.setBounds(ipSize);
		ip3.setText("0000");
		ip3.setLocation(ipX+dot3X, ipY);
		ip3.setBackground(colorContent);
		ip3.setForeground(new Color(-50));
		ip3.setBorder(null);
		
		ip4.setBounds(ipSize);
		ip4.setText("0000");
		ip4.setLocation(ipX+dot3X+46, ipY);
		ip4.setBackground(colorContent);
		ip4.setForeground(new Color(-50));
		ip4.setBorder(null);
		
		info.setBounds(new Rectangle(new Dimension(165,20)));
		info.setText("Please enter your IP-Address");
		info.setEditable(false);
		info.setBackground(colorContent);
		info.setForeground(new Color(-50));
		info.setBorder(null);
		info.setLocation(dot1X-35,ipY-50);
		
		//panel parameters
		ipPanel.setVisible(true);
		ipPanel.setSize(380,350);
		ipPanel.setLocation(0,50);
		
		//add everything to panel
		ipPanel.add(info);
		ipPanel.add(ip1);
		ipPanel.add(dot1);
		ipPanel.add(ip2);
		ipPanel.add(dot2);
		ipPanel.add(ip3);
		ipPanel.add(dot3);
		ipPanel.add(ip4);
		
		content.add(ipPanel);
				
	}
	
	/**
	 * Checks if the entered IP has a valid format and converts it to an integer
	 * returns the IP
	 * throws a NumberFormatException
	 * */
	private int checkIp() throws NumberFormatException
	{
		if((ip1.getText().length()==4)&&(ip2.getText().length()==4)&&(ip3.getText().length()==4)&&(ip4.getText().length()==4))
		{
			String full=ip1.getText()+""+ip2.getText()+""+ip3.getText()+""+ip4.getText();
			int fullip=Integer.parseInt(full);
			return fullip;
		}
		else
		{
			throw new NumberFormatException();
		}
			
	}
	
	/**
	 * returns the entered IP as integer
	 * */
	public int getIp() 
	{
		return checkIp();
	}
	
	/**
	 * Opens the onPlugin button
	 */
	public void onPlugin() {
		throw new UnsupportedOperationException();
	}
	@Override
	public void actionPerformed(ActionEvent actionPerformed) {
		// TODO Auto-generated method stub
		
		//pluginButton clicked
		if(actionPerformed.getSource()==pluginButton){
			System.out.println("Plugin");
		}
		
		//helpButton clicked
		if(actionPerformed.getSource()==helpButton){
			onHelp();
		}
		
		//restoreButton clicked
		if(actionPerformed.getSource()==restoreButton){
			System.out.println("Restore");
		}
		
		//connectButton clicked
		if(actionPerformed.getSource()==connectButton){
			onConnect();
		}
	}

}
