package cn.surveyking.server.core.uitls;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * 国际化辅助，统一管理题库 Excel 模板的 sheet 名称和表头
 */
public final class RepoTemplateI18n {

	private static final ResourceBundleMessageSource MESSAGE_SOURCE = initMessageSource();

	private static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.CHINA,
			Locale.ENGLISH, Locale.US);

	private static final Map<String, Set<String>> ALIAS_CACHE = new ConcurrentHashMap<>();

	private RepoTemplateI18n() {
	}

	private static ResourceBundleMessageSource initMessageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setFallbackToSystemLocale(false);
		return messageSource;
	}

	private static String normalize(String value) {
		return value == null ? null : value.trim().toLowerCase(Locale.ROOT);
	}

	private static Set<String> getAliases(String key, String defaultMessage) {
		return ALIAS_CACHE.computeIfAbsent(key, k -> buildAliases(key, defaultMessage, null));
	}

	private static Set<String> getAliases(String key, String defaultMessage, Object[] args) {
		String cacheKey = key + Arrays.deepToString(args);
		return ALIAS_CACHE.computeIfAbsent(cacheKey, k -> buildAliases(key, defaultMessage, args));
	}

	private static Set<String> buildAliases(String key, String defaultMessage, Object[] args) {
		Set<String> aliases = new LinkedHashSet<>();
		for (Locale locale : SUPPORTED_LOCALES) {
			String message = MESSAGE_SOURCE.getMessage(key, args, defaultMessage, locale);
			if (StringUtils.hasText(message)) {
				aliases.add(normalize(message));
			}
		}
		if (StringUtils.hasText(defaultMessage)) {
			aliases.add(normalize(defaultMessage));
		}
		return aliases.stream().filter(Objects::nonNull).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	private static boolean matches(String input, Set<String> aliases) {
		return input != null && aliases.contains(normalize(input));
	}

	public static String message(String key, String defaultMessage) {
		return MESSAGE_SOURCE.getMessage(key, null, defaultMessage, LocaleContextHolder.getLocale());
	}

	public static String message(String key, String defaultPattern, Object... args) {
		return MESSAGE_SOURCE.getMessage(key, args, defaultPattern, LocaleContextHolder.getLocale());
	}

	public static boolean isOptionColumn(String cellText) {
		return matches(cellText, OptionHeader.ALL_LABELS);
	}

	public static boolean isBlankColumn(String cellText) {
		return matches(cellText, BlankHeader.ALL_LABELS);
	}

	public static String optionLabel(String suffix) {
		return message("repo.template.header.option", "选项{0}", suffix);
	}

	public static String blankLabel(String index) {
		return message("repo.template.header.blank", "空{0}", index);
	}

	public static String workbookName() {
		return message("repo.template.workbook.name", "题库");
	}

	private static class OptionHeader {

		private static final List<String> OPTION_SUFFIXES = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H");

		private static final Set<String> ALL_LABELS = OPTION_SUFFIXES.stream()
			.flatMap(suffix -> getAliases("repo.template.header.option", "选项{0}", new Object[] { suffix }).stream())
			.collect(Collectors.toCollection(LinkedHashSet::new));

	}

	private static class BlankHeader {

		private static final List<String> BLANK_INDEXES = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");

		private static final Set<String> ALL_LABELS = BLANK_INDEXES.stream()
			.flatMap(index -> getAliases("repo.template.header.blank", "空{0}", new Object[] { index }).stream())
			.collect(Collectors.toCollection(LinkedHashSet::new));

	}

	public enum SheetType {

		SINGLE_CHOICE("repo.template.sheet.singleChoice", "单选题"),
		MULTIPLE_CHOICE("repo.template.sheet.multipleChoice", "多选题"),
		TRUE_FALSE("repo.template.sheet.trueFalse", "判断题"),
		FILL_BLANK("repo.template.sheet.fillBlank", "填空题"),
		TEXTAREA("repo.template.sheet.textarea", "简答题");

		private final String key;

		private final String defaultLabel;

		private final Set<String> aliases;

		SheetType(String key, String defaultLabel) {
			this.key = key;
			this.defaultLabel = defaultLabel;
			this.aliases = getAliases(key, defaultLabel);
		}

		public String displayName() {
			return message(key, defaultLabel);
		}

		public boolean matches(String name) {
			return RepoTemplateI18n.matches(name, aliases);
		}

		public static SheetType fromName(String name) {
			return Arrays.stream(values()).filter(type -> type.matches(name)).findFirst().orElse(null);
		}

	}

	public enum HeaderLabel {

		SERIAL_NO("repo.template.header.serialNo", "序号", (map, index) -> map.put("serialNo", index)),
		TITLE("repo.template.header.title", "题干", (map, index) -> map.put("title", index)),
		ANALYSIS("repo.template.header.analysis", "解析", (map, index) -> map.put("examAnalysis", index)),
		SCORE("repo.template.header.score", "分数", (map, index) -> map.put("examScore", index)),
		SINGLE_BLANK_SCORE("repo.template.header.singleBlankScore", "单空分数", (map, index) -> map.put("examScore", index)),
		ANSWER("repo.template.header.answer", "答案", (map, index) -> map.put("examCorrectAnswer", index)),
		TAGS("repo.template.header.tags", "标签", (map, index) -> map.put("tags", index));

		private final String key;

		private final String defaultLabel;

		private final Set<String> aliases;

		private final BiConsumer<Map<String, Integer>, Integer> mapper;

		HeaderLabel(String key, String defaultLabel, BiConsumer<Map<String, Integer>, Integer> mapper) {
			this.key = key;
			this.defaultLabel = defaultLabel;
			this.aliases = getAliases(key, defaultLabel);
			this.mapper = mapper;
		}

		public boolean matches(String name) {
			return RepoTemplateI18n.matches(name, aliases);
		}

		public void apply(Map<String, Integer> target, int columnIndex) {
			mapper.accept(target, columnIndex);
		}

		public String displayLabel() {
			return message(key, defaultLabel);
		}

		public static HeaderLabel fromText(String text) {
			return Arrays.stream(values()).filter(label -> label.matches(text)).findFirst().orElse(null);
		}

	}

}
