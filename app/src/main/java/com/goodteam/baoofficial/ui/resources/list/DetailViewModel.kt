package com.goodteam.baoofficial.ui.resources.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prof.rssparser.Parser
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class DetailViewModel  : ViewModel() {
     var result=""

    fun getString(content: String, link: String): String {
        viewModelScope.launch {
            try {
                val doc: Document = Jsoup.connect(link).get()
                result = doc.getElementsByClass("container container--narrow").text();
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }


}

