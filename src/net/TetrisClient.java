package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TetrisClient {

	String hostName;
	Socket tSocket;
	PrintWriter out;
	BufferedReader in;
	BufferedReader stdIn;
	Thread clientThread;
	
	public TetrisClient(String hostName) throws ConnectException {
		
		this.hostName = hostName;
		
		clientThread = createThread();
		
		clientThread.run();
		
	}

	public void connect() throws ConnectException {

		tSocket = null;
		out = null;
		in = null;
		try {
			tSocket = new Socket(hostName, 3210);
			out = new PrintWriter(tSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					tSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + hostName);
			throw new ConnectException("Could not connect to host: " + hostName);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + hostName);
			throw new ConnectException("Could not get I/O from the host: " + hostName);
		}
		stdIn = new BufferedReader(new InputStreamReader(System.in));
		
		recieve();
	}

	public Thread createThread() {
		
		return new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					connect();
				} catch (ConnectException e) {
				}
			}
		});
	}
	
	public void recieve() {

		String fromServer;
		String fromUser = null;

		try {
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server: " + fromServer);
				if (fromServer.equals("Bye."))
					break;
				fromUser = stdIn.readLine();

			}
		} catch (IOException e) {
			System.err.println("An IO Exception occured: " + e);
		}
		if (fromUser != null) {
			System.out.println("Client: " + fromUser);
			out.println(fromUser);
		}
		
		close();
	}
	
	public void close() {
		
		try {
			out.close();
			in.close();
			stdIn.close();
			tSocket.close();
		} catch (IOException e) {
			System.err.println("Failed to close socket");
		}
	}
}
