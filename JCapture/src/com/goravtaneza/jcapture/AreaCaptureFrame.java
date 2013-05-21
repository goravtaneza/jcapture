package com.goravtaneza.jcapture;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import com.goravtaneza.jcapture.util.WindowHelper;

@SuppressWarnings("serial")
public class AreaCaptureFrame extends JFrame {
	
	private static Rectangle selectedRect;
	private JCapture mainFrame;
	private Image image;
	
	public AreaCaptureFrame(JCapture p_parent, Image p_image) {
		mainFrame = p_parent;
		image=p_image;				
		setUndecorated(true);
		pack();
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		WindowHelper.resizeFullScreen(this);
		add(new DragPanel(this,image));
		//setOpacity(0.4f);
	}
	
	public static Rectangle getSelectedRect(){
		return selectedRect;
	}

class DragPanel extends JPanel implements MouseInputListener, ActionListener, IConstants {
	
	private Point currentPoint;
	private Point pressedPoint;
	private boolean isMouseDragged;
	private JButton confirmButton;
	private int x;
	private int y;
	private int width;
	private int height;
	private JFrame frame;
	private Image image;
	
	public DragPanel(JFrame p_frame, Image p_image) {
		frame = p_frame;
		image= p_image;
		confirmButton = new JButton(AREA_CAPTURE_CONFIRM_BUTTON_LABEL);
		confirmButton.setVisible(false);
		confirmButton.setBorderPainted(false);
		confirmButton.setFocusPainted(false);
		confirmButton.setContentAreaFilled(true);
		confirmButton.setOpaque(true);
		confirmButton.setBackground(Color.RED);
		confirmButton.addActionListener(this);
		confirmButton.setActionCommand(AREA_CAPTURE_CONFIRM_BUTTON_LABEL);
		confirmButton.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		add(confirmButton);
		addMouseListener(this);
		addMouseMotionListener(this);
		repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		isMouseDragged = true;
		currentPoint = e.getPoint();
		confirmButton.setVisible(false);
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		confirmButton.setVisible(false);
		pressedPoint = e.getPoint();
		currentPoint = pressedPoint;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(isMouseDragged){
			confirmButton.setVisible(true);
		}
		isMouseDragged =false;
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image,0,0,this);
		if(pressedPoint!=null){
			g.setColor(Color.RED);
			x=pressedPoint.x;
			y=pressedPoint.y;
			width = currentPoint.x-pressedPoint.x;
			height =  currentPoint.y-pressedPoint.y;
			if(currentPoint.x < pressedPoint.x){
					x = currentPoint.x;
					width = pressedPoint.x - currentPoint.x;
			}
			if(currentPoint.y < pressedPoint.y){
					y =	currentPoint.y;
					height = pressedPoint.y - currentPoint.y;
			}			
			g.drawRect(x, y, width, height);
			g.drawString(width + " " + height,currentPoint.x + 5 , currentPoint.y + 12);
			confirmButton.setLocation(currentPoint.x, currentPoint.y + 20);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(AREA_CAPTURE_CONFIRM_BUTTON_LABEL)){
			selectedRect = new Rectangle(x,y,width,height);
			frame.dispose();
			mainFrame.setState(Frame.NORMAL);
			mainFrame.setStatusBarText("Selected area : " + width + " x " + height);
		}
		
	}
}

}


