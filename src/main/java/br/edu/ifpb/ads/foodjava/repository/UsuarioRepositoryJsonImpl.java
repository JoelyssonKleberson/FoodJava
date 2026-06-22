package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.model.Usuario;
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

public class UsuarioRepositoryJsonImpl implements UsuarioRepository {

    private final Gson gson;
    private List<Cliente> clientes;
    private List<Gerente> gerentes;

    private static final String FILE_PATH_CLIENTES = "src/main/resources/data/clientes.json";
    private static final String FILE_PATH_GERENTES = "src/main/resources/data/gerentes.json";

    public UsuarioRepositoryJsonImpl() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        clientes = new ArrayList<>();
        gerentes = new ArrayList<>();
        carregarDados();
    }

    private void carregarDados() {
        try (Reader reader = new FileReader(FILE_PATH_CLIENTES)) {
            Type tipoCliente = new TypeToken<List<Cliente>>(){}.getType();
            List<Cliente> clientesCarregados = gson.fromJson(reader, tipoCliente);

            if (clientesCarregados != null) {
                clientes = clientesCarregados;
            }
        } catch (IOException e) {

        }

        try (Reader reader = new FileReader(FILE_PATH_GERENTES)) {
            Type tipoGerente = new TypeToken<List<Gerente>>(){}.getType();
            List<Gerente> gerentesCarregados = gson.fromJson(reader, tipoGerente);

            if (gerentesCarregados != null) {
                gerentes = gerentesCarregados;
            }
        } catch (IOException e) {

        }
    }

    private void salvarDados() {
        try (Writer writerClientes = new FileWriter(FILE_PATH_CLIENTES);
             Writer writerGerentes = new FileWriter(FILE_PATH_GERENTES)) {

            gson.toJson(clientes, writerClientes);
            gson.toJson(gerentes, writerGerentes);
        } catch (IOException e) {
            System.err.println("Erro ao guardar os utilizadores: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Usuario usuario) {
        if (usuario instanceof Cliente) {
            clientes.removeIf(c -> c.getEmail().equals(usuario.getEmail()));
            clientes.add((Cliente) usuario);
        } else if (usuario instanceof Gerente) {
            gerentes.removeIf(g -> g.getEmail().equals(usuario.getEmail()));
            gerentes.add((Gerente) usuario);
        }
        salvarDados();
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        for (Cliente c : clientes) {
            if (c.getEmail().equals(email)) return c;
        }
        for (Gerente g : gerentes) {
            if (g.getEmail().equals(email)) return g;
        }
        return null;
    }

    @Override
    public Usuario buscarPorCpf(String cpf) {
        for (Cliente c : clientes) {
            if (c.getCpf().equals(cpf)) return c;
        }
        return null;
    }
}
