package br.edu.ifpb.ads.foodjava.model;

import br.edu.ifpb.ads.foodjava.enums.Categoria;

import java.util.Objects;

public class ItemCardapio {

    private String nome;
    private String descricao;
    private Double preco;
    private final Categoria categoria;
    private boolean disponivel;
    private String imagemPath;

    public ItemCardapio(String nome, String descricao, Double preco, Categoria categoria, boolean disponivel, String imagemPath) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
        this.disponivel = disponivel;
        this.imagemPath = imagemPath;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public String getImagemPath() {
        return imagemPath;
    }

    public void setImagemPath(String imagemPath) {
        this.imagemPath = imagemPath;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemCardapio that = (ItemCardapio) o;
        return Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nome);
    }

    @Override
    public String toString() {
        return  String.format("Nome: %s%n" +
                "Descrição: %s%n" +
                "Preço: R$%.2f%n" +
                "Categoria: %s%n" +
                "Disponivel: %b%n" +
                "ImagemPath: %s%n",
                nome, descricao, preco, categoria, disponivel, imagemPath
        );
    }
}
