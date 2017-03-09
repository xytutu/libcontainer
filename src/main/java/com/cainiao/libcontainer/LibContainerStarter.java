package com.cainiao.libcontainer;

import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import com.cainiao.libcontainer.export.LibContainerClassLoader;
import com.cainiao.libcontainer.module.ModuleUtils;
import com.cainiao.libcontainer.module.ModulesLoad;

/**
 * 按一下文件结构将要隔离的lib和配置放在，放在classpath路径下
 * 
 * libcontainer
 * 
 * ---libA
 * 
 * -------exportLibs
 * 
 * -------conf.properties
 * 
 * ----------export=com.test.a;com.test1.*
 * 
 * @author bianshuai
 *
 */
public class LibContainerStarter {

	private static final String path = "libcontainer";

	private static AtomicBoolean start = new AtomicBoolean(false);

	public static void start() {
		if (start.compareAndSet(false, true)) {
			ModulesLoad.Load(path);
			ClassLoader classLoader = LibContainerStarter.class.getClassLoader();
			while (classLoader.getParent().getParent() != null) {
				classLoader = classLoader.getParent();
			}
			try {
				LibContainerClassLoader commonClassLoader = new LibContainerClassLoader(new URL[] {},
						ModuleUtils.findExtClassLoader());
				// System.out.println(findExtClassLoader());
				// ModuleUtils.changeClassLoaderParent(commonClassLoader,
				// ModuleUtils.findExtClassLoader());
				ModuleUtils.changeClassLoaderParent(classLoader, commonClassLoader);
				// System.out.println(commonClassLoader.getParent());
			} catch (Exception e) {
				ModuleUtils.LOGGER.error(e.getMessage());
			}
		}
	}

}
