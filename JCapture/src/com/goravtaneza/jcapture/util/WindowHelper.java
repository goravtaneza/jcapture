package com.goravtaneza.jcapture.util;

import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * 
 * Helper class to center, resize among other awt/window utility methods
 * 
 * @author Gorav Taneza <tech@goravtaneza.com>
 *
 */
public class WindowHelper {

	public static void centreWindow(Window frame) {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) (dimension.getWidth() / 3);
	    int y = (int) (dimension.getHeight() / 3);
	    frame.setLocation(x, y);
	}
	
	public static void resizeFullScreen(Window window) {
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


