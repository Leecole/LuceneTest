package com.yiibai.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import sun.misc.Version;

import java.io.IOException;
import java.nio.file.Paths;

public class Searcher {     //The class of Index
     static Indexer indexer = null;


    //这个方法是搜索索引的方法，传入索引路径和查询表达式
       public static void search(String indexDir,String query) throws IOException, ParseException{
           Directory dir=FSDirectory.open(Paths.get(indexDir));
           IndexSearcher searcher=new IndexSearcher(DirectoryReader.open(dir));
           QueryParser parser=new QueryParser("contents",new StandardAnalyzer());
           Query q=parser.parse(query);
           long start=System.currentTimeMillis();
           TopDocs hits=searcher.search(q, 10);
           long end=System.currentTimeMillis();
           System.out.println(hits.totalHits);
           System.out.println(end-start);
           for(ScoreDoc scoreDoc:hits.scoreDocs){
               Document doc=searcher.doc(scoreDoc.doc);
               System.out.println(doc.get("fullpath"));
           }
       }

       public static void main(String[] args) throws IOException, ParseException {

           String indexDir="indexer";
           String query="青豆";

           try {
               indexer = new Indexer(indexDir);
               //numIndexed = indexer.index(dataDir, new TextFilesFilter());
           } catch (IOException e) {
               e.printStackTrace();
           } catch (Exception e) {
               e.printStackTrace();
           }finally{
               try {
                   //IndexerWriter写索引操作关闭，提交写索引（如没关闭会造成索引无法完整创建，查询时出错）
                   indexer.close();
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
           search(indexDir, query);
       }

}
