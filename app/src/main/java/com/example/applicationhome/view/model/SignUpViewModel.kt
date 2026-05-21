package com.example.applicationhome.view.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.FirebasePostResponse
import com.example.applicationhome.data.models.RetrofitInstance
import com.example.applicationhome.data.models.UserClass
import kotlinx.coroutines.launch
import retrofit2.Response

class SignUpViewModel : ViewModel(){
    val firstnamestate = TextFieldState()
    val lastnamestate = TextFieldState()
    val emailstate = TextFieldState()
    val passwordstate = TextFieldState()
    val confirmpasswordstate = TextFieldState()
    val phonenumberstate = TextFieldState()
    val addressstate = TextFieldState()


    var userRequest by mutableStateOf(
        UserClass(
        null,
        null,
        null,
        null,
        null,
        null
        )
    )

    var bottonState by mutableStateOf(false)

    var userId by mutableStateOf(""); private set

    var isEmailDone by mutableStateOf(false)

    // 2. متغير عشان يشيل رسالة النجاح أو الفشل ونراقبها من الـ UI
    private val _signUpResult = MutableLiveData<String>()
    val signUpResult: LiveData<String> get() = _signUpResult

    var signupPages by mutableStateOf(1)

    fun signUpButton(){
        userRequest = userRequest.copy(
            firstnamestate.text.toString(),
            lastnamestate.text.toString(),
            emailstate.text.toString(),
            passwordstate.text.toString(),
            phonenumberstate.text.toString(),
            addressstate.text.toString()
        )
        if(userRequest != null) {
            registerUserInFirebase(userRequest!!)
        }
    }

    fun bottonstate(){
        val allowedEmail = "^[A-Za-z0-9._%+-]+@(gmail|yahoo|outlook)\\.(com|net)$".toRegex(RegexOption.IGNORE_CASE)
        val isEmailValid = emailstate.text.matches(allowedEmail)
        searchForEmail(emailstate.text.toString())
        if(
            firstnamestate.text.isNotEmpty()
            && lastnamestate.text.isNotEmpty()
            && emailstate.text.isNotEmpty()
            && isEmailValid
            && passwordstate.text.isNotEmpty()
            && passwordstate.text.length >= 8
            && passwordstate.text == confirmpasswordstate.text
        ){
            bottonState = true
        }else{
            bottonState = false
        }
    }

    fun nextPage(){
        if(isEmailDone){
            signupPages += 1
        }
    }

    fun lastPage(){
        signupPages -= 1
    }

    fun registerUserInFirebase(userRequest : UserClass) {

        // بنفتح السكوب بتاع الكوروتين عشان نشتغل في الخلفية
        viewModelScope.launch {
            try {
                // بننادي الدالة اللي عملناها في الـ ApiService
                val response: Response<FirebasePostResponse> = RetrofitInstance.api.signUp(userRequest)

                // 🟢 الـ if الذكية بتاعتك هنا عشان تتأكد إن العملية نجحت
                if (response.isSuccessful && response.body() != null) {
                    // الفايربيز عملت الـ ID بنجاح ورجعتهولنا جوه response.body()?.name
                    userId = response.body()?.name.toString()

                    // بنكتب الرسالة اللي أنت عايزها تظهر لما العملية تنجح
                    _signUpResult.value = "Account created"
                } else {
                    // السيرفر رد بس فيه مشكلة (مثلاً اللينك غلط أو الداتا ناقصة)
                    _signUpResult.value = "فشل التسجيل: ${response.message()}"
                }

            } catch (e: Exception) {
                println("خطأ")
                // حصلت مشكلة في النت (مفيش واي فاي، السيرفر واقع تماماً، إلخ)
                _signUpResult.value = "خطأ في الشبكة: ${e.message}"
            }
        }
    }

    fun searchForEmail(useremail : String){
        viewModelScope.launch {
            try{
                val formattedEmail = "\"$useremail\""
                val response = RetrofitInstance.api.getUserEmail(email = formattedEmail)
                if(response.isSuccessful && response.body() != null){
                    val userMap = response.body()!!
                    if(userMap.isNotEmpty()){
                        isEmailDone = false
                    }else{
                        isEmailDone = true
                    }
                }else {
                    println("❌ كود الفشل من السيرفر: ${response.code()}")
                    println("❌ رسالة الفرايربيز: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception){
                println("خطأ في الشبكة: ${e.message}")
            }
        }
    }
}