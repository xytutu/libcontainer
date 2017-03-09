package com.cainiao.libcontainer.module;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ModuleInfo {

	private List<URL> urlInfo = new ArrayList<URL>();

	private String exportInfo;

	public String getExportInfo() {
		return exportInfo;
	}

	public void setExportInfo(String exportInfo) {
		this.exportInfo = exportInfo;
	}

	public List<URL> getUrlInfo() {
		return urlInfo;
	}

	@SuppressWarnings("deprecation")
	public void addUrl(File file) {
		try {
			urlInfo.add(file.toURL());
		} catch (MalformedURLException e) {
			ModuleUtils.LOGGER.error(e.getMessage());
		}
	}

}
