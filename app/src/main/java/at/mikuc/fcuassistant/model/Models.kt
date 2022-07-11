package at.mikuc.fcuassistant.model

import android.net.Uri
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SSORequest(
    @SerialName("Account") val account: String,
    @SerialName("Password") val password: String,
    @SerialName("RedirectService") val redirectService: Service,
)

@Serializable
data class SSOResponse(
    @SerialName("Message") val message: String = "",
    @SerialName("RedirectService") val redirectService: Service,
    @SerialName("RedirectUrl") @Serializable(with = UriAsStringSerializer::class) val redirectUri: Uri,
    @SerialName("Success") val success: Boolean,
)

@Serializable
enum class Service {
    @SerialName("MyFCU Information System")
    MYFCU,

    @SerialName("iLearn 2.0")
    ILEARN2,

    @SerialName("IC Card")
    IC_CARD,
    ;
}

@Serializable
data class LoginRequest(
    @SerialName("Account") val account: String,
    @SerialName("Password") val password: String,
    @SerialName("DeviceType") val deviceType: String,
    @SerialName("DeviceName") val deviceName: String,
    @SerialName("DeviceToken") val deviceToken: String,
)

@Serializable
data class Config(
    val nid: String,
    val password: String,
)

// TODO v0.2
//@Serializable
//data class Timetable(
//    @SerialName("Message") val message: String,
//    @SerialName("Success") val success: Boolean,
//    @SerialName("TimetableEn") val enPeriods: List<CoursePeriod>,
//    @SerialName("TimetableTw") val twPeriods: List<CoursePeriod>,
//)
//
//@Serializable
//data class CoursePeriod(
////    @SerialName("ClsId") val classId: "CE07131",
////    @SerialName("ClsName") val className: " ",
////    @SerialName("ColorCode") val colorCode: " ",
////    @SerialName("CourseMethod") val courseMethod: "4",
////    @SerialName("Memo") val memo: "mix class",
////    @SerialName("PeriodTime") val periodTime: "09:10~10:00",
////    @SerialName("RomId") val roomId: " ",
////    @SerialName("RomName") val roomName: "IEB306",
////    @SerialName("ScoWarning") val ScoWarning: "N",
////    @SerialName("ScrDup") val ScrDup: "001",
////    @SerialName("ScrSelcode") val ScrSelcode: "1271",
////    @SerialName("SctPeriod") val SctPeriod: 2,
////    @SerialName("SctWeek") val SctWeek: 1,
////    @SerialName("SubId") val subId: "48590",
////    @SerialName("SubName") val subName: "PROGRAMMING LANGUAGE",
////    @SerialName("TchLink") val TchLink: " ",
//    @SerialName("YmsSmester") val semester: Int,
//    @SerialName("YmsYear") val year: Int,
//)


