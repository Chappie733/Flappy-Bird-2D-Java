package Utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IO {
	
	  static File folder = new File("saves");
	  static File score = new File("saves/score");
	  static BufferedWriter bw;
	  static BufferedReader br;
	  
	  public static void checkIfExist()
	  {
	    if (!folder.exists()) { // if the record folder doesn't exists yet
	      folder.mkdirs(); // create it
	    }
	    if (!score.exists()) { // and if the file to store the record data doesn't exists
	      try
	      {
	        score.createNewFile(); // then create it
	      }
	      catch (IOException localIOException) {}
	    }
	  }
	  
	  public static void writeHS(int points)
	  {
	    
	    try
	    {
	      bw = new BufferedWriter(new FileWriter(score)); // Opens a data stream with the saves file
	      bw.write(String.valueOf(points)); // writes in the record
	      bw.close(); // and makes sure to close the data stream
	    }
	    catch (IOException localIOException) {}
	  }
	  
	  public static String getHS()
	  {
	    try
	    {
	      br = new BufferedReader(new FileReader(score)); // Opens a data stream with the saves file
	      if (br.ready()) { // if it's ready to do his job
	        return br.readLine(); // return the first line (the record) of the file as a String
	      }
	    }
	    catch (IOException localIOException) {}
	    return "0"; // this code will never be reached, cause in case the br.readLine() is null, the program will just
	  } // crash or return "", an empty string, just added this to not make the compilation crash, cause java can't understand
} // this little thing apparently lol
