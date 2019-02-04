package com.olek;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws Exception {
		System.out.print("Witaj w Connected ChatRoom :) Podaj swój nick: ");
		Scanner nicker = new Scanner(System.in);
		String nick = nicker.nextLine();
		final int PORT = 9999;
		final String HOST = "localhost";
		Socket socket = new Socket(HOST, PORT);
		ClientThread clientThread = new ClientThread(socket);
		clientThread.start();
		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		printWriter.println(nick);
		printWriter.flush();
		while (true) {
			String request = bufferedReader.readLine();
			printWriter.println(nick + "#&#" + request);
			printWriter.flush();
			if (request.equals("q")) {
//				socket.close();
				printWriter.close();
				nicker.close();
				System.exit(0);
			}
		}
	}
}
