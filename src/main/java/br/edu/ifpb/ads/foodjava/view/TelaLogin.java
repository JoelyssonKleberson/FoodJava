package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.controller.AutenticacaoController;
import br.edu.ifpb.ads.foodjava.model.Usuario;
import br.edu.ifpb.ads.foodjava.repository.UsuarioRepositoryJsonImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TelaLogin {

    private final AutenticacaoController authController;
    private final VBox layoutPrincipal;
    private TextField txtEmail;
    private PasswordField txtSenha;
    private Button btnEntrar, btnNovoCadastro;

    public TelaLogin() {
        this.authController = new AutenticacaoController(new UsuarioRepositoryJsonImpl());

        this.layoutPrincipal = new VBox(25);
        this.layoutPrincipal.setAlignment(Pos.CENTER);
        this.layoutPrincipal.setPadding(new Insets(40, 50, 40, 50));
        this.layoutPrincipal.setStyle("-fx-background-color: #ffffff;");

        montarInterface();
        configurarEventos();
    }

    private void montarInterface() {
        Label lblTitulo = new Label("FoodJava");
        lblTitulo.setStyle("-fx-font-size: 38px; -fx-font-weight: 900; -fx-text-fill: #e84118;");

        Label lblSubtitulo = new Label("Faça login para continuar");
        lblSubtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8fa6;");
        VBox.setMargin(lblSubtitulo, new Insets(-20, 0, 20, 0));

        txtEmail = new TextField();
        txtEmail.setPromptText("Seu endereço de e-mail");
        txtEmail.setStyle("-fx-background-radius: 5; -fx-padding: 10; -fx-font-size: 14px;");

        txtSenha = new PasswordField();
        txtSenha.setPromptText("Sua senha secreta");
        txtSenha.setStyle("-fx-background-radius: 5; -fx-padding: 10; -fx-font-size: 14px;");

        btnEntrar = new Button("Acessar Conta");
        btnEntrar.setMaxWidth(Double.MAX_VALUE);
        btnEntrar.setStyle("-fx-background-color: #e84118; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px; -fx-padding: 10; -fx-background-radius: 5; -fx-cursor: hand;");

        btnNovoCadastro = new Button("Não possui conta? Crie uma agora");
        btnNovoCadastro.setStyle("-fx-background-color: transparent; -fx-text-fill: #273c75; -fx-cursor: hand; -fx-underline: true;");

        layoutPrincipal.getChildren().addAll(lblTitulo, lblSubtitulo, txtEmail, txtSenha, btnEntrar, btnNovoCadastro);
    }

    private void configurarEventos() {
        btnEntrar.setOnAction(e -> {
            String email = txtEmail.getText().trim();
            String senha = txtSenha.getText();

            if (email.isEmpty() || senha.isEmpty()) {
                exibirAlerta(Alert.AlertType.WARNING, "Atenção", "Preencha e-mail e senha.");
                return;
            }

            Usuario user = authController.fazerLogin(email, senha);
            if (user != null) {
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Bem-vindo(a), " + user.getNome() + "!");
                // No futuro redimensionaremos a janela e iremos para o cardápio aqui
            } else {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", "Credenciais inválidas.");
            }
        });

        btnNovoCadastro.setOnAction(e -> {
            Stage stage = (Stage) btnNovoCadastro.getScene().getWindow();
            // A tela de cadastro com o GridPane precisa de um pouco mais de largura para caber as duas colunas
            stage.setScene(new Scene(new TelaCadastrarCliente().getLayout(), 700, 500));
        });
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String msg) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public VBox getLayout() {
        return layoutPrincipal;
    }
}