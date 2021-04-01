package com.github.wenslo.Lucene到Elasticsearch.chapter2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author wenhailin
 * @create 2021/4/1-2:52 下午
 */
public class QueryParseTest {
    private static final String PATH = "/Users/wenhailin/test";

    public static void main(String[] args) throws Exception {
        String field = "title";
        Path indexPath = Paths.get(PATH);
        Directory directory = FSDirectory.open(indexPath);
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(indexReader);
        Analyzer analyzer = new IKAnalyzer8x();
        // 单域搜索
//        QueryParser parser = new QueryParser(field, analyzer);

        // 多域搜索
//        String[] fields = {"title", "content"};
//        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);
//        parser.setDefaultOperator(QueryParser.Operator.AND);
//        Query query = parser.parse("一带一路");

        // 词项搜索
//        Term term = new Term("title", "建设");
//        Query query = new TermQuery(term);

        // 布尔搜索
//        Query query1 = new TermQuery(new Term("title", "建设"));
//        Query query2 = new TermQuery(new Term("title", "轨道"));
//        BooleanClause bc1 = new BooleanClause(query1, BooleanClause.Occur.MUST);
//        BooleanClause bc2 = new BooleanClause(query2, BooleanClause.Occur.MUST_NOT);
//        BooleanQuery query = new BooleanQuery.Builder().add(bc1).add(bc2).build();

        // 范围搜索
//        Query query = IntPoint.newRangeQuery("reply", 500, 1000);

        // 前缀搜索
//        Term term = new Term("title", "沈");
//        Query query = new PrefixQuery(term);

        // 多关键字搜索
//        PhraseQuery.Builder builder = new PhraseQuery.Builder();
//        builder.add(new Term("title", "日本"), 2);
//        builder.add(new Term("title", "美国"), 3);
//        PhraseQuery query = builder.build();

        // 模糊查询
//        Term term = new Term("title", "一带一路");
//        FuzzyQuery query = new FuzzyQuery(term);

        // 通配符搜索
        WildcardQuery query = new WildcardQuery(new Term(field, "一?"));


        System.out.println("Query:" + query.toString());
        TopDocs tds = searcher.search(query, 10);
        for (ScoreDoc sd : tds.scoreDocs) {
            Document doc = searcher.doc(sd.doc);
            System.out.println("DocID:" + sd.doc);
            System.out.println("id:" + doc.get("id"));
            System.out.println("title:" + doc.get("title"));
            System.out.println("文档评分：" + sd.score);
        }
        directory.close();
        indexReader.close();
    }
}
