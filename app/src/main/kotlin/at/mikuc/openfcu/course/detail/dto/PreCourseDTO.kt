package at.mikuc.openfcu.course.detail.dto

import kotlinx.serialization.Serializable

/*
[
  {
    "pre_subid3": "COE214            ",
    "sub_name": "有機化學(一)",
    "sub_ename": "ORGANIC CHEMISTRY(1)",
    "pre_describe": "具備化學合成及化學反應的重要概念與技能",
    "pre_describe_en": null
  }
]
*/

@Serializable
data class PreCourseDTO(
    val pre_subid3: String = "",
    val sub_name: String = "",
    val sub_ename: String = "",
    val pre_describe: String = "",
    val pre_describe_en: String = "",
)
