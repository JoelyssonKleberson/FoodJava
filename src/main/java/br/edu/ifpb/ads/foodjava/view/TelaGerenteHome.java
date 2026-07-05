package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.controller.CardapioController;
import br.edu.ifpb.ads.foodjava.controller.PedidoController;
import br.edu.ifpb.ads.foodjava.enums.Categoria;
import br.edu.ifpb.ads.foodjava.enums.StatusPedido;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.model.Pedido;
import br.edu.ifpb.ads.foodjava.model.Usuario;
import br.edu.ifpb.ads.foodjava.repository.CardapioRepositoryJsonImpl;
import br.edu.ifpb.ads.foodjava.repository.PedidoRepositoryJsonImpl;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.List;
import java.util.Map;

public class TelaGerenteHome {

    private final String COR_SIDEBAR = "#2c3e50";
    private final String COR_VERMELHO = "#ea1d2c";
    private final String COR_FUNDO = "#f4f4f4";

    private BorderPane telaPrincipal;
    private Usuario gerenteLogado;
    private CardapioController cardapioController;
    private PedidoController pedidoController;

    private Button btnPedidos;
    private Button btnCardapio;

    private VBox colPendentes;
    private VBox colPreparo;
    private VBox colEntregues;

    public TelaGerenteHome(Usuario gerenteLogado) {
        this.gerenteLogado = gerenteLogado;

        this.cardapioController = new CardapioController(new CardapioRepositoryJsonImpl(), new PedidoRepositoryJsonImpl());
        this.pedidoController = new PedidoController(new PedidoRepositoryJsonImpl());

        telaPrincipal = new BorderPane();
        telaPrincipal.setStyle("-fx-background-color: " + COR_FUNDO + ";");

        telaPrincipal.setLeft(criarSidebar());
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
        btnSair.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; -fx-font-size: 15px; -fx-font-weight: bold; -fx-alignment: CENTER_LEFT; -fx-padding: 10 20 10 20; -fx-cursor: hand; -fx-border-color: #e74c3c; -fx-border-width: 2px; -fx-border-radius: 8px;");

        btnSair.setOnAction(e -> {
            Stage stage = (Stage) btnSair.getScene().getWindow();
            TelaSplash splash = new TelaSplash();
            stage.setScene(new Scene(splash.getLayout(), 1100, 700));
            splash.iniciarTransicaoEroteamento(stage);
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

    private void abrirAbaPedidos() {
        atualizarBotoesMenu(btnPedidos);

        VBox layoutCentral = new VBox(25);
        layoutCentral.setPadding(new Insets(40));

        Label tituloAba = new Label("Pedidos em Andamento");
        tituloAba.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox kanbanBoard = new HBox(20);
        kanbanBoard.setAlignment(Pos.TOP_CENTER);

        colPendentes = criarColunaKanban("⏳ Pendentes", "#f39c12");
        colPreparo = criarColunaKanban("👨‍🍳 Em Preparo", "#3498db");
        colEntregues = criarColunaKanban("✅ Entregues", "#2ecc71");

        Button btnLimparEntregues = new Button("🧹 Limpar Lista");
        btnLimparEntregues.setStyle("-fx-background-color: transparent; -fx-text-fill: #7f8c8d; -fx-cursor: hand;");
        btnLimparEntregues.setOnAction(e -> {
            colEntregues.getChildren().removeIf(node -> node instanceof VBox);
        });
        colEntregues.getChildren().add(btnLimparEntregues);

        HBox.setHgrow(colPendentes, Priority.ALWAYS);
        HBox.setHgrow(colPreparo, Priority.ALWAYS);
        HBox.setHgrow(colEntregues, Priority.ALWAYS);
        kanbanBoard.getChildren().addAll(colPendentes, colPreparo, colEntregues);

        layoutCentral.getChildren().addAll(tituloAba, kanbanBoard);

        ScrollPane scroll = new ScrollPane(layoutCentral);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
        telaPrincipal.setCenter(scroll);

        atualizarKanban();
    }

    private void atualizarKanban() {
        colPendentes.getChildren().removeIf(node -> node instanceof VBox);
        colPreparo.getChildren().removeIf(node -> node instanceof VBox);
        colEntregues.getChildren().removeIf(node -> node instanceof VBox);

        List<Pedido> pedidos = pedidoController.listarTodosPedidos();

        for (Pedido p : pedidos) {
            if (p.getStatus() == StatusPedido.CANCELADO) continue;

            VBox cartao = criarCartaoPedido(p);

            if (p.getStatus() == StatusPedido.AGUARDANDO_CONFIRMACAO || p.getStatus() == StatusPedido.CONFIRMADO) {
                colPendentes.getChildren().add(cartao);
            } else if (p.getStatus() == StatusPedido.EM_PREPARO || p.getStatus() == StatusPedido.SAIU_PARA_ENTREGA) {
                colPreparo.getChildren().add(cartao);
            } else if (p.getStatus() == StatusPedido.ENTREGUE) {
                colEntregues.getChildren().add(cartao);
            }
        }
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

    private VBox criarCartaoPedido(Pedido p) {
        VBox cartao = new VBox(8);
        cartao.setStyle("-fx-background-color: white; -fx-background-radius: 8px; -fx-padding: 15;");
        DropShadow sombra = new DropShadow(); sombra.setColor(Color.rgb(0, 0, 0, 0.1)); cartao.setEffect(sombra);

        Label lblCliente = new Label("#" + p.getId() + " - " + p.getCliente().getNome());
        lblCliente.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2c3e50;");

        StringBuilder resumo = new StringBuilder();
        for (Map.Entry<ItemCardapio, Integer> entry : p.getItens().entrySet()) {
            resumo.append(entry.getValue()).append("x ").append(entry.getKey().getNome()).append("\n");
        }
        Label lblResumo = new Label(resumo.toString().trim());
        lblResumo.setStyle("-fx-font-size: 13px; -fx-text-fill: #7f8c8d;");

        Label lblStatus = new Label("Status: " + p.getStatus().name());
        lblStatus.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #e67e22;");

        cartao.getChildren().addAll(lblCliente, lblResumo, lblStatus);

        if (p.getStatus() != StatusPedido.ENTREGUE) {
            Button btnAvancar = new Button("Avançar Status ➔");
            btnAvancar.setMaxWidth(Double.MAX_VALUE);
            btnAvancar.setStyle("-fx-background-color: " + COR_VERMELHO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5px;");

            btnAvancar.setOnAction(e -> {
                try {
                    pedidoController.avancarStatusPedido(p);
                    atualizarKanban();
                } catch (Exception ex) {
                    mostrarAlertaErro(ex.getMessage());
                }
            });
            cartao.getChildren().add(btnAvancar);
        }

        return cartao;
    }

    private void abrirAbaCardapio() {
        atualizarBotoesMenu(btnCardapio);

        VBox layoutCentral = new VBox(25);
        layoutCentral.setPadding(new Insets(40));

        Label tituloAba = new Label("Gerenciamento de Cardápio");
        tituloAba.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Region espacadorTopo = new Region();
        HBox.setHgrow(espacadorTopo, Priority.ALWAYS);

        Button btnImportar = new Button("📥 Importar JSON");
        btnImportar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px; -fx-pref-height: 40px; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 0 20 0 20;");

        btnImportar.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecione o arquivo do Cardápio");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos JSON", "*.json"));
            File file = fileChooser.showOpenDialog(telaPrincipal.getScene().getWindow());

            if (file != null) {
                try {
                    cardapioController.importarCardapio(file.getAbsolutePath());
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Importação Concluída");
                    alerta.setHeaderText(null);
                    alerta.setContentText("O cardápio foi importado com sucesso!");
                    alerta.showAndWait();
                    abrirAbaCardapio();
                } catch (Exception ex) {
                    mostrarAlertaErro("Erro ao importar: " + ex.getMessage());
                }
            }
        });

        HBox topoBox = new HBox(tituloAba, espacadorTopo, btnImportar);
        topoBox.setAlignment(Pos.CENTER_LEFT);

        VBox cartaoFormulario = new VBox(20);
        cartaoFormulario.setPadding(new Insets(30));
        cartaoFormulario.setStyle("-fx-background-color: white; -fx-background-radius: 12px;");
        DropShadow sombra = new DropShadow(); sombra.setColor(Color.rgb(0, 0, 0, 0.1)); sombra.setRadius(15);
        cartaoFormulario.setEffect(sombra);

        montarFormularioCardapio(cartaoFormulario);

        VBox cartaoLista = new VBox(15);
        cartaoLista.setPadding(new Insets(30));
        cartaoLista.setStyle("-fx-background-color: white; -fx-background-radius: 12px;");
        cartaoLista.setEffect(new DropShadow(15, Color.rgb(0,0,0,0.1)));

        Label lblLista = new Label("Itens Cadastrados no Cardápio");
        lblLista.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        cartaoLista.getChildren().add(lblLista);

        VBox containerItensCadastrados = new VBox(10);
        atualizarListaCardapio(containerItensCadastrados);
        cartaoLista.getChildren().add(containerItensCadastrados);

        layoutCentral.getChildren().addAll(topoBox, cartaoFormulario, cartaoLista);

        ScrollPane scroll = new ScrollPane(layoutCentral);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
        telaPrincipal.setCenter(scroll);
    }

    private void atualizarListaCardapio(VBox container) {
        container.getChildren().clear();
        List<ItemCardapio> itens = cardapioController.listarCardapioCompleto();

        if (itens == null || itens.isEmpty()) {
            container.getChildren().add(new Label("Nenhum item cadastrado no sistema ainda."));
            return;
        }

        for (ItemCardapio item : itens) {
            HBox linha = new HBox(15);
            linha.setAlignment(Pos.CENTER_LEFT);
            linha.setStyle("-fx-border-color: #ecf0f1; -fx-border-width: 0 0 1 0; -fx-padding: 10;");

            Label lblNome = new Label(item.getNome());
            lblNome.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
            Label lblPreco = new Label(String.format("R$ %.2f", item.getPreco()));

            Region espacador = new Region();
            HBox.setHgrow(espacador, Priority.ALWAYS);

            Button btnRemover = new Button("🗑 Remover");
            btnRemover.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; -fx-cursor: hand;");

            btnRemover.setOnAction(e -> {
                try {
                    cardapioController.removerItem(item);
                    atualizarListaCardapio(container);
                } catch (Exception ex) {
                    mostrarAlertaErro(ex.getMessage());
                }
            });

            linha.getChildren().addAll(lblNome, lblPreco, espacador, btnRemover);
            container.getChildren().add(linha);
        }
    }

    private void montarFormularioCardapio(VBox cartaoFormulario) {
        cartaoFormulario.getChildren().clear();

        Label lblNovoItem = new Label("Adicionar Novo Prato Manualmente");
        lblNovoItem.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + COR_VERMELHO + ";");

        TextField txtNome = new TextField(); txtNome.setPromptText("Ex: Pizza Marguerita"); estilizarCampo(txtNome);
        ComboBox<Categoria> cbCategoria = new ComboBox<>();
        cbCategoria.getItems().addAll(Categoria.values());
        cbCategoria.setPromptText("Categoria");
        cbCategoria.setStyle("-fx-pref-height: 40px; -fx-font-size: 14px; -fx-background-radius: 5px; -fx-border-color: #e2e8f0; -fx-border-radius: 5px;");

        HBox linha1 = new HBox(20, criarBoxComLabel("Nome do Prato", txtNome), criarBoxComLabel("Categoria", cbCategoria));
        HBox.setHgrow(linha1.getChildren().get(0), Priority.ALWAYS);

        TextField txtDescricao = new TextField(); txtDescricao.setPromptText("Descrição apetitosa..."); estilizarCampo(txtDescricao);

        TextField txtImagem = new TextField(); txtImagem.setPromptText("Ex: pizza_queijo.jpg"); estilizarCampo(txtImagem);

        HBox linha2 = new HBox(20, criarBoxComLabel("Descrição", txtDescricao), criarBoxComLabel("Nome da Imagem", txtImagem));
        HBox.setHgrow(linha2.getChildren().get(0), Priority.ALWAYS);

        TextField txtPreco = new TextField(); txtPreco.setPromptText("Ex: 45.90"); txtPreco.setPrefWidth(150); estilizarCampo(txtPreco);

        Button btnSalvar = new Button("Salvar no Cardápio");
        btnSalvar.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px; -fx-pref-height: 40px; -fx-pref-width: 200px; -fx-background-radius: 8px; -fx-cursor: hand;");

        HBox linha3 = new HBox(20, criarBoxComLabel("Preço (R$)", txtPreco));
        linha3.setAlignment(Pos.CENTER_LEFT);
        Region espacadorBtn = new Region(); HBox.setHgrow(espacadorBtn, Priority.ALWAYS);
        linha3.getChildren().addAll(espacadorBtn, btnSalvar);

        btnSalvar.setOnAction(e -> {
            try {
                String nome = txtNome.getText();
                String desc = txtDescricao.getText();
                double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
                Categoria cat = cbCategoria.getValue();

                if (nome.trim().isEmpty() || cat == null) throw new IllegalArgumentException("Preencha todos os campos obrigatórios!");

                // O CÉREBRO DA IMAGEM: Ele salva APENAS o nome do arquivo, e garante que tenha extensão!
                String imgNome = txtImagem.getText().trim();
                String nomeArquivoImagem = null;

                if (!imgNome.isEmpty()) {
                    if (imgNome.toLowerCase().endsWith(".jpg")) {
                        nomeArquivoImagem = imgNome;
                    } else {
                        nomeArquivoImagem = imgNome + ".jpg"; // Adiciona .jpg automático se o cara esquecer
                    }
                }

                cardapioController.adicionarItem(nome, desc, preco, cat, true, nomeArquivoImagem);
                mostrarSucessoInterno(cartaoFormulario, nome);

            } catch (NumberFormatException ex) {
                mostrarAlertaErro("Preço inválido! Use formato 00.00");
            } catch (Exception ex) {
                mostrarAlertaErro(ex.getMessage());
            }
        });

        cartaoFormulario.getChildren().addAll(lblNovoItem, linha1, linha2, linha3);
    }

