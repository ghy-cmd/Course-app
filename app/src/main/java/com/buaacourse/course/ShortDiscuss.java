package com.buaacourse.course;

public class ShortDiscuss {
    public String name;
    public String imageUrl;
    public String content;
    public String title;
    public int star;
    public int follows;
    public String time;
    public String id;

    public ShortDiscuss(String name, String imageUrl, String content, String title,int star,
                        int follows,String time,String id) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.content = content;
        this.title=title;
        this.star=star;
        this.follows=follows;
        this.time=time;
        this.id=id;
    }
}
