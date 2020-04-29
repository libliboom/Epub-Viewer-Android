package com.github.libliboom.epubviewer.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.libliboom.epubviewer.main.adapter.BookListAdapter

class BookshelfFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = RecyclerView(requireContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view as RecyclerView
        view.apply {
            layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
            adapter = BookListAdapter(context)
        }
    }

    companion object {
        fun newInstance() = BookshelfFragment()
        private const val SPAN_COUNT = 3
    }
}