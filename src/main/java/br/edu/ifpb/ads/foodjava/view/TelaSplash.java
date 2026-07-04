package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.repository.RestauranteRepository;
import br.edu.ifpb.ads.foodjava.repository.RestauranteRepositoryJsonImpl;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TelaSplash {

    private StackPane layout;

    public TelaSplash() {
        layout = new StackPane();
        // Fundo com gradiente elegante (Vermelho pro vinho)
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #ea1d2c, #b31622);");

        ImageView imgLogo = new ImageView();
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/logotipo.png"));
            imgLogo.setImage(logo);
            imgLogo.setFitWidth(500); // Logo bem grande como pedido
            imgLogo.setPreserveRatio(true);

            // Sombra para a logo saltar da tela
            DropShadow sombraLogo = new DropShadow();
            sombraLogo.setColor(Color.rgb(0, 0, 0, 0.3));
            sombraLogo.setRadius(20);
            sombraLogo.setSpread(0.1);
            imgLogo.setEffect(sombraLogo);

        } catch (Exception e) {
            System.out.println("Logotipo não encontrado.");
        }

        // Animação de batimento cardíaco / fome
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

        // Fade in (Aparecimento suave do layout)
        FadeTransition ft = new FadeTransition(Duration.seconds(1.5), layout);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public void iniciarTransicaoEroteamento(Stage stageAtual) {
        // Lógica que checa se é a primeira vez (enquanto o Splash roda)
        RestauranteRepository restauranteRepo = new RestauranteRepositoryJsonImpl();
        boolean jaExisteRestaurante = restauranteRepo.existeRestauranteCadastrado();

        // Pausa de 5 segundos exatos como você pediu
        PauseTransition delay = new PauseTransition(Duration.seconds(5.0));
        delay.setOnFinished(event -> {
            if (!jaExisteRestaurante) {
                TelaConfiguracaoRestaurante telaConfig = new TelaConfiguracaoRestaurante();
                stageAtual.setScene(new Scene(telaConfig.getLayout(), 1100, 700));
            } else {
                TelaLogin telaLogin = new TelaLogin();
                stageAtual.setScene(new Scene(telaLogin.getLayout(), 1100, 700));
            }
        });
        delay.play();
    }

    public StackPane getLayout() {
        return layout;
    }
}
