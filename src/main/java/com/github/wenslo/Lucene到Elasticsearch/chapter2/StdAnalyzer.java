package com.github.wenslo.Lucene到Elasticsearch.chapter2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author wenhailin
 * @create 2021/3/31-5:30 下午
 */
public class StdAnalyzer {
    private static String strCh = "中华人民共和国简称中国，是一个有13亿人口的国家";
    private static String strEn = "Dogs can not achieve a place, eyes can reach;";

    public static void stdAnalyzer(String str) throws IOException {
        Analyzer analyzer = new StandardAnalyzer();
        StringReader reader = new StringReader(str);
        TokenStream tokenStream = analyzer.tokenStream(str, reader);
        tokenStream.reset();
        CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
        System.out.println("分词结果：");
        while (tokenStream.incrementToken()) {
            System.out.print(termAttribute.toString() + "|");
        }
        System.out.println("\n");
        analyzer.close();
    }

    public static void printAnalyzer(Analyzer analyzer) throws Exception {
        StringReader reader = new StringReader(strCh);
        TokenStream tokenStream = analyzer.tokenStream(strCh, reader);
        tokenStream.reset();
        CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
        while (tokenStream.incrementToken()) {
            System.out.print(termAttribute.toString() + "|");
        }
        System.out.println("\n");
        analyzer.close();
    }


    public static void main(String[] args) throws Exception {
        System.out.println("StandardAnalyzer对中文分词：");
        stdAnalyzer(strCh);
        System.out.println("StandardAnalyzer对英文分词：");
        stdAnalyzer(strEn);

        System.out.println("标准分词测试结束");
        System.out.println("其他分词测试开始");
        Analyzer analyzer = null;
        // 标准分词
        analyzer = new StandardAnalyzer();
        System.out.println("标准分词：" + analyzer.getClass());
        printAnalyzer(analyzer);
        // 空格分词
        analyzer = new WhitespaceAnalyzer();
        System.out.println("空格分词：" + analyzer.getClass());
        printAnalyzer(analyzer);
        // 简单分词
        analyzer = new SimpleAnalyzer();
        System.out.println("简单分词：" + analyzer.getClass());
        printAnalyzer(analyzer);
        // 二分法分词
        analyzer = new CJKAnalyzer();
        System.out.println("二分法分词：" + analyzer.getClass());
        printAnalyzer(analyzer);
        // 关键字分词
        analyzer = new KeywordAnalyzer();
        System.out.println("关键字分词：" + analyzer.getClass());
        printAnalyzer(analyzer);
        // 停用词分词
        analyzer = new StopAnalyzer(CharArraySet.EMPTY_SET);
        System.out.println("停用词分词：" + analyzer.getClass());
        printAnalyzer(analyzer);
        // 中文智能分词
        analyzer = new SmartChineseAnalyzer();
        System.out.println("中文智能分词：" + analyzer.getClass());
        printAnalyzer(analyzer);

    }


}
