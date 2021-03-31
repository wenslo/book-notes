package com.github.wenslo.Lucene到Elasticsearch.chapter2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

public class IKAnalyzer8x extends Analyzer {
    private boolean useSmart;

    public boolean isUseSmart() {
        return useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    public IKAnalyzer8x() {
        // IK分词器Lucene Analyzer接口实现类；默认细粒度切分算法
        this(false);
    }

    public IKAnalyzer8x(boolean useSmart) {
        this.useSmart = useSmart;
    }

    /**
     * 重写最新版本的createComponents；重载Analyzer接口，构造分词组件
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer _IKTokenizer = new IKTokenizer8x(this.isUseSmart());
        return new TokenStreamComponents(_IKTokenizer);
    }
}
