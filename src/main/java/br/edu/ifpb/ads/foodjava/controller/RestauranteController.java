package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.model.Restaurante;
import br.edu.ifpb.ads.foodjava.repository.RestauranteRepository;
import br.edu.ifpb.ads.foodjava.repository.UsuarioRepository;
import br.edu.ifpb.ads.foodjava.utils.SenhaUtil;
import br.edu.ifpb.ads.foodjava.utils.ValidadorUtil;

public class RestauranteController {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;

    public RestauranteController(RestauranteRepository restauranteRepository, UsuarioRepository usuarioRepository) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public boolean verificarPrimeiraExecucao() {
        return restauranteRepository.existeRestauranteCadastrado();
    }

    public void configurarRestaurante(String nome, String cnpj, String endereco, String telefone, String categoriaCulinaria, String emailGerente, String senhaGerente) {
        ValidadorUtil.validarCnpj(cnpj);
        SenhaUtil.validarSenha(senhaGerente);

        if (restauranteRepository.existeRestauranteCadastrado()) {
            throw new IllegalStateException("O restaurante já foi configurado!");
        }

        Restaurante restaurante = new Restaurante(nome, cnpj, endereco, telefone, categoriaCulinaria, emailGerente, senhaGerente);
        restauranteRepository.salvar(restaurante);

        Gerente gerente = new Gerente(nome, emailGerente, senhaGerente);
        usuarioRepository.salvar(gerente);
    }

    public Restaurante obterDadosRestaurante() {
        return restauranteRepository.carregar();
    }
}
