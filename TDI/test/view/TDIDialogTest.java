package view;

import static org.junit.Assert.*;
import model.ConfigLoader;
import model.TDILogger;

import org.junit.Test;

public class TDIDialogTest {

	@Test
	public void test() {
		Object rowData[][] = { { "Music", true }, { "Anilator", true }, { "PlugMeIn!", false },
	      	      { "Bästewogibtplugin", true }, { "Plugin Nr.5", false }, };
		TDIDialog t=new TDIDialog(rowData);
		while(true){
			
		}
	}

}
