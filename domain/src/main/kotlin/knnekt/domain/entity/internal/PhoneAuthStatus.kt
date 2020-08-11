package knnekt.domain.entity.internal

sealed class PhoneAuthStatus {

    data class CodeSent(val verificationId: String): PhoneAuthStatus()
    data class Completed(val confirmCode: String?): PhoneAuthStatus()
    data class Failure(val e: Exception): PhoneAuthStatus()

}