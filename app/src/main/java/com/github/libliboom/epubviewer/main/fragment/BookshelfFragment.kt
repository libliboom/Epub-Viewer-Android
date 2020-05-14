package com.github.libliboom.epubviewer.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.base.BaseFragment
import com.github.libliboom.epubviewer.main.recycler.adapter.BookListAdapter
import com.github.libliboom.epubviewer.main.viewmodel.BookshelfViewModel
import com.github.libliboom.epubviewer.reader.activity.EPubReaderActivity
import javax.inject.Inject

class BookshelfFragment : BaseFragment() {

    @Inject
    lateinit var mAdapter: BookListAdapter

    @Inject
    lateinit var mRequestManager: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = RecyclerView(requireContext())

    override fun getLayoutId(): Int {
        return R.layout.fragment_bookshelf
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initRecyclerView(view)
    }

    private fun initAdapter() {
        val viewModel = ViewModelProvider(requireActivity(), factory).get(BookshelfViewModel::class.java)
        mAdapter.updateViewModel(viewModel, mRequestManager)

        // FIXME: 2020/05/14 issue of event click that loads duplicated activity
        mAdapter.getPublishSubject().subscribe {
            requireContext().startActivity(EPubReaderActivity.newIntent(requireActivity(), viewModel.ePubFiles[it]))
        }
    }

    private fun initRecyclerView(view: View) {
        view as RecyclerView
        view.apply {
            layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
            this.adapter = mAdapter
        }
    }

    companion object {
        fun newInstance() = BookshelfFragment()
        private const val SPAN_COUNT = 3
    }
}