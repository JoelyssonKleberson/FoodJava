package br.edu.ifpb.ads.foodjava;

import br.edu.ifpb.ads.foodjava.view.TelaSplash;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        File dataPath = new File("data/imagens_pratos");
        if (!dataPath.exists()) {
            dataPath.mkdirs();
        }

        TelaSplash splashView = new TelaSplash();
        Scene cenaInicial = new Scene(splashView.getLayout(), 1100, 700);

        stage.setTitle("FoodJava - Sistema de Pedidos");
        stage.setScene(cenaInicial);
        stage.setResizable(false);
        stage.show();

        splashView.iniciarTransicaoEroteamento(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
