package app.delivery.ownerapp.ui.viewmodels

import android.app.Activity
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.delivery.contract.ViewEvent
import app.delivery.domain.usecases.UserLoginUseCase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userLoginUseCase: UserLoginUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(LoginViewState())
    val viewState: StateFlow<LoginViewState> = _viewState

    fun onNameEntered(name: String) {
        _viewState.value = _viewState.value.copy(name = name)
    }

    fun onPhoneNumberEntered(phoneNumber: String) {
        _viewState.value = _viewState.value.copy(phoneNumber = phoneNumber)
    }

    fun onOTPEntered(otp: String) {
        _viewState.value = _viewState.value.copy(otp = otp)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Timber.i("Verification Complete")
            _viewState.value =
                _viewState.value.copy(sendingOTP = false, credential = credential)

        }

        override fun onVerificationFailed(e: FirebaseException) {
            Timber.w("Verification Failed")
            Timber.e(e)
            FirebaseCrashlytics.getInstance().recordException(e)
            _viewState.value =
                _viewState.value.copy(
                    sendingOTP = false,
                    verifyingOTP = false,
                    actionError = ViewEvent(e)
                )
        }

        override fun onCodeSent(
            verificationId: String,
            foreceResendingToken: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(verificationId, foreceResendingToken)
            Timber.i("Code Sent Successfully")
            _viewState.value =
                _viewState.value.copy(
                    sendingOTP = false,
                    sendOTPSuccess = true,
                    verificationId = verificationId
                )

        }
    }

    fun requestOTP(activity: Activity) {
        Timber.i("Requesting OTP")
        _viewState.value = _viewState.value.copy(sendingOTP = true)
        val phoneNumber = viewState.value.phoneNumber
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(COUNTRY_CODE_INDIA + phoneNumber)
            .setTimeout(10L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOTP(activity: Activity) {
        val otp = viewState.value.otp
        val verificationId = viewState.value.verificationId
        if (verificationId == null) {
            val exception = Exception("Verification id is null")
            FirebaseCrashlytics.getInstance().recordException(exception)
            _viewState.value = _viewState.value.copy(actionError = ViewEvent(exception))
            return
        }
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        signInWithPhoneAuthCredential(activity, credential)
    }

    fun signInWithPhoneAuthCredential(activity: Activity, credential: PhoneAuthCredential) {
        _viewState.value =
            _viewState.value.copy(sendingOTP = false, verifyingOTP = true)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Timber.i("Login Success")
                    performPostLogin()
                } else {
                    val exception = task.exception ?: Exception("Unable to verify")
                    Timber.e(exception)
                    FirebaseCrashlytics.getInstance().recordException(exception)
                    _viewState.value =
                        _viewState.value.copy(
                            verifyingOTP = false,
                            actionError = ViewEvent(exception)
                        )
                }
            }
    }

    private fun performPostLogin() {
        viewModelScope.launch {
            val name = viewState.value.name
            userLoginUseCase.performPostLogin(name).collect { result ->
                result.fold(
                    onSuccess = {
                        _viewState.value = _viewState.value.copy(userCreated = true)
                    },
                    onFailure = {
                        _viewState.value = _viewState.value.copy(actionError = ViewEvent(it))
                    }
                )
            }
        }
    }

    data class LoginViewState(
        val name: String = "",
        val phoneNumber: String = "",
        val otp: String = "",
        val sendingOTP: Boolean = false,
        val sendOTPSuccess: Boolean = false,
        val verifyingOTP: Boolean = false,
        val actionError: ViewEvent<Throwable>? = null,
        val userCreated: Boolean = false,
        val credential: PhoneAuthCredential? = null,
        val verificationId: String? = null
    ) {
        fun isFormValid(): Boolean {
            return name.isBlank().not()
                    && phoneNumber.isBlank().not()
                    && phoneNumber.length == MOBILE_NUMBER_LENGTH
                    && Patterns.PHONE.matcher(phoneNumber).matches()
        }

        companion object {
            private const val MOBILE_NUMBER_LENGTH = 10
        }
    }

    companion object {
        private const val COUNTRY_CODE_INDIA = "+91"
    }
}