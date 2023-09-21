package br.com.generic.base.ui.activities.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.webkit.URLUtil
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import br.com.generic.base.BuildConfig
import br.com.generic.base.R
import br.com.generic.base.data.extensions.countMatches
import br.com.generic.base.data.extensions.hideSoftKeyboard
import br.com.generic.base.data.extensions.loginFailed
import br.com.generic.base.data.extensions.userConnectionCode
import br.com.generic.base.data.extensions.userExhibitionName
import br.com.generic.base.databinding.ActivityLoginBinding
import br.com.generic.base.ui.activities.loading.LoadingActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigInteger
import java.security.MessageDigest

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater)}
    private val loginViewModel: LoginViewModel by viewModels()
    private var updateText = false
    private var lastUserName = ""
    private var lastText = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.cvConfigure.isVisible = false
        binding.tvVersion.text = "Versão: ${BuildConfig.VERSION_NAME}"

        // Remover as quatro linhas seguintes após o lançamento
        //binding.btConfigure.isVisible = false
        //binding.tiConfigure.setText(BASE_URL)
        //saveServer()
        //binding.tiLoginEditUser.setText("LUCAS PASTRE")

        loadServerData()
        insertListeners()

    }

    override fun onResume() {
        super.onResume()
        binding.tvResponse.text = loginFailed.ifEmpty { "" }
    }

    // Carrega o servidor se já houver sido configurado
    @SuppressLint("SetTextI18n")
    private fun loadServerData() {
        loginViewModel.serverData.observe(this) { serverData ->
            if (serverData?.serverData?.isNotEmpty() == true) {
                binding.tvResponse.text = "Usando ${serverData.serverData} como servidor"
                loginViewModel.loadedServerData.value = true
            }
        }
        loginViewModel.recordedUserData.observe(this) {userData ->
            if (userData?.userName?.isNotEmpty() == true) {
                lastUserName = userData.userName
                binding.tiLoginEditUser.setText(lastUserName)
                binding.scSaveUser.isChecked = true
            }
        }
    }

    //Insere os observadores
    private fun insertListeners() {

        // Abre a configuração do servidor
        binding.btConfigure.setOnClickListener {
            if (loginViewModel.serverData.value?.serverData?.isNotEmpty() == true) {
                binding.tiConfigure.setText(loginViewModel.serverData.value?.serverData.toString())
            }
            if (binding.cvConfigure.isVisible) {
                binding.cvConfigure.isVisible = false
                binding.tiLoginEditUser.requestFocus()
            } else {
                binding.cvConfigure.isVisible = true
                binding.tiConfigure.requestFocus()
            }
        }

        // Salva a configuração do servidor
        binding.btSaveServer.setOnClickListener {
            if (binding.tiConfigure.text?.isNotEmpty() == true) {
                saveServer()
            }
        }

        binding.btServerCancel.setOnClickListener {
            binding.tiConfigure.hideSoftKeyboard()
            binding.cvConfigure.isVisible = false
            binding.tiLoginEditUser.requestFocus()
        }

        // Controle de validação do campo usuário
        binding.tiLoginEditUser.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString() != lastText) {
                    lastText = p0.toString()
                    updateText = true
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                loginViewModel.userFilled.value = (binding.tiLoginEditUser.text?.length!! > 0)
            }

            override fun afterTextChanged(p0: Editable?) {
                if (updateText && lastText != p0.toString()) {
                    val text = binding.tiLoginEditUser.text?.toString() ?: ""
                    binding.tiLoginEditUser.setText(text.uppercase())
                    updateText = false
                    if (p0 != null) {
                        binding.tiLoginEditUser.setSelection(p0.length)
                    }
                } else {
                    updateText = false
                }
            }
        })

        // Controle de validação do campo senha
        binding.tiLoginEditPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                loginViewModel.passwordFilled.value =
                    (binding.tiLoginEditPassword.text?.length!! > 0)
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        // Verifica se usuário foi preenchido para ativar parte do validador de exibição do botão de conectar
        loginViewModel.userFilled.observe(this) {
            binding.btLogin.isVisible = (validateLogin())
        }

        // Verifica se senha foi preenchida para ativar parte do validador de exibição do botão de conectar
        loginViewModel.passwordFilled.observe(this) {
            binding.btLogin.isVisible = (validateLogin())
        }

        // Verifica se o servidor está configurado e salvo para exibir o botão de conectar
        loginViewModel.loadedServerData.observe(this) { serverFilled ->
            if (serverFilled) {
                binding.tiConfigure.setText(loginViewModel.serverData.value?.serverData.toString())
            }
        }

        // Observador caso o campo usuário tenha recebido uma tecla Enter
        binding.tiLoginEditUser.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    val newText = binding.tiLoginEditUser.text.toString()
                    binding.tiLoginEditUser.setText(newText.uppercase())
                    return true
                }
                return false
            }
        })

        // Observador caso o campo senha tenha recebido uma tecla enter
        binding.tiLoginEditPassword.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && validateLogin()) {
                    startLogin()
                    return true
                }
                return false
            }
        })

        // Observador para o botão conectar
        binding.btLogin.setOnClickListener {
            if (binding.scSaveUser.isChecked) {
                saveMonitorUser()
            } else {
                startLogin()
            }
        }
    }

    private fun saveMonitorUser() {
        if (lastUserName == "" || lastUserName != binding.tiLoginEditUser.text.toString()) {
            loginViewModel.saveUserName(binding.tiLoginEditUser.text.toString().trim())
        }
        startLogin()
    }

    // Valida se exibe o botão de conectar
    private fun validateLogin(): Boolean {
        if (loginViewModel.loadedServerData.value == false) {
            binding.tvResponse.text = getString(R.string.text_set_server)
        } else {
            binding.tvResponse.text = ""
        }
        return (loginViewModel.passwordFilled.value == true && loginViewModel.passwordFilled.value == true && loginViewModel.loadedServerData.value == true)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    // Executa o acesso e vai pra tela principal
    private fun startLogin() {
        val md = MessageDigest.getInstance("MD5")
        val loginUser = binding.tiLoginEditUser.text.toString().trim()
        val loginData = "$loginUser${binding.tiLoginEditPassword.text.toString().trim()}"
        val convertedData: String = BigInteger(1, md.digest(loginData.toByteArray())).toString(16).padStart(32, '0')
        binding.tiLoginEditPassword.setText("")
        val intent = Intent(this, LoadingActivity::class.java)
        userExhibitionName = loginUser
        userConnectionCode = convertedData
        startActivity(intent)
    }

    private fun saveServer() {
        val server = binding.tiConfigure.text.toString()
        val count = countMatches(server, "http")
        if (count > 0) {
            val validServer = URLUtil.isValidUrl(server)
            if (validServer) {
                loginViewModel.redirectUrl(server)
                binding.cvConfigure.isVisible = false
                binding.tiConfigure.hideSoftKeyboard()
                binding.tiLoginEditUser.requestFocus()
            } else {
                binding.tiConfigure.hideSoftKeyboard()
                showSnackBar("O servidor \"$server\" não é válido")
            }
        } else {
            binding.tiConfigure.hideSoftKeyboard()
            showSnackBar("\"$server\" não é um servidor")
        }
    }

}