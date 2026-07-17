package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.model.Restaurante;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class RestauranteRepositoryJsonImpl implements RestauranteRepository {

    private final Gson gson;
    private static final String FILE_PATH = "data/restaurante.json";

    public RestauranteRepositoryJsonImpl() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void salvar(Restaurante restaurante) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(restaurante, writer);
        } catch (IOException e) {
            System.err.println("Erro ao guardar os dados do restaurante: " + e.getMessage());
        }
    }

    @Override
    public Restaurante carregar() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            return gson.fromJson(reader, Restaurante.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean existeRestauranteCadastrado() {
        return carregar() != null;
    }
}
