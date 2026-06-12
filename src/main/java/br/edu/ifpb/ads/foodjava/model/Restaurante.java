package br.edu.ifpb.ads.foodjava.model;

public class Restaurante {

    private String nome;
    private final String cnpj;
    private String endereco;
    private String telefone;
    private String categoriaCulinaria;
    private String email;
    private String senha;

    public Restaurante(String nome, String cnpj, String endereco, String telefone, String categoriaCulinaria, String email, String senha) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.telefone = telefone;
        this.categoriaCulinaria = categoriaCulinaria;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCategoriaCulinaria() {
        return categoriaCulinaria;
    }

    public void setCategoriaCulinaria(String categoriaCulinaria) {
        this.categoriaCulinaria = categoriaCulinaria;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}