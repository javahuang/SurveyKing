package cn.surveyking.server.core.uitls;

import cn.surveyking.server.domain.dto.DatabaseInitProperties;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * @author javahuang
 * @date 2022/1/25
 */
public class DatabaseInitHelper {

	private static final String CONFIG_FILE = "application.properties";

	private static final String IP_PATTERN = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	private static final String INIT_DB_SCRIPT_FILE_NAME = "init-%s.sql";

	private static final String MYSQL_DRIVER_DOWNLOAD_URL = "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.28/mysql-connector-java-8.0.28.jar";

	static Scanner sc = new Scanner(System.in);

	private static String db = "mysql";

	public static void init(String[] args) {
		try {
			if (!needCreateProperties()) {
				return;
			}
			// 初始化启动配置文件，初始化数据库脚本
			if ("i".equals(args[0])) {
				initializeDatabase();
			}
			else {
				// 通过启动命令行参数来创建配置文件
				createFileFromProperties(setPropertiesFromArguments(args));
			}
		}
		finally {
			sc.close();
		}
	}

	private static void initializeDatabase() {
		colorOutput("请选择您的数据库类型 : ");
		colorOutput("1) MySql");
		colorOutput("2) H2");
		String selection = sc.nextLine();
		switch (selection) {
		case "1":
			db = "mysql";
			break;
		case "2":
			db = "h2";
			break;
		default:
			return;
		}
		initDb();
	}

	private static void initDb() {
		if ("mysql".equals(db)) {
			initMySql();
		}
	}

	public static void initMySql() {
		// 设置连接参数
		DatabaseInitProperties properties = setProperties();
		// 下载数据库驱动
		downloadDriverJar(properties);
		// 测试数据库链接
		testDatabaseConnection(properties);
		// 创建启动文件
		createFileFromProperties(properties);
		// 执行数据库初始化操作
		initDatabaseSchema(properties);
		// 完成
		finish();
	}

	public static void initH2() {
		// TODO
	}

	@SneakyThrows
	private static boolean needCreateProperties() {
		File f = new File(CONFIG_FILE);
		if (f.exists()) {
			if (yesOrNo("文件已存在,是否覆盖？")) {
				return true;
			}
			return false;
		}
		else {
			f.createNewFile();
		}
		return true;
	}

	private static DatabaseInitProperties setProperties() {
		DatabaseInitProperties properties = new DatabaseInitProperties();
		properties.setApplicationName(setProperty("请输入应用名称:", "名称不能为空", ".+", "SurveyKing"));
		properties.setServerPort(Integer.parseInt(setProperty("请输入应用端口号(默认 1991):", "端口号不能为空", "\\d{1,5}", "1991")));
		properties.setDbIp(setProperty("请输入数据库地址(默认 127.0.0.1):", "IP 地址格式不正确，请重新输入", IP_PATTERN, "127.0.0.1"));
		properties.setDbPort(Integer.parseInt(setProperty("请输入数据库端口(默认 3306):", "端口格式不正确，请重新输入", "\\d{1,5}", "3306")));
		properties.setDbName(setProperty("请输入数据库名称:", "数据库名称不能为空", "\\w+", ""));
		properties.setUsername(setProperty("请输入数据库用户名:", "数据库用户名不能为空", ".+", ""));
		properties.setPassword(setProperty("请输入数据库密码:", "数据库密码不能为空", ".+", ""));
		properties.setDriverClassName("com.mysql.cj.jdbc.Driver");
		return properties;
	}

	private static DatabaseInitProperties setPropertiesFromArguments(String[] args) {
		DatabaseInitProperties properties = new DatabaseInitProperties();
		for (String arg : args) {
			String[] kv = arg.split("=", 2);
			if (kv[0].toLowerCase().contains("port")) {
				properties.setServerPort(Integer.parseInt(kv[1]));
			}
			else if (kv[0].toLowerCase().contains("url")) {
				properties.setDataSourceUrl(kv[1]);
			}
			else if (kv[0].toLowerCase().contains("username")) {
				properties.setUsername(kv[1]);
			}
			else if (kv[0].toLowerCase().contains("password")) {
				properties.setPassword(kv[1]);
			}
		}

		return properties;
	}

	private static String setProperty(String prompt, String errorMsg, String regex, String defaultValue) {
		String result;
		colorOutput(prompt);
		while (true) {
			result = sc.nextLine().trim();
			if (result.isEmpty() && !defaultValue.isEmpty()) {
				result = defaultValue;
				break;
			}
			else if (!result.matches(regex)) {
				colorOutput(errorMsg, 2);
				continue;
			}
			else {
				break;
			}
		}
		return result;
	}

