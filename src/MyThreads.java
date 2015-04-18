package com.company;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import java.io.*;
import java.util.*;

import static com.company.Main.create_file;

/**
 * Created by Jin on 4/9/2015.
 */
public class MyThreads implements Runnable{
    private static String docNum;
    private String url;
    private String text;
    private String title;

    public MyThreads(String docNum,String url, String title){
        this.docNum = docNum;
        this.url = url;
        this.title = title;
    }

    public void run(){

        try {
            Document doc = Jsoup.connect(url).get();
            text = doc.body().text();
        }catch(IOException e){}

        try{
            create_file(docNum);
        }
        catch(IOException e){
            System.out.println("create file failed");
        }
        try {

            PrintWriter writer = new PrintWriter(docNum);
            writer.println(title + "\n");
            writer.println(text);
            writer.close();
        }
        catch (FileNotFoundException e){
            System.out.println("open file fail");
        }
    }
	 public static void create_file(String doc)throws IOException{
        File f = new File(doc);
        f.createNewFile();
    }

}
