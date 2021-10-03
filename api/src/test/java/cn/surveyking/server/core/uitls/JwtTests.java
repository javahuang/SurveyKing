package cn.surveyking.server.core.uitls;

import cn.surveyking.server.web.domain.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author javahuang
 * @date 2021/8/23
 */
public class JwtTests {

	@Test
	public void sampleGenerate() {
		// We need a signing key, so we'll create one just for this example. Usually
		// the key would be read from your application configuration instead.
		Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		String secretString = Encoders.BASE64.encode(key.getEncoded());
		System.out.println(secretString);

		String jws = Jwts.builder().setSubject("Joe").signWith(key).compact();

		try {
			assert Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws).getBody().getSubject()
					.equals("Joe");
			// OK, we can trust this JWT
		}
		catch (JwtException e) {
			// don't trust the JWT!
		}

		String token = Jwts.builder().setSubject(String.format("%s,%s", "234", "dahuang")).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
				.signWith(SignatureAlgorithm.HS256, key).compact();
		System.out.println(token);
	}

	@Test
	public void testJackson() {
		ObjectMapper mapper = new ObjectMapper();
		User user = new User();
		Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		Map<String, Object> result = new HashMap<>();
		result.put("user", user);
		String jws = Jwts.builder().serializeToJsonWith(new JacksonSerializer(mapper)).setClaims(result)
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
				.signWith(key).compact();
		System.out.println(jws);

		User newUser = Jwts.parserBuilder()
				.deserializeJsonWith(new JacksonDeserializer(Maps.of("user", User.class).build())).setSigningKey(key)
				.build().parseClaimsJws(jws).getBody().get("user", User.class);
	}

	public static void main(String[] args) {
		Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		String secretString = Encoders.BASE64.encode(key.getEncoded());
		System.out.println(secretString);

		String key1 = "oEAR2+wr+cckpS2cfAbHVxZ0qbdy+xfZgqhu/3S6AO0=";
		System.out.println(new String(Keys.hmacShaKeyFor(key1.getBytes()).getEncoded()));
	}

}
