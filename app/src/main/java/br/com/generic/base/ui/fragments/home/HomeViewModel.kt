package br.com.generic.base.ui.fragments.home

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
import br.com.generic.base.models.view.response.generic.GenericResponseNames
import br.com.generic.base.models.view.response.generic.multi.MultiResponseBody
import br.com.generic.base.models.view.response.generic.single.SingleResponseBody
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
class HomeViewModel @Inject constructor(private val requestRepo : RequestRepository) : ViewModel() {

    val serverData : LiveData<ServerData> = requestRepo.server.getServer().asLiveData()
    val loginResponse = MutableLiveData<LoginResponse?>()
    val responseBody = MutableLiveData<ResponseBody?>()
    val sessionId = MutableLiveData<JSessionID?>()
    val timerStatus = MutableLiveData(false)
    val logoutStatus = MutableLiveData(false)
    val requestStatus = MutableLiveData(false)
    val connectionFailure = MutableLiveData(false)
    val serverStatus = MutableLiveData(false)
    val connectionStatusDown = MutableLiveData(false)
    val fragmentViewLoaded = MutableLiveData(false)
    val multiViewResponseBody = MutableLiveData<MultiResponseBody>() // Aqui pode ser trocado de tela para tela
    val singleViewResponseBody = MutableLiveData<SingleResponseBody>() // Aqui pode ser trocado de tela para tela
    var viewArray = ArrayList<GenericResponseNames?>() // Aqui pode ser trocado de tela para tela
    var cookieList : List<String?> = ArrayList()
    var responseData : String = ""
    var failureMessage : String = ""
    var timerJob: Job? = null

    // Requisição de Login
    fun getSessionId(serviceRequest: ServiceRequest, newUrl: String) {
        requestRepo.remote.getSessionId(serviceRequest, newUrl).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                val gson = Gson()
                if (response.body().toString().contains("jsessionid")) {
                    cookieList = response.headers().values("Set-Cookie")
                    loginResponse.value = gson.fromJson(gson.toJson(response.body()), LoginResponse::class.java)
                    responseBody.value = loginResponse.value?.responseBody
                    sessionId.value = responseBody.value?.jsessionid
                    responseData = sessionId.value.toString()
                    requestStatus.value = true
                    timerJob?.cancel()
                } else {
                    connectionFailure.value = true
                    failureMessage = "Usuário ou senha inválidos"
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                failureMessage = t.toString()
                connectionFailure.value = true
            }
        })
    }

    fun getViewData(viewRequest: ViewRequest, newUrl: String, cookie: String) {
        requestRepo.remote.getViewData(viewRequest, newUrl, cookie).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.body().toString().contains("Não autorizado")) {
                    connectionStatusDown.value = true
                } else {
                    timerJob?.cancel()
                    val gson = Gson()
                    val counter = countMatches(response.body().toString(), "CODUSU")
                    if (counter > 1) {
                        multiViewResponseBody.value = gson.fromJson(gson.toJson(response.body()), MultiResponseBody::class.java)
                        viewArray = multiViewResponseBody.value?.responseBody?.records?.viewNames!!
                        fragmentViewLoaded.value = true
                    } else {
                        viewArray = ArrayList()
                        if (counter == 1) {
                            singleViewResponseBody.value = gson.fromJson(gson.toJson(response.body()), SingleResponseBody::class.java)
                            viewArray.add(singleViewResponseBody.value?.responseBody?.records?.viewNames!!)
                            fragmentViewLoaded.value = true
                        } else {
                            if (response.body().toString().contains("situação de concorrência")) {
                                failureMessage = ""
                                timerJob?.cancel()
                                connectionFailure.value = true
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                failureMessage = t.toString()
                connectionFailure.value = true
            }
        })
    }

    fun logoutSession(serviceRequest: ServiceRequest, newUrl: String, cookie: String) {
        requestRepo.remote.logoutSession(serviceRequest, newUrl, cookie).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.body().toString().contains("logout")) {
                    logoutStatus.value = true
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                failureMessage = t.toString()
                connectionFailure.value = true
            }
        })
    }

    // Timer para verificar tempo de resposta
    fun startTimer() {
        timerJob = viewModelScope.launch {
            delay(15000)
            timerStatus.value = true
        }
    }

}