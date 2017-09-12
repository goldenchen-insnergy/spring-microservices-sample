package com.example.db.persistence.repository;

import com.insnergy.config.db.persistence.entity.ConfigTable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.SQLException;
import java.util.List;

public class ConfigTableRepository {

	private JdbcTemplate jdbcTemplate;

	private RowMapper<ConfigTable> rowMapper = new BeanPropertyRowMapper<>(ConfigTable.class);

	public ConfigTableRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

    public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<ConfigTable> findByProfile(String profile,
										   String tableName,
										   String colProperty,
										   String colValue,
										   String colProfile) throws SQLException {
		// 自 PropertyTable 取得欄位名稱
		String query = new StringBuilder()
				.append("SELECT ").append(colProperty).append(",").append(colValue)
				.append(" FROM \"").append(tableName).append("\"")
				.append(" WHERE ").append(colProfile).append("='").append(profile).append("'")
				.toString();
		return jdbcTemplate.query(query, rowMapper);
	}
}