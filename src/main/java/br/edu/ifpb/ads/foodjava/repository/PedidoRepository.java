package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.Pedido;

import java.util.List;

public interface PedidoRepository {

    void salvar(Pedido pedido);
    List<Pedido> buscarTodos();
    List<Pedido> buscarPorCliente(Cliente cliente);
}
