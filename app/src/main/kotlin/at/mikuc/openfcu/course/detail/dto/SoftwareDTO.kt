package at.mikuc.openfcu.course.detail.dto

import kotlinx.serialization.Serializable

/*
{
	"d": [
		{
			"seq_no": 1,
			"soft_name": "Dev C++",
			"soft_name_en": " ",
			"soft_version": "6.3",
			"soft_version_en": " ",
			"soft_memo": "或任何相容的C/C++ compiler",
			"soft_memo_en": " "
		}
	]
}
 */

@Serializable
data class SoftwareDTO(
    val seq_no: Int,
    val soft_name: String = "",
    val soft_name_en: String = "",
    val soft_version: String = "",
    val soft_version_en: String = "",
    val soft_memo: String = "",
    val soft_memo_en: String = "",
)
