package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.enums.Categoria;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CardapioRepositoryJsonImpl implements CardapioRepository {

    private final Gson gson;
    private List<ItemCardapio> itens;
    private static final String FILE_PATH = "src/main/resources/data/cardapio.json";

    public CardapioRepositoryJsonImpl() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        itens = new ArrayList<>();
        carregarDados();
    }

    private void carregarDados() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type tipoLista = new TypeToken<List<ItemCardapio>>(){}.getType();
            List<ItemCardapio> itensCarregados = gson.fromJson(reader, tipoLista);

            if (itensCarregados != null) {
                itens = itensCarregados;
            }
        } catch (IOException e) {

        }
    }

    private void salvarDados() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(itens, writer);
        } catch (IOException e) {
            System.err.println("Erro ao guardar o cardápio: " + e.getMessage());
        }
    }

    @Override
    public void salvarItem(ItemCardapio item) {
        itens.add(item);
        salvarDados();
    }

    @Override
    public void atualizarItem(ItemCardapio itemAtualizado) {
        int index = itens.indexOf(itemAtualizado);
        if (index != -1) {
            itens.set(index, itemAtualizado);
            salvarDados();
        }
    }

    @Override
    public void removerItem(String nomeItem) {
        itens.removeIf(item -> item.getNome().equals(nomeItem));
        salvarDados();
    }

    @Override
    public List<ItemCardapio> buscarTodos() {
        return new ArrayList<>(itens);
    }

    @Override
    public List<ItemCardapio> buscarPorCategoria(Categoria categoria) {
        List<ItemCardapio> filtrados = new ArrayList<>();
        for (ItemCardapio item : itens) {
            if (item.getCategoria() == categoria) {
                filtrados.add(item);
            }
        }
        return filtrados;
    }
}
