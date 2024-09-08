package com.opsc.currencyconverterapp.model

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("base_currency_code")
    val baseCurrencyCode: String,

    @SerializedName("base_currency_name")
    val baseCurrencyName: String,

    @SerializedName("amount")
    val amount: String,

    @SerializedName("updated_date")
    val updatedDate: String,

    @SerializedName("rates")
    val rates: Map<String, Rate>,

    @SerializedName("status")
    val status: String
)

data class Rate(
    @SerializedName("currency_name")
    val currencyName: String,

    @SerializedName("rate")
    val rate: String,

    @SerializedName("rate_for_amount")
    val rateForAmount: String
)
