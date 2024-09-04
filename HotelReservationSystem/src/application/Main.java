//package application;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ButtonType;
//import javafx.stage.Stage;
//import javafx.stage.Window;
//
//public class Main extends Application {
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage stage) throws Exception{
//        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/WelcomePage.fxml"));
//            Parent root = loader.load();
//
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            //make sure the stage cannot be resized
//            stage.setResizable(false);
//            stage.show();
//    }
//
//}


package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main extends Application {

    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    private static Main instance;

    public static void main(String[] args) {
        launch(args);
    }
    public static Main getInstance() {
        return instance;
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Load the WelcomePage.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/WelcomePage.fxml"));
        Parent root = loader.load();
        instance = this;

        // Set up the scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        // Connect to the server
        connectToServer();
    }

    private void connectToServer() {
        try {
            // Replace "localhost" with the server's IP address or hostname
            socket = new Socket("localhost", 8000);
            System.out.println("Connected to server");

            // Set up input and output streams for the server
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());

            // Start a new thread to handle server messages
            new Thread(()->run()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataToServer(String message) {
        try {`
            // Send data to the server
            dout.writeUTF(message);
            dout.flush(); // Flush the output stream to ensure data is sent immediately
            System.out.println("Sent to server: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run() {
        try {
            while (true) {
                // Read messages from the server
                String message = din.readUTF();
                System.out.println("Received from server: " + message);

                // Process the message (if needed)
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

