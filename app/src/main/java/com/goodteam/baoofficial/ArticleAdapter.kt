package com.goodteam.baoofficial

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goodteam.baoofficial.ui.resources.list_detail.ListFragmentDirections
import com.prof.rssparser.Article
import kotlinx.coroutines.NonDisposableHandle.parent
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter(private var articles: MutableList<Article>) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false))

    override fun getItemCount() = articles.size

    private val countClicks = mutableListOf<Int>()

    //init countClicks
    init {
        for (i in 0 until articles.size) {
            countClicks.add(0)
        }
    }



    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articles[position])

        //set onLongClickListener
        holder.itemView.setOnLongClickListener {
            //add to favourite
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Add to favourite?")
            builder.setMessage("Are you sure you want to delete this article?")
            builder.setPositiveButton("Yes") { _, _ ->
                //visible favourite icon
                holder.favouriteIcon.visibility = View.VISIBLE

                notifyDataSetChanged()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                //cancel the dialog
                dialog.dismiss()

            }
            builder.show()
            true
        }

        //set onClickListener
        val content = articles[position].content.toString()
        val link = articles[position].link.toString()

        //set onClickListener
        holder.itemView.setOnClickListener {
            //count the number of times read article
            //if the article is clicked, add 1 countClicks
            //countClicks[position]=countClicks[position]+1
            //toast the number of times read article

/*
            Toast.makeText(
                holder.itemView.context,
                "You have read this article ${countClicks[position]} times",
                Toast.LENGTH_SHORT
            ).show()
*/

            //open article in detail fragment
            holder.itemView.findNavController().navigate(
                ListFragmentDirections.actionListFragmentToDetailFragment(
                    content,
                    link
                )
            )
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearArticles() {
        articles = mutableListOf()
        notifyDataSetChanged()
    }

    //searching for articles
    @SuppressLint("NotifyDataSetChanged")
    fun searchArticles(query: String) {
        val newArticles = mutableListOf<Article>()
        val oldArticle = articles
        for (article in articles) {
            if (article.title?.lowercase(Locale.getDefault())!!.contains(query.lowercase(Locale.getDefault()))) {
                newArticles.add(article)
            }
        }
        articles = newArticles

        notifyDataSetChanged()
        //if the query is empty, show all articles
/*
        if (query.isEmpty()) {
            articles = oldArticle
            notifyDataSetChanged()
        }
*/
    }

    //sort articles by alphabet
    @SuppressLint("NotifyDataSetChanged")
    fun sortArticles() {
        articles.sortBy { it.title }
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title = itemView.findViewById<TextView>(R.id.title)
        private val pubDate = itemView.findViewById<TextView>(R.id.pubDate)
        private val categoriesText = itemView.findViewById<TextView>(R.id.categories)
        private val image = itemView.findViewById<ImageView>(R.id.image)
        val favouriteIcon = itemView.findViewById<ImageView>(R.id.imgFav)

                @SuppressLint("SetJavaScriptEnabled")
        fun bind(article: Article) {

            var pubDateString = article.pubDate

            try {
                val sourceDateString = article.pubDate
                val sourceSdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
                if (sourceDateString != null) {
                    val date = sourceSdf.parse(sourceDateString)
                    if (date != null) {
                        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                        pubDateString = sdf.format(date)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            title.text = article.title

            Glide.with(itemView).load(article.image).centerCrop()
                .placeholder(R.drawable.placeholder).into(image)

            pubDate.text = pubDateString

            var categories = ""
            for (i in 0 until article.categories.size) {
                categories = if (i == article.categories.size - 1) {
                    categories + article.categories[i]
                } else {
                    categories + article.categories[i] + ", "
                }
            }

            categoriesText.text = categories
        }
    }
}


