package br.edu.ifpb.ads.foodjava;

import br.edu.ifpb.ads.foodjava.view.TelaSplash;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Instancia a tela de Splash
        TelaSplash splashView = new TelaSplash();

        // 2. Cria a cena principal com a resolução travada
        Scene cenaInicial = new Scene(splashView.getLayout(), 1100, 700);

        // 3. Configuração do Stage (Janela)
        stage.setTitle("FoodJava");
        stage.setScene(cenaInicial);

        // Trava o redimensionamento para manter o layout perfeito e igual à referência
        stage.setResizable(false);

        stage.show();

        // 4. Inicia a transição de carregamento
        splashView.iniciarTransicaoParaLogin(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
