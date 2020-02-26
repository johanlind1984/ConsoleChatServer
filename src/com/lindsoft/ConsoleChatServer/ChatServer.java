package com.lindsoft.ConsoleChatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {

    Set<String> nickNames;
    Set<ClientThread> clientThreads;
    int port;

    public ChatServer(int port) {
        this.port = port;
        nickNames = new HashSet<>();
        clientThreads = new HashSet<>();
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                System.out.println("Running server and waiting for clients to connect...");
                Socket socket = serverSocket.accept();
                System.out.println("User Connected");
                ClientThread newClient = new ClientThread(socket, this);
                clientThreads.add(newClient);
                newClient.start();
                printClients(newClient);

            }
        } catch (
                IOException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    void broadcast(String message, ClientThread excludeUser) {
        for (ClientThread user : clientThreads) {
            if (user != excludeUser) {
                user.sendMessageToClient(message);
            }
        }
    }

    public boolean isEmpty() {
        if(nickNames.size()<1) {
            return true;
        } else {
            return false;
        }
    }

    public void printClients(ClientThread client) {
        for (String nickName:nickNames) {
            System.out.println(nickName);
        }
    }

    public void addNickName(String nickName) {
        nickNames.add(nickName);
    }

    public void broadCast(String message, ClientThread clientThread) {
        for (ClientThread client : clientThreads) {
            if(client != clientThread) {
                client.sendMessageToClient(message);
            }
        }
    }

    public void removeUser(String nickName, ClientThread clientThread) {
        if(clientThreads.contains(clientThread)) {
            clientThreads.remove(clientThread);
            nickNames.remove(clientThread.getNickName());
            System.out.println(clientThread.getNickName() +  " has left the chat.");
        }
    }
}
