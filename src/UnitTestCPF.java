import java.util.Scanner;

public class UnitTestCPF {
    public static void main(String[] args) {
        String cpf;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the CPF number: ");
        cpf = scanner.nextLine();
        if(cpfExist(cpf)){
            System.out.println("CPF exist");
        }else {
            System.out.println("CPF does not exist");
        }
    }

    protected static boolean cpfExist(String cpf) {
        if (cpf.length() != 11) {
            return false;
        }

        //primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int firstDigit = 11 - (sum % 11);
        if (firstDigit >= 10) firstDigit = 0;

        if (firstDigit != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        //segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int secondDigit = 11 - (sum % 11);
        if (secondDigit >= 10) secondDigit = 0;

        return secondDigit == Character.getNumericValue(cpf.charAt(10));
    }
}
