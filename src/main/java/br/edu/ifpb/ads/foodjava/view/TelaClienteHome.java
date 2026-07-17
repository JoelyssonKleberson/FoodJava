package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.controller.CardapioController;
import br.edu.ifpb.ads.foodjava.enums.Categoria;
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

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaClienteHome {

    private final String COR_VERMELHO = "#ea1d2c";
    private final String COR_FUNDO = "#f4f4f4";

    private BorderPane telaPrincipal;
    private Usuario clienteLogado;
    private CardapioController cardapioController;

    private Map<ItemCardapio, Integer> carrinhoDeCompras = new HashMap<>();
    private Button btnCarrinho;
    private FlowPane painelPratos;

    private List<Button> botoesCategorias;

    private boolean alertaImagemMostrado = false;

    public TelaClienteHome(Usuario clienteLogado) {
        this.clienteLogado = clienteLogado;
        this.cardapioController = new CardapioController(new CardapioRepositoryJsonImpl(), new PedidoRepositoryJsonImpl());

        telaPrincipal = new BorderPane();
        telaPrincipal.setStyle("-fx-background-color: " + COR_FUNDO + ";");

        telaPrincipal.setTop(criarCabecalho());
        telaPrincipal.setCenter(criarCorpoDoCardapio());
    }

    private HBox criarCabecalho() {
        HBox cabecalho = new HBox(15);
        cabecalho.setAlignment(Pos.CENTER_LEFT);
        cabecalho.setPadding(new Insets(10, 40, 10, 40));
        cabecalho.setStyle("-fx-background-color: " + COR_VERMELHO + ";");

        DropShadow sombra = new DropShadow(); sombra.setColor(Color.rgb(0, 0, 0, 0.2)); sombra.setRadius(10);
        cabecalho.setEffect(sombra);

        ImageView imgLogo = new ImageView();
        try {
            imgLogo.setImage(new Image(getClass().getResourceAsStream("/images/logotipo.png")));
            imgLogo.setFitHeight(60);
            imgLogo.setPreserveRatio(true);
        } catch (Exception e) {}

        Label saudacao = new Label("Olá, " + clienteLogado.getNome() + "!");
        saudacao.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        Button btnPedidos = new Button("📄 Meus Pedidos");
        btnPedidos.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px; -fx-cursor: hand; -fx-border-color: white; -fx-border-radius: 20px; -fx-padding: 8 15 8 15;");
        btnPedidos.setOnAction(e -> {
            Stage stage = (Stage) btnPedidos.getScene().getWindow();
            TelaHistoricoPedidosCliente tela = new TelaHistoricoPedidosCliente(clienteLogado);
            stage.setScene(new Scene(tela.getLayout(), 1100, 700));
        });

        Button btnSair = new Button("Sair");
        btnSair.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px; -fx-cursor: hand; -fx-border-color: white; -fx-border-radius: 20px; -fx-padding: 8 20 8 20;");
        btnSair.setOnAction(e -> {
            Stage stage = (Stage) btnSair.getScene().getWindow();
            TelaSplash splash = new TelaSplash();
            stage.setScene(new Scene(splash.getLayout(), 1100, 700));
            splash.iniciarTransicaoEroteamento(stage);
        });

        btnCarrinho = new Button("🛒 Meu Carrinho (0)");
        btnCarrinho.setStyle("-fx-background-color: white; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 20px; -fx-padding: 10 20 10 20; -fx-cursor: hand;");
        btnCarrinho.setOnAction(e -> {
            Stage stage = (Stage) btnCarrinho.getScene().getWindow();
            TelaCarrinho telaCarrinho = new TelaCarrinho(clienteLogado, carrinhoDeCompras);
            stage.setScene(new Scene(telaCarrinho.getLayout(), 1100, 700));
        });

        cabecalho.getChildren().addAll(imgLogo, saudacao, espacador, btnPedidos, btnSair, btnCarrinho);
        return cabecalho;
    }

    private ScrollPane criarCorpoDoCardapio() {
        VBox layoutCentral = new VBox(25);
        layoutCentral.setPadding(new Insets(30, 40, 30, 40));

        Label tituloCat = new Label("O que vamos pedir hoje?");
        tituloCat.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Button btnTodos = criarBotaoCategoria("🍔 Todos");
        Button btnEntrada = criarBotaoCategoria("🥗 Entrada");
        Button btnPrincipal = criarBotaoCategoria("🍛 Prato Principal");
        Button btnSobremesa = criarBotaoCategoria("🍰 Sobremesa");
        Button btnBebidas = criarBotaoCategoria("🥤 Bebidas");

        botoesCategorias = Arrays.asList(btnTodos, btnEntrada, btnPrincipal, btnSobremesa, btnBebidas);

        btnTodos.setOnAction(e -> { destacarBotaoCat(btnTodos); carregarPratosDoBanco(null); });
        btnEntrada.setOnAction(e -> { destacarBotaoCat(btnEntrada); carregarPratosDoBanco(Categoria.ENTRADA); });
        btnPrincipal.setOnAction(e -> { destacarBotaoCat(btnPrincipal); carregarPratosDoBanco(Categoria.PRATO_PRINCIPAL); });
        btnSobremesa.setOnAction(e -> { destacarBotaoCat(btnSobremesa); carregarPratosDoBanco(Categoria.SOBREMESA); });
        btnBebidas.setOnAction(e -> { destacarBotaoCat(btnBebidas); carregarPratosDoBanco(Categoria.BEBIDAS); });

        destacarBotaoCat(btnTodos);

        HBox categorias = new HBox(15, btnTodos, btnEntrada, btnPrincipal, btnSobremesa, btnBebidas);

        painelPratos = new FlowPane();
        painelPratos.setHgap(30);
        painelPratos.setVgap(30);

        carregarPratosDoBanco(null);

        layoutCentral.getChildren().addAll(tituloCat, categorias, painelPratos);

        ScrollPane scroll = new ScrollPane(layoutCentral);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
        return scroll;
    }

    private void destacarBotaoCat(Button ativo) {
        String inativo = "-fx-background-color: white; -fx-text-fill: #7f8c8d; -fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 20px; -fx-padding: 10 22 10 22; -fx-cursor: hand; -fx-border-color: #dcdde1; -fx-border-radius: 20px;";
        String selecionado = "-fx-background-color: " + COR_VERMELHO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 20px; -fx-padding: 10 22 10 22; -fx-cursor: hand;";

        for (Button b : botoesCategorias) b.setStyle(inativo);
        ativo.setStyle(selecionado);
    }

    private void carregarPratosDoBanco(Categoria filtro) {
        painelPratos.getChildren().clear();

        List<ItemCardapio> itens;
        if (filtro == null) itens = cardapioController.listarCardapioCompleto();
        else itens = cardapioController.listarPorCategoria(filtro);

        if (itens == null || itens.isEmpty()) {
            Label lblVazio = new Label("Nenhum prato encontrado nesta categoria.");
            lblVazio.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");
            painelPratos.getChildren().add(lblVazio);
            return;
        }

        for (ItemCardapio item : itens) {
            if (item.isDisponivel()) painelPratos.getChildren().add(criarCartaoProduto(item));
        }
    }

    private Button criarBotaoCategoria(String nome) {
        return new Button(nome);
    }

    private VBox criarCartaoProduto(ItemCardapio item) {
        VBox cartao = new VBox(10);
        cartao.setPadding(new Insets(15));
        cartao.setPrefWidth(220);
        cartao.setStyle("-fx-background-color: white; -fx-background-radius: 15px;");
        DropShadow sombra = new DropShadow(); sombra.setColor(Color.rgb(0, 0, 0, 0.08)); sombra.setRadius(15);
        cartao.setEffect(sombra);

        ImageView imgPrato = new ImageView();
        Image imagemFinal = null;
        String imgPath = item.getImagemPath();

        if (imgPath != null && !imgPath.trim().isEmpty()) {
            try {
                if (imgPath.contains("/")) {
                    imgPath = imgPath.substring(imgPath.lastIndexOf("/") + 1);
                }

                File fileFisico = new File("data/imagens_pratos/" + imgPath);
                if (fileFisico.exists()) {
                    Image tempImg = new Image(fileFisico.toURI().toString());
                    if (!tempImg.isError()) {
                        imagemFinal = tempImg;
                    }
                } else {
                    InputStream is = getClass().getResourceAsStream("/images/" + imgPath);
                    if (is != null) {
                        Image tempImg = new Image(is);
                        if (!tempImg.isError()) {
                            imagemFinal = tempImg;
                        }
                    }
                }
            } catch (Exception ignored) {}
        }

        if (imagemFinal == null) {
            try {
                InputStream isPadrao = getClass().getResourceAsStream("/images/comida_padrao.jpg");
                if (isPadrao != null) {
                    imagemFinal = new Image(isPadrao);
                } else {
                    if (!alertaImagemMostrado) {
                        mostrarAlertaErro("Aviso do Sistema: A imagem padrão (comida_padrao.jpg) não foi encontrada na sua pasta images do projeto.");
                        alertaImagemMostrado = true;
                    }
                }
            } catch (Exception e) {}
        }

        if (imagemFinal != null) {
            imgPrato.setImage(imagemFinal);
        } else {
            imgPrato.setStyle("-fx-background-color: #dcdde1;");
        }

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

        btnAdd.setOnAction(e -> {
            int qtdAtual = carrinhoDeCompras.getOrDefault(item, 0);
            carrinhoDeCompras.put(item, qtdAtual + 1);

            int totalItens = carrinhoDeCompras.values().stream().mapToInt(Integer::intValue).sum();
            btnCarrinho.setText("🛒 Meu Carrinho (" + totalItens + ")");

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

    private void mostrarAlertaErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Atenção"); alerta.setHeaderText(null); alerta.setContentText(mensagem); alerta.showAndWait();
    }

    public BorderPane getLayout() { return telaPrincipal; }
}
