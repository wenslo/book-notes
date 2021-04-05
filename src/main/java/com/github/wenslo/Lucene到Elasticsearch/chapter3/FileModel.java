package com.github.wenslo.Lucene到Elasticsearch.chapter3;

/**
 * @author wenhailin
 * @create 2021/4/2-2:39 下午
 */
public class FileModel {
    /** 文件标题 **/
    private String title;
    /** 文件内容 **/
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FileModel() {
    }

    public FileModel(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "FileModel{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
