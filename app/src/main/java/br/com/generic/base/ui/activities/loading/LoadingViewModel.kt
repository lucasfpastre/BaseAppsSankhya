package br.com.generic.base.ui.activities.loading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import br.com.generic.base.data.extensions.countMatches
import br.com.generic.base.data.repository.RequestRepository
import br.com.generic.base.models.server.ServerData
import br.com.generic.base.models.service.request.ServiceRequest
import br.com.generic.base.models.service.response.JSessionID
import br.com.generic.base.models.service.response.LoginResponse
import br.com.generic.base.models.service.response.ResponseBody
import br.com.generic.base.models.view.request.ViewRequest
import br.com.generic.base.models.view.response.user.UserResponseBody
import br.com.generic.base.models.view.response.user.UserResponseData
import br.com.generic.base.models.view.response.user.UserResponseFields
import br.com.generic.base.models.view.response.user.UserResponseNames
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(private val requestRepo : RequestRepository) : ViewModel() {

    val serverData : LiveData<ServerData> = requestRepo.server.getServer().asLiveData()
    val loginResponse = MutableLiveData<LoginResponse?>()
    val responseBody = MutableLiveData<ResponseBody?>()
    val sessionId = MutableLiveData<JSessionID?>()
    val userResponseBody = MutableLiveData<UserResponseBody?>()
    val userResponseData = MutableLiveData<UserResponseData?>()
    val userResponseFields = MutableLiveData<UserResponseFields?>()
    val userResponseNames = MutableLiveData<UserResponseNames?>()
    val viewResponseStatus = MutableLiveData(false)
    val requestStatus = MutableLiveData(false)
    val connectionFailure = MutableLiveData(false)
    val connectionStatusDown = MutableLiveData(false)
    val timerStatus = MutableLiveData(false)
    val startApplication = MutableLiveData(false)
    var responseData : String = ""
    var failureMessage : String = ""
    var viewData : String = ""
    var cookieList : List<String?> = ArrayList()
    var timerJob: Job? = null

    // Requisição de Login
    fun getSessionId(serviceRequest: ServiceRequest, newUrl: String) {
        requestRepo.remote.getSessionId(serviceRequest, newUrl).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                timerJob?.cancel()
                if (response.body().toString().contains("jsessionid")) {
                    val gson = Gson()
                    cookieList = response.headers().values("Set-Cookie")
                    loginResponse.value = gson.fromJson(gson.toJson(response.body()), LoginResponse::class.java)
                    responseBody.value = loginResponse.value?.responseBody
                    sessionId.value = responseBody.value?.jsessionid
                    responseData = sessionId.value.toString()
                    requestStatus.value = true
                } else {
                    connectionFailure.value = true
                    failureMessage = "Usuário ou senha inválidos"
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                timerJob?.cancel()
                failureMessage = t.toString()
                connectionFailure.value = true
            }
        })
    }

    // Requisição de View - Como pode voltar mais de um dado no json, verifico com um contador para saber qual classe utilizar
    fun getViewData(viewRequest: ViewRequest, newUrl: String, cookie: String) {
        requestRepo.remote.getViewData(viewRequest, newUrl, cookie).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                timerJob?.cancel()
                // Essa mensagem ocorre quando a chave perde a validade
                if (response.body().toString().contains("Não autorizado")) {
                    connectionStatusDown.value = true
                } else {
                    // Podem ocorrer duas chamadas seguidas, então uma delas é cancelada automaticamente
                    if (response.body().toString().contains("situação de concorrência")) {
                        connectionFailure.value = true
                    } else {
                        val counter = countMatches(response.body().toString(), "CODUSU")
                        if (counter == 1) {
                            // Se encontrar o dado, dou continuidade ao login
                            timerJob?.cancel()
                            val gson = Gson()
                            userResponseBody.value = gson.fromJson(gson.toJson(response.body()), UserResponseBody::class.java)
                            userResponseData.value = userResponseBody.value?.responseBody
                            userResponseFields.value = userResponseData.value?.records
                            userResponseNames.value = userResponseFields.value?.viewNames
                            viewData = response.body().toString()
                            viewResponseStatus.value = true
                        } else {
                            // Nesse caso específico eu posso criar uma nova view que permite ou não o usuário de acessar o app
                            failureMessage = "Dados não localizados ou usuário não possui liberação"
                            viewResponseStatus.value = true
                        }
                    }
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                timerJob?.cancel()
                connectionFailure.value = true
            }
        })
    }

    // Timer para verificar tempo de resposta, roda a cada 15 segundos para dizer pro usuário que a função ainda está executando
    fun startTimer() {
        timerJob = viewModelScope.launch {
            delay(15000) // Cada 1000 equivale a 1 segundo
            timerStatus.value = true
        }
    }

}