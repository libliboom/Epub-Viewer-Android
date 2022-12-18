package com.github.libliboom.epubviewer.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColors = lightColors(
  primary = color_ff795548,
  primaryVariant = color_ffa98274,
  onPrimary = color_ffffffff,
  secondary = color_ffffa726,
  secondaryVariant = color_ffffd95b,
  onSecondary = color_ff000000,
  error = color_ffff3d00
)

@Composable
fun GutenbergTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colors = LightColors,
    content = content
  )
}
