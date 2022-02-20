package com.github.libliboom.common

import com.github.libliboom.common.const.Resource

object Constant {

  fun getKeepFilelist(): List<String> {
    return listOf(
      Resource.CONTAINER_FILE_NAME,
      Resource.CONTENTS_SAMPLE_FILE_NAME_00,
      Resource.CONTENTS_SAMPLE_FILE_NAME_01,
      Resource.CONTENTS_COVER_FILE_NAME,
      Resource.CONTENT_OPF_FILE_NAME,
      Resource.TOC_NCX_FILE_NAME,
      Resource.CONTENT_GUIDE_FILE_NAME,
      Resource.MIME_TYPE_FILE_NAME
    )
  }
}
