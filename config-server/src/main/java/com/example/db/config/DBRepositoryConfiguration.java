package com.example.db.config;

import com.insnergy.config.db.persistence.repository.ConfigTableRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@Profile("db")
@ConditionalOnMissingBean(EnvironmentRepository.class)
public class DBRepositoryConfiguration {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}

	@Bean
	public EnvironmentRepository environmentRepository() {
		EnvironmentRepositoryFromDB dbEnvironmentRepository = new EnvironmentRepositoryFromDB();
		dbEnvironmentRepository.setConfigTableRepository(new ConfigTableRepository(jdbcTemplate()));
		return dbEnvironmentRepository;
	}

}