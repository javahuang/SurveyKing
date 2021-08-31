package cn.surveyking.server.core.uitls;

import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author javahuang
 * @date 2021/2/1
 */
public class ExcelExporter {

	private Workbook workbook;

	private Map<String, List<String>> columnsBySheet = new ConcurrentHashMap<>();

	private Map<String, Integer> rowIndexBySheet = new ConcurrentHashMap<>();

	private ByteArrayOutputStream os = new ByteArrayOutputStream();

	ThreadLocal<Worksheet> threadLocal = new ThreadLocal<>();

	public ExcelExporter() {
		this.workbook = new Workbook(os, "survey", "1.0");
	}

	public Worksheet createSheet(String sheetName) {
		Worksheet worksheet = workbook.newWorksheet(sheetName);
		worksheet.setFitToPage(true);
		threadLocal.set(worksheet);
		rowIndexBySheet.put(sheetName, 1);
		return worksheet;
	}

	public void createHeader(List<String> columns) {
		Worksheet sheet = threadLocal.get();
		for (int i = 0; i < columns.size(); i++) {
			sheet.value(0, i, columns.get(i));
		}
	}

	public void createRow(List<List<Object>> rows) {
		Worksheet sheet = threadLocal.get();
		String sheetName = sheet.getName();
		int rowNum = rowIndexBySheet.get(sheetName);
		for (int r = 1; r <= rows.size(); r++) {
			List<Object> row = rows.get(r - 1);
			for (int c = 0; c < row.size(); c++) {
				Object value = row.get(c);
				if (value instanceof Integer) {
					sheet.value(r, c, (Number) value);
				}
				else if (value instanceof String) {
					sheet.value(r, c, (String) value);
				}
				else if (value != null) {
					sheet.value(r, c, value.toString());
				}
			}
			rowNum++;
		}
		rowIndexBySheet.put(sheetName, rowNum);
	}

	public ByteArrayInputStream export() {
		try {
			this.workbook.finish();
			return new ByteArrayInputStream(this.os.toByteArray());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
