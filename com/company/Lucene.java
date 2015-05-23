package com.company;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;





import java.util.*;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Lucene {

  private static long docSize = 0;
  private static long docSize_init = 10000;
  private static String pathdir = "E:\\IdeaProjects\\WebCrawler\\documents\\";
  private static Path path = FileSystems.getDefault().getPath("C:\\Users\\Jin\\IdeaProjects\\searchEngine\\index\\");
  private static String time_count_file = "C:\\Users\\Jin\\IdeaProjects\\searchEngine\\counter.txt";
  private static StopWatch counter = new StopWatch();


  public static void main(String[] args) throws IOException, ParseException {
    // 0. Specify the analyzer for tokenizing text.
    //    The same analyzer should be used for indexing and searching
    EnglishAnalyzer analyzer = new EnglishAnalyzer();

    // 1. create the index
    Directory index = new MMapDirectory(path);

     //new MMapDirectory(path);

    IndexWriterConfig config = new IndexWriterConfig(analyzer);

    IndexWriter w = new IndexWriter(index, config);

    counter.start();
    // adding documents
      fileInput(w);

    w.close();

    // 2. query
    String querystr = args.length > 0 ? args[0] : "Help";

    // the "title" arg specifies the default field to use
    // when no field is explicitly specified in the query.
    Query q = new QueryParser("title", analyzer).parse(querystr);

    // 3. search
    int hitsPerPage = 10;
    //
    IndexReader reader = DirectoryReader.open(index);
    IndexSearcher searcher = new IndexSearcher(reader);

    //
    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
    searcher.search(q, collector);
    ScoreDoc[] hits = collector.topDocs().scoreDocs;
   
    // 4. display results
    System.out.println("Found " + hits.length + " hits.");
    for(int i=0;i<hits.length;++i) {
      int docId = hits[i].doc;
      Document d = searcher.doc(docId);
      float score = hits[i].score;
      System.out.println((i + 1) + ". " + d.get("isbn") + "\t score = " + Float.toString(score)+ "\t"+ d.get("title"));
    }

    // reader can only be closed when there
    // is no need to access the documents any more.
    reader.close();
  }

   private static void addDoc(IndexWriter w, String title, String docID, String text) throws IOException {
    Document doc = new Document();
    TextField titleField = new TextField("title", title, Field.Store.YES);
    titleField.setBoost(5f);
    if(text == null)
        text = " ";
    TextField textF = new TextField("text", text, Field.Store.YES);
    textF.setBoost(1f);
    doc.add(titleField);
   // doc.add(new TextField("body",text, Field.Store.YES));
    // use a string field for isbn because we don't want it tokenize
    doc.add(textF);
    //doc.add(new TextField("text", text, Field.Store.YES));
    doc.add(new StringField("isbn", docID, Field.Store.YES));
    w.addDocument(doc);
  }
 
public static void scanner(String aFile,IndexWriter w, String docId)throws IOException{
    ReadFile r = new ReadFile();
    r.openFile(aFile);
    if(r.readFile())
        addDoc(w, r.getTitle(), docId, r.getText());
    r.closeFile();
  }
 
  public static void fileInput(IndexWriter w)throws IOException{
      File dir = new File(pathdir);
       File[] directoryListing = dir.listFiles();

       if (directoryListing != null) {
          //create time counting file.
          File f = new File(time_count_file);
          f.createNewFile();
          PrintWriter writer = new PrintWriter(time_count_file);
        for (File child : directoryListing) {
         // Do something with child
            String docPath = child.getPath();
          String docId = child.getName();
          docSize++;
          scanner(docPath, w, docId);
          if(docSize == docSize_init){
              writer.println("number of documents: "+ docSize +
                      "   elapse time = "+counter.getElapsedTimeSecs());
              System.out.println("number of documents: "+ docSize +
                      "   elapse time = "+counter.getElapsedTimeSecs());
              docSize_init += 10000;
          }



          //System.out.println(child.getPath());
         }
          writer.close();
       } else {
        System.err.println("not a directory");
      }
  }
}