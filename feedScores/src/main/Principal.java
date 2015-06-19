package main;

import java.util.Date;

import socket.ClientSocketConnection;
import socket.ServerSocketConnection;
import data.ConfigData;

public class Principal {
	public static void main(String args[]) {
		if (ConfigData.deviceMode.equals("server")) {
			ServerSocketConnection serverMode = new ServerSocketConnection();
			while (true) {
				serverMode.printMessage(new Date().toString());
			}
		} else if (ConfigData.deviceMode.equals("client")) {
			ClientSocketConnection clientMode = new ClientSocketConnection();
			while (true) {
				System.out.println(clientMode.getMessage());
			}
		}
	}
}
