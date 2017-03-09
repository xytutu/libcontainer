package com.cainiao.libcontainer.module;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModuleUtils {

	public static final Logger LOGGER = LoggerFactory.getLogger(ModuleUtils.class);

	public static ClassLoader findExtClassLoader() {
		ClassLoader currentClassLoader = ModuleUtils.class.getClassLoader();
		while (currentClassLoader.getParent() != null) {
			currentClassLoader = currentClassLoader.getParent();
		}
		return currentClassLoader;
	}

	public static void changeClassLoaderParent(ClassLoader classLoader, ClassLoader parent)
			throws NoSuchFieldException, IllegalAccessException {
		Class<ClassLoader> sysClass = ClassLoader.class;
		Field parentField = sysClass.getDeclaredField("parent");
		parentField.setAccessible(true);
		parentField.set(classLoader, parent);
	}

}
