# Vireya - Hydrocore Mobile

Esse projeto foi desenvolvido por alunos do Instituto J&F, do curso Germinare Tech. Vireya é um projeto que tem a proposta de simplificar e ajudar no gerenciamento de ETAs (Empresas de Tratamento de Água) e diminuir erros humanos. Trazendo soluções a problemas reais dentro de ETAs desde superdosagem ou subdosagem até o gerenciamento de estoque e geração de relatórios exigidos por lei e periódicos.

O aplicativo mobile é usado por **funcionários** da ETA, ondem eles podem executar algumas tarefas como:

- Utilizar a calculadora de químicos para calcular a dosagem dos produtos utilizados
- Utilizar a calculadora e potabilidade
- Visualizar dashboards que ajudam no seu trabalho
- Visualizar e concluir suas tarefas
- Baixar os relatórios legais
- Visualizar o estoque de produtos químicos
- Consultar chatbot IA personalizado
- Ver avisos diários
- Editar seu perfil

## Como configurar o projeto

Configurações necessárias para inicializar o projeto:

- **Java** (versão >= 21)
- **Git**

#### Para inicializar o projeto

```bash
$ git clone https://github.com/Vireya-Hydrocore/hydrocore-mobile

$ cd hydrocore-mobile/vireyaWeb

# após isso entre no Android Studio, rode o gradle e depois de o build no projeto
```

## Dependências do projeto

### Android

- **androidx.appcompat**: Fornece compatibilidade com versões antigas do Android e componentes base da interface.
- **com.google.android.material:material**: Biblioteca oficial do Material Design para Android (botões, inputs, toolbars, etc.).
- **androidx.constraintlayout:constraintlayout**: Gerenciador de layout flexível e performático.
- **androidx.lifecycle:livedata-ktx**: Facilita a criação de dados observáveis reativos, respeitando o ciclo de vida.
- **androidx.lifecycle:viewmodel-ktx**: Gerencia dados da UI de forma segura durante mudanças de configuração.
- **androidx.navigation:navigation-fragment**: Gerencia a navegação entre fragments dentro do app.
- **androidx.navigation:navigation-ui**: Integra a navegação com a UI (menus, toolbars, bottom navigation).
- **androidx.activity**: Oferece componentes modernos para o ciclo de vida de Activities.
- **androidx.annotation**: Fornece anotações de segurança de tipo, como @NonNull e @Nullable.

### Firebase

- **com.google.firebase:firebase-auth**: Gerencia autenticação de usuários com Firebase.
- **com.google.firebase:firebase-messaging**: Envia e recebe notificações push via Firebase Cloud Messaging.

### Networking

- **com.squareup.okhttp3:okhttp**: Cliente HTTP rápido e eficiente para requisições de rede.
- **com.squareup.retrofit2:retrofit**: Framework para consumo de APIs REST de forma simples.
- **com.squareup.retrofit2:converter-gson**: Converte JSON para objetos Kotlin/Java usando Gson.

### Banco de Dados

- **androidx.room:room-runtime**: Abstração sobre o SQLite para persistência local de dados.
- **androidx.room:room-compiler**: Gera automaticamente o código necessário para o Room funcionar.

### UI e Visualização

- **androidx.cardview:cardview**: Exibe conteúdo dentro de cartões com sombras e cantos arredondados.
- **com.github.bumptech.glide:glide**: Biblioteca para carregamento e cache de imagens.
- **com.github.PhilJay:MPAndroidChart**: Criação de gráficos (barras, linhas, pizza, etc.) de forma fácil.
- **com.github.sundeepk:compact-calendar-view**: Exibe um calendário compacto e personalizável na interface.

### PDF e Relatórios

- **com.itextpdf:itext7-core**: Criação e manipulação de arquivos PDF diretamente no app.

### Testes

- **junit**: Framework para testes unitários em Java.
- **androidx.test.ext:junit**: Integra o JUnit com o ambiente de testes do Android.
- **androidx.test.espresso:espresso-core**: Framework para testes automatizados de interface (UI)

### Linguagens e tecnologias Utilizadas

<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/android/android-original.svg" height="40" alt="android logo" />
<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" height="40" alt="java logo"  />
<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/gradle/gradle-original.svg" height="40" alt="gradle logo"  />

## Documentação

- [Adapters](./docs/adapters.md)


Este projeto está sob a licença <a href="https://opensource.org/licenses/MIT">MIT</a>. Veja o arquivo LICENSE para detalhes.
