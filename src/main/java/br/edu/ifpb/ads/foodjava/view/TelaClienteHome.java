package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.controller.CardapioController;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.model.Usuario;
import br.edu.ifpb.ads.foodjava.repository.CardapioRepositoryJsonImpl;
import br.edu.ifpb.ads.foodjava.repository.PedidoRepositoryJsonImpl;
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

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaClienteHome {

    private final String COR_VERMELHO = "#ea1d2c";
    private final String COR_FUNDO = "#f4f4f4";

    private BorderPane telaPrincipal;
    private Usuario clienteLogado;
    private CardapioController cardapioController;

    // ESTADO DO CARRINHO (Guarda Item -> Quantidade)
    private Map<ItemCardapio, Integer> carrinhoDeCompras = new HashMap<>();
    private Button btnCarrinho;
    private FlowPane painelPratos;

    public TelaClienteHome(Usuario clienteLogado) {
        this.clienteLogado = clienteLogado;
        this.cardapioController = new CardapioController(new CardapioRepositoryJsonImpl(), new PedidoRepositoryJsonImpl());

        telaPrincipal = new BorderPane();
        telaPrincipal.setStyle("-fx-background-color: " + COR_FUNDO + ";");

        telaPrincipal.setTop(criarCabecalho());
        telaPrincipal.setCenter(criarCorpoDoCardapio());
    }

    // ==========================================
    // CABEÇALHO (Logo, Sair, Carrinho)
    // ==========================================
    private HBox criarCabecalho() {
        HBox cabecalho = new HBox(20);
        cabecalho.setAlignment(Pos.CENTER_LEFT);
        cabecalho.setPadding(new Insets(10, 40, 10, 40));
        cabecalho.setStyle("-fx-background-color: " + COR_VERMELHO + ";");

        DropShadow sombra = new DropShadow();
        sombra.setColor(Color.rgb(0, 0, 0, 0.2));
        sombra.setRadius(10);
        cabecalho.setEffect(sombra);

        ImageView imgLogo = new ImageView();
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/logotipo.png"));
            imgLogo.setImage(logo);
            imgLogo.setFitHeight(75);
            imgLogo.setPreserveRatio(true);
        } catch (Exception e) {}

        Label saudacao = new Label("Olá, " + clienteLogado.getNome() + "!");
        saudacao.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        // BOTÃO SAIR
        Button btnSair = new Button("Sair");
        btnSair.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px; -fx-cursor: hand; -fx-border-color: white; -fx-border-radius: 20px; -fx-padding: 8 20 8 20;");
        btnSair.setOnAction(e -> {
            Stage stage = (Stage) btnSair.getScene().getWindow();
            TelaSplash splash = new TelaSplash();
            stage.setScene(new Scene(splash.getLayout(), 1100, 700));
            splash.iniciarTransicaoEroteamento(stage);
        });

        // BOTÃO CARRINHO
        btnCarrinho = new Button("🛒 Meu Carrinho (0)");
        btnCarrinho.setStyle("-fx-background-color: white; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 20px; -fx-padding: 10 20 10 20; -fx-cursor: hand;");
        btnCarrinho.setOnAction(e -> {
            Stage stage = (Stage) btnCarrinho.getScene().getWindow();
            TelaCarrinho telaCarrinho = new TelaCarrinho(clienteLogado, carrinhoDeCompras);
            stage.setScene(new Scene(telaCarrinho.getLayout(), 1100, 700));
        });

        cabecalho.getChildren().addAll(imgLogo, saudacao, espacador, btnSair, btnCarrinho);
        return cabecalho;
    }

    // ==========================================
    // CORPO DO CARDÁPIO (Puxa do Banco de Dados)
    // ==========================================
    private ScrollPane criarCorpoDoCardapio() {
        VBox layoutCentral = new VBox(25);
        layoutCentral.setPadding(new Insets(30, 40, 30, 40));

        Label tituloCat = new Label("O que vamos pedir hoje?");
        tituloCat.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox categorias = new HBox(15);
        categorias.getChildren().addAll(
                criarBotaoCategoria("🍔 Todos", true),
                criarBotaoCategoria("🥗 Entrada", false),
                criarBotaoCategoria("🍛 Prato Principal", false),
                criarBotaoCategoria("🍰 Sobremesa", false),
                criarBotaoCategoria("🥤 Bebidas", false)
        );

        painelPratos = new FlowPane();
        painelPratos.setHgap(30);
        painelPratos.setVgap(30);

        carregarPratosDoBanco(); // Puxa os dados reais!

        layoutCentral.getChildren().addAll(tituloCat, categorias, painelPratos);

        ScrollPane scroll = new ScrollPane(layoutCentral);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
        return scroll;
    }

    private void carregarPratosDoBanco() {
        painelPratos.getChildren().clear();
        List<ItemCardapio> itens = cardapioController.listarCardapioCompleto();

        if (itens == null || itens.isEmpty()) {
            Label lblVazio = new Label("O restaurante ainda não cadastrou nenhum prato! :(");
            lblVazio.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");
            painelPratos.getChildren().add(lblVazio);
            return;
        }

        for (ItemCardapio item : itens) {
            painelPratos.getChildren().add(criarCartaoProduto(item));
        }
    }

    private Button criarBotaoCategoria(String nome, boolean selecionado) {
        Button btn = new Button(nome);
        if (selecionado) {
            btn.setStyle("-fx-background-color: " + COR_VERMELHO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 20px; -fx-padding: 10 22 10 22; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: white; -fx-text-fill: #7f8c8d; -fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 20px; -fx-padding: 10 22 10 22; -fx-cursor: hand; -fx-border-color: #dcdde1; -fx-border-radius: 20px;");
        }
        return btn;
    }

    private VBox criarCartaoProduto(ItemCardapio item) {
        VBox cartao = new VBox(10);
        cartao.setPadding(new Insets(15));
        cartao.setPrefWidth(220);
        cartao.setStyle("-fx-background-color: white; -fx-background-radius: 15px;");

        DropShadow sombra = new DropShadow();
        sombra.setColor(Color.rgb(0, 0, 0, 0.08));
        sombra.setRadius(15);
        cartao.setEffect(sombra);

        ImageView imgPrato = new ImageView();
        InputStream isImg = getClass().getResourceAsStream("/images/comida_padrao.jpg");
        if (isImg != null) imgPrato.setImage(new Image(isImg));
        imgPrato.setFitWidth(190);
        imgPrato.setFitHeight(130);

        Label lblTitulo = new Label(item.getNome());
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #2c3e50;");

        Label lblDesc = new Label(item.getDescricao());
        lblDesc.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");
        lblDesc.setWrapText(true);
        lblDesc.setPrefHeight(40);

        Label lblPreco = new Label(String.format("R$ %.2f", item.getPreco()));
        lblPreco.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: " + COR_VERMELHO + ";");

        Button btnAdd = new Button("+ Adicionar");
        btnAdd.setPrefWidth(Double.MAX_VALUE);
        btnAdd.setStyle("-fx-background-color: " + COR_VERMELHO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-font-size: 14px;");

        // Lógica de Adicionar ao Carrinho!
        btnAdd.setOnAction(e -> {
            int qtdAtual = carrinhoDeCompras.getOrDefault(item, 0);
            carrinhoDeCompras.put(item, qtdAtual + 1);

            // Atualiza o botão no topo
            int totalItens = carrinhoDeCompras.values().stream().mapToInt(Integer::intValue).sum();
            btnCarrinho.setText("🛒 Meu Carrinho (" + totalItens + ")");

            // Animação no botão para mostrar que adicionou
            btnAdd.setText("Adicionado ✔");
            btnAdd.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-font-size: 14px;");

            PauseTransition pt = new PauseTransition(Duration.seconds(1));
            pt.setOnFinished(ev -> {
                btnAdd.setText("+ Adicionar");
                btnAdd.setStyle("-fx-background-color: " + COR_VERMELHO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-font-size: 14px;");
            });
            pt.play();
        });

        cartao.getChildren().addAll(imgPrato, lblTitulo, lblDesc, lblPreco, btnAdd);
        return cartao;
    }

    public BorderPane getLayout() {
        return telaPrincipal;
    }
}
