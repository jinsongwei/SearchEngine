package com.company;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;
/**
 * Example program to list links from a URL.
 */
public class Main {
    private static String path = "C:\\Users\\Jin\\IdeaProjects\\searchEngine\\documents\\";
    private static String path_record = "C:\\Users\\Jin\\IdeaProjects\\searchEngine\\record.txt";
    private static long docNum = 1;
	private static Hashtable<String, String> duplicateUrlChecker = new Hashtable<String, String> ();
	private static final double CONVERT = 1024*1024*1024;
    private static final double LIMIT = 5;
    private static int stop = 0;
    private static String recentURL;
    private static double dataSize = 0;

    private static String host = "http://www.ucr.edu";
    //private static String host = "http://en.wikipedia.org/wiki/Main_Page";
    public static StopWatch timer = new StopWatch();
    public static double getDataSize(){
        return dataSize;
    }
    public static void setRecentURL(String url){recentURL = url;}

    public static void addData(double data){
        dataSize += data;
    }
    public static void setDocNum(int docNumber){
        docNum = docNumber;
    }
    public static long getDocNum(){
        docNum++;
        return docNum - 1;
    }
    public static Hashtable<String,String> getDuplicateUrlChecker(){
        return duplicateUrlChecker;
    }

    public static int isStop(){return stop;}

    //----------------->>>>>>>>>>>   main  <<<<<<<<<<<---------------------------
    public static void main(String[] args) throws IOException, InterruptedException {
        timer.start();
       webCrawlerDemo(host);

    }
    public static void webCrawlerDemo(String hUrl) throws IOException, InterruptedException {
        String fileName = path + Long.toString(docNum) + ".txt";
        String hostUrl = hUrl;
        //String hostUrl = "http://en.wikipedia.org/wiki/Main_Page";


        create_file(fileName);
       // start("http://www.ucr.edu",fileName);
        Queue<String> q = new LinkedList<>() ;

  //      Queue<String> q = new LinkedList<>();
  //      q.add(hostUrl);

        duplicateUrlChecker.put(hostUrl,"ok");

        PrintWriter writer = new PrintWriter(fileName);

        Document doc = Jsoup.connect(hostUrl).get();
        Elements links = doc.select("a[href]");
        String text = doc.body().text();
        String title = doc.title();

        writer.println(title + "\n");
        writer.println(text);
        writer.close();

        for(Element link: links){
            String url = link.attr("abs:href");
            if(!duplicateUrlChecker.containsKey(url)) {
                duplicateUrlChecker.put(url,"ok");
                q.add(url);
            }
        }
        Thread main_thread = Thread.currentThread();
        main_thread.setPriority(10);

        while(!q.isEmpty()){
            String url = q.remove();
            MyThreads t = new MyThreads(url);
            Thread thr = new Thread(t);
            thr.setPriority(9);
            thr.start();
        }
    }
    public static void checkSize() throws IOException, InterruptedException {
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path_record, true)))) {
			out.println(recentURL);
            out.close();
		}catch (IOException e) {
		//exception handling left as an exercise for the reader
		}


        /*
        PrintWriter writer = new PrintWriter(path_record);
        writer.println(recentURL);
        writer.close();
        */
        int numOfTHreads = Thread.activeCount();
        if(numOfTHreads < 150) {
            System.out.println("number of running threads:  ---> " + numOfTHreads);
            webCrawlerDemo(recentURL);
            if(numOfTHreads < 10){
                //duplicateUrlChecker.elements();
            }
        }
       // System.out.println("data size is " + dataSize + "   limit size = " + LIMIT);
        if(dataSize > LIMIT) {
            System.out.println("data Size is : " + dataSize + "  limit " + LIMIT);
            System.out.println("using time: " + timer.getElapsedTimeSecs());
            System.exit(0);
        }
    }
    public static void create_file(String doc)throws IOException{
        File f = new File(doc);
        f.createNewFile();
    }


    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}


