package br.com.its.latlong.rest;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.its.latlong.model.Address;
import br.com.its.latlong.model.AddressRequest;
import br.com.its.latlong.model.GeocodeResult;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

@RestController
public class GeocodeController {

	@RequestMapping(path = "/geocode", method = RequestMethod.GET)
	public GeocodeResult getGeocode(@RequestParam String address) throws IOException {
		return this.callGoolgleAddress(address);
	}

	@PostMapping(path = "/geocodes", produces = "application/json", consumes = "application/json")
	public ResponseEntity<?> address(@Valid @RequestBody AddressRequest addressRequest) throws IOException {
		AddressRequest news = new AddressRequest();
		List<Address> address_news = new ArrayList<Address>();
 		GeocodeResult result = null;
		for (Address address: addressRequest.getAdresses()) {
			String add = address.getNumber()+" "+address.getStreet()+" "+address.getCity()+
					" "+address.getProvince()+" "+address.getCountry();
			result = this.callGoolgleAddress(add);
			address.setLatitude(result.getResults().get(0).getGeometry().getGeocodeLocation().getLatitude());
			address.setLongitude(result.getResults().get(0).getGeometry().getGeocodeLocation().getLongitude());
			address_news.add(address);
		}
		news.setAdresses(address_news);
		return ResponseEntity.ok(news);
	}
	
	private GeocodeResult callGoolgleAddress(String address) throws IOException{
		OkHttpClient client = new OkHttpClient();
		String encodedAddress = URLEncoder.encode(address, "UTF-8");
		Request request = new Request.Builder().url("https://maps.googleapis.com/maps/api/geocode/json?address="
				+ encodedAddress + "").get().build();
		ResponseBody responseBody = client.newCall(request).execute().body();
		ObjectMapper objectMapper = new ObjectMapper();
		GeocodeResult result = objectMapper.readValue(responseBody.string(), GeocodeResult.class);
		return result;
	}
	
}