package cn.surveyking.server.flow;

import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.junit.jupiter.api.Test;

/**
 * @author javahuang
 * @date 2022/2/11
 */
public class FlowDatabaseInitTests {

	@Test
	public void initDb() {
		ProcessEngineConfiguration pec = new StandaloneProcessEngineConfiguration();
		pec.setJdbcDriver("com.mysql.cj.jdbc.Driver");
		pec.setJdbcUrl(
				"jdbc:mysql://localhost:3306/workflow?characterEncoding=utf8&useSSL=false&nullCatalogMeansCurrent=true");
		pec.setJdbcUsername("root");
		pec.setJdbcPassword("dota8888");
		pec.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		pec.buildProcessEngine();
	}

}
