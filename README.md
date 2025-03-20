![Java](https://img.shields.io/badge/Java-17%2B-blue)  
![License](https://img.shields.io/badge/License-MIT-green)  
![POO](https://img.shields.io/badge/Design-Orientado%20a%20Objetos-brightgreen)

# Calculadora Avançada 🚀

Este repositório contém um projeto Java que implementa uma **calculadora avançada**. Ela é capaz de avaliar expressões aritméticas complexas com suporte a:
- **Operações Básicas**: Adição, subtração, multiplicação e divisão.
- **Parênteses**: Para expressões aninhadas.
- **Multiplicação Implícita**: Ex.: `2(3)` é interpretado como `2 * (3)`.

O código é estruturado de forma **modular** utilizando pacotes e interfaces, facilitando a manutenção, escalabilidade e clareza do projeto. ✨

## 📚 Índice
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Detalhamento dos Componentes](#detalhamento-dos-componentes)
  - [1️⃣ Pacote `calculadora`](#1️⃣-pacote-calculadora)
    - [CalculadoraMain.java](#calculadoramainjava)
  - [2️⃣ Pacote `calculadora.entities`](#2️⃣-pacote-calculadoraentities)
    - [Operacoes.java](#operacoesjava)
  - [3️⃣ Pacote `calculadora.entities.interfaces`](#3️⃣-pacote-calculadoraentitiesinterfaces)
    - [OperacaoInterface.java e Implementações](#operacaointerfacejava-e-implementações)
- [Fluxo de Execução e Lógica do Código](#fluxo-de-execução-e-lógica-do-código)
- [Conclusão](#conclusão)
- [Roadmap](#roadmap)
- [Contato e Licença](#contato-e-licença)

---

## 📂 Estrutura do Projeto <a name="estrutura-do-projeto"></a>
O projeto está dividido em **3 pacotes principais**:

1. **`calculadora`**  
   Contém a classe principal que gerencia a interação com o usuário e a execução do programa.

2. **`calculadora.entities`**  
   Abriga a classe que contém a lógica central de processamento e avaliação de expressões aritméticas.

3. **`calculadora.entities.interfaces`**  
   Define a interface para as operações aritméticas e fornece as implementações específicas para cada operação (soma, subtração, multiplicação e divisão).

---

## 🛠️ Detalhamento dos Componentes <a name="detalhamento-dos-componentes"></a>

### 1️⃣ Pacote `calculadora` <a name="1️⃣-pacote-calculadora"></a>

#### CalculadoraMain.java <a name="calculadoramainjava"></a>

- **Objetivo:**  
  Serve como **ponto de entrada** do programa, gerenciando a interação com o usuário via console. 💻

- **Principais Funcionalidades:**
  - **Importações:**  
    - `Operacoes` do pacote `calculadora.entities` para realizar os cálculos.
    - `Scanner` do pacote `java.util` para leitura de entrada.
    
  - **Método `main`:**
    - Inicializa um objeto `Scanner` para capturar as entradas do usuário.
    - Instancia a classe `Operacoes`, que contém a lógica de avaliação das expressões.
    - Exibe um **cabeçalho** com mensagem de boas-vindas e instruções, informando que o usuário pode digitar **"sair"** a qualquer momento para encerrar.
    - Entra em um **loop contínuo** (`while (true)`) que:
      - Solicita a expressão aritmética (ex.: "3 + 5 * 2").
      - Converte a entrada para uppercase para padronizar a verificação do comando "SAIR".
      - Verifica se a entrada é "SAIR"; se sim, exibe uma mensagem de encerramento e finaliza o loop.
      - Tenta calcular a expressão usando o método `calcularExpressao` da classe `Operacoes`.  
      - Se o cálculo for bem-sucedido, exibe o resultado formatado pelo método `resultado`.
      - Em caso de erro, captura a exceção e exibe uma mensagem amigável com o método `getMensagemErro`.
    - Fecha o `Scanner` ao final para liberar os recursos.

  - **Método `getMensagemErro(Exception e)`:**
    - **Objetivo:** Traduzir e formatar mensagens de erro para que fiquem mais compreensíveis pelo usuário.
    - **Lógica:**
      - Para `ArithmeticException` ou `IllegalArgumentException`, retorna a mensagem original.
      - Se a exceção tiver uma causa do tipo `NumberFormatException`, extrai a parte inválida e retorna uma mensagem personalizada.
      - Para outros tipos de exceção, retorna uma mensagem genérica informando que ocorreu um erro inesperado, com o nome da exceção.

---

### 2️⃣ Pacote `calculadora.entities` <a name="2️⃣-pacote-calculadoraentities"></a>

#### Operacoes.java <a name="operacoesjava"></a>

- **Objetivo:**  
  Centralizar a lógica de processamento e avaliação de expressões aritméticas, garantindo a execução correta das operações com validações e tratamento de parênteses. 🔄

- **Atributos:**
  - `soma`, `subtracao`, `multiplicacao` e `divisao`:  
    Objetos que implementam a interface `OperacaoInterface`, representando as operações aritméticas básicas. Cada um é instanciado com sua implementação respectiva (Soma, Subtracao, Multiplicacao, Divisao).

- **Construtor:**
  - Inicializa os atributos com as implementações correspondentes, permitindo execução modular de cada operação.

- **Principais Métodos:**

  - **`calcularExpressao(String expressao)`**
    - **Função:**  
      Avalia a expressão aritmética passada como string e retorna o resultado em um `double`.
    - **Passos:**
      1. **Validação Inicial:**  
         Chama `validarExpressao` para garantir que a expressão esteja correta (não vazia, parênteses balanceados, caracteres válidos, etc.).
      2. **Normalização:**  
         Remove espaços e substitui vírgulas por pontos para padronizar os números.
      3. **Multiplicação Implícita:**  
         Chama `adicionarMultiplicacaoEntreNumeroEParenteses` para inserir o operador `*` onde necessário.
      4. **Processamento de Parênteses:**  
         Resolve expressões contidas entre parênteses de forma recursiva com o método `processarParenteses`.
      5. **Processamento de Operações:**  
         - Primeiro, processa multiplicações e divisões com regex.
         - Em seguida, processa adições e subtrações.
      6. **Conversão Final:**  
         Converte a expressão reduzida a um único valor para `double` e retorna o resultado.

  - **`processarParenteses(String expr)`**
    - **Objetivo:**  
      Resolver recursivamente todas as expressões dentro de parênteses.
    - **Como Funciona:**  
      Localiza o parêntese de abertura mais interno, identifica o fechamento, extrai a expressão interna, calcula seu resultado (chamada recursiva a `calcularExpressao`) e substitui a expressão original pelo resultado. Repete até não restarem parênteses.

  - **`adicionarMultiplicacaoEntreNumeroEParenteses(String expr)`**
    - **Objetivo:**  
      Inserir o operador de multiplicação `*` onde a multiplicação é implícita.
    - **Exemplos:**
      - `2(3)` é transformado em `2*(3)`.
      - `(2)(3)` ou `)(` são automaticamente corrigidos.

  - **`validarExpressao(String expressao)`**
    - **Objetivo:**  
      Garantir que a expressão seja sintaticamente correta.
    - **Validações:**
      - Expressão não pode ser vazia.
      - Parênteses devem estar balanceados.
      - Somente caracteres permitidos: dígitos, operadores aritméticos, vírgulas, pontos, parênteses e espaços.
      - Números não podem conter múltiplos pontos ou vírgulas.
      - A expressão não pode terminar com um operador.
      - Não podem existir operadores consecutivos inválidos.
      - A expressão não deve iniciar com um operador inválido (exceto o sinal negativo).
      - Números separados por espaços sem operador são proibidos.

  - **`processarOperacao(String expr, String operadores)`**
    - **Objetivo:**  
      Identificar e processar uma operação aritmética individual na expressão.
    - **Como Funciona:**  
      Utiliza expressões regulares para localizar padrões do tipo `(-?\d+\.?\d*)([*/+-])(-?\d+\.?\d*)`. Uma vez identificado:
      - Extrai operandos e o operador.
      - Converte os operandos para `double`.
      - Chama `realizarOperacao` para executar a operação.
      - Substitui a parte da expressão pelo resultado obtido.

  - **`realizarOperacao(String operador, double num1, double num2)`**
    - **Objetivo:**  
      Delegar a execução da operação para a implementação correta da interface `OperacaoInterface` conforme o operador.
    - **Implementação:**  
      Utiliza um `switch` para:
      - `+`: invocar a operação de soma.
      - `-`: invocar a operação de subtração.
      - `*`: invocar a operação de multiplicação.
      - `/`: invocar a operação de divisão (com verificação para evitar divisão por zero).

  - **`resultado(double resultado)`**
    - **Objetivo:**  
      Formatar e exibir o resultado final de acordo com o padrão brasileiro (vírgula como separador decimal e duas casas decimais).
    - **Implementação:**  
      Utiliza `String.format` com a localidade `pt-BR` e exibe o resultado via `System.out.printf`.

---

### 3️⃣ Pacote `calculadora.entities.interfaces` <a name="3️⃣-pacote-calculadoraentitiesinterfaces"></a>

#### OperacaoInterface.java e Implementações <a name="operacaointerfacejava-e-implementações"></a>

- **OperacaoInterface.java**  
  - **Objetivo:**  
    Estabelecer um contrato para todas as operações aritméticas, garantindo que as classes implementem:
    - `double calcular(double num1, double num2);`

- **Soma.java**  
  - **Função:**  
    Realiza a adição: retorna a soma de `num1` e `num2`. ➕

- **Subtracao.java**  
  - **Função:**  
    Realiza a subtração: retorna o resultado de `num1 - num2`. ➖

- **Multiplicacao.java**  
  - **Função:**  
    Realiza a multiplicação: retorna o produto de `num1` e `num2`. ✖️

- **Divisao.java**  
  - **Função:**  
    Realiza a divisão: retorna o resultado de `num1 / num2`.  
  - **Detalhe Importante:**  
    Verifica se `num2` é zero. Se for, lança uma `ArithmeticException` com a mensagem de que não é possível dividir por zero. ➗❌

---

## 🔄 Fluxo de Execução e Lógica do Código <a name="fluxo-de-execução-e-lógica-do-código"></a>
1. **Inicialização:**  
   Ao iniciar o programa, o método `main` é executado, criando o objeto `Scanner` e instanciando a classe `Operacoes` com a lógica central para o processamento das expressões.

2. **Interação com o Usuário:**  
   O programa exibe uma mensagem inicial e entra num loop infinito, onde solicita que o usuário digite uma expressão aritmética. A qualquer momento, o usuário pode digitar **"SAIR"** para encerrar o programa. 👋

3. **Processamento da Expressão:**
   - **Validação:** A expressão é validada para garantir que está corretamente formada.
   - **Normalização:** Remoção de espaços e padronização dos separadores decimais.
   - **Multiplicação Implícita:** Inserção do operador `*` quando necessário.
   - **Resolução de Parênteses:** Processamento recursivo das expressões aninhadas.
   - **Avaliação de Operações:** Respeitando a precedência (multiplicação/divisão antes de adição/subtração), utilizando regex para identificar e calcular cada operação.
   - **Resultado:** A expressão final é convertida para `double` e exibida.

4. **Exibição do Resultado:**  
   O resultado final é formatado para o padrão brasileiro e exibido ao usuário. 🎯

5. **Tratamento de Erros:**  
   Se ocorrer algum erro (ex.: parênteses desbalanceados, operadores consecutivos inválidos, formatação numérica incorreta ou divisão por zero), uma exceção é lançada, capturada no método `main` e uma mensagem de erro amigável é exibida. ⚠️

---

## 🔚 Conclusão <a name="conclusão"></a>
Este projeto demonstra uma abordagem **robusta e modular** para construir uma calculadora avançada em Java. Com a separação de responsabilidades em pacotes e o uso de interfaces, o código é facilmente expansível e manutenível. A forte ênfase em validação e tratamento de exceções garante que o usuário receba feedback adequado, evitando falhas inesperadas durante a execução. 👍

Este README detalhado serve como um guia completo para desenvolvedores interessados em entender, utilizar ou expandir as funcionalidades desta calculadora avançada.

---

## Roadmap 🌟 <a name="roadmap"></a>
| Prioridade | Recurso             | Status               |
|------------|---------------------|----------------------|
| 🔥 Alta    | Potência (^)        | Em desenvolvimento   |
| 🟡 Média   | Funções (sqrt, log) | Planejado            |
| 🟢 Baixa   | Interface Gráfica   | Backlog              |

---

## Contato e Licença <a name="contato-e-licença"></a>
📧 **Contato**:  
✉️ matheuscunhaprado@gmail.com  

📜 **Licença**: MIT License - Libre para uso e modificação
