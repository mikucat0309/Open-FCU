package at.mikuc.openfcu

import at.mikuc.openfcu.setting.Credential
import at.mikuc.openfcu.setting.UserPreferenceRepository
import at.mikuc.openfcu.timetable.Section
import at.mikuc.openfcu.timetable.TimetableViewModel
import io.kotest.core.spec.IsolationMode
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalStdlibApi::class)
class TimetableViewModelTest : BaseTest() {
    init {
        coroutineTestScope = true
        isolationMode = IsolationMode.InstancePerLeaf

        Given("ID and password") {
            val pref = mockk<UserPreferenceRepository>()
            coEvery { pref.getCredential() } returns Credential("mock", "mock")
            val repo = mockk<FcuRepository>()

            When("fetch timetable") {
                val vm = TimetableViewModel(pref, repo)
                Then("login failed") {
                    coEvery { repo.fetchTimetable(any()) } returns null
                    val initialState = vm.state
                    vm.fetchTimetable(dispatcher)
                    testCoroutineScheduler.advanceUntilIdle()

                    coVerify(exactly = 1) { repo.fetchTimetable(any()) }
                    vm.state shouldBe initialState
                }
                Then("login succeed and received timetable") {
                    val expectedSections = listOf(
                        Section(
                            method = 4,
                            memo = "memo",
                            time = "09:10-10:00",
                            location = "資電館234資電館234資電館234",
                            section = 2,
                            day = 2,
                            name = "我是課程名稱我是課程名稱我是課程名稱",
                        )
                    )
                    coEvery { repo.fetchTimetable(any()) } returns expectedSections
                    vm.fetchTimetable(dispatcher)
                    testCoroutineScheduler.advanceUntilIdle()

                    coVerify(exactly = 1) { repo.fetchTimetable(any()) }
                    vm.state.sections shouldBe expectedSections
                }
            }
        }
    }
}
