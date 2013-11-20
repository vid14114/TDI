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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
		header.setBackground(new Color(200));
		content.setSize(480,550);
		content.setLocation(0,50);
		content.setBackground(new Color(100));
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

	public void onHelp() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Shows information about connect (connecting)
	 */
	public void onConnect() {
		throw new UnsupportedOperationException();
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
			System.out.println("Help");
		}
		
		//restoreButton clicked
		if(actionPerformed.getSource()==restoreButton){
			System.out.println("Restore");
		}
		
		//connectButton clicked
		if(actionPerformed.getSource()==connectButton){
			System.out.println("Connect");
		}
	}

}
