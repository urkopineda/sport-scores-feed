package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import data.ConfigData;

public class ClientSocketConnection {
	Socket socket = null;
	
	public ClientSocketConnection() {
		try {
			socket = new Socket(ConfigData.serverIP, ConfigData.socketPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeSocket() {
		try {
			if (socket != null && socket.isConnected()) socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getMessage() {
		if (socket != null && socket.isConnected()) {
			BufferedReader input = null;
			try {
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				return input.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			} return "ERROR";
		} else return "ERROR";
	}
}
