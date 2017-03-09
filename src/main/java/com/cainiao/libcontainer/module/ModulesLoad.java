package com.cainiao.libcontainer.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModulesLoad {

	public static void Load(String resoucePath) {

		File[] files = spiltModules(resoucePath);

		if (files != null) {
			for (File file : files) {
				if (file != null) {
					ModuleInfo moduleInfo = splitModle(file);
					preLoadModule(moduleInfo);
				}
			}
		}

	}

	private static File[] spiltModules(String resoucePath) {

		URL topResource = ModulesLoad.class.getClassLoader().getResource(resoucePath);

		String topResourcePath = topResource.getFile();

		File topFile = new File(topResourcePath);

		if (topFile != null && topFile.isDirectory()) {
			File[] secondFiles = topFile.listFiles();
			return secondFiles;
		}
		return null;
	}

	private static ModuleInfo splitModle(File moduleFilePath) {
		if (moduleFilePath.isDirectory()) {
			File[] infoFiles = moduleFilePath.listFiles();
			if (infoFiles != null) {
				ModuleInfo result = new ModuleInfo();
				for (File infoFile : infoFiles) {
					if (infoFile.getName().endsWith("jar"))
						result.addUrl(infoFile);
					else if (infoFile.getName().equals("conf.properties"))
						result.setExportInfo(exportInfo(infoFile));
				}
				return result;
			}
		}
		return null;
	}

	private static String exportInfo(File exportFile) {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(exportFile));
			if (properties.containsKey("export")) {
				return properties.getProperty("export");
			}
		} catch (Exception e) {
			ModuleUtils.LOGGER.error(e.getMessage());
		}
		return null;
	}

	private static void preLoadModule(ModuleInfo moduleInfo) {
		ModuleClassLoader moduleClassLoader = new ModuleClassLoader(
				moduleInfo.getUrlInfo().toArray(new URL[moduleInfo.getUrlInfo().size()]),
				ModuleUtils.findExtClassLoader(), moduleInfo.getExportInfo());

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(moduleClassLoader);
		try {
			preload(moduleInfo, moduleClassLoader);
		} catch (Exception e) {
			ModuleUtils.LOGGER.error(e.getMessage());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	/**
	 * 手动触发 解析jar然后加载class
	 * 
	 * 这里并不是真正的查询类 而是预加载所有的class
	 * 
	 * @param moduleInfo
	 * @param classLoader
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static void preload(ModuleInfo moduleInfo, ClassLoader classLoader)
			throws IOException, ClassNotFoundException {
		for (URL url : moduleInfo.getUrlInfo()) {
			JarFile jarFile = null;
			try {
				jarFile = new JarFile(url.getFile());
				Enumeration<JarEntry> es = jarFile.entries();
				while (es.hasMoreElements()) {
					JarEntry jarEntry = es.nextElement();
					String name = jarEntry.getName();
					if (name != null && name.endsWith(".class")) {
						Class.forName(name.replace("/", ".").substring(0, name.length() - 6), true, classLoader);
					}
				}
			} finally {
				try {
					jarFile.close();
				} catch (IOException e) {
				}
			}
		}
	}

}
