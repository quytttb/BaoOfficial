package com.goodteam.baoofficial.ui.home

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prof.rssparser.Channel
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Hello World!"
    }
    val text: LiveData<String> = _text


/*
    fun requestWeather(cityNameText: TextView, tempText: TextView, minmaxTempText: TextView) {
        viewModelScope.launch {
            try {
                val url =
                    "https://api.openweathermap.org/data/2.5/weather?q=hanoi&units=metric&appid=5e6a88f6f31f73becb01faf755644081"
                val resultJson = URL(url).readText()
                Log.d("Weather Report", resultJson)
                val jsonObj = JSONObject(resultJson)
                val cityName = jsonObj.getString("name")
                val main = jsonObj.getJSONObject("main")
                val temp = main.getString("temp") + "°C"
                val minmaxTemp =
                    main.getString("temp_min") + "°C/" + main.getString("temp_max") + "°C"
                cityNameText.text = cityName
                tempText.text = temp
                minmaxTempText.text = minmaxTemp
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }
*/
}
