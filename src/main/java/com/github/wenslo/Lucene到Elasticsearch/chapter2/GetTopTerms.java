package com.github.wenslo.Lucene到Elasticsearch.chapter2;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取新闻热词
 */
public class GetTopTerms {
    private static final String PATH = "/Users/wenhailin/test";

    public static void main(String[] args) throws Exception {
        Directory directory = FSDirectory.open(Paths.get(PATH));
        IndexReader reader = DirectoryReader.open(directory);
        // 因为之索引了一个文档，所以DocID为0
        // 通过getTermVector获取content字段的词频
        Terms terms = reader.getTermVector(0, "content");
        // 遍历词项
        TermsEnum termsEnum = terms.iterator();
        Map<String, Integer> map = new HashMap<>();
        BytesRef thisTerm;
        while ((thisTerm = termsEnum.next()) != null) {
            // 词项
            String termText = thisTerm.utf8ToString();
            // 通过totalTermFreq()方法获取词项频率
            map.put(termText, (int) termsEnum.totalTermFreq());
        }
        List<Map.Entry<String, Integer>> sortedMap = new ArrayList<>(map.entrySet());
        sortedMap.sort((o1, o2) -> o2.getValue() - o1.getValue());
        getTopN(sortedMap, 10);
    }

    private static void getTopN(List<Map.Entry<String, Integer>> sortedMap, int n) {
        for (int i = 0; i < n; i++) {
            System.out.println(sortedMap.get(i).getKey() + "：" + sortedMap.get(i).getValue());
        }
    }
}
