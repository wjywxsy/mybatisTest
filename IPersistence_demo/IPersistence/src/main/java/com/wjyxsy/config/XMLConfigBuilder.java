package com.wjyxsy.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.wjyxsy.io.Resources;
import com.wjyxsy.pojo.Configuration;
import com.wjyxsy.pojo.MappedStatement;
import org.apache.commons.dbcp2.BasicDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class XMLConfigBuilder {

    public Configuration parseConfig(InputStream in) throws DocumentException, PropertyVetoException, SQLException {
        Document document = new SAXReader().read(in);

        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.selectNodes("//property");

        Properties properties = new Properties();
        for (Element element : list) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name, value);
        }

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(properties.getProperty("driverClass"));
        basicDataSource.setUrl(properties.getProperty("jdbcUrl"));
        basicDataSource.setUsername(properties.getProperty("username"));
        basicDataSource.setPassword(properties.getProperty("password"));
//        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
//        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
//        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
//        comboPooledDataSource.setUser(properties.getProperty("username"));
//        comboPooledDataSource.setPassword(properties.getProperty("password"));

        Configuration configuration = new Configuration(basicDataSource);

        List<Element> mapperList = rootElement.selectNodes("//mapper");

        for (Element mapper : mapperList) {
            String resource = mapper.attributeValue("resource");
            InputStream mapperStream = Resources.getResourceAsStream(resource);
            XMLMapperConfigBuilder xmlMapperConfigBuilder = new XMLMapperConfigBuilder(configuration);
            xmlMapperConfigBuilder.parseConfig(mapperStream);
        }

        return configuration;
    }
}
