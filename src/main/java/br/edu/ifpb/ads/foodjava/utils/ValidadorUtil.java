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

    public static boolean isEmailGmailValido(String email) {
        if (email == null) return false;
        return email.matches("^[a-zA-Z0-9.-]{6,30}@gmail\\.com$");
    }

    public static boolean isNomeCompleto(String nome) {
        if (nome == null || nome.trim().isEmpty()) return false;
        String[] partes = nome.trim().split("\\s+");
        return partes.length >= 2 && partes[0].length() >= 2 && partes[1].length() >= 2;
    }

    public static boolean isTelefoneWhatsAppValido(String telefone) {
        if (telefone == null) return false;
        return telefone.matches("^\\(\\d{2}\\) \\d{5}-\\d{4}$");
    }

    public static boolean isCpfValido(String cpf) {
        return calculoValidacaoCpf(cpf);
    }

    public static boolean isCnpjValido(String cnpj) {
        return calculoValidacaoCnpj(cnpj);
    }

    public static boolean isSenhaValida(String senha) {
        if (senha == null) return false;
        return senha.length() >= 8 && senha.matches(".*\\d.*");
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

    private static int calcularDigito(String str, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < str.length(); i++) {
            soma += Character.getNumericValue(str.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : (11 - resto);
    }
}
