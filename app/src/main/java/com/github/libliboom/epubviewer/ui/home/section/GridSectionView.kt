package com.github.libliboom.epubviewer.ui.home.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.libliboom.epubviewer.domain.home.Book
import com.github.libliboom.epubviewer.domain.home.usecase.GridSection

@Composable
fun GridSectionView(
  section: GridSection,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    SectionTitleView(section.name)
    LazyHorizontalGrid(
      rows = Fixed(2),
      modifier = modifier.height(180.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(
        items = section.books,
        itemContent = { item -> GridSectionItemView(item) }
      )
    }
  }
}

@Composable
private fun GridSectionItemView(book: Book) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { },
    elevation = 4.dp
  ) {
    Row(modifier = Modifier.width(220.dp)) {
      BookCoverView(book = book)
      ConstraintLayout(
        modifier = Modifier
          .padding(all = 6.dp)
      ) {
        val (title, author) = createRefs()
        BookTitleView(book, Modifier.constrainAs(title) { top.linkTo(parent.top) })
        BookAuthorView(book, Modifier.constrainAs(author) { top.linkTo(title.bottom) })
      }
    }
  }
}
