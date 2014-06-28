package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


/**
 * Die MusicDialog Klasse zeigt den Dialog f�r die Musik an
 * 
 * @author TDI Team
 */

public class MusicDialog extends JDialog implements ActionListener, Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2621802262329154580L;

	/**
	 * Der Knopf um das vorherige Lied abzuspielen
	 */
	JButton previousButton = new JButton();
	
	/**
	 * Der Knopf um das naechste Lied abzuspielen
	 */
	JButton nextButton = new JButton();
	
	/**
	 * Der Knopf um zu pausieren und weiter zu spielen
	 */
	JButton togglePauseButton = new JButton();
	
	/**
	 * Das Lied das gerade spielt
	 */
	JLabel musicLabel = new JLabel();
	
	/**
	 * Die ID des TDIs
	 */
	float id;
	
	/**
	 * X-Positon des TDIs
	 */
	float xPos;
	
	/**
	 * Y-Position des TDIs
	 */
	float yPos;
	
	File[] audioFiles;
	int audioIndex = 0;
	Player p;
	boolean playing;
	boolean moving;
	
	/**
	 * Z-Position des TDIs
	 */
	float zPos;
	
	/**
	 * Die Kompensation damit nicht jede kleinste Bewegung als Befehl zaehlt
	 */
	int compensation;
	
	/**
	 * Der Musikdialog
	 */
	public MusicDialog(){
		super();
		id=-1;
		xPos=-1;
		yPos=-1;
		zPos=-1;
		compensation = 50;
		setTitle("Tangible Musicplayer");
		setSize(600,600);
		setLocation(400, 200);		
		setContentPane(musicPane());
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		loadAudioFiles();
	}
	
	private static File[] returnMP3Files() {
		return new File(System.getProperty("user.home")+ "/Music").listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".mp3");
			}
		});
	}
	
	private void loadAudioFiles() {
		audioFiles = returnMP3Files();
		if(audioFiles == null || audioFiles.length == 0) 
			JOptionPane.showMessageDialog(this, "No mp3 files found in Music directory");
		else{
			try {				
				p = new Player(new FileInputStream(audioFiles[audioIndex]));
				musicLabel.setText(audioFiles[audioIndex].getName());
			} catch (JavaLayerException | IOException e) {}
		}		
	}

	/**
	 * @return der Inhalt das MusikDialogs
	 */
	public JPanel musicPane(){
		JPanel main = new JPanel();
		main.setLayout(new BorderLayout());
		
		previousButton.setText("Previous");
		nextButton.setText("Next");
		togglePauseButton.setText("Stop/Play");
					
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
	
	/**
	 * Schaut ob sich das TDI bewegt
	 * @param id um das TDi zu identifizieren
	 * @param x die neue X-Position
	 * @param y die neue Y-Position
	 * @param z die neue Z-Position
	 */
	public boolean TDIMoved(float id,float x,float y,float z){
		if(this.id==-1){
			this.id=id;
			this.xPos = x;
			this.yPos = y;
			this.zPos = z;
			return false; 
		}
		if(this.id==id && !moving){
			//move right
			if(x>this.xPos+compensation){
				nextSong();
				moving = true;
				return true;
			}
			//move left
			if(x<this.xPos-compensation){
				previousSong();
				moving = true;
				return true;
			}
			//toggle
			if(y>this.yPos+compensation){
				play();
				moving = true;
				return true;
			}
		}
		else if(this.id == id && x < xPos-20 || x > xPos+20 || y > yPos+20 )
			moving = false;
		return false; 
	}	

	/**
	 * @return the id
	 */
	public float getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(float id) {
		this.id = id;
	}

	/**
	 * @return the x
	 */
	public float getXPos() {
		return xPos;
	}


	/**
	 * @return the y
	 */
	public float getYPos() {
		return yPos;
	}

	/**
	 * @return the z
	 */
	public float getZPos() {
		return zPos;
	}

	/**
	 * Falls ein Knopf gedrueckt wird
	 */
	@Override
	public void actionPerformed(ActionEvent actionPerformed) {
		if(actionPerformed.getSource() == previousButton)
			previousSong();
		
		if(actionPerformed.getSource() == nextButton)
			nextSong();
		
		if(actionPerformed.getSource() == togglePauseButton)
			play();
	}
	
	private void previousSong() {
		p.close();
		if(audioIndex == 0) audioIndex = audioFiles.length-1;
		else if(audioIndex == audioFiles.length-1) audioIndex = 0;
		else audioIndex--;
		playing = true;
		musicLabel.setText(audioFiles[audioIndex].getName());
		new Thread(this).start();
	}

	private void nextSong(){
		p.close();
		if(audioIndex == audioFiles.length-1) audioIndex = 0;
		else audioIndex++;
		playing = true;
		musicLabel.setText(audioFiles[audioIndex].getName());
		new Thread(this).start();
	}
	
	private void play(){
		if(!playing)
			new Thread(this).start();
		else
			p.close();
		playing = !playing;
	}

	@Override
	public void run() {
		try {
			p = new Player(new FileInputStream(audioFiles[audioIndex]));
			p.play();
		} catch (JavaLayerException e) {
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		p.close();
		super.finalize();
	}
}
