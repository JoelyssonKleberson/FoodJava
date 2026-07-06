# 🍽️ FoodJava

Sistema de gerenciamento de pedidos para um restaurante desenvolvido como Projeto Final da disciplina de **Programação Orientada a Objetos (POO)** do Curso Superior de Análise e Desenvolvimento de Sistemas do **IFPB – Campus Monteiro**.

---

# 👨‍💻 Integrante

| Nome                               | Matrícula    |
|------------------------------------|--------------|
| Joelysson Kleberson Alves Ferreira | 202515020022 |

---

# 📖 Sobre o Projeto

O FoodJava é uma aplicação desktop desenvolvida em Java que simula o funcionamento de um sistema de pedidos para um restaurante.

O sistema possui dois perfis de usuário: **Gerente** e **Cliente**.

O gerente é responsável por configurar o restaurante, gerenciar o cardápio, importar itens através de arquivos JSON, acompanhar os pedidos recebidos e controlar o andamento de cada pedido.

O cliente pode realizar seu cadastro, efetuar login, visualizar o cardápio organizado por categorias, montar um carrinho de compras, confirmar pedidos e acompanhar seu histórico e o status de cada pedido.

O projeto foi desenvolvido aplicando os principais conceitos de Programação Orientada a Objetos, arquitetura MVC e persistência de dados em arquivos JSON.

---

# ✨ Funcionalidades

## Gerente

* Configuração inicial do restaurante;
* Login administrativo;
* Cadastro, edição e exclusão de itens do cardápio;
* Ativação e desativação de itens;
* Importação de cardápio através de arquivo JSON;
* Upload de imagens dos itens;
* Exibição de imagem padrão para itens sem foto;
* Visualização de todos os pedidos;
* Filtro de pedidos por status;
* Avanço do status dos pedidos;
* Cancelamento de pedidos conforme as regras de negócio;
* Visualização do resumo diário de pedidos e faturamento.

## Cliente

* Auto cadastro;
* Login no sistema;
* Visualização do cardápio por categorias;
* Adição e remoção de itens do carrinho;
* Confirmação de pedidos;
* Histórico de pedidos;
* Acompanhamento do status dos pedidos em tempo real.

---

# 🧠 Conceitos de POO Aplicados

O projeto foi desenvolvido utilizando os principais conceitos estudados na disciplina:

* Encapsulamento;
* Herança;
* Polimorfismo;
* Abstração;
* Interfaces;
* Classes Abstratas;
* Tratamento de Exceções Personalizadas;
* Arquitetura MVC;
* Camada Repository;
* Persistência em JSON.

---

# 🛠️ Tecnologias Utilizadas

* Java 17+
* JavaFX
* Maven
* Gson
* JSON
* Git
* GitHub

---

# 📂 Estrutura do Projeto

```text
FoodJava
├── .idea
├── exemplos-json
├── out
├── src
│   └── main
│       ├── java
│       │   └── br.edu.ifpb.ads.foodjava
│       │       ├── controller
│       │       ├── enums
│       │       ├── exceptions
│       │       ├── model
│       │       ├── repository
│       │       ├── utils
│       │       ├── view
│       │       └── MainApp.java
│       │
│       └── resources
│           └── data
│           └── images
│               ├── comida_padrao.jpg
│               ├── fundo_login.jpg
│               └── logotipo.png
│
├── target
├── pom.xml
└── README.md
```

---

# 💾 Persistência de Dados

Os dados da aplicação são armazenados em arquivos JSON gerados automaticamente durante a execução do sistema.

São persistidos dados como:

* Restaurante;
* Clientes;
* Cardápio;
* Pedidos.

---

# ▶️ Executando o Projeto

## Pré-requisitos

Antes de executar o projeto é necessário possuir instalado:

* Java JDK 17 ou superior;
* Maven 3.8 ou superior.

Verifique as versões instaladas:

```bash
java -version
```

```bash
mvn -version
```

---

## Clonando o Repositório

```bash
git clone https://github.com/JoelyssonKleberson/FoodJava.git
```

```bash
cd FoodJava
```

---

## Executando pelo Terminal

```bash
mvn javafx:run
```

---

## Executando pelo IntelliJ IDEA

1. Abra o projeto.
2. Aguarde o Maven carregar todas as dependências.
3. Abra:

```
Maven
    Plugins
        javafx
            javafx:run
```

4. Execute **javafx:run** com um duplo clique.

---

# 🖼️ Imagens do Cardápio

O sistema permite adicionar imagens aos itens do cardápio.

Caso seja necessário converter uma imagem para o formato JPG, utilize o site:

https://www.iloveimg.com/pt/converter-para-jpg

Após a conversão, utilize a imagem normalmente durante o cadastro ou importação dos itens.

---

# 📥 Importação de Cardápio

O sistema permite importar itens através de um arquivo JSON seguindo o padrão especificado no projeto.

Um arquivo de exemplo encontra-se na pasta:

```text
exemplos-json/
```

Durante a importação, registros válidos são inseridos normalmente enquanto registros inválidos são informados individualmente sem interromper a importação.

---

# 📌 Arquitetura

O projeto segue o padrão arquitetural **MVC (Model-View-Controller)** com uma camada **Repository**, separando as responsabilidades em:

* **Model:** entidades e regras de negócio;
* **View:** interface gráfica desenvolvida em JavaFX;
* **Controller:** comunicação entre interface e regras de negócio;
* **Repository:** persistência dos dados em arquivos JSON;
* **Util:** classes auxiliares e validações;
* **Exception:** exceções personalizadas.

---

# 📚 Projeto Acadêmico

Projeto desenvolvido exclusivamente para fins acadêmicos na disciplina de Programação Orientada a Objetos do Instituto Federal da Paraíba - Campus Monteiro (IFPB) .
