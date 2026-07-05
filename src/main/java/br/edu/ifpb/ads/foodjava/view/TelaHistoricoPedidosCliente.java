package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.controller.PedidoController;
import br.edu.ifpb.ads.foodjava.enums.StatusPedido;
import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.model.Pedido;
import br.edu.ifpb.ads.foodjava.model.Usuario;
import br.edu.ifpb.ads.foodjava.repository.PedidoRepositoryJsonImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class TelaHistoricoPedidosCliente {

    private final String COR_VERMELHO = "#ea1d2c";
    private final String COR_FUNDO = "#f4f4f4";

    private BorderPane telaPrincipal;
    private Usuario clienteLogado;
    private PedidoController pedidoController;
    private FlowPane painelPedidos;

    public TelaHistoricoPedidosCliente(Usuario clienteLogado) {
        this.clienteLogado = clienteLogado;
        this.pedidoController = new PedidoController(new PedidoRepositoryJsonImpl());

        telaPrincipal = new BorderPane();
        telaPrincipal.setStyle("-fx-background-color: " + COR_FUNDO + ";");
        telaPrincipal.setPadding(new Insets(40));

        Button btnVoltar = new Button("⬅ Voltar ao Cardápio");
        btnVoltar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + COR_VERMELHO + "; -fx-font-weight: bold; -fx-font-size: 15px; -fx-cursor: hand; -fx-border-color: " + COR_VERMELHO + "; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-padding: 8 15 8 15;");
        btnVoltar.setOnAction(e -> {
            Stage stage = (Stage) btnVoltar.getScene().getWindow();
            stage.setScene(new Scene(new TelaClienteHome(clienteLogado).getLayout(), 1100, 700));
        });

        // CORREÇÃO: Botão Limpar lê diretamente a "etiqueta" ID do cartão agora
        Button btnLimparHistorico = new Button("🧹 Ocultar Finalizados");
        btnLimparHistorico.setStyle("-fx-background-color: transparent; -fx-text-fill: #7f8c8d; -fx-font-weight: bold; -fx-font-size: 15px; -fx-cursor: hand; -fx-border-color: #7f8c8d; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-padding: 8 15 8 15;");
        btnLimparHistorico.setOnAction(e -> {
            painelPedidos.getChildren().removeIf(node -> {
                String idCartao = node.getId();
                return "ENTREGUE".equals(idCartao) || "CANCELADO".equals(idCartao);
            });
        });

        Label lblTitulo = new Label("Meus Pedidos");
        lblTitulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Region espacadorTopo = new Region();
        HBox.setHgrow(espacadorTopo, Priority.ALWAYS);

        HBox botoesTopo = new HBox(15, btnVoltar, espacadorTopo, btnLimparHistorico);
        VBox topo = new VBox(15, botoesTopo, lblTitulo);
        topo.setPadding(new Insets(0, 0, 20, 0));
        telaPrincipal.setTop(topo);

        painelPedidos = new FlowPane();
        painelPedidos.setHgap(30);
        painelPedidos.setVgap(30);

        ScrollPane scroll = new ScrollPane(painelPedidos);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
        telaPrincipal.setCenter(scroll);

        carregarHistorico();
    }

    private void carregarHistorico() {
        painelPedidos.getChildren().clear();
        List<Pedido> meusPedidos = pedidoController.listarPedidosPorCliente((Cliente) clienteLogado);

        if (meusPedidos == null || meusPedidos.isEmpty()) {
            Label vazio = new Label("Você ainda não fez nenhum pedido.");
            vazio.setStyle("-fx-font-size: 18px; -fx-text-fill: #7f8c8d;");
            painelPedidos.getChildren().add(vazio);
            return;
        }

        for (int i = meusPedidos.size() - 1; i >= 0; i--) {
            painelPedidos.getChildren().add(criarCartaoPedido(meusPedidos.get(i)));
        }
    }

    private VBox criarCartaoPedido(Pedido p) {
        VBox cartao = new VBox(10);

        // CORREÇÃO: A etiqueta invisível que o botão "Ocultar" vai ler
        cartao.setId(p.getStatus().name());

        cartao.setPadding(new Insets(20));
        cartao.setPrefWidth(320);
        cartao.setStyle("-fx-background-color: white; -fx-background-radius: 12px;");
        DropShadow sombra = new DropShadow(); sombra.setColor(Color.rgb(0, 0, 0, 0.1)); cartao.setEffect(sombra);

        Label lblId = new Label("Pedido #" + p.getId());
        lblId.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");

        Label lblStatus = new Label("Status: " + p.getStatus().name());
        lblStatus.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: " + getCorStatus(p.getStatus()) + ";");

        StringBuilder resumo = new StringBuilder();
        for (Map.Entry<ItemCardapio, Integer> entry : p.getItens().entrySet()) {
            resumo.append(entry.getValue()).append("x ").append(entry.getKey().getNome()).append("\n");
        }
        Label lblItens = new Label(resumo.toString().trim());
        lblItens.setStyle("-fx-font-size: 13px; -fx-text-fill: #7f8c8d;");

        Label lblTotal = new Label(String.format("Total: R$ %.2f", p.getValorTotal()));
        lblTotal.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: " + COR_VERMELHO + ";");

        cartao.getChildren().addAll(lblId, lblStatus, new Separator(), lblItens, new Separator(), lblTotal);

        if (p.getStatus() == StatusPedido.AGUARDANDO_CONFIRMACAO) {
            Button btnCancelar = new Button("Cancelar Pedido");
            btnCancelar.setPrefWidth(Double.MAX_VALUE);
            btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-border-color: #e74c3c; -fx-border-radius: 5px; -fx-cursor: hand;");

            btnCancelar.setOnAction(e -> {
                try {
                    pedidoController.cancelarPedido(p);
                    carregarHistorico();
                } catch (Exception ex) {
                    Alert erro = new Alert(Alert.AlertType.ERROR); erro.setContentText(ex.getMessage()); erro.showAndWait();
                }
            });
            cartao.getChildren().add(btnCancelar);
        }

        return cartao;
    }

    private String getCorStatus(StatusPedido status) {
        switch (status) {
            case AGUARDANDO_CONFIRMACAO: return "#f39c12";
            case CONFIRMADO: case EM_PREPARO: case SAIU_PARA_ENTREGA: return "#3498db";
            case ENTREGUE: return "#2ecc71";
            case CANCELADO: return "#e74c3c";
            default: return "#7f8c8d";
        }
    }

    public BorderPane getLayout() { return telaPrincipal; }
}
