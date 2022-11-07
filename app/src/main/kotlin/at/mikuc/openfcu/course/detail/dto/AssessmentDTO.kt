package at.mikuc.openfcu.course.detail.dto

import kotlinx.serialization.Serializable

/*
{
	"d": [
		{
			"tpa_score_new": "(1) Homework assignment: 10%......",
			"tpa_score_new_en": "",
			"tpa_url": "",
			"tpa_remark": "",
			"tpa_remark_en": "",
			"yms_year": 111,
			"yms_smester": 1,
			"cls_id": "CE05131",
			"sub_id": "77032",
			"scr_dup": "001"
		}
	]
}
 */

@Serializable
data class AssessmentDTO(
    val __type: String = "",
    val tpa_score_new: String = "",
    val tpa_score_new_en: String = "",
    val tpa_url: String = "",
    val tpa_remark: String = "",
    val tpa_remark_en: String = "",
    val yms_year: Int,
    val yms_smester: Int,
    val cls_id: String = "",
    val sub_id: String = "",
    val scr_dup: String = "",
)
