package com.aias.springboot.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <b>
 *
 * <br>
 * <b>@ClassName:</b> JwtProperties <br>
 * <b>@Date:</b> 2019/10/30  <br>
 *
 * @author <a> liuhy </a><br>
 */
@Data
@ConfigurationProperties(prefix = JwtProperties.JWT_PROPERTIES_PREFIX)
public class JwtProperties {
   static final String JWT_PROPERTIES_PREFIX = "jwt";
   private String secret;
   private int expiration;
   private String headerKey;

   private String[] excludePathPatterns;
}
