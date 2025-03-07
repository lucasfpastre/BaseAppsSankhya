package br.com.generic.base.data.extensions

import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import br.com.generic.base.R
import br.com.generic.base.utils.Constants.Companion.BASE_URL
import br.com.generic.base.utils.Constants.Companion.EVENT_CHANNEL_ID

var serverURL = BASE_URL
var loginFailed = ""
var cookie = ""
var userExhibitionName = ""
var userConnectionCode = ""
var userCode = ""
var isChannelCreated = false

/**
 * Cria um alerta simples exibindo um Ok para fechar, ao clicar fora da mensagem é considarado como um false, então é interessante fazer as tratativas de acordo
 * @param context Contexto para exibir a mensagem, geralmente é a atividade ou o contexto do fragmento
 * @param title Título da caixa de diálogo
 * @param message Mensagem que será exibida na caixa de diálogo
 * @param resultBoolean Verdadeiro se clicado no campo OK
 * @return Exibe uma caixa de diálogo com um botão OK para fechar
 */
fun createAlertDialog(context: Context, title: String, message: String, resultBoolean: (Boolean) -> Unit) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(R.string.ok_text) { _, _ -> resultBoolean(true) }
    builder.show()
}

/**
 * Cria um alerta com opções de sim e não para escolha de ação
 * @param context Contexto para exibir a mensagem, geralmente é a atividade ou o contexto do fragmento
 * @param message Mensagem que será exibida na caixa de diálogo
 * @param resultBoolean Verdadeiro se clicado no campo Sim, falso se clicado no campo Não
 * @return Exibe uma caixa de diálogo com opções Sim e Não para tratamento de opções
 */
fun createQuestionDialog(context: Context, title: String, message: String, resultBoolean: (Boolean) -> Unit) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton("Sim") { _, _ -> resultBoolean(true) }
    builder.setNegativeButton("Não") { _, _ -> resultBoolean(false) }
    builder.show()
}

/**
 * Usado para esconder o teclado
 */
fun View.hideSoftKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Verificador de resposta de view para determinar a classe que será utilizada
 * @param string Sentença a ser analizada
 * @param pattern Padrão que será verificado na sentença
 * @return Retorna quantidade de vezes que o padrão aparece na sentença
 */
fun countMatches(string: String, pattern: String): Int {
    var index = 0
    var count = 0

    while (true)
    {
        index = string.indexOf(pattern, index)
        index += if (index != -1)
        {
            count++
            pattern.length
        }
        else {
            return count
        }
    }
}

/**
 * Exemplo de WHERE para buscar na consulta de view, pode ser montado a partir de parâmetros na tela na própria atividade ou fragmento
 * @return Retorna o WHERE da consulta de view
 */
fun getUserQuery() : String {
    return "CODUSU = STP_GET_CODUSULOGADO FETCH FIRST 1 ROWS ONLY"
}

/**
 * Carrega a foto do usuário de acordo com o código passado
 * @param server Servidor do Sankhya
 * @param userCode Código do usuário
 * @return Retorna a URL completa com o usuario a ter a foto exibida
 */
fun loadUserLogo(server: String, userCode: String): String {
    return "${server}/mge/Usuario@FOTO@CODUSU=$userCode.dbimage"
}

/**
 * Cria um canal de notificação para exibir mensagem no Android
 * @context Contexto deve ser a activity ou o contexto do fragmento
 * @return Cria o canal para permitir notificações no android
 */
fun createChannel(context: Context) {
    val channel = NotificationChannel(EVENT_CHANNEL_ID, "AVISOS INTERNOS", NotificationManager.IMPORTANCE_DEFAULT)
    channel.description = "Notificações"
    channel.lightColor = Color.parseColor("#18A5E8")
    val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java)
    notificationManager!!.createNotificationChannel(channel)
    isChannelCreated = true
}

