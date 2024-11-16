# Distributed File System Using RMI for Node Communication
 
This project implements a distributed file system in Java utilizing Remote Method Invocation (RMI) for inter-node communication. The system supports basic file operations—upload, download, search, and delete—while ensuring consistency through a logical clock mechanism and ordering transactions using a priority queue. 
 
        
# Components
1. Node Class: Represents each node in the system, implementing the NodeI interface for remote method calls. It manages transactions, acknowledgments, and user interactions.
2. Multicast Transaction: Initiates and multicasts transactions across nodes, ensuring synchronized logical clock values.
3. Perform Transaction: Processes incoming transactions, updates local state, and multicasts acknowledgments.
4. Multicast Ack: Sends transaction acknowledgments to all nodes, maintaining logical clock synchronization.
5. Fetch New Transaction: Executes transactions with sufficient acknowledgments, performing file operations as specified.



# Code Overview

## Main.java:

The Main class serves as the entry point for initializing a distributed file system node within the project. It is responsible for setting up both the RMI (Remote Method Invocation) server and client, which facilitate communication between nodes in the distributed system. This class supports a range of file operations, including upload, search, download, and delete, based on user input. A Timer is utilized to periodically fetch and process new transactions. The system ensures that only files with valid extensions are handled, and it manages transaction creation and multicasting to maintain synchronization across all nodes in the network.
  
## Node.java:

The Node class is pivotal in implementing distributed file operations using Java’s RMI framework. It supports core functionalities such as uploading, downloading, searching, and deleting files while managing transactions and acknowledgments between nodes. The class leverages a logical clock mechanism to ensure consistent ordering of transactions and performs multicast operations to communicate transactions across the network. Transactions are maintained in a priority queue, processed based on acknowledgment counts, and managed for file serialization and deserialization. This ensures accurate file data transfer and consistency across distributed nodes.

  
## NodeI Interface:

The NodeI interface defines the remote methods available for interaction with nodes in the distributed system, extending Java's Remote interface. It specifies attributes such as IP addresses, service names, and port numbers essential for network communication. The interface includes methods for various operations: performTransaction, ack, downloadFile, searchFiles, uploadFile, and deleteFile. Each method is designed to throw RemoteException, handling potential network or communication issues. These methods enable nodes to perform transactions, acknowledge them, and manage files remotely, ensuring comprehensive management of distributed file operations.
  
## Transaction Class:

The Transaction class represents a transaction within the distributed file system and implements the Comparable interface for sorting transactions. It encompasses constants for different operations (upload, download, search, delete) and includes fields for transaction details such as ID, sender, filename, logical clock time, and operation type. The class features constructors for initializing these fields based on transaction specifics. Transactions are ordered primarily by logical clock values and secondarily by process ID to ensure a total ordering. The class's serializability facilitates the transmission of transaction data over the network.

  
## FileSerializable Class:

The FileSerializable class is designed for the serialization of file data in Java applications. Implementing the Serializable interface, it allows instances to be converted into a byte stream suitable for storage or transmission. The class contains attributes for the file’s name, path, byte data, and last modified date, along with corresponding getter and setter methods. It offers two constructors: one for initializing all attributes and a default constructor for general use. This class ensures that file information is easily serialized and deserialized while preserving essential file metadata.



  
## Delayer Service: 

The Delayer class, implementing the Runnable interface, manages the delayed execution of transactions within the distributed system. Upon instantiation, it is initialized with a port number, service name, and a Transaction object. In its run() method, the class introduces a 5-second delay to simulate network latency before locating the RMI registry and retrieving the remote service using the specified service name. It then performs the transaction by invoking the performTransaction method on the remote node. The class captures and prints any exceptions, such as remote or binding errors and interruptions, for debugging purposes.





## AckDelayer Class: 

The AckDelayer class is designed to simulate network latency by delaying the sending of acknowledgment (ACK) messages in a distributed system. Implementing the Runnable interface, it operates as a separate thread to introduce a delay before processing. Upon execution, it waits for 7 seconds, then locates the RMI registry and retrieves the remote service using the provided port and service name. After finding the service, it sends an acknowledgment for the specified transaction. This class models real-world network delays that may impact the timing of acknowledgment messages.


# Screenshots
These screenshots demonstrate key operations in the distributed file system, including file transactions, acknowledgment handling, and search functionality, all coordinated through RMI communication.



![image](https://github.com/user-attachments/assets/7f65054a-1d4e-4b17-945f-cc7ca93f5512)
- This image shows the initialization of a node in the distributed file system. The node starts up by binding to the RMI registry, registering its service name and port number for communication with other nodes.




![image](https://github.com/user-attachments/assets/0c8e990a-7f9f-4200-b44a-da3dcc3ccef5)

- This screenshot illustrates a node sending a multicast transaction. The transaction contains details about the operation (e.g., file upload or download), and is sent to all other nodes in the system to ensure synchronized processing.
  
![image](https://github.com/user-attachments/assets/8cb35038-eba4-499b-aadd-49064eb13178)

- In this image, we see the reception and queuing of transactions on a node. Transactions are added to the priority queue, and the node updates its logical clock, ensuring consistency across all nodes.
![image](https://github.com/user-attachments/assets/6e0479e4-b795-4596-a08f-f788fe334d9b)

- This shows the acknowledgment of a transaction. Once a node processes a transaction, it sends an acknowledgment to the originating node and other nodes in the system, allowing the transaction to be confirmed.


![image](https://github.com/user-attachments/assets/1e5241af-1b1a-4b28-8ed2-060e507f4018)

- The screenshot captures the execution of a file download operation. A client requests a file from the distributed system, and the node processes the request, returning the file to the client.

![image](https://github.com/user-attachments/assets/405be658-65da-4a90-a835-a0e94b2ecf07)


- Here, a search operation is performed. The user searches for a specific file, and the system responds with whether the file exists and its location within the distributed file system.
  
![image](https://github.com/user-attachments/assets/c6fda818-ed1f-4b9a-8343-8baae597ba09)

- This final screenshot shows a delayed acknowledgment, simulating network latency. The AckDelayer class introduces a delay before an acknowledgment is sent, mimicking real-world network conditions.
