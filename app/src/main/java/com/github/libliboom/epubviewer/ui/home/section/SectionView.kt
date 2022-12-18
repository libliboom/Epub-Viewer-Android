package com.github.libliboom.epubviewer.ui.home.section

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest.Builder
import com.github.libliboom.epubviewer.domain.home.Book

@Composable
fun SectionTitleView(name: String) {
  Text(
    text = name,
    fontSize = 16.sp,
    fontWeight = FontWeight.Bold
  )
}

@Composable
fun BookCoverView(book: Book, modifier: Modifier = Modifier) {
  AsyncImage(
    model = Builder(LocalContext.current)
      .data(book.imageUrl)
      .crossfade(true)
      .build(),
    contentDescription = null,
    modifier = modifier.aspectRatio(1 / 1.6f),
    contentScale = ContentScale.FillHeight,
  )
}

@Composable
fun BookTitleView(
  book: Book,
  modifier: Modifier = Modifier,
  textAlignCenter: Boolean = false
) {
  Text(
    text = book.title,
    modifier = modifier,
    maxLines = 2,
    overflow = TextOverflow.Ellipsis,
    style = TextStyle(
      fontFamily = FontFamily.Monospace,
      fontWeight = FontWeight.W800,
      fontStyle = FontStyle.Italic,
    ),
    textAlign = if (textAlignCenter) TextAlign.Center else TextAlign.Start
  )
}

@Composable
fun BookAuthorView(
  book: Book,
  modifier: Modifier = Modifier,
  textAlignCenter: Boolean = false
) {
  Text(
    text = book.author,
    modifier = modifier.padding(top = 4.dp),
    maxLines = 2, overflow = TextOverflow.Ellipsis,
    style = TextStyle(
      fontFamily = FontFamily.Monospace,
      fontWeight = FontWeight.W200,
    ),
    textAlign = if (textAlignCenter) TextAlign.Center else TextAlign.Start
  )
}

@Composable
fun BookInfoTextView(text: String, maxLines: Int = 1) {
  Text(
    text = text,
    maxLines = maxLines, overflow = TextOverflow.Ellipsis,
    fontSize = 12.sp,
    style = TextStyle(
      fontFamily = FontFamily.Monospace,
      fontWeight = FontWeight.W100,
    ),
  )
}
