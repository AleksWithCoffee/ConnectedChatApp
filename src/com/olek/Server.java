package com.olek;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	private ArrayList<String> users;
	private ArrayList<Socket> connections;

	public static void main(String[] args) throws Exception {
		Server server = new Server();
		server.start();
	}

	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		Scanner scanner = null;
		try {
			connections = new ArrayList<>();
			users= new ArrayList<>();
			final int PORT = 9999;
			serverSocket = new ServerSocket(PORT);
			int counter = 0;
			while (true) {
				System.out.println("Waiting for request... nr " + counter);

				socket = serverSocket.accept();  //tu sie zatrzymujemy czekajac na nastepnego klienta
				
				scanner = new Scanner(socket.getInputStream());
				String nick = scanner.nextLine();
				// ////////////////////////////na potrzeby konsolowe
				
				if(nick.equals("exit")) {
					socket.close();
					serverSocket.close();
					scanner.close();
					System.exit(0);
				}
				/////////////////////////////
				
				users.add(nick);
				/////////////////////////////////miejsce na polaczenie sie z baza sprawdzenie czy jest i odrzucenie 
				//jesli nie ma, jesli nie ma stworz nowego create ...
				connections.add(socket);
				ServerThread serverThread = new ServerThread(socket, nick,connections, users);
				serverThread.start();
				counter++;
				nick="";

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	}
}
