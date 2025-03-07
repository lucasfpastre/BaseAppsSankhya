# Base Apps Sankhya

## Base para apps utilizando as APIs do Sankhya

>Esse projeto serve de base para apps Android com versão 9 ou superior, ele vem com funcionalidade básica utilizando login e senha criptografada, é totalmente desenvolvido em Kotlin conforme orientado pela Google.
>
>Para funcionamento básico, deve ser informado o servidor do Sankhya e deve ser criada uma View de nome AD_VIEWUSUARIO com os campos CODUSU e NOMEUSU da TSIUSU, ou o código pode ser modificado para que atenda as necessidades de cada empresa.

### Funcionalidades

>Para uma completa funcionalidade do app, utilizo a biblioteca do Retrofit2 para conexão com o servidor e coleta dos dados em formato json, o Room para salvar alguns dados de maneira local, o Hilt para injeção de dependência e o Picasso para manipulação de imagens, além das bibliotecas padrões do Android.

### Comentários pertinentes
* O acesso pode começar a falhar depois de um tempo por conta de certificação e como o Android lida com isso, para resolver esse problema é possível utilizar algumas configurações de segurança
> No AndroidManifest substitui o usesCleartextTraffic pelo networkSecurityConfig e criei o arquivo network_security_config dentro de res/xml/ nesse arquivo eu adiciono cleartexttrafic apenas aos domínios que estiverem marcados.
> Além disso, é possível adicionar os certificados para facilitar a comunicação, eu costumo colocar o certificado novo e o que está para vencer e faço atualizações conforme é necessário mudar, isso facilita pois não interfere no processo dos usuários e permite mais de um certificado.
* Em versões mais recentes do Android é necessário fazer algumas configurações para exibir notificações
> Fiz as alterações necessárias no AndroidManifest e criei um canal genérico como exemplo dentro das Extensions e na MainActivity. No LoginActivity foi adicionada a requisição de permissões para que tudo funcione nos padrões do Android.
* A partir do Android 15 é necessário fazer algumas alterações para exibir corretamente a tela sem que haja sobreposição da barra superior e dos botões inferiores
> Adicionado nos layouts das activities o fitsSystemWindows e no código o enableEdgeToEdge() dentro do OnCreate, isso permite que em versões futuras não ocorra sobreposição da barra e dos botões.

<table><tr>
<td><img src="https://github.com/lucasfpastre/ImageRepository/blob/main/Base/Base_Tela_Login.png" width=300 alt="Imagem da tela inicial do aplicativo exibindo usuário, senha, botão para configurar o servidor e uma chave para gravar o usuário ou não" title="Tela de Login" border=/></td>
<td><img src="https://github.com/lucasfpastre/ImageRepository/blob/main/Base/Base_Tela_Inicial_Teste.png" width=300 alt="Tela inicial exibindo o usuário Teste" title="Tela Inicial"/></td>
<td><img src="https://github.com/lucasfpastre/ImageRepository/blob/main/Base/Base_Tela_Inicial_Lucas.png" width=300 alt="Tela inicial exibindo o usuário Lucas Pastre" title="Tela Inicial"/></td>
<td><img src="https://github.com/lucasfpastre/ImageRepository/blob/main/Base/Base_Tela_Inicial_Menu.png" width=300 alt="Tela inicial exibindo o menu com opções: Home, Gallery e Slideshow, e uma foto do usuário conectado, Lucas Pastre e sua foto" title="Menu do App"/></td>
<td><img src="https://github.com/lucasfpastre/ImageRepository/blob/main/Base/Base_Tela_Configurações.png" width=300 alt="Tela de configurações com um botão de voltar" title="Tela de Configurações"/></td>
</tr></table>

### Alguns esclarecimentos

- Dentro da pasta [models](https://github.com/lucasfpastre/BaseAppsSankhya/tree/main/app/src/main/java/br/com/generic/base/models) estão as classes conforme retorno do json das APIs, é possível adicionar tudo dentro de um só arquivo, esse exemplo mantém separado para facilitar o endendimento.
- As classes dentro de [request](https://github.com/lucasfpastre/BaseAppsSankhya/tree/main/app/src/main/java/br/com/generic/base/models/procedure/request) das procedures podem ser utilizadas por qualquer procedure, é importante apenas usar a tipagem correta do servidor, o [response](https://github.com/lucasfpastre/BaseAppsSankhya/blob/main/app/src/main/java/br/com/generic/base/models/procedure/response) é sempre uma String, mas pode ser modificado para utilizar outros campos do json.  
- As classes dentro de [service](https://github.com/lucasfpastre/BaseAppsSankhya/tree/main/app/src/main/java/br/com/generic/base/models/service) são utilizadas apenas para o logon e logoff.
- As classes de [view](https://github.com/lucasfpastre/BaseAppsSankhya/tree/main/app/src/main/java/br/com/generic/base/models/view) podem ser utilizadas de 2 maneiras, o [request](https://github.com/lucasfpastre/BaseAppsSankhya/tree/main/app/src/main/java/br/com/generic/base/models/view/request) pode ser utilizado por qualquer view, apenas montando os campos necessários no array de ViewFields, mas o [response](https://github.com/lucasfpastre/BaseAppsSankhya/tree/main/app/src/main/java/br/com/generic/base/models/view/response) precisa ter uma versão para um registro ou múltiplos registros, isso é necessário por conta da forma que o json é retornado. Registro único retorna como um objeto, mas vários registros retornam como um array. A forma mais fácil de resolver isso é verificando a ocorrência de um nome de variável e se ocorrer mais de uma vez utilizar a classe única, e mais ocorrências usar a classe múltipla. Pode ser criada apenas uma classe com todas as possibilidades de campos ou uma para cada situação para facilitar a manutenção.

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