	private static void downloadDriverJar(DatabaseInitProperties properties) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Class.forName(properties.getDriverClassName());
		}
		catch (ClassNotFoundException e) {
			if (yesOrNo("未监测到数据库驱动，是否下载？")) {
				// TODO: 将数据库驱动下载到当前 lib 目录
				colorOutput("开始下载驱动");
			}
		}
	}

	private static void testDatabaseConnection(DatabaseInitProperties properties) {
		if (!yesOrNo("是否测试数据库连接？")) {
			return;
		}
		boolean reachable;
		try (Connection connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s",
				properties.getDbIp(), properties.getDbPort(), properties.getDbName()), properties.getUsername(),
				properties.getPassword())) {
			reachable = connection.isValid(10);
		}
		catch (SQLException e) {
			reachable = false;
		}
		if (reachable) {
			colorOutput("数据库连接成功", 1);
		}
		else {
			if (yesOrNo("数据库连接不可用，是否重新设置？")) {
				initMySql();
			}
		}

	}

	/**
	 * 数据库初始化
	 * @param properties
	 */
	@SneakyThrows
	private static void initDatabaseSchema(DatabaseInitProperties properties) {
		if (!yesOrNo("是否执行数据库初始化操作？")) {
			return;
		}
		try (Connection connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s",
				properties.getDbIp(), properties.getDbPort(), properties.getDbName()), properties.getUsername(),
				properties.getPassword())) {
			ScriptRunner runner = new ScriptRunner(connection, false, false);
			String file = getJarFilePath() + File.separator + String.format(INIT_DB_SCRIPT_FILE_NAME, db);
			if (!new File(file).exists()) {
				colorOutput(String.format("初始化脚本不存在 %s", file), 2);
				initDatabaseSchema(properties);
				return;
			}
			runner.runScript(new BufferedReader(new FileReader(file)));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		colorOutput("数据库初始化完成", 1);
	}

	private static void finish() {
		colorOutput(String.format("初始化完成，有问题请联系作者。"), 1);
	}

	@SneakyThrows
	private static void createFileFromProperties(DatabaseInitProperties properties) {
		File f = new File(CONFIG_FILE);
		writeLine(f, "spring.application.name=%s\n", properties.getApplicationName());
		writeLine(f, "server.port=%d\n", properties.getServerPort());
		writeLine(f, "spring.datasource.driver-class-name=%s\n", properties.getDriverClassName());
		if (properties.getDataSourceUrl() == null) {
			writeLine(f, "spring.datasource.url=jdbc:mysql://%s:%d/%s\n", properties.getDbIp(), properties.getDbPort(),
					properties.getDbName());
		}
		else {
			writeLine(f, "spring.datasource.url=%s\n", properties.getDataSourceUrl());
		}
		writeLine(f, "spring.datasource.username=%s\n", properties.getUsername());
		writeLine(f, "spring.datasource.password=%s\n", properties.getPassword());
	}

	@SneakyThrows
	private static void writeLine(File file, String key, Object... value) {
		if (value.length > 0 && value[0] != null) {
			Files.write(file.toPath(), String.format(key, value).getBytes(StandardCharsets.UTF_8),
					StandardOpenOption.APPEND);
		}
	}

	private static boolean yesOrNo(String prompt) {
		colorOutput(String.format("%s Y(是) N(否)", prompt));
		String yesOrNo = sc.nextLine();
		if (yesOrNo.toUpperCase().contains("Y")) {
			return true;
		}
		return false;
	}

	/**
	 * 获取当前 jar 运行目录
	 * @return
	 */
	@SneakyThrows
	public static String getJarFilePath() {
		String path = DatabaseInitHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath = URLDecoder.decode(path, "UTF-8");
		return new File(decodedPath.substring(0, decodedPath.indexOf("!/BOOT-INF")).split(":")[1]).getParent();
	}

	private static void colorOutput(String output) {
		colorOutput(output, 0);
	}

	private static void colorOutput(String output, int level) {
		switch (level) {
		case 1: // success
			System.out.println(Ansi.Green.colorize(output));
			break;
		case 2: // fail
			System.out.println(Ansi.Red.colorize(output));
			break;
		default: // info
			System.out.println(Ansi.Blue.colorize(output));
			break;
		}
	}

}
