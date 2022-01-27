package cn.surveyking.server.flow.helper;

import cn.surveyking.server.flow.domain.model.FlowEntryNode;
import cn.surveyking.server.flow.service.FlowEntryNodeService;
import cn.surveyking.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author javahuang
 * @date 2022/1/19
 */
@Component("t")
@RequiredArgsConstructor
public class TaskHelper {

	private final FlowEntryNodeService nodeService;

	private final UserService userService;

	/**
	 * 根据 xml 节点计算审批人
	 * @param activityId 当前 XML 节点 ID
	 * @param starterUserId 发起人用户 ID
	 */
	public Set<String> getUsers(String activityId, String starterUserId) {
		FlowEntryNode node = nodeService.getById(activityId);
		Set<String> result = new LinkedHashSet<>();
		for (String identity : node.getIdentity()) {
			// 普通用户
			if (identity.startsWith("U:")) {
				result.add(identity.split(":")[1]);
			}
			else if (identity.startsWith("R:") || identity.startsWith("P:")) {
				// 角色和岗位
				result.addAll(userService.getUsersByGroup(identity, starterUserId));
			}
		}
		return result;
	}

}
