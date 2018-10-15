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
	    if (!folder.exists()) {
	      folder.mkdirs();
	    }
	    if (!score.exists()) {
	      try
	      {
	        score.createNewFile();
	      }
	      catch (IOException localIOException) {}
	    }
	  }
	  
	  public static void writeHS(int points)
	  {
	    
	    try
	    {
	      bw = new BufferedWriter(new FileWriter(score));
	      bw.write(String.valueOf(points));
	      bw.close();
	    }
	    catch (IOException localIOException) {}
	  }
	  
	  public static String getHS()
	  {
	    try
	    {
	      br = new BufferedReader(new FileReader(score));
	      if (br.ready()) {
	        return br.readLine();
	      }
	    }
	    catch (IOException localIOException) {}
	    return "0";
	  }
}
