# Base Apps Sankhya

## Base para apps utilizando as APIs do Sankhya

>Esse projeto serve de base para apps Android com versão 9 ou superior, ele vem com funcionalidade básica utilizando login e senha criptografada, é totalmente desenvolvido em Kotlin conforme orientado pela Google.
>
>Para funcionamento básico, deve ser informado o servidor do Sankhya e deve ser criada uma View de nome AD_VIEWUSUARIO com os campos CODUSU e NOMEUSU da TSIUSU, ou o código pode ser modificado para que atenda as necessidades de cada empresa.

### Funcionalidades

>Para uma completa funcionalidade do app, utilizo a biblioteca do Retrofit2 para conexão com o servidor e coleta dos dados em formato json, o Room para salvar alguns dados de maneira local, o Hilt para injeção de dependência e o Picasso para manipulação de imagens, além das bibliotecas padrões do Android.


<table><tr>
<td><img src="https://github.com/lucasfpastre/ImageRepository/blob/main/Base/Base_Tela_Login.png" width=300 alt="Imagem da tela inicial do aplicativo exibindo usuário, senha, botão para configurar o servidor e uma chave para gravar o usuário ou não" title="Tela de Login" border=/></td>
<td><img src="https://github.com/lucasfpastre/ImageRepository/blob/main/Base/Base_Tela_Inicial_Teste.png" width=300 alt="Tela inicial exibindo o usuário Teste" title="Tela Inicial"/></td>
<td><img src="https://github.com/lucasfpastre/ImageRepository/blob/main/Base/Base_Tela_Inicial_Lucas.png" width=300 alt="Tela inicial exibindo o usuário Lucas Pastre" title="Tela Inicial"/></td>
<td><img src="https://github.com/lucasfpastre/ImageRepository/blob/main/Base/Base_Tela_Inicial_Menu.png" width=300 alt="Tela inicial exibindo o menu com opções: Home, Gallery e Slideshow, e uma foto do usuário conectado, Lucas Pastre e sua foto" title="Menu do App"/></td>
<td><img src="https://github.com/lucasfpastre/ImageRepository/blob/main/Base/Base_Tela_Configurações.png" width=300 alt="Tela de configurações com um botão de voltar" title="Tela de Configurações"/></td>
</tr></table>

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

Esse código não é um requisito para criação de apps, ele é uma base para quem tem interesse em fazer e não tem ideia de como começar.
