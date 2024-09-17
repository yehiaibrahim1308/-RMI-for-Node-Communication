
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.Random;

public class Node extends UnicastRemoteObject implements NodeI {
	private static final long serialVersionUID = 1L;
	final static int n = ipAddr.length;
	int lClock, myPort;
	String myIp, myService;
	PriorityQueue<Transaction> transactions;
	HashMap<String, Integer> transactionsAcks;// hfdl mestny rqm equal to the same number of acks as the umber of nodes
	Scanner scan;

	// object mn el node and intiallize the variables
	// idx is based on the node in the main if 0 hay3ml node be 0 ...
	protected Node(int idx) throws RemoteException {
		myIp = ipAddr[idx];
		myPort = ports[idx];
		myService = services[idx];
		lClock = 0;
		transactions = new PriorityQueue<Transaction>();
		transactionsAcks = new HashMap<String, Integer>();
		scan = new Scanner(System.in);
	}

	// step 1
	// based on the main this is the first multicast transaction function thatwill
	// be called from the main
	// the goal of the multi cast is to let the other nodes know what the node is
	// doing (performing=transaction )
	public void multicastTransaction(Transaction t) throws RemoteException, NotBoundException {
		boolean delay = true;
		// hamshi 3la kol el nodes el mawgoda
		for (int i = 0; i < n; i++) {
			// here i am testing the delay so that all the processes have the same value
			// before doing a transaction (to have a consistent value )
			if (delay && t.sender.equals("A")) { // Delay Sending Transaction from A to C
				if (i == 3) {
					Delayer delayer = new Delayer(ports[i], services[i], t);
					new Thread(delayer).start();
					continue;
				}
			}
			// till this is a testing to see will c wait before doing a new transaction so
			// that it get the same value
			Registry reg = LocateRegistry.getRegistry(ports[i]);
			// this is the remote method invocation
			NodeI e = (NodeI) reg.lookup(services[i]);
			e.performTransaction(t);
		}
		System.out.println("End Multicast Transaction:");
		displayTransactions();
		displayAcks();
	}

	// step 2
	// with every transaction the node make it adds the transaction to the priority
	// que and it will be ordered based on the compare function
	@Override
	public void performTransaction(Transaction t) throws RemoteException, NotBoundException {
		transactions.add(t);
		if (!t.sender.equalsIgnoreCase(myService)) { // If the sender is not me (since multicast so it may be me self
														// transacting ) and see the max between the local logical and
														// the clock from the timer and add 1 to the local clock
			lClock = Math.max(lClock, t.clock) + 1;
		}
		System.out.println("Perform Transaction:");
		if (t.operation == 2 || t.operation == 3) {
			ack(t);

		}

		displayTransactions();
		displayAcks();
		multicastAck(t);
	}

	// step 3
	// the multicast of the ack and it is used for to know if the transaction sam3
	// fe kol el nodes
	private void multicastAck(Transaction t) throws RemoteException, NotBoundException {
		boolean delay = true;
		for (int i = 0; i < n; i++) {
			if (delay && myService.equals("A")) { // Delay Sending Ack from A to C
				if (i == 2) {
					System.out.println("|||||||||||||||||||||||||||||||||||||||||");
					AckDelayer ackDelayer = new AckDelayer(ports[i], services[i], t);
					new Thread(ackDelayer).start();
					continue;
				}
			}
			Registry reg = LocateRegistry.getRegistry(ports[i]);
			NodeI e = (NodeI) reg.lookup(services[i]);
			e.ack(t);
		}
	}

	// fel download and search file hanady 3l download aw search wa hassign a random
	// number of port wa service for the leader i think ,

	// for every trans ther ewill be an ack
	@Override
	public void ack(Transaction t) throws RemoteException {
		if (transactionsAcks.containsKey(t.tId)) {
			transactionsAcks.put(t.tId, transactionsAcks.get(t.tId) + 1);
		} else
			transactionsAcks.put(t.tId, 1);
	}

	// hena bynfz el transactions hena el upload and delete
	// checks if there is a new transaction to perform
	public void fetchNewTransaction() throws RemoteException, NotBoundException {
		if (transactions.size() > 0 && transactionsAcks.containsKey(transactions.peek().tId)
				&& transactionsAcks.get(transactions.peek().tId) == n) {

			System.out.println("Fetch New Transaction:");
			displayTransactions();
			displayAcks();

			Transaction t = transactions.poll();
			transactionsAcks.remove(t.tId);

			System.out.println("After Fetch New Transaction:");
			displayTransactions();
			displayAcks();

			System.out.println("Performing Transaction: lol" + t);
			if (t.operation == 1) {
				Upload(t.filename);
			} else if (t.operation == 2) {
				search(t.filename);
			} else if (t.operation == 3) {
				download(t.filename);
			} else if (t.operation == 4) {
				delete(t.filename);
			}

		}
	}

