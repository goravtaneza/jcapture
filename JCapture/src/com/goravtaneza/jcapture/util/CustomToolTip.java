package com.goravtaneza.jcapture.util;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JToolTip;


/**
 * 
 * Override JToolTip for a custom background 
 * 
 * @author Gorav Taneza
 *
 */
@SuppressWarnings("serial")
public class CustomToolTip extends JToolTip { 
	// Constructor
	public CustomToolTip(JComponent component) { 
		super();
		setComponent(component);
		setBackground(Color.white);
	} 
} 

