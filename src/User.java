import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class User {
    private String name;
    private String email;
    private String password;
    private String confirmedPassword;
    private String birth;
    private String cpf;
    private String phone;

    protected static String validateUser(User user) {

        // Verifica se há campos vazios
        if (user.name == null || user.email == null || user.password == null || user.confirmedPassword == null || user.cpf == null || user.birth == null || user.phone == null) {
            return "Campos obrigatórios estão vazios.";
        }

        if (!Objects.equals(user.password, user.confirmedPassword)) {
            return "As senhas não coincidem.";
        }

        if (user.password.length() < 8) {
            return "A senha deve ter pelo menos 8 caracteres.";
        }

        String passwordPattern = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$";
        Pattern passwordCompiled = Pattern.compile(passwordPattern);
        Matcher passwordMatcher = passwordCompiled.matcher(user.password);
        if (!passwordMatcher.matches()) {
            return "A senha deve conter pelo menos uma letra maiúscula e um caractere especial.";
        }

        String namePattern = "^(?!.*\\d)([A-Z][a-z]+(?: [A-Z][a-z]+)*)$";
        if (user.name.length() > 50) {
            return "O nome deve ter no máximo 50 caracteres.";
        }
        Pattern pattern = Pattern.compile(namePattern);
        Matcher matcher = pattern.matcher(user.name);
        if (!matcher.matches()) {
            return "O nome deve começar com uma letra maiúscula e não deve conter números.";
        }

        if (user.email.length() > 60) {
            return "O email deve ter no máximo 60 caracteres.";
        }

        if (!user.email.contains("@")) {
            return "Deve ser um e-mail válido.";
        }

        String phonePattern = "^[0-9]{11}$"; // Exatamente 11 dígitos
        Pattern phonePatternCompiled = Pattern.compile(phonePattern);
        Matcher phoneMatcher = phonePatternCompiled.matcher(user.phone);
        if (!phoneMatcher.matches()) {
            return "O telefone deve conter 11 dígitos (00 00000 0000).";
        }

        if (user.phone.length() > 11) {
            return "O telefone deve ter no 11 dígitos.";
        }

        if (user.cpf.length() > 11) {
            return "O CPF deve ter no máximo 11 dígitos.";
        }

        String cpfPattern = "^[0-9]{1,11}$";
        Pattern cpfPatternCompiled = Pattern.compile(cpfPattern);
        Matcher cpfMatcher = cpfPatternCompiled.matcher(user.cpf);

        if (!cpfMatcher.matches()) {
            return "O CPF deve conter apenas números.";
        }

        if (!cpfExist(user.cpf)) {
            return "O CPF informado não é válido.";
        }

        if (!isValidBirthDate(user.birth)) {
            return "A data de nascimento é inválida.";
        }

        return null;
    }


    private static boolean cpfExist(String cpf) {
        // Verifica se o CPF tem exatamente 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Validação do primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int firstDigit = 11 - (sum % 11);
        if (firstDigit >= 10) firstDigit = 0;

        if (firstDigit != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        // Validação do segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int secondDigit = 11 - (sum % 11);
        if (secondDigit >= 10) secondDigit = 0;

        return secondDigit == Character.getNumericValue(cpf.charAt(10));
    }

    private static boolean isValidBirthDate(String birthDateString) {
        // Define o formato da data
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            // Tenta fazer o parse da data de nascimento
            LocalDate birthDate = LocalDate.parse(birthDateString, dateFormatter);

            // Verifica se o mês está entre 1 e 12
            int month = birthDate.get(ChronoField.MONTH_OF_YEAR);
            if (month < 1 || month > 12) {
                return false;
            }

            // Verifica se o ano está entre 1900 e o ano atual
            int year = birthDate.get(ChronoField.YEAR);
            int currentYear = LocalDate.now().get(ChronoField.YEAR);
            if (year < 1900 || year > currentYear) {
                return false;
            }

            // Verifica se o dia é válido para o mês
            int dayOfMonth = birthDate.get(ChronoField.DAY_OF_MONTH);
            int maxDayOfMonth = birthDate.getMonth().length(birthDate.isLeapYear());  // Lida com anos bissextos
            if (dayOfMonth < 1 || dayOfMonth > maxDayOfMonth) {
                return false;
            }

            return true; // Data é válida

        } catch (DateTimeParseException e) {
            // Se a data não puder ser parseada, não é válida
            return false;
        }
    }


}

