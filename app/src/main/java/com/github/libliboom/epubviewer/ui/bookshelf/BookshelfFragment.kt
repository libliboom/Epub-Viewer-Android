package com.github.libliboom.epubviewer.ui.bookshelf

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.github.libliboom.epubviewer.app.ui.BaseFragment
import com.github.libliboom.epubviewer.databinding.FragmentBookshelfBinding
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore.Action
import com.github.libliboom.epubviewer.ui.bookshelf.recycler.BookListAdapter
import com.github.libliboom.epubviewer.ui.viewer.EPubReaderActivity
import javax.inject.Inject

class BookshelfFragment : BaseFragment<FragmentBookshelfBinding>(), BookshelfViewBinder {

  @Inject
  lateinit var requestManager: RequestManager

  private val viewModel: BookshelfViewModel by activityViewModels()

  private val bookListAdapter by lazy { BookListAdapter() { viewModel.dispatch(it) } }

  override fun inflateBinding(container: ViewGroup?) =
    FragmentBookshelfBinding.inflate(layoutInflater, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.bind(this)
  }

  override fun afterInitView(binding: FragmentBookshelfBinding) {
    super.afterInitView(binding)
    viewModel.dispatch(Action.Ui.UpdateList)
  }

  override fun represent(state: BookshelfStore.State) {
    when (state.type) {
      BookshelfStore.StateType.UPDATED_LIST -> representUpdateList()
      BookshelfStore.StateType.BOOK_CLICKED -> representBookClicked(state)
      else -> Unit
    }
  }

  // TODO: Refactoring to Init and Action
  private fun representUpdateList() {
    fun initAdapter() {
      bookListAdapter.updateViewModel(viewModel, requestManager)
    }
    fun initRecyclerView() {
      binding.bookshlefRv.apply {
        layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
        adapter = bookListAdapter
      }
    }
    initAdapter()
    initRecyclerView()
  }

  // TODO: Navigation
  private fun representBookClicked(state: BookshelfStore.State) {
    requireContext().startActivity(
      EPubReaderActivity.newIntent(
        requireActivity(),
        viewModel.ePubFiles[state.clickedIndex]
      )
    )
  }

  companion object {
    private const val SPAN_COUNT = 3
    fun newInstance() = BookshelfFragment()
  }
}
