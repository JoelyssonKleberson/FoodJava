package br.edu.ifpb.ads.foodjava.enums;

public enum StatusPedido {

    AGUARDANDO_CONFIRMACAO("Aguardando Confirmação..."),
    CONFIRMADO("Confirmado!"),
    EM_PREPARO("Em preparo..."),
    SAIU_PARA_ENTREGA("Saiu para entrega!"),
    ENTREGUE("Entregue!"),
    CANCELADO("Cancelado!");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
