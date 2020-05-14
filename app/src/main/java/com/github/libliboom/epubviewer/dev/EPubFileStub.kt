package com.github.libliboom.epubviewer.dev

// TODO: 2020/05/12 Save those path into room after file is extracted
object EPubFileStub {
    const val BOOKS_PATH = "books/"
    const val OEBPS_PATH = "OEBPS/"
    const val EPUB_FILE_PATH_01 = "books/pg1342.epub"
    const val EPUB_FILE_PATH_02 = "books/pg59603.epub"
    const val EXTRACTED_EPUB_FILE_PATH = "extracted/"

    const val ASSET_EPUB_FILE_PATH_01 = "file:///android_asset/books/pg1342.epub"
    const val ASSET_EPUB_FILE_PATH_02 = "file:///android_asset/books/pg59603.epub"
    const val ASSET_EXTRACTED_COVER_FILE_PATH_01 = "file:///android_asset/covers/@public@vhost@g@gutenberg@html@files@1342@1342-h@images@cover.jpg"
    const val ASSET_EXTRACTED_COVER_FILE_PATH_02 = "file:///android_asset/covers/@public@vhost@g@gutenberg@html@files@59603@59603-h@images@cover.jpg"

    const val EXTRACTED_EPUB_FILE_PATH_01 = "temp/"
    const val EXTRACTED_OEBPS_FILE_PATH_01 = "temp/OEBPS/"
    const val EXTRACTED_COVER_FILE_PATH_01 = "temp/OEBPS/@public@vhost@g@gutenberg@html@files@1342@1342-h@images@cover.jpg"
    const val ASSET_EXTRACTED_EPUB_FILE_PATH_01 = "file:///android_asset/temp/"
}