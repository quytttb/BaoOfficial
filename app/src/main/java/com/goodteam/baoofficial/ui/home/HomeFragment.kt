package com.goodteam.baoofficial.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.goodteam.baoofficial.R
import com.goodteam.baoofficial.databinding.FragmentHomeBinding
import org.json.JSONObject
import java.net.URL

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val cityNameText: TextView = binding.txtName

        val tempText: TextView = binding.txtTemperature

        val minmaxTempText: TextView = binding.txtMinMax

        val imgIcon: ImageView = binding.imgIcon
        //coroutines to get weather
        val weatherTask = Runnable {
            try {
                val url =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=hanoi&units=metric&appid=5e6a88f6f31f73becb01faf755644081")
                val json = JSONObject(url.readText())
                val main = json.getJSONObject("main")
                val temp = main.getString("temp") + "°C"
                val minmaxTemp =
                    main.getString("temp_min") + "°C/" + main.getString("temp_max") + "°C"
                val cityName = json.getString("name")
                //get icon from json
                val weather = json.getJSONArray("weather")
                val weatherObject = weather.getJSONObject(0)
                val icon = weatherObject.getString("icon")
                val iconUrl = "http://openweathermap.org/img/wn/$icon@2x.png"
                cityNameText.text = cityName
                tempText.text = temp
                minmaxTempText.text = minmaxTemp
                Glide.with(requireActivity()).load(iconUrl)
                    .placeholder(R.drawable.ic_baseline_rss_feed_24).into(imgIcon)


            } catch (e: Exception) {
                e.message?.let { Log.e("Error", it) }
            }
        }
        val weatherThread = Thread(weatherTask)
        weatherThread.start()


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //you can set the title for your toolbar here for different fragments different titles
        requireActivity().title = "Home"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}