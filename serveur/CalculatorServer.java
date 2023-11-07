package serveur;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
public class CalculatorServer {
    private static int clientCount = 0; 
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server is running.");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientCount++;
                System.out.println("New client connected. Client count: " + clientCount);
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket; 
        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    try {
                        String[] tokens = inputLine.split(" ");
                        if (tokens.length != 3) {
                            out.println("Invalid input. Please provide input in the format: operand1 operator operand2");
                            continue;
                        }

                        double operand1 = Double.parseDouble(tokens[0]);
                        double operand2 = Double.parseDouble(tokens[2]);
                        String operator = tokens[1];

                        double result = 0;
                        switch (operator) {
                            case "+":
                                result = operand1 + operand2;
                                break;
                            case "-":
                                result = operand1 - operand2;
                                break;
                            case "*":
                                result = operand1 * operand2;
                                break;
                            case "/":
                                if (operand2 == 0) {
                                    out.println("Error: Division by zero.");
                                    continue;
                                }
                                result = operand1 / operand2;
                                break;
                            default:
                                out.println("Invalid operator. Please use one of the following: +, -, *, /");
                                continue;
                        }
                        out.println("Result: " + result);
                    } catch (NumberFormatException e) {
                        out.println("Invalid input. Please provide numeric values.");
                    }
                }
                in.close();
                out.close();
                clientSocket.close();
                clientCount--; 
                System.out.println("Client disconnected. Client count: " + clientCount);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}