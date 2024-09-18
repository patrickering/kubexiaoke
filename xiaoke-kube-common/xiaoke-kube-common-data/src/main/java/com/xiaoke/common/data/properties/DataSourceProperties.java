

package com.xiaoke.common.data.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * minio 配置信息
 *
 * @author  xiaoke
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {
	private String url;
	private String username;
	private String password;
	private String driverClassName;
	private String type;
	private Integer initialSize;
	private Integer minIdle;
	private Integer maxActive;
	private Integer maxWait;
	private Integer timeBetweenEvictionRunsMillis;
	private Integer minEvictableIdleTimeMillis;
	private String validationQuery;
	private Boolean testWhileIdle;
	private Boolean testOnBorrow;
	private Boolean testOnReturn;
	private Boolean poolPreparedStatements;
	private Integer maxPoolPreparedStatementPerConnectionSize;
	private String filters;

}
