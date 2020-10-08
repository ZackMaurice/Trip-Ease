package com.example.myapplication.util;

public enum Currency {
	//TODO: replace hard-coded values with DB calls
	USD(1f),
	CAD(1.33f),
	AUD(1.4f),
	JPY(105.97f),
	EUR(0.85f);

	final float conversionRate; //the rate by which USD is converted to this currency

	Currency(float conversionRate) {
		this.conversionRate = conversionRate;
	}

	public float convertTo(Currency newCurrency, float amount){
		return amount * getConversionRateTo(newCurrency);
	}

	public float getConversionRateTo(Currency newCurrency){
		return newCurrency.conversionRate/this.conversionRate;
	}
}
