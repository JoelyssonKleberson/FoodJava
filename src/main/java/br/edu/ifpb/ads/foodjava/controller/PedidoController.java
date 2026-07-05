package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.model.Pedido;
import br.edu.ifpb.ads.foodjava.repository.PedidoRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PedidoController {

    private PedidoRepository pedidoRepository;

    public PedidoController(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    // Adaptado para receber o Map direto da Tela do Carrinho
    public void finalizarPedido(Cliente cliente, Map<ItemCardapio, Integer> itensCarrinho) {
        String idUnico = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // O construtor do Pedido recebe o ID, Cliente e os Itens
        Pedido novoPedido = new Pedido(idUnico, cliente, itensCarrinho);

        pedidoRepository.salvar(novoPedido);
    }

    public void avancarStatusPedido(Pedido pedido) {
        // A lógica e as exceções ficam dentro do Model (Excelente prática de POO)
        pedido.avancarStatus();

        // OBRIGATÓRIO: Salvar novamente no repositório para o JSON atualizar o status!
        pedidoRepository.salvar(pedido);
    }

    public void cancelarPedido(Pedido pedido) {
        // A lógica e as exceções ficam dentro do Model
        pedido.cancelarPedido();

        // OBRIGATÓRIO: Salvar novamente no repositório para o JSON atualizar!
        pedidoRepository.salvar(pedido);
    }

    // Nomes atualizados para baterem com o que você já tinha planejado
    public List<Pedido> listarTodosPedidos() {
        return pedidoRepository.buscarTodos();
    }

    public List<Pedido> listarPedidosPorCliente(Cliente cliente) {
        return pedidoRepository.buscarPorCliente(cliente);
    }
}
