package com.github.libliboom.epubviewer.ui.home.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.github.libliboom.epubviewer.domain.home.Book
import com.github.libliboom.epubviewer.domain.home.LinearColumnSection

@Composable
fun LinearColumnSectionView(
  section: LinearColumnSection,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    SectionTitleView(name = section.name)
    Spacer(modifier = Modifier.height(6.dp))
    section.books.forEach { book ->
      LinearColumnSectionItemView(book, modifier)
      Spacer(modifier = modifier)
    }
  }
}

@Composable
private fun LinearColumnSectionItemView(book: Book, modifier: Modifier) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { }
      .padding(horizontal = 6.dp),
    elevation = 4.dp
  ) {
    ConstraintLayout(
      modifier = modifier
        .fillMaxWidth()
    ) {
      val (cover, title, author, info) = createRefs()

      BookCoverView(
        book = book,
        modifier = Modifier
          .constrainAs(cover) {
            start.linkTo(parent.start)
          }
          .height(150.dp)
      )

      BookTitleView(
        book = book,
        modifier = Modifier
          .constrainAs(title) {
            width = Dimension.fillToConstraints
            start.linkTo(cover.end)
            end.linkTo(parent.end)
          }
          .fillMaxWidth(),
        textAlignCenter = true
      )

      BookAuthorView(
        book = book,
        modifier = Modifier
          .constrainAs(author) {
            width = Dimension.fillToConstraints
            top.linkTo(title.bottom)
            start.linkTo(cover.end)
            end.linkTo(parent.end)
          }
          .wrapContentWidth(),
        textAlignCenter = true
      )

      Column(
        modifier = Modifier
          .constrainAs(info) {
            width = Dimension.fillToConstraints
            start.linkTo(cover.end)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
          }
          .fillMaxWidth()
          .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        BookInfoTextView(text = buildInfoText("Language", book.language))
        BookInfoTextView(text = buildInfoText("Date", book.releaseDate))
        BookInfoTextView(text = buildInfoText("Origin", book.originalPublication), maxLines = 2)
      }
    }
  }
}

private fun buildInfoText(name: String, contents: String) = buildAnnotatedString {
  append(
    AnnotatedString("$name - ", spanStyle = SpanStyle(fontWeight = FontWeight.W500))
  )
  append(
    AnnotatedString(contents, spanStyle = SpanStyle(fontWeight = FontWeight.W200))
  )
}.toString()
