package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.controller.RestauranteController;
import br.edu.ifpb.ads.foodjava.repository.RestauranteRepositoryJsonImpl;
import br.edu.ifpb.ads.foodjava.repository.UsuarioRepositoryJsonImpl;
import br.edu.ifpb.ads.foodjava.utils.ValidadorUtil;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;

public class TelaConfiguracaoRestaurante {

    private final String COR_VERMELHO_FUNDO = "#ea1d2c";
    private final String COR_PROGRESSO = "black";
    private final String COR_BOTAO_DESATIVADO = "#dcdde1";

    private RestauranteController restauranteController;
    private VBox telaPrincipal;
    private VBox cartao; // O cartão branco onde tudo acontece

    private int passoAtual = 1;
    private VBox passo1Box, passo2Box, passo3Box;
    private ProgressBar progressBar;
    private Label lblEtapa;
    private Button btnProximo;

    // Campos
    private TextField txtNomeRestaurante, txtCnpj, txtWhatsApp, txtRua, txtNumero, txtBairro, txtEmail;
    private ComboBox<String> cbCategoria;
    private PasswordField txtSenhaOculta;
    private TextField txtSenhaVisivel;

    private boolean isUpdatingMask = false;

    public TelaConfiguracaoRestaurante() {
        this.restauranteController = new RestauranteController(new RestauranteRepositoryJsonImpl(), new UsuarioRepositoryJsonImpl());

        telaPrincipal = new VBox();
        telaPrincipal.setAlignment(Pos.CENTER);
        telaPrincipal.setStyle("-fx-background-color: " + COR_VERMELHO_FUNDO + ";");

        cartao = new VBox(15);
        cartao.setAlignment(Pos.TOP_CENTER);
        cartao.setPadding(new Insets(30, 50, 30, 50));
        cartao.setMaxWidth(480);
        cartao.setStyle("-fx-background-color: white; -fx-background-radius: 15px;");

        DropShadow sombra = new DropShadow();
        sombra.setColor(Color.rgb(0, 0, 0, 0.2));
        sombra.setRadius(25);
        cartao.setEffect(sombra);

        // Logo muito maior no topo
        ImageView imgLogo = new ImageView();
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/logotipo.png"));
            imgLogo.setImage(logo);
            imgLogo.setFitHeight(110);
            imgLogo.setPreserveRatio(true);
        } catch (Exception e) {}

        Label titulo = new Label("Bem-vindo ao FoodJava!");
        titulo.setStyle("-fx-text-fill: " + COR_VERMELHO_FUNDO + "; -fx-font-size: 26px; -fx-font-weight: bold;");

        Label subtitulo = new Label("Vamos configurar os dados do seu Restaurante");
        subtitulo.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 16px; -fx-font-weight: bold;");

        criarPasso1();
        criarPasso2();
        criarPasso3();

        progressBar = new ProgressBar(0.33);
        progressBar.setPrefWidth(Double.MAX_VALUE);
        progressBar.setPrefHeight(10);
        progressBar.setStyle("-fx-accent: " + COR_PROGRESSO + ";");

        lblEtapa = new Label("Etapa 1 de 3");
        lblEtapa.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 13px;");

        VBox boxProgresso = new VBox(8, progressBar, lblEtapa);
        boxProgresso.setAlignment(Pos.CENTER);
        boxProgresso.setPadding(new Insets(10, 0, 5, 0));

        btnProximo = new Button("Próximo ➔");
        btnProximo.setPrefWidth(Double.MAX_VALUE); // Botão ocupando a largura igual no login
        btnProximo.setPrefHeight(45);

        btnProximo.setOnAction(e -> {
            if (passoAtual < 3) {
                passoAtual++;
                atualizarVisaoDoWizard();
            } else {
                finalizarConfiguracao();
            }
        });

        cartao.getChildren().addAll(imgLogo, titulo, subtitulo, passo1Box, passo2Box, passo3Box, boxProgresso, btnProximo);
        telaPrincipal.getChildren().add(cartao);

        atualizarVisaoDoWizard();
    }

    private void criarPasso1() {
        passo1Box = new VBox(10);
        Label lblSecao = new Label("Dados do Restaurante");
        lblSecao.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;"); // Maior e Preto

        txtNomeRestaurante = criarCampoTexto("Nome do restaurante", "Ex.: Restaurante FoodJava");
        txtCnpj = criarCampoTexto("CNPJ", "00.000.000/0000-00");

        cbCategoria = new ComboBox<>();
        cbCategoria.getItems().addAll("Pizzaria", "Hamburgueria", "Comida Brasileira", "Japonesa", "Lanchonete", "Outros");
        cbCategoria.setPromptText("Selecione a Categoria");
        cbCategoria.setStyle("-fx-pref-height: 45px; -fx-background-radius: 5px; -fx-font-size: 15px; -fx-border-color: #e0e0e0; -fx-border-radius: 5px;");
        cbCategoria.setMaxWidth(Double.MAX_VALUE);

        // Máscara CNPJ
        txtCnpj.textProperty().addListener((obs, oldVal, newVal) -> {
            if (isUpdatingMask) return;
            isUpdatingMask = true;
            String limpo = newVal.replaceAll("\\D", "");
            if (limpo.length() > 14) limpo = limpo.substring(0, 14);

            StringBuilder mask = new StringBuilder();
            for (int i = 0; i < limpo.length(); i++) {
                if (i == 2 || i == 5) mask.append(".");
                if (i == 8) mask.append("/");
                if (i == 12) mask.append("-");
                mask.append(limpo.charAt(i));
            }
            txtCnpj.setText(mask.toString());
            isUpdatingMask = false;
            validarPassoAtual();
        });

        txtNomeRestaurante.textProperty().addListener((obs, o, n) -> validarPassoAtual());
        cbCategoria.valueProperty().addListener((obs, o, n) -> validarPassoAtual());

        passo1Box.getChildren().addAll(lblSecao, new Label("Nome"), txtNomeRestaurante, new Label("CNPJ"), txtCnpj, new Label("Categoria"), cbCategoria);
    }

    private void criarPasso2() {
        passo2Box = new VBox(10);
        Label lblSecao = new Label("Contato e Entrega");
        lblSecao.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        txtWhatsApp = criarCampoTexto("WhatsApp", "(00) 00000-0000");
        txtRua = criarCampoTexto("Rua/Avenida", "Nome da Rua");
        txtNumero = criarCampoTexto("Número", "Nº");
        txtNumero.setPrefWidth(90); txtNumero.setMinWidth(90); txtNumero.setMaxWidth(90);
        txtBairro = criarCampoTexto("Bairro", "Seu Bairro");

        HBox linhaEndereco = new HBox(10, txtRua, txtNumero);
        HBox.setHgrow(txtRua, Priority.ALWAYS);

        txtWhatsApp.textProperty().addListener((obs, oldVal, newVal) -> {
            if (isUpdatingMask) return;
            isUpdatingMask = true;
            String limpo = newVal.replaceAll("\\D", "");
            if (limpo.length() > 11) limpo = limpo.substring(0, 11);

            StringBuilder mask = new StringBuilder();
            for (int i = 0; i < limpo.length(); i++) {
                if (i == 0) mask.append("(");
                if (i == 2) mask.append(") ");
                if (i == 7) mask.append("-");
                mask.append(limpo.charAt(i));
            }
            txtWhatsApp.setText(mask.toString());
            isUpdatingMask = false;
            validarPassoAtual();
        });

        txtRua.textProperty().addListener((obs, o, n) -> validarPassoAtual());
        txtNumero.textProperty().addListener((obs, o, n) -> validarPassoAtual());
        txtBairro.textProperty().addListener((obs, o, n) -> validarPassoAtual());

        passo2Box.getChildren().addAll(lblSecao, new Label("WhatsApp"), txtWhatsApp, new Label("Endereço"), linhaEndereco, txtBairro);
    }

    private void criarPasso3() {
        passo3Box = new VBox(10);
        Label lblSecao = new Label("Dados do Gerente");
        lblSecao.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        txtEmail = criarCampoTexto("E-mail", "exemplo@gmail.com");

        String estiloCampoSenha = "-fx-pref-height: 45px; -fx-background-radius: 5px; -fx-font-size: 15px; -fx-border-color: #e0e0e0; -fx-border-radius: 5px; -fx-padding: 0 40 0 10;";

        txtSenhaOculta = new PasswordField();
        txtSenhaOculta.setPromptText("Mín. 8 caracteres e 1 número");
        txtSenhaOculta.setStyle(estiloCampoSenha);

        txtSenhaVisivel = new TextField();
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

        txtEmail.textProperty().addListener((obs, o, n) -> validarPassoAtual());
        txtSenhaOculta.textProperty().addListener((obs, o, n) -> validarPassoAtual());

        passo3Box.getChildren().addAll(lblSecao, new Label("E-mail"), txtEmail, new Label("Senha"), stackSenha);
    }

    private void atualizarVisaoDoWizard() {
        passo1Box.setVisible(passoAtual == 1); passo1Box.setManaged(passoAtual == 1);
        passo2Box.setVisible(passoAtual == 2); passo2Box.setManaged(passoAtual == 2);
        passo3Box.setVisible(passoAtual == 3); passo3Box.setManaged(passoAtual == 3);

        lblEtapa.setText("Etapa " + passoAtual + " de 3");
        progressBar.setProgress(passoAtual / 3.0);

        if (passoAtual == 3) {
            btnProximo.setText("Salvar e Iniciar");
        } else {
            btnProximo.setText("Próximo ➔");
        }
        validarPassoAtual();
    }

    private void validarPassoAtual() {
        boolean valido = false;

        if (passoAtual == 1) {
            boolean nomeOk = !txtNomeRestaurante.getText().trim().isEmpty();
            boolean cnpjOk = ValidadorUtil.isCnpjValido(txtCnpj.getText());
            boolean catOk = cbCategoria.getValue() != null;
            marcarCampoVisual(txtNomeRestaurante, nomeOk);
            marcarCampoVisual(txtCnpj, cnpjOk);
            if (catOk) cbCategoria.setStyle("-fx-pref-height: 45px; -fx-background-radius: 5px; -fx-font-size: 15px; -fx-border-color: #2ecc71; -fx-border-radius: 5px; -fx-border-width: 2px;");
            else cbCategoria.setStyle("-fx-pref-height: 45px; -fx-background-radius: 5px; -fx-font-size: 15px; -fx-border-color: #e0e0e0; -fx-border-radius: 5px;");
            valido = nomeOk && cnpjOk && catOk;
        }
        else if (passoAtual == 2) {
            boolean whatsOk = ValidadorUtil.isTelefoneWhatsAppValido(txtWhatsApp.getText());
            boolean ruaOk = !txtRua.getText().trim().isEmpty();
            boolean numOk = !txtNumero.getText().trim().isEmpty();
            boolean bairroOk = !txtBairro.getText().trim().isEmpty();
            marcarCampoVisual(txtWhatsApp, whatsOk);
            marcarCampoVisual(txtRua, ruaOk);
            marcarCampoVisual(txtNumero, numOk);
            marcarCampoVisual(txtBairro, bairroOk);
            valido = whatsOk && ruaOk && numOk && bairroOk;
        }
        else if (passoAtual == 3) {
            boolean emailOk = ValidadorUtil.isEmailGmailValido(txtEmail.getText());
            boolean senhaOk = ValidadorUtil.isSenhaValida(txtSenhaOculta.getText());
            marcarCampoVisual(txtEmail, emailOk);
            marcarCampoVisualFormatoSenha(txtSenhaOculta, txtSenhaVisivel, senhaOk);
            valido = emailOk && senhaOk;
        }

        btnProximo.setDisable(!valido);
        if (valido) {
            btnProximo.setStyle("-fx-background-color: " + COR_VERMELHO_FUNDO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 8px; -fx-cursor: hand;");
        } else {
            btnProximo.setStyle("-fx-background-color: " + COR_BOTAO_DESATIVADO + "; -fx-text-fill: #7f8c8d; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 8px;");
        }
    }

    private void marcarCampoVisual(Control campo, boolean valido) {
        String baseStyle = "-fx-pref-height: 45px; -fx-background-radius: 5px; -fx-font-size: 15px; ";
        if (campo instanceof TextField && ((TextField) campo).getText().isEmpty()) {
            campo.setStyle(baseStyle + "-fx-border-color: #e0e0e0; -fx-border-radius: 5px;");
        } else if (valido) {
            campo.setStyle(baseStyle + "-fx-border-color: #2ecc71; -fx-border-radius: 5px; -fx-border-width: 2px;");
        } else {
            campo.setStyle(baseStyle + "-fx-border-color: #e74c3c; -fx-border-radius: 5px; -fx-border-width: 2px; -fx-background-color: #fff0f0;");
        }
    }

    private void marcarCampoVisualFormatoSenha(PasswordField oculto, TextField visivel, boolean valido) {
        String baseStyle = "-fx-pref-height: 45px; -fx-background-radius: 5px; -fx-font-size: 15px; -fx-padding: 0 40 0 10; ";
        String estiloExtra = "";
        if (oculto.getText().isEmpty()) {
            estiloExtra = "-fx-border-color: #e0e0e0; -fx-border-radius: 5px;";
        } else if (valido) {
            estiloExtra = "-fx-border-color: #2ecc71; -fx-border-radius: 5px; -fx-border-width: 2px;";
        } else {
            estiloExtra = "-fx-border-color: #e74c3c; -fx-border-radius: 5px; -fx-border-width: 2px; -fx-background-color: #fff0f0;";
        }
        oculto.setStyle(baseStyle + estiloExtra);
        visivel.setStyle(baseStyle + estiloExtra);
    }

    private void finalizarConfiguracao() {
        try {
            String cnpjLimpo = txtCnpj.getText().replaceAll("\\D", "");
            String endereco = txtRua.getText() + ", " + txtNumero.getText() + " - " + txtBairro.getText();

            restauranteController.configurarRestaurante(
                    txtNomeRestaurante.getText(), cnpjLimpo, endereco, txtWhatsApp.getText(),
                    cbCategoria.getValue(), txtEmail.getText(), txtSenhaOculta.getText()
            );

            mostrarTelaSucessoEvoltarParaLogin();

        } catch (Exception ex) {
            // Em caso de erro de banco, mostraremos um alerta comum mesmo
            Alert erro = new Alert(Alert.AlertType.ERROR);
            erro.setTitle("Erro");
            erro.setContentText(ex.getMessage());
            erro.showAndWait();
        }
    }

    // O Sistema Elegante de Transição (Fim das janelinhas feias do Windows!)
    private void mostrarTelaSucessoEvoltarParaLogin() {
        cartao.getChildren().clear(); // Limpa o formulário do cartão

        Label check = new Label("✔️");
        check.setStyle("-fx-font-size: 60px; -fx-text-fill: #2ecc71;");

        Label lblSucesso = new Label("Configuração Concluída!");
        lblSucesso.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label lblMsg = new Label("Tudo pronto. Redirecionando para o login...");
        lblMsg.setStyle("-fx-font-size: 15px; -fx-text-fill: #7f8c8d;");

        cartao.getChildren().addAll(check, lblSucesso, lblMsg);
        cartao.setAlignment(Pos.CENTER);

        // Espera 2.5 segundos e vai para o Login sozinho
        PauseTransition delay = new PauseTransition(Duration.seconds(2.5));
        delay.setOnFinished(e -> {
            Stage stage = (Stage) telaPrincipal.getScene().getWindow();
            stage.setScene(new Scene(new TelaLogin().getLayout(), 1100, 700));
        });
        delay.play();
    }

    private TextField criarCampoTexto(String nome, String prompt) {
        TextField txt = new TextField();
        txt.setPromptText(prompt);
        txt.setStyle("-fx-pref-height: 45px; -fx-background-radius: 5px; -fx-font-size: 15px; -fx-border-color: #e0e0e0; -fx-border-radius: 5px;");
        return txt;
    }

    public VBox getLayout() {
        return telaPrincipal;
    }
}
