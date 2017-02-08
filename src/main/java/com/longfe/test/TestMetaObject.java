package com.longfe.test;

import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;

import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_FACTORY;
import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY;

public class TestMetaObject {

    public static void main(String[] args) {
        Title[] titles = new Title[2];
        Title title = new Title();
        title.setName("lonfe");
        titles[1] = title;
        Content content = new Content();
        content.setTitle(titles);
        Example example = new Example();
        example.setContent(content);

        MetaObject javaBeanMeta = MetaObject.forObject(example,
                DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        System.out.println(javaBeanMeta.getValue("content.title[0].name"));
    }

}

class Example {
    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}


class Content {
    private Title[] title;

    public Title[] getTitle() {
        return title;
    }

    public void setTitle(Title[] title) {
        this.title = title;
    }
}

class Title {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
