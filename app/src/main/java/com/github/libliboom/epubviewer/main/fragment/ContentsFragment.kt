package com.github.libliboom.epubviewer.main.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.base.BaseFragment
import com.github.libliboom.epubviewer.dev.EPubFileStub.EXTRACTED_COVER_FILE_PATH_01
import com.github.libliboom.epubviewer.main.recycler.adapter.ContentsAdapter
import com.github.libliboom.epubviewer.util.file.StorageManager
import kotlinx.android.synthetic.main.fragment_contents.iv_contents
import kotlinx.android.synthetic.main.fragment_contents.rv_contents
import javax.inject.Inject

class ContentsFragment : BaseFragment() {

    @Inject
    lateinit var mAdapter: ContentsAdapter

    @Inject
    lateinit var mRequestManager: RequestManager

    override fun getLayoutId() = R.layout.fragment_contents

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCover()
        initAdapter()
        initRecyclerView()
    }

    private fun initCover() {
        arguments?.getString(ARGS_HREF)?.let {
            val absPath = getAbsolutePath(it)
            mRequestManager
                .load(absPath)
                .into(iv_contents)
        }
    }

    // TODO: 2020/05/13 fetch cover file path
    private fun getAbsolutePath(it: String) =
        StorageManager.getDir(requireContext()) + EXTRACTED_COVER_FILE_PATH_01

    private fun initAdapter() {
        arguments?.getString(ARGS_HREF)?.let {
            mAdapter.update(it)
        }
        arguments?.getStringArrayList(ARGS_CONTENTS_LIST)?.let {
            mAdapter.update(it)
        }
    }

    private fun initRecyclerView() {
        rv_contents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
    }

    companion object {
        const val ARGS_HREF = "href"
        const val ARGS_CONTENTS_LIST = "contents_list"

        fun newInstance(href: String, list: ArrayList<String>): ContentsFragment {
            val args = Bundle()
            args.putString(ARGS_HREF, href)
            args.putStringArrayList(ARGS_CONTENTS_LIST, list)

            val fragment = ContentsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}