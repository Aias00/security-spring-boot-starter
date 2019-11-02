package com.aias.springboot.security.jwt;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.aias.springboot.security.properties.SecurityProperties;

import io.jsonwebtoken.*;

@Component
public class JwtTokenUtil {

	private final Log logger = LogFactory.getLog(JwtTokenUtil.class);
	// (主题)
	private static final String CLAIM_KEY_USER_ID = "sub";
	// (目标受众)
	private static final String CLAIM_KEY_CREATED = "created";

	public static final String ANONYMOUS_USER = "anonymousUser";

	private String secret;

	private Integer expirationDay;

	public String headerKey;

	public String getHeaderKey() {
		return headerKey;
	}

	@Autowired
	private SecurityProperties properties;

	@PostConstruct
	public void init() {
		this.secret = this.properties.getJwt().getSecret();
		this.expirationDay = this.properties.getJwt().getExpiration();
		this.headerKey = this.properties.getJwt().getHeaderKey();
	}

	public String getUsernameFromToken(String token) {
		logger.info("secret = " + secret + ",  expirationDay = " + expirationDay
				+ ",  token:" + token);
		String username;
		try {
			final Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}

	public Date getCreatedDateFromToken(String token) {
		Date created;
		try {
			final Claims claims = getClaimsFromToken(token);
			created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
		} catch (Exception e) {
			created = null;
		}
		return created;
	}

	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			JwtParser jwtParser = Jwts.parser().setSigningKey(secret);
			Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
			claims = claimsJws.getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	private Date generateExpirationDate() {

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(5, expirationDay);
		return cal.getTime();

	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		logger.info(
				"JwtTokenUtil isTokenExpired expirationDay = " + expiration);
		return expiration.before(new Date());
	}

	private Boolean isCreatedBeforeLastPasswordReset(Date created,
			Date lastPasswordReset) {
		logger.info("JwtTokenUtil isTokenExpired created = " + created
				+ ", lastPasswordReset=" + lastPasswordReset);
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_USER_ID, userDetails.getUsername());
		claims.put(CLAIM_KEY_CREATED, new Date());
		return generateToken(claims);
	}

	String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims)
				.setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		final Date created = getCreatedDateFromToken(token);
		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
				&& !isTokenExpired(token);
	}

	public String refreshToken(String token) {
		String refreshedToken;
		try {
			final Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			refreshedToken = generateToken(claims);
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String userId = getUsernameFromToken(token);
		final Date created = getCreatedDateFromToken(token);

		logger.info("JwtTokenUtil validateToken userId = " + userId
				+ ", created =" + created);

		boolean userIdEqual = userId.equals(userDetails.getUsername());
		boolean isTokenExpired = isTokenExpired(token);
		logger.info("JwtTokenUtil validateToken userIdEqual = " + userIdEqual
				+ ", isTokenExpired =" + isTokenExpired);
		return (userIdEqual && !isTokenExpired);
	}

	public static String string2Unicode(String string) {
		StringBuffer unicode = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			// 取出每一个字符
			char c = string.charAt(i);
			// 转换为unicode
			unicode.append("\\u" + Integer.toHexString(c));
		}
		return unicode.toString();
	}
}
