package com.goodteam.baoofficial.ui.resources

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.goodteam.baoofficial.ArticleAdapter
import com.goodteam.baoofficial.R
import com.goodteam.baoofficial.databinding.FragmentResourcesBinding
import com.goodteam.baoofficial.ui.resources.list.ListViewModel
import com.goodteam.baoofficial.util.AlertDialogHelper
import com.google.android.material.snackbar.Snackbar
import com.prof.rssparser.Parser


class ResourcesFragment : Fragment() {
    private lateinit var adapter: ArticleAdapter
    private lateinit var parser: Parser


    private var _binding: FragmentResourcesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val listViewModel: ListViewModel by viewModels()


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentResourcesBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parser = Parser.Builder()
            .cacheExpirationMillis(24L * 60L * 60L * 100L) // one day
            .build()

        binding.recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.setHasFixedSize(true)

        listViewModel.rssChannel.observe(viewLifecycleOwner) { channel ->
            if (channel != null) {
                adapter = ArticleAdapter(channel.articles)
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
            listViewModel.fetchFeed(parser)
        }

        if (!isOnline(requireContext())) {
            val builder = AlertDialog.Builder(this.requireContext())
            builder.setMessage(R.string.alert_message)
                .setTitle(R.string.alert_title)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_positive
                ) { _, _ -> onDestroyView() }

            val alert = builder.create()
            alert.show()
        } else if (isOnline(requireContext()))
        listViewModel.fetchFeed(parser)



    }

        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_raw_parser) {
            AlertDialogHelper(
                title = R.string.alert_raw_parser_title,
                positiveButton = R.string.alert_raw_parser_positive,
                negativeButton = R.string.alert_raw_parser_negative
            ).buildInputDialog(requireContext()) { url ->
                listViewModel.fetchForUrlAndParseRawData(url)
            }.show()
        }

        return super.onOptionsItemSelected(item)
    }

    @Suppress("DEPRECATION")
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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