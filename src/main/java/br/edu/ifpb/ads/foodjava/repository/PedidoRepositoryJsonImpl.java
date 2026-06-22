package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.Pedido;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepositoryJsonImpl implements PedidoRepository {

    private final Gson gson;
    private List<Pedido> pedidos;
    private static final String FILE_PATH = "src/main/resources/data/pedidos.json";

    public PedidoRepositoryJsonImpl() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                        new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context) ->
                        LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .setPrettyPrinting()
                .create();

        pedidos = new ArrayList<>();
        carregarDados();
    }

    private void carregarDados() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type tipoLista = new TypeToken<List<Pedido>>(){}.getType();
            List<Pedido> pedidosCarregados = gson.fromJson(reader, tipoLista);

            if (pedidosCarregados != null) {
                pedidos = pedidosCarregados;
            }
        } catch (IOException e) {

        }
    }

    private void salvarDados() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(pedidos, writer);
        } catch (IOException e) {
            System.err.println("Erro ao guardar os pedidos: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Pedido pedido) {
        pedidos.removeIf(p -> p.getId().equals(pedido.getId()));
        pedidos.add(pedido);
        salvarDados();
    }

    @Override
    public List<Pedido> buscarTodos() {
        return new ArrayList<>(pedidos);
    }

    @Override
    public List<Pedido> buscarPorCliente(Cliente cliente) {
        List<Pedido> filtrados = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (pedido.getCliente().getEmail().equals(cliente.getEmail())) {
                filtrados.add(pedido);
            }
        }
        return filtrados;
    }
}
