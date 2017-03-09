package com.cainiao.libcontainer;

import java.lang.reflect.Field;

import org.junit.Test;

public class StartTest {
	@Test
	public void testStart() {
		LibContainerStarter.start();

		ClassLoader currentClassLoader = StartTest.class.getClassLoader();
		while (currentClassLoader != null) {
			System.out.println(currentClassLoader);
			currentClassLoader = currentClassLoader.getParent();
		}
		try {
			Class<?> object = Class.forName("org.apache.http.concurrent.BasicFuture");
			// Class<?> object =
			// Class.forName("org.apache.http.impl.io.DefaultHttpRequestParserFactory");
			for (Field field : object.getDeclaredFields()) {
				System.out.println(field.getName());
			}
			System.out.println("class loader : " + object.getClassLoader());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
