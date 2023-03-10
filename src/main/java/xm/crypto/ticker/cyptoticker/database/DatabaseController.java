package xm.crypto.ticker.cyptoticker.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

@Component
public class DatabaseController {

	private Connection connection;

	public DatabaseController() throws SQLException, ClassNotFoundException {
		if (connection == null) {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:xmCrypto2.db");
			createTableIfNotExists();
		}
	}

	private void createTableIfNotExists() throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement("CREATE TABLE IF NOT EXISTS ticker (lastPrice NUMERIC, timestamp TEXT)");
		statement.execute();
	}

	public void insert(double price, String timestamp) throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement("INSERT INTO ticker (lastPrice, timestamp) VALUES (?, ?)");
		statement.setDouble(1, price);
		statement.setString(2, timestamp);
		statement.executeUpdate();
	}

	public Map<String, Object> selectLastItem() throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement("SELECT * FROM ticker ORDER BY timestamp DESC LIMIT 1");
		ResultSet resultSet = statement.executeQuery();

		double price = 0.0;
		long savedTimestamp = 0;
		while (resultSet.next()) {
			price = resultSet.getDouble("lastPrice");
			savedTimestamp = resultSet.getLong("timestamp");
		}

		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("price", price);
		responseMap.put("timestamp", savedTimestamp);
		return responseMap;
	}

	public Map<String, Object> selectAll() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM ticker ORDER BY timestamp DESC");
		ResultSet resultSet = statement.executeQuery();
		Map<String, Object> responseMap = new HashMap<>();
		while (resultSet.next()) {
			responseMap.put(resultSet.getString("timestamp"),resultSet.getDouble("lastPrice"));
		}
		
		return responseMap;
	}

	@PreDestroy
	public void close() throws SQLException {
		connection.close();
	}
}