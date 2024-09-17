# Distributed File System Using RMI for Node Communication

This project implements a distributed file system in Java utilizing Remote Method Invocation (RMI) for inter-node communication. The system supports basic file operations—upload, download, search, and delete—while ensuring consistency through a logical clock mechanism and ordering transactions using a priority queue. 


# Components
1. Node Class: Represents each node in the system, implementing the NodeI interface for remote method calls. It manages transactions, acknowledgments, and user interactions.
2. Multicast Transaction: Initiates and multicasts transactions across nodes, ensuring synchronized logical clock values.
3. Perform Transaction: Processes incoming transactions, updates local state, and multicasts acknowledgments.
4. Multicast Ack: Sends transaction acknowledgments to all nodes, maintaining logical clock synchronization.
5. Fetch New Transaction: Executes transactions with sufficient acknowledgments, performing file operations as specified.



# Code Overview

- Main.java: Entry point for initializing and running nodes, managing transactions, and user inputs.
- Node.java: Core functionality including server/client setup, transaction handling, and file operations.
- NodeI Interface: Defines remote methods for transaction handling and file operations.
- Transaction Class: Represents a transaction with fields for identification, operation type, and logical clock time.
- FileSerializable Class: Facilitates serialization and deserialization of files for network transmission.
- Delayer Service: Delays transactions to simulate network latency or specific timing requirements.
- AckDelayer Class: Simulates network delay for acknowledgment messages to model real-world scenarios.


# screen shots


![image](https://github.com/user-attachments/assets/7f65054a-1d4e-4b17-945f-cc7ca93f5512)

![image](https://github.com/user-attachments/assets/0c8e990a-7f9f-4200-b44a-da3dcc3ccef5)

![image](https://github.com/user-attachments/assets/8cb35038-eba4-499b-aadd-49064eb13178)

![image](https://github.com/user-attachments/assets/6e0479e4-b795-4596-a08f-f788fe334d9b)

![image](https://github.com/user-attachments/assets/1e5241af-1b1a-4b28-8ed2-060e507f4018)

![image](https://github.com/user-attachments/assets/405be658-65da-4a90-a835-a0e94b2ecf07)


![image](https://github.com/user-attachments/assets/c6fda818-ed1f-4b9a-8343-8baae597ba09)

