package com.opsc.currencyconverterapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.opsc.currencyconverterapp.databinding.ActivityMainBinding
import com.opsc.currencyconverterapp.model.CurrencyResponse
import com.opsc.currencyconverterapp.network.CurrencyApiService
import com.opsc.currencyconverterapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val API_KEY = "e195c00c4d872b389b05a322ae42f42da62de858" //--- Shown for Varsity Purposes
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.convertButton.setOnClickListener {
            convertCurrency()
        }
    }

    private fun convertCurrency() {
        val from = binding.fromCurrency.text.toString()
        val to = binding.toCurrency.text.toString()
        val amt = binding.amount.text.toString()

        if (from.isEmpty() || to.isEmpty() || amt.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val retrofit = RetrofitClient.getRetrofitInstance()
        val apiService = retrofit.create(CurrencyApiService::class.java)

        val call = apiService.getExchangeRate(API_KEY, from, to, amt)
        call.enqueue(object : Callback<CurrencyResponse> {
            override fun onResponse(call: Call<CurrencyResponse>, response: Response<CurrencyResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val currencyResponse = response.body()!!
                    val rateForAmount = currencyResponse.rates[to]?.rateForAmount
                    binding.result.text = "Converted Amount: $rateForAmount"
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "API Response Failure: $errorBody")
                    Toast.makeText(this@MainActivity, "Failed to get conversion rate", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                Log.e(TAG, "API Call Failed: ${t.message}", t)
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
