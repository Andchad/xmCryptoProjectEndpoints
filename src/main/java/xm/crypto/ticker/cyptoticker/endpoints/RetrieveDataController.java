package xm.crypto.ticker.cyptoticker.endpoints;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import xm.crypto.ticker.cyptoticker.database.DatabaseController;

@RestController
public class RetrieveDataController {
	
	@Autowired
	private DatabaseController databaseController;
	
	@GetMapping("/getData")
	public ResponseEntity<Map<String, Object>> getTicker() throws IOException, ClassNotFoundException, SQLException {
		
        Map<String, Object> responseMap = databaseController.selectAll();
      
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(responseMap, headers, HttpStatus.OK);

	}
}
