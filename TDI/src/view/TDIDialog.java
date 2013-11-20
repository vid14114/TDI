package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

public class TDIDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = -1221054153637014114L;
	String[] plugin;
	
	public TDIDialog(String[] plugin){
		
	}

	public void onWelcome() {
		throw new UnsupportedOperationException();
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
	 * Closes the dialog
	 */
	public void onClose() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Opens the onPlugin button
	 */
	public void onPlugin() {
		throw new UnsupportedOperationException();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}