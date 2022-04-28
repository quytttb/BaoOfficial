package com.goodteam.baoofficial.ui.resources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.goodteam.baoofficial.ArticleAdapter
import com.goodteam.baoofficial.databinding.FragmentResourcesBinding
import com.prof.rssparser.Parser


class ResourcesFragment : Fragment() {
    private var _binding: FragmentResourcesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var url: String
    private val urlResources = listOf(
        "https://vatvostudio.vn/feed/",
        "https://genk.vn/rss/home.rss",
        "https://vnexpress.net/rss/tin-moi-nhat.rss",
        "https://tuoitre.vn/rss/tin-moi-nhat.rss",
        "https://www.androidauthority.com/feed",
        "https://rss.nytimes.com/services/xml/rss/nyt/World.xml"
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentResourcesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Resources"

        binding.ivVatVo.setOnClickListener {
            selectResource(0)
        }
        binding.ivGenk.setOnClickListener {
            selectResource(1)
        }
        binding.ivVNExpress.setOnClickListener {
            selectResource(2)
        }
        binding.ivTuoiTre.setOnClickListener {
            selectResource(3)
        }
        binding.ivAndroidAuthority.setOnClickListener {
            selectResource(4)
        }
        binding.ivNewYorkTimes.setOnClickListener {
            selectResource(5)
        }
    }

    private fun selectResource(index: Int) {
        url = urlResources[index]
        this.findNavController()
            .navigate(ResourcesFragmentDirections.actionResourcesFragmentToListFragment(url))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}