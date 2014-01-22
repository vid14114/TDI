package view;

import model.ConfigLoader;
import model.PluginTableModel;

import org.junit.Test;

import controller.TDIActionListener;

public class TDIDialogTest {

	@Test
	public void test() {
		Object rowData[][] = { { "Music", true }, { "Anilator", true },
				{ "PlugMeIn!", false }, { "B�stewogibtplugin", true },
				{ "Plugin Nr.5", false }, };
		PluginTableModel pluginTableModel = new PluginTableModel(rowData);
		new TDIDialog(new TDIActionListener(pluginTableModel));
		//TDIDialog t = new TDIDialog();
		while (true) {

		}
	}

}
