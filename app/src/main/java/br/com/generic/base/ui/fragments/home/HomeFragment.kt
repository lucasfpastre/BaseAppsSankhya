package br.com.generic.base.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.generic.base.R
import br.com.generic.base.data.extensions.cookie
import br.com.generic.base.data.extensions.createQuestionDialog
import br.com.generic.base.data.extensions.loginFailed
import br.com.generic.base.data.extensions.serverURL
import br.com.generic.base.data.extensions.userConnectionCode
import br.com.generic.base.data.extensions.userExhibitionName
import br.com.generic.base.databinding.FragmentHomeBinding
import br.com.generic.base.models.procedure.request.ProcedureBody
import br.com.generic.base.models.procedure.request.ProcedureCall
import br.com.generic.base.models.procedure.request.ProcedureFields
import br.com.generic.base.models.procedure.request.ProcedureParam
import br.com.generic.base.models.procedure.request.ProcedureParams
import br.com.generic.base.models.procedure.request.ProcedureRequestBody
import br.com.generic.base.models.procedure.request.ProcedureRow
import br.com.generic.base.models.procedure.request.ProcedureRows
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
import br.com.generic.base.utils.Constants.Companion.EXIT_SESSION
import br.com.generic.base.utils.Constants.Companion.GET_QUERY
import br.com.generic.base.utils.Constants.Companion.GET_STP
import br.com.generic.base.utils.Constants.Companion.SESSION_ID
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Array

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var fragmentViewModel : HomeViewModel
    private lateinit var fragmentAdapter : HomeAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var sessionId = ""
    private var logoutSession = ""
    private var getView = ""
    private var getProcedure = ""
    private var viewArray = ArrayList<ViewFieldName>()
    private var viewFields = ViewFields(viewArray)
    private var updating = false
    private var exiting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Controla a ação do botão de voltar para sair do app fazendo logoff da sessão aberta
         */
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                createQuestionDialog(requireContext(), "Sair do App", "Deseja realmente sair?") { result ->
                    if (result) {
                        exiting = true
                        binding.srlHome.isRefreshing = false
                        logoutUser()
                    }
                }
            }
        })

        fragmentViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        fragmentAdapter = HomeAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Insere os observadores
        insertListeners()
        insertObservers()
        binding.rvHome.adapter = fragmentAdapter
    }

    /**
     * Insere os observadores de botões, gestos e ações de tela
     */
    private fun insertListeners() {

        /**
         * Controle do gesto de recarregar
         */
        binding.srlHome.setOnRefreshListener {
            if (!updating && !exiting) {
                getViewData()
            }
        }

        /**
         * Controle do botão de chamar procedure
         */
        binding.btCallProcedure.setOnClickListener {
            callProcedure()
        }
    }

    /**
     * Insere observadores de variáveis assíncronas
     */
    private fun insertObservers() {

        /**
         * Verifica se carregou os dados da API para fazer as chamadas
         */
        fragmentViewModel.serverData.observe(viewLifecycleOwner) { serverData ->
            if (serverData.serverData.isNotEmpty()) {
                serverURL = serverData.serverData
                setConstants()
            }
        }

        /**
         * Se carregou a API o status avisa que devem ser inicializadas as constantes do programa para utilizar as rotinas
         */
        fragmentViewModel.requestStatus.observe(viewLifecycleOwner) {
            if (it) {
                if (fragmentViewModel.responseData.length == 55) {
                    if (fragmentViewModel.responseData.substring(13,14) == "=") {
                        cookie = fragmentViewModel.cookieList[0].toString()
                        updating = true
                        getViewData()
                    }
                }
            }
        }

        /**
         * Verifica se caiu e a conexão é reinicia
         */
        fragmentViewModel.connectionStatusDown.observe(viewLifecycleOwner) {
            if (it) {
                showSnackBar("Reiniciando conexão, aguarde...")
                fragmentViewModel.startTimer()
                updating = false
                loginUser()
            }
        }

        /**
         * Reinicio o timer se não houve resposta no tempo determinado
         */
        fragmentViewModel.timerStatus.observe(viewLifecycleOwner) {
            if (it) {
                showSnackBar(getString(R.string.text_wait_timer))
                fragmentViewModel.timerStatus.value = false
                fragmentViewModel.startTimer()
            }
        }

        /**
         * Verifica se falhou a conexão
         */
        fragmentViewModel.connectionFailure.observe(viewLifecycleOwner) {
            if (it) {
                showSnackBar(fragmentViewModel.failureMessage)
                binding.srlHome.isRefreshing = false
                updating = false
                fragmentAdapter.setUpItems(fragmentViewModel.viewArray)
            }
        }

        /**
         * Verifica se o usuário foi desconectado do servidor para sair do app
         */
        fragmentViewModel.logoutStatus.observe(viewLifecycleOwner) {
            if (it) {
                requireActivity().finishAffinity()
            }
        }

        /**
         * Ao retornar uma resposta positiva da chamada da view, retorno os dados
         */
        fragmentViewModel.fragmentViewLoaded.observe(viewLifecycleOwner) {
            if (it) {
                binding.srlHome.isRefreshing = false
                updating = false
                fragmentAdapter.setUpItems(fragmentViewModel.viewArray)
            }
        }

        /**
         * Em caso de retorno positivo da procedure, exibo a mensagem de retorno
         */
        fragmentViewModel.procedureReturn.observe(viewLifecycleOwner) {
            if (it) {
                showSnackBar(fragmentViewModel.procedureResponse.value.toString())
            }
        }
    }


    /**
     * Define as constantes da rotina
     */
    private fun setConstants() {
        sessionId = serverURL + SESSION_ID
        logoutSession = serverURL + EXIT_SESSION
        getView = serverURL + GET_QUERY
        getProcedure = serverURL + GET_STP
        viewArray = ArrayList()

        viewArray.add(ViewFieldName("CODUSU"))
        viewArray.add(ViewFieldName("NOMEUSU"))
        viewFields = ViewFields(viewArray)

        getViewData()
    }

    /**
     * Preparo a chamada da view
     */
    private fun getViewData() {
        if (!exiting) {
            val serviceName = "CRUDServiceProvider.loadView"
            val viewRequestBody = ViewRequestBody("AD_VIEWUSUARIO", "NOMEUSU", ViewFieldName(), viewFields)
            val viewData = ViewData(viewRequestBody)
            val viewRequest = ViewRequest(serviceName, viewData)
            fragmentViewModel.getViewData(viewRequest, getView, cookie)
            binding.srlHome.isRefreshing = true
        }
    }

    /**
     * Função que faz a chamada de login de usuário
     */
    private fun loginUser() {
        val bodyData = ServiceBody(LoginUser(userExhibitionName), LoginCode(userConnectionCode), LoginConnection("S"))
        val serviceRequest = ServiceRequest("MobileLoginSP.login",bodyData)
        fragmentViewModel.getSessionId(serviceRequest, sessionId)
        fragmentViewModel.startTimer()
        loginFailed = ""
    }

    /**
     * Preparo a chamada da procedure
     */
    private fun callProcedure() {
        val procedureParam = arrayListOf<ProcedureParam>()
        procedureParam.add(ProcedureParam("I","PARAMETROINT", "1"))
        procedureParam.add(ProcedureParam("T","PARAMETROTEXTO","TEXTO"))
        val procedureParams = ProcedureParams(procedureParam)
        val procedureFields = ProcedureFields("CAMPOTABELA","1")
        val procedureRows = ProcedureRows(arrayListOf(ProcedureRow(arrayListOf(procedureFields))))
        val procedureCall = ProcedureCall("NUMERO PROCEDURE","NOME_PROCEDURE","NOMETABELA","NONE",procedureRows,procedureParams)
        val procedureRequestBody = ProcedureRequestBody(procedureCall)
        val procedureBody = ProcedureBody("ActionButtonsSP.executeSTP",procedureRequestBody)
        fragmentViewModel.procedureCall(procedureBody, getProcedure, cookie)
    }

    /**
     * Função que faz a chamada de logout do usuário
     */
    private fun logoutUser() {
        val bodyData = ServiceBody(LoginUser(userExhibitionName), LoginCode(userConnectionCode), LoginConnection("N"))
        val serviceRequest = ServiceRequest("MobileLoginSP.logout", bodyData)
        fragmentViewModel.logoutSession(serviceRequest, logoutSession, cookie)
    }

    /**
     * Exibe mensagem em tela
     * @param message
     * @return Exibe mensagem na tela como snack bar
     */
    private fun showSnackBar(message: String) {
        fragmentViewModel.timerJob?.cancel()
        if (message.isNotEmpty()) {
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
        }
    }

    /**
     * Destrói a view para liberar memória
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}