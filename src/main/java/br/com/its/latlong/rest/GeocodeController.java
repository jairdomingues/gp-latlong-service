package br.com.its.latlong.rest;

import java.io.IOException;
import java.net.URLEncoder;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.order.SalesOrderRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.its.latlong.model.GeocodeResult;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

@RestController
public class GeocodeController {

	@RequestMapping(path = "/geocode", method = RequestMethod.GET)
	public GeocodeResult getGeocode(@RequestParam String address) throws IOException {
		OkHttpClient client = new OkHttpClient();
		String encodedAddress = URLEncoder.encode(address, "UTF-8");
		ResponseBody responseBody = client.newCall(request).execute().body();
		ObjectMapper objectMapper = new ObjectMapper();
		GeocodeResult result = objectMapper.readValue(responseBody.string(), GeocodeResult.class);
		return result;
	}
	
	@PostMapping(path = "/geocodes", produces = "application/json", consumes = "application/json")
	public ResponseEntity<?> address(@Valid @RequestBody Address address) {
		return ResponseEntity.ok(salesOrderService.createOrder(salesOrderRequest));
	}
	
}