import java.awt.SecondaryLoop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
		PrintWriter fw = new PrintWriter(new FileOutputStream(userFile), true);
		if (line == null) {
			System.out.println("IN IF");
			fw.println(name + " " + empId + " " + email + " " + department);
		} else {
			while (line != null) {
				System.out.println("IN LOOP");
				fw.println(line);
				line = br.readLine();
			}
			fw.println(name + " " + empId + " " + email + " " + department);
		}
		fw.flush();
		fw.close();
		br.close();
	}

	public synchronized void writeBugFile(String appName, String dateTime, String platform, String description,
			String status) throws IOException {
		File bugFile = new File("C:\\Users\\G00343745\\Desktop\\bugs.txt");
		// buffered reader caused issur
		BufferedReader br = new BufferedReader(new FileReader(bugFile));
		int counter = 0;
		String line = br.readLine();
		System.out.println(line);
		PrintWriter fw = new PrintWriter(new FileOutputStream(bugFile), true);
		if (line == null) {
			counter = 1;
			System.out.println("IN IF");
			fw.println(counter + ". " + appName + " " + dateTime + " " + platform + " " + description + " " + status);
		} else {
			// since first line is already proven to exist
			counter = 1;
			while (line != null) {
				counter += 1;
				System.out.println("IN LOOP");
				fw.println(line);
				line = br.readLine();
			}
			fw.println(counter + ". " + appName + " " + dateTime + " " + platform + " " + description + " " + status);
		}
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
		String unique = userUnique(employeeId, email);
		System.out.println(unique);
		if (unique.equals("0")) {
			sendMessage("User not created Email and ID must be unique");
		} else {
			sendMessage("Unique ID + email detected writing user to file");
			writeUserFile(name, employeeId, email, department);
		}
	}

	public synchronized void registerBug() throws ClassNotFoundException, IOException {
		String appName, dateTime, platform, description, status;
		sendMessage("Please enter application name: ");
		appName = (String) in.readObject();
		sendMessage("Please enter date time: ");
		dateTime = (String) in.readObject();
		sendMessage("Please enter platform: ");
		platform = (String) in.readObject();
		sendMessage("Please enter description: ");
		description = (String) in.readObject();
		sendMessage("Please enter status: ");
		status = (String) in.readObject();
		writeBugFile(appName, dateTime, platform, description, status);
	}

	public synchronized String login(String name, String empId, String email, String department) throws IOException {
		File userFile = new File("C:\\Users\\G00343745\\Desktop\\users.txt");
		BufferedReader br = new BufferedReader(new FileReader(userFile));
		String line = br.readLine();
		String check = name + " " + empId + " " + email + " " + department;
		System.out.println(line);
		// if file is null
		if (line == null) {
			br.close();
			sendMessage("NO USERS FOUND PLEASE REGISTER");
			return "0";
		}
		while (line != null) {
			if (line.equals(check)) {
				sendMessage("logged in");
				return "1";
			}
			line = br.readLine();
		}
		sendMessage("Info incorrect");
		br.close();
		return "0";
	}

	public synchronized String userUnique(String empId, String email) throws IOException {
		String user, empUnique, emailUnique, dept;
		File userFile = new File("C:\\Users\\G00343745\\Desktop\\users.txt");
		Scanner scan = new Scanner(userFile);
		// if file is null
		if (scan.hasNext() == false) {
			scan.close();
			return "1";
		}
		while (scan.hasNext() != false) {
			user = scan.next();
			empUnique = scan.next();
			emailUnique = scan.next();
			dept = scan.next();
			if (empUnique.equalsIgnoreCase(empId) || emailUnique.equalsIgnoreCase(email) && scan.hasNext() == false) {
				return "0";
			}
			scan.nextLine();

		}
		scan.close();
		return "1";
	}

	public synchronized int readBugFile() throws IOException {
		System.out.println("In READ");
		File bugFile = new File("C:\\Users\\G00343745\\Desktop\\bugs.txt");
		BufferedReader br = new BufferedReader(new FileReader(bugFile));
		int counter = 0;
		String line = "";
		if (br.readLine() == null) {
			sendMessage("No bugs in file");
		} else {
			while (line != null) {
				line = br.readLine();
				counter += 1; 
			}
			String check = Integer.toString(counter);
			sendMessage(check);
		}	
		br.close();
		br = new BufferedReader(new FileReader(bugFile));
		if(counter > 0) {
			for(int i = 0; i < counter; i++)
			{
				line = br.readLine();
				sendMessage(line);
			}
		}
		br.close();
		System.out.println("COUNTER: " + counter);
		return counter;
	}
	public synchronized void assignDev(String id, String empId) throws IOException {
		File bugFile = new File("C:\\Users\\G00343745\\Desktop\\bugs.txt");
		Scanner scan = new Scanner(bugFile);
		PrintWriter fw = new PrintWriter(new FileOutputStream(bugFile), true);
		String bugNo = null;
		String appName, dateTime, platform, description, status;
		// if file is null
		if (!scan.hasNext()) {
			System.out.println("IN if");
			sendMessage("No bugs to assign to");
		}
		while (scan.hasNext()) {
			bugNo = scan.next();
			scan.next();
			appName = scan.next();
			dateTime = scan.next();
			platform = scan.next();
			description = scan.next();
			status = scan.next();
			System.out.println(bugNo);
			if (id.equals(bugNo)) {
			 System.out.println("IN IF");
			 status = "assigned";
			 fw.println(bugNo + " " + appName + " " + dateTime + " " + platform + " " + description + " " + status);
			 sendMessage("Assigned");
			}
			scan.nextLine();

		}
		scan.close();
		fw.close();
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
				} else if (message.equals("3")) {
					registerBug();
				}
				else if(message.equals("4"))
				{
					sendMessage("Enter bug ID to assign to developer: ");
					String id; 
					id = (String) in.readObject();
					sendMessage("Enter employee ID to assign to developer: ");
					String empId;
					empId = (String) in.readObject();
					assignDev(id,empId);
				}
				else if(message.equals("5"))
				{
					
				}
				else if(message.equals("6"))
				{
					readBugFile();
				}
				else if(message.equals("7"))
				{
					
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
