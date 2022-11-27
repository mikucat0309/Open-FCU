package at.mikuc.openfcu.course.detail.dto

import kotlinx.serialization.Serializable

/*
[
    {
        "yearsms": "111學年度第1學期",
        "yearsms_en": "111-1",
        "sub_name": "纖維染色學",
        "sub_ename": "FIBER DYEING PROCESS",
        "cls_name": "纖複三甲",
        "cls_ename": "Department of Fiber and Composite Materials",
        "combin_id": "CE05131-77032-001",
        "mso": "必修",
        "mso_en": "Required",
        "scr_credit": 2,
        "scr_period": "(一)03-04 語205 廖盛焜",
        "scr_period_eng": "(Mon)03-04 LB205 Shen-Kung Liao",
        "language": "中文",
        "lang_ename": "Chinese",
        "sub_id3": "FCME350",
        "scr_periodonly": "(一)03-04",
        "scr_periodonly_eng": "(Mon)03-04",
        "scr_room": "語205",
        "scr_room_eng": "LB205",
        "scr_teacher": "廖盛焜",
        "scr_teacher_eng": "Shen-Kung Liao",
        "soft_mark": "N",
        "emp_ename": "Shen-Kung Liao                                              "
    }
]
*/

@Serializable
data class CourseInfoDTO(
    val yearsms: String = "",
    val yearsms_en: String = "",
    val sub_name: String = "",
    val sub_ename: String = "",
    val cls_name: String = "",
    val cls_ename: String = "",
    val combin_id: String = "",
    val mso: String = "",
    val mso_en: String = "",
    val scr_credit: Double,
    val scr_period: String = "",
    val scr_period_eng: String = "",
    val language: String = "",
    val lang_ename: String = "",
    val sub_id3: String = "",
    val scr_periodonly: String = "",
    val scr_periodonly_eng: String = "",
    val scr_room: String = "",
    val scr_room_eng: String = "",
    val scr_teacher: String = "",
    val scr_teacher_eng: String = "",
    val soft_mark: String = "",
    val emp_ename: String = "",
)
