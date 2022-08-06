package at.mikuc.openfcu

import android.net.Uri
import android.util.Log
import at.mikuc.openfcu.course.Course
import at.mikuc.openfcu.course.Opener
import at.mikuc.openfcu.course.Period
import at.mikuc.openfcu.course.search.CourseSearchViewModel
import at.mikuc.openfcu.course.search.FcuCourseSearchRepository
import at.mikuc.openfcu.course.search.SearchFilter
import at.mikuc.openfcu.qrcode.FcuQrcodeRepository
import at.mikuc.openfcu.qrcode.QrcodeViewModel
import at.mikuc.openfcu.redirect.FcuSsoRepository
import at.mikuc.openfcu.redirect.RedirectViewModel
import at.mikuc.openfcu.redirect.SSOResponse
import at.mikuc.openfcu.setting.UserPreferencesRepository
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalStdlibApi::class)
internal class ViewModelTest : BehaviorSpec() {

    private val dispatcher = StandardTestDispatcher()

    override suspend fun beforeAny(testCase: TestCase) {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.w(any(), any<String>()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    override suspend fun beforeEach(testCase: TestCase) {
        Dispatchers.setMain(dispatcher)
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        Dispatchers.resetMain()
    }

    init {
        // config
        coroutineTestScope = true
        isolationMode = IsolationMode.InstancePerLeaf

        // unit tests
        Given("ID and password") {
            val pref = mockk<UserPreferencesRepository>()
            coEvery { pref.get<String>(any()) } returns "mock"

            When("single sign-on") {
                val mockMsg = "mock"
                val mockUri = mockk<Uri>()

                Then("failed") {
                    val sso = mockk<FcuSsoRepository>()
                    val resp = SSOResponse(mockMsg, mockk(), mockUri, false)
                    coEvery { sso.singleSignOn(any()) } returns resp
                    val vm = RedirectViewModel(pref, sso)
                    vm.fetchRedirectToken(mockk())
                    testCoroutineScheduler.advanceUntilIdle()

                    vm.event.value.message shouldBe mockMsg
                }
                Then("succeed") {
                    val sso = mockk<FcuSsoRepository>()
                    val resp = SSOResponse(mockMsg, mockk(), mockUri, true)
                    coEvery { sso.singleSignOn(any()) } returns resp
                    val vm = RedirectViewModel(pref, sso)
                    vm.fetchRedirectToken(mockk())
                    testCoroutineScheduler.advanceUntilIdle()

                    vm.event.value.uri shouldBe mockUri
                }
            }
            When("login QRCode system") {
                val qrcodeRepo = mockk<FcuQrcodeRepository>()
                val vm = QrcodeViewModel(pref, qrcodeRepo)
                val initialHexStr = vm.state.hexStr
                initialHexStr shouldBe null
                Then("failed") {
                    unmockkObject(qrcodeRepo)
                    coEvery { qrcodeRepo.fetchQrcode(any(), any()) } returns null
                    vm.fetchQrcode()
                    testCoroutineScheduler.advanceUntilIdle()
                    coVerify(exactly = 1) { qrcodeRepo.fetchQrcode(any(), any()) }
                    vm.state.hexStr shouldBe initialHexStr
                }
                Then("succeed") {
                    unmockkObject(qrcodeRepo)
                    val mockHexStr = "1234567890"
                    coEvery { qrcodeRepo.fetchQrcode(any(), any()) } returns mockHexStr
                    vm.fetchQrcode()
                    testCoroutineScheduler.advanceUntilIdle()
                    coVerify(exactly = 1) { qrcodeRepo.fetchQrcode(any(), any()) }
                    vm.state.hexStr shouldBe mockHexStr
                }
            }
        }
        Given("two different and fake courses") {
            val course1 = Course(
                name = "Course1",
                id = "ABC0001",
                code = 1,
                teacher = "Teacher1",
                periods = listOf(
                    Period(1, (10..14), "B1"),
                    Period(2, (1..2), "B2")
                ),
                credit = 5,
                opener = Opener("CSIE", "Academy1", "Depart1", 'A', 'B', 'C'),
                openNum = 80,
                acceptNum = 79,
                remark = "Remark1",
            )
            val course2 = Course(
                name = "Course2",
                id = "ABC0002",
                code = 2,
                teacher = "Teacher2",
                periods = listOf(
                    Period(1, (10..12), "3F"),
                    Period(2, (1..3), "4F")
                ),
                credit = 6,
                opener = Opener("CSIE", "Academy2", "Depart2", 'A', 'B', 'C'),
                openNum = 30,
                acceptNum = 10,
                remark = "Remark2",
            )
            And("a filter set year 111, 1st semester, monday, section 12 and 13rd") {
                val filter = SearchFilter(
                    year = 111,
                    semester = 1,
                    day = 1,
                    sections = setOf(12, 13),
                )

                When("search course") {
                    val csRepo = mockk<FcuCourseSearchRepository>()
                    coEvery { csRepo.search(any()) } returns listOf(course1, course1, course2)
                    Then("get course1") {
                        val vm = CourseSearchViewModel(csRepo)
                        vm.state = filter
                        vm.search()
                        testCoroutineScheduler.advanceUntilIdle()
                        coVerify(exactly = 1) { csRepo.search(any()) }
                        vm.result shouldBe listOf(course1)
                    }
                }
            }
        }
    }
}
