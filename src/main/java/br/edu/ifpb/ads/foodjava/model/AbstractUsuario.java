package br.edu.ifpb.ads.foodjava.model;

public abstract class AbstractUsuario {

    private String nome;
    private String email;
    private String senha;

    public AbstractUsuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
}