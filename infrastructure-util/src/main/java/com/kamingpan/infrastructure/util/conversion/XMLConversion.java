package com.kamingpan.infrastructure.util.conversion;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * xml转换
 *
 * @author kamingpan
 * @since 2016-05-05
 */
public class XMLConversion {

    /**
     * 对象转换成xml(格式化及去除头信息)
     *
     * @param object 转换对象
     * @param <T>    对象泛型
     * @return xml字符串
     * @throws JAXBException                jaxb异常
     * @throws UnsupportedEncodingException 不支持字符编码异常
     */
    public static <T> String convertToXML(T object) throws JAXBException, UnsupportedEncodingException {
        return XMLConversion.convertToXML(object, true, true);
    }

    /**
     * 对象转换成xml
     *
     * @param object         转换对象
     * @param isFormat       是否格式化
     * @param isIgnoreHeader 是否去除头信息
     * @param <T>            对象泛型
     * @return xml字符串
     * @throws JAXBException                jaxb异常
     * @throws UnsupportedEncodingException 不支持字符编码异常
     */
    public static <T> String convertToXML(T object, boolean isFormat, boolean isIgnoreHeader)
            throws JAXBException, UnsupportedEncodingException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, isFormat); // 是否格式化生成的xml串
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, isIgnoreHeader); // 是否省略xml头信息
        marshaller.marshal(object, byteArrayOutputStream); // 将对象转换为对应的XML文件

        return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
    }

    /**
     * xml转换成对象
     *
     * @param xml   xml字符串
     * @param clazz 返回对象
     * @param <T>   对象泛型
     * @return 对象
     * @throws JAXBException                jaxb异常
     * @throws UnsupportedEncodingException 不支持字符编码异常
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertToObject(String xml, Class<T> clazz) throws JAXBException, UnsupportedEncodingException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        // 转为系统默认字符编码输入流
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(System.getProperty("file.encoding")));
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(inputStream);
    }

}
