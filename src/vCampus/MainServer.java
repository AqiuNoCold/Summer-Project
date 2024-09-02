package vCampus;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class MainServer {
    private static final int PORT = 5101;
    private static final int MAX_CONNECTIONS = 10;
    private static ConcurrentHashMap<Socket, Thread> socketMap = new ConcurrentHashMap<>();


    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for client...");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                if (socketMap.size() >= MAX_CONNECTIONS) {
                    System.out.println("Max connections reached, rejecting new connection.");
                    clientSocket.close();
                    continue;
                }

                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                socketMap.put(clientSocket, clientThread);
                clientThread.start();

                System.out.println("Client connected!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            while (true) {
                // 不断读取客户端发送的对象
                Object receivedData = in.readObject();
                System.out.println("Received from client: " + receivedData + clientSocket.getRemoteSocketAddress());

                if (receivedData instanceof String data) {
                    System.out.println("Received MyData from client: " + data + " from " + clientSocket.getRemoteSocketAddress());
                    // 示例：将当前全局变量的值发送回客户端
                    String responseMessage = "Response from server: " + data;
                    out.writeObject(responseMessage);
                    out.flush();
                }
                else {
                    String responseMessage = "Unsupported data type received!";
                    out.writeObject(responseMessage);
                    out.flush();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socketMap.remove(clientSocket);
            System.out.println("Connection closed with " + clientSocket.getRemoteSocketAddress());
        }
    }
}
