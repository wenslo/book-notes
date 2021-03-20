package com.github.wenslo.Spring源码深度解析.chapter2;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class BeanFactoryTest {
    public void testSimpleLoad() {
        BeanFactory bf = new XmlBeanFactory(new ClassPathResource("beanFactoryTest.xml"));
        MyTestBean bean = (MyTestBean) bf.getBean("myTestBean");
        System.out.println("testStr".equals(bean.getTestStr()));
    }

    public static void main(String[] args) {
        BeanFactoryTest test = new BeanFactoryTest();
        test.testSimpleLoad();
    }
}
