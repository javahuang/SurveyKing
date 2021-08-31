package cn.surveyking.server.core.mybatis;

import cn.surveyking.server.core.uitls.SecurityContextUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author javahuang
 * @date 2021/8/31
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		this.strictInsertFill(metaObject, "createAt", () -> new Date(), Date.class);
		this.strictInsertFill(metaObject, "createBy", () -> SecurityContextUtils.getUsername(), String.class);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.strictUpdateFill(metaObject, "updateAt", () -> new Date(), Date.class);
		this.strictUpdateFill(metaObject, "updateBy", () -> SecurityContextUtils.getUsername(), String.class);
	}

}
