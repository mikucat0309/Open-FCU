package at.mikuc.openfcu

import at.mikuc.openfcu.qrcode.QrcodeViewModel
import at.mikuc.openfcu.setting.Credential
import at.mikuc.openfcu.setting.UserPreferenceRepository
import io.kotest.core.spec.IsolationMode
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalStdlibApi::class)
class QrcodeViewModelTest : BaseTest() {
    init {
        coroutineTestScope = true
        isolationMode = IsolationMode.InstancePerLeaf

        Given("ID and password") {
            val pref = mockk<UserPreferenceRepository>()
            coEvery { pref.getCredential() } returns Credential("mock", "mock")
            val repo = mockk<FcuRepository>()

            When("login QRCode system") {
                val vm = QrcodeViewModel(pref, repo)
                val initialHexStr = vm.state.hexStr
                initialHexStr shouldBe null
                Then("failed") {
                    coEvery { repo.fetchQrcode(any()) } returns null
                    vm.fetchQrcode(dispatcher)
                    testCoroutineScheduler.advanceUntilIdle()

                    coVerify(exactly = 1) { repo.fetchQrcode(any()) }
                    vm.state.hexStr shouldBe initialHexStr
                }
                Then("succeed") {
                    val mockHexStr = "1234567890"
                    coEvery { repo.fetchQrcode(any()) } returns mockHexStr
                    vm.fetchQrcode(dispatcher)
                    testCoroutineScheduler.advanceUntilIdle()

                    coVerify(exactly = 1) { repo.fetchQrcode(any()) }
                    vm.state.hexStr shouldBe mockHexStr
                }
            }
        }
    }
}
