package com.goravtaneza.jcapture.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

 
public class ImageFilter extends FileFilter {
 
    //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
 
        String extension = FileUtil.getExtension(f);
        if (extension != null) {
            if (extension.equals(FileUtil.jpeg) ||
                extension.equals(FileUtil.jpg) ||
                extension.equals(FileUtil.png)) {
                    return true;
            } else {
                return false;
            }
        }
 
        return false;
    }
 
    //The description of this filter
    public String getDescription() {
        return ".jpeg, .jpg and .png";
    }
}

