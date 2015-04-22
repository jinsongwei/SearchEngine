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
    private static String path = "E:\\IdeaProjects\\WebCrawler\\documents\\";
    private static  long docNum = 1;
	private static Hashtable<String, String> duplicateUrlChecker = new Hashtable<String, String> ();
	private static final double CONVERT = 1024*1024*1024;
    private static final double LIMIT = 5;

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

    //----------------->>>>>>>>>>>   main  <<<<<<<<<<<---------------------------
    public static void main(String[] args) throws IOException, InterruptedException {
       webCrawlerDemo();

    }
    public static void webCrawlerDemo() throws IOException, InterruptedException {
        String fileName = path + "0.txt";
        String hostUrl = "http://en.wikipedia.org";

        create_file(fileName);
       // start("http://www.ucr.edu",fileName);
        Queue<String> q = new LinkedList<>() ;

  //      Queue<String> q = new LinkedList<>();
  //      q.add(hostUrl);

        PrintWriter writer = new PrintWriter(fileName);

        Document doc = Jsoup.connect(hostUrl).get();
        Elements links = doc.select("a[href]");
        String text = doc.body().text();
        String title = doc.title();

        writer.println(title + "\n");
        writer.println(text);
        writer.close();

        for(Element link: links){
            q.add(link.attr("abs:href"));
        }

        while(!q.isEmpty()){
            String url = q.remove();
            MyThreads t = new MyThreads(url);
            t.run();
        }
        //writer.println(hostUrl);
        /*
        while (!q.isEmpty() && count < 1) {
             String url = q.remove();
           //  int docName = count;
           //  String file = path + Integer.toString(docName) + ".txt";
            links = passUrl(url);
             if (!links.isEmpty()) {
                 for (String link : links) {
                     q.add(link);
        //             System.out.println(link);
                     writer.println(link);
                     }
                 writer.println();
                 }
             count++;
        }
        writer.close();
        */
    }
    public static void create_file(String doc)throws IOException{
        File f = new File(doc);
        f.createNewFile();
    }

    /* param: the url
        return: a vector contain all links in this url
     */
/*    public static Vector<String> passUrl(String url)throws IOException{
        Vector<String> v = new Vector<String>();


        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        String text = doc.body().text();

        //create_file(fileName);
        //PrintWriter writer = new PrintWriter(fileName);
       // writer.println(text);
        //writer.close();

        for(Element link : links){
            String urls = link.attr("abs:href");
            v.add(urls);
            String pathTemp = path + Integer.toString(docNum) + ".txt";
            System.out.println(urls + "\t" + link.text());
            MyThreads t = new MyThreads(pathTemp,urls,link.text());
            t.run();
            docNum++;
        }
        return v;
    }
*/
    public static void start(String urlTemp,String fileN)throws IOException{
        //Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        String url = urlTemp;



        //        "http://www.ucr.edu";
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        String text = doc.body().text();
        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img"))
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }

        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
        }

        print("\nLinks: (%d)", links.size());

        try(BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
        }
        String everything = sb.toString();
    }
/*
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
*/

        print("\ntext: ");

       // print(" ******** :(%s)",text);

    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}


