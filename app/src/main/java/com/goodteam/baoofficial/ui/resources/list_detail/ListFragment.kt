package com.goodteam.baoofficial.ui.resources.list_detail

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.goodteam.baoofficial.ArticleAdapter
import com.goodteam.baoofficial.R
import com.goodteam.baoofficial.databinding.FragmentListBinding
import com.google.android.material.snackbar.Snackbar
import com.prof.rssparser.Article
import com.prof.rssparser.Parser


class ListFragment : Fragment() {
    private lateinit var adapter: ArticleAdapter
    private lateinit var parser: Parser
    private lateinit var url: String


    private var _binding: FragmentListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val listViewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString("url").toString()
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "List"
        //back button
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        parser = Parser.Builder()
            .cacheExpirationMillis(24L * 60L * 60L * 100L) // one day
            .build()

        binding.recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.setHasFixedSize(true)

        listViewModel.rssChannel.observe(viewLifecycleOwner) { channel ->
            if (channel != null) {
                adapter = ArticleAdapter(channel.articles as MutableList<Article>)
                binding.recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
                binding.swipeLayout.isRefreshing = false
            }

        }

        listViewModel.snackbar.observe(viewLifecycleOwner) { value ->
            value?.let {
                Snackbar.make(binding.rootLayout, value, Snackbar.LENGTH_LONG).show()
                listViewModel.onSnackbarShowed()
            }
        }

        binding.swipeLayout.setColorSchemeResources(R.color.teal_700, R.color.purple_200)
        binding.swipeLayout.canChildScrollUp()
        binding.swipeLayout.setOnRefreshListener {
            adapter.clearArticles()
            binding.swipeLayout.isRefreshing = true
            listViewModel.fetchFeed(parser, url)
        }

        if (!isOnline(requireContext())) {
            val builder = AlertDialog.Builder(this.requireContext())
            builder.setMessage(R.string.alert_message)
                .setTitle(R.string.alert_title)
                .setCancelable(false)
                .setPositiveButton(
                    R.string.alert_positive
                ) { _, _ -> onDestroyView() }

            val alert = builder.create()
            alert.show()
        } else if (isOnline(requireContext()))
            listViewModel.fetchFeed(parser, url)


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        val search: MenuItem? = menu.findItem(R.id.nav_search)
        val searchView: SearchView? = search?.actionView as SearchView?
        searchView?.queryHint = "Search article"

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapter.searchArticles(newText)
                }


                return false
            }
        })
        return super.onCreateOptionsMenu(menu, inflater)
    }


    @Suppress("DEPRECATION")
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}