[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/kotlin-%230095D5.svg?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg?style=for-the-badge&logoColor=white)](https://ktlint.github.io/)
[![License](https://img.shields.io/github/license/mikucat0309/Open-FCU?style=for-the-badge&logoColor=white)](LICENSE)

# Open FCU

比行動逢甲更好用的行動 APP  
捨棄難用的內嵌網頁，全部使用原生界面  
去除不常用功能，新增課程查詢等實用功能

歡迎許願、提 issue 或 PR ，也歡迎找我聊天（？）

## UI Design

<p>
  <img src="img/home.jpg" width="200" />
  <img src="img/course_search.jpg" width="200" />
  <img src="img/daily_timetable.jpg" width="200" />
  <img src="img/qrcode.jpg" width="200" />
</p>

Figma: [https://www.figma.com/file/aMSNSXjII1Tk0ubtaKlWWg/Open-FCU](https://www.figma.com/file/aMSNSXjII1Tk0ubtaKlWWg/Open-FCU)

## Feature Planning

### 0.1

- [x] 儲存帳號密碼
- [x] 網頁跳轉
  - [x] iLearn 2.0
  - [x] MyFCU
  - [x] 請假
  - [x] 空間借用
  - [x] 自主健康管理
  - [x] 課程查詢

### 0.2

- [ ] 快速課堂簽到
- [x] 快速 PASS
- [x] 顯示 QRCode

~~暑假沒課讓我簽到，沒法模擬~~  
~~PASS 等開學再看情況決定是否實裝~~

### 0.3

- [x] 課表
  - [ ] ~~日模式~~
  - [x] 周模式

日模式課表比較少用，移至 todolist

### 0.4

- [x] 課程查詢

使用學校課程檢索 API 查詢

### 0.5

- [x] 捷徑 (Shortcut)

按一下開啟 MyFCU 或 iLearn 2.0

### 0.6

- [x] 假 PASS

### 0.7

- [ ] 重新設計 UI
- [ ] 課堂簽到

### 1.0

- [x] 完善 UI/UX
  - [x] 帳號密碼已儲存提醒
  - [ ] 課程查詢防呆設計
  - [x] 課程查詢結果的課程資訊重新排版

## Todolist

- 新的 icon
- MyFCU 通知
- 自訂快速跳轉捷徑
- 快速簽到
- 點選查詢結果顯示課程詳細資訊
- 日模式課表
- 上課提醒
- 手勢縮放課表
- QRCode 最大亮度

## Wishlist

- 將更多功能轉換成原生界面
- Desktop 支援
- iOS 支援 (哪天有人送我能跑 XCode 的電腦就會支援)

## Tech Stack

- Android SDK
- Jetpack Compose
- Kotlin/JVM
