package com.github.wenslo.Lucene到Elasticsearch.chapter2;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;

public class IKTokenizer8x extends Tokenizer {
    // IK分词器实现
    private IKSegmenter _IKImplement;
    // 词元文本属性
    private final CharTermAttribute termAttribute;
    // 词元位移属性
    private final OffsetAttribute offsetAttribute;
    // 词元分类属性
    private final TypeAttribute typeAttribute;
    // 记录最后一个词元的结束位置
    private int endPosition;

    public IKTokenizer8x(boolean useSmart) {
        super();
        offsetAttribute = addAttribute(OffsetAttribute.class);
        termAttribute = addAttribute(CharTermAttribute.class);
        typeAttribute = addAttribute(TypeAttribute.class);
        _IKImplement = new IKSegmenter(input, useSmart);
    }


    @Override
    public boolean incrementToken() throws IOException {
        // 清除所有的词元属性
        clearAttributes();
        Lexeme nextLexeme = _IKImplement.next();
        if (nextLexeme != null) {
            /* 将Lexeme转成Attributes */
            // 设置词元文本
            termAttribute.append(nextLexeme.getLexemeText());
            // 设置词元长度
            termAttribute.setLength(nextLexeme.getLength());
            offsetAttribute.setOffset(nextLexeme.getBeginPosition(), nextLexeme.getEndPosition());
            // 记录分词的最后位置
            endPosition = nextLexeme.getEndPosition();
            // 记录词元分类
            typeAttribute.setType(nextLexeme.getLexemeText());
            // 返回true告知还有下个词元
            return true;
        }
        // 返回false告知词元输出完毕
        return false;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        _IKImplement.reset(input);
    }

    @Override
    public void end() throws IOException {
        int finalOffset = correctOffset(this.endPosition);
        offsetAttribute.setOffset(finalOffset, finalOffset);
        
    }
}
