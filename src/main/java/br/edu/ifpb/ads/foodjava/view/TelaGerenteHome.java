package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.controller.CardapioController;
import br.edu.ifpb.ads.foodjava.enums.Categoria;
import br.edu.ifpb.ads.foodjava.model.Usuario;
import br.edu.ifpb.ads.foodjava.repository.CardapioRepository;
import br.edu.ifpb.ads.foodjava.repository.CardapioRepositoryJsonImpl;
import br.edu.ifpb.ads.foodjava.repository.PedidoRepository;
import br.edu.ifpb.ads.foodjava.repository.PedidoRepositoryJsonImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class TelaGerenteHome {

    private final String COR_SIDEBAR = "#2c3e50"; // Azul escuro / Chumbo profundo
    private final String COR_VERMELHO = "#ea1d2c";
    private final String COR_FUNDO = "#f4f4f4";

    private BorderPane telaPrincipal;
    private Usuario gerenteLogado;
    private CardapioController cardapioController;

    public TelaGerenteHome(Usuario gerenteLogado) {
        this.gerenteLogado = gerenteLogado;

        // CORREÇÃO APLICADA: Instanciando AMBOS os repositórios exigidos pelo seu Controller
        CardapioRepository repoCardapio = new CardapioRepositoryJsonImpl();
        PedidoRepository repoPedido = new PedidoRepositoryJsonImpl();

        this.cardapioController = new CardapioController(repoCardapio, repoPedido);

        telaPrincipal = new BorderPane();
        telaPrincipal.setStyle("-fx-background-color: " + COR_FUNDO + ";");

        // Montagem do Layout
        telaPrincipal.setLeft(criarSidebar());
        telaPrincipal.setCenter(criarAbaCardapio()); // Inicia na aba de gerenciar cardápio
    }

    // ==========================================
    // 1. MENU LATERAL (SIDEBAR)
    // ==========================================
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

        // Botões de Navegação
        Button btnPedidos = criarBotaoSidebar("📋 Pedidos do Dia");
        Button btnCardapio = criarBotaoSidebar("🍔 Gerenciar Cardápio");

        // Estilo especial para mostrar que a aba "Cardápio" está ativa
        btnCardapio.setStyle("-fx-background-color: " + COR_VERMELHO + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: CENTER_LEFT; -fx-padding: 10 20 10 20; -fx-background-radius: 8px; -fx-cursor: hand;");

        Region espacador = new Region();
        VBox.setVgrow(espacador, Priority.ALWAYS);

        Button btnSair = criarBotaoSidebar("🚪 Sair do Sistema");
        btnSair.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; -fx-font-size: 15px; -fx-font-weight: bold; -fx-alignment: CENTER_LEFT; -fx-padding: 10 20 10 20; -fx-cursor: hand;");

        sidebar.getChildren().addAll(boxHeader, btnPedidos, btnCardapio, espacador, btnSair);
        return sidebar;
    }

    private Button criarBotaoSidebar(String texto) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: CENTER_LEFT; -fx-padding: 10 20 10 20; -fx-cursor: hand; -fx-background-radius: 8px;");
        return btn;
    }

    // ==========================================
    // 2. ABA: GERENCIAR CARDÁPIO (Onde o Gerente cadastra a comida)
    // ==========================================
    private ScrollPane criarAbaCardapio() {
        VBox layoutCentral = new VBox(25);
        layoutCentral.setPadding(new Insets(40));

        Label tituloAba = new Label("Gerenciamento de Cardápio");
        tituloAba.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // --- CARTÃO DO FORMULÁRIO DE CADASTRO DE ITEM ---
        VBox cartaoFormulario = new VBox(20);
        cartaoFormulario.setPadding(new Insets(30));
        cartaoFormulario.setStyle("-fx-background-color: white; -fx-background-radius: 12px;");

        DropShadow sombra = new DropShadow();
        sombra.setColor(Color.rgb(0, 0, 0, 0.1));
        sombra.setRadius(15);
        cartaoFormulario.setEffect(sombra);

        Label lblNovoItem = new Label("Adicionar Novo Prato");
        lblNovoItem.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + COR_VERMELHO + ";");

        // Linha 1: Nome e Categoria
        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do prato (Ex: Pizza Marguerita)");
        estilizarCampo(txtNome);

        ComboBox<Categoria> cbCategoria = new ComboBox<>();
        cbCategoria.getItems().addAll(Categoria.values());
        cbCategoria.setPromptText("Selecione a Categoria");
        cbCategoria.setStyle("-fx-pref-height: 40px; -fx-font-size: 14px; -fx-background-radius: 5px; -fx-border-color: #e2e8f0; -fx-border-radius: 5px;");
        cbCategoria.setPrefWidth(200);

        HBox linha1 = new HBox(20, criarBoxComLabel("Nome do Prato", txtNome), criarBoxComLabel("Categoria", cbCategoria));
        HBox.setHgrow(linha1.getChildren().get(0), Priority.ALWAYS); // Faz o campo de nome esticar

        // Linha 2: Descrição
        TextField txtDescricao = new TextField();
        txtDescricao.setPromptText("Descrição curta e apetitosa...");
        estilizarCampo(txtDescricao);
        VBox boxDesc = criarBoxComLabel("Descrição", txtDescricao);

        // Linha 3: Preço e Botão Salvar
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

        // AÇÃO DO BOTÃO: Salvar o item no JSON via Controller!
        btnSalvar.setOnAction(e -> {
            try {
                String nome = txtNome.getText();
                String desc = txtDescricao.getText();
                // Troca a vírgula por ponto para não dar erro no Double.parseDouble
                double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
                Categoria cat = cbCategoria.getValue();

                if (nome.isEmpty() || cat == null) {
                    throw new IllegalArgumentException("Preencha todos os campos obrigatórios!");
                }

                // Passamos true para "disponível" e null para a imagem (vai usar o placeholder)
                cardapioController.adicionarItem(nome, desc, preco, cat, true, null);

                Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
                sucesso.setTitle("Sucesso");
                sucesso.setHeaderText(null);
                sucesso.setContentText(nome + " adicionado ao cardápio com sucesso!");
                sucesso.showAndWait();

                // Limpa os campos para o gerente poder cadastrar outro prato
                txtNome.clear();
                txtDescricao.clear();
                txtPreco.clear();
                cbCategoria.setValue(null);

            } catch (NumberFormatException ex) {
                mostrarAlertaErro("Preço inválido! Digite apenas números e use ponto ou vírgula.");
            } catch (Exception ex) {
                mostrarAlertaErro(ex.getMessage());
            }
        });

        cartaoFormulario.getChildren().addAll(lblNovoItem, linha1, boxDesc, linha3);
        layoutCentral.getChildren().addAll(tituloAba, cartaoFormulario);

        ScrollPane scroll = new ScrollPane(layoutCentral);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
        return scroll;
    }

    private void estilizarCampo(Control campo) {
        campo.setStyle("-fx-pref-height: 40px; -fx-background-radius: 5px; -fx-font-size: 14px; -fx-border-color: #e2e8f0; -fx-border-radius: 5px;");
    }

    private VBox criarBoxComLabel(String textoLabel, Control controle) {
        Label lbl = new Label(textoLabel);
        lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #7f8c8d; -fx-font-size: 13px;");
        VBox box = new VBox(5, lbl, controle);
        if (controle instanceof TextField) {
            HBox.setHgrow(box, Priority.ALWAYS);
        }
        return box;
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public BorderPane getLayout() {
        return telaPrincipal;
    }
}
