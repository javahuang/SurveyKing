package cn.surveyking.server.core.common;

/**
 * 二元组对象，用于返回多个值场景
 *
 * @author javahuang
 * @date 2022/2/28
 */
public class Tuple2<T1, T2> {

	private final T1 first;

	private final T2 second;

	public Tuple2(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}

	public T1 getFirst() {
		return first;
	}

	public T2 getSecond() {
		return second;
	}

}
