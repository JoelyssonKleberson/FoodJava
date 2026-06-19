package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exceptions.UsuarioDuplicadoException;
import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.Usuario;
import br.edu.ifpb.ads.foodjava.repository.UsuarioRepository;
import br.edu.ifpb.ads.foodjava.utils.SenhaUtil;
import br.edu.ifpb.ads.foodjava.utils.ValidadorUtil;

public class AutenticacaoController {

    private final UsuarioRepository usuarioRepository;

    public AutenticacaoController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void cadastrarCliente(String nome, String email, String senha, String cpf, String telefone, String endereco) {
        ValidadorUtil.validarCpf(cpf);
        SenhaUtil.validarSenha(senha);

        if (usuarioRepository.buscarPorEmail(email) != null) {
            throw new UsuarioDuplicadoException("Já existe um usuário cadastrado com este e-mail!");
        }
        if (usuarioRepository.buscarPorCpf(cpf) != null) {
            throw new UsuarioDuplicadoException("Já existe um usuário cadastrado com este CPF!");
        }

        Cliente novoCliente = new Cliente(nome, email, senha, cpf, telefone, endereco);
        usuarioRepository.salvar(novoCliente);
    }

    public Usuario fazerLogin(String email, String senha) {
        Usuario usuarioEncontrado = usuarioRepository.buscarPorEmail(email);

        if (usuarioEncontrado != null && usuarioEncontrado.getSenha().equals(senha)) {
            return usuarioEncontrado;
        }
        return null;
    }
}
