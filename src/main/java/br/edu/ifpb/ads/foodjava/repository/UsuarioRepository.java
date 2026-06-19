package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.model.Usuario;

public interface UsuarioRepository {

    void salvar(Usuario usuario);
    Usuario buscarPorEmail(String email);
    Usuario buscarPorCpf(String cpf);
}
