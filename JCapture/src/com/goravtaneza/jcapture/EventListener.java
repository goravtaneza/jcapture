package com.goravtaneza.jcapture;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.goravtaneza.jcapture.util.FileUtil;
import com.goravtaneza.jcapture.util.ImageFilter;
import com.goravtaneza.jcapture.util.MessageDialog;

/**
 * 
 * @author Gorav Taneza
 *
 */
class EventListener implements ActionListener, IConstants {

	private int cond;
	private int last = 0;
	boolean displayMode = false;
	private int activation = 1;
	private JFileChooser fc;
	private static File saveFile = null;
	private javax.swing.Timer t;
	private JCapture mainFrame;
	private static ResultFrame resultFrame;
	
	public EventListener(JCapture frame) {
		mainFrame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		
		Rectangle captureArea= mainFrame.getCaptureArea();

		try {
			cond = Integer.parseInt(e.getActionCommand());
			if (last != cond || last == 0 || cond == 6 || cond == 7 || cond == 8) {

				switch (cond) {

				case 1:

					displayMode = true;

					break;

				case 3:

					activation = 1;

					break;

				case 4:

					activation = 2;

					break;

				case 5:

					activation = 2;

					break;

				case 2:

					fc = new JFileChooser();
					fc.setDialogTitle("Choose a directory");
					fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					fc.setFileHidingEnabled(false);
					fc.addChoosableFileFilter(new ImageFilter());
					int returnVal = fc.showSaveDialog(mainFrame);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						saveFile = fc.getSelectedFile();
						String extension = FileUtil.getExtension(saveFile);
						if(extension!=null && (extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png"))){
							displayMode = false;
							// do nothing...
						} else {
							displayMode = true;
							mainFrame.doClickCaptureAndShowRadio();
							MessageDialog.showMessageDialog(mainFrame, "Incorrect file extension", "File names should have .jpeg, .jpg or .png extension", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						mainFrame.doClickCaptureAndShowRadio();
					}
					break;

				case 6:

					if (mainFrame.isCaptureAndShow() && activation == 1) {
						mainFrame.setState(Frame.ICONIFIED);
						//mainFrame.setVisible(false);
						try {
							Thread.sleep(300);
						} catch (Exception ef) {
						}

						try {
							saveFile = File.createTempFile("temp", ".jpg");
							saveFile.deleteOnExit();
						} catch (IOException io) {
							System.out.println("Error Creating Temp File");
						}

						String extension = FileUtil.getExtension(saveFile);
						CaptureHelper gimg = new CaptureHelper(extension,captureArea);
						gimg.writeImage(saveFile);
						mainFrame.setState(Frame.NORMAL);
						//mainFrame.setVisible(true);
						if(resultFrame==null){
							resultFrame = new ResultFrame();
						}
						resultFrame.displayCapture(saveFile);
					}

					if (!mainFrame.isCaptureAndShow() && activation == 1) {

						mainFrame.setState(Frame.ICONIFIED);
						//mainFrame.setVisible(false);
						try {
							Thread.sleep(300);
						} catch (Exception ef) {
						}
						try {
							String extension = FileUtil.getExtension(saveFile);
							CaptureHelper gimg = new CaptureHelper(extension,captureArea);
							gimg.writeImage(saveFile);
						} catch (Exception e1) {
						}
						mainFrame.setState(Frame.NORMAL);
						//mainFrame.setVisible(true);
						mainFrame.doClickCaptureAndShowRadio();
						MessageDialog.showMessageDialog(mainFrame, "File Saved", "File saved at: " + saveFile.getAbsolutePath(), JOptionPane.INFORMATION_MESSAGE);
					}

					if (activation == 2)
					{
						String timerValueStr = mainFrame.getTimerValue();
						if(timerValueStr==null || timerValueStr.length()==0){
							MessageDialog.showMessageDialog(mainFrame, "Enter timer value", "Enter timer value in seconds", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						mainFrame.setState(Frame.ICONIFIED);
						//mainFrame.setVisible(false);
						displayMode=false;
						int timeIntValue = Integer.parseInt(timerValueStr);
						t = new javax.swing.Timer(timeIntValue * 1000, new EventListener(mainFrame));
						t.setActionCommand("6");
						t.setRepeats(false);
						t.start();
						break;
					}
					break;

				case 7:
					System.exit(0);
					break;

				case 8:
					new AboutDialog(mainFrame).showDialog();
					break;
					
				case 9:
					mainFrame.setStatusBarText("Full screen");
					break;
					
				case 10:
						try {
							saveFile = File.createTempFile("temp", ".jpg");
							saveFile.deleteOnExit();
						} catch (IOException io) {
							System.out.println("Error Creating Temp File");
						}
 						mainFrame.setState(Frame.ICONIFIED);
 						Thread.sleep(500);
						Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
						CaptureHelper captureHelper = new CaptureHelper("jpg",new Rectangle(0, 0, dim.width, dim.height));
						captureHelper.writeImage(saveFile);
						String fullPath = FileUtil.findAndReplace(saveFile.getAbsolutePath(),"\\","/",true);
						Image image = captureHelper.readImage(new File(fullPath));
						new AreaCaptureFrame(mainFrame, image);
 						mainFrame.setState(Frame.ICONIFIED);	
     					break;
				}
				last = cond;

			}

		} catch (NumberFormatException nex) {
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
