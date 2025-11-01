# Adapters

Os Adapters são responsáveis por conectar os dados das diferentes áreas do aplicativo (Agenda, Chatbot, Estoque, Relatórios e Tarefas) às interfaces gráficas que exibem essas informações para o usuário.

Cada módulo com ReciclerVuew possui seu próprio Adapter especializado, que traduz objetos de modelo (como Aviso, Message, Produto, RelatorioResumo e Tarefa) em elementos visuais exibidos em listas (RecyclerView).

Segue o padrão de projeto [Adapter Pattern](https://refactoring.guru/pt-br/design-patterns/adapter)


### [AvisoAdapter.java](../app/src/main/java/com/vireya/hydrocore/agenda/adapter/AvisoAdapter.java)

#### Pacote: com.vireya.hydrocore.agenda.adapter
#### Função: Exibe uma lista de avisos na tela de Agenda.
#### Modelo utilizado: Aviso
#### Layout: item_schedule.xml
#### Observação: Cada aviso muda de cor conforme a prioridade.
#### Principais métodos:
- onBindViewHolder(): Define o texto e a cor do aviso com base na prioridade.
- updateList(List<Aviso>): Atualiza a lista exibida e notifica o RecyclerView.

#### Lógica de cor:
- Vermelho ---> Prioridade Alta
- Amarelo ---> Prioridade Média
- Verde ---> Prioridade Baixa

### [ChatAdapter](../app/src/main/java/com/vireya/hydrocore/chatbot/adapter/ChatAdapter.java)

#### Pacote: com.vireya.hydrocore.chatbot.adapter
#### Função: Exibe as mensagens entre o usuário e o chatbot.
#### Modelo utilizado: Message
#### Layouts:
- item_message_user.xml ---> Mensagens do usuário
- item_message_bot.xml ---> Mensagens do bot
#### Observação:
Utiliza dois tipos de ViewHolder (UserViewHolder e BotViewHolder) para exibir mensagens com layouts diferentes.
#### Principais métodos:
- getItemViewType() ---> Identifica se a mensagem é do usuário ou do bot.
- onBindViewHolder() ---> Atribui o texto da mensagem ao tipo correto de ViewHolder.

## [ProdutoAdapter](../app/src/main/java/com/vireya/hydrocore/estoque/adapter/ProdutoAdapter.java)

#### Pacote: com.vireya.hydrocore.estoque.adapter
#### Função: Exibe a lista de produtos no estoque.
#### Modelo utilizado: Produto
#### Layout: item_produto.xml
#### Observação: Cada item exibe nome, quantidade e uma barra de status com cores que indicam o nível do produto.

#### Lógica de cor do status:
- Verde ---> Suficiente
- Amarelo ---> Próximo ao fim
- Vermelho ---> Insuficiente
- Cinza ---> Indefinido

#### Principais métodos:
- updateList(List<Produto>): Atualiza a lista de produtos exibida.
- onBindViewHolder(): Define nome, quantidade e cor da barra de status.

## [RelatorioAdapter](../app/src/main/java/com/vireya/hydrocore/relatorio/adapter/RelatorioAdapter.java)

#### Pacote: com.vireya.hydrocore.relatorio.adapter
#### Função: Lista relatórios e permite buscar detalhes ou baixar informações.
#### Modelos utilizados:
- RelatorioResumo
- RelatorioDetalhado
- API: RelatorioApi (via Retrofit)
#### Layout: item_relatorio.xml

#### Observação: Ao clicar no botão de download, faz uma chamada Retrofit para buscar o relatório detalhado e exibe um Toast com os dados retornados.

#### Principais métodos:
- onBindViewHolder() ---> Configura o botão de download e executa a requisição à API.
- Callback Retrofit ---> Exibe mensagem com nome e cidade do relatório.

## [TarefasAdapter](../app/src/main/java/com/vireya/hydrocore/tarefas/adapter/TarefasAdapter.java)


#### Pacote: com.vireya.hydrocore.tarefas.adapter
#### Função: Gerencia a exibição e marcação de tarefas concluídas.
#### Modelo utilizado: Tarefa
#### Layout: card_tarefa.xml

#### Destaque:Permite marcar/desmarcar tarefas como "concluída" e mantém o estado atualizado na lista.

#### Principais métodos:
- setTarefas(List<Tarefa>): Atualiza a lista de tarefas.
- getTarefasSelecionadas(): Retorna apenas as tarefas marcadas como concluídas.
- onBindViewHolder() → Sincroniza o CheckBox com o status da tarefa.