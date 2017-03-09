package com.cainiao.libcontainer.module;

import java.net.URL;
import java.net.URLClassLoader;

import com.cainiao.libcontainer.export.LibContainerClassLoader;

public class ModuleClassLoader extends URLClassLoader {

	private String exportInfo;

	public ModuleClassLoader(URL[] urls, ClassLoader parent, String exportInfo) {
		super(urls, parent);
		this.exportInfo = exportInfo;
	}

	/**
	 * 导出需要到处的类
	 */
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> target = null;
		try {
			target = super.loadClass(name);
			return target;
		} finally {
			if (target != null && isExportClass(name)) {
				LibContainerClassLoader.exportClass(target);
			}
		}
	}

	private boolean isExportClass(String className) {
		String[] exports = exportInfo.split(";");
		for (String export : exports) {
			if (export.endsWith("*")) {
				String exportTmp = export.substring(0, export.length() - 1);
				if (exportTmp.isEmpty()) {
					return true;
				} else {
					exportTmp = exportTmp.substring(0, exportTmp.length() - 1);
					return className.startsWith(exportTmp) ? true : false;
				}
			} else {
				return className.equals(export) ? true : false;
			}
		}
		return false;
	}

}
