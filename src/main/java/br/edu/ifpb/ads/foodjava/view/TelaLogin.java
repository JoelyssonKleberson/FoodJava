package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.controller.AutenticacaoController;
import br.edu.ifpb.ads.foodjava.repository.UsuarioRepository;
import br.edu.ifpb.ads.foodjava.repository.UsuarioRepositoryJsonImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.InputStream;

public class TelaLogin {

    // --- VARIÁVEIS DE DESIGN ---
    private final String COR_PRINCIPAL = "#ea1d2c";
    private final String COR_FUNDO = "#f4f4f4";
    private final String COR_BOTAO_SEC = "#747d8c";

    private AutenticacaoController loginController;
    private HBox telaPrincipal;

    public TelaLogin() {
        UsuarioRepository repo = new UsuarioRepositoryJsonImpl();
        this.loginController = new AutenticacaoController(repo);

        telaPrincipal = new HBox();
        telaPrincipal.setStyle("-fx-background-color: " + COR_FUNDO + ";");
        telaPrincipal.setAlignment(Pos.CENTER);

        // ==========================================
        // LADO ESQUERDO (Logo com Fundo Personalizado)
        // ==========================================
        VBox ladoEsquerdo = new VBox();
        ladoEsquerdo.setAlignment(Pos.CENTER);
        ladoEsquerdo.setPrefWidth(550);

        // Tentando aplicar uma imagem de fundo (entregador/comida) atrás da logo
        InputStream isFundo = getClass().getResourceAsStream("/images/fundo_login.jpg");
        if (isFundo != null) {
            Image imgFundo = new Image(isFundo);
            BackgroundImage bImg = new BackgroundImage(imgFundo,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true));
            ladoEsquerdo.setBackground(new Background(bImg));
        } else {
            // Se não tiver imagem, aplica um degradê vermelho lindíssimo estilo iFood
            ladoEsquerdo.setStyle("-fx-background-color: linear-gradient(to bottom right, #ea1d2c, #ff5e62);");
        }

        // A logo grandona
        ImageView imgLogo = new ImageView();
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/logotipo.png"));
            imgLogo.setImage(logo);
            imgLogo.setFitWidth(350); // Aumentado!
            imgLogo.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Aviso: logotipo.png não encontrada.");
        }

        // Efeito de sombra leve apenas na logo para destacá-class do fundo
        DropShadow sombraLogo = new DropShadow();
        sombraLogo.setColor(Color.rgb(0, 0, 0, 0.3));
        sombraLogo.setRadius(20);
        imgLogo.setEffect(sombraLogo);

        ladoEsquerdo.getChildren().add(imgLogo);

        // ==========================================
        // LADO DIREITO (Formulário Branco Aumentado)
        // ==========================================
        VBox ladoDireito = new VBox(20);
        ladoDireito.setAlignment(Pos.CENTER);
        ladoDireito.setPrefWidth(550);

        VBox cartaoLogin = new VBox(20); // Espaçamento interno maior
        cartaoLogin.setAlignment(Pos.TOP_CENTER);
        cartaoLogin.setPadding(new Insets(50)); // Margens aumentadas
        cartaoLogin.setMaxWidth(450); // CARTÃO MAIS LARGO (era 350)
        cartaoLogin.setStyle("-fx-background-color: white; -fx-background-radius: 15px;");

        DropShadow sombraCartao = new DropShadow();
        sombraCartao.setColor(Color.rgb(0, 0, 0, 0.1));
        sombraCartao.setRadius(20);
        cartaoLogin.setEffect(sombraCartao);

        Label titulo = new Label("Bem-vindo!");
        titulo.setStyle("-fx-text-fill: " + COR_PRINCIPAL + "; -fx-font-size: 34px; -fx-font-weight: bold;"); // Fonte maior

        Label subtitulo = new Label("Faça login para continuar");
        subtitulo.setStyle("-fx-font-size: 16px; -fx-text-fill: #666666;");

        // Estilo compartilhado para os campos de texto (Mais altos e confortáveis)
        String estiloCampo = "-fx-pref-height: 50px; -fx-background-radius: 8px; -fx-font-size: 16px; -fx-border-color: #e0e0e0; -fx-border-radius: 8px; -fx-background-color: #fafafa;";

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Digite seu e-mail");
        txtEmail.setStyle(estiloCampo);

        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("Sua senha secreta");
        txtSenha.setStyle(estiloCampo);

        Button btnEntrar = new Button("Acessar Conta");
        btnEntrar.setPrefWidth(Double.MAX_VALUE);
        // Botão mais alto e com fonte maior
        btnEntrar.setStyle("-fx-background-color: " + COR_PRINCIPAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-height: 50px; -fx-background-radius: 8px; -fx-font-size: 18px; -fx-cursor: hand;");

        Button btnCadastrar = new Button("Novo por aqui? Crie sua conta");
        btnCadastrar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + COR_BOTAO_SEC + "; -fx-font-size: 15px; -fx-cursor: hand;");

        // Ações dos Botões
        btnCadastrar.setOnAction(e -> {
            Stage stage = (Stage) btnCadastrar.getScene().getWindow();
            TelaCadastrarCliente telaCad = new TelaCadastrarCliente();
            stage.setScene(new Scene(telaCad.getLayout(), 1100, 700));
        });

        btnEntrar.setOnAction(e -> {
            String email = txtEmail.getText();
            String senha = txtSenha.getText();
            try {
                var usuarioEncontrado = loginController.fazerLogin(email, senha);
                if (usuarioEncontrado != null) {
                    System.out.println("Login de sucesso para: " + usuarioEncontrado.getNome());
                } else {
                    mostrarAlerta("Erro", "E-mail ou senha incorretos.");
                }
            } catch (Exception ex) {
                mostrarAlerta("Erro no Login", ex.getMessage());
            }
        });

        cartaoLogin.getChildren().addAll(titulo, subtitulo, txtEmail, txtSenha, btnEntrar, btnCadastrar);
        ladoDireito.getChildren().add(cartaoLogin);
        telaPrincipal.getChildren().addAll(ladoEsquerdo, ladoDireito);
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public HBox getLayout() {
        return telaPrincipal;
    }
}
