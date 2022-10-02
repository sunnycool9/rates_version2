package com.demo1.rates1.web;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.demo1.rates1.dao.CurrencyCodes;
import com.demo1.rates1.dao.CurrencyCodesRepository;
import com.demo1.rates1.dto.CurrencyRatesDto;

@Controller
public class CurrencyRatesViewController {

	private static final Logger logger1 = LoggerFactory.getLogger(CurrencyRatesViewController.class);
	private static final String DEFAULT_CURRENCY_RATES = "EUR,CAD";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private CurrencyCodesRepository currencyCodesRepository;

	@Value("${currency.rates.apikey}")
	private String currencyRatesApikey;

	@GetMapping(value = "/")
	public final ModelAndView showDefaultPage(
			@CurrentSecurityContext(expression = "authentication.name") String loggedInUsername,
			HttpServletRequest httprequest) {
		ModelAndView modelAndView = new ModelAndView("index");
		getCurencyRatesInfo(DEFAULT_CURRENCY_RATES, modelAndView);
		return modelAndView;
	}

	@PostMapping(value = "/index")
	public final ModelAndView showDefaultPage_WithWeatherUpdates(
			@RequestParam("currencycodes") String currencyCodes,
			@CurrentSecurityContext(expression = "authentication.name") String loggedInUsername,
			HttpServletRequest httprequest) 
	{
		
		// Check if input only has alphabets and ,
		 boolean isValidInput = ((currencyCodes != null) && (!currencyCodes.equals(""))
	                && (currencyCodes.matches("^[a-zA-Z,]*$")));
		
		 if (!isValidInput) {
			 throw new RestClientException("Invalid Input");
		 }
		 
		 ModelAndView modelAndView = new ModelAndView("index");
		 logger1.info("Got Currency codes: " + currencyCodes);
		 getCurencyRatesInfo(currencyCodes, modelAndView);
		 return modelAndView;
	}

	@GetMapping(value = "/login")
	public String showLoginPage() {
		return "login";
	}

	// This is a secure page
	@GetMapping(value = "/home")
	public ModelAndView showHomePage(
			@CurrentSecurityContext(expression = "authentication.name") String loggedInUsername,
			HttpServletRequest httprequest) {
		ModelAndView modelAndView = new ModelAndView("index");
		getCurencyRatesInfo(DEFAULT_CURRENCY_RATES, modelAndView);
		return modelAndView;
	}

	/**
	 * Get weather details using open weather api
	 * 
	 * @param zipCode
	 */
	private final void getCurencyRatesInfo(String currencyCodesInputStr, ModelAndView modelAndView) throws RestClientException 
	{
		LocalDateTime curLocalDateTime = LocalDateTime.now();
		CurrencyCodes currencyCodes = null;
		String responseStr = null;

		// Check if the Currency Codes already exist for this input
		Optional<CurrencyCodes> currencyCodes_optional = currencyCodesRepository
				.findByInputStrAndExpiryTimeGreaterThan(currencyCodesInputStr, curLocalDateTime);

		if (currencyCodes_optional.isEmpty()) 
		{
			try {
				
				// Step 1 : make request to Geo Code API
				String requestURL = new StringBuffer("https://exchange-rates.abstractapi.com/v1/live/?"
						+ "api_key='+currencyRatesApikey+'&base=USD&target=")
						.append(currencyCodesInputStr).toString();
						
				ResponseEntity<CurrencyRatesDto> currencyRatesResponseEntity = restTemplate.exchange(requestURL,
						HttpMethod.GET, null, CurrencyRatesDto.class);

				CurrencyRatesDto currencyRates = currencyRatesResponseEntity.getBody();
				logger1.info(currencyRates.toString());
				
				
				Map<String, String> exchange_rates = currencyRates.getExchange_rates();
				
				if (exchange_rates != null) 
				{
					StringBuilder sbuf = new StringBuilder();
				
					// Iterating the HashMap and create response String
				     for (Map.Entry<String, String> set : exchange_rates.entrySet()) 
				     {			 
				    	 sbuf.append("<h3 class=\"text-primary\">").append(set.getKey()).append(" = ").append(set.getValue()).append("</h3>");;
				     }
				     
				     responseStr = sbuf.toString();			     
				}
				else {
					throw new RestClientException("No Currency Rates found");
				}

				// Delete Existing Zipcode rows if present
				currencyCodesRepository.delete_existing_currency_rows(currencyCodesInputStr);
				
				// Store new data into table
				currencyCodes = currencyCodesRepository.save(new CurrencyCodes(
						curLocalDateTime, // createTime,
						curLocalDateTime.plusMinutes(30), // expiryTime -- Active for 30 minutes from create time,
						currencyCodesInputStr,
						responseStr
				));
				
			} catch (RestClientException e) 
			{
				logger1.error("Currency Rates Service not available", e);
				throw new RestClientException("Error retreiving the Currency Rates data" + e.getMessage());
			}

		}

		else {
			logger1.info("currencyCodes record found in cache");
			currencyCodes = currencyCodes_optional.get();
			responseStr = currencyCodes.getResponseStr(); 
		} 

		modelAndView.addObject("currency_rates", responseStr);	
			
	}

}
