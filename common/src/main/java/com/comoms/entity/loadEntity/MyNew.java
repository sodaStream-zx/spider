package commoncore.entity.loadEntity;

import java.io.Serializable;

/**
 * 自定义新闻类
 *
 * @author Twilight
 */
public class MyNew implements Serializable {
    private static final long serialVersionUID = 12345L;
    private int newsId;
    private String source;
    private String title;
    private String URL;
    private String content;
    private String media;
    private String author;
    private String time;

    public MyNew() {
    }

    public MyNew(String source, String title, String URL, String content, String media, String author, String time) {
        this.source = source;
        this.title = title;
        this.URL = URL;
        this.content = content;
        this.media = media;
        this.author = author;
        this.time = time;
    }

    /**
     * desc: 打印新闻简介
     **/
    public String pumpInfo() {
        return new StringBuffer()
                .append("title:")
                .append((title.substring(1, 5) + " , "))
                .append("url:")
                .append(URL).toString();
    }

    @Override
    public String toString() {
        return "MyNew{" +
                "newsId=" + newsId +
                ", source='" + source + '\'' +
                ", title='" + title + '\'' +
                ", URL='" + URL + '\'' +
                ", content='" + content + '\'' +
                ", media='" + media + '\'' +
                ", author='" + author + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
