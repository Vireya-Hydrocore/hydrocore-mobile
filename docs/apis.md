# Apis

O módulo de APIs do HydroCore é responsável pela comunicação entre o aplicativo mobile e o backend.

Todas as APIs seguem a estrutura do Retrofit 2, utilizando requisições HTTP tipadas, modelos de dados (Request/Response) e conversão automática de JSON via Gson.

O RetrofitClient centraliza a configuração de rede e aplica o padrão Singleton, garantindo uma única conexão configurada com cabeçalhos de autenticação e tratamento de datas.





# [RetrofitClient]

Pacote: com.vireya.hydrocore.core.network

Função: Gerencia a configuração e criação das instâncias de Retrofit utilizadas em todo o aplicativo.

Padrão aplicado: Singleton — garante que apenas uma instância de Retrofit seja criada e reutilizada.

Principais responsabilidades:

Configura a URL base (BASE_URL).

Cria um OkHttpClient com interceptor para incluir cabeçalhos de autenticação automaticamente.

Registra conversores Gson customizados para tratar diferentes formatos de data.

Expõe métodos específicos para obter APIs (ex: getRelatorioApi(), getTarefasApi()).

Métodos principais:

getRetrofit() → Retorna uma instância configurada do Retrofit.

getRelatorioApi() → Retorna uma instância de RelatorioApi.

getTarefasApi() → Retorna uma instância de TarefasApi.

Padrões implementados:

Singleton Pattern: para garantir uma única instância global.

Factory Pattern: para criar instâncias de APIs sob demanda.



C:\Users\vitorcarvalho-ieg\OneDrive - Instituto Germinare\Área de Trabalho\VireyaMobile\hydrocore-mobile\app\src\main\java\com\vireya\hydrocore\core\network\RetrofitClientMongo.java










## [ApiClient (Agenda API)](../app/src/main/java/com/vireya/hydrocore/agenda/api/ApiClient.java)

#### Pacote: com.vireya.hydrocore.agenda.api
#### Função: Realiza a comunicação com o backend para listar os avisos exibidos na agenda.
#### Modelo utilizado: Aviso
#### Métodos disponíveis:
- @GET("v1/avisos/listar") ---> Retorna Call<List<Aviso>>
#### Uso:
```java
CalculadoraApi api = RetrofitClient.getRetrofit(getContext()).create(CalculadoraApi.class);
api.getAvisos().enqueue(new Callback<List<Aviso>>() { ... });
```

## [CalculadoraApi](../app/src/main/java/com/vireya/hydrocore/calculadora/api/CalculadoraApi.java)

#### Pacote: com.vireya.hydrocore.calculadora.api
#### Função: Responsável por operações relacionadas aos cálculos de coagulação, floculação e listagem de produtos.
#### Modelos utilizados:
- CalculoCoagulacaoRequest
- CalculoFloculacaoRequest
- CalculoResponse
- ProdutoResponse
#### Endpoints:
- @POST("v1/calculadora/coagulacao") ---> Retorna Call<CalculoResponse>
- @POST("v1/calculadora/floculacao") ---> Retorna Call<CalculoResponse>
- @GET("v1/estoque/listar/produtos") ---> Retorna Call<List<ProdutoResponse>>
#### Uso:
```java
CalculadoraApi api = RetrofitClient.getRetrofit(getContext()).create(CalculadoraApi.class);
```

## [ApiService (Login API)](../app/src/main/java/com/vireya/hydrocore/entrada/api/ApiService.java)

#### Pacote: com.vireya.hydrocore.entrada.api;
#### Função: Realiza a comunicação com o backend de mongo para fazer funções de login do usuário
#### Modelo utilizado: ResponseBody
#### Métodos disponíveis:
- @POST("auth/login") ---> Retorna Call<ResponseBody>
- @POST("auth/forgot-password") ---> Retorna Call<ResponseBody>
- @POST("auth/reset-password") ---> Retorna Call<ResponseBody>
#### Uso:
```java
CalculadoraApi api = RetrofitClient.getRetrofit(getContext()).create(CalculadoraApi.class);
api.getAvisos().enqueue(new Callback<List<Aviso>>() { ... });
```

## [ApiService (Estoque API)](../app/src/main/java/com/vireya/hydrocore/estoque/api/ApiService.java)

#### Pacote: com.vireya.hydrocore.estoque.api;
#### Função: Realiza a comunicação com o backend de mongo para fazer funções de login do usuário
#### Modelo utilizado:
- ProdutoResponse
- Funcionario
#### Métodos disponíveis:
- @GET("/v1/funcionario/email") ---> Retorna Call<Funcionario>
- @GET("/v1/funcionario/{id}") ---> Retorna Call<Funcionario>
- @GET("/v1/estoque/listar/produtos") ---> Retorna Call<List<ProdutoResponse>>
#### Uso:
```java
CalculadoraApi api = RetrofitClient.getRetrofit(getContext()).create(CalculadoraApi.class);
api.getAvisos().enqueue(new Callback<List<Aviso>>() { ... });
```

## [RelatorioApi](../app/src/main/java/com/vireya/hydrocore/relatorio/api/RelatorioApi.java)

#### Pacote: com.vireya.hydrocore.relatorio.api;
#### Função: Realiza a comunicação com o backend de mongo para fazer funções de login do usuário
#### Modelos utilizado:
- RelatorioDetalhado
- RelatorioResumo
#### Métodos disponíveis:
- @GET("v1/eta/listar-relatorios/{idEta}") ---> Retorna Call<List<RelatorioResumo>>
- @GET("v1/eta/relatorio/{mes}/{ano}") ---> Retorna Call<RelatorioDetalhado>
#### Uso:
```java
CalculadoraApi api = RetrofitClient.getRetrofit(getContext()).create(CalculadoraApi.class);
api.getAvisos().enqueue(new Callback<List<Aviso>>() { ... });
```

## [TarefasApi](../app/src/main/java/com/vireya/hydrocore/tarefas/api/TarefasApi.java)

#### Pacote: com.vireya.hydrocore.tarefas.api;
#### Função: Realiza a comunicação com o backend de mongo para fazer funções de login do usuário
#### Modelos utilizado:
- Tarefa
- Funcionario
#### Métodos disponíveis:
- @GET("v1/funcionario/email") ---> Retorna Call<Funcionario>
- @GET("v1/tarefas/listar-nome/{nome}") ---> Retorna Call<List<Tarefa>>
- @GET("v1/tarefas/listar-nome/{nome}") ---> Retorna Call<List<Tarefa>>
- @PATCH("v1/tarefas/atualizar-status") ---> Retorna Call<Tarefa>
#### Uso:
```java
CalculadoraApi api = RetrofitClient.getRetrofit(getContext()).create(CalculadoraApi.class);
api.getAvisos().enqueue(new Callback<List<Aviso>>() { ... });
```

## [TarefasApi](../app/src/main/java/com/vireya/hydrocore/tarefas/api/TarefasApi.java)

#### Pacote: com.vireya.hydrocore.tarefas.api;
#### Função: Realiza a comunicação com o backend de mongo para fazer funções de login do usuário
#### Modelos utilizado:
- Tarefa
- Funcionario
#### Métodos disponíveis:
- @GET("v1/funcionario/email") ---> Retorna Call<Funcionario>
- @GET("v1/tarefas/listar-nome/{nome}") ---> Retorna Call<List<Tarefa>>
- @GET("v1/tarefas/listar-nome/{nome}") ---> Retorna Call<List<Tarefa>>
- @PATCH("v1/tarefas/atualizar-status") ---> Retorna Call<Tarefa>
#### Uso:
```java
CalculadoraApi api = RetrofitClient.getRetrofit(getContext()).create(CalculadoraApi.class);
api.getAvisos().enqueue(new Callback<List<Aviso>>() { ... });
```

## [ApiService (ConfiguracoesApi)](../app/src/main/java/com/vireya/hydrocore/configuracoes/api/ApiService.java)

#### Pacote: com.vireya.hydrocore.ui.configuracoes.api;
#### Função: Realiza a comunicação com o backend de mongo para fazer funções de login do usuário
#### Modelo utilizado: Funcionario
#### Métodos disponíveis:
- @GET("v1/funcionario/listar") ---> Retorna Call<Funcionario>
- @GET("v1/funcionario/email") ---> Call<Funcionario>
#### Uso:
```java
CalculadoraApi api = RetrofitClient.getRetrofit(getContext()).create(CalculadoraApi.class);
api.getAvisos().enqueue(new Callback<List<Aviso>>() { ... });
```

## [ApiFuncionario](../app/src/main/java/com/vireya/hydrocore/perfil/api/ApiFuncionario.java)

#### Pacote: com.vireya.hydrocore.ui.perfil.api;
#### Função: Realiza a comunicação com o backend de mongo para fazer funções de login do usuário
#### Modelo utilizado: Estatistica
#### Métodos disponíveis:
- @GET("v1/funcionario/{id}/resumo-tarefas") ---> Retorna Call<Funcionario>
- @GET("v1/funcionario/email") ---> Call<Estatistica>
#### Uso:
```java
CalculadoraApi api = RetrofitClient.getRetrofit(getContext()).create(CalculadoraApi.class);
api.getAvisos().enqueue(new Callback<List<Aviso>>() { ... });
```
