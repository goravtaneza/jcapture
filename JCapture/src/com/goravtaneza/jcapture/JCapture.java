package com.goravtaneza.jcapture;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;
import javax.swing.border.TitledBorder;
import com.goravtaneza.jcapture.util.CustomToolTip;
import com.goravtaneza.jcapture.util.JStatusBar;
import com.goravtaneza.jcapture.util.JTextFieldFilter;
import com.goravtaneza.jcapture.util.WindowHelper;


/**
 * JCapture
 * 
 * 
 * This code is Copyright 2013 by Gorav Taneza (tech@goravtaneza.com) and may be
 * redistributed or modified in any form without restrictions as long as the portion of this
 * comment from this paragraph through the end of the comment is not removed.  The author
 * requests that he be notified of any application, applet, or other binary that makes use of
 * this code, but that's more out of curiosity than anything and is not required.  This software
 * includes no warranty.  The author is not responsible for any loss of data or functionality
 * or any adverse or unexpected effects of using this software.
 * 
 * 
 * @author Gorav Taneza (<a href="mailto:tech@goravtaneza.com">tech@goravtaneza.com</a>)
 * @version 2.0
 */

    /**
     * Main class for the JCapture utility
     *  
     * @author Gorav Taneza
     *
     */
	@SuppressWarnings("serial")
	public class JCapture extends JFrame implements IConstants{
	   
		private JTextField timerField;
		private JRadioButton captureAndShowRadio;
		private JRadioButton captureAndSaveRadio;
		private ButtonGroup resultSelectionGroup;
		private ButtonGroup activationSelectionGroup;
		private ButtonGroup areaSelectionGroup;
		private JRadioButton instantCaptureRadio;
		private JRadioButton timedCaptureRadio;
		private JRadioButton fullScreenSelectionRadio;
		private JRadioButton customSelectionRadio;
		private JPanel resultsPanel;        
		private JPanel activationPanel;
		private JPanel selectionPanel;
		private JPanel bottomPanel;
		private JLabel secondsLabel;
		private JButton startButton;
		private JButton quitButton;
		private JButton aboutButton;
		private JStatusBar statusBar;
		private JLabel leftStatusBarLabel;
		
		public JCapture() {
			super(TITLE);
			createComponents();
		}		
		
		public static void main(String args[]) {
	        new JCapture();			
		}

		private void createComponents() {
			
				statusBar = new JStatusBar();
			    resultsPanel = new JPanel();        
				activationPanel = new JPanel();
				selectionPanel = new JPanel();
				bottomPanel = new JPanel();
				leftStatusBarLabel = new JLabel("Full screen");
				resultsPanel.setLayout(new BoxLayout(resultsPanel,BoxLayout.X_AXIS));
				activationPanel.setLayout(new BoxLayout(activationPanel,BoxLayout.X_AXIS));
				selectionPanel.setLayout(new BoxLayout(selectionPanel,BoxLayout.X_AXIS));
				TitledBorder resultsTitledBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), RESULT_BORDER_TITLE);
				TitledBorder activationTitledBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ACTIVATION_BORDER_TITLE);
				TitledBorder selectionTitledBorder =  BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), SELECTION_BORDER_TITLE);
				resultsPanel.setBorder(resultsTitledBorder);
				activationPanel.setBorder(activationTitledBorder);
				selectionPanel.setBorder(selectionTitledBorder);
		        statusBar.setLeftComponent(leftStatusBarLabel);
				
				// area selection radio group
				fullScreenSelectionRadio = new JRadioButton(FULLSCREEN_AREA_CAPTURE_LABEL){
				// override to use custom ToolTip
		            public JToolTip createToolTip() { 
		                return(new CustomToolTip(this));
		            } 
				};
				fullScreenSelectionRadio.setMnemonic(KeyEvent.VK_F);
				fullScreenSelectionRadio.setActionCommand("9");
				fullScreenSelectionRadio.setSelected(true);
				fullScreenSelectionRadio.setToolTipText(TOOLTIP_FULLSCREEN_CAPTURE);
				
				customSelectionRadio = new JRadioButton(CUSTOM_AREA_CAPTURE_LABEL){
				// override to use custom ToolTip
		            public JToolTip createToolTip() { 
		                return(new CustomToolTip(this));
		            } 
				};
				customSelectionRadio.setMnemonic(KeyEvent.VK_C);
				customSelectionRadio.setActionCommand("10");
				customSelectionRadio.setSelected(false);
				customSelectionRadio.setToolTipText(TOOLTIP_FULLSCREEN_CAPTURE);
				
				areaSelectionGroup = new ButtonGroup();
				areaSelectionGroup.add(fullScreenSelectionRadio);
				areaSelectionGroup.add(customSelectionRadio);
				selectionPanel.add(fullScreenSelectionRadio);
				selectionPanel.add(customSelectionRadio);
				//capture type radio group			
				captureAndShowRadio = new JRadioButton(CAPTURE_AND_SHOW_LABEL){
				// override to use custom ToolTip
		            public JToolTip createToolTip() { 
		                return(new CustomToolTip(this));
		            } 
				};
		        captureAndShowRadio.setMnemonic(KeyEvent.VK_C);
		        captureAndShowRadio.setActionCommand("1");
		        captureAndShowRadio.setSelected(true);
				captureAndShowRadio.setToolTipText(TOOLTIP_CAPTURE_AND_SHOW);
				
				captureAndSaveRadio = new JRadioButton(CAPTURE_AND_SAVE_LABEL){
				// override to use custom ToolTip
		            public JToolTip createToolTip() { 
		                return(new CustomToolTip(this));
		            } 
				};
		        captureAndSaveRadio.setMnemonic(KeyEvent.VK_A);
		        captureAndSaveRadio.setActionCommand("2");
		        captureAndSaveRadio.setSelected(false);
				captureAndSaveRadio.setToolTipText(TOOLTIP_CAPTURE_AND_SAVE); 

				resultSelectionGroup = new ButtonGroup();
				resultSelectionGroup.add(captureAndShowRadio);
				resultSelectionGroup.add(captureAndSaveRadio);
				resultsPanel.add(captureAndShowRadio);		
				resultsPanel.add(captureAndSaveRadio);

				instantCaptureRadio = new JRadioButton(INSTANT_LABEL){
				// override to use custom ToolTip
		            public JToolTip createToolTip() { 
		                return(new CustomToolTip(this));
		            } 

				};
		        instantCaptureRadio.setMnemonic(KeyEvent.VK_I);
		        instantCaptureRadio.setActionCommand("3");
		        instantCaptureRadio.setSelected(true);
				instantCaptureRadio.setToolTipText(TOOLTIP_TEXT_INSTANT);		

	     		timedCaptureRadio = new JRadioButton(TIMER_LABEL){
				// override to use custom ToolTip
		            public JToolTip createToolTip() { 
		                return(new CustomToolTip(this));
		            } 

				};
				timedCaptureRadio.setMnemonic(KeyEvent.VK_T);
		        timedCaptureRadio.setActionCommand("5");
		        timedCaptureRadio.setSelected(false);	
				timedCaptureRadio.setToolTipText(TOOLTIP_TEXT_TIMER);

				activationSelectionGroup = new ButtonGroup();		
				activationSelectionGroup.add(instantCaptureRadio);
				activationSelectionGroup.add(timedCaptureRadio);
				timerField = new JTextField("3",3){
				// override to use custom ToolTip
		            public JToolTip createToolTip() { 
		                return(new CustomToolTip(this));
		            } 

				};

				timerField.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC));
				secondsLabel = new JLabel(SECONDS_LABEL);
				timerField.setToolTipText(TOOLTIP_TEXT_TIMER_VALUE);
				activationPanel.add(instantCaptureRadio);
				activationPanel.add(timedCaptureRadio);
				activationPanel.add(timerField);
				activationPanel.add(secondsLabel);
				ToolTipManager.sharedInstance().setInitialDelay(0);
				
				startButton = new JButton(START_LABEL){
				// override to use custom ToolTip
		            public JToolTip createToolTip() { 
		                return(new CustomToolTip(this));
		            } 

				};
				startButton.setPreferredSize(new Dimension(20, 30));
				startButton.setMnemonic(KeyEvent.VK_S);
		        startButton.setActionCommand("6");
				startButton.setToolTipText(TOOLTIP_START_TEXT);	

				quitButton = new JButton(QUIT_LABEL){
				// override to use custom ToolTip
		            public JToolTip createToolTip() { 
		                return(new CustomToolTip(this));
		            } 

				};
				quitButton.setPreferredSize(new Dimension(20, 30));
				quitButton.setMnemonic(KeyEvent.VK_Q);
		        quitButton.setActionCommand("7");	

				aboutButton = new JButton(ABOUT_LABEL){
				// override to use custom ToolTip
		            public JToolTip createToolTip() { 
		                return(new CustomToolTip(this));
		            } 

				};
				aboutButton.setPreferredSize(new Dimension(20, 30));
				aboutButton.setToolTipText(TOOLTIP_ABOUT_TEXT);
				aboutButton.setActionCommand("8");
				
				EventListener myListener = new EventListener(this);
				captureAndShowRadio.addActionListener(myListener);
				captureAndSaveRadio.addActionListener(myListener);
				instantCaptureRadio.addActionListener(myListener);
				timedCaptureRadio.addActionListener(myListener);
				startButton.addActionListener(myListener);
				quitButton.addActionListener(myListener);
				aboutButton.addActionListener(myListener);
				customSelectionRadio.addActionListener(myListener);
				fullScreenSelectionRadio.addActionListener(myListener);
			    this.addWindowListener(new WindowAdapter() {
		            public void windowClosing(WindowEvent e) {
		                System.exit(0);
		            }
		        });
			    
			    GridLayout gridLayout = new GridLayout(1, 3);
			    bottomPanel.setLayout(gridLayout);
				bottomPanel.add(startButton);
				bottomPanel.add(quitButton);
				bottomPanel.add(aboutButton);
				
				selectionPanel.setBounds(5, 0, 240, 50);
				resultsPanel.setBounds(245,0,170, 50);
				activationPanel.setBounds(415,0,250,50);
				bottomPanel.setBounds(470, 50, 200, 25);
				statusBar.setBounds(0, 75, 670, 30);
								
				this.getContentPane().add(selectionPanel);
				this.getContentPane().add(resultsPanel);
				this.getContentPane().add(activationPanel);
				this.getContentPane().add(bottomPanel);
				this.getContentPane().add(statusBar);
				
		        setLayout(null);
				setResizable(false);
				WindowHelper.centreWindow(this);
			    this.pack();
			    setSize(670,120);
		        this.setVisible(true);  	

		}
		
		protected Rectangle getCaptureArea() {
			if(fullScreenSelectionRadio.isSelected()){
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				return new Rectangle(0, 0, dim.width, dim.height);
			}else{
				return AreaCaptureFrame.getSelectedRect();
			}
		}
		
		protected void setStatusBarText(String text){
			leftStatusBarLabel.setText(text);
		}
		
		protected boolean isCaptureAndShow() {
			return captureAndShowRadio.isSelected();
		}
		
		protected void doClickCaptureAndShowRadio() {
			captureAndShowRadio.doClick();
		}
				
		protected String getTimerValue()
		{
			return timerField.getText();
		}
		
	}	 

	