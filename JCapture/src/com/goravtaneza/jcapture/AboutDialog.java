package com.goravtaneza.jcapture;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.goravtaneza.jcapture.util.BrowserLauncher;
import com.goravtaneza.jcapture.util.WindowHelper;

@SuppressWarnings("serial")
public class AboutDialog extends JDialog implements ActionListener, MouseMotionListener, MouseListener, IConstants {
	
	JLabel aboutLabel;
	JButton closeButton;
	JPanel bottomPanel;
	JPanel topPanel;
	boolean readyForClick;
		
	public AboutDialog(Frame frame) {
			super(frame,ABOUT_DIALOG_TITLE);
			setAlwaysOnTop(true);
			setModalityType(ModalityType.APPLICATION_MODAL);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setResizable(false);
			initComponents();
			setSize(350, 230);
			WindowHelper.centreWindow(this);
			Point point = getLocation();
			setLocation(new Point(point.x+50,point.y+50));
	}
	
	private void initComponents(){
			bottomPanel = new JPanel();
			topPanel = new JPanel();
		    closeButton=new JButton(ABOUT_CLOSE_BUTTON_LABEL);
		    closeButton.addActionListener(this);
		    closeButton.setActionCommand(ABOUT_CLOSE_BUTTON_LABEL);
		    closeButton.setBounds(0, 0, 10, 10);
		    bottomPanel.add(closeButton);
			aboutLabel= new JLabel();
			aboutLabel.setText("<html><b><center>JCapture</center></b><br><center>Version " + VERSION + "</center><br><center>(c) 2013 Gorav Taneza<br><br></center><a href=\"www.goravtaneza.com\">www.goravtaneza.com</a><br><br><center>All rights reserved.</center></html>");
			topPanel.add(aboutLabel);
			aboutLabel.addMouseListener(this);
			aboutLabel.addMouseMotionListener(this);
			Container contentPane = getContentPane( );
		    contentPane.add(topPanel, BorderLayout.CENTER);
		    contentPane.add(bottomPanel,BorderLayout.SOUTH);
		    pack();
			
	}
	
	public void showDialog(){
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand()!=null && e.getActionCommand().equals(ABOUT_CLOSE_BUTTON_LABEL)){
			this.dispose();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {

		if((e.getPoint().x>0 && e.getPoint().x<140) && (e.getPoint().y>100 && e.getPoint().y<110))
		{
			aboutLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			readyForClick=true;
        } else {
        	aboutLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        	readyForClick=false;
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(readyForClick){
			try {			
				BrowserLauncher.openURL(ABOUT_URL);
			} catch (IOException e1) {
				//not a big deal, ignore it...
			}
			dispose();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
