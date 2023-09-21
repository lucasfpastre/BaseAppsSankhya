package br.com.generic.base.ui.activities.loading

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.generic.base.R
import br.com.generic.base.data.extensions.cookie
import br.com.generic.base.data.extensions.getUserQuery
import br.com.generic.base.data.extensions.loginFailed
import br.com.generic.base.data.extensions.userCode
import br.com.generic.base.data.extensions.userConnectionCode
import br.com.generic.base.data.extensions.userExhibitionName
import br.com.generic.base.databinding.ActivityLoadingBinding
import br.com.generic.base.models.service.request.LoginCode
import br.com.generic.base.models.service.request.LoginConnection
import br.com.generic.base.models.service.request.LoginUser
import br.com.generic.base.models.service.request.ServiceBody
import br.com.generic.base.models.service.request.ServiceRequest
import br.com.generic.base.models.view.request.ViewData
import br.com.generic.base.models.view.request.ViewFieldName
import br.com.generic.base.models.view.request.ViewFields
import br.com.generic.base.models.view.request.ViewRequest
import br.com.generic.base.models.view.request.ViewRequestBody
import br.com.generic.base.ui.activities.main.MainActivity
import br.com.generic.base.utils.Constants.Companion.GET_QUERY
import br.com.generic.base.utils.Constants.Companion.SESSION_ID
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadingActivity : AppCompatActivity()  {

    private val binding by lazy { ActivityLoadingBinding.inflate(layoutInflater)}
    private val loadingViewModel: LoadingViewModel by viewModels()
    private var serverUrl = ""
    private var sessionId = ""
    private var getView = ""
    private var userArray = ArrayList<ViewFieldName>()
    private var userFields = ViewFields(userArray)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        insertListeners()
        binding.tvViewResponse.text = getString(R.string.text_wait_validation)

    }

    // Insere os observadores para continuar o programa conforme as etapas são completadas
    private fun insertListeners() {

        // Verifica se o servidor foi preenchido e se retornou corretamente chama a função de preencher as constantes
        loadingViewModel.serverData.observe(this) { serverData ->
            if (serverData.serverData.isNotEmpty()) {
                serverUrl = serverData.serverData
                setConstants()
            }
        }

        // Se conseguiu a conexão com o servidor, chama a view para puxar os dados do usuário e validar se a chave é válida
        loadingViewModel.requestStatus.observe(this) {connectionStatus ->
            if (connectionStatus) {
                if (loadingViewModel.responseData.length == 55) {
                    if (loadingViewModel.responseData.substring(13,14) == "=") {
                        cookie = loadingViewModel.cookieList[0].toString()
                        getUserView()
                    }
                }
            }
        }

        // Se tiver uma resposta positiva do servidor, preencho as constantes do programa para exibição dos dados do usuário
        loadingViewModel.viewResponseStatus.observe(this) {viewResponse ->
            if (viewResponse) {
                if (loadingViewModel.userResponseNames.value?.userCode?.viewField?.isNotEmpty() == true) {
                    userCode = loadingViewModel.userResponseNames.value!!.userCode?.viewField.toString()
                    userExhibitionName = loadingViewModel.userResponseNames.value!!.userName?.viewField.toString()
                    binding.ivViewResponse.setImageResource(R.drawable.ic_ok)
                    binding.tvViewResponse.text = getString(R.string.text_user_ok)
                    loadingViewModel.startApplication.value = true
                } else {
                    // Se a conexão falhar ou não estiver liberado ao usuário, retorno a tela inicial
                    binding.ivViewResponse.setImageResource(R.drawable.ic_stop)
                    binding.tvViewResponse.text = loadingViewModel.failureMessage
                    loadingViewModel.viewResponseStatus.value = false
                    loadingViewModel.connectionFailure.value = true
                }
            }
        }

        // Se tudo estiver correto, inicio a parte funcional da aplicação
        loadingViewModel.startApplication.observe(this) {
            if (it) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Se a conexão cair, inicio o timer e tento uma reconexão
        loadingViewModel.connectionStatusDown.observe(this) {
            if (it) {
                showSnackBar("Reiniciando Conexão, aguarde...")
                loadingViewModel.startTimer()
                loginUser()
            }
        }

        // Se o usuário ou senha estiver incorreto retorna a tela inicial
        loadingViewModel.connectionFailure.observe(this) {
            if (it) {
                binding.ivViewResponse.setImageResource(R.drawable.ic_stop)
                binding.tvViewResponse.text = loadingViewModel.failureMessage
                finish()
            }
        }

        // Reinicio o timer se não houve resposta no tempo determinado
        loadingViewModel.timerStatus.observe(this) {
            if (it) {
                showSnackBar(getString(R.string.text_wait_timer))
                loadingViewModel.timerStatus.value = false
                loadingViewModel.startTimer()
            }
        }
    }

    // Defino as constantes dessa etapa
    private fun setConstants() {
        sessionId = serverUrl + SESSION_ID
        getView = serverUrl + GET_QUERY

        userArray.add(ViewFieldName("CODUSU"))
        userArray.add(ViewFieldName("NOMEUSU"))
        userFields = ViewFields(userArray)

        loginUser()
    }

    // Monto a chamada de login para pegar a chave
    private fun loginUser() {
        // Se por algum motivo, o usuário e senha estiverem vazis, eu volto a tela inicial
        if (userExhibitionName.isEmpty() || userConnectionCode.isEmpty()) {
            loginFailed = getString(R.string.text_user_empty)
            finish()
        }
        val bodyData = ServiceBody(LoginUser(userExhibitionName), LoginCode(userConnectionCode), LoginConnection("S"))
        val serviceRequest = ServiceRequest("MobileLoginSP.login",bodyData)
        loadingViewModel.getSessionId(serviceRequest, sessionId)
        loadingViewModel.startTimer()
        loginFailed = ""

    }

    // Monto a chamada da view
    private fun getUserView() {
        val serviceName = "CRUDServiceProvider.loadView"
        val viewRequestBody = ViewRequestBody("AD_VIEWUSUARIO", "", ViewFieldName(getUserQuery()), userFields)
        val viewData = ViewData(viewRequestBody)
        val viewRequest = ViewRequest(serviceName, viewData)
        loadingViewModel.getViewData(viewRequest, getView, cookie)
    }

    // Exibição genérica de mensagem na tela
    private fun showSnackBar(message: String) {
        loadingViewModel.timerJob?.cancel()
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

}