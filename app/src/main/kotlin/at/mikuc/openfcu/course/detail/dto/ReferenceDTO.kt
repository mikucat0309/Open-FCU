package at.mikuc.openfcu.course.detail.dto

import kotlinx.serialization.Serializable

/*
[
  {
    "tpc_book": "染色化學",
    "tpc_author": "邱永亮、魏盛德編譯",
    "tpc_publish": "徐氏文教基金會,339頁，2000。",
    "tpc_descr": null,
    "tpc_book_en": null,
    "tpc_author_en": null,
    "tpc_publish_en": null,
    "tpc_descr_en": null,
    "tpc_seq": 1
  },
  {
    "tpc_book": "“染色技術實習”",
    "tpc_author": "廖盛焜、張振忠合著",
    "tpc_publish": "全威圖書公司出版,230頁，October 2002。",
    "tpc_descr": " ",
    "tpc_book_en": " ",
    "tpc_author_en": " ",
    "tpc_publish_en": " ",
    "tpc_descr_en": " ",
    "tpc_seq": 2
  }
]
 */

@Serializable
data class ReferenceDTO(
    val tpc_seq: Int,
    val tpc_book: String = "",
    val tpc_author: String = "",
    val tpc_publish: String = "",
    val tpc_descr: String = "",
    val tpc_book_en: String = "",
    val tpc_author_en: String = "",
    val tpc_publish_en: String = "",
    val tpc_descr_en: String = "",
)
