package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.PluginTableModel;

public class TDIActionListener implements ActionListener{
	PluginTableModel pluginTableModel;
	
	public PluginTableModel getPluginTableModel() {
		return pluginTableModel;
	}
	
	public TDIActionListener(PluginTableModel pluginTableModel){
		this.pluginTableModel=pluginTableModel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
