import java.util.Scanner;

public class UserAuthentication {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password123"; // Ideally, store a hashed password

    public boolean authenticate() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String inputUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String inputPassword = scanner.nextLine();

        return USERNAME.equals(inputUsername) && PASSWORD.equals(inputPassword);
    }
}

