package ext.atos.mobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;


public class WifiConnection implements Runnable{
	
	public static int SERVER_LISTEN_PORT = 4444;
	public static String SERVER_HOSTNAME = "localhost";
	
	private static String WIFI_CONNECTION = "WIFI_CONNECTION";
	
	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	
	
	public void run(){
		
		try{
			socket = new Socket(SERVER_HOSTNAME, SERVER_LISTEN_PORT);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String message = null;
			while ((message = in.readLine()) != null) {
			    Log.i(WIFI_CONNECTION, "Message from server: " + message);
			}
			
		} catch (IOException e) {
			Log.i(WIFI_CONNECTION, "Could not connect to the server on port: " + SERVER_LISTEN_PORT);
		}
		
		try{
			
			if(out != null){
				out.close();
			}
			
			if(in != null){
				in.close();
			}
			
			if(socket != null){
				socket.close();
			}
			
		}catch (IOException e){
			
		}
		
		Log.i(WIFI_CONNECTION, "The connection to the server has ended.");
		
	}
	
	public void closeConnection(){
		
		try{
			socket.close();
		}catch(IOException e){
			Log.i(WIFI_CONNECTION, "Exception occured while closing the socket.");
		}
	}
}
