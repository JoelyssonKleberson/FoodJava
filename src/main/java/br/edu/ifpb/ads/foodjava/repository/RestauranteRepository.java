package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.model.Restaurante;

public interface RestauranteRepository {

    void salvar(Restaurante restaurante);
    Restaurante carregar();
    boolean existeRestauranteCadastrado();
}
