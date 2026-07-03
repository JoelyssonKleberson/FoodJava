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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.InputStream;

public class TelaLogin {

    private final String COR_VERMELHO = "#ea1d2c";
    private final String COR_AZUL_LINK = "#0984e3";

    private AutenticacaoController loginController;
    private HBox telaPrincipal;

    public TelaLogin() {
        UsuarioRepository repo = new UsuarioRepositoryJsonImpl();
        this.loginController = new AutenticacaoController(repo);

        telaPrincipal = new HBox();
        telaPrincipal.setAlignment(Pos.CENTER);

        // LADO ESQUERDO: Imagem
        StackPane painelImagemEsquerdo = new StackPane();
        painelImagemEsquerdo.setPrefWidth(660);
        painelImagemEsquerdo.setMinWidth(660);
        painelImagemEsquerdo.setMaxWidth(660);

        InputStream streamFundo = getClass().getResourceAsStream("/images/fundo_login.jpg");
        if (streamFundo != null) {
            Image imgFundo = new Image(streamFundo);
            BackgroundImage bImg = new BackgroundImage(imgFundo, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true));
            painelImagemEsquerdo.setBackground(new Background(bImg));
        } else {
            painelImagemEsquerdo.setStyle("-fx-background-color: #cccccc;");
        }

        // LADO DIREITO: Fundo Vermelho e Cartão
        VBox painelFormularioDireito = new VBox();
        painelFormularioDireito.setAlignment(Pos.CENTER);
        painelFormularioDireito.setPrefWidth(440);
        painelFormularioDireito.setMinWidth(440);
        painelFormularioDireito.setMaxWidth(440);
        painelFormularioDireito.setStyle("-fx-background-color: " + COR_VERMELHO + ";");

        VBox cartaoLogin = new VBox(22);
        cartaoLogin.setAlignment(Pos.TOP_CENTER);
        cartaoLogin.setPadding(new Insets(45, 35, 45, 35));
        cartaoLogin.setMaxWidth(380);
        cartaoLogin.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 16px;");

        DropShadow sombraPerimetro = new DropShadow();
        sombraPerimetro.setColor(Color.rgb(0, 0, 0, 0.25));
        sombraPerimetro.setRadius(25);
        cartaoLogin.setEffect(sombraPerimetro);

        Label titulo = new Label("Acessar Conta");
        titulo.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 28px; -fx-font-weight: bold;");

        // 1) MELHORIA: Subtítulo mais nítido e visível (Cor mais escura e negrito leve)
        Label subtitulo = new Label("Digite suas credenciais abaixo");
        subtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e; -fx-font-weight: bold;");

        String estiloCampo = "-fx-pref-height: 48px; -fx-background-radius: 8px; -fx-font-size: 15px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-background-color: #f8fafc;";

        // Estilo especial para a senha, com espaço extra na direita para o botão do olho não ficar em cima do texto
        String estiloCampoSenha = estiloCampo + " -fx-padding: 0 40 0 10;";

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("E-mail corporativo ou pessoal");
        txtEmail.setStyle(estiloCampo);

        // 2) MELHORIA: Sistema de "Mostrar Senha"
        PasswordField txtSenhaOculta = new PasswordField();
        txtSenhaOculta.setPromptText("Sua senha cadastrada");
        txtSenhaOculta.setStyle(estiloCampoSenha);

        TextField txtSenhaVisivel = new TextField();
        txtSenhaVisivel.setPromptText("Sua senha cadastrada");
        txtSenhaVisivel.setStyle(estiloCampoSenha);
        txtSenhaVisivel.setVisible(false); // Começa escondido

        // Sincroniza o texto digitado entre o campo oculto e o visível
        txtSenhaVisivel.textProperty().bindBidirectional(txtSenhaOculta.textProperty());

        Button btnOlho = new Button("👁");
        btnOlho.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 16px; -fx-text-fill: #7f8c8d;");

        btnOlho.setOnAction(e -> {
            boolean isVisivel = txtSenhaVisivel.isVisible();
            txtSenhaVisivel.setVisible(!isVisivel);
            txtSenhaOculta.setVisible(isVisivel);
            // Troca o ícone (Olho aberto / Macaquinho cobrindo o rosto ou olho cortado)
            btnOlho.setText(isVisivel ? "👁" : "🙈");
        });

        // Empilha o campo oculto, o visível e o botão do olho por cima de tudo à direita
        StackPane stackSenha = new StackPane(txtSenhaVisivel, txtSenhaOculta, btnOlho);
        StackPane.setAlignment(btnOlho, Pos.CENTER_RIGHT);
        StackPane.setMargin(btnOlho, new Insets(0, 5, 0, 0));

        Button btnEntrar = new Button("Entrar");
        btnEntrar.setPrefWidth(Double.MAX_VALUE);
        btnEntrar.setStyle("-fx-background-color: " + COR_VERMELHO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-height: 48px; -fx-background-radius: 8px; -fx-font-size: 16px; -fx-cursor: hand;");

        Button btnCadastrar = new Button("Ainda não tem conta? Cadastre-se");
        btnCadastrar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + COR_AZUL_LINK + "; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");

        btnCadastrar.setOnAction(e -> {
            Stage stage = (Stage) btnCadastrar.getScene().getWindow();
            TelaCadastrarCliente telaCad = new TelaCadastrarCliente();
            stage.setScene(new Scene(telaCad.getLayout(), 1100, 700));
        });

        btnEntrar.setOnAction(e -> {
            String email = txtEmail.getText();
            String senha = txtSenhaOculta.getText();
            try {
                var usuarioEncontrado = loginController.fazerLogin(email, senha);
                if (usuarioEncontrado != null) {

                    // CORREÇÃO: Exibindo um pop-up visual já que não temos a próxima tela!
                    Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
                    sucesso.setTitle("Acesso Permitido");
                    sucesso.setHeaderText(null);
                    sucesso.setContentText("Login efetuado com sucesso!\n\nBem-vindo(a), " + usuarioEncontrado.getNome() + "!");
                    sucesso.showAndWait();

                    System.out.println("Login efetuado com sucesso no console!");

                    // NO FUTURO: Aqui chamaremos a Tela do Cardápio!

                } else {
                    mostrarAlerta("Credenciais Inválidas", "Verifique se digitou o e-mail e senha corretamente.");
                }
            } catch (Exception ex) {
                mostrarAlerta("Erro de Conexão", ex.getMessage());
            }
        });

        cartaoLogin.getChildren().addAll(titulo, subtitulo, txtEmail, stackSenha, btnEntrar, btnCadastrar);
        painelFormularioDireito.getChildren().add(cartaoLogin);
        telaPrincipal.getChildren().addAll(painelImagemEsquerdo, painelFormularioDireito);
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
