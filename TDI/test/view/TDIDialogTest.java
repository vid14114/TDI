package view;

import model.PluginTableModel;

import org.junit.Test;

import controller.BigLogic;

public class TDIDialogTest {

	@Test
	public void test() {
		Object rowData[][] = { { "MPC Plugin", true },
				{ "Libreoffice Plugin", true }, { "VLC Player plugin", false },
				{ "Firefox plugin", false } };
		PluginTableModel pluginTableModel = new PluginTableModel(rowData);
		new TDIDialog(new BigLogic(), pluginTableModel);
		// TDIDialog t = new TDIDialog();
		while (true) {

		}
	}

}
