/**
 * 
 */
package org.irods.jargon.irodsext.jwt;

/**
 * Basic configs for a service to issue and decode jwts used in iRODS
 * microservices
 * 
 * @author Mike Conway - NIEHS
 *
 */
public class JwtServiceConfig {

	/**
	 * Issuer typically in reverse dns name format, used as "iss" in the JWT
	 */
	private String issuer = "";
	/**
	 * Secret used to sign tokens given the provided algo
	 */
	private String secret = "";
	/**
	 * Signing algo used in JWT
	 */
	private String algo = "";

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getAlgo() {
		return algo;
	}

	public void setAlgo(String algo) {
		this.algo = algo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JwtServiceConfig [issuer=").append(issuer).append(", algo=").append(algo).append("]");
		return builder.toString();
	}

}
