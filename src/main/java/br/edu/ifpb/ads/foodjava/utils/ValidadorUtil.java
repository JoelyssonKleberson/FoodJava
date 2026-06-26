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
        if (cpf == null) return false;

        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;

        int digito1 = calcularDigito(cpf.substring(0, 9), new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2});
        int digito2 = calcularDigito(cpf.substring(0, 9) + digito1, new int[]{11, 10, 9, 8, 7, 6, 5, 4, 3, 2});

        return cpf.equals(cpf.substring(0, 9) + digito1 + digito2);
    }

    private static boolean calculoValidacaoCnpj(String cnpj) {
        if (cnpj == null) return false;

        cnpj = cnpj.replaceAll("\\D", "");
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) return false;

        int digito1 = calcularDigito(cnpj.substring(0, 12), new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
        int digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});

        return cnpj.equals(cnpj.substring(0, 12) + digito1 + digito2);
    }

    private static int calcularDigito(String texto, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < texto.length(); i++) {
            soma += Character.getNumericValue(texto.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : (11 - resto);
    }
}
