package com.demo1.rates1.dto;

import java.util.Map;

public class CurrencyRatesDto {
	private String base;
	private float last_updated;
	private Map<String, String> exchange_rates;

	public CurrencyRatesDto() {
		super();
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public float getLast_updated() {
		return last_updated;
	}

	public void setLast_updated(float last_updated) {
		this.last_updated = last_updated;
	}

	public Map<String, String> getExchange_rates() {
		return exchange_rates;
	}

	public void setExchange_rates(Map<String, String> exchange_rates) {
		this.exchange_rates = exchange_rates;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CurrencyRatesDto [base=").append(base).append(", last_updated=").append(last_updated)
				.append(", exchange_rates=").append(exchange_rates).append("]");
		return builder.toString();
	}
	
}
