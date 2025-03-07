package br.com.generic.base.ui.activities.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import br.com.generic.base.data.repository.RequestRepository
import br.com.generic.base.models.server.ServerData
import br.com.generic.base.models.user.RecordedUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val requestRepo : RequestRepository) : ViewModel() {

    val serverData: LiveData<ServerData?> = requestRepo.server.getServer().asLiveData()
    val recordedUserData: LiveData<RecordedUserData?> = requestRepo.recordedUser.getMonitorUser().asLiveData()
    val userFilled = MutableLiveData(false)
    val passwordFilled = MutableLiveData(false)
    val loadedServerData = MutableLiveData(false)

    /**
     * Função da chamada assíncrona de validação da url
     */
    fun redirectUrl(url: String? ) {
        getRedirectUrl(url)
    }

    /**
     * Validação da url
     * @param url URL a ser validada
     */
    private fun getRedirectUrl(url: String?) = viewModelScope.launch(Dispatchers.IO) {
        var urlTmp: URL? = null
        var redUrl: String? = ""
        var connection: HttpURLConnection? = null
        try {
            urlTmp = URL(url)
        } catch (e1: MalformedURLException) {
            e1.printStackTrace()
        }
        try {
            connection = urlTmp!!.openConnection() as HttpURLConnection
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            connection!!.responseCode
        } catch (e: IOException) {
            e.printStackTrace()
        }
        redUrl = connection!!.url.toString()
        connection.disconnect()
        saveServerData(redUrl)
    }

    /**
     * Verifica se grava ou atualiza o servidor no banco local
     * @param serverName String com o servidor a ser salvo
     */
    private fun saveServerData(serverName: String) {
        if (loadedServerData.value == true) {
            updateServerData(serverName)
        } else {
            saveNewServerData(serverName)
        }
    }

    /**
     * Atualiza o servidor no banco local
     * @param serverName String com o servidor novo
     */
    private fun updateServerData(serverName: String) = viewModelScope.launch(Dispatchers.IO) {
        requestRepo.server.updateServerData(serverName)
    }

    /**
     * Grava o servidor no banco local
     * @param serverName String com o servidor novo
     */
    private fun saveNewServerData(serverName: String) = viewModelScope.launch(Dispatchers.IO) {
        val newServerData = ServerData(1,serverName)
        requestRepo.server.insertServer(newServerData)
    }

    /**
     * Faz a chamada assíncrona de salvar o usuário no servidor local e prepara o objeto a ser salvo
     * @param monitorUserName nome do usuário a ser salvo
     */
    fun saveUserName(monitorUserName: String) {
        val newMonitorUser = RecordedUserData(1,monitorUserName)
        insertNewUserName(newMonitorUser)
    }

    /**
     * Grava o usuário no banco local
     * @param monitorUserData Objeto com o usuário a ser gravado
     */
    private fun insertNewUserName(monitorUserData : RecordedUserData) = viewModelScope.launch(Dispatchers.IO) {
        requestRepo.recordedUser.insertUser(monitorUserData)
    }

}