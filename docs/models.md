# Models

## [Aviso](../app/src/main/java/com/vireya/hydrocore/agenda/model/Aviso.java)

#### Pacote: com.vireya.hydrocore.agenda.model;
#### Usos: 
#### Campos: 
```ts
private int id;
private String descricao;
private String dataOcorrencia;
private String nomeEta;
private int idPrioridade;
}
```

## [CalculoCoagulacaoRequest](../app/src/main/java/com/vireya/hydrocore/calculadora/model/CalculoCoagulacaoRequest.java)

#### Pacote: com.vireya.hydrocore.calculadora.model;
#### Usos: 
#### Campos:
```ts
private double turbidez;
private double ph;
private String cor;
private double volume;
private double alumina;
private double alcalinidade;
private String produtoQuimico;
```

## [CalculoFloculacaoRequest](../app/src/main/java/com/vireya/hydrocore/calculadora/model/CalculoFloculacaoRequest.java)

#### Pacote: com.vireya.hydrocore.calculadora.model;
#### Usos: 
#### Campos:
```ts
private double turbidez;
private double ph;
private String cor;
private String produtoQuimico;
```

## [CalculoResponse](../app/src/main/java/com/vireya/hydrocore/calculadora/model/CalculoResponse.java)

#### Pacote: com.vireya.hydrocore.calculadora.model;
#### Usos: 
#### Campos:
```ts
private String produto;
private double quantidade;
```

## [ProdutoResponse](../app/src/main/java/com/vireya/hydrocore/calculadora/model/ProdutoResponse.java)

#### Pacote: com.vireya.hydrocore.calculadora.model;
#### Usos: 
#### Campos:
```ts
private int id;
private List<String> produtos;
```

## [Login](../app/src/main/java/com/vireya/hydrocore/entrada/model/Login.java)

#### Pacote: com.vireya.hydrocore.entrada.model;
#### Usos: 
#### Campos:
```ts
private String email;
private String password;
private String codigoEmpresa;
```

## [LoginResponse](../app/src/main/java/com/vireya/hydrocore/entrada/model/LoginResponse.java)

#### Pacote: com.vireya.hydrocore.entrada.model;
#### Usos: 
#### Campos:
```ts
private String token;
private String chaveApi;
```

## [Produto](../app/src/main/java/com/vireya/hydrocore/estoque/model/Produto.java)

#### Pacote: com.vireya.hydrocore.estoque.model;
#### Usos: 
#### Campos:
```ts
private int id;
private String nome;
private int quantidade;
private String status;
```

## [ProdutoResponse](../app/src/main/java/com/vireya/hydrocore/estoque/model/ProdutoResponse.java)

#### Pacote: com.vireya.hydrocore.estoque.model;
#### Usos: 
#### Campos:
```ts
private int id;
private List<String> produtos;
```

## [Funcionario](../app/src/main/java/com/vireya/hydrocore/funcionario/model/Funcionario.java)

#### Pacote: com.vireya.hydrocore.funcionario.model;
#### Usos: 
#### Campos:
```ts
private int id;
private String nome;
private String email;
private String dataAdmissao;
private String dataNascimento;
private String eta;
private String cargo;
```

## [PotabilityResponse](../app/src/main/java/com/vireya/hydrocore/potabilidade/model/PotabilityResponse.java)

#### Pacote: com.vireya.hydrocore.potabilidade.model;
#### Usos: 
#### Campos:
```ts
public String potability;
```
#### Métodos:
- PotabilityResponse fromJson(String json) ---> verifica se a água é potável ou não de acordo com o JSON de resposta

## [PotabilityResponse](../app/src/main/java/com/vireya/hydrocore/potabilidade/model/PotabilityResponse.java)

#### Pacote: com.vireya.hydrocore.potabilidade.model;
#### Usos: 
#### Campos:
```ts
public String potability;
```
#### Métodos:
- PotabilityResponse fromJson(String json) ---> verifica se a água é potável ou não de acordo com o JSON de resposta

## [WaterData](../app/src/main/java/com/vireya/hydrocore/potabilidade/model/WaterData.java)

#### Pacote: com.vireya.hydrocore.potabilidade.model;
#### Usos: 
#### Campos:
```ts
public double ph;
public double Hardness;
public double Solids;
public double Chloramines;
public double Sulfate;
public double Conductivity;
public double Organic_carbon;
public double Trihalomethanes;
public double Turbidity;
```

## [WaterData](../app/src/main/java/com/vireya/hydrocore/potabilidade/model/WaterData.java)

#### Pacote: com.vireya.hydrocore.potabilidade.model;
#### Usos: 
#### Campos:
```ts
public double ph;
public double Hardness;
public double Solids;
public double Chloramines;
public double Sulfate;
public double Conductivity;
public double Organic_carbon;
public double Trihalomethanes;
public double Turbidity;
```

## [RelatorioDetalhado](../app/src/main/java/com/vireya/hydrocore/relatorio/model/RelatorioDetalhado.java)

#### Pacote: package com.vireya.hydrocore.relatorio.model;

#### Usos: 
#### Campos:
```ts
private int id;
private double volumeTratado;
private String nome;
private String nomeAdmin;
private String cidade;
private String estado;
private String bairro;
private double phMin;
private double phMax;
private String comentarioGerente;
```

## [RelatorioResumo](../app/src/main/java/com/vireya/hydrocore/relatorio/model/RelatorioResumo.java)

#### Pacote: com.vireya.hydrocore.relatorio.model;

#### Usos: 
#### Campos:
```ts
private int idRelatorio;
private String data;
```

## [Tarefa](../app/src/main/java/com/vireya/hydrocore/tarefa/model/Tarefa.java)

#### Pacote: com.vireya.hydrocore.tarefas.model;

#### Usos: 
#### Campos:
```ts
private int id;
private String descricao;
private Date dataCriacao;
private Date dataConclusao;
private String status;
private int idFuncionario;
private String nivel;
private String prioridade;
```
#### Métodos:
-  isConcluida() ---> verifica se a tarefa está concluída
- alternarStatus ---> alterna os status entre pendente e concluída

## [Funcionário](../app/src/main/java/com/vireya/hydrocore/funcionario/model/Funcionario.java)

#### Pacote: com.vireya.hydrocore.ui.configuracoes.model;
#### Usos: 
#### Campos:
```ts
private int id;
private String nome;
private String email;
private Date dataAdmissao;
private Date dataNascimento;
private String eta;
private String cargo;
```