	// here it displays the transactions np
	private void displayTransactions() {
		System.out.println("---------------Transactions--------------");
		for (Transaction t : transactions) {
			System.out.println("(" + (t.sender + ", " + t.clock
					+ ", " + t.tId + ")"));
		}
		System.out.println("=========================================");
	}

	// here it displays the acks mp
	private void displayAcks() {
		System.out.println("---------------Acks--------------");
		Set<String> keys = transactionsAcks.keySet();
		for (String k : keys) {
			System.out.println("(" + k + ", " + transactionsAcks.get(k) + ")");
		}
		System.out.println("=========================================");
	}

	// Inside the Node class
	// delete
	public void delete(String filename) throws RemoteException, NotBoundException {
		for (int i = 0; i < n; i++) {
			Registry reg = LocateRegistry.getRegistry(ports[i]);
			NodeI e = (NodeI) reg.lookup(services[i]);
			boolean res = e.deleteFile(filename);
			if (res)
				System.out.println("File deleted successfully.");
			else
				System.out.println("An error occurred. Try again later");
		}
	}

	@Override
	public boolean deleteFile(String filename) {
		System.out.println("Deleting File...");
		File f = new File(Main.PATH + filename);
		return f.delete();
	}

	// upload
	public void Upload(String filename) throws RemoteException, NotBoundException {

		for (int i = 0; i < n; i++) {
			Registry reg = LocateRegistry.getRegistry(ports[i]);
			NodeI e = (NodeI) reg.lookup(services[i]);
			FileSerializable fs = new FileSerializable();
			File localFile = new File(Main.PATH + filename);

			int fileSize = (int) localFile.length();
			byte[] buffer = new byte[fileSize];
			try {
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(localFile));
				in.read(buffer, 0, buffer.length);
				in.close();
			} catch (FileNotFoundException ee) {
				ee.printStackTrace();
			} catch (IOException ee) {
				ee.printStackTrace();
			}

			fs.setData(buffer);
			fs.setName(filename);
			fs.setLastModifiedDate(new Date(localFile.lastModified()));

			boolean res = e.uploadFile(fs);
			if (res)
				System.out.println("File uploaded successfully.");
			else
				System.out.println("An error occurred. Try again later");
		}
	}

	@Override
	public boolean uploadFile(FileSerializable fs) throws RemoteException {

		System.out.println("Uploading File...");
		File localFile = new File(Main.PATH + "local\\" + fs.getName());

		if (!localFile.exists()) {
			localFile.getParentFile().mkdir();
		}
		try {
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(localFile));
			out.write(fs.getData(), 0, fs.getData().length);
			out.flush();
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	// search
	public void search(String filename) throws RemoteException, NotBoundException {
		Random random = new Random();
		int randomNumber = random.nextInt(4);
		Registry reg = LocateRegistry.getRegistry(ports[randomNumber]);
		// this is the remote method invocation
		NodeI e = (NodeI) reg.lookup(services[randomNumber]);
		boolean res = e.searchFiles(filename);
		if (res)
			System.out.println("File found");
		else
			System.out.println("No matches found");

	}

	@Override
	public boolean searchFiles(String fileName) throws RemoteException {
		System.out.println("Searching for File...");
		File f = new File(Main.PATH + fileName);
		if (f.exists())
			return true;
		return false;
	}

	// logical clock handle them
	public void download(String filename) throws RemoteException, NotBoundException {
		Random random = new Random();
		int randomNumber = random.nextInt(4);
		Registry reg = LocateRegistry.getRegistry(ports[randomNumber]);
		// this is the remote method invocation
		NodeI e = (NodeI) reg.lookup(services[randomNumber]);
		FileSerializable fs = e.downloadFile(filename);
		File localFile = new File(Main.PATH + filename);

		if (!localFile.exists()) {
			localFile.getParentFile().mkdir();
		}
		try {
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(localFile));
			out.write(fs.getData(), 0, fs.getData().length);
			out.flush();
			out.close();

		} catch (FileNotFoundException ee) {
			ee.printStackTrace();
		} catch (IOException ee) {
			ee.printStackTrace();
		}
	}

	@Override
	public FileSerializable downloadFile(String fileName) throws RemoteException {
		System.out.println("Downloading File...");

		File f = new File(Main.PATH + "local\\" + fileName);
		FileSerializable fs = new FileSerializable();

		int fileSize = (int) f.length();
		byte[] buffer = new byte[fileSize];
		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(f))) {
			in.read(buffer, 0, buffer.length);
		} catch (IOException e) {
			e.printStackTrace();
		}

		fs.setData(buffer);
		fs.setName(fileName);
		fs.setLastModifiedDate(new Date(f.lastModified()));

		return fs;
	}

}
