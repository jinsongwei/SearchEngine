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
	private static String path = "E:\\IdeaProjects\\WebCrawler\\documents\\";
	private static Hashtable<String, String> duplicateUrlChecker = new Hashtable<String, String> ();
    private static Queue<String> q = new LinkedList<>();
	private static final double CONVERT = 1024*1024*1024;
    private static final double LIMIT = 5;
	
 //   private static String docNum;
    private String hostUrl;
 //   private String text;
 //   private String title;

    public MyThreads(String url){
       // this.docNum = docNum;
        this.hostUrl = url;
    //    this.title = title;
    }

    public void run(){
      //  Vector<String> links;

        q.add(hostUrl);
        int count = 0;
//        PrintWriter writer = new PrintWriter(fileName);
        while(!q.isEmpty() && count < 1 ){
            String url = q.remove();
            try {
                passUrl(url);
            } catch (IOException e) {}
            count++;
        }

        /*
        try {

            PrintWriter writer = new PrintWriter(docNum);
            writer.println(title + "\n");
            writer.println(text);
            writer.close();
           // Main.getDuplicateUrlChecker();
        }
        catch (FileNotFoundException e){
            System.out.println("open file fail");
        }
        */
    }
	 public static void create_file(String doc)throws IOException{
        File f = new File(doc);
        f.createNewFile();
    }
	public static void passUrl(String url)throws IOException{
        Vector<String> v = new Vector<String>();

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");

        String text = doc.body().text();
        String title = doc.title();
        long docNumber = Main.getDocNum();

        System.out.println(docNumber);

        String file = path + Long.toString(docNumber) + ".txt";

        create_file(file);
        PrintWriter writer = new PrintWriter(file);
        writer.println(title + "\n");
        writer.println(text);
        writer.close();


        for(Element link : links){
            String url_inQueue = link.attr("abs:href");
            q.add(url_inQueue);
        }
    }
    

}
