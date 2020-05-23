package com.github.libliboom.epubviewer.main.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.base.BaseFragment
import com.github.libliboom.epubviewer.main.recycler.adapter.ContentsAdapter
import kotlinx.android.synthetic.main.fragment_contents.iv_contents
import kotlinx.android.synthetic.main.fragment_contents.rv_contents
import javax.inject.Inject

class ContentsFragment : BaseFragment() {

    @Inject
    lateinit var contentsAdapter: ContentsAdapter

    @Inject
    lateinit var requestManager: RequestManager

    private lateinit var contents: ArrayList<String>

    override fun getLayoutId() = R.layout.fragment_contents

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCover()
        initAdapter()
        initRecyclerView()
    }

    private fun initCover() {
        arguments?.getString(ARGS_COVER)?.let {
            requestManager
                .load(it)
                .into(iv_contents)
        }
    }

    private fun initAdapter() {
        arguments?.run {
            val cover = getString(ARGS_COVER) ?: ""
            contents = getStringArrayList(ARGS_CHAPTERS_LIST) ?: ArrayList<String>()
            contentsAdapter.init(cover, contents)
        }

        contentsAdapter.getPublishSubject().subscribe {
            val intent = Intent()
            intent.putExtra(EXTRA_INDEX_OF_CHAPTER, it)
            sendResult(intent)
        }
    }

    private fun initRecyclerView() {
        rv_contents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = contentsAdapter
        }
    }

    private fun sendResult(intent: Intent) {
        requireActivity().run {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        const val ARGS_COVER = "cover"
        const val ARGS_CHAPTERS_LIST = "contents_chapters_list"
        const val EXTRA_INDEX_OF_CHAPTER = "com.github.libliboom.epubviewer.main.fragment.index_of_chapter"

        fun newInstance(cover: String, chapters: ArrayList<String>): ContentsFragment {
            val args = Bundle()
            args.putString(ARGS_COVER, cover)
            args.putStringArrayList(ARGS_CHAPTERS_LIST, chapters)

            val fragment = ContentsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}