    private void mostrarSucessoInterno(VBox cartaoFormulario, String nomePrato) {
        cartaoFormulario.getChildren().clear();
        Label check = new Label("✔️"); check.setStyle("-fx-font-size: 50px; -fx-text-fill: #2ecc71;");
        Label lblSucesso = new Label("Prato Adicionado!"); lblSucesso.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        cartaoFormulario.setAlignment(Pos.CENTER);
        cartaoFormulario.getChildren().addAll(check, lblSucesso, new Label(nomePrato + " está no cardápio."));

        PauseTransition delay = new PauseTransition(Duration.seconds(2.0));
        delay.setOnFinished(ev -> {
            cartaoFormulario.setAlignment(Pos.TOP_LEFT);
            abrirAbaCardapio();
        });
        delay.play();
    }

    private void estilizarCampo(Control campo) {
        campo.setStyle("-fx-pref-height: 40px; -fx-background-radius: 5px; -fx-font-size: 14px; -fx-border-color: #e2e8f0; -fx-border-radius: 5px;");
    }

    private VBox criarBoxComLabel(String textoLabel, Control controle) {
        Label lbl = new Label(textoLabel); lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #7f8c8d; -fx-font-size: 13px;");
        VBox box = new VBox(5, lbl, controle);
        if (controle instanceof TextField) HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Atenção"); alerta.setHeaderText(null); alerta.setContentText(mensagem); alerta.showAndWait();
    }

    public BorderPane getLayout() { return telaPrincipal; }
}
