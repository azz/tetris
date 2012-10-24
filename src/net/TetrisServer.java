package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ServerException;

public class TetrisServer {

	ServerSocket serverSocket;
	Socket clientSocket;
	PrintWriter out;
	BufferedReader in;
	TTP protocol;
	
	public TetrisServer() throws ServerException {
		
		serverSocket = null;
		try {
			serverSocket = new ServerSocket(3210);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 3210.");
			throw new ServerException("Could not listen on port 3210");
		}
	
		clientSocket = null;
		try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
 
        try {
        	out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
        	System.err.println("Failed to get input and output stream: " + e);
        	throw new ServerException("Feiled to get inout and output stream: " + e);
        }
       
        String inputLine, outputLine;
        
        protocol = new TTP();
 
        outputLine = protocol.processInput(null);
        out.println(outputLine);
 
        try {
			while ((inputLine = in.readLine()) != null) {
			     outputLine = protocol.processInput(inputLine);
			     out.println(outputLine);
			     if (outputLine.equals("Bye."))
			        break;
			}
		} catch (IOException e) {
			System.err.println("IO Exception occured: " + e);
		}
        try {
	        out.close();
			in.close();
	        clientSocket.close();
	        serverSocket.close();
        } catch (IOException e) {
        	System.err.println("Failed to close sockets " + e);
        }
    }
}
