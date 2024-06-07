package br.com.poc.desafio.mock.validador;

public class CPFValidator {

    public static boolean isValid(String cpf) {

        // Remover caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verificar se o CPF tem 11 dígitos
        if (cpf.length() != 11)
            return false;

        // Verificar se todos os dígitos são iguais
        boolean allEqual = true;
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                allEqual = false;
                break;
            }
        }
        if (allEqual)
            return false;

        // Calcular o primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (10 - i) * (cpf.charAt(i) - '0');
        }
        int firstDigit = 11 - (sum % 11);
        if (firstDigit >= 10)
            firstDigit = 0;

        // Verificar o primeiro dígito verificador
        if (cpf.charAt(9) - '0' != firstDigit)
            return false;

        // Calcular o segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (11 - i) * (cpf.charAt(i) - '0');
        }
        int secondDigit = 11 - (sum % 11);
        if (secondDigit >= 10)
            secondDigit = 0;

        // Verificar o segundo dígito verificador
        return cpf.charAt(10) - '0' == secondDigit;
    }
}
