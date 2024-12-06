package main.java;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        try(ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started.");

            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("New line connected.");

                new Thread(() -> hadleRequest(socket)).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void hadleRequest(Socket socket) {
        try (BufferedReader input = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream(),
                        StandardCharsets.UTF_8));
             PrintWriter output = new PrintWriter(socket.getOutputStream())) {

            while (!input.ready()) ;

            String firstLine = input.readLine();
            String[] parts = firstLine.split(" ");

            System.out.println(parts);

            while (input.ready()) {
                System.out.println(input.readLine());
            }

            Path path = Paths.get("C:\\Users\\matve\\Desktop\\simple-server\\simple-server\\www", parts[1]);
            if(!Files.exists(path)) {
                output.println("HTTP/1.1 404 NOT_FOUND");
                output.println("HTTP/1.1 200 OK");
                output.println("Content-Type: text/html; charset=utf-8");
                output.println();
                output.println("<p>File not found</p>");
                output.flush();
                return;
            }

            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/html; charset=utf-8");
            output.println();

            Files.newBufferedReader(path).transferTo(output);
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
