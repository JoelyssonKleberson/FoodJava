package br.edu.ifpb.ads.foodjava;

import br.edu.ifpb.ads.foodjava.view.TelaSplash;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        // Agora o MainApp SEMPRE abre o Splash primeiro, mantendo a imersão do usuário
        TelaSplash splashView = new TelaSplash();
        Scene cenaInicial = new Scene(splashView.getLayout(), 1100, 700);

        stage.setTitle("FoodJava - Sistema de Pedidos");
        stage.setScene(cenaInicial);
        stage.setResizable(false);
        stage.show();

        // A lógica de roteamento agora fica dentro do Splash
        splashView.iniciarTransicaoEroteamento(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
