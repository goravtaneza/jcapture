package com.goravtaneza.jcapture.util;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MessageDialog {
	
		public static void showMessageDialog(JFrame frame, String title, String message, int messageType){
			JOptionPane.showMessageDialog(frame, message, title, messageType);		
	}
	
}
