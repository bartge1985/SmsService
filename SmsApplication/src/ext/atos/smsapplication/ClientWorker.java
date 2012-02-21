package ext.atos.smsapplication;

import java.net.Socket;

import javax.swing.JFrame;

public class ClientWorker implements Runnable {

	private Socket client;
	private JFrame app;
	
	public ClientWorker(Socket client, JFrame app){
		this.client = client;
		this.app = app;
	}
	
	public void run() {
		
		//Implementation of all client communication
		System.out.println("Client connection has been accepted on the server.");
	}

}
