package com.lindsoft.ConsoleChatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {

    private Socket socket;
    private ChatServer chatServer;
    private BufferedReader reader;
    private PrintWriter writer;
    private String nickName;

    public ClientThread(Socket socket, ChatServer chatServer) {
        this.socket = socket;
        this.chatServer = chatServer;

    }

    @Override
    public void run() {

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            printConnectedClients();

            nickName = reader.readLine();
            chatServer.addNickName(nickName);

            String messageToServer = nickName + "Has connected to the chat";
            chatServer.broadCast(messageToServer, this);

            printConnectedClients();

            String message = " ";

            do {
                message = reader.readLine();
                messageToServer = "[" + nickName + "]: " + message;
                chatServer.broadCast(messageToServer, this);

                if(message.equals("!quit")) {
                    break;
                }

            } while (true);

            chatServer.removeUser(nickName, this);
            socket.close();

            messageToServer = nickName + " left the chat.";
            chatServer.broadcast(message, this);

        } catch (IOException e) {
            e.printStackTrace();
            e.getMessage();
        }

    }

    private void printConnectedClients() {
        if(!chatServer.isEmpty()) {
            System.out.println("Server have the following clients connected:");
        } else {
            System.out.println("No other clients connected");
        }
    }

    void sendMessageToClient(String message) {
        writer.println(message);
    }

    public String getNickName() {
        return nickName;
    }
}
