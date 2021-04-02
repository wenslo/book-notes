package com.github.wenslo.Lucene到Elasticsearch.chapter3;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 * 文件解析测试
 *
 * @author wenhailin
 * @create 2021/4/2-1:47 下午
 */
public class TikaParsePdf {
    public static void main(String[] args) throws Exception {
        String filepath = "/Users/wen/test/深入理解Java虚拟机_JVM高级特性与最佳实践_第3版.pdf";
        File pdfFile = new File(filepath);
        // 创建内容处理器对象
        BodyContentHandler handler = new BodyContentHandler();
        // 创建元数据对象
        Metadata metadata = new Metadata();
        FileInputStream inputStream = new FileInputStream(pdfFile);
//        InputStream inputStream1 = TikaInputStream.get(pdfFile);
        // 创建内容解析器对象
        ParseContext parseContext = new ParseContext();
        // 实例化PDFParser对象
        PDFParser parser = new PDFParser();
        // 调用parse()解析文件
        parser.parse(inputStream, handler, metadata, parseContext);
        // 遍历元数据内容
        System.out.println("文件属性信息：");
        for (String name : metadata.names()) {
            System.out.println(name + "：" + metadata.get(name));
        }
        // 打印pdf文件中的内容
        System.out.println("pdf文件中的内容：");
        System.out.println(handler.toString());

//        // 解析MS Office文档
//        OOXMLParser parser1 = new OOXMLParser();
//        // 解析文本文件
//        TXTParser parser2 = new TXTParser();
//        // 解析HTML文件
//        HtmlParser parser3 = new HtmlParser();
//        // 解析XML文件
//        XMLParser parser4 = new XMLParser();
//        // 解析class文件
//        ClassParser parser5 =new ClassParser();

        // 还可以解析图像、音频、视频等多种类型的文件
    }
}
