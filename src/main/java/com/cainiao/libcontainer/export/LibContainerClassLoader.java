package com.cainiao.libcontainer.export;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentHashMap;

import com.cainiao.libcontainer.module.ModuleUtils;

/**
 * 共同导出classLoader
 * 
 * @author bianshuai
 *
 */
public class LibContainerClassLoader extends URLClassLoader {

	private static ConcurrentHashMap<String, Class<?>> exportClass = new ConcurrentHashMap<String, Class<?>>();

	public LibContainerClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	/**
	 * 打破双亲委派的做法
	 */
	// @Override
	// public Class<?> loadClass(String name, boolean resolve) throws
	// ClassNotFoundException {
	// Class<?> c = findLoadedClass(name);
	// Class<?> targetClass = exportClass.get(name);
	// if (targetClass != null) {
	// return targetClass;
	// }
	//
	// return super.loadClass(name, resolve);
	// }

	/**
	 * 不打破双亲委派的做法
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> targetClass = exportClass.get(name);
		if (targetClass != null) {
			return targetClass;
		}
		throw new ClassNotFoundException(name);
	}

	public static void exportClass(Class<?> _class) {
		Object preObject = exportClass.putIfAbsent(_class.getName(), _class);
		if (preObject != null) {
			ModuleUtils.LOGGER.error(_class.getName() + "has bean export by other model.");
		}
	}

}
