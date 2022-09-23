package at.mikuc.openfcu.redirect

data class RedirectUiState(
    val redirectItems: List<RedirectItem> = listOf(
        RedirectItem(
            title = "iLearn 2.0",
            service = "iLearn 2.0",
        ),
        RedirectItem(
            title = "MyFCU",
            service = "https://myfcu.fcu.edu.tw/main/webClientMyFcuMain.aspx#/prog/home",
        ),
        RedirectItem(
            title = "自主健康管理",
            service = "https://myfcu.fcu.edu.tw/main/S4301/S430101_temperature_record.aspx",
        ),
        RedirectItem(
            title = "空間借用",
            service = "https://myfcu.fcu.edu.tw/main/S5672/S5672_mainApply.aspx",
        ),
        RedirectItem(
            title = "學生請假",
            service = "https://myfcu.fcu.edu.tw/main/S3401/s3401_leave.aspx",
        ),
        RedirectItem(
            title = "課程查詢",
            service = "https://myfcu.fcu.edu.tw/main/coursesearch.aspx?sso",
        ),
    )
)
