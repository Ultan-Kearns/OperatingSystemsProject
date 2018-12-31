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
			String login = "";
			System.out.println("Client Side ready to communicate");
			// for some reason only getting intro message second time
			message = (String) in.readObject();
			System.out.println(message);
			// intro showing twice minor error 
			message = console.next();
			sendMessage(message);
			while (true) {
				// msg from choice
				if (message.equals("1")) {
					// name
					message = (String) in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					// id
					message = (String) in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					// email
					message = (String) in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					// dept
					message = (String) in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					message = (String) in.readObject();
					System.out.println(message);
				}
				else if(message.equals("2"))
				{
					//name
					message = (String) in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					//empID
					message = (String) in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					//email
					message = (String) in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					//department
					message = (String) in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					//logon messsage
					message = (String) in.readObject();
					System.out.println(message);
					login = (String) in.readObject();
				}
				else if(message.equals("3"))
				{
					//appname
					message = (String) in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					//date time
					message = (String) in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					//platform
					message = (String) in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					//desc
					message = (String) in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					//status
					message = (String) in.readObject();
					String prompt = message;
					System.out.println(message);
					//if not correct option
					do
					{
						message = console.next();
						//only show if user enters wrong option
						if(!message.equalsIgnoreCase("Open") && !message.equalsIgnoreCase("Assigned") && !message.equalsIgnoreCase("Closed"))
							System.out.println(prompt);
					}while(!message.equalsIgnoreCase("Open") && !message.equalsIgnoreCase("Assigned") && !message.equalsIgnoreCase("Closed"));
					sendMessage(message);
				}
				// msg for intro
				message = (String) in.readObject();
				System.out.println(message);
				if(login.equals("1"))
				{
					message = (String) in.readObject();
					System.out.println(message);
				}
				message = console.next();
				sendMessage(message);
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
