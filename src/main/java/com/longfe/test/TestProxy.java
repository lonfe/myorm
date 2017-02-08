package com.longfe.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

interface ProxyInterFace {
    void findById(String id);
}

class SqlSession {
    Map<String, String> map;

    public SqlSession() {
        map = new HashMap<String, String>();
        map.put("findById", "select * from User where id = ");
    }

    public String getById(String id) {
        return map.get(id);
    }
}

class ProxyObject implements InvocationHandler {
    SqlSession sqlSession;
    public ProxyObject (SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        sqlSession.getById(method.getName());
        return null;
    }
}

public class TestProxy {
    public static void main(String[] args) {
        ProxyObject proxyObject = new ProxyObject(new SqlSession());
        ProxyInterFace proxy = (ProxyInterFace) Proxy.newProxyInstance(ProxyInterFace.class.getClassLoader(), new Class[]{ProxyInterFace.class}, proxyObject);
        proxy.findById("1");
    }
}