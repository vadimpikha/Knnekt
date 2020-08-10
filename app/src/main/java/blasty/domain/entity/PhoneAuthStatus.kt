package blasty.domain.entity

sealed class PhoneAuthStatus {

    data class CodeSent(val verificationId: String): PhoneAuthStatus()
    data class Completed(val confirmCode: String?): PhoneAuthStatus()
    data class Failure(val e: Exception): PhoneAuthStatus()

}