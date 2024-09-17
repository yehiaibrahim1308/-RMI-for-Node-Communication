
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// a class where it delays the message  and then it performs the transaction (sending the transaction) 
public class Delayer implements Runnable {

	private int port;
	private String serviceName;
	private Transaction transaction;

	public Delayer(int port, String serviceName, Transaction transaction) {
		this.port = port;
		this.serviceName = serviceName;
		this.transaction = transaction;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(5000);
			Registry reg = LocateRegistry.getRegistry(port);
			NodeI e = (NodeI) reg.lookup(serviceName);
			e.performTransaction(transaction);
		} catch (RemoteException | NotBoundException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}

}
