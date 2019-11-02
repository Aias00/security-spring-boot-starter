package com.aias.springboot.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * <b>
 *
 * <br>
 * <b>@ClassName:</b> SecurityProperties <br>
 * <b>@Date:</b> 2019/11/2 <br>
 *
 * @author <a> liuhy </a><br>
 */
@Data
@ConfigurationProperties(prefix = SecurityProperties.SECURITY_PROPERTIES_PREFIX)
public class SecurityProperties {
	static final String SECURITY_PROPERTIES_PREFIX = "spring.security";

	private HttpSecurityProperties http;
	private WebSecurityProperties web;
	private JwtProperties jwt;

	@Data
	public static class HttpSecurityProperties {
		private String[] allowPages;
		private String loginPage;
	}

	@Data
	public static class WebSecurityProperties {
		private String[] ignorePages;
	}
	@Data
	public static class JwtProperties {
		private String secret;
		private int expiration;
		private String headerKey;

		private String[] excludePathPatterns;
	}

}
