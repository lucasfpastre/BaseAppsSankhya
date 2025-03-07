package br.com.generic.base.ui.activities.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.webkit.URLUtil
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import br.com.generic.base.BuildConfig
import br.com.generic.base.R
import br.com.generic.base.data.extensions.countMatches
import br.com.generic.base.data.extensions.createAlertDialog
import br.com.generic.base.data.extensions.hideSoftKeyboard
import br.com.generic.base.data.extensions.loginFailed
import br.com.generic.base.data.extensions.userConnectionCode
import br.com.generic.base.data.extensions.userExhibitionName
import br.com.generic.base.databinding.ActivityLoginBinding
import br.com.generic.base.ui.activities.loading.LoadingActivity
import br.com.generic.base.utils.Constants.Companion.REQUEST_CODE
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigInteger
import java.security.MessageDigest

// Lógica da tela de login
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater)} // Acesso aos componentes do layout
    private val loginViewModel: LoginViewModel by viewModels() // Instância do ViewModel para controles assíncronos
    private var updateText = false
    private var lastUserName = ""
    private var lastText = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.cvConfigure.isVisible = false
        binding.tvVersion.text = "Versão: ${BuildConfig.VERSION_NAME}"

        // Remover as quatro linhas seguintes após o lançamento
        //binding.btConfigure.isVisible = false
        //binding.tiConfigure.setText(BASE_URL)
        //saveServer()
        //binding.tiLoginEditUser.setText("USUARIO")

        requestPermissions()
        loadServerData()
        insertListeners()
        insertObservers()

    }

    override fun onResume() {
        super.onResume()
        binding.tvResponse.text = loginFailed.ifEmpty { "" }
    }
    // Carrega o servidor se já tiver sido configurado
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

    /**
     * Insere os observadores de botões, gestos e ações de tela
     */
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

    /**
     * Controle dos observadores de funções assíncronas
     */
    private fun insertObservers() {
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
    }

    /**
     * Função que salva o usuário no banco local
     */
    private fun saveMonitorUser() {
        if (lastUserName == "" || lastUserName != binding.tiLoginEditUser.text.toString()) {
            loginViewModel.saveUserName(binding.tiLoginEditUser.text.toString().trim())
        }
        startLogin()
    }

    /**
     * Valida se exibe o botão de conectar
     */
    private fun validateLogin(): Boolean {
        if (loginViewModel.loadedServerData.value == false) {
            binding.tvResponse.text = getString(R.string.text_set_server)
        } else {
            binding.tvResponse.text = ""
        }
        return (loginViewModel.passwordFilled.value == true && loginViewModel.passwordFilled.value == true && loginViewModel.loadedServerData.value == true)
    }

    /**
     * Exibe mensagem em tela
     * @param message
     * @return Exibe mensagem na tela como snack bar
     */
    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Executa o acesso e vai pra tela de carregamento
     */
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

    /**
     * Salva o servidor no banco local
     */
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

    /**
     * Solicia as permissões do aplicativo
     */
    private fun requestPermissions() {
        var permissions = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions += Manifest.permission.POST_NOTIFICATIONS
        }
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsToRequest.isNotEmpty()) {
            createTimerDialog(
                this@LoginActivity,
                "Permissões",
                "Aceite as permissões",
                5000L
            ) {
                ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), REQUEST_CODE)
            }
        }
    }

    /**
     * Faz tratamentos ao final do aceite das permissões
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            for ((index, _) in permissions.withIndex()) {
                if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                    createAlertDialog(this@LoginActivity, "Permissões Recusadas", "Escolha o que fazer ao recusar as permissões") {
                        finish()
                    }
                }
            }
        }
    }

    /**
     * Caixa de diálogo com timer que libera o botão ok após esgotar o tempo
     * @param context Contexto
     * @param title Título da caixa de diálogo
     * @param message Mensagem a ser exibida na caixa de diálogo
     * @param time tempo que o botão de confirmar ficará bloqueado em milisegundos
     * @param resultBoolean Sempre retorna verdadeiro
     * @return Retorna a caixa de diálogo com os parâmetros informados
     */
    private fun createTimerDialog(context: Context, title: String, message: String, time: Long, resultBoolean: (Boolean) -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)

        // Criar um layout LinearLayout para adicionar o countdownTextView
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 20, 50, 20) // Ajuste o padding conforme necessário

        // Adiciona o TextView para o contador e alinha-o à direita
        val countdownTextView = TextView(context)
        countdownTextView.text = (time / 1000).toString() // Valor inicial do contador
        countdownTextView.textSize = 20f
        countdownTextView.textAlignment = TextView.TEXT_ALIGNMENT_VIEW_END // Alinha o texto à direita

        // Define o layout params para alinhar à direita no layout
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 0, 36, 0) // Margem de 36dp à direita
        layoutParams.gravity = Gravity.END
        countdownTextView.layoutParams = layoutParams

        layout.addView(countdownTextView)

        // Define o layout customizado no AlertDialog
        builder.setView(layout)

        // Configuração do botão positivo do diálogo
        builder.setPositiveButton(R.string.ok_text) { dialog, _ ->
            resultBoolean(true)
            dialog.dismiss()
        }
        builder.setOnDismissListener { _ -> resultBoolean(false) }

        val dialog = builder.create()

        // Iniciar o CountDownTimer quando o diálogo for exibido
        dialog.setOnShowListener {
            val positiveButton: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.isEnabled = false

            // Iniciar o CountDownTimer
            object : CountDownTimer(time, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    countdownTextView.text = (millisUntilFinished / 1000).toString()
                }

                override fun onFinish() {
                    positiveButton.isEnabled = true
                    countdownTextView.text = "0"
                }
            }.start()
        }

        dialog.show()
    }
}