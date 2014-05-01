package inventorymanagementsystem;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Mike
 */
public class PictureFileFilter extends FileFilter implements java.io.FileFilter{

    @Override
    public boolean accept(File f) {
        if (f.getName().toLowerCase().endsWith(".jpg") ||
                f.getName().toLowerCase().endsWith(".jpeg") ||
                f.getName().toLowerCase().endsWith(".bmp") ||
                f.getName().toLowerCase().endsWith(".gif") ||
                f.getName().toLowerCase().endsWith(".png")){
            return true;
        }
        if(f.isDirectory()){
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "JPEG, JPG, BMP, GIF, and PNG files";
    }
    
}
