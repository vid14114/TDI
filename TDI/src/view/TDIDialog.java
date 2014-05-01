package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.PluginTableModel;

public class TDIDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = -1221054153637014114L;
    
    /**
     * Fuer Fehlermeldungen
     */
    private final JTextField errorMessage = new JTextField();
    
    /**
     * Farbe des Inhalts  
     */
    private final Color colorContent = new Color(240, 248, 255);
    
    /**
     * Farbe der Kopfzeile
     */
    private final Color colorHeader = new Color(240, 248, 255);
    
    /**
     * Farbe der Optionen  
     */
    private final Color colorOptions = new Color(240, 248, 255);
    
    /**
     * Farbe des Vordergrundes 
     */
    private final Color foregroundColor = new Color(1);
    
    /**
     * das Hauptpanel 
     */
    private final JPanel main = new JPanel();
    
    /**
     * das Kopfzeilenpanel
     * es ist im Hauptpanel
     */
    private final JPanel header = new JPanel();
    
    /**
     * das Inhaltspanel
     * es ist im Hauptpanel
     */
    private final JPanel content = new JPanel();
    
    /**
     * das Optionenpanel
     * es ist im Hauptpanel
     */
    private final JPanel options = new JPanel();
    
    /**
     * Der Knopf um Pluginview aufzurufen
     */
    private final JButton pluginButton = new JButton("Plugins");
    
    /**
     * Der Knopf um Restoreview aufzurufen
     */
    private final JButton restoreButton = new JButton("Restore");
    
    /**
     * Der Knopf um Helpview aufzurufen
     */
    private final JButton helpButton = new JButton("!S.O.S!");
    
    /**
     * Der Knopf um Verbindungsinputfeld zu oeffnen
     */
    private final JButton connectButton = new JButton("Start");
    
    /**
     * Der Knopf um das Programm zu starten
     */
    private final JButton startTDI = new JButton("start TDI");
    
    /**
     * Der Knopf um das Tutorial zu starten
     */
    private final JButton startTutorialButton = new JButton("Tutorial starten");
    
    
    /**
     * Textfeld fuer den ersten Teil der IP
     */
    private final JTextField ip1 = new JTextField();
    
    /**
     * Textfeld fuer den zweiten Teil der IP
     */
    private final JTextField ip2 = new JTextField();
    
    /**
     * Textfeld fuer den dritten Teil der IP
     */
    private final JTextField ip3 = new JTextField();
    
    /**
     * Textfeld fuer den vierten Teil der IP
     */
    private final JTextField ip4 = new JTextField();
    
    /**
     * Der Actionlistener
     */
    private final ActionListener actionListener;
    
    /**
     * Das Model mit den Plugins
     */
    private final PluginTableModel pluginTableModel;
    
    /**
     * Das Panel fuer das View wo man die IP eingibt
     */
    private final JPanel ipPanel = new JPanel(new FlowLayout());

    /**
	 * Konstruktor TDIDialog
	 * 
	 * @param tdiActionListener der ActionListener fuer das View
	 * @param pluginTableModel das TableModel fuer die Plugins
	 */
    public TDIDialog(ActionListener tdiActionListener, PluginTableModel pluginTableModel) {
        super();
        setTitle("Tangible Desktop Items");
        setSize(600, 600);
        setLocation(400, 200);
        this.actionListener = tdiActionListener;
        this.pluginTableModel = pluginTableModel;
        setContentPane(onWelcome());
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    /**
     * Setzt den Content auf den Welcomepanel
     */
    JPanel onWelcome() {
        main.setLayout(new BorderLayout());
        // Changing options of the panels
        header.setSize(600, 100);
        header.setLocation(0, 0);
        header.setBackground(colorHeader);
        content.setSize(480, 550);
        content.setLocation(0, 50);
        content.setBackground(colorContent);
        options.setSize(120, 550);
        options.setLocation(480, 50);
        options.setBackground(colorOptions);

        BufferedImage logo = null;
        try {
            logo = ImageIO.read(new File("images/tdi.jpg"));
        } catch (FileNotFoundException e) {
            System.out.print("Image not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
       
         JLabel picLabel = new JLabel(new ImageIcon(logo));
         picLabel.setPreferredSize(new Dimension(50, 30));
        header.add(picLabel, BorderLayout.NORTH);
        
        // add a text to the first "startup" page
        JPanel welcomePanel = new JPanel();
        welcomePanel.setSize(500, 500);
        welcomePanel.setLayout(new BoxLayout(welcomePanel,BoxLayout.PAGE_AXIS));
        JTextArea welcomeText = new JTextArea();
        welcomeText.setText("Welcome to the TDI application. To start  the TDI application or to start the Tutorial click on the startbutton to the right! Have fun! ");
        welcomeText.setVisible(true);
        welcomeText.setBounds(new Rectangle(new Dimension(100, 100)));
        welcomeText.setLineWrap(true); // line breaks
        welcomeText.setAutoscrolls(true);
        welcomeText.setBorder(null);
        welcomeText.setBackground(colorContent);
        
        // Placing buttons statically
        pluginButton.setPreferredSize(new Dimension(90, 30));
        restoreButton.setPreferredSize(new Dimension(90, 30));
        helpButton.setPreferredSize(new Dimension(90, 30));
        connectButton.setPreferredSize(new Dimension(90, 30));

        pluginButton.addActionListener(this);
        restoreButton.addActionListener(actionListener);
        helpButton.addActionListener(this);
        connectButton.addActionListener(this);

        // Buttons added to options panel
        options.add(pluginButton);
        options.add(helpButton);
        options.add(restoreButton);
        options.add(connectButton);
        
        welcomePanel.add(welcomeText);
        welcomePanel.add(Box.createRigidArea(new Dimension(450,0)));
        content.add(welcomePanel);
   //     content.add(Box.createRigidArea(new Dimension(450, 200)));
        
        
        content.updateUI();

        // Panels added to main panel
        main.add(content);
        main.add(options);
        main.add(header);
        return main;
    }

    /**
     * Setzt den Content auf den Helppanel
     */
    void onHelp() {

        // clear panel
        content.removeAll();

        JPanel helpContentPanel = new JPanel();
        helpContentPanel.setLayout(new BoxLayout(helpContentPanel,
                BoxLayout.PAGE_AXIS));

        // create TextArea
        JTextArea helpText = new JTextArea();

        // set parameters for Text Area
        helpText.setText("The first bold line creates a top-to-bottom box layout and sets it up as the layout manager for listPane. The two arguments to the BoxLayout constructor are the container that it manages and the axis along which the components will be laid out. The PAGE_AXIS constant specifies that components should be laid out in the direction that lines flow across a page as determined by the target container's ComponentOrientation property. The LINE_AXIS constant specifies that components should be laid out in the direction of a line of text as determined by the target container's ComponentOrientation property. These constants allow for internationalization, by laying out components in their container with the correct left-to-right, right-to-left or top-to-bottom orientation for the language being used.");
        helpText.setEditable(false);
        helpText.setBackground(colorContent);
        helpText.setBounds(new Rectangle(new Dimension(30, 40)));
        helpText.setLineWrap(true); // line breaks
        helpText.setAutoscrolls(true);
        helpText.setForeground(foregroundColor);
        helpText.setBorder(null);
        // help.setLocation(50,50);


        // helpContentPanel parameters
        helpContentPanel.setVisible(true);
        helpContentPanel.setBackground(colorContent);
        helpContentPanel.setSize(new Dimension(480, 550));
        // helpe.setLocation(0,100);

        // add everything
        helpContentPanel.add(helpText);
        helpContentPanel.add(Box.createRigidArea(new Dimension(450, 200)));

        content.add(helpContentPanel);

        // refresh view
        content.updateUI();
        helpContentPanel.updateUI();
    }

    /**
     * Setzt den Content auf den Connectpanel
     */
    void onConnect() {

        // clear panel
        content.removeAll();
        ipPanel.removeAll();

        // create OverallPanel to include every component
        JPanel overallPanel = new JPanel();
        // create buttonPanel to include every button
        JPanel buttonPanel = new JPanel();

        ipPanel.setLayout(new BoxLayout(ipPanel, BoxLayout.X_AXIS));
        overallPanel.setLayout(new BoxLayout(overallPanel, BoxLayout.Y_AXIS));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        // creating none editable text area for dots x.x.x.x
        JTextField dot1 = new JTextField();
        JTextField dot2 = new JTextField();
        JTextField dot3 = new JTextField();

        // creating none editable text area for info
        JTextField info = new JTextField();

        Rectangle ipSize = new Rectangle(new Dimension(34, 20));

        // setting parameters
        dot1.setText(".");
        dot1.setBounds(5, 5, 5, 5);
        dot1.setEditable(false);
        dot1.setBackground(colorContent);
        dot1.setForeground(foregroundColor);
        dot1.setBorder(null);

        dot2.setText(".");
        dot2.setBounds(5, 5, 5, 5);
        dot2.setEditable(false);
        dot2.setBackground(colorContent);
        dot2.setForeground(foregroundColor);
        dot2.setBorder(null);

        dot3.setText(".");
        dot3.setBounds(5, 5, 5, 5);
        dot3.setEditable(false);
        dot3.setBackground(colorContent);
        dot3.setForeground(foregroundColor);
        dot3.setBorder(null);

        ip1.setBounds(ipSize);
        ip1.setText("000");
        // ip1.setLocation(ipX+dot1X, ipY);
        ip1.setBackground(colorContent);
        ip1.setForeground(foregroundColor);
        ip1.setBorder(null);

        ip2.setBounds(ipSize);
        ip2.setText("000");
        ip2.setBackground(colorContent);
        ip2.setForeground(foregroundColor);
        ip2.setBorder(null);

        ip3.setBounds(ipSize);
        ip3.setText("000");
        ip3.setBackground(colorContent);
        ip3.setForeground(foregroundColor);
        ip3.setBorder(null);

        ip4.setBounds(ipSize);
        ip4.setText("000");
        ip4.setBackground(colorContent);
        ip4.setForeground(foregroundColor);
        ip4.setBorder(null);

        // info textfiedl for user to know what to do
        info.setBounds(new Rectangle(new Dimension(165, 20)));
        info.setText("Please enter your IP-Address:");
        info.setEditable(false);
        info.setBackground(colorContent);
        info.setForeground(foregroundColor);
        info.setBorder(null);

        // button location
        startTDI.setPreferredSize(new Dimension(90, 30));
        startTDI.addActionListener(actionListener);
        // startTutorial.setLocation(1, 1);
        startTutorialButton.setPreferredSize(new Dimension(90, 30));
        startTutorialButton.addActionListener(actionListener);

        // Display Error Message;
        errorMessage.setEditable(false);
        errorMessage.setBackground(colorContent);
        errorMessage.setForeground(foregroundColor);
        errorMessage.setBorder(null);

        // ipPanel parameters
        ipPanel.setBackground(colorContent);

        //button panel params
        buttonPanel.setBackground(colorContent);

        // OverallPanel parameters
        overallPanel.setPreferredSize(new Dimension(400, 400));
        overallPanel.setBackground(colorContent);


        // add everything to panel
        ipPanel.add(info);
        ipPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        ipPanel.add(ip1);
        ipPanel.add(dot1);
        ipPanel.add(ip2);
        ipPanel.add(dot2);
        ipPanel.add(ip3);
        ipPanel.add(dot3);
        ipPanel.add(ip4);
        buttonPanel.add(startTutorialButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(90, 0)));
        buttonPanel.add(startTDI);
        overallPanel.add(ipPanel, BorderLayout.CENTER);
        overallPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        overallPanel.add(errorMessage, BorderLayout.CENTER);
        overallPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        overallPanel.add(buttonPanel, BorderLayout.LINE_END);

        content.add(overallPanel);

        // referesh
        overallPanel.updateUI();
        content.updateUI();
        ipPanel.updateUI();
    }

    /**
     * @param message das ist die Errormessage die die derzeitige gesetzt wird
     */
    public void setErrorMessage(String message) {
        errorMessage.setText(message);
        ipPanel.updateUI();
    }

    /**
     * Setzt den Content auf den Pluginpanel
     */
    void onPlugin() {

        content.removeAll();
        final JTable table = new JTable(pluginTableModel);
        // table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        // table.setFillsViewportHeight(true);

        // Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        content.add(scrollPane);
        content.setVisible(true);
        content.updateUI();
    }

    /**
     * @param actionPerformed diese Methode wird bei einer Aktion aufgerufen und actionPerfomed gibt dabei an welche Aktion
     */
    @Override
    public void actionPerformed(ActionEvent actionPerformed) {
        // pluginButton clicked
        if (actionPerformed.getSource() == pluginButton) {
            onPlugin();
        }

        // helpButton clicked
        if (actionPerformed.getSource() == helpButton) {
            onHelp();
        }

        // connectButton clicked
        if (actionPerformed.getSource() == connectButton) {
            onConnect();
        }
    }

    /**
     * @return Gibt das erste IPfeld zurueck
     */
    public JTextField getIp1() {
        return ip1;
    }

    /**
     * @return Gibt das zweite IPfeld zurueck
     */
    public JTextField getIp2() {
        return ip2;
    }

    /**
     * @return Gibt das dritte IPfeld zurueck
     */
    public JTextField getIp3() {
        return ip3;
    }

    /**
     * @return Gibt das vierte IPfeld zurueck
     */
    public JTextField getIp4() {
        return ip4;
    }

}
