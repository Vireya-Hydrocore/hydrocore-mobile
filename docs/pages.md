# Pages
Os Pages são responsáveis por cuidar da lógica e da apresentação visual das telas do aplicativo: 

- Agenda
- Calculadora
- ChatBot
- Entrada
- Estoque
- Funcionario
- Graficos
- Potabilidade
- Relatorio
- Tarefas
- Configurações
- Perfil

Cada Page tem sua respectiva lógica que será aplicada na tela em que a mesma se refere.

### [Agenda.java](../app/src/main/java/com/vireya/hydrocore/agenda/Agenda.java)

#### Pacote: com.vireya.hydrocore.agenda
#### Modelo utilizado: Agenda

#### Principais métodos:
- carregarAvisosDaApi(): Carrega todos os avisos e os configura por prioridade, cada um com sua respectiva cor.

- atualizarRecycler(): Atualiza o recicle view com base nos avisos trazidos pelo método anterior.

### [Calculadora.java](../app/src/main/java/com/vireya/hydrocore/calculadora/Calculadora.java)

#### Pacote: com.vireya.hydrocore.calculadora
#### Modelo utilizado: Calculadora

#### Principais métodos:

- ValidarCalculadora(): Chama os métodos para fazer todas as validações necessárias dos campos.

- ValidarCampos(): Faz a validação do campo recebido como parâmetro.

- ValidarCamposCoagulacao(): Valida os campos de coagulação.

- validarCamposFloculacao(): Valida os campos de floculação.

- DefineVisibilidadeCombo(): Pega o valor da etapa, e define qual card deve ser mostrado (Coagulação ou Floculação).

- gerarComboEtapa(): Gera os valores do comboBox de Etapa.

- executarCalculo(): Executa o cálculo com base nos parâmetros recebidos.

### [ChatBot.java](../app/src/main/java/com/vireya/hydrocore/chatbot/ChatBot.java)

#### Pacote: com.vireya.hydrocore.chatbot
#### Modelo utilizado: ChatBot

#### Principais métodos:

- sendMessage(): Configura a visualização e a lógica de enviar mensagem para o chatbot.

- addBotMessage(): Pega a resposta do chatbot e mostra na tela.

- showProgress(): Mostrar a barra de progresso quando estiver sendo feito a requisição para a API.

### [LoginService.java](../app/src/main/java/com/vireya/hydrocore/entrada/LoginService.java)

#### Pacote: com.vireya.hydrocore.entrada
#### Modelo utilizado: LoginService

#### Principais métodos:

- validarCampos(): Valida os campos para logar no aplicativo.
- realizarLogin(): Faz todo o proceso de pegar os parâmetros e logar no aplicativo.

### [EsqueceuSenha.java](../app/src/main/java/com/vireya/hydrocore/entrada/EsqueceuSenha.java)

#### Pacote: com.vireya.hydrocore.entrada
#### Modelo utilizado: EsqueceuSenha

#### Principais métodos:

- enviarEmailRecuperacao(): Funcionalidade para enviar email para recuperar a senha, porém o render está bloqueando envio, então método não utilizado por enquanto.

Objetivo da tela atualmente: Pegar o email do usuário para redefinir sua senha.

### [RedefinirSenha.java](../app/src/main/java/com/vireya/hydrocore/entrada/RedefinirSenha.java)

#### Pacote: com.vireya.hydrocore.entrada
#### Modelo utilizado: RedefinirSenha

#### Principais métodos:
- redefinirSenha(): Faz o processo de pegar a nova senha e sua confirmação e redefini-la.

### [Estoque.java](../app/src/main/java/com/vireya/hydrocore/estoque/Estoque.java)

#### Pacote: com.vireya.hydrocore.estoque
#### Modelo utilizado: Estoque

### Principais métodos:
- carregarProdutos(): Carrega os produtos em estoque com base na ETA em que o usuário logado está cadastrado.

- carregarOffline(): Guarda os produtos de forma offline e depois carrega eles.

- filterList(): Faz o filtro dos produtos (Todos, Suficiente, Próx. ao fim, Insuficiente)

- updateButtonUI(): Atualizar as cores dos botões do filtro de acordo com o que for clicado.

