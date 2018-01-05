package com.kzk.study.jaxb;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;


public class JaxbTest {

	public static void main(String[] args) throws JAXBException, URISyntaxException, MalformedURLException, SAXException {
		System.out.println("当前执行路径："+System.getProperty("user.dir"));//user.dir指定了当前的路径 
		
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sf.newSchema(new File("testFile/jaxb/user.xsd"));
		
		String xmlFilePath = "testFile/jaxb/user.xml";
		User user = new User();
		user.setId(1001L);
		user.setName("kazeki1");
		
		User.Children children= new User.Children();
		User child = new User();
		child.setId(1002L);
		child.setName("hokosaki");
		children.getChild().add(child);
		child = new User();
		child.setId(1003L);
		child.setName("boo");
		children.getChild().add(child);
		user.setChildren(children);

		JaxbHelper.writeXml(xmlFilePath, user, schema);
		
		User user2 = JaxbHelper.parse(xmlFilePath, User.class, schema);
		System.out.println(Arrays.deepToString(new User[]{user2}));
	}
	
}



class JaxbHelper {
	public static <T> void writeXml(String path, T bean) throws JAXBException {
		writeXml(path, bean, null);
	}
	
	@SuppressWarnings("restriction")
	public static <T> void writeXml(String path, T bean, Schema schema) throws JAXBException {
		JAXBContext ctx = JAXBContext.newInstance(bean.getClass());
		Marshaller marshaller = ctx.createMarshaller();
		//设置schema验证
		marshaller.setSchema(schema);
		//设置是否格式化输出xml
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(bean, new File(path));
	}

	@SuppressWarnings("restriction")
	public static <T> T parse(String path, Class<T> type) throws JAXBException {
		return parse(path,type,null);
	}
	
	@SuppressWarnings({ "restriction", "unchecked" })
	public static <T> T parse(String path, Class<T> type, Schema schema) throws JAXBException {
		JAXBContext ctx = JAXBContext.newInstance(type);
		Unmarshaller unmarshaller = ctx.createUnmarshaller();
		//设置schema验证
		unmarshaller.setSchema(schema);
		//可以设置一个事件监听器
		unmarshaller.setListener(new Unmarshaller.Listener() {
			@Override
			public void beforeUnmarshal(Object target, Object parent) {
				super.beforeUnmarshal(target, parent);
				System.out.println("beforeUnmarshal target:" + target + " parent:" + parent);
			}
			@Override
			public void afterUnmarshal(Object target, Object parent) {
				System.out.println("afterUnmarshal target:" + target + " parent:" + parent);
				super.afterUnmarshal(target, parent);
			}
		});
		T bean = (T) unmarshaller.unmarshal(new File(path));
		return bean;
	}
	
}
