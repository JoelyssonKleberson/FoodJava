package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.controller.AutenticacaoController;
import br.edu.ifpb.ads.foodjava.repository.UsuarioRepository;
import br.edu.ifpb.ads.foodjava.repository.UsuarioRepositoryJsonImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TelaCadastrarCliente {

    private final String COR_PRINCIPAL = "#ea1d2c";
    private final String COR_FUNDO = "#fafafa";
    private final String COR_BOTAO_VOLTAR = "#747d8c";

    private AutenticacaoController loginController;
    private VBox telaPrincipal;

    public TelaCadastrarCliente() {
        // Criando a dependência do Controller também na tela de Cadastro
        UsuarioRepository repo = new UsuarioRepositoryJsonImpl();
        this.loginController = new AutenticacaoController(repo);

        telaPrincipal = new VBox(20);
        telaPrincipal.setAlignment(Pos.CENTER);
        telaPrincipal.setStyle("-fx-background-color: " + COR_FUNDO + ";");

        VBox cartao = new VBox(20);
        cartao.setAlignment(Pos.TOP_CENTER);
        cartao.setPadding(new Insets(40));
        cartao.setMaxWidth(700);
        cartao.setStyle("-fx-background-color: white; -fx-background-radius: 15px;");

        DropShadow sombra = new DropShadow();
        sombra.setColor(Color.rgb(0, 0, 0, 0.1));
        sombra.setRadius(15);
        cartao.setEffect(sombra);

        Label titulo = new Label("Criar Conta");
        titulo.setStyle("-fx-text-fill: " + COR_PRINCIPAL + "; -fx-font-size: 28px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        String estiloCampo = "-fx-pref-height: 35px; -fx-background-radius: 5px;";

        TextField txtNome = new TextField(); txtNome.setPromptText("Nome Completo"); txtNome.setStyle(estiloCampo);
        TextField txtCpf = new TextField(); txtCpf.setPromptText("CPF"); txtCpf.setStyle(estiloCampo);
        TextField txtTelefone = new TextField(); txtTelefone.setPromptText("Telefone/Celular"); txtTelefone.setStyle(estiloCampo);

        TextField txtEmail = new TextField(); txtEmail.setPromptText("E-mail"); txtEmail.setStyle(estiloCampo);
        PasswordField txtSenha = new PasswordField(); txtSenha.setPromptText("Senha"); txtSenha.setStyle(estiloCampo);
        TextField txtEndereco = new TextField(); txtEndereco.setPromptText("Endereço Completo"); txtEndereco.setStyle(estiloCampo);

        grid.add(new Label("Dados Pessoais:"), 0, 0);
        grid.add(txtNome, 0, 1);
        grid.add(txtCpf, 0, 2);
        grid.add(txtTelefone, 0, 3);

        grid.add(new Label("Acesso e Endereço:"), 1, 0);
        grid.add(txtEmail, 1, 1);
        grid.add(txtSenha, 1, 2);
        grid.add(txtEndereco, 1, 3);

        Button btnSalvar = new Button("Confirmar Cadastro");
        btnSalvar.setStyle("-fx-background-color: " + COR_PRINCIPAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-height: 40px; -fx-pref-width: 200px; -fx-background-radius: 8px; -fx-cursor: hand;");

        Button btnVoltar = new Button("Voltar");
        btnVoltar.setStyle("-fx-background-color: " + COR_BOTAO_VOLTAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-height: 40px; -fx-pref-width: 100px; -fx-background-radius: 8px; -fx-cursor: hand;");

        HBox botoes = new HBox(15, btnVoltar, btnSalvar);
        botoes.setAlignment(Pos.CENTER);
        botoes.setPadding(new Insets(20, 0, 0, 0));

        // Ações
        btnVoltar.setOnAction(e -> {
            Stage stage = (Stage) btnVoltar.getScene().getWindow();
            TelaLogin telaLogin = new TelaLogin();
            stage.setScene(new Scene(telaLogin.getLayout(), 1100, 700));
        });

        btnSalvar.setOnAction(e -> {
            try {
                // Chama o Controller, que cuida das validações (CPF, Senha, Duplicados)
                loginController.cadastrarCliente(
                        txtNome.getText(), txtEmail.getText(), txtSenha.getText(),
                        txtCpf.getText(), txtTelefone.getText(), txtEndereco.getText()
                );

                // Se não lançou exceção, deu certo!
                Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
                sucesso.setTitle("Sucesso!");
                sucesso.setHeaderText(null);
                sucesso.setContentText("Conta criada com sucesso! Faça seu login.");
                sucesso.showAndWait();

                // Volta para o login
                btnVoltar.fire();
            } catch (Exception ex) {
                Alert erro = new Alert(Alert.AlertType.ERROR);
                erro.setTitle("Erro no Cadastro");
                erro.setHeaderText(null);
                erro.setContentText(ex.getMessage());
                erro.showAndWait();
            }
        });

        cartao.getChildren().addAll(titulo, grid, botoes);
        telaPrincipal.getChildren().add(cartao);
    }

    public VBox getLayout() {
        return telaPrincipal;
    }
}
