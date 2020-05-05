package controller.impl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import repository.JDBConnectionWrapper;
import repository.api.*;
import repository.impl.*;
import service.api.EmployeeService;
import service.api.UserService;
import service.impl.EmployeeServiceImpl;
import service.impl.UserServiceImpl;

import java.io.IOException;

public class MyMain extends Application {

    private static JDBConnectionWrapper jdbConnectionWrapper;
    private static UserRepository userRepository;
    private static AccountRepository accountRepository;
    private static CardRepository cardRepository;
    private static ClientRepository clientRepository;
    private static TransferRepository transferRepository;
    private static AuditRepository auditRepository;
    private static UserService userService;
    private static EmployeeService employeeService;
    private static LoginController loginController;
    private static AdminController adminController;
    private static EmployeeController employeeController;
    private static final int WIDTH = 750;
    private static final int HEIGHT = 500;
    private static Stage primaryStage;


    public static void main(String[] args) {
        //jdbc connector
        jdbConnectionWrapper = new JDBConnectionWrapper("banking");
        //repo
        userRepository = new UserRepositoryImpl(jdbConnectionWrapper);
        accountRepository = new AccountRepositoryImpl(jdbConnectionWrapper);
        cardRepository = new CardRepositoryImpl(jdbConnectionWrapper);
        clientRepository = new ClientRepositoryImpl(jdbConnectionWrapper);
        transferRepository = new TransferRepositoryImpl(jdbConnectionWrapper);
        auditRepository= new AuditRepositoryImpl(jdbConnectionWrapper);
        //service
        userService = new UserServiceImpl(auditRepository, userRepository);
        employeeService = new EmployeeServiceImpl(auditRepository,accountRepository, cardRepository, clientRepository, transferRepository);
        loginController = new LoginController(userService, WIDTH, HEIGHT);
        launch(args);
    }

    public static void openEmployeeView(String username) throws IOException {
        primaryStage.close();
        employeeService.setUsername(username);
        employeeController = new EmployeeController(auditRepository, employeeService, WIDTH, HEIGHT, username);

        System.out.println(username);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MyMain.class.getResource("/EmployeeView.fxml"));
        loader.setController(employeeController);
        Parent root = loader.load();
        primaryStage = new Stage();
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setTitle("WELCOME");
        primaryStage.resizableProperty().setValue(false);
        primaryStage.show();
    }

    public static void openAdminView(String userName) throws IOException {
        primaryStage.close();
        userService.setCurrentUsername(userName);
        adminController = new AdminController(auditRepository, userService, WIDTH, HEIGHT, userName);
        System.out.println(userName);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MyMain.class.getResource("/AdminView.fxml"));
        loader.setController(adminController);
        Parent root = loader.load();
        primaryStage = new Stage();
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setTitle("WELCOME");
        primaryStage.resizableProperty().setValue(false);
        primaryStage.show();
    }

    public static void openLoginView() throws IOException {
        primaryStage.close();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MyMain.class.getResource("/LoginView.fxml"));
        loader.setController(loginController);
        Parent root = loader.load();
        primaryStage = new Stage();
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setTitle("WELCOME");
        primaryStage.resizableProperty().setValue(false);
        primaryStage.show();
    }

    @Override
    public  void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/LoginView.fxml"));
        loader.setController(loginController);
        Parent root = loader.load();
        primaryStage = new Stage();
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setTitle("WELCOME");
        primaryStage.resizableProperty().setValue(false);
        primaryStage.show();
    }
}
