package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Welcome to the Calculator Client!");
            System.out.println("Enter the first number:");
            String number1 = stdIn.readLine();
            System.out.println("Enter the operation (+, -, *, /):");
            String operation = stdIn.readLine();
            System.out.println("Enter the second number:");
            String number2 = stdIn.readLine();
            String userInput = number1 + " " + operation + " " + number2;
            out.println(userInput);
            System.out.println("Result: " + in.readLine());
            out.close();
            in.close();
            stdIn.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}