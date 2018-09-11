package com.suixingpay.config.server;

import com.suixingpay.config.common.io.StringResource;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;

import java.io.*;
import java.util.Properties;

/**
 * TODO
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月4日 下午4:24:50
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月4日 下午4:24:50
 */
public class PropertySourceTest {

    /**
     * TODO
     *
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable {
        toProperties();
        toYaml();
    }

    private static void toProperties() throws Throwable {
        StringBuilder builder = new StringBuilder();
        // builder.append("suixingpay: \n");
        // builder.append(" name: 随行付\n");

        builder.append("suixingpay.name= 随行付\n");// 不支持中文

        Properties properties = new Properties();
        try {
            InputStream inputStream = new ByteArrayInputStream(builder.toString().getBytes("utf-8"));
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            properties.load(bf);
            inputStream.close(); // 关闭流
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Properties props=PropertiesLoaderUtils.loadProperties(new
        // StringResource(builder.toString(), "iso-8859-1"));
        String name = (String) properties.get("suixingpay.name");
        System.out.println(name);
    }

    private static void toYaml() throws Throwable {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        StringBuilder builder = new StringBuilder();
        // builder.append("suixingpay: \n");
        // builder.append(" name: 随行付\n");

        builder.append("suixingpay.name: 随行付 \n");

        yaml.setResources(new StringResource(builder.toString(), "UTF-8"));
        Properties props = yaml.getObject();
        String name = (String) props.get("suixingpay.name");
        System.out.println(name);

    }

}
