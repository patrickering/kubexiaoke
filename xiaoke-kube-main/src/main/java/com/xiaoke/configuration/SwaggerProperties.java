
package com.xiaoke.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * SwaggerProperties
 *
 * @author: xiaoke
 * @date: 2018/7/25  14:00
 */
@Data
@Component
@ConfigurationProperties("swagger")
public class SwaggerProperties {
	/**
	 * 是否开启swagger
	 */
	private Boolean enabled;
	/**
	 * swagger会解析的包路径
	 **/
	private String basePackage = "";
	/**
	 * swagger会解析的url规则
	 **/
	private List<String> basePath = new ArrayList<>();
	/**
	 * 在basePath基础上需要排除的url规则
	 **/
	private List<String> excludePath = new ArrayList<>();
	/**
	 * 标题
	 **/
	private String title = "";
	/**
	 * 描述
	 **/
	private String description = "";
	/**
	 * 版本
	 **/
	private String version = "";
	/**
	 * 许可证
	 **/
	private String license = "";
	/**
	 * 许可证URL
	 **/
	private String licenseUrl = "";
	/**
	 * 服务条款URL
	 **/
	private String termsOfServiceUrl = "";

	/**
	 * host信息
	 **/
	private String host = "";
	/**
	 * 联系人信息
	 */
	private Contact contact = new Contact();

	@Data
	@NoArgsConstructor
	public static class Contact {

		/**
		 * 联系人
		 **/
		private String name = "";
		/**
		 * 联系人url
		 **/
		private String url = "";
		/**
		 * 联系人email
		 **/
		private String email = "";

	}
}
