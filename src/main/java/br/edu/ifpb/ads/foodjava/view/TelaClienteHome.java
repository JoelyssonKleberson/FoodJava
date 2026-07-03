package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.model.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.InputStream;

public class TelaClienteHome {

    private final String COR_VERMELHO = "#ea1d2c";
    private final String COR_FUNDO = "#f4f4f4";

    private BorderPane telaPrincipal;
    private Usuario clienteLogado;

    public TelaClienteHome(Usuario clienteLogado) {
        this.clienteLogado = clienteLogado;

        telaPrincipal = new BorderPane();
        telaPrincipal.setStyle("-fx-background-color: " + COR_FUNDO + ";");

        telaPrincipal.setTop(criarCabecalho());
        telaPrincipal.setCenter(criarCorpoDoCardapio());
    }

    // ==========================================
    // 1. CABEÇALHO (Logo, Saudação, Carrinho)
    // ==========================================
    private HBox criarCabecalho() {
        HBox cabecalho = new HBox(20);
        cabecalho.setAlignment(Pos.CENTER_LEFT);
        cabecalho.setPadding(new Insets(10, 40, 10, 40));
        // 2) Barra branca trocada para vermelho!
        cabecalho.setStyle("-fx-background-color: " + COR_VERMELHO + ";");

        DropShadow sombra = new DropShadow();
        sombra.setColor(Color.rgb(0, 0, 0, 0.2));
        sombra.setRadius(10);
        cabecalho.setEffect(sombra);

        // 1) Logo maior sem ultrapassar a barra
        ImageView imgLogo = new ImageView();
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/logotipo.png"));
            imgLogo.setImage(logo);
            imgLogo.setFitHeight(75); // Aumentado de 50 para 75
            imgLogo.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Erro ao carregar a logo: " + e.getMessage());
        }

        // Saudação: Branca para dar contraste com o fundo vermelho
        Label saudacao = new Label("Olá, " + clienteLogado.getNome() + "!");
        saudacao.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        // 3) Botão do Carrinho: Fundo branco e texto preto escuro
        Button btnCarrinho = new Button("🛒 Meu Carrinho (0)");
        btnCarrinho.setStyle("-fx-background-color: white; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 20px; -fx-padding: 10 20 10 20; -fx-cursor: hand;");

        cabecalho.getChildren().addAll(imgLogo, saudacao, espacador, btnCarrinho);
        return cabecalho;
    }

    // ==========================================
    // 2. CORPO DO CARDÁPIO (Categorias e Pratos)
    // ==========================================
    private ScrollPane criarCorpoDoCardapio() {
        VBox layoutCentral = new VBox(25);
        layoutCentral.setPadding(new Insets(30, 40, 30, 40));

        Label tituloCat = new Label("O que vamos pedir hoje?");
        tituloCat.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // 4) Novos nomes para as categorias do catálogo
        HBox categorias = new HBox(15);
        categorias.getChildren().addAll(
                criarBotaoCategoria("🍔 Todos", true),
                criarBotaoCategoria("🥗 Entrada", false),
                criarBotaoCategoria("🍛 Prato Principal", false),
                criarBotaoCategoria("🍰 Sobremesa", false),
                criarBotaoCategoria("🥤 Bebidas", false)
        );

        FlowPane painelPratos = new FlowPane();
        painelPratos.setHgap(30);
        painelPratos.setVgap(30);

        painelPratos.getChildren().addAll(
                criarCartaoProduto("Pizza de Calabresa", "Deliciosa pizza com calabresa artesanal, cebola e queijo derretido.", "R$ 45,90"),
                criarCartaoProduto("X-Tudo Monstro", "Hambúrguer de 200g, bacon, salsicha, ovo, queijo e salada.", "R$ 28,50"),
                criarCartaoProduto("Coca-Cola 2L", "Refrigerante Coca-Cola 2 Litros bem gelado.", "R$ 14,00"),
                criarCartaoProduto("Batata Frita Especial", "Porção de 500g de batata frita com cheddar e bacon.", "R$ 32,00")
        );

        layoutCentral.getChildren().addAll(tituloCat, categorias, painelPratos);

        ScrollPane scroll = new ScrollPane(layoutCentral);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");

        return scroll;
    }

    // ==========================================
    // MÉTODOS DE DESIGN (Fabricantes de Componentes)
    // ==========================================

    private Button criarBotaoCategoria(String nome, boolean selecionado) {
        Button btn = new Button(nome);
        // 5) Fonte maior (15px) e padding ajustado para não quebrar a caixa
        if (selecionado) {
            btn.setStyle("-fx-background-color: " + COR_VERMELHO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 20px; -fx-padding: 10 22 10 22; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: white; -fx-text-fill: #7f8c8d; -fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 20px; -fx-padding: 10 22 10 22; -fx-cursor: hand; -fx-border-color: #dcdde1; -fx-border-radius: 20px;");
        }
        return btn;
    }

    private VBox criarCartaoProduto(String titulo, String descricao, String preco) {
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
        if (isImg != null) {
            imgPrato.setImage(new Image(isImg));
        }
        imgPrato.setFitWidth(190);
        imgPrato.setFitHeight(130);

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #2c3e50;");

        Label lblDesc = new Label(descricao);
        lblDesc.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");
        lblDesc.setWrapText(true);
        lblDesc.setPrefHeight(40);

        Label lblPreco = new Label(preco);
        lblPreco.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: " + COR_VERMELHO + ";");

        Button btnAdd = new Button("+ Adicionar");
        btnAdd.setPrefWidth(Double.MAX_VALUE);
        btnAdd.setStyle("-fx-background-color: " + COR_VERMELHO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-font-size: 14px;");

        cartao.getChildren().addAll(imgPrato, lblTitulo, lblDesc, lblPreco, btnAdd);
        return cartao;
    }

    public BorderPane getLayout() {
        return telaPrincipal;
    }
}
