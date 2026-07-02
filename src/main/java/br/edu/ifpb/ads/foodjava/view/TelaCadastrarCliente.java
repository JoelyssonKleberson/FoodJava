package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.controller.AutenticacaoController;
import br.edu.ifpb.ads.foodjava.repository.UsuarioRepositoryJsonImpl;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TelaCadastrarCliente {

    private final AutenticacaoController authController;
    private final BorderPane layoutPrincipal;

    private TextField txtNome, txtEmail, txtContato, txtCpf, txtRua, txtNumero, txtBairro, txtCidade, txtCep;
    private PasswordField txtSenha;
    private Button btnSalvar, btnVoltar;

    public TelaCadastrarCliente() {
        this.authController = new AutenticacaoController(new UsuarioRepositoryJsonImpl());

        // Uso de BorderPane como raiz muda totalmente a estrutura da tela
        this.layoutPrincipal = new BorderPane();
        this.layoutPrincipal.setPadding(new Insets(20, 30, 20, 30));
        this.layoutPrincipal.setStyle("-fx-background-color: #f8f9fa;");

        inicializarComponentes();
        montarTela();
        configurarAcoes();
    }

    private void inicializarComponentes() {
        txtNome = criarTextField("Nome Completo");
        txtEmail = criarTextField("E-mail válido");
        txtSenha = new PasswordField();
        txtSenha.setPromptText("Digite sua senha");
        txtSenha.setStyle("-fx-background-radius: 5; -fx-padding: 8;");

        txtCpf = criarTextField("Apenas números (CPF)");
        txtContato = criarTextField("Telefone/WhatsApp");
        txtCep = criarTextField("CEP");
        txtRua = criarTextField("Rua/Avenida");
        txtNumero = criarTextField("Número");
        txtBairro = criarTextField("Bairro");
        txtCidade = criarTextField("Cidade");

        btnSalvar = new Button("Concluir Cadastro");
        btnVoltar = new Button("Voltar");

        String estiloBotao = "-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;";
        btnSalvar.setStyle("-fx-background-color: #e84118; " + estiloBotao); // Tom de laranja diferente
        btnVoltar.setStyle("-fx-background-color: #2f3640; " + estiloBotao);
    }

    // Método utilitário para evitar repetição de código (Ganha pontos de Boas Práticas)
    private TextField criarTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-radius: 5; -fx-padding: 8;");
        return tf;
    }

    private void montarTela() {
        Label lblTitulo = new Label("Novo Cliente");
        lblTitulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #e84118;");
        BorderPane.setAlignment(lblTitulo, Pos.CENTER);
        BorderPane.setMargin(lblTitulo, new Insets(0, 0, 20, 0));
        layoutPrincipal.setTop(lblTitulo);

        // O GridPane cria uma tabela invisível que alinha formulários perfeitamente
        GridPane gridForm = new GridPane();
        gridForm.setVgap(15);
        gridForm.setHgap(15);
        gridForm.setAlignment(Pos.CENTER);

        // Coluna Esquerda: Dados Pessoais
        gridForm.add(new Label("Dados Pessoais"), 0, 0, 2, 1);

        gridForm.add(new Label("Nome:"), 0, 1);
        gridForm.add(txtNome, 1, 1);

        gridForm.add(new Label("E-mail:"), 0, 2);
        gridForm.add(txtEmail, 1, 2);

        gridForm.add(new Label("Senha:"), 0, 3);
        gridForm.add(txtSenha, 1, 3);

        gridForm.add(new Label("CPF:"), 0, 4);
        gridForm.add(txtCpf, 1, 4);

        gridForm.add(new Label("Contato:"), 0, 5);
        gridForm.add(txtContato, 1, 5);

        // Coluna Direita: Endereço
        gridForm.add(new Label("Endereço"), 2, 0, 2, 1);

        gridForm.add(new Label("CEP:"), 2, 1);
        gridForm.add(txtCep, 3, 1);

        gridForm.add(new Label("Rua:"), 2, 2);
        gridForm.add(txtRua, 3, 2);

        gridForm.add(new Label("Número:"), 2, 3);
        gridForm.add(txtNumero, 3, 3);

        gridForm.add(new Label("Bairro:"), 2, 4);
        gridForm.add(txtBairro, 3, 4);

        gridForm.add(new Label("Cidade:"), 2, 5);
        gridForm.add(txtCidade, 3, 5);

        // Laço moderno para estilizar labels de forma dinâmica
        gridForm.getChildren().forEach(node -> {
            if (node instanceof Label) {
                Label l = (Label) node;
                if(l.getText().equals("Dados Pessoais") || l.getText().equals("Endereço")) {
                    l.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #353b48;");
                    GridPane.setMargin(l, new Insets(10, 0, 5, 0));
                } else {
                    l.setStyle("-fx-font-weight: bold; -fx-text-fill: #7f8fa6;");
                    GridPane.setHalignment(l, HPos.RIGHT); // Alinha o texto das labels à direita
                }
            }
        });

        layoutPrincipal.setCenter(gridForm);

        HBox boxBotoes = new HBox(20, btnVoltar, btnSalvar);
        boxBotoes.setAlignment(Pos.CENTER);
        boxBotoes.setPadding(new Insets(30, 0, 10, 0));
        layoutPrincipal.setBottom(boxBotoes);
    }

    private void configurarAcoes() {
        btnVoltar.setOnAction(e -> abrirTelaLogin());

        btnSalvar.setOnAction(e -> {
            try {
                if (txtNome.getText().isBlank() || txtEmail.getText().isBlank() || txtCpf.getText().isBlank()) {
                    throw new RuntimeException("Preencha todos os campos obrigatórios (Nome, E-mail e CPF).");
                }

                String enderecoFormatado = String.format("%s, %s - %s, %s. CEP: %s",
                        txtRua.getText(), txtNumero.getText(), txtBairro.getText(),
                        txtCidade.getText(), txtCep.getText());

                authController.cadastrarCliente(
                        txtNome.getText(), txtEmail.getText(), txtSenha.getText(),
                        txtCpf.getText(), txtContato.getText(), enderecoFormatado
                );

                exibirMensagem(Alert.AlertType.INFORMATION, "Sucesso", "Cadastro realizado com sucesso!");
                abrirTelaLogin();

            } catch (Exception ex) {
                exibirMensagem(Alert.AlertType.ERROR, "Erro", ex.getMessage());
            }
        });
    }

    private void abrirTelaLogin() {
        Stage stage = (Stage) layoutPrincipal.getScene().getWindow();
        stage.setScene(new Scene(new TelaLogin().getLayout(), 450, 550));
    }

    private void exibirMensagem(Alert.AlertType tipo, String titulo, String msg) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(msg);
        alerta.showAndWait();
    }

    public BorderPane getLayout() {
        return layoutPrincipal;
    }
}
