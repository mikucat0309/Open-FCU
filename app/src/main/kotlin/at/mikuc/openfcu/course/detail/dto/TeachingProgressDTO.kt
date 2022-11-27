package at.mikuc.openfcu.course.detail.dto

import kotlinx.serialization.Serializable

/*
[
  {
    "tpd_seq": 1,
    "tpd_week": "1-2",
    "tpd_content": "纖維染整工業之架構與染料類別",
    "tpd_exam": "上課點名學生，課堂中隨時提問請學生回答",
    "tpd_remark": " ",
    "tpd_content_en": " ",
    "tpd_exam_en": " ",
    "tpd_remark_en": " "
  },
  {
    "tpd_seq": 2,
    "tpd_week": "3",
    "tpd_content": "纖維染整製程概況分析",
    "tpd_exam": "課堂中隨時提問請學生回答,學生有任何學習上的問題",
    "tpd_remark": "紡織產業上市公司之核心技術說明",
    "tpd_content_en": " ",
    "tpd_exam_en": " ",
    "tpd_remark_en": " "
  },
]
 */

@Serializable
data class TeachingProgressDTO(
    val tpd_seq: Int,
    val tpd_week: String = "",
    val tpd_content: String = "",
    val tpd_exam: String = "",
    val tpd_remark: String = "",
    val tpd_content_en: String = "",
    val tpd_exam_en: String = "",
    val tpd_remark_en: String = "",
)
