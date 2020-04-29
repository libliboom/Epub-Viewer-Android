package com.github.libliboom.epubviewer.main.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.main.activity.EpubReaderActivity
import kotlinx.android.synthetic.main.item_book.view.image_view_book

class BookListAdapter() : RecyclerView.Adapter<BookListAdapter.BookViewHolder>() {

    private lateinit var mContext: Context

    constructor(context: Context) : this() {
        mContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).run {
            inflate(R.layout.item_book, parent, false)
        }

        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.onBindItem(position)
    }

    override fun getItemCount() = LIST_COUNT

    inner class BookViewHolder(view: View) : RViewHolder(view) {

        private var bookIv: ImageView = view.image_view_book

        override fun onBindItem(position: Int) {
            bookIv.apply {
                setImageResource(getImageResId(position))
                setOnClickListener {
                    mContext.startActivity(Intent(mContext, EpubReaderActivity::class.java))
                    Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getImageResId(position: Int): Int {
            return when (position) {
                0 -> R.drawable.ic_book_black
                1 -> R.drawable.ic_book_red
                2 -> R.drawable.ic_book_purple
                3 -> R.drawable.ic_book_yellow
                else -> R.drawable.ic_book_black
            }
        }
    }

    companion object {
        private const val LIST_COUNT = 4
    }
}
