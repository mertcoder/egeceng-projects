package com.ege.reservation.ui;
import com.ege.reservation.service.UserService;
import com.ege.reservation.data.User;

public class LoginScreen  extends Screen{
    private final UserService userService;

    public LoginScreen(UserService userService, UIFactory uiFactory){
        super(uiFactory);
        this.userService = userService;
    }

    @Override
    protected void renderContent() {
        System.out.println("LOG IN");
        System.out.println("-------------------------");
        System.out.println("1 -> Log in");
        System.out.println("2 -> Register");
        System.out.println("3 -> Exit ");
        System.out.println("Type your choice: ");
    }

    @Override
    protected void handleInput() {
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> handleLogin();
            case "2" -> uiFactory.createRegisterScreen().display();
            case "3" -> {
                System.out.println("Goodbye!");
                System.exit(0);
            }
        }
    }

    private void handleLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User user = userService.login(username, password);

        if (user != null) {
            System.out.println("Login successful. Welcome, " + user.getFullName() + "!");

            // Navigate to appropriate dashboard based on user role
            if (user.isAdmin()) {
                uiFactory.createAdminDashboardScreen().display();
            } else {
                uiFactory.createMainMenuScreen().display();
            }
        } else {
            System.out.println("Invalid username or password. Please try again.");
            display();
        }
    }


}
