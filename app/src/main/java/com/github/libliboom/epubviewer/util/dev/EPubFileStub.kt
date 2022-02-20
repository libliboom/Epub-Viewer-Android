package com.github.libliboom.epubviewer.util.dev

// TODO: 2020/05/12 Save those path into room after file is extracted
object EPubFileStub {
  const val BOOKS_PATH = "books/"
  const val OEBPS_PATH = "OEBPS/"
  const val EXTRACTED_EPUB_FILE_PATH = "extracted/"

  // REFACTORING: 2020/05/22 replace those stub files with function on runtime
  private const val ASSET_COVER_PATH = "file:///android_asset/covers/"
  const val ASSET_EXTRACTED_COVER_FILE_PATH_01 =
    "$ASSET_COVER_PATH@public@vhost@g@gutenberg@html@files@1342@1342-h@images@cover.jpg"
  const val ASSET_EXTRACTED_COVER_FILE_PATH_02 =
    "$ASSET_COVER_PATH@public@vhost@g@gutenberg@html@files@1400@1400-h@images@cover.jpg"
  const val ASSET_EXTRACTED_COVER_FILE_PATH_03 =
    "$ASSET_COVER_PATH@public@vhost@g@gutenberg@html@files@215@215-h@images@cover.jpg"
  const val ASSET_EXTRACTED_COVER_FILE_PATH_04 =
    "$ASSET_COVER_PATH@public@vhost@g@gutenberg@html@files@42108@42108-h@images@cover.jpg"
}
