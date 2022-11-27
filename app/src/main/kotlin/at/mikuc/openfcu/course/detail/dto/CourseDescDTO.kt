package at.mikuc.openfcu.course.detail.dto

import kotlinx.serialization.Serializable

/*
[
  {
    "sub_id3": "IECS201  ",
    "scr_credit": 3,
    "sub_name": "資料結構",
    "sub_ename": "DATA STRUCTURE",
    "describe_ch": "本課程的目標在於讓學生經由實習課程的進行......",
    "describe_en": "The purpose of this course is to make......"
  }
]
*/

@Serializable
data class CourseDescDTO(
    val sub_id3: String = "",
    val scr_credit: Double,
    val sub_name: String = "",
    val sub_ename: String = "",
    val describe_ch: String = "",
    val describe_en: String = "",
)
