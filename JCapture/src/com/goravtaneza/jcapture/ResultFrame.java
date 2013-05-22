package com.goravtaneza.jcapture;

import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import com.goravtaneza.jcapture.util.FileUtil;

/**
 * 
 * Frame to display the capture preview
 * 
 * @author Gorav Taneza
 *
 */
@SuppressWarnings("serial")
public class ResultFrame extends JFrame implements IConstants{
	
	JLabel picture;
	JScrollPane scrollPane;

	public ResultFrame()
	{
		super(CAPTURE_RESULT_WINDOW_TITLE);
	}

	public void displayCapture(File file) {
		picture = new JLabel(new ImageIcon(FileUtil.findAndReplace(file.getAbsolutePath(),"\\","/",true)));
		scrollPane = new JScrollPane(picture);
		this.getContentPane().removeAll();
		this.getContentPane().add(scrollPane);
		this.pack();
		this.setVisible(true);
		setResizable(false);
	}
	
	
}
