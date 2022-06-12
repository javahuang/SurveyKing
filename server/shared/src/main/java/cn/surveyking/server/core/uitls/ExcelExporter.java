package cn.surveyking.server.core.uitls;

import lombok.Data;
import lombok.experimental.Accessors;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.surveyking.server.core.uitls.SchemaHelper.openidColumnName;

/**
 * 轻量级 excel 导出工具，换成 poi 打的包会多出十几 M，不能接受，-_-||
 *
 * @author javahuang
 * @date 2021/2/1
 */
public class ExcelExporter {

	private Workbook workbook;

	private Map<String, List<String>> columnsBySheet = new ConcurrentHashMap<>();

	private Map<String, Integer> rowIndexBySheet = new ConcurrentHashMap<>();

	private ByteArrayOutputStream os = new ByteArrayOutputStream();

	ThreadLocal<Worksheet> localSheet = new ThreadLocal<>();

	protected static ThreadLocal<Integer> localOpenid = new ThreadLocal<>();

	public ExcelExporter() {
		this.workbook = new Workbook(os, "survey", "1.0");
	}

	public ExcelExporter(OutputStream outputStream) {
		this.workbook = new Workbook(outputStream, "survey", "1.0");
	}

	public Worksheet createSheet(String sheetName) {
		Worksheet worksheet = workbook.newWorksheet(sheetName);
		worksheet.fitToWidth((short) 10);
		worksheet.setFitToPage(true);
		localSheet.set(worksheet);
		rowIndexBySheet.put(sheetName, 1);
		return worksheet;
	}

	public void createHeader(List<String> columns) {
		Worksheet sheet = localSheet.get();
		for (int i = 0; i < columns.size(); i++) {
			sheet.value(0, i, columns.get(i));
		}
	}

	public void createRow(List<List<Object>> rows) {
		Worksheet sheet = localSheet.get();
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

	public void exportToStream() {
		try {
			this.workbook.finish();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Data
	@Accessors(chain = true)
	public static class Builder {

		private String sheetName;

		private List<String> columns;

		private List<List<Object>> rows;

		private OutputStream outputStream;

		public ExcelExporter build() {
			ExcelExporter excelExporter;
			if (this.outputStream != null) {
				excelExporter = new ExcelExporter(this.outputStream);
			}
			else {
				excelExporter = new ExcelExporter();
			}
			// 如果答案中不存在 openid，则删除该列
			if (!Boolean.TRUE.equals(SchemaHelper.localOpenId.get())) {
				int openidColumnIndex = this.columns.indexOf(openidColumnName);
				this.columns.remove(openidColumnIndex);
				this.rows.forEach(row -> {
					row.remove(openidColumnIndex);
				});
				SchemaHelper.localOpenId.remove();
			}
			excelExporter.createSheet(this.sheetName);
			excelExporter.createRow(this.rows);
			excelExporter.createHeader(this.columns);

			return excelExporter;
		}

	}

}
