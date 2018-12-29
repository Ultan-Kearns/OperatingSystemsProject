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

	public static void main(String[] args) throws IOException {
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

	public void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public void clientapp() throws IOException {

		try {
			server = new Socket(ip, port);
			System.out.println("IN: " + ip + "  " + port);
			out = new ObjectOutputStream(server.getOutputStream());
			in = new ObjectInputStream(server.getInputStream());
			System.out.println("Client Side ready to communicate");
			//for some reason only getting intro message second time
			message = (String) in.readObject();
			System.out.println(message);
			while (true) {
				//not getting past here
				message = console.next();
				System.out.println(message);
				sendMessage(message);
				message = (String) in.readObject();
				System.out.println(message);
				message = (String) in.readObject();
				System.out.println(message);
			}
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			in.close();
			out.close();
		}

	}
}
