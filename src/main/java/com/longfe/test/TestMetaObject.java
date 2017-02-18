package com.longfe.test;

import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;

import java.util.HashMap;
import java.util.Map;

import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_FACTORY;
import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY;

public class TestMetaObject {

    public static void main(String[] args) {
        Title title = new Title();
        title.setName("lonfe");

        Map<String, Title> map = new HashMap<String, Title>();
        map.put("title", title);

        MetaObject mapMeta = MetaObject.forObject(map,
                DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        System.out.println(mapMeta.getValue("title.name"));
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
