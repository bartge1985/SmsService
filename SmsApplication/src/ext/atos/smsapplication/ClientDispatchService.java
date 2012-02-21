package ext.atos.smsapplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;



public class ClientDispatchService implements Runnable {
	
	public static final int SERVER_LISTEN_PORT = 4444;
	private ServerSocket server = null;
	private ClientWorker w = null;
	private JFrame app = null;
	private List<Thread> workerThreads = null;
	
	public boolean threadDone;
	
	
	public ClientDispatchService(JFrame app){
		this.app = app;
		workerThreads = new ArrayList<Thread>();
		threadDone = false;
	}
	
	public void run(){
		try{
			server = new ServerSocket(SERVER_LISTEN_PORT);
		} catch (IOException e) {
			System.out.println("Could not listen on port " + SERVER_LISTEN_PORT);
		}
		
		while(!threadDone){
	    
			try{
				//server.accept returns a client connection
				Socket clientSocket = server.accept();
				w = new ClientWorker(clientSocket , app);
				Thread t = new Thread(w);
				t.start();
				workerThreads.add(t);
			} catch (IOException e) {
				System.out.println("Accept failed: " + SERVER_LISTEN_PORT);
			} 
		}
	}
	
	public boolean closeSocket(){
		
		try{
			
			if(!threadDone){
				server.close();
				threadDone = true;
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return threadDone;
	}
	
}
