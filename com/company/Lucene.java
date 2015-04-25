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
  private static String doc_title;
  private static String doc_id;
  private static String pathdir = "E:\\IdeaProjects\\WebCrawler\\document\\";
  private static Path path = FileSystems.getDefault().getPath("E:\\IdeaProjects\\WebCrawler\\indexs\\");

  public static void main(String[] args) throws IOException, ParseException {
    // 0. Specify the analyzer for tokenizing text.
    //    The same analyzer should be used for indexing and searching
    EnglishAnalyzer analyzer = new EnglishAnalyzer();

    // 1. create the index
    Directory index = new RAMDirectory();

     //new MMapDirectory(path);

    IndexWriterConfig config = new IndexWriterConfig(analyzer);

    IndexWriter w = new IndexWriter(index, config);
    // adding documents

    fileInput(w);
    /*
    while()
    addDoc(w, "Managing Gigabytes", "55063554A");
    addDoc(w, "The Art of Computer Science", "9900333X");
    */
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
    r.readFile();
    addDoc(w, r.getTitle(), docId, r.getText());
    r.closeFile();
  }
  
  public static void fileInput(IndexWriter w)throws IOException{
      File dir = new File(pathdir);
	  File[] directoryListing = dir.listFiles();

	  if (directoryListing != null) {
        for (File child : directoryListing) {
         // Do something with child
          String docPath = child.getPath();
          String docId = child.getName();
          scanner(docPath, w, docId);

          //System.out.println(child.getPath());
	    }
	  } else {
        System.err.println("not a directory");
      }
  }
}