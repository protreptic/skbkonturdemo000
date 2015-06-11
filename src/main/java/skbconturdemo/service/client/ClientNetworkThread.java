package skbconturdemo.service.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClientNetworkThread extends Thread {
	
	private Socket socket;
	
	private volatile Boolean connected;
	
	public ClientNetworkThread(String server, int port) {
		try {
			socket = new Socket(server, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		connected = true;
	}
	
	@Override
	public void run() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		try {
			executorService.execute(new IncomingThread(socket.getInputStream()));
			executorService.execute(new OutcomingThread(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while (connected) {
			
		}
		
		executorService.shutdown();
		
		try {
			if (executorService.awaitTermination(10, TimeUnit.SECONDS)) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else throw new RuntimeException("Unexpected behavior");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private class IncomingThread implements Runnable {

		private BufferedReader reader;
		
		public IncomingThread(InputStream inputStream) {
			reader = new BufferedReader(new InputStreamReader(inputStream));
		}
		
		public void run() {
			while (connected) {
				try {
					if (reader.ready()) {
						String message = reader.readLine();
						
						if (message != null && !message.isEmpty()) {
							if (message.equals("disconnect")) {
								connected = false;
							} else {
								synchronized (System.out) {
									System.out.println(message); 
								}
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private class OutcomingThread implements Runnable {

		private PrintWriter writer;
		
		public OutcomingThread(OutputStream outputStream) {
			writer = new PrintWriter(outputStream, true);
		}
		
		public void run() {
			Scanner scanner = new Scanner(System.in);
			
			while(connected) {
				if (scanner.hasNextLine()) {
					String command = scanner.nextLine();
					
					if (command != null && !command.isEmpty()) {
						if (command.startsWith("get ")) {
							writer.println(command); 
						} else if (command.startsWith("quit")) {
							connected = false;
						} else {
							synchronized (System.out) {
								System.out.println("unknown command, use: get <prefix> or quit");  
							}
						}
					}
				}
			}
			
			scanner.close();
		}
		
	}
	
}
