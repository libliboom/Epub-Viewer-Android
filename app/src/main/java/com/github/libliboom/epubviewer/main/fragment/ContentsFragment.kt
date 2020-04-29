package com.github.libliboom.epubviewer.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.main.adapter.ContentsAdapter
import kotlinx.android.synthetic.main.fragment_contents.rv_contents

class ContentsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contents, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = rv_contents
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ContentsAdapter()
        }
    }

    companion object {
        fun newInstance() = ContentsFragment()
    }
}