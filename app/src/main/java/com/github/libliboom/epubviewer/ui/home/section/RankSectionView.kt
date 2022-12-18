package com.github.libliboom.epubviewer.ui.home.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.libliboom.epubviewer.domain.home.RankSection

@Composable
fun RankSectionView(
  section: RankSection,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    SectionTitleView(name = section.name)
    RankITemView(modifier, section)
  }
}

@Composable
@OptIn(ExperimentalTextApi::class)
private fun RankITemView(
  modifier: Modifier,
  section: RankSection
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable { },
    elevation = 4.dp
  ) {
    Column {
      section.catalogs.forEach { item ->
        Text(
          text = item.title,
          modifier = Modifier.padding(2.dp),
          style = TextStyle(brush = Brush.linearGradient(colors = listOf(Color.Cyan, Color.Blue, Color.Red))),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}
