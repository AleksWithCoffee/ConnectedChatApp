package com.olek;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerThread extends Thread {
	private Socket socket;
	private Scanner scanner;
	private PrintWriter printwriterToAll;
	private ArrayList<Socket> connections;
	private ArrayList<String> users;
	
	
	private boolean socketState;
	private String nick;
	private String text;
	private String[] request;
	private boolean lifeCycle;
	
	public ServerThread(Socket socket,String nick, ArrayList<Socket> connections, ArrayList<String> users) throws IOException {
		this.socket = socket;
		this.nick=nick;
		this.connections = connections;
		this.users = users;
		this.scanner = new Scanner(socket.getInputStream());
		this.lifeCycle=true;

	}

	@Override
	public void run() {
		try {
			scanner = new Scanner(socket.getInputStream());
			
			while (lifeCycle) {
				System.out.println("watek serwera ciagle dizala");
				if(!scanner.hasNextLine()) {
					lifeCycle=false;
					System.out.println(nick+ " umiera...");
				}
				if (scanner.hasNextLine()) {
					System.out.println("request accepted" + connections);
					String requestCatch = "";
					String nameFromClient = null;
					requestCatch = scanner.nextLine();
					if (requestCatch.contains("#&#")) {
						requestCatch = requestCatch.replace("#&#", ": ");
						request = requestCatch.split(": ");
						nameFromClient = request[0];
						text = request[1];
					}
					System.out.println("imie podane przez klienta: "+nameFromClient);
					System.out.println(text+" -wyslane przez:"+nick);
					if (text.equals("q")) {

						socketState = true;
						check();
					}
					System.out.println(requestCatch);
					for (int i = 0; i < connections.size(); i++) {
						printwriterToAll = null;
						try {
							printwriterToAll = new PrintWriter(
									new OutputStreamWriter(connections.get(i).getOutputStream()));
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (printwriterToAll != null) {
							printwriterToAll.println("Online: " + users + ", message: " + requestCatch);
							printwriterToAll.flush();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		
		}
	}

	public void check() throws IOException {

		if (socketState) {

			System.out.println("serwerowy reset !");
			for (int i = 0; i < connections.size(); i++) {
				if (connections.get(i) == socket) {
					connections.remove(i);
				}
			}
			for (int i = 0; i < connections.size(); i++) {

				socket = connections.get(i);
				printwriterToAll = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				printwriterToAll.println(socket.getLocalAddress().getHostName() + ": " + nick + " disconnected");
				printwriterToAll.flush();

			}
			
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).equals(this.nick)) {
					users.remove(i);
				}
			}
			socketState = false;
		}
	}

}
