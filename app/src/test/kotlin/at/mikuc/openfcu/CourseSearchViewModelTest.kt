package at.mikuc.openfcu

import at.mikuc.openfcu.course.Course
import at.mikuc.openfcu.course.Opener
import at.mikuc.openfcu.course.Period
import at.mikuc.openfcu.course.search.CourseSearchViewModel
import at.mikuc.openfcu.course.search.SearchFilter
import io.kotest.core.spec.IsolationMode
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalStdlibApi::class)
internal class CourseSearchViewModelTest : BaseTest() {

//    override fun extensions() = listOf(KoinExtension(myTestModule))

    init {
        coroutineTestScope = true
        isolationMode = IsolationMode.InstancePerLeaf

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
                    val repo = mockk<FcuRepository>()
                    Then("get course1") {
                        coEvery { repo.search(any()) } returns listOf(course1, course1, course2)
                        val vm = CourseSearchViewModel(repo)
                        vm.search(filter, dispatcher)
                        testCoroutineScheduler.advanceUntilIdle()

                        coVerify(exactly = 1) { repo.search(any()) }
                        vm.result shouldBe listOf(course1)
                    }
                }
            }
        }
    }
}
