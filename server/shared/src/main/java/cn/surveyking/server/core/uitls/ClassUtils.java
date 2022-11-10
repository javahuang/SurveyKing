package cn.surveyking.server.core.uitls;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author javahuang
 * @date 2022/11/10
 */
public class ClassUtils {

	/**
	 * 将 class的多个层级的field 打平成 . 分割的
	 * @param cls
	 * @param parentAttr
	 * @param maxDepth
	 * @return
	 */
	public static List<String> flatClassFields(Class cls, List<String> parentAttr, int maxDepth) {
		List<String> attributes = new ArrayList<>();
		for (Field field : cls.getDeclaredFields()) {
			List<String> newArr = new ArrayList<>(parentAttr);
			if (field.getType().isMemberClass() && !field.getType().isEnum() && newArr.size() < maxDepth - 1) {
				newArr.add(field.getName());
				attributes.addAll(flatClassFields(field.getType(), newArr, maxDepth));
			}
			else {
				newArr.add(field.getName());
				attributes.add(newArr.stream().collect(Collectors.joining(".")));
			}
		}
		return attributes;
	}

}
