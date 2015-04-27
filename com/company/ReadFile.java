package com.company;

/**
 * Created by songwei on 4/12/2015.
 */

import java.util.*;
import java.io.*;

public class ReadFile {

    private Scanner x;
    private String text;
    private String title;
    private String docId;

	public void openFile(String path){
    try {
      x = new Scanner(new File(path));
    }catch(Exception e){
      System.out.println("couldn not find file");
    }
  }

  public boolean readFile(){
      //System.out.println(x.nextLine());
      if(x.hasNext())
          title = x.nextLine();
      else
        return false;

    while(x.hasNext()){
      text += x.nextLine();
      text += "\n";
    }
      return true;
  }

  public void closeFile(){
    x.close();
  }

    public String getTitle(){return title;}
    public String getText(){return text;}
}
