package com.stockprophet.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadCss {

	public static void main(String[] args) {
		File css = 	new File("css/");
		List<String> cssFiles = new ArrayList<String>();
		for(File file : css.listFiles())
			if(file.isFile() && file.getName().endsWith("css"))
				cssFiles.add(file.getName().replaceAll(".*/", ""));
		System.out.println(cssFiles);
	}

}
