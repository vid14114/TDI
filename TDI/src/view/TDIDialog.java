package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.print.attribute.standard.OrientationRequested;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
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
	private JButton connectButton = new JButton("Start");//different name

	private JButton startTDI = new JButton("connect");// button to start actual program
	private JButton startTutorialButton = new JButton("Tutorial");// button to start Tutorial

	//creating textbox for ip-insert
	private JTextField ip1=new JTextField();
	private JTextField ip2=new JTextField();
	private JTextField ip3=new JTextField();
	private JTextField ip4=new JTextField();
	
	//for errorMessages
	JTextField errorMessage = new JTextField();
	
	//Panel used for onConnect ip insertion
	private JPanel ipPanel=new JPanel(new FlowLayout());
	
	//background colors
	Color colorContent=new Color(100);
	Color colorHeader=new Color(200);
	
	
	public TDIDialog(String[] plugin){
        super();
        setTitle("Tangible Desktop Items"); // Must be changed
        setSize(600, 600);
        setLocation(400, 200);
        setContentPane(onWelcome());
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
	}

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
		
		JPanel helpContentPanel=new JPanel();
		helpContentPanel.setLayout(new BoxLayout(helpContentPanel, BoxLayout.PAGE_AXIS));
		
		//create TextArea
		JTextArea helpText=new JTextArea();
		
		//set parameters for Text Area
		helpText.setText("The first bold line creates a top-to-bottom box layout and sets it up as the layout manager for listPane. The two arguments to the BoxLayout constructor are the container that it manages and the axis along which the components will be laid out. The PAGE_AXIS constant specifies that components should be laid out in the direction that lines flow across a page as determined by the target container's ComponentOrientation property. The LINE_AXIS constant specifies that components should be laid out in the direction of a line of text as determined by the target container's ComponentOrientation property. These constants allow for internationalization, by laying out components in their container with the correct left-to-right, right-to-left or top-to-bottom orientation for the language being used.");
		helpText.setEditable(false);
		helpText.setBackground(colorContent);
		helpText.setBounds(new Rectangle(new Dimension(30,40)));
		helpText.setLineWrap(true); // line breaks
		helpText.setAutoscrolls(true);
		helpText.setForeground(new Color(-50));
		helpText.setBorder(null);
		//help.setLocation(50,50);
		
		//button parameters
		//startTutorial.setLocation(1, 1);
		startTutorialButton.setPreferredSize(new Dimension(90,30));
		startTutorialButton.addActionListener(this);
		
		//helpContentPanel parameters
		helpContentPanel.setVisible(true);
		helpContentPanel.setBackground(colorContent);
		helpContentPanel.setSize(new Dimension(480,550));
		//helpe.setLocation(0,100);
			
		//add everything
		helpContentPanel.add(helpText);
		helpContentPanel.add(Box.createRigidArea(new Dimension(450,200)));
		helpContentPanel.add(startTutorialButton);
		
		content.add(helpContentPanel);
		
		//refresh view
		content.updateUI();
		helpContentPanel.updateUI();
	}

	/**
	 * Initializes 4 input Fields separated by dots to enter the users IP and a Button to start the connection
	 */
	public void onConnect() { //Maria
		
		// clear panel
		content.removeAll();
		ipPanel.removeAll();
		
		//create OverallPanel to include every component
		JPanel OverallPanel = new JPanel();
		OverallPanel.removeAll();
		
		ipPanel.setLayout(new BoxLayout(ipPanel, BoxLayout.X_AXIS));
		OverallPanel.setLayout(new BoxLayout(OverallPanel, BoxLayout.Y_AXIS));
		
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
		dot1.setBackground(colorContent);
		dot1.setForeground(new Color(-50));
		dot1.setBorder(null);
		
		dot2.setText(".");
		dot2.setBounds(5, 5, 5, 5);
		dot2.setEditable(false);
		dot2.setBackground(colorContent);
		dot2.setForeground(new Color(-50));
		dot2.setBorder(null);
		
		
		dot3.setText(".");
		dot3.setBounds(5, 5, 5, 5);
		dot3.setEditable(false);
		dot3.setBackground(colorContent);
		dot3.setForeground(new Color(-50));
		dot3.setBorder(null);
		
		
		ip1.setBounds(ipSize);
		ip1.setText("000");
	//	ip1.setLocation(ipX+dot1X, ipY);
		ip1.setBackground(colorContent);
		ip1.setForeground(new Color(-50));
		ip1.setBorder(null);
		
		ip2.setBounds(ipSize);
		ip2.setText("000");
		ip2.setBackground(colorContent);
		ip2.setForeground(new Color(-50));
		ip2.setBorder(null);
		
		ip3.setBounds(ipSize);
		ip3.setText("000");
		ip3.setBackground(colorContent);
		ip3.setForeground(new Color(-50));
		ip3.setBorder(null);
		
		ip4.setBounds(ipSize);
		ip4.setText("000");
		ip4.setBackground(colorContent);
		ip4.setForeground(new Color(-50));
		ip4.setBorder(null);
		
		//info textfiedl for user to know what to do
		info.setBounds(new Rectangle(new Dimension(165,20)));
		info.setText("Please enter your IP-Address:");
		info.setEditable(false);
		info.setBackground(colorContent);
		info.setForeground(new Color(-50));
		info.setBorder(null);
		
		//button location
		startTDI.setPreferredSize(new Dimension(90,30));
		startTDI.addActionListener(this);
		

		// Display Error Message;
		errorMessage.setEditable(false);
		errorMessage.setBackground(colorContent);
		errorMessage.setForeground(new Color(-200));
		errorMessage.setBorder(null);
		
		//ipPanel parameters
		ipPanel.setBackground(colorContent);
		
		//OverallPanel parameters
		OverallPanel.setPreferredSize(new Dimension(400,400));
		OverallPanel.setBackground(colorContent);
		
		//add everything to panel
		ipPanel.add(info);
		ipPanel.add(Box.createRigidArea(new Dimension(10,0)));
		ipPanel.add(ip1);
		ipPanel.add(dot1);
		ipPanel.add(ip2);
		ipPanel.add(dot2);
		ipPanel.add(ip3);
		ipPanel.add(dot3);
		ipPanel.add(ip4);
		OverallPanel.add(ipPanel, BorderLayout.CENTER);
		OverallPanel.add(Box.createRigidArea(new Dimension(350,0)));
		OverallPanel.add(errorMessage, BorderLayout.CENTER);
		OverallPanel.add(Box.createRigidArea(new Dimension(100,0)));
		OverallPanel.add(startTDI, BorderLayout.LINE_END);
		
		
		content.add(OverallPanel);
		
		//referesh
		OverallPanel.updateUI();
		content.updateUI();
		ipPanel.updateUI();
	}
	
	/**
	 * Checks if the entered IP has a valid format and converts it to an integer
	 * @return int ip
	 * @throws NumberFormatException
	 * */
	private String checkIp()
	{
		int fullIP[] =new int[4];
		String ip="1.1.1.1";
		//Checks if appropriate length
		if((ip1.getText().length()<=3)&&(ip2.getText().length()<=3)&&(ip3.getText().length()<=3)&&(ip4.getText().length()<=3))
		{
			//not null check
			if((ip1.getText().length()>0)&&(ip2.getText().length()>0)&&(ip3.getText().length()>0)&&(ip4.getText().length()>0))
			{
				try
				{
					//checks format 
					fullIP[0] = Integer.parseInt(ip1.getText());
					fullIP[1] = Integer.parseInt(ip2.getText());
					fullIP[2] = Integer.parseInt(ip3.getText());
					fullIP[3] = Integer.parseInt(ip4.getText());
					
					//checks ip<255
					if((fullIP[0]<255)&&(fullIP[1]<255)&&(fullIP[2]<255)&&(fullIP[3]<255))
					{
						ip= fullIP[0]+"."+fullIP[1]+"."+fullIP[2]+"."+fullIP[3];
						return ip;
					}
					else
					{
						errorMessage.setText("Number must be <255");
						ipPanel.updateUI();
					}
				}
				catch(NumberFormatException e){
					errorMessage.setText("Please enter only numbers");
					ipPanel.updateUI();
				}
			}
			else
			{
				errorMessage.setText("Input must not be empty");
				ipPanel.updateUI();
			}
		}
		else
		{
			errorMessage.setText("input only in the following format: 000.000.000.000");
			ipPanel.updateUI();
			return ip;
		}
		return ip;
	}
	
	/**
	 * Opens the onPlugin button
	 */
	public void onPlugin(Object rowData[][])  {
		
		content.removeAll();
		PluginTableModel c = new PluginTableModel(rowData);
		final JTable table = new JTable(c);
//      table.setPreferredScrollableViewportSize(new Dimension(500, 70));
//      table.setFillsViewportHeight(true);

      //Create the scroll pane and add the table to it.
      JScrollPane scrollPane = new JScrollPane(table);
      
      content.add(scrollPane);
		
		content.setVisible(true);
		content.updateUI();
	}
	@Override
	public void actionPerformed(ActionEvent actionPerformed) {
		// TODO Auto-generated method stub
		
		//pluginButton clicked
		if(actionPerformed.getSource()==pluginButton){
			Object rowData[][] = { { "Music", true }, { "Anilator", true }, { "PlugMeIn!", false },
		      	      { "B�stewogibtplugin", true }, { "Plugin Nr.5", false }, };
			onPlugin(rowData);
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
		//startTDI clicked
		if(actionPerformed.getSource()==startTDI){
			System.out.println("start the TDI connection"); 
			errorMessage.setText("");
			content.updateUI();
			checkIp();
			//start Program/ BigLogic (Is Master, is big).
		}
		//startTutorial clicked
		if(actionPerformed.getSource()==startTutorialButton){
			System.out.println("start Tutorial");
			//start Tutorial
		}
	}

}
