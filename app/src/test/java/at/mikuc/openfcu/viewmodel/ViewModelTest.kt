package at.mikuc.openfcu.viewmodel

import android.util.Log
import at.mikuc.openfcu.data.SSOResponse
import at.mikuc.openfcu.repository.FcuSsoRepository
import at.mikuc.openfcu.repository.UserPreferencesRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
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

        // unit tests
        Given("wrong ID or password") {
            val pref = mockk<UserPreferencesRepository>()
            coEvery { pref.get<String>(any()) } returns "wrong"

            When("Single Sign-On") {
                val failedMsg = "Login Failed"

                val resp = SSOResponse(failedMsg, mockk(), mockk(), false)
                val sso = mockk<FcuSsoRepository>()
                coEvery { sso.singleSignOn(any()) } returns resp

                val vm = RedirectViewModel(pref, sso)
                vm.fetchRedirectToken(mockk())
                testCoroutineScheduler.advanceUntilIdle()

                vm.event.value.message shouldBe failedMsg
            }
        }
    }
}