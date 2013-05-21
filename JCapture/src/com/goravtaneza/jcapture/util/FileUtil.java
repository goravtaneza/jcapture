package com.goravtaneza.jcapture.util;

import java.io.File;
import javax.swing.ImageIcon;

public class FileUtil {
    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";
 
    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
 
        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
 
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = FileUtil.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    public static String findAndReplace(String string,String search,String replace,boolean ignoreCase) {
		//Save the search string as a StringBuffer object so
		//we can take advantage of the replace capabilities
		StringBuffer s = new StringBuffer(string);
		//Calculate totals we'll need more than once for speed and clarity
		int stopAt = (s.length() - search.length()) + 1;
		int lengthDifference = replace.length() - search.length();
		int searchLength = search.length();
		//If the search string is bigger than the original string
		//then don't even continue. There would never be any match
		if (stopAt > 0) {
			//Loop through looking for the search string
			for (int i = 0; i < stopAt; i++) {
				//If we find it, replace it with the new text
				if (s.toString().regionMatches(ignoreCase, i, search, 0, searchLength)) {
					s.replace(i, i + searchLength, replace);
					i = i + lengthDifference;
					stopAt = stopAt + lengthDifference;
				}
			}
		}
		//Convert it back into a string and hand it back
		return s.toString();
	}
}
