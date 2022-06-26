package com.goodteam.baoofficial.ui.information.aboutUs

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.goodteam.baoofficial.R
import com.goodteam.baoofficial.databinding.FragmentAboutUsBinding
import com.goodteam.baoofficial.databinding.FragmentInformationBinding
import com.goodteam.baoofficial.ui.information.InformationViewModel
import java.util.*

class AboutUsFragment : Fragment() {
    private var _binding: FragmentAboutUsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val aboutUsViewModel =
            ViewModelProvider(this)[AboutUsViewModel::class.java]

        _binding = FragmentAboutUsBinding.inflate(inflater, container, false)

        val textView: TextView = binding.txtInformation
        aboutUsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Detail"
        //back button
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}