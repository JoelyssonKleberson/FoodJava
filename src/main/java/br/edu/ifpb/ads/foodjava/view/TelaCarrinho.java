package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.controller.PedidoController;
import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.model.Usuario;
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

import java.util.Map;

public class TelaCarrinho {

    private final String COR_VERMELHO = "#ea1d2c";
    private final String COR_FUNDO = "#f4f4f4";

    private BorderPane telaPrincipal;
    private Usuario clienteLogado;
    private Map<ItemCardapio, Integer> carrinho;
    private PedidoController pedidoController;

    private VBox listaItensContainer;
    private Label lblSubtotalNum;
    private Label lblTotalNum;
    private VBox cartaoResumo;

    public TelaCarrinho(Usuario clienteLogado, Map<ItemCardapio, Integer> carrinho) {
        this.clienteLogado = clienteLogado;
        this.carrinho = carrinho;
        this.pedidoController = new PedidoController(new PedidoRepositoryJsonImpl());

        telaPrincipal = new BorderPane();
        telaPrincipal.setStyle("-fx-background-color: " + COR_FUNDO + ";");
        telaPrincipal.setPadding(new Insets(40));

        // BOTÃO VOLTAR
        Button btnVoltar = new Button("⬅ Voltar ao Cardápio");
        btnVoltar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + COR_VERMELHO + "; -fx-font-weight: bold; -fx-font-size: 15px; -fx-cursor: hand; -fx-border-color: " + COR_VERMELHO + "; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-padding: 8 15 8 15;");
        btnVoltar.setOnAction(e -> {
            Stage stage = (Stage) btnVoltar.getScene().getWindow();
            stage.setScene(new Scene(new TelaClienteHome(clienteLogado).getLayout(), 1100, 700));
        });

        // NOVO: BOTÃO LIMPAR CARRINHO
        Button btnLimpar = new Button("🗑 Limpar Carrinho");
        btnLimpar.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 15px; -fx-cursor: hand; -fx-border-color: #e74c3c; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-padding: 8 15 8 15;");
        btnLimpar.setOnAction(e -> {
            this.carrinho.clear();
            atualizarTela();
        });

        Label lblTitulo = new Label("Seu Carrinho");
        lblTitulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Region espacadorTopo = new Region();
        HBox.setHgrow(espacadorTopo, Priority.ALWAYS);

        HBox botoesTopo = new HBox(15, btnVoltar, espacadorTopo, btnLimpar);
        VBox topo = new VBox(15, botoesTopo, lblTitulo);
        topo.setPadding(new Insets(0, 0, 20, 0));
        telaPrincipal.setTop(topo);

        HBox centro = new HBox(30);
        centro.setAlignment(Pos.TOP_CENTER);

        listaItensContainer = new VBox(15);
        ScrollPane scrollItens = new ScrollPane(listaItensContainer);
        scrollItens.setFitToWidth(true);
        scrollItens.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
        HBox.setHgrow(scrollItens, Priority.ALWAYS);

        cartaoResumo = criarCartaoResumo();

        centro.getChildren().addAll(scrollItens, cartaoResumo);
        telaPrincipal.setCenter(centro);

