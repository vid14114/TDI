package main;

public class Main {

	public static void main(String[] args) {
		MusicDialog m = new MusicDialog();
		new Thread(new PluginServer(m)).start();
	}
}
