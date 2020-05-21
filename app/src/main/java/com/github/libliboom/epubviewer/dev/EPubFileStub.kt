package com.github.libliboom.epubviewer.dev

// TODO: 2020/05/12 Save those path into room after file is extracted
object EPubFileStub {
    const val BOOKS_PATH = "books/"
    const val OEBPS_PATH = "OEBPS/"
    const val EXTRACTED_EPUB_FILE_PATH = "extracted/"

    // REFACTORING: 2020/05/22 replace those stub files with function on runtime
    const val ASSET_EXTRACTED_COVER_FILE_PATH_01 = "file:///android_asset/covers/@public@vhost@g@gutenberg@html@files@11@11-h@images@cover.jpg"
    const val ASSET_EXTRACTED_COVER_FILE_PATH_02 = "file:///android_asset/covers/@public@vhost@g@gutenberg@html@files@1342@1342-h@images@cover.jpg"
    const val ASSET_EXTRACTED_COVER_FILE_PATH_03 = "file:///android_asset/covers/@public@vhost@g@gutenberg@html@files@25525@25525-h@images@cover.jpg"
    const val ASSET_EXTRACTED_COVER_FILE_PATH_04 = "file:///android_asset/covers/@public@vhost@g@gutenberg@html@files@43@43-h@images@jekyllhydecover.jpg"
    const val ASSET_EXTRACTED_COVER_FILE_PATH_05 = "file:///android_asset/covers/@public@vhost@g@gutenberg@html@files@59603@59603-h@images@cover.jpg"
}