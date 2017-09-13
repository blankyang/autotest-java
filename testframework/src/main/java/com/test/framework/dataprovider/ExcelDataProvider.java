package com.test.framework.dataprovider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;




import org.testng.Assert;
import org.testng.annotations.DataProvider;

import com.test.framework.utils.ReadExcelUtil;

public class ExcelDataProvider {
	
	private static Object[][] obj;
	private static int sheetIndex;

	static {
		Properties prop = new Properties();
		try {
			String path = System.getProperty("user.dir");
			prop.load(new FileInputStream(path + "/data/config.properties"));
			sheetIndex = Integer.parseInt(prop.getProperty("sheetIndex"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	// 从Excel文本文件中获得数据
	@DataProvider(name = "GetDataFromExcel")
	public static Object[][] getTestDataFromExcel(Method m)
			throws FileNotFoundException {
		// 通过反射获得函数名称，可以为多个测试方法提供数据驱动
		Object[][] o = new Object[][] {};
		// 角标从0开始，后面数字表示取多少行数据
		if (m.getName().equals("testImageRecognition")) {
			return new BaseExcelData().getData("testA", "DataProvider.xls", 0,2);
		}
		return o;
	}

	@DataProvider(name = "testImageRec")
	public static Object[][] getTestDataToBankCard(Method method)
			throws FileNotFoundException {
		List<Map<String, String>> parList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> sonList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> exList = ReadExcelUtil.readXlsx(
				"data/DataProvider.xls", sheetIndex);
		for (int i = 0; i < exList.size(); i++) {
			Map<String, String> map = (Map<String, String>) exList.get(i);
			sonList.add(map);
		}
		if (sonList.size() > 0) {
			obj = new Object[sonList.size()][];
			for (int i = 0; i < sonList.size(); i++) {
				obj[i] = new Object[] { sonList.get(i) };
			}
			return obj;
		} else {
			Assert.assertTrue(sonList.size() != 0, parList + "为空，找不到属性值："
					+ method.getName());
			return null;
		}
	}	
}
