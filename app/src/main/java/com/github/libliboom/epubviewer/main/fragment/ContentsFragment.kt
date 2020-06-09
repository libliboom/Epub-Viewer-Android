package com.github.libliboom.epubviewer.main.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.base.BaseFragment
import com.github.libliboom.epubviewer.databinding.FragmentContentsBinding
import com.github.libliboom.epubviewer.main.recycler.adapter.ContentsAdapter
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ContentsFragment : BaseFragment() {

    @Inject
    lateinit var contentsAdapter: ContentsAdapter

    @Inject
    lateinit var requestManager: RequestManager

    private val binding: FragmentContentsBinding by lazy {
        getBinding() as FragmentContentsBinding
    }

    private lateinit var contentsList: List<String>
    private lateinit var srcs: List<String>
    private lateinit var disposableSubject: Disposable

    override fun getLayoutId() = R.layout.fragment_contents

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCover()
        initAdapter()
        initRecyclerView()
    }

    override fun onDestroyView() {
        if (disposableSubject.isDisposed.not()) {
            disposableSubject.dispose()
        }
        super.onDestroyView()
    }

    private fun initCover() {
        arguments?.getString(ARGS_COVER)?.let {
            requestManager
                .load(it)
                .into(binding.contentsIvCover)
        }
    }

    private fun initAdapter() {
        arguments?.run {
            val cover = getString(ARGS_COVER) ?: ""
            contentsList = getStringArray(ARGS_CHAPTERS_LIST)?.toList() as List<String>
            srcs = getStringArray(ARGS_SRC_LIST)?.toList() as List<String>
            contentsAdapter.init(cover, contentsList, srcs)
        }

        disposableSubject = contentsAdapter.getPublishSubject().subscribe {
            val intent = Intent()
            intent.putExtra(EXTRA_INDEX_OF_CHAPTER, it)
            sendResult(intent)
        }
    }

    private fun initRecyclerView() {
        binding.contentsRvChapters.apply {
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
        const val ARGS_SRC_LIST = "contents_src_list"
        const val EXTRA_INDEX_OF_CHAPTER = "com.github.libliboom.epubviewer.main.fragment.index_of_chapter"

        fun newInstance(
            cover: String,
            chapters: List<String>,
            srcs: List<String>
        ): ContentsFragment {
            val args = Bundle()
            args.putString(ARGS_COVER, cover)
            args.putStringArray(ARGS_CHAPTERS_LIST, chapters.toTypedArray())
            args.putStringArray(ARGS_SRC_LIST, srcs.toTypedArray())

            val fragment = ContentsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
