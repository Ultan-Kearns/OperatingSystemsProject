import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	private String ip;
	private int port;
	private Scanner console = new Scanner(System.in);
	private Socket server;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String message;
	public static void main(String[] args)
	{
		Client client = new Client();
		client.client();
		client.clientapp();
	}
	public void client() {
		System.out.print("Enter IP address: ");
		ip = console.next();
		System.out.print("Enter port number: ");
		port = console.nextInt();
	}
	public void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}

	public void clientapp()
	{
		
		try 
		{
			System.out.println("IN: " + ip + "  " + port);
			server = new Socket(ip,port);
			out = new ObjectOutputStream(server.getOutputStream());
			out.flush();
			in = new ObjectInputStream(server.getInputStream());
			System.out.println("Client Side ready to communicate");
			message = (String)in.readObject();
			System.out.println(message);
			message = console.next();
			sendMessage(message);
			message = (String)in.readObject();
			while(!message.equalsIgnoreCase("-12End"))
			{
				System.out.println(message);
				
				message = (String)in.readObject();
			}
			
		} 
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
