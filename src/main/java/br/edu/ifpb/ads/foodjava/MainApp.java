package br.edu.ifpb.ads.foodjava;

import br.edu.ifpb.ads.foodjava.view.TelaLogin;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        TelaLogin loginView = new TelaLogin();
        // Ajustamos as proporções iniciais para combinar com a nova interface centralizada
        Scene cenaInicial = new Scene(loginView.getLayout(), 450, 550);

        stage.setTitle("FoodJava");
        stage.setScene(cenaInicial);

        // --- MUDANÇAS AQUI ---
        stage.setMaximized(false); // Faz a tela abrir pegando o monitor inteiro
        stage.setResizable(true); // Devolve os botões de minimizar e maximizar da janela

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



