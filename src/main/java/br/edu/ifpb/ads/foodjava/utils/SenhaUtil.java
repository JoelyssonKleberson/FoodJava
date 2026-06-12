package br.edu.ifpb.ads.foodjava.utils;

import br.edu.ifpb.ads.foodjava.exceptions.SenhaInvalidaException;

public final class SenhaUtil {

    private SenhaUtil() {}

    public static void validarSenha(String senha) throws SenhaInvalidaException {
        if (!calculoValidacaoSenha(senha)) {
            throw new SenhaInvalidaException("Senha Inválida!");
        }
    }

    private static boolean calculoValidacaoSenha(String senha) {
        if (senha.length() >= 8) {
            for (char valor : senha.toCharArray()) {
                if (Character.isDigit(valor)) {
                    return true;
                }
            }
        }
        return false;
    }
}