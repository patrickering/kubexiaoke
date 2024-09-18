

package com.xiaoke.common.sequence;


import com.xiaoke.common.sequence.builder.SnowflakeSeqBuilder;
import com.xiaoke.common.sequence.properties.SequenceSnowflakeProperties;
import com.xiaoke.common.sequence.sequence.Sequence;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaoke
 * @date 2019-05-26
 */
@Configuration
@ComponentScan("com.xiaoke.common.sequence")
@ConditionalOnMissingBean(Sequence.class)
public class SequenceAutoConfiguration {

	/**
	 * snowflak 算法作为发号器实现
	 *
	 * @param properties
	 * @return
	 */
	@Bean
	@ConditionalOnBean(SequenceSnowflakeProperties.class)
	public Sequence snowflakeSequence(SequenceSnowflakeProperties properties) {
		return SnowflakeSeqBuilder
				.create()
				.datacenterId(properties.getDatacenterId())
				.workerId(properties.getWorkerId())
				.build();
	}
}