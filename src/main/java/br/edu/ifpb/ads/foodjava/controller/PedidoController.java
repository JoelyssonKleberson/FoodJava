package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.model.Carrinho;
import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.Pedido;
import br.edu.ifpb.ads.foodjava.repository.PedidoRepository;

import java.util.List;
import java.util.UUID;

public class PedidoController {

    private PedidoRepository pedidoRepository;

    public PedidoController(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public void finalizarPedido(Cliente cliente, Carrinho carrinho) {
        String idUnico = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Pedido novoPedido = new Pedido(idUnico, cliente, carrinho.getItens());

        pedidoRepository.salvar(novoPedido);
        carrinho.limparCarrinho();
    }

    public void avancarStatusPedido(Pedido pedido) {
        pedido.avancarStatus();
    }

    public void cancelarPedido(Pedido pedido) {
        pedido.cancelarPedido();
    }

    public List<Pedido> listarTodosPedidos() {
        return pedidoRepository.buscarTodos();
    }

    public List<Pedido> listarPedidosPorCliente(Cliente cliente) {
        return pedidoRepository.buscarPorCliente(cliente);
    }
}
