package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.controller.AutenticacaoController;
import br.edu.ifpb.ads.foodjava.model.Usuario;
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

    // Definições de cores e constantes visuais de acordo com o pedido
    private final String COR_VERMELHO = "#ea1d2c";
    private final String COR_AZUL_LINK = "#0984e3";

    private AutenticacaoController loginController;
    private HBox telaPrincipal;

    public TelaLogin() {
        // Inicializa o repositório JSON e injeta no Controller do Back-end
        UsuarioRepository repo = new UsuarioRepositoryJsonImpl();
        this.loginController = new AutenticacaoController(repo);

        telaPrincipal = new HBox();
        telaPrincipal.setAlignment(Pos.CENTER);

        // ==========================================
        // LADO ESQUERDO: Imagem do Entregador (60%)
        // ==========================================
        StackPane painelImagemEsquerdo = new StackPane();
        painelImagemEsquerdo.setPrefWidth(660);
        painelImagemEsquerdo.setMinWidth(660);
        painelImagemEsquerdo.setMaxWidth(660);

        InputStream streamFundo = getClass().getResourceAsStream("/images/fundo_login.jpg");
        if (streamFundo != null) {
            Image imgFundo = new Image(streamFundo);
            BackgroundImage bImg = new BackgroundImage(
                    imgFundo,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true)
            );
            painelImagemEsquerdo.setBackground(new Background(bImg));
        } else {
            painelImagemEsquerdo.setStyle("-fx-background-color: #cccccc;");
        }

        // ==========================================
        // LADO DIREITO: Fundo Vermelho e Cartão (40%)
        // ==========================================
        VBox painelFormularioDireito = new VBox();
        painelFormularioDireito.setAlignment(Pos.CENTER);
        painelFormularioDireito.setPrefWidth(440);
        painelFormularioDireito.setMinWidth(440);
        painelFormularioDireito.setMaxWidth(440);
        painelFormularioDireito.setStyle("-fx-background-color: " + COR_VERMELHO + ";");

        // Cartão branco de Login com cantos arredondados
        VBox cartaoLogin = new VBox(22);
        cartaoLogin.setAlignment(Pos.TOP_CENTER);
        cartaoLogin.setPadding(new Insets(45, 35, 45, 35));
        cartaoLogin.setMaxWidth(380);
        cartaoLogin.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 16px;");

        // Perímetro de sombra para profundidade
        DropShadow sombraPerimetro = new DropShadow();
        sombraPerimetro.setColor(Color.rgb(0, 0, 0, 0.25));
        sombraPerimetro.setRadius(25);
        cartaoLogin.setEffect(sombraPerimetro);

        Label titulo = new Label("Acessar Conta");
        titulo.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 28px; -fx-font-weight: bold;");

        // Subtítulo nítido, visível e escuro
        Label subtitulo = new Label("Digite suas credenciais abaixo");
        subtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e; -fx-font-weight: bold;");

        // Estilos para campos de texto
        String estiloCampo = "-fx-pref-height: 48px; -fx-background-radius: 8px; -fx-font-size: 15px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-background-color: #f8fafc;";
        String estiloCampoSenha = estiloCampo + " -fx-padding: 0 40 0 10;";

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("E-mail corporativo ou pessoal");
        txtEmail.setStyle(estiloCampo);

        // Sistema dinâmico de mostrar/ocultar senha
        PasswordField txtSenhaOculta = new PasswordField();
        txtSenhaOculta.setPromptText("Sua senha cadastrada");
        txtSenhaOculta.setStyle(estiloCampoSenha);

        TextField txtSenhaVisivel = new TextField();
        txtSenhaVisivel.setPromptText("Sua senha cadastrada");
        txtSenhaVisivel.setStyle(estiloCampoSenha);
        txtSenhaVisivel.setVisible(false);

        // Vincula de forma bidirecional os dois inputs de senha
        txtSenhaVisivel.textProperty().bindBidirectional(txtSenhaOculta.textProperty());

        // Botão de alternar visualização (Olho)
        Button btnOlho = new Button("👁");
        btnOlho.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 16px; -fx-text-fill: #7f8c8d;");

        btnOlho.setOnAction(e -> {
            boolean isVisivel = txtSenhaVisivel.isVisible();
            txtSenhaVisivel.setVisible(!isVisivel);
            txtSenhaOculta.setVisible(isVisivel);
            btnOlho.setText(isVisivel ? "👁" : "🙈");
        });

        // Agrupamento dos campos de senha e do ícone de olho
        StackPane stackSenha = new StackPane(txtSenhaVisivel, txtSenhaOculta, btnOlho);
        StackPane.setAlignment(btnOlho, Pos.CENTER_RIGHT);
        StackPane.setMargin(btnOlho, new Insets(0, 5, 0, 0));

        Button btnEntrar = new Button("Entrar");
        btnEntrar.setPrefWidth(Double.MAX_VALUE);
        btnEntrar.setStyle("-fx-background-color: " + COR_VERMELHO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-height: 48px; -fx-background-radius: 8px; -fx-font-size: 16px; -fx-cursor: hand;");

        // Link de cadastro estilizado na cor azul destacada
        Button btnCadastrar = new Button("Ainda não tem conta? Cadastre-se");
        btnCadastrar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + COR_AZUL_LINK + "; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");

        // Eventos e Navegações
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
                    System.out.println("Login efetuado com sucesso no console!");

                    // Navegação real para a tela do cardápio enviando os dados do usuário autenticado
                    Stage stage = (Stage) btnEntrar.getScene().getWindow();
                    TelaClienteHome telaHome = new TelaClienteHome(usuarioEncontrado);
                    stage.setScene(new Scene(telaHome.getLayout(), 1100, 700));

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
