package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.controller.CardapioController;
import br.edu.ifpb.ads.foodjava.enums.Categoria;
import br.edu.ifpb.ads.foodjava.model.Usuario;
import br.edu.ifpb.ads.foodjava.repository.CardapioRepository;
import br.edu.ifpb.ads.foodjava.repository.CardapioRepositoryJsonImpl;
import br.edu.ifpb.ads.foodjava.repository.PedidoRepository;
import br.edu.ifpb.ads.foodjava.repository.PedidoRepositoryJsonImpl;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TelaGerenteHome {

    private final String COR_SIDEBAR = "#2c3e50";
    private final String COR_VERMELHO = "#ea1d2c";
    private final String COR_FUNDO = "#f4f4f4";

    private BorderPane telaPrincipal;
    private Usuario gerenteLogado;
    private CardapioController cardapioController;

    // Elementos de controle de abas
    private Button btnPedidos;
    private Button btnCardapio;

    public TelaGerenteHome(Usuario gerenteLogado) {
        this.gerenteLogado = gerenteLogado;

        CardapioRepository repoCardapio = new CardapioRepositoryJsonImpl();
        PedidoRepository repoPedido = new PedidoRepositoryJsonImpl();
        this.cardapioController = new CardapioController(repoCardapio, repoPedido);

        telaPrincipal = new BorderPane();
        telaPrincipal.setStyle("-fx-background-color: " + COR_FUNDO + ";");

        telaPrincipal.setLeft(criarSidebar());

        // Inicia na aba de pedidos (Kanban)
        abrirAbaPedidos();
    }

    private VBox criarSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: " + COR_SIDEBAR + ";");
        sidebar.setPadding(new Insets(30, 20, 30, 20));

        Label lblAdmin = new Label("Painel Gerencial");
        lblAdmin.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");

        Label lblNomeGerente = new Label("Olá, " + (gerenteLogado != null ? gerenteLogado.getNome() : "Gerente"));
        lblNomeGerente.setStyle("-fx-text-fill: #bdc3c7; -fx-font-size: 14px;");

        VBox boxHeader = new VBox(5, lblAdmin, lblNomeGerente);
        boxHeader.setPadding(new Insets(0, 0, 30, 0));

        btnPedidos = criarBotaoSidebar("📋 Pedidos do Dia");
        btnCardapio = criarBotaoSidebar("🍔 Gerenciar Cardápio");

        btnPedidos.setOnAction(e -> abrirAbaPedidos());
        btnCardapio.setOnAction(e -> abrirAbaCardapio());

        Region espacador = new Region();
        VBox.setVgrow(espacador, Priority.ALWAYS);

        Button btnSair = new Button("🚪 Sair do Sistema");
        btnSair.setMaxWidth(Double.MAX_VALUE);
        btnSair.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #e74c3c; -fx-font-size: 15px; -fx-font-weight: bold; -fx-alignment: CENTER_LEFT; -fx-padding: 10 20 10 20; -fx-cursor: hand; -fx-border-color: #e74c3c; -fx-border-width: 2px; -fx-border-radius: 8px;"
        );

        btnSair.setOnAction(e -> {
            Stage stage = (Stage) btnSair.getScene().getWindow();
            TelaSplash splash = new TelaSplash();
            stage.setScene(new Scene(splash.getLayout(), 1100, 700));
            splash.iniciarTransicaoEroteamento(stage); // Recomeça o sistema!
        });

        sidebar.getChildren().addAll(boxHeader, btnPedidos, btnCardapio, espacador, btnSair);
        return sidebar;
    }

    private Button criarBotaoSidebar(String texto) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: CENTER_LEFT; -fx-padding: 10 20 10 20; -fx-cursor: hand; -fx-background-radius: 8px;");
        return btn;
    }

    private void atualizarBotoesMenu(Button ativo) {
        String estiloInativo = "-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: CENTER_LEFT; -fx-padding: 10 20 10 20; -fx-cursor: hand; -fx-background-radius: 8px;";
        String estiloAtivo = "-fx-background-color: " + COR_VERMELHO + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: CENTER_LEFT; -fx-padding: 10 20 10 20; -fx-background-radius: 8px; -fx-cursor: hand;";

        btnPedidos.setStyle(estiloInativo);
        btnCardapio.setStyle(estiloInativo);
        ativo.setStyle(estiloAtivo);
    }

    // ==========================================
    // ABA 1: KANBAN DE PEDIDOS DO DIA
    // ==========================================
    private void abrirAbaPedidos() {
        atualizarBotoesMenu(btnPedidos);

        VBox layoutCentral = new VBox(25);
        layoutCentral.setPadding(new Insets(40));

        Label tituloAba = new Label("Pedidos em Andamento");
        tituloAba.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox kanbanBoard = new HBox(20);
        kanbanBoard.setAlignment(Pos.TOP_CENTER);

        // Colunas do Kanban
        VBox colPendentes = criarColunaKanban("⏳ Pendentes", "#f39c12");
        VBox colPreparo = criarColunaKanban("👨‍🍳 Em Preparo", "#3498db");
        VBox colEntregues = criarColunaKanban("✅ Entregues", "#2ecc71");

        // Mock visual de como os cartões de pedido ficam na coluna
        colPendentes.getChildren().add(criarCartaoPedido("#001 - João Silva", "1x Pizza, 2x Coca", "Aguardando"));
        colPreparo.getChildren().add(criarCartaoPedido("#002 - Maria", "1x X-Tudo", "Em Preparo"));

        HBox.setHgrow(colPendentes, Priority.ALWAYS);
        HBox.setHgrow(colPreparo, Priority.ALWAYS);
        HBox.setHgrow(colEntregues, Priority.ALWAYS);
        kanbanBoard.getChildren().addAll(colPendentes, colPreparo, colEntregues);

        layoutCentral.getChildren().addAll(tituloAba, kanbanBoard);

        ScrollPane scroll = new ScrollPane(layoutCentral);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
        telaPrincipal.setCenter(scroll);
    }

    private VBox criarColunaKanban(String titulo, String corCabelho) {
        VBox coluna = new VBox(15);
        coluna.setStyle("-fx-background-color: #e0e6ed; -fx-background-radius: 10px; -fx-padding: 15;");

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + corCabelho + ";");
        lblTitulo.setMaxWidth(Double.MAX_VALUE);
        lblTitulo.setAlignment(Pos.CENTER);

        coluna.getChildren().add(lblTitulo);
        return coluna;
    }

    private VBox criarCartaoPedido(String clienteInfo, String resumoItens, String statusAtual) {
        VBox cartao = new VBox(8);
        cartao.setStyle("-fx-background-color: white; -fx-background-radius: 8px; -fx-padding: 15;");
        DropShadow sombra = new DropShadow();
        sombra.setColor(Color.rgb(0, 0, 0, 0.1));
        cartao.setEffect(sombra);

        Label lblCliente = new Label(clienteInfo);
        lblCliente.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2c3e50;");

        Label lblResumo = new Label(resumoItens);
        lblResumo.setStyle("-fx-font-size: 13px; -fx-text-fill: #7f8c8d;");

        Button btnAvancar = new Button("Avançar Status ➔");
        btnAvancar.setMaxWidth(Double.MAX_VALUE);
        btnAvancar.setStyle("-fx-background-color: " + COR_VERMELHO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5px;");

        cartao.getChildren().addAll(lblCliente, lblResumo, btnAvancar);
        return cartao;
    }

    // ==========================================
    // ABA 2: GERENCIAR CARDÁPIO
    // ==========================================
    private void abrirAbaCardapio() {
        atualizarBotoesMenu(btnCardapio);

        VBox layoutCentral = new VBox(25);
        layoutCentral.setPadding(new Insets(40));

        Label tituloAba = new Label("Gerenciamento de Cardápio");
        tituloAba.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox cartaoFormulario = new VBox(20);
        cartaoFormulario.setPadding(new Insets(30));
        cartaoFormulario.setStyle("-fx-background-color: white; -fx-background-radius: 12px;");
        DropShadow sombra = new DropShadow();
        sombra.setColor(Color.rgb(0, 0, 0, 0.1));
        cartaoFormulario.setEffect(sombra);

        montarFormularioCardapio(cartaoFormulario);

        layoutCentral.getChildren().addAll(tituloAba, cartaoFormulario);

        ScrollPane scroll = new ScrollPane(layoutCentral);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
        telaPrincipal.setCenter(scroll);
    }

    private void montarFormularioCardapio(VBox cartaoFormulario) {
        cartaoFormulario.getChildren().clear();

        Label lblNovoItem = new Label("Adicionar Novo Prato");
        lblNovoItem.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + COR_VERMELHO + ";");

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do prato (Ex: Pizza Marguerita)");
        estilizarCampo(txtNome);

        ComboBox<Categoria> cbCategoria = new ComboBox<>();
        cbCategoria.getItems().addAll(Categoria.values());
        cbCategoria.setPromptText("Selecione a Categoria");
        cbCategoria.setStyle("-fx-pref-height: 40px; -fx-font-size: 14px; -fx-background-radius: 5px; -fx-border-color: #e2e8f0; -fx-border-radius: 5px;");
        cbCategoria.setPrefWidth(200);

        HBox linha1 = new HBox(20, criarBoxComLabel("Nome do Prato", txtNome), criarBoxComLabel("Categoria", cbCategoria));
        HBox.setHgrow(linha1.getChildren().get(0), Priority.ALWAYS);

        TextField txtDescricao = new TextField();
        txtDescricao.setPromptText("Descrição curta e apetitosa...");
        estilizarCampo(txtDescricao);
        VBox boxDesc = criarBoxComLabel("Descrição", txtDescricao);

        TextField txtPreco = new TextField();
        txtPreco.setPromptText("Ex: 45.90");
        txtPreco.setPrefWidth(150);
        estilizarCampo(txtPreco);

        Button btnSalvar = new Button("Salvar no Cardápio");
        btnSalvar.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px; -fx-pref-height: 40px; -fx-pref-width: 200px; -fx-background-radius: 8px; -fx-cursor: hand;");

        HBox linha3 = new HBox(20, criarBoxComLabel("Preço (R$)", txtPreco));
        linha3.setAlignment(Pos.CENTER_LEFT);
        Region espacadorBtn = new Region();
        HBox.setHgrow(espacadorBtn, Priority.ALWAYS);
        linha3.getChildren().addAll(espacadorBtn, btnSalvar);

        btnSalvar.setOnAction(e -> {
            try {
                String nome = txtNome.getText();
                String desc = txtDescricao.getText();
                double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
                Categoria cat = cbCategoria.getValue();

                if (nome.trim().isEmpty() || cat == null) {
                    throw new IllegalArgumentException("Preencha todos os campos obrigatórios!");
                }

                cardapioController.adicionarItem(nome, desc, preco, cat, true, null);

                // ANIMAÇÃO DE SUCESSO INTERNA (Sem Alert Feio)
                mostrarSucessoInterno(cartaoFormulario, nome);

            } catch (Exception ex) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Erro");
                alerta.setHeaderText(null);
                alerta.setContentText("Preço inválido ou erro: " + ex.getMessage());
                alerta.showAndWait();
            }
        });

        cartaoFormulario.getChildren().addAll(lblNovoItem, linha1, boxDesc, linha3);
    }

    private void mostrarSucessoInterno(VBox cartaoFormulario, String nomePrato) {
        cartaoFormulario.getChildren().clear();

        Label check = new Label("✔️");
        check.setStyle("-fx-font-size: 50px; -fx-text-fill: #2ecc71;");
        Label lblSucesso = new Label("Prato Adicionado!");
        lblSucesso.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Label lblMsg = new Label(nomePrato + " já está no cardápio.");
        lblMsg.setStyle("-fx-font-size: 15px; -fx-text-fill: #7f8c8d;");

        cartaoFormulario.setAlignment(Pos.CENTER);
        cartaoFormulario.getChildren().addAll(check, lblSucesso, lblMsg);

        PauseTransition delay = new PauseTransition(Duration.seconds(2.5));
        delay.setOnFinished(ev -> {
            cartaoFormulario.setAlignment(Pos.TOP_LEFT);
            montarFormularioCardapio(cartaoFormulario); // Remonta o form vazio
        });
        delay.play();
    }

    private void estilizarCampo(Control campo) {
        campo.setStyle("-fx-pref-height: 40px; -fx-background-radius: 5px; -fx-font-size: 14px; -fx-border-color: #e2e8f0; -fx-border-radius: 5px;");
    }

    private VBox criarBoxComLabel(String textoLabel, Control controle) {
        Label lbl = new Label(textoLabel);
        lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #7f8c8d; -fx-font-size: 13px;");
        VBox box = new VBox(5, lbl, controle);
        if (controle instanceof TextField) HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    public BorderPane getLayout() {
        return telaPrincipal;
    }
}
