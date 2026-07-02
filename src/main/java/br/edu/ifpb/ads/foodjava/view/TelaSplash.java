package br.edu.ifpb.ads.foodjava.view;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class TelaSplash {

    private StackPane layout;

    public TelaSplash() {
        layout = new StackPane();
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #ea1d2c, #b31622);");

        ImageView imgLogo = new ImageView();
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/logotipo.png"));
            imgLogo.setImage(logo);
            imgLogo.setFitWidth(500);
            imgLogo.setPreserveRatio(true);

            DropShadow sombraLogo = new DropShadow();
            sombraLogo.setColor(Color.rgb(0, 0, 0, 0.3));
            sombraLogo.setRadius(20);
            sombraLogo.setSpread(0.1);
            imgLogo.setEffect(sombraLogo);

        } catch (Exception e) {
            System.out.println("Logotipo não encontrado.");
        }

        ScaleTransition pulse = new ScaleTransition(Duration.seconds(1.2), imgLogo);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.05);
        pulse.setToY(1.05);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.play();

        ProgressIndicator loading = new ProgressIndicator();
        loading.setMaxSize(40, 40);
        loading.setStyle("-fx-progress-color: white;");
        StackPane.setAlignment(loading, Pos.BOTTOM_CENTER);
        StackPane.setMargin(loading, new javafx.geometry.Insets(0, 0, 50, 0));

        layout.getChildren().addAll(imgLogo, loading);

        FadeTransition ft = new FadeTransition(Duration.seconds(1.5), layout);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public void iniciarTransicaoParaLogin(javafx.stage.Stage stageAtual) {
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> {
            TelaLogin telaLogin = new TelaLogin();
            stageAtual.setScene(new javafx.scene.Scene(telaLogin.getLayout(), 1100, 700));
        });
        delay.play();
    }

    public StackPane getLayout() {
        return layout;
    }
}
