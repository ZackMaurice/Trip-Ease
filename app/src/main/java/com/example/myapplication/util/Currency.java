package com.example.myapplication.util;

import java.text.DecimalFormat;

public enum Currency {
	//TODO: replace hard-coded values with DB calls
	USD(1f, 2, "$"),
	CAD(1.33f, 2, "$"),
	AUD(1.4f, 2, "$"),
	JPY(105.97f, 0, "¥"),
	EUR(0.85f, 2, "€");

	/** the rate by which USD is converted to this currency */
	final float conversionRate;
	/** the number of decimal placed to which this currency is normally displayed */
	final int precision;
	/** the symbol for this currency */
	final String symbol;

	Currency(float conversionRate, int precision, String symbol) {
		this.conversionRate = conversionRate;
		this.precision = precision;
		this.symbol = symbol;
	}

	/**
	 * Convert some amount in this currency to another currency.
	 * @param newCurrency the currency being converted into
	 * @param amount the amount being converted
	 * @return the converted amount
	 */
	public float convertTo(Currency newCurrency, float amount){
		return amount * getConversionRateTo(newCurrency);
	}

	/**
	 * @return the conversion rate from this currency to another currency.
	 */
	public float getConversionRateTo(Currency newCurrency){
		return newCurrency.conversionRate/this.conversionRate;
	}

	/**
	 * Format a some amount to a string representation of that amount.
	 * @param amount the amount to be formatted
	 * @param includeSymbol true if the symbol for this currency is to be included
	 * @return the formatted String
	 */
	public String format(float amount, boolean includeSymbol) {
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setGroupingUsed(false);
		decimalFormat.setMaximumFractionDigits(precision);
		String formattedAmount = decimalFormat.format(amount);

		if(includeSymbol)
			formattedAmount = symbol + formattedAmount;

		return formattedAmount;
	}
}
