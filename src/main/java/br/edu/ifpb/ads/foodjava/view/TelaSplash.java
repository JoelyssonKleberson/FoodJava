package br.edu.ifpb.ads.foodjava.view;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;

public class TelaSplash {

    private VBox layout;

    // --- CONFIGURAÇÕES DO DESIGN ---
    private final String COR_TEXTO = "#ea1d2c"; // Vermelho iFood
    private final String FRASE_EFEITO = "Preparando a sua fome...";

    public TelaSplash() {
        inicializarTela();
    }

    private void inicializarTela() {
        layout = new VBox(25); // Aumentei o espaço entre a logo e o texto
        layout.setAlignment(Pos.CENTER);

        // --- SISTEMA DE BACKGROUND (FUNDO) ---
        // Ele tenta buscar uma imagem sua chamada fundo_splash.jpg. Se não achar, usa um gradiente moderno.
        InputStream isFundo = getClass().getResourceAsStream("/images/fundo_splash.jpg");
        if (isFundo != null) {
            Image imgFundo = new Image(isFundo);
            BackgroundImage bImg = new BackgroundImage(imgFundo,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true));
            layout.setBackground(new Background(bImg));
        } else {
            // Fallback: Gradiente cinza muito suave e moderno (estilo fundo de estúdio)
            layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #ffffff, #efefef);");
        }

        // --- LOGOTIPO ---
        ImageView imgLogo = new ImageView();
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/logotipo.png"));
            imgLogo.setImage(logo);
            imgLogo.setFitWidth(280); // Aumentei um pouco a logo!
            imgLogo.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Aviso: logotipo.png não encontrada na pasta images.");
        }

        // --- TEXTO E LOADING ---
        Label lblFrase = new Label(FRASE_EFEITO);
        lblFrase.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + COR_TEXTO + ";");

        ProgressIndicator loading = new ProgressIndicator();
        loading.setStyle("-fx-progress-color: " + COR_TEXTO + ";");
        loading.setMaxSize(50, 50); // Bolinha de carregamento maior

        layout.getChildren().addAll(imgLogo, lblFrase, loading);
    }

    public void iniciarTransicaoParaLogin(Stage stageAtual) {
        PauseTransition delay = new PauseTransition(Duration.seconds(3));

        delay.setOnFinished(event -> {
            TelaLogin telaLogin = new TelaLogin();
            Scene cenaLogin = new Scene(telaLogin.getLayout(), 1100, 700);
            stageAtual.setScene(cenaLogin);
        });

        delay.play();
    }

    public VBox getLayout() {
        return layout;
    }
}
