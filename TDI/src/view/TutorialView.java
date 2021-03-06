package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.JTextArea;
import javax.swing.JWindow;

/**
 * Die TutorialView Klasse zeigt das Tutorial an
 * 
 * @author TDI Team
 */

public class TutorialView {
	/**
	 * Das Textfeld
	 */
	private final JTextArea jt;
	/**
	 * Das Fenster
	 */
	private final JWindow w;

	/**
	 * Konstruktor von TutorialView
	 */
	public TutorialView() {
		// Initialize a window and a textfield
		w = new JWindow();
		jt = new JTextArea(6, 1);
		// Setting basic properties
		w.setSize(350, 100);
		w.setLocation((int) GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds().getWidth() - 150, 0);
		w.setBackground(Color.GRAY);
		jt.setFont(new Font(null, Font.BOLD, 14));
		jt.setText("Willkommen zum Tutorial.\n Hier erfahren Sie, wie du die TDIs zum Steuern des Desktops und den Programmen nutzen kannst.Bitte warten Sie, waehrend das Tutorial wird geladen");
		jt.setLineWrap(true);
		w.add(jt);

		w.setVisible(true);
		w.setAlwaysOnTop(true);
	}
	
	public void exit(){
		jt.setVisible(false);
		w.dispose();
	}

	/**
	 * @param text
	 *            das ist der Text auf den der derzeitige gesetzt wird
	 */
	public void setText(String text) {
		jt.setText(text);
		w.repaint();
	}
}
