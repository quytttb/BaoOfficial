package com.goodteam.baoofficial.ui.resources.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.*
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.goodteam.baoofficial.R
import com.goodteam.baoofficial.databinding.FragmentDetailBinding
import java.util.*


class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var content: String
    private lateinit var mTTS: TextToSpeech


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            content = it.getString("content").toString()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "CHI TIẾT"




        binding.webview.loadDataWithBaseURL(
            null,
            "<style>img{display: inline; height: auto; max-width: 100%;}</style>\n" +
                    "<style>iframe{ max-height: 100%; max-width: 100%;}</style>\n" +
                    content,
            null,
            "utf-8",
            null)
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.webViewClient = WebViewClient()

        mTTS = TextToSpeech(requireContext()) { i ->
            Toast.makeText(requireContext(), R.string.text_to_speech + i, Toast.LENGTH_SHORT).show()
            if (i == TextToSpeech.SUCCESS) {
                mTTS.language = Locale.forLanguageTag("vi-VN")
            }
        }

/*                val articleView = WebView(itemView.context)
                articleView.settings.loadWithOverviewMode = true
                articleView.settings.javaScriptEnabled = true
                articleView.isHorizontalScrollBarEnabled = false
                articleView.webChromeClient = WebChromeClient()
                articleView.loadDataWithBaseURL(
                    null,
                    "<style>img{display: inline; height: auto; max-width: 100%;} " +

                            "</style>\n" + "<style>iframe{ height: auto; width: auto;}" + "</style>\n" + article.content,
                    null,
                    "utf-8",
                    null
                )

                val alertDialog =
                    androidx.appcompat.app.AlertDialog.Builder(itemView.context).create()
                alertDialog.setTitle(article.title)
                alertDialog.setView(articleView)
                alertDialog.setButton(
                    androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK"
                ) { dialog, _ -> dialog.dismiss() }
                alertDialog.show()

                (alertDialog.findViewById<View>(android.R.id.message) as TextView).movementMethod =
                    LinkMovementMethod.getInstance()
*/
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.text_to_speech, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_text_to_speech) {
            textToSpeech()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun textToSpeech() {
        mTTS.speak(content, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mTTS.stop()
        mTTS.shutdown()
        _binding = null
    }

}