        atualizarTela();
    }

    private void atualizarTela() {
        listaItensContainer.getChildren().clear();
        double subtotal = 0;

        if (carrinho.isEmpty()) {
            Label vazio = new Label("Seu carrinho está vazio :(");
            vazio.setStyle("-fx-font-size: 18px; -fx-text-fill: #7f8c8d;");
            listaItensContainer.getChildren().add(vazio);
        } else {
            for (Map.Entry<ItemCardapio, Integer> entry : carrinho.entrySet()) {
                ItemCardapio item = entry.getKey();
                int qtd = entry.getValue();

                if (qtd > 0) {
                    listaItensContainer.getChildren().add(criarLinhaItem(item, qtd));
                    subtotal += item.getPreco() * qtd;
                }
            }
        }

        lblSubtotalNum.setText(String.format("R$ %.2f", subtotal));
        lblTotalNum.setText(String.format("R$ %.2f", subtotal));
    }

    private HBox criarLinhaItem(ItemCardapio item, int qtd) {
        HBox linha = new HBox(20);
        linha.setPadding(new Insets(20));
        linha.setStyle("-fx-background-color: white; -fx-background-radius: 12px;");
        DropShadow sombra = new DropShadow(); sombra.setColor(Color.rgb(0, 0, 0, 0.05)); linha.setEffect(sombra);
        linha.setAlignment(Pos.CENTER_LEFT);

        VBox descBox = new VBox(5);
        Label lblNome = new Label(item.getNome());
        lblNome.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");
        Label lblPreco = new Label(String.format("R$ %.2f", item.getPreco()));
        lblPreco.setStyle("-fx-font-size: 15px; -fx-text-fill: #7f8c8d;");
        descBox.getChildren().addAll(lblNome, lblPreco);
        HBox.setHgrow(descBox, Priority.ALWAYS);

        Button btnMenos = new Button("-");
        btnMenos.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-cursor: hand;");
        Label lblQtd = new Label(String.valueOf(qtd));
        lblQtd.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 0 10 0 10;");
        Button btnMais = new Button("+");
        btnMais.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-cursor: hand;");

        btnMenos.setOnAction(e -> {
            carrinho.put(item, qtd - 1);
            if (carrinho.get(item) <= 0) carrinho.remove(item);
            atualizarTela();
        });
        btnMais.setOnAction(e -> { carrinho.put(item, qtd + 1); atualizarTela(); });

        HBox controlesQtd = new HBox(btnMenos, lblQtd, btnMais);
        controlesQtd.setAlignment(Pos.CENTER);

        Label lblTotalItem = new Label(String.format("R$ %.2f", item.getPreco() * qtd));
        lblTotalItem.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: " + COR_VERMELHO + ";");

        linha.getChildren().addAll(descBox, controlesQtd, lblTotalItem);
        return linha;
    }

    private VBox criarCartaoResumo() {
        VBox cartao = new VBox(20);
        cartao.setPrefWidth(350); cartao.setMinWidth(350); cartao.setPadding(new Insets(30));
        cartao.setStyle("-fx-background-color: white; -fx-background-radius: 15px;");
        DropShadow sombra = new DropShadow(); sombra.setColor(Color.rgb(0, 0, 0, 0.1)); sombra.setRadius(20); cartao.setEffect(sombra);

        Label lblTit = new Label("Resumo do Pedido");
        lblTit.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox subBox = new HBox();
        Label lblSub = new Label("Subtotal"); lblSub.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 15px;");
        lblSubtotalNum = new Label("R$ 0,00"); lblSubtotalNum.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 15px; -fx-font-weight: bold;");
        Region esp1 = new Region(); HBox.setHgrow(esp1, Priority.ALWAYS); subBox.getChildren().addAll(lblSub, esp1, lblSubtotalNum);

        HBox taxaBox = new HBox();
        Label lblTaxa = new Label("Taxa de Entrega"); lblTaxa.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 15px;");
        Label lblTaxaNum = new Label("Grátis"); lblTaxaNum.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 15px; -fx-font-weight: bold;");
        Region esp2 = new Region(); HBox.setHgrow(esp2, Priority.ALWAYS); taxaBox.getChildren().addAll(lblTaxa, esp2, lblTaxaNum);

        HBox totBox = new HBox();
        Label lblTot = new Label("Total"); lblTot.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 20px; -fx-font-weight: bold;");
        lblTotalNum = new Label("R$ 0,00"); lblTotalNum.setStyle("-fx-text-fill: " + COR_VERMELHO + "; -fx-font-size: 22px; -fx-font-weight: bold;");
        Region esp3 = new Region(); HBox.setHgrow(esp3, Priority.ALWAYS); totBox.getChildren().addAll(lblTot, esp3, lblTotalNum);

        Button btnFinalizar = new Button("Confirmar Pedido");
        btnFinalizar.setPrefWidth(Double.MAX_VALUE);
        btnFinalizar.setStyle("-fx-background-color: " + COR_VERMELHO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-background-radius: 8px; -fx-padding: 15; -fx-cursor: hand;");

        btnFinalizar.setOnAction(e -> {
            if (carrinho.isEmpty()) {
                Alert erro = new Alert(Alert.AlertType.ERROR); erro.setContentText("Adicione itens ao carrinho primeiro!"); erro.showAndWait();
                return;
            }
            try {
                pedidoController.finalizarPedido((Cliente) clienteLogado, carrinho);

                cartao.getChildren().clear();
                Label check = new Label("✔️"); check.setStyle("-fx-font-size: 60px; -fx-text-fill: #2ecc71;");
                Label sucesso = new Label("Pedido Confirmado!"); sucesso.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
                Label msg = new Label("A cozinha já está preparando."); msg.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

                cartao.setAlignment(Pos.CENTER); cartao.getChildren().addAll(check, sucesso, msg);

                PauseTransition delay = new PauseTransition(Duration.seconds(3));
                delay.setOnFinished(ev -> {
                    carrinho.clear();
                    Stage stage = (Stage) telaPrincipal.getScene().getWindow();
                    stage.setScene(new Scene(new TelaHistoricoPedidosCliente(clienteLogado).getLayout(), 1100, 700));
                });
                delay.play();
            } catch (Exception ex) {
                Alert erro = new Alert(Alert.AlertType.ERROR); erro.setContentText(ex.getMessage()); erro.showAndWait();
            }
        });

        cartao.getChildren().addAll(lblTit, subBox, taxaBox, new Separator(), totBox, btnFinalizar);
        return cartao;
    }

    public BorderPane getLayout() { return telaPrincipal; }
}
