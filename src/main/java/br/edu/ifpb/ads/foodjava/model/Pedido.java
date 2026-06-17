package br.edu.ifpb.ads.foodjava.model;

import br.edu.ifpb.ads.foodjava.enums.StatusPedido;
import br.edu.ifpb.ads.foodjava.exceptions.CancelamentoNaoPermitidoException;
import br.edu.ifpb.ads.foodjava.exceptions.CarrinhoVazioException;
import br.edu.ifpb.ads.foodjava.exceptions.StatusInvalidoException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Pedido {

    private String id;
    private LocalDateTime dataHora;
    private Cliente cliente;
    private Map<ItemCardapio, Integer> itens;
    private Double valorTotal;
    private StatusPedido status;

    private static final DateTimeFormatter DATA_FORMATADA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Pedido(String id, Cliente cliente, Map<ItemCardapio, Integer> itensCarrinho) throws CarrinhoVazioException {
        if (itensCarrinho == null || itensCarrinho.isEmpty()) {
            throw new CarrinhoVazioException("Não é possível fechar um pedido com o carrinho vazio");
        }

        this.id = id;
        this.cliente = cliente;
        this.dataHora = LocalDateTime.now();
        this.itens = new LinkedHashMap<>(itensCarrinho);
        this.status = StatusPedido.AGUARDANDO_CONFIRMACAO;
        calcularValor();
    }

    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getDataHoraFormatada() {
        return dataHora.format(DATA_FORMATADA);
    }

    public Map<ItemCardapio, Integer> getItens() {
        return Collections.unmodifiableMap(itens);
    }

    public StatusPedido getStatus() {
        return status;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    private void calcularValor() {
        double total = 0.0;
        for(Map.Entry<ItemCardapio, Integer> entrada : itens.entrySet()) {
            total += entrada.getKey().getPreco() * entrada.getValue();
        }
        valorTotal = total;
    }

    public void avancarStatus() throws StatusInvalidoException {
        switch (status) {
            case AGUARDANDO_CONFIRMACAO -> status = StatusPedido.CONFIRMADO;
            case CONFIRMADO -> status = StatusPedido.EM_PREPARO;
            case EM_PREPARO -> status = StatusPedido.SAIU_PARA_ENTREGA;
            case SAIU_PARA_ENTREGA -> status = StatusPedido.ENTREGUE;
            default -> throw new StatusInvalidoException("Não é possível avançar o status a partir de: " + status);
        }
    }

    public void cancelarPedido() throws CancelamentoNaoPermitidoException {
        if (status == StatusPedido.AGUARDANDO_CONFIRMACAO) {
            status = StatusPedido.CANCELADO;
        } else {
            throw new CancelamentoNaoPermitidoException("Tentativa de cancelar pedido após confirmação pelo gerente");
        }
    }
}
