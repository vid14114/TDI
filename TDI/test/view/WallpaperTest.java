package view;

import static org.junit.Assert.*;

import java.util.ArrayList;

import model.ConfigLoader;

import org.junit.Test;

import controller.BigLogic;
import controller.Executor;

public class WallpaperTest {

    @Test
    public void test() throws InterruptedException {
    	BigLogic b=new BigLogic();
    	TDI t1=new TDI((byte) 49, 124, 357, 763, null);
    	TDI t2=new TDI((byte) 50, 189, 65, 34, null);
    	ArrayList<TDI> tdis=new ArrayList<>();
    	tdis.add(t1);
    	tdis.add(t2);
    	b.setTdis(tdis);
    	b.splitIcons();
    	b.getTdis().get(0).rotateIconsClockwise();
    	b.getTdis().get(0).rotateIconsClockwise();
    	b.refreshBackground();
    	Thread.sleep(3000);
    	b.getTdis().get(0).rotateIconsCounterClockwise();
    	b.getTdis().get(1).rotateIconsClockwise();
    	b.refreshBackground();
    	while(true);
    }

}
