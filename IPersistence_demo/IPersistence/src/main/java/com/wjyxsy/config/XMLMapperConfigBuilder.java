package com.wjyxsy.config;

import com.wjyxsy.pojo.Configuration;
import com.wjyxsy.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperConfigBuilder {

    private Configuration configuration;

    public XMLMapperConfigBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parseConfig(InputStream mapperStream) throws DocumentException {

        Document document = new SAXReader().read(mapperStream);

        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");

        List<Element> selectList = rootElement.selectNodes("//select");
        List<Element> insertList = rootElement.selectNodes("//insert");
        List<Element> updateList = rootElement.selectNodes("//update");
        List<Element> deleteList = rootElement.selectNodes("//delete");

        for (Element element : selectList) {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String paramterType = element.attributeValue("paramterType");
            String sql = element.getTextTrim();
            String statementId = namespace + "." + id;

            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setStatementId(statementId);
            mappedStatement.setParamterType(paramterType);
            mappedStatement.setResultType(resultType);
            mappedStatement.setSqlText(sql);
            mappedStatement.setSqlType("select");

            configuration.getMappedStatementMap().put(statementId, mappedStatement);
        }for (Element element : insertList) {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String paramterType = element.attributeValue("paramterType");
            String sql = element.getTextTrim();
            String statementId = namespace + "." + id;

            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setStatementId(statementId);
            mappedStatement.setParamterType(paramterType);
            mappedStatement.setResultType(resultType);
            mappedStatement.setSqlText(sql);
            mappedStatement.setSqlType("insert");

            configuration.getMappedStatementMap().put(statementId, mappedStatement);
        }for (Element element : updateList) {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String paramterType = element.attributeValue("paramterType");
            String sql = element.getTextTrim();
            String statementId = namespace + "." + id;

            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setStatementId(statementId);
            mappedStatement.setParamterType(paramterType);
            mappedStatement.setResultType(resultType);
            mappedStatement.setSqlText(sql);
            mappedStatement.setSqlType("update");

            configuration.getMappedStatementMap().put(statementId, mappedStatement);
        }for (Element element : deleteList) {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String paramterType = element.attributeValue("paramterType");
            String sql = element.getTextTrim();
            String statementId = namespace + "." + id;

            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setStatementId(statementId);
            mappedStatement.setParamterType(paramterType);
            mappedStatement.setResultType(resultType);
            mappedStatement.setSqlText(sql);
            mappedStatement.setSqlType("delete");

            configuration.getMappedStatementMap().put(statementId, mappedStatement);
        }

    }
}
