package com.company;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.util.StringTokenizer;
import java.util.concurrent.Exchanger;

/**
 * Created by Jin on 5/15/2015.
 */
public class Combine {
    private static String prefix = "doc";
    private static String postfix = ".txt";
    private static int docId = 0;
    private static int id = 0;
    private static String pathdir = "E:\\IdeaProjects\\WebCrawler\\documents\\";
    private static String pathCombine = "E:\\IdeaProjects\\WebCrawler\\combines\\";

    public static void main(String[] args) throws IOException{
        insertDocs();
    }
    public static void insertDocs() throws IOException {


        int count = 0;
        int threshold = 0;


        File dir = new File(pathdir);
        File[] directoryListing = dir.listFiles();
        for(File child : directoryListing){

            String name = child.getName();
            String docPath = child.getPath();
            String title="";
            String text = "";

            try {
                Scanner x = new Scanner(new File(docPath));
                if(x.hasNext())
                    title = x.nextLine();
                while(x.hasNext())
                    text += x.nextLine();
            }catch(Exception e){}


            if(threshold == 0 || docId % threshold == 0 ){
                String file = pathCombine + Integer.toString(count) + postfix;
                File f = new File(file);
                f.createNewFile();

                try
                {
                    FileWriter fileWriter = new FileWriter(file,true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);


                    bufferedWriter.write(name);
                    bufferedWriter.write("##########");
                    bufferedWriter.write(title);
                    bufferedWriter.write("##########");
                    bufferedWriter.write(text);
                    String nl = System.getProperty("line.separator");
                    bufferedWriter.write(nl);

                    // Always close files.
                    bufferedWriter.close();

                }
                catch(IOException ex) {
                    System.out.println("Error writing to file '"+ file + "'");
                }

                threshold += 1000;
                count++;
            }
            else {
                String file = pathCombine + Integer.toString(count) + postfix;

                try
                {
                    FileWriter fileWriter = new FileWriter(file,true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);


                    bufferedWriter.write(name);
                    bufferedWriter.write("##########");
                    bufferedWriter.write(title);
                    bufferedWriter.write("##########");
                    bufferedWriter.write(text);
                    String nl = System.getProperty("line.separator");
                    bufferedWriter.write(nl);

                    // Always close files.
                    bufferedWriter.close();

                }
                catch(IOException ex) {
                    System.out.println("Error writing to file '"+ file + "'");
                }
            }
            docId++;
        }
    }


}