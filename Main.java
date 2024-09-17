import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Main {
	public static String PATH = "C:\\Users\\97156\\OneDrive\\Desktop\\distributed systems\\section\\again\\";

	public static void main(String[] args) {
		// int pId = Integer.parseInt(args[0]);
		int pId = 3;

		try {
			System.out.println("Process: " + NodeI.services[pId]);

			Node obj = new Node(pId);
			initServer(obj);
			initClient(obj, pId);

		} catch (RemoteException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void initServer(Node obj) throws RemoteException {
		Registry reg = LocateRegistry.createRegistry(obj.myPort);
		reg.rebind(obj.myService, obj);// momkn el glt hena .
	}

	private static boolean isValidFileExtension(String fileName) {
		// Define a list of valid file extensions
		String[] validExtensions = { ".txt", ".mp3", ".jpg", ".pdf" /* Add more extensions as needed */ };

		for (String extension : validExtensions) {
			if (fileName.toLowerCase().endsWith(extension)) {
				return true;
			}
		}

		return false;
	}

	private static void initClient(Node obj, int pId) throws RemoteException, NotBoundException {

		Timer timer = new Timer();

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					obj.fetchNewTransaction();
				} catch (RemoteException ex) {
					System.out.println(ex.getMessage());
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 0, 100);

		// search and search file
		// for raft rkz fel timeout value,rkz fel time raft call

		while (true) {

			// Get message from client console
			System.out.println("1 - Upload\n2 - Search\n3 - Download\n4 - Delete");
			int operation = obj.scan.nextInt();
			if (operation < 1 || operation > 4)
				continue;
			String tId = UUID.randomUUID().toString();
			String sender = obj.myService;
			obj.lClock++;
			Transaction t = null;
			if (operation == 1) {
				System.out.println("Enter the name of the file (example: 'yadi.txt'): ");
				String fileName = obj.scan.next();
				if (isValidFileExtension(fileName)) {
					// Create an instance of FileServant
					// Perform the file upload operation
					t = new Transaction(tId, pId, sender, operation, fileName, obj.lClock);
					obj.multicastTransaction(t);
					break;
				} else {
					System.out.println("Invalid file extension. Please enter a file with a recognized extension.");

				}
			} else if (operation == 3) {
				System.out.println("Enter the name of the file (example: 'yadi.txt'): ");
				String fileName = obj.scan.next();
				if (isValidFileExtension(fileName)) {
					t = new Transaction(tId, pId, sender, operation, fileName, obj.lClock);
					obj.download(fileName);
				} else {
					System.out.println("Invalid file extension. Please enter a file with a recognized extension.");
				}

			} else if (operation == 2) {
				System.out.println("Enter the name of the file (example: 'yadi.txt'): ");
				String fileName = obj.scan.next();
				if (isValidFileExtension(fileName)) {
					t = new Transaction(tId, pId, sender, operation, fileName, obj.lClock);
					obj.search(fileName);
				} else {
					System.out.println("Invalid file extension. Please enter a file with a recognized extension.");
				}
			} else if (operation == 4) {
				System.out.println("Enter the name of the file (example: 'yadi.txt'): ");
				String fileName = obj.scan.next();
				if (isValidFileExtension(fileName)) {
					// Create an instance of FileServant
					// Perform the file upload operation
					t = new Transaction(tId, pId, sender, operation, fileName, obj.lClock);
					obj.multicastTransaction(t);
					break;
				} else {
					System.out.println("Invalid file extension. Please enter a file with a recognized extension.");

				}
			}
		}
	}
}
