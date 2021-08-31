package cn.surveyking.server.core.uitls;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SchemaParserTest {

	@Test
	void trimHtmlTag() {
		Assertions.assertEquals("hello world",
				SchemaParser.trimHtmlTag("<div><span style=\"font-size:10px\">hello world</span></div>"));
	}

}