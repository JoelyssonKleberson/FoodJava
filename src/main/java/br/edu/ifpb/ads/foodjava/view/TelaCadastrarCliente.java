package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.controller.AutenticacaoController;
import br.edu.ifpb.ads.foodjava.repository.UsuarioRepository;
import br.edu.ifpb.ads.foodjava.repository.UsuarioRepositoryJsonImpl;
import br.edu.ifpb.ads.foodjava.utils.ValidadorUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TelaCadastrarCliente {

    // Cores de Acordo com o seu novo design
    private final String COR_VERMELHO_FUNDO = "#ea1d2c";
    private final String COR_PROGRESSO = "black";
    private final String COR_BOTAO_DESATIVADO = "#dcdde1";

    private AutenticacaoController loginController;
    private VBox telaPrincipal;

    private int passoAtual = 1;

    // Componentes que mudam dinamicamente
    private VBox passo1Box, passo2Box, passo3Box;
    private ProgressBar progressBar;
    private Label lblEtapa;
    private Button btnProximo, btnVoltar;

    // Campos do Formulário
    private TextField txtEmail, txtNome, txtCpf, txtWhatsApp, txtRua, txtNumero, txtBairro;
    private PasswordField txtSenha;

    private boolean isUpdatingMask = false; // Controle para evitar loop na máscara

    public TelaCadastrarCliente() {
        UsuarioRepository repo = new UsuarioRepositoryJsonImpl();
        this.loginController = new AutenticacaoController(repo);

        telaPrincipal = new VBox();
        telaPrincipal.setAlignment(Pos.CENTER);
        // Fundo vermelho iFood igual ao splash
        telaPrincipal.setStyle("-fx-background-color: " + COR_VERMELHO_FUNDO + ";");

        // --- CARTÃO BRANCO ---
        VBox cartao = new VBox(20);
        cartao.setAlignment(Pos.TOP_CENTER);
        cartao.setPadding(new Insets(40, 50, 40, 50));
        cartao.setMaxWidth(480);
        cartao.setStyle("-fx-background-color: white; -fx-background-radius: 15px;");

        DropShadow sombra = new DropShadow();
        sombra.setColor(Color.rgb(0, 0, 0, 0.2));
        sombra.setRadius(25);
        cartao.setEffect(sombra);

        Label titulo = new Label("Cadastrar-se");
        titulo.setStyle("-fx-text-fill: " + COR_VERMELHO_FUNDO + "; -fx-font-size: 28px; -fx-font-weight: bold;");

        // --- CONSTRUINDO OS 3 PASSOS ---
        criarPasso1();
        criarPasso2();
        criarPasso3();

        // --- BARRA DE PROGRESSO ---
        progressBar = new ProgressBar(0.33);
        progressBar.setPrefWidth(Double.MAX_VALUE);
        progressBar.setPrefHeight(10);
        // Progress bar preta para combinar e contrastar bem
        progressBar.setStyle("-fx-accent: " + COR_PROGRESSO + ";");

        lblEtapa = new Label("Etapa 1 de 3");
        lblEtapa.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 13px;");

        VBox boxProgresso = new VBox(8, progressBar, lblEtapa);
        boxProgresso.setAlignment(Pos.CENTER);
        boxProgresso.setPadding(new Insets(15, 0, 10, 0));

        // --- BOTÕES DE AÇÃO ---
        btnVoltar = new Button("Voltar");
        btnVoltar.setStyle("-fx-background-color: transparent; -fx-text-fill: #7f8c8d; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 15px;");

        btnProximo = new Button("Próximo ➔");
        btnProximo.setPrefWidth(140);
        btnProximo.setPrefHeight(45); // Botão maior

        HBox boxBotoes = new HBox(150, btnVoltar, btnProximo);
        boxBotoes.setAlignment(Pos.CENTER);
        boxBotoes.setPadding(new Insets(10, 0, 0, 0));

        // Eventos dos Botões Principais
        btnVoltar.setOnAction(e -> {
            if (passoAtual > 1) {
                passoAtual--;
                atualizarVisaoDoWizard();
            } else {
                Stage stage = (Stage) btnVoltar.getScene().getWindow();
                stage.setScene(new Scene(new TelaLogin().getLayout(), 1100, 700));
            }
        });

        btnProximo.setOnAction(e -> {
            if (passoAtual < 3) {
                passoAtual++;
                atualizarVisaoDoWizard();
            } else {
                finalizarCadastro();
            }
        });

        cartao.getChildren().addAll(titulo, passo1Box, passo2Box, passo3Box, boxProgresso, boxBotoes);
        telaPrincipal.getChildren().add(cartao);

        atualizarVisaoDoWizard(); // Esconde os outros passos no início
    }

    // =========================================================
    // PASSO 1: ACESSO
    // =========================================================
    private void criarPasso1() {
        passo1Box = new VBox(15);
        Label lblSubtitulo = new Label("Dados de Acesso\n\nComo você fará login?");
        lblSubtitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        txtEmail = criarCampoTexto("E-mail", "exemplo@gmail.com");
        txtSenha = new PasswordField();
        txtSenha.setPromptText("Mínimo 8 caracteres e 1 número");
        estilizarCampo(txtSenha);

        txtEmail.textProperty().addListener((obs, o, n) -> validarPassoAtual());
        txtSenha.textProperty().addListener((obs, o, n) -> validarPassoAtual());

        passo1Box.getChildren().addAll(lblSubtitulo, new Label("E-mail"), txtEmail, new Label("Senha"), txtSenha);
    }

    // =========================================================
    // PASSO 2: DADOS PESSOAIS
    // =========================================================
    private void criarPasso2() {
        passo2Box = new VBox(15);
        Label lblSubtitulo = new Label("Dados Pessoais\n\nPrecisamos saber quem é você.");
        lblSubtitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        txtNome = criarCampoTexto("Nome Completo", "Seu nome e sobrenome");
        txtCpf = criarCampoTexto("CPF", "000.000.000-00");

        // Máscara e Validação CPF
        txtCpf.textProperty().addListener((obs, oldVal, newVal) -> {
            if (isUpdatingMask) return;
            isUpdatingMask = true;
            String limpo = newVal.replaceAll("\\D", "");
            if (limpo.length() > 11) limpo = limpo.substring(0, 11);

            StringBuilder mask = new StringBuilder();
            for (int i = 0; i < limpo.length(); i++) {
                if (i == 3 || i == 6) mask.append(".");
                if (i == 9) mask.append("-");
                mask.append(limpo.charAt(i));
            }
            txtCpf.setText(mask.toString());
            isUpdatingMask = false;
            validarPassoAtual();
        });

        txtNome.textProperty().addListener((obs, o, n) -> validarPassoAtual());

        passo2Box.getChildren().addAll(lblSubtitulo, new Label("Nome Completo"), txtNome, new Label("CPF"), txtCpf);
    }

    // =========================================================
    // PASSO 3: CONTATO E ENDEREÇO
    // =========================================================
    private void criarPasso3() {
        passo3Box = new VBox(10);
        Label lblSubtitulo = new Label("Contato e Entrega\n\nPara onde enviaremos seu pedido?");
        lblSubtitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        txtWhatsApp = criarCampoTexto("WhatsApp", "(00) 00000-0000");
        txtRua = criarCampoTexto("Rua/Avenida", "Nome da Rua");
        txtNumero = criarCampoTexto("Número", "Ex: 123");
        txtBairro = criarCampoTexto("Bairro", "Seu Bairro");

        HBox linhaEndereco = new HBox(10, txtRua, txtNumero);
        HBox.setHgrow(txtRua, Priority.ALWAYS);
        txtNumero.setPrefWidth(90);

        // Máscara WhatsApp
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

        passo3Box.getChildren().addAll(
                lblSubtitulo,
                new Label("WhatsApp"), txtWhatsApp,
                new Label("Endereço"), linhaEndereco, txtBairro
        );
    }

    // =========================================================
    // LÓGICA DO WIZARD (Validação Inteligente)
    // =========================================================
    private void atualizarVisaoDoWizard() {
        passo1Box.setVisible(passoAtual == 1); passo1Box.setManaged(passoAtual == 1);
        passo2Box.setVisible(passoAtual == 2); passo2Box.setManaged(passoAtual == 2);
        passo3Box.setVisible(passoAtual == 3); passo3Box.setManaged(passoAtual == 3);

        lblEtapa.setText("Etapa " + passoAtual + " de 3");
        progressBar.setProgress(passoAtual / 3.0);

        if (passoAtual == 3) {
            btnProximo.setText("Cadastrar-se");
        } else {
            btnProximo.setText("Próximo ➔");
        }

        validarPassoAtual(); // Força a revalidação ao trocar de tela
    }

    private void validarPassoAtual() {
        boolean valido = false;

        if (passoAtual == 1) {
            boolean emailOk = ValidadorUtil.isEmailGmailValido(txtEmail.getText());
            boolean senhaOk = ValidadorUtil.isSenhaValida(txtSenha.getText());
            marcarCampoVisual(txtEmail, emailOk);
            marcarCampoVisual(txtSenha, senhaOk);
            valido = emailOk && senhaOk;
        }
        else if (passoAtual == 2) {
            boolean nomeOk = ValidadorUtil.isNomeCompleto(txtNome.getText());
            boolean cpfOk = ValidadorUtil.isCpfValido(txtCpf.getText());
            marcarCampoVisual(txtNome, nomeOk);
            marcarCampoVisual(txtCpf, cpfOk);
            valido = nomeOk && cpfOk;
        }
        else if (passoAtual == 3) {
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

        // Habilita ou "desliga" o botão visualmente
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
            campo.setStyle(baseStyle + "-fx-border-color: #2ecc71; -fx-border-radius: 5px; -fx-border-width: 2px;"); // Verde Suave
        } else {
            campo.setStyle(baseStyle + "-fx-border-color: #e74c3c; -fx-border-radius: 5px; -fx-border-width: 2px; -fx-background-color: #fff0f0;"); // Vermelho e fundo rosado
        }
    }

    private void finalizarCadastro() {
        try {
            String enderecoCompleto = txtRua.getText() + ", " + txtNumero.getText() + " - " + txtBairro.getText();
            loginController.cadastrarCliente(
                    txtNome.getText(), txtEmail.getText(), txtSenha.getText(),
                    txtCpf.getText(), txtWhatsApp.getText(), enderecoCompleto
            );

            Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
            sucesso.setTitle("Sucesso!");
            sucesso.setHeaderText(null);
            sucesso.setContentText("Conta criada com sucesso! Faça seu login.");
            sucesso.showAndWait();

            btnVoltar.fire();
        } catch (Exception ex) {
            Alert erro = new Alert(Alert.AlertType.ERROR);
            erro.setTitle("Erro no Cadastro");
            erro.setContentText(ex.getMessage());
            erro.showAndWait();
        }
    }

    private TextField criarCampoTexto(String nome, String prompt) {
        TextField txt = new TextField();
        txt.setPromptText(prompt);
        estilizarCampo(txt);
        return txt;
    }

    private void estilizarCampo(Control campo) {
        campo.setStyle("-fx-pref-height: 45px; -fx-background-radius: 5px; -fx-font-size: 15px; -fx-border-color: #e0e0e0; -fx-border-radius: 5px;");
    }

    public VBox getLayout() {
        return telaPrincipal;
    }
}
