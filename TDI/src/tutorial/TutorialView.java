/**
 * 
 */
package tutorial;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import javax.swing.JTextArea;
import javax.swing.JWindow;

/**
 * @author abideen
 *
 */
public class TutorialView{	
	JTextArea jt;
	JWindow w;
	
	public TutorialView(){
		//Initialize a window and a textfield
		w = new JWindow();
		jt = new JTextArea(6,1);
		//Setting basic properties		
		w.setSize(350,100);
		w.setLocation((int)GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth() - 
				  150, 0);
		w.setBackground(Color.GRAY);
		jt.setFont(new Font(null, Font.BOLD, 14));
		jt.setText("Welcome to TDI Tutorial\nPlease wait while the tutorial is loading");
		w.add(jt);
		
		w.setVisible(true);
		w.setAlwaysOnTop(true);
	}
	
	public void setText(String text){
		jt.setText(text);
		w.repaint();
	}		
}
