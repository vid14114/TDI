package view;

import model.PluginTableModel;

import org.junit.Test;

import controller.BigLogic;

class TDIDialogTest {

	@Test
	public void test() {
		final Object rowData[][] = { { "Music", true }, { "Anilator", true },
				{ "PlugMeIn!", false }, { "Baestewogibtplugin", true },
				{ "Plugin Nr.5", false }, };
		final PluginTableModel pluginTableModel = new PluginTableModel(rowData);
		new TDIDialog(new BigLogic(), pluginTableModel);
		// TDIDialog t = new TDIDialog();
		while (true) {

		}
	}

}
