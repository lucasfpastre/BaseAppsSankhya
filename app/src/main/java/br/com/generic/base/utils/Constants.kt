package br.com.generic.base.utils

/**
 * Classe centralizadora de constantes para chamadas de API, controle de banco de dados e afins
 */
class Constants {

    companion object {

        // URL básica para não ter problema ao iniciar o app, pode ser qualquer uma
        const val BASE_URL = "https://www.sankhya.com.br/"
        // Nome do database para salvar o servidor
        const val SERVER_DATA = "server_database"
        // Nome do database para salvar o usuário, pode ser modificado para guardar a senha também
        const val USER_DATA = "user_database"
        // Endpoint para login do usuário
        const val SESSION_ID = "/mge/service.sbr?serviceName=MobileLoginSP.login&outputType=json"
        // Endpoint para logout do usuário
        const val EXIT_SESSION = "/mge/service.sbr?serviceName=MobileLoginSP.logout&outputType=json"
        // Endpoint para carregar os dados de uma view
        const val GET_QUERY = "/mge/service.sbr?serviceName=CRUDServiceProvider.loadView&outputType=json"
        // Endpoint para executar uma stored procedure de um botão de ação
        const val GET_STP = "/mge/service.sbr?serviceName=ActionButtonsSP.executeSTP&outputType=json"
        // Endpoint para executar uma procedure java de um botão de ação
        const val GET_JAVA = "/mge/service.sbr?serviceName=ActionButtonsSP.executeJava&outputType=json"
        // Endpoint para fazer um insert no banco de dados
        const val INSERT_URL = "/mge/service.sbr?serviceName=DatasetSP.save&outputType=json"
        // Endpoint para abrir um mapa igual a Entrega Automática de Mapas
        const val START_MAP = "/mgewms/service.sbr?serviceName=ExecucaoTarefaImpressaSP.iniciaExecucaoMapaAutomatico&application=EntregaAutomaticaMapas&outputType=json&resourceID=br.com.sankhya.wms.EntregaAutomaticaMapas&mgeSession="
        // Endpoint para fechar um mapa igual a Entrega Automática de Mapas
        const val CLOSE_MAP = "/mgewms/service.sbr?serviceName=ExecucaoTarefaImpressaSP.encerrarMapaAutomatico&application=EntregaAutomaticaMapas&outputType=json&resourceID=br.com.sankhya.wms.EntregaAutomaticaMapas&mgeSession="
        // Utilizado para disparar uma notificação
        const val EVENT_CHANNEL_ID = "EVENT_CHANNEL_ID"
        // Requisição para solicitar permissões
        const val REQUEST_CODE = 123

    }
}