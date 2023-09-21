package br.com.generic.base.utils

class Constants {

    companion object {

        const val BASE_URL = "https://www.sankhya.com.br/"
        const val SERVER_DATA = "server_database"
        const val USER_DATA = "user_database"
        const val SESSION_ID = "service.sbr?serviceName=MobileLoginSP.login&outputType=json"
        const val EXIT_SESSION = "service.sbr?serviceName=MobileLoginSP.logout&outputType=json"
        const val GET_QUERY = "service.sbr?serviceName=CRUDServiceProvider.loadView&outputType=json"
        const val GET_STP = "service.sbr?serviceName=ActionButtonsSP.executeSTP&outputType=json"

    }
}