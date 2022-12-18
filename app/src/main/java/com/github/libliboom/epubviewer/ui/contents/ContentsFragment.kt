package com.github.libliboom.epubviewer.ui.contents

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.github.libliboom.epubviewer.app.ui.BaseFragment
import com.github.libliboom.epubviewer.databinding.FragmentContentsBinding
import com.github.libliboom.epubviewer.presentation.contents.ContentsStore
import com.github.libliboom.epubviewer.ui.contents.recycler.ContentsAdapter
import javax.inject.Inject

class ContentsFragment : BaseFragment<FragmentContentsBinding>(), ContentsViewBinder {

  @Inject
  lateinit var requestManager: RequestManager

  private val viewModel: ContentsViewModel by viewModels()

  private val contentsAdapter by lazy { ContentsAdapter() { viewModel.dispatch(it) } }

  private lateinit var parameter: ContentsParameter

  override fun getArgs() {
    arguments?.run {
      val cover = getString(ARGS_COVER) ?: ""
      val contentsList = getStringArray(ARGS_CHAPTERS_LIST)?.toList() as List<String>
      val srcs = getStringArray(ARGS_SRC_LIST)?.toList() as List<String>
      parameter = ContentsParameter(cover, contentsList, srcs)
    }
  }

  override fun inflateBinding(container: ViewGroup?) =
    FragmentContentsBinding.inflate(layoutInflater, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.bind(this)
  }

  override fun afterInitView(binding: FragmentContentsBinding) {
    fun initCover() {
      requestManager
        .load(parameter.cover)
        .into(binding.contentsCover)
    }
    fun initAdapter() {
      contentsAdapter.init(parameter.cover, parameter.chapters, parameter.srcs)
    }
    fun initRecyclerView() {
      binding.contentsChaptersRecyclerView.apply {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = contentsAdapter
      }
    }
    if (::parameter.isInitialized.not()) finish()
    super.afterInitView(binding)
    initCover()
    initAdapter()
    initRecyclerView()
  }

  override fun represent(state: ContentsStore.State) {
    when (state.type) {
      ContentsStore.StateType.CLICK_CONTENTS -> representClickContents(state)
      else -> {}
    }
  }

  private fun representClickContents(state: ContentsStore.State) {
    fun sendResult(intent: Intent) {
      requireActivity().run {
        setResult(Activity.RESULT_OK, intent)
        finish()
      }
    }
    val intent = Intent()
    intent.putExtra(EXTRA_INDEX_OF_CHAPTER, state.contentName)
    sendResult(intent)
  }

  companion object {
    const val ARGS_COVER = "cover"
    const val ARGS_CHAPTERS_LIST = "contents_chapters_list"
    const val ARGS_SRC_LIST = "contents_src_list"
    const val EXTRA_INDEX_OF_CHAPTER = "com.github.libliboom.epubviewer.ui.contents.index_of_chapter"

    fun newInstance(parameter: ContentsParameter): ContentsFragment {
      val args = Bundle()
      args.putString(ARGS_COVER, parameter.cover)
      args.putStringArray(ARGS_CHAPTERS_LIST, parameter.chapters.toTypedArray())
      args.putStringArray(ARGS_SRC_LIST, parameter.srcs.toTypedArray())

      val fragment = ContentsFragment()
      fragment.arguments = args
      return fragment
    }
  }
}
