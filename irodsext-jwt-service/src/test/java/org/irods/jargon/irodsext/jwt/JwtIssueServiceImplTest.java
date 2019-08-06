package org.irods.jargon.irodsext.jwt;

import org.junit.Assert;
import org.junit.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.WeakKeyException;

public class JwtIssueServiceImplTest {

	@Test(expected = WeakKeyException.class)
	public void testIssueJwtWeakKey() {
		JwtServiceConfig config = new JwtServiceConfig();
		config.setAlgo(SignatureAlgorithm.HS256.getValue());
		config.setIssuer("test");
		config.setSecret("thisisasecret");
		JwtIssueServiceImpl jwtIssueServiceImpl = new JwtIssueServiceImpl(config);
		jwtIssueServiceImpl.issueJwtToken("subject");
	}

	@Test
	public void testIssueJwt() {
		JwtServiceConfig config = new JwtServiceConfig();
		config.setAlgo(SignatureAlgorithm.HS256.getValue());
		config.setIssuer("test");
		config.setSecret("thisisasecretthatisverysecretyouwillneverguessthiskey");
		JwtIssueServiceImpl jwtIssueServiceImpl = new JwtIssueServiceImpl(config);
		String jwt = jwtIssueServiceImpl.issueJwtToken("subject");
		Assert.assertNotNull("no jwt issued", jwt);
	}

	@Test
	public void testIssueAndDecodeJwt() {
		JwtServiceConfig config = new JwtServiceConfig();
		config.setAlgo(SignatureAlgorithm.HS256.getValue());
		config.setIssuer("test");
		config.setSecret("thisisasecretthatisverysecretyouwillneverguessthiskeyhurray");
		JwtIssueServiceImpl jwtIssueServiceImpl = new JwtIssueServiceImpl(config);
		String jwt = jwtIssueServiceImpl.issueJwtToken("subject");
		Jws<Claims> actual = jwtIssueServiceImpl.decodeJwtToken(jwt);
		Assert.assertNotNull("claims not returned", actual);

	}

}
