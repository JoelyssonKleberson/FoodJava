package br.edu.ifpb.ads.foodjava.model;

public class Cliente extends AbstractUsuario {

    private final String cpf;
    private String telefone;
    private String endereco;

    public Cliente(String nome, String email, String senha, String cpf, String telefone, String endereco) {
        super(nome, email, senha);
        this.cpf = cpf;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCpf() {
        return cpf;
    }
}