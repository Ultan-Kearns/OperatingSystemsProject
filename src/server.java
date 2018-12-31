import java.awt.SecondaryLoop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ServerSocket listener;
		int clientid = 0;
		try {
			listener = new ServerSocket(5000, 10);

			while (true) {
				System.out.println("Main thread listening for incoming new connections");
				Socket newconnection = listener.accept();

				System.out.println("New connection received and spanning a thread");
				Connecthandler t = new Connecthandler(newconnection, clientid);
				clientid++;
				t.start();
			}

		}

		catch (IOException e) {
			System.out.println("Socket not opened");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class Connecthandler extends Thread {

	Socket individualconnection;
	int socketid;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;

	public Connecthandler(Socket s, int i) {
		individualconnection = s;
		socketid = i;
	}

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("client> " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public synchronized void writeUserFile(String name, String empId, String email, String department)
			throws IOException {
		File userFile = new File("C:\\Users\\G00343745\\Desktop\\users.txt");
		// buffered reader caused issur
		 BufferedReader br = new BufferedReader(new FileReader(userFile));
		String line = br.readLine();
		System.out.println(line);
		PrintWriter fw = new PrintWriter(new FileOutputStream(userFile),true);
		if(line == null)
		{
			System.out.println("IN IF");
			fw.println(name + " " + empId + " " + email + " " + department);
		}
		while(line != null)
		{
			System.out.println("IN LOOP");
			fw.println(line);
			line = br.readLine();
		}
		fw.println(name + " " + empId + " " + email + " " + department);
		fw.flush();
		fw.close();
		br.close();
	}
	
	public synchronized void registerUser() throws ClassNotFoundException, IOException {
		String name, employeeId, email, department;
		// check to if user file created;
		sendMessage("Please enter name: ");
		name = (String) in.readObject();
		sendMessage("Please enter employee id: ");
		employeeId = (String) in.readObject();
		sendMessage("Please enter Email: ");
		email = (String) in.readObject();
		sendMessage("Please enter Department: ");
		department = (String) in.readObject();
		System.out.println(name + " " + employeeId + " " + email + " " + department);
		writeUserFile(name, employeeId, email, department);
	}
	public synchronized String login(String name, String empId, String email, String department ) throws IOException
	{
		File userFile = new File("C:\\Users\\G00343745\\Desktop\\users.txt");
		 BufferedReader br = new BufferedReader(new FileReader(userFile));
		String line = br.readLine();
		String check = name + " " + empId + " " + email + " " + department;
		System.out.println(line);
		//if file is null
		if(line == null)
		{
			sendMessage("NO USERS FOUND PLEASE REGISTER");
			return "0";
		}
		while(line != null)
		{ 
			if(line.equals(check))
			{
				sendMessage("logged in");
				return "1";
			}
			line = br.readLine();
		}
		sendMessage("Info incorrect");
		return "0";
	}
	public void run() {

		try {
			out = new ObjectOutputStream(individualconnection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(individualconnection.getInputStream());
			String login = "";
			System.out.println("Connection" + socketid + " from IP address " + individualconnection.getInetAddress());
			sendMessage("Press 1 for register\npress 2 for login");
			message = (String) in.readObject();
			while (true) {
				if (message.equals("1")) {
					registerUser();
					out.flush();
				} else if (message.equals("2")) {
					String name, empId, email, department;
					sendMessage("Enter name");
				    name = (String) in.readObject();
					sendMessage("Enter employee id");
				    empId = (String) in.readObject();
					sendMessage("Enter email");
				    email = (String) in.readObject();
					sendMessage("Enter department");
				    department = (String) in.readObject();
					login = login(name, empId, email, department);
					System.out.println(login);
					sendMessage(login);
				}
				sendMessage("Press 1 for register\npress 2 for login");
				// set login in class getLogin?
				if (login.equals("1")) {
					sendMessage(
							"Press 3 for add bug report\nPress 4 for assign bug report\npress 5 to view all unassigned bugs\npress 6 to view all bugs in system\npress 7 to update bug info");
				}
				message = (String) in.readObject();
			}
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			try {
				out.close();
				in.close();
				individualconnection.close();
			}

			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
