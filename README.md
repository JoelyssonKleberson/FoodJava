# 🍽️ FoodJava — Sistema de Pedidos para Restaurante

> Projeto base para o **Projeto Final** da disciplina de **Programação Orientada a Objetos**  
> Curso Superior de Análise e Desenvolvimento de Sistemas — IFPB Campus Monteiro

---

## 👥 Equipe

| Nome                               | Matrícula    |
|------------------------------------|--------------|
| Joelysson Kleberson Alves Ferreira | 202515020022 |

> ✏️ Atualize esta tabela com os dados reais da equipe.

---

## ✅ Pré-requisitos

- **Java Development Kit (JDK) 21** ou superior  
  Verifique com: `java -version`  
  Download: https://adoptium.net

- **Maven 3.8+** instalado e disponível no PATH  
  Verifique com: `mvn -version`  
  Download: https://maven.apache.org/download.cgi

---

## 🚀 Como Executar

### 1. Clone o repositório

```bash
git clone https://gitlab.com/alisson.werner.ifpb/POO.2026.1.git
cd POO.2026.1
```

---

### 2a. Executar pelo Terminal

```bash
mvn javafx:run
```

> Execute sempre a partir da **raiz do projeto** (onde está o `pom.xml`).

---

### 2b. Executar no Eclipse

1. **File → Import → Maven → Existing Maven Projects** → selecione a pasta do projeto → **Finish**
2. Aguarde o Eclipse baixar as dependências (barra de progresso no rodapé)
3. Clique com o botão direito no projeto → **Run As → Maven Build...** (com reticências)
4. No campo **Goals**, digite: `javafx:run`
5. Clique em **Run**

> Nas próximas execuções: botão direito no projeto → **Run As → Maven Build** (sem reticências).

---

### 2c. Executar no VS Code

1. Instale a extensão **Extension Pack for Java** (Microsoft)
2. **File → Open Folder** → selecione a pasta do projeto
3. Aguarde o VS Code indexar o projeto (barra de progresso no rodapé)
4. Abra o terminal integrado: **Terminal → New Terminal**
5. Execute:

```bash
mvn javafx:run
```

Alternativamente, use o painel **Maven** na barra lateral → expanda o projeto → **Plugins → javafx → javafx:run** (duplo clique).

---

## 🗂️ Estrutura do Projeto

```
foodjava/
├── src/
│   └── main/
│       ├── java/
│       │   └── br/edu/ifpb/ads/foodjava/
│       │       ├── MainApp.java          ← ponto de entrada da aplicação
│       │       ├── model/                ← entidades do domínio (Cliente, Pedido, etc.)
│       │       ├── view/                 ← classes Java que constroem telas via código
│       │       │                            (alternativa ao FXML quando a tela é gerada
│       │       │                             programaticamente, sem arquivo externo)
│       │       ├── controller/           ← controladores das telas
│       │       ├── repository/           ← leitura e escrita de arquivos JSON
│       │       ├── exception/            ← exceções personalizadas
│       │       └── util/                 ← utilitários (validações, hash, etc.)
│       └── resources/
│           ├── fxml/                     ← arquivos .fxml criados no SceneBuilder
│           │                                (definem o layout visual das telas de forma
│           │                                 declarativa, separada do código Java)
│           ├── css/                      ← folhas de estilo
│           ├── images/                   ← imagens estáticas do sistema
│           └── data/                     ← arquivos JSON gerados em runtime
├── exemplos-json/
│   ├── cardapio_exemplo.json             ← exemplo de importação de cardápio
│   └── imagens/                          ← imagens referenciadas no JSON de exemplo
├── pom.xml                               ← configuração Maven
└── README.md
```

### `view/` vs `resources/fxml/` — qual a diferença?

No JavaFX existem duas formas de criar uma tela:

- **`resources/fxml/`** — arquivos `.fxml` editados visualmente no **SceneBuilder**. Definem o layout de forma declarativa (como HTML), separando completamente a estrutura visual do código Java. É a abordagem recomendada para a maioria das telas.

- **`view/`** — classes Java que montam a tela via código (ex.: `new Button("Entrar")`). Útil para telas geradas dinamicamente, como listas e cards cujo conteúdo só é conhecido em runtime.

Na prática, a maioria dos projetos usa os dois: SceneBuilder para telas fixas e classes Java para componentes dinâmicos.

---

## 🎨 Criando Telas com SceneBuilder

O **SceneBuilder** é uma ferramenta visual de arrastar e soltar para criar arquivos `.fxml`. Com ele você monta o layout da tela sem escrever código, e o JavaFX carrega o arquivo gerado em runtime.

**Download:** https://gluonhq.com/products/scene-builder

**Como usar no Eclipse:**
1. Crie um arquivo `.fxml` em `src/main/resources/fxml/`
2. Clique com o botão direito no arquivo → **Open with SceneBuilder**
3. Monte a tela visualmente e salve
4. No Controller correspondente, carregue com:

```java
FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NomeDaTela.fxml"));
Parent root = loader.load();
```

---

## 📁 Exemplo de Cardápio para Importação

Um arquivo de exemplo está disponível em `exemplos-json/cardapio_exemplo.json`, com 13 itens cobrindo todas as categorias e com imagens de placeholder prontas para teste.

Categorias válidas: `ENTRADA` · `PRATO_PRINCIPAL` · `SOBREMESA` · `BEBIDAS`

---

## 📄 Documentação Completa

A descrição detalhada do projeto — requisitos funcionais, regras de negócio, exceções, arquitetura e critérios de avaliação — está disponível no PDF entregue pelo professor no Google Classroom.

---

*Projeto acadêmico — IFPB Campus Monteiro · 2026.1*
