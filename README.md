# Base Apps Sankhya

## Base para apps utilizando as APIs do Sankhya

>Esse projeto serve de base para apps Android com versão 9 ou superior, ele vem com funcionalidade básica utilizando login e senha criptografada, é totalmente desenvolvido em Kotlin conforme orientado pela Google.
>
>Para funcionamento básico, deve ser informado o servidor do Sankhya e deve ser criada uma View de nome AD_VIEWUSUARIO com os campos CODUSU e NOMEUSU da TSIUSU, ou o código pode ser modificado para que atenda as necessidades de cada empresa.

### Funcionalidades

>Para uma completa funcionalidade do app, utilizo a biblioteca do Retrofit2 para conexão com o servidor e coleta dos dados em formato json, o Room para salvar alguns dados de maneira local, o Hilt para injeção de dependência e o Picasso para manipulação de imagens, além das bibliotecas padrões do Android.

### Links para as Bibliotecas

>[Retrofit2](https://square.github.io/retrofit/)
>
>[Room](https://developer.android.com/training/data-storage/room)
>
>[Hilt](https://dagger.dev/hilt/gradle-setup)
>
>[Picasso](https://github.com/square/picasso)
>
>[Coroutines](https://developer.android.com/kotlin/coroutines?hl=pt-br)
>
>[LifeCycle](https://developer.android.com/topic/libraries/architecture/lifecycle)

Esse código não é um requisito para criação de apps, ele é uma base para quem interesse em fazer e não tem ideia de como começar.