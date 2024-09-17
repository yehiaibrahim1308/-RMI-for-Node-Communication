
import java.io.Serializable;

//upload==download 
//search
public class Transaction implements Comparable<Transaction>, Serializable {
	private static final long serialVersionUID = 1L;// i will use this as an id to identify the transaction

	public static final int download = 3;
	public static final int upload = 1;
	public static final int Search = 2;
	public static final int delete = 4;
	FileSerializable file;
	String tId, sender, filename;
	int pId, clock, amount, operation;
	double interestRate;

	// 4 constructors hena
	// here i intialize these based on the two operations if it is deposit operation
	// (the constructor for deposit)or intrest rate
	public Transaction(String tId, int pId, String sender, int operation, String filename, int clock) {
		this.tId = tId;
		this.pId = pId;
		this.sender = sender;
		this.filename = filename;
		this.clock = clock;
		this.operation = operation;
	}

	// here we print the details of the transaction

	// Total Order using logical clocks i order them based on the logic time and if
	// there is a tie break i order based on the process id
	@Override
	public int compareTo(Transaction o) {
		// Tie Breaker
		if (this.clock == o.clock)
			return this.pId - o.pId;
		return this.clock - o.clock;
	}

}
