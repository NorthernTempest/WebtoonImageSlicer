package ca.northerntempest.maven.webtoonsimageslicer;

import java.io.File;
import java.util.Scanner;

import ij.IJ;
import ij.ImagePlus;
import ij.io.FileSaver;

/**
 * @author Jesse David Goerzen
 *
 */
public class App 
{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 1280;
	
    public static void main( String[] args )
    {
    	Scanner in = new Scanner(System.in);
    	boolean loop = true;
    	while (loop) {
    		
	    	System.out.print("Enter image path, or x to exit: ");
	    	String filepath = in.nextLine();
	    	
	    	if( filepath.length() > 0 ) {
	    		if( filepath.equalsIgnoreCase("x") ) {
	    			loop = false;
	    		} else {
	    			if( filepath.startsWith("\"") )
	    				filepath = filepath.substring(1, filepath.length() - 1);
	    			File file = new File( filepath );
	    			if( file.exists() ) {
	    				String name = file.getName().substring(0, file.getName().length() - 4);
	    				
	    				ImagePlus importedImage = IJ.openImage(filepath);
	    				int importedHeight = importedImage.getHeight();
	    				int importedWidth = importedImage.getWidth();
	    				int scaledHeight = WIDTH * importedHeight / importedWidth;
	    				ImagePlus scaledImage = importedImage.resize(WIDTH, scaledHeight, "none");
	    				
	    				File folder = new File( file.getParentFile().getAbsolutePath() + "/" + name + "/" );
	    				folder.mkdirs();
	    				
	    				for(int i = 0; i * HEIGHT < scaledHeight; i++) {
	    					if( i * HEIGHT + HEIGHT > scaledHeight )
	    						scaledImage.setRoi(0, i * HEIGHT, WIDTH, scaledHeight - i * HEIGHT);
	    					else
	    						scaledImage.setRoi(0, i * HEIGHT, WIDTH, HEIGHT);
	    					ImagePlus slice = scaledImage.crop();
	    					FileSaver sliceFS = new FileSaver(slice);
	    					sliceFS.saveAsJpeg(folder.getAbsolutePath() + "/" + name + "_" + i + ".jpg");
	    				}
	    			} else {
	    				System.out.println("Filepath invalid.");
	    			}
	    		}
	    	} else {
	    		System.out.println("Please enter the path to an image.");
	    	}
    	}
    	System.out.println("Goodbye.");
    	in.nextLine();
    	in.close();
    }
}
