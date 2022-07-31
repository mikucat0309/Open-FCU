package at.mikuc.openfcu.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimetableResponseDTO(
    @SerialName("Message") val message: String,
    @SerialName("Success") val isSuccess: Boolean,
    @SerialName("TimetableEn") val timeTableEn: List<SectionDTO>?,
    @SerialName("TimetableTw") val timeTableTw: List<SectionDTO>?,
)

@Serializable
data class SectionDTO(
    @SerialName("ClsId") val classId: String,
    @SerialName("ClsName") val className: String,
    @SerialName("ColorCode") val color: String?,
    @SerialName("CourseMethod") val method: String,
    @SerialName("Memo") val memo: String,
    @SerialName("PeriodTime") val time: String,
    @SerialName("RomId") val roomId: String,
    @SerialName("RomName") val roomName: String,
    @SerialName("ScoWarning") val scoreWarning: String?,
    @SerialName("ScrDup") val duplicate: String,
    @SerialName("ScrSelcode") val code: String,
    @SerialName("SctPeriod") val section: Int,
    @SerialName("SctWeek") val day: Int,
    @SerialName("SubId") val subId: String,
    @SerialName("SubName") val subName: String,
    @SerialName("TchLink") val tchLink: String,
    @SerialName("YmsSmester") val semester: Int,
    @SerialName("YmsYear") val year: Int,
) {
    fun toSection(): Section {
        return Section(
            method = method.toIntOrNull(),
            memo = memo,
            time = time,
            location = roomName,
            section = section,
            day = day,
            name = subName,
        )
    }
}
