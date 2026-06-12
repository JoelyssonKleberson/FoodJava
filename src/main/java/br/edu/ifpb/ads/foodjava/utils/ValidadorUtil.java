package br.edu.ifpb.ads.foodjava.utils;

import br.edu.ifpb.ads.foodjava.exceptions.DocumentoInvalidoException;

public final class ValidadorUtil {

    private ValidadorUtil() {}

    public static void validarCpf(String cpf) throws DocumentoInvalidoException {
        if (!calculoValidacaoCpf(cpf)) {
            throw new DocumentoInvalidoException("CPF Inválido!");
        }
    }

    public static void validarCnpj(String cnpj) throws DocumentoInvalidoException {
        if (!calculoValidacaoCnpj(cnpj)) {
            throw new DocumentoInvalidoException("CNPJ Inválido!");
        }
    }

    private static boolean calculoValidacaoCpf(String cpf) {
        if (cpf.length() == 11) {
            return true;
        }
        return false;
    }

    private static boolean calculoValidacaoCnpj(String cnpj) {
        if (cnpj.length() == 14) {
            return true;
        }
        return false;
    }
}