### [Graficos.java](../app/src/main/java/com/vireya/hydrocore/graficos/Graficos.java)

#### Pacote: com.vireya.hydrocore.graficos
#### Modelo utilizado: Gráficos

A classe em geral consiste em apenas carregar os gráficos do BI.


### [Potabilidade.java](../app/src/main/java/com/vireya/hydrocore/potabilidade/Potabilidade.java)

#### Pacote: com.vireya.hydrocore.potabilidade
#### Modelo utilizado: Potabilidade

### Principais métodos:
- configurarValidacoes(): Chama as validações de todos os campos pelo método ValidarCampo().
- validarCampo(): Faz a validação do campo recebido como parâmetro.

### [SimpleTextWatcher.java](../app/src/main/java/com/vireya/hydrocore/potabilidade/SimpleTextWatcher.java)

#### Pacote: com.vireya.hydrocore.potabilidade
#### Modelo utilizado: SimpleTextWatcher

Em geral a classe consiste apenas para ser chamada nos métodos de validação na potabilidade, pois ela analisa caracter por caracter.

### [Relatorio.java](../app/src/main/java/com/vireya/hydrocore/relatorio/Relatorio.java)

#### Pacote: com.vireya.hydrocore.relatorio
#### Modelo utilizado: Relatório

### Principais métodos:
- carregarRelatorios(): carrega todos os relatórios disponíveis da ETA em que o usuário logado é cadastrado.

### [PdfGenerator.java](../app/src/main/java/com/vireya/hydrocore/relatorio/PdfGenerator.java)

#### Pacote: com.vireya.hydrocore.relatorio
#### Modelo utilizado: PdfGenerator

### Principais métodos:
- gerarPdf(): pega os dados do mês selecionado, e monta um relatório em pdf com base nisso.

### [Tarefas.java](../app/src/main/java/com/vireya/hydrocore/tarefas/Tarefas.java)

#### Pacote: com.vireya.hydrocore.tarefas
#### Modelo utilizado: Tarefas

- carregarTarefas(): Traz todas as tarefas pendentes do usuário logado.
- onViewCreated(): Além de chamar o método de carregar tarefas, também atualiza o status das tarefas selecionadas.

### [Configuracoes.java](../app/src/main/java/com/vireya/hydrocore/ui/configuracoes/Configuracoes.java)

#### Pacote: com.vireya.hydrocore.ui.configuracoes
#### Modelo utilizado: Configurações

- carregarDadosFuncionario(): Traz todos os dados do  funcionário.

- showInformations(): Abre a tela de informações de login do usuário

- setupToggle(): Configura o toogle das opções, para ativar ou desativar.

### [InformacoesConfig.java](../app/src/main/java/com/vireya/hydrocore/ui/configuracoes/InformacoesConfig.java)

#### Pacote: com.vireya.hydrocore.ui.configuracoes
#### Modelo utilizado: InformacoesConfig

- carregarInfosFuncionarioLogado(): Traz todos os dados do funcionário logado e preenche na tela.

### [Perfil.java](../app/src/main/java/com/vireya/hydrocore/ui/perfil/Perfil.java)

#### Pacote: com.vireya.hydrocore.ui.perfil
#### Modelo utilizado: Perfil

- showImageOptions(): Mostra as opções ao clicar no ícone para mudar foto:
  - Visualizar Foto
  - Tirar Foto
  - Escolher da galeria

- visualizarFoto(): visualiza a foto que está no perfil do usuário.

- openCamera(): Abre a câmera para tirar foto.

- openGallery(): Abre a galeria do usuário, para que ele possa escolher a foto que desejar.

- saveImageToInternalStorage(): Pega a foto selecionada ou tirada pelo usuário, e a salva no Storage Interno do aplicativo.

- loadProfileImage(): Carrega a imagem de perfil do funcionário.

- loadTarefasStats(): Mostra todas as tarefas concluídas e pendentes que o usuário tem. Além de mostrar o total de tarefas.

- loadFuncionarioInfo(): Traz o nome e o cargo do funcionário logado no aplicativo.

- loadGraficoProdutividade(): Com base no resumo de tarefas do usuário carrega um gráfico da sua produtividade.

- atualizarGrafico(): Atualiza com os dados o Gráfico de Produtividade. 







