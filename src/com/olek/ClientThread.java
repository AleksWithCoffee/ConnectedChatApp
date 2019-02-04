package com.olek;


import java.net.Socket;
import java.util.Scanner;

public class ClientThread extends Thread {
	private Socket socket;

	public ClientThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		Scanner scanner = null;
		try {
			scanner = new Scanner(socket.getInputStream());

			while (true) {
				if (scanner != null) {
					if (scanner.hasNextLine()) {

						String message;
						{
							try {
								message = scanner.nextLine();
								System.out.println(message);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			scanner.close();
		}
	}
}