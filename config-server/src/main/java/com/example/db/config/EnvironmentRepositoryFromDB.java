package com.example.db.config;

import com.insnergy.config.db.persistence.entity.ConfigTable;
import com.insnergy.config.db.persistence.repository.ConfigTableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.cloud.config.server.environment.SearchPathLocator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ConfigurationProperties("spring.cloud.config.server.db")
public class EnvironmentRepositoryFromDB implements EnvironmentRepository, SearchPathLocator {

	private static Logger logger = LoggerFactory.getLogger(EnvironmentRepositoryFromDB.class);

	private ConfigTableRepository configTableRepository;

	private List<PropertyTable> applications = new ArrayList<>();

	public static class PropertyTable {
		private String application;
		private String table = "table";
		private String profile = "profile";
		private String property = "property";
		private String value = "value";

		public String getApplication() {
			return application;
		}

		public void setApplication(String application) {
			this.application = application;
		}

		public String getTable() {
			return table;
		}

		public void setTable(String table) {
			this.table = table;
		}

		public String getProfile() {
			return profile;
		}

		public void setProfile(String profile) {
			this.profile = profile;
		}

		public String getProperty() {
			return property;
		}

		public void setProperty(String property) {
			this.property = property;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	public List<PropertyTable> getApplications() {
		return applications;
	}

	public void setApplications(List<PropertyTable> applications) {
		this.applications = applications;
	}

	public ConfigTableRepository getConfigTableRepository() {
		return configTableRepository;
	}

	public void setConfigTableRepository(ConfigTableRepository configTableRepository) {
		this.configTableRepository = configTableRepository;
	}

	@Override
	public Locations getLocations(String application, String profile, String label) {
		return new Locations(application, profile, label, null, new String[]{getDataSourceUrl()});
	}

	@Override
	public Environment findOne(String application, String profile, String label) {

		Environment environment = new Environment(getDataSourceUrl(), new String[]{profile}, label, null, null);

		PropertyTable propertyTable = getPropertyTable(application);
		if (propertyTable != null) {
			environment.add(new PropertySource(
					getResourceName(application, profile), getProperties(propertyTable, profile))
			);
		}
		return environment;
	}

	private PropertyTable getPropertyTable(String application) {
		return getApplications().stream()
				.filter(table -> table.getApplication().equalsIgnoreCase(application))
				.findFirst()
				.orElse(new PropertyTable());
	}

	private String getDataSourceUrl() {
		try {
			return configTableRepository.getJdbcTemplate().getDataSource().getConnection().getMetaData().getURL();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private String getResourceName(String application, String profile) {
		return application + "-" + profile;
	}
	
	private Map<String, String> getProperties(PropertyTable propertyTable, String profile) {
		String tableName = propertyTable.getTable();
		String colProperty = propertyTable.getProperty();
		String colValue = propertyTable.getValue();
		String colProfile = propertyTable.getProfile();
		try {
			List<ConfigTable> configTables =
					configTableRepository.findByProfile(profile, tableName, colProperty, colValue, colProfile);
			return configTables.stream().collect(Collectors.toMap(ConfigTable::getProperty, ConfigTable::getValue));
		} catch(Exception e) {
			logger.error("Error getting properties", e);
		}
		return new HashMap<>();
	}

}