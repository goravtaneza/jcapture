package com.goravtaneza.jcapture.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * 
 * @author Gaurav Taneja
 *
 */
@SuppressWarnings("serial")
public class JTextFieldFilter extends PlainDocument {
	public static final String LOWERCASE  =
			"abcdefghijklmnopqrstuvwxyz";
	public static final String UPPERCASE  =
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String ALPHA   = 
			LOWERCASE + UPPERCASE;
	public static final String NUMERIC = 
			"0123456789";
	public static final String FLOAT = 
			NUMERIC + ".";
	public static final String ALPHA_NUMERIC = 
			ALPHA + NUMERIC;

	protected String acceptedChars = null;
	protected boolean negativeAccepted = false;

	public JTextFieldFilter() {
		this(ALPHA_NUMERIC);
	}
	public JTextFieldFilter(String acceptedchars) {
		acceptedChars = acceptedchars;
	}

	public void setNegativeAccepted(boolean negativeaccepted) {
		if (acceptedChars.equals(NUMERIC) ||
				acceptedChars.equals(FLOAT) ||
				acceptedChars.equals(ALPHA_NUMERIC)){
			negativeAccepted = negativeaccepted;
			acceptedChars += "-";
		}
	}

	private int limit=3;

	public void insertString
	(int offset, String  str, AttributeSet attr)
			throws BadLocationException {
		if (str == null) return;

		if ((getLength() + str.length()) > limit) {

			return;

		}

		if (acceptedChars.equals(UPPERCASE))
			str = str.toUpperCase();
		else if (acceptedChars.equals(LOWERCASE))
			str = str.toLowerCase();

		for (int i=0; i < str.length(); i++) {
			if (acceptedChars.indexOf(String.valueOf(str.charAt(i))) == -1)
				return;
		}

		if (acceptedChars.equals(FLOAT) || 
				(acceptedChars.equals(FLOAT + "-") && negativeAccepted)) {
			if (str.indexOf(".") != -1) {
				if (getText(0, getLength()).indexOf(".") != -1) {
					return;
				}
			}
		}

		if (negativeAccepted && str.indexOf("-") != -1) {
			if (str.indexOf("-") != 0 || offset != 0 ) {
				return;
			}
		}

		super.insertString(offset, str, attr);
	}
}

