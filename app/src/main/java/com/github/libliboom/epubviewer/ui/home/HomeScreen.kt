package com.github.libliboom.epubviewer.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.libliboom.epubviewer.domain.home.LinearColumnSection
import com.github.libliboom.epubviewer.domain.home.LinearRowSection
import com.github.libliboom.epubviewer.domain.home.RankSection
import com.github.libliboom.epubviewer.domain.home.usecase.GridSection
import com.github.libliboom.epubviewer.presentation.home.HomeStore
import com.github.libliboom.epubviewer.presentation.home.HomeStore.Action.Business.LoadRank
import com.github.libliboom.epubviewer.presentation.home.HomeStore.Action.Business.LoadSection
import com.github.libliboom.epubviewer.ui.home.section.GridSectionView
import com.github.libliboom.epubviewer.ui.home.section.LinearColumnSectionView
import com.github.libliboom.epubviewer.ui.home.section.LinearRowSectionView
import com.github.libliboom.epubviewer.ui.home.section.RankSectionView

@Composable
fun HomeScreen(
  viewModel: HomeViewModel,
  modifier: Modifier = Modifier
) {
  viewModel.dispatch(LoadRank)
  viewModel.dispatch(LoadSection)
  val state = viewModel.state.collectAsState()
  Section(state, modifier.padding(6.dp))
}

@Composable
fun Section(state: State<HomeStore.State>, modifier: Modifier = Modifier) {
  LazyColumn {
    items(
      items = state.value.sections,
      itemContent = { section ->
        when (section) {
          is RankSection -> RankSectionView(section, modifier)
          is GridSection -> GridSectionView(section, modifier)
          is LinearRowSection -> LinearRowSectionView(section, modifier)
          is LinearColumnSection -> LinearColumnSectionView(section, modifier)
        }
      }
    )
  }
}
