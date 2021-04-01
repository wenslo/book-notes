package com.github.wenslo.Lucene到Elasticsearch.chapter2;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.StringReader;

/**
 * @author wenhailin
 * @create 2021/4/1-9:34 上午
 */
public class ExtDicTest {
    private static String str = "厉害了我的哥！中国环保部门即将发布智力北京雾霾的方法";

    /*
     * 未定义自定义词典之前的分词结果为：
     * 厉|害了|的哥|中国|环保部门|发布|智力|北京|雾|霾|方法|
     *
     * 定义自定义词典之后的分词结果为：
     * 厉害了我的哥|中国环保部门|发布|智力|北京雾霾|方法|
     */


    public static void main(String[] args) throws Exception {
        Analyzer analyzer = new IKAnalyzer8x(true);
        StringReader reader = new StringReader(str);
        TokenStream tokenStream = analyzer.tokenStream(str, reader);
        tokenStream.reset();
        CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
        System.out.println("分词结果：");
        while (tokenStream.incrementToken()) {
            System.out.print(termAttribute.toString() + "|");
        }
        System.out.println("");
        analyzer.close();
    }
}
