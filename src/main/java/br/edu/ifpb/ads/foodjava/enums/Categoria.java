package br.edu.ifpb.ads.foodjava.enums;

public enum Categoria {

    ENTRADA("Entrada"),
    PRATO_PRINCIPAL("Prato principal"),
    SOBREMESA("Sobremesa"),
    BEBIDAS("Bebidas");

    private final String descricao;

    Categoria(String descricao) {
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
