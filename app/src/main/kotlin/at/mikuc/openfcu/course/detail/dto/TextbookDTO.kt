package at.mikuc.openfcu.course.detail.dto

import kotlinx.serialization.Serializable

/*
[
  {
    "tpb_book": "\"染整技術原理與實務\"",
    "tpb_author": "廖盛焜、林尚明、郭文貴、張振忠、陳兆琦編著",
    "tpb_publish": "紡織綜合研究所出版,196頁，Oct 2005",
    "tpb_descr": " ",
    "tpb_seq": 1,
    "tpb_book_en": null,
    "tpb_author_en": null,
    "tpb_publish_en": null,
    "tpb_descr_en": null,
    "tpb_seq1": 1
  },
  {
    "tpb_book": "機能性紡織品之製造與評估",
    "tpb_author": "王立主等15人合編",
    "tpb_publish": "台灣區絲織工業同業公會編製,292頁，Oct 2005",
    "tpb_descr": " ",
    "tpb_seq": 2,
    "tpb_book_en": " ",
    "tpb_author_en": " ",
    "tpb_publish_en": " ",
    "tpb_descr_en": " ",
    "tpb_seq1": 2
  }
]
 */

@Serializable
data class TextbookDTO(
    val tpb_seq: Int,
    val tpb_seq1: Int,
    val tpb_book: String = "",
    val tpb_author: String = "",
    val tpb_publish: String = "",
    val tpb_descr: String = "",
    val tpb_book_en: String = "",
    val tpb_author_en: String = "",
    val tpb_publish_en: String = "",
    val tpb_descr_en: String = "",
)
