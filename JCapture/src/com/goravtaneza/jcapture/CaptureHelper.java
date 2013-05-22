package com.goravtaneza.jcapture;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 
 * Utility class to perform capture via the {@link Robot} class
 * 
 * @author Gorav Taneza
 *
 */
public class CaptureHelper {

	private Robot robot;
	private String extension;
	private Rectangle capturearea;
	
	public CaptureHelper(String ext, Rectangle p_capturearea) throws AWTException 
	{
		extension=ext;
		capturearea=p_capturearea;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			throw e;
		}
	}

	public void writeImage(File file) throws HeadlessException, IllegalArgumentException, IOException
	{
		try {
			BufferedImage bfrimg = robot.createScreenCapture(capturearea);
			ImageIO.write(bfrimg, extension, file);
		} catch (HeadlessException he) {
			throw he;
		} catch (IllegalArgumentException iae) {
			throw iae;
		} catch (IOException ioe) {
			throw ioe;
		}
	}
	
	public Image readImage(File file) throws IOException{
		Image image=ImageIO.read(file);
		return image;
	}
}
