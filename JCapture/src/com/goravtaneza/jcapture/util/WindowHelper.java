package com.goravtaneza.jcapture.util;

import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;

public class WindowHelper {

	public static void centreWindow(Window frame) {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) (dimension.getWidth() / 3);
	    int y = (int) (dimension.getHeight() / 3);
	    frame.setLocation(x, y);
	}
	
	public static void resizeFullScreen(Window window) {
	    //Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    //window.setSize(dimension);
	    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	    if (gd.isFullScreenSupported()) {
    			gd.setFullScreenWindow(window);
       	}
	}
	
	public static boolean isTransparencySupported() {
		 GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		 GraphicsDevice gd = ge.getDefaultScreenDevice();
		 return gd.isWindowTranslucencySupported(TRANSLUCENT);
	}	
}


