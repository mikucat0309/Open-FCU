package at.mikuc.openfcu

import android.net.Uri
import at.mikuc.openfcu.redirect.RedirectViewModel
import at.mikuc.openfcu.redirect.SSOResponse
import at.mikuc.openfcu.setting.Credential
import at.mikuc.openfcu.setting.UserPreferenceRepository
import io.kotest.core.spec.IsolationMode
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalStdlibApi::class)
class RedirectViewModelTest : BaseTest() {
    init {
        coroutineTestScope = true
        isolationMode = IsolationMode.InstancePerLeaf

        Given("ID and password") {
            val pref = mockk<UserPreferenceRepository>()
            coEvery { pref.getCredential() } returns Credential("mock", "mock")

            When("single sign-on") {
                val mockMsg = "mock"
                val mockUri = mockk<Uri>()
                val mockService = "mock"
                val repo = mockk<FcuRepository>()

                Then("failed") {
                    val resp = SSOResponse(mockMsg, mockService, mockUri, false)
                    coEvery { repo.singleSignOn(any()) } returns resp
                    val vm = RedirectViewModel(pref, repo)
                    vm.fetchRedirectToken(mockService, dispatcher)
                    testCoroutineScheduler.advanceUntilIdle()

                    vm.event.value.message shouldBe mockMsg
                }
                Then("succeed") {
                    val resp = SSOResponse(mockMsg, mockService, mockUri, true)
                    coEvery { repo.singleSignOn(any()) } returns resp
                    val vm = RedirectViewModel(pref, repo)
                    vm.fetchRedirectToken(mockService, dispatcher)
                    testCoroutineScheduler.advanceUntilIdle()

                    vm.event.value.uri shouldBe mockUri
                }
            }
        }
    }
}
