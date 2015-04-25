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
    //private Thread t;
	private static String path = "E:\\IdeaProjects\\WebCrawler\\documents\\";
	private static Hashtable<String, String> duplicateUrlChecker = new Hashtable<String, String> ();
    private static Queue<String> q = new LinkedList<>();

 //   private static String docNum;
    private static int name;
    private String hostUrl;
 //   private String text;
 //   private String title;

    public MyThreads(String url, int name){
       // this.docNum = docNum;
        this.hostUrl = url;
        this.name = name;
    //    this.title = title;
    }

    public void run(){
      //  Vector<String> links;

        try {
                passUrl(hostUrl);
            } catch (IOException e) {
                System.out.println("Thread " + name + "interrupted");
            }
        int count = 0;
//        PrintWriter writer = new PrintWriter(fileName);
   //     double dataTotal = Main.getDataSize();
        while(!q.isEmpty() && count < 100 ){
            String url = q.remove();
       //     System.out.println(count);
            Main.checkSize();
            try {
                passUrl(url);
            } catch (IOException e) {
                System.out.println("Thread " + name + "interrupted");
            }
            count++;
        }

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

        System.out.println("Thread " + name + ", " + docNumber);

        String file = path + Long.toString(docNumber) + ".txt";

        create_file(file);
        PrintWriter writer = new PrintWriter(file);
        writer.println(title + "\n");
        writer.println(text);
        writer.close();

        File thisFile = new File(file);
        Main.addData(thisFile.length());

        for(Element link : links){
            String url_inQueue = link.attr("abs:href");
            if(!Main.getDuplicateUrlChecker().containsKey(url_inQueue)) {
                Main.getDuplicateUrlChecker().put(url_inQueue,"OK");
                q.add(url_inQueue);
            }
        }
    }
    /*
    public void start(){
        System.out.println("Starting" + name);
        if(t == null){
            t = new Thread(this);
            t.run();
        }
    }
    */
    

}
