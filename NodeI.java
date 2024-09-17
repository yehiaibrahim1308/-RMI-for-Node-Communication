import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NodeI extends Remote {
	String[] ipAddr = { "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1" }; // ip address (local host)
	String[] services = { "A", "B", "C", "D" }; // three nodes or three devices
	Integer[] ports = { 2009, 3009, 4009, 5009 }; // the port number

	// every node has these operations a transaction and acknoledgement
	void performTransaction(Transaction t) throws RemoteException, NotBoundException;

	void ack(Transaction t) throws RemoteException;

	FileSerializable downloadFile(String fileName) throws RemoteException;

	boolean searchFiles(String fileName) throws RemoteException;

	boolean uploadFile(FileSerializable f) throws RemoteException;

	boolean deleteFile(String fileName) throws RemoteException;

}
