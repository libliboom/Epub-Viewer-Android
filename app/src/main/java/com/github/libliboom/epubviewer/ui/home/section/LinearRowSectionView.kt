package com.github.libliboom.epubviewer.ui.home.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.libliboom.epubviewer.domain.home.Book
import com.github.libliboom.epubviewer.domain.home.LinearRowSection

@Composable
fun LinearRowSectionView(
  section: LinearRowSection,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    SectionTitleView(section.name)
    LazyRow(
      modifier = modifier,
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      items(
        items = section.books,
        itemContent = { item -> LinearSectionItemView(item) }
      )
    }
  }
}

@Composable
private fun LinearSectionItemView(book: Book) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { },
    elevation = 4.dp
  ) {
    Column {
      BookCoverView(book, Modifier.height(132.dp))
    }
  }
}
