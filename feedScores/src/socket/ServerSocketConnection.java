package socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import data.ConfigData;

public class ServerSocketConnection {
	ServerSocket listener = null;
	Socket socket = null;
	
	public ServerSocketConnection() {
		try {
			listener = new ServerSocket(ConfigData.socketPort);
			socket = listener.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeSocket() {
		try {
			if (listener != null && !listener.isClosed()) listener.close();
			if (socket != null && socket.isConnected()) socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printMessage(String message) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
