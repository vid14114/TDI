package main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MusicDialog extends JDialog implements ActionListener{
	JButton previousButton = new JButton();
	JButton nextButton = new JButton();
	JButton togglePauseButton = new JButton();
	JLabel musicLabel = new JLabel();
	public MusicDialog(){
		super();
		setTitle("Tangible Musicplayer");
		setSize(600,600);
		setLocation(400, 200);
		setContentPane(musicPane());
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
	}
	
	public JPanel musicPane(){
		JPanel main = new JPanel();
		main.setLayout(new BorderLayout());
		
		previousButton.setText("Previous");
		nextButton.setText("Next");
		togglePauseButton.setText("Pause/Play");
		musicLabel.setText("DJ Anil - Anihilate them!");
		
		musicLabel.setHorizontalAlignment(JLabel.CENTER);
		
		previousButton.setFocusPainted(false);
		nextButton.setFocusPainted(false);
		togglePauseButton.setFocusPainted(false);
		
		previousButton.addActionListener(this);
		nextButton.addActionListener(this);
		togglePauseButton.addActionListener(this);
		
		main.add(previousButton,BorderLayout.EAST);
		main.add(nextButton,BorderLayout.WEST);
		main.add(musicLabel,BorderLayout.CENTER);
		main.add(togglePauseButton,BorderLayout.SOUTH);
		return main;
	}

	@Override
	public void actionPerformed(ActionEvent actionPerformed) {
		if(actionPerformed.getSource() == previousButton){
		}
		if(actionPerformed.getSource() == nextButton){
		}
		if(actionPerformed.getSource() == togglePauseButton){
		}
	}
}
