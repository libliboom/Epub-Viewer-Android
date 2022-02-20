package com.github.libliboom.epubviewer.ui.bookshelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.base.BaseFragment
import com.github.libliboom.epubviewer.databinding.FragmentBookshelfBinding
import com.github.libliboom.epubviewer.ui.bookshelf.recycler.BookListAdapter
import com.github.libliboom.epubviewer.ui.viewer.EPubReaderActivity
import javax.inject.Inject

class BookshelfFragment : BaseFragment() {

  @Inject
  lateinit var requestManager: RequestManager

  private val viewModel: BookshelfViewModel by activityViewModels()

  private val bookListAdapter by lazy { BookListAdapter() }

  private val binding: FragmentBookshelfBinding by lazy {
    getBinding() as FragmentBookshelfBinding
  }

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
    bookListAdapter.run {
      updateViewModel(viewModel, requestManager)
      getPublishSubject().subscribe {
        requireContext().startActivity(
          EPubReaderActivity.newIntent(
            requireActivity(),
            viewModel.ePubFiles[it]
          )
        )
      }
    }
  }

  private fun initRecyclerView(view: View) {
    view as RecyclerView
    view.apply {
      layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
      adapter = bookListAdapter
    }
  }

  companion object {
    fun newInstance() = BookshelfFragment()
    private const val SPAN_COUNT = 3
  }
}
