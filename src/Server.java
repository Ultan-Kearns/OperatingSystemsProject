import java.awt.SecondaryLoop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class Server {

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
		String line;
		BufferedReader fileRead = new BufferedReader(new FileReader(userFile));
		FileWriter fw = new FileWriter(userFile);
		System.out.println(fileRead.readLine());
		while ((line = fileRead.readLine()) != null) {
			System.out.println("IN LOOP " + line);
		}
		fw.write(name + " " + empId + " " + email + " " + department);
		fw.flush();
		fw.close();
		fileRead.close();
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

	public void run() {

		try {
			out = new ObjectOutputStream(individualconnection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(individualconnection.getInputStream());
			System.out.println("Connection" + socketid + " from IP address " + individualconnection.getInetAddress());
			sendMessage("Press 1 for register\npress 2 for login");
			while (true) {

				sendMessage("Press 1 for register\npress 2 for login");
				message = (String) in.readObject();
				// set login in class getLogin?
				int login = 0;
				if (login == 1) {
					sendMessage(
							"Press 3 for add bug report\nPress 4 for assign bug report\npress 5 to view all unassigned bugs\npress 6 to view all bugs in system\npress 7 to update bug info");
				}
				if (message.equals("1")) {
					registerUser();
					out.flush();
				} else if (message.equals("2")) {
					sendMessage("test user....");
				}
				System.out.println("THIS IS MSG " + message);
				// REad the files and send the line to the clieny
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
