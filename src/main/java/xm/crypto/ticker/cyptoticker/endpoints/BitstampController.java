package xm.crypto.ticker.cyptoticker.endpoints;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xm.crypto.ticker.cyptoticker.database.DatabaseController;

@RestController
public class BitstampController {
	
	@Autowired
	private DatabaseController databaseController;


	@GetMapping("/tickerBitStamp")
	public ResponseEntity<Map<String, Object>> getTicker() throws IOException, ClassNotFoundException, SQLException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url("https://www.bitstamp.net/api/v2/ticker/btcusd/")
				.method("GET", null).build();
		Response response = client.newCall(request).execute();
		String responseData = response.body().string();
		JSONObject jsonObject = new JSONObject(responseData);
		String timestamp = jsonObject.getString("timestamp");
		double lastPrice = jsonObject.getDouble("last");
		double formattedLastPrice = Double.parseDouble(String.format("%.2f", lastPrice));

		databaseController.insert(formattedLastPrice, timestamp);
		
		Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("price", formattedLastPrice);
        responseMap.put("timestamp", timestamp);
		
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(responseMap, headers, HttpStatus.OK);

	}
}
