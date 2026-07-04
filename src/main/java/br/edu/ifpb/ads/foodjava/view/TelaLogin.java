package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.controller.AutenticacaoController;
import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.model.Usuario;
import br.edu.ifpb.ads.foodjava.repository.UsuarioRepository;
import br.edu.ifpb.ads.foodjava.repository.UsuarioRepositoryJsonImpl;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;

public class TelaLogin {

    private final String COR_VERMELHO = "#ea1d2c";
    private final String COR_AZUL_LINK = "#0984e3";

    private AutenticacaoController loginController;
    private HBox telaPrincipal;
    private VBox cartaoLogin; // Mantendo a referência para a tela de sucesso

    public TelaLogin() {
        UsuarioRepository repo = new UsuarioRepositoryJsonImpl();
        this.loginController = new AutenticacaoController(repo);

        telaPrincipal = new HBox();
        telaPrincipal.setAlignment(Pos.CENTER);

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

        VBox painelFormularioDireito = new VBox();
        painelFormularioDireito.setAlignment(Pos.CENTER);
        painelFormularioDireito.setPrefWidth(440);
        painelFormularioDireito.setMinWidth(440);
        painelFormularioDireito.setMaxWidth(440);
        painelFormularioDireito.setStyle("-fx-background-color: " + COR_VERMELHO + ";");

        cartaoLogin = new VBox(22);
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

        Label subtitulo = new Label("Digite suas credenciais abaixo");
        subtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e; -fx-font-weight: bold;");

        String estiloCampo = "-fx-pref-height: 48px; -fx-background-radius: 8px; -fx-font-size: 15px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-background-color: #f8fafc;";
        String estiloCampoSenha = estiloCampo + " -fx-padding: 0 40 0 10;";

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("exemplo@gmail.com"); // A pedido do cliente
        txtEmail.setStyle(estiloCampo);

        PasswordField txtSenhaOculta = new PasswordField();
        txtSenhaOculta.setPromptText("Mín. 8 caracteres e 1 número"); // A pedido do cliente
        txtSenhaOculta.setStyle(estiloCampoSenha);

        TextField txtSenhaVisivel = new TextField();
        txtSenhaVisivel.setPromptText("Mín. 8 caracteres e 1 número");
        txtSenhaVisivel.setStyle(estiloCampoSenha);
        txtSenhaVisivel.setVisible(false);
        txtSenhaVisivel.textProperty().bindBidirectional(txtSenhaOculta.textProperty());

        Button btnOlho = new Button("👁");
        btnOlho.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 16px; -fx-text-fill: #7f8c8d;");

        btnOlho.setOnAction(e -> {
            boolean isVisivel = txtSenhaVisivel.isVisible();
            txtSenhaVisivel.setVisible(!isVisivel);
            txtSenhaOculta.setVisible(isVisivel);
            btnOlho.setText(isVisivel ? "👁" : "🙈");
        });

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
                Usuario usuarioEncontrado = loginController.fazerLogin(email, senha);
                if (usuarioEncontrado != null) {

                    mostrarTelaSucessoEredirecionar(usuarioEncontrado);

                } else {
                    mostrarAlertaErro("Verifique se digitou o e-mail e senha corretamente.");
                }
            } catch (Exception ex) {
                mostrarAlertaErro(ex.getMessage());
            }
        });

        cartaoLogin.getChildren().addAll(titulo, subtitulo, txtEmail, stackSenha, btnEntrar, btnCadastrar);
        painelFormularioDireito.getChildren().add(cartaoLogin);
        telaPrincipal.getChildren().addAll(painelImagemEsquerdo, painelFormularioDireito);
    }

    // NOVO: Tela de Sucesso Embutida no Cartão! (Sem alertas)
    private void mostrarTelaSucessoEredirecionar(Usuario usuarioEncontrado) {
        cartaoLogin.getChildren().clear();

        Label check = new Label("✔️");
        check.setStyle("-fx-font-size: 60px; -fx-text-fill: #2ecc71;");

        Label lblSucesso = new Label("Login Efetuado!");
        lblSucesso.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label lblMsg = new Label("Bem-vindo(a), " + usuarioEncontrado.getNome());
        lblMsg.setStyle("-fx-font-size: 15px; -fx-text-fill: #7f8c8d;");

        cartaoLogin.getChildren().addAll(check, lblSucesso, lblMsg);
        cartaoLogin.setAlignment(Pos.CENTER);

        // Espera 3s e manda para a Home certa
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> {
            Stage stage = (Stage) telaPrincipal.getScene().getWindow();

            // O Roteador Inteligente: Ele checa a instância da classe!
            if (usuarioEncontrado instanceof Gerente) {
                TelaGerenteHome telaGerente = new TelaGerenteHome(usuarioEncontrado);
                stage.setScene(new Scene(telaGerente.getLayout(), 1100, 700));
            } else if (usuarioEncontrado instanceof Cliente) {
                TelaClienteHome telaCliente = new TelaClienteHome(usuarioEncontrado);
                stage.setScene(new Scene(telaCliente.getLayout(), 1100, 700));
            }
        });
        delay.play();
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Acesso Negado");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public HBox getLayout() {
        return telaPrincipal;
    }
}
