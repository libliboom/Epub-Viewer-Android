package com.github.libliboom.epubviewer.ui.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.libliboom.epubviewer.datasource.home.HomeRemoteDatasource
import com.github.libliboom.epubviewer.domain.home.usecase.HomeUseCase
import com.github.libliboom.epubviewer.repository.home.HomeRepository
import com.github.libliboom.epubviewer.ui.theme.GutenbergTheme

class HomeActivity : AppCompatActivity() {

  // TODO: Inject
  private val usecase = HomeUseCase(HomeRepository(HomeRemoteDatasource()))

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      GutenbergTheme {
        Scaffold(
          topBar = { HomeTopAppBar() }
        ) {
          val viewModel: HomeViewModel = viewModel()
          viewModel.bind(usecase)
          HomeScreen(
            viewModel = viewModel,
            Modifier.padding(it)
          )
        }
      }
    }
  }
}

@Composable
private fun HomeTopAppBar() {
  TopAppBar(
    title = {
      Text(
        text = "Gutenberg",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
      )
    }
  )
}

@Preview
@Composable
private fun HomeTopAppBarPreview() {
  HomeTopAppBar()
}
