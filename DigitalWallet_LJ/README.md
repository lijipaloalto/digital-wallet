# Table of Contents
1. [Summary]
2. [Three Features] 
3. [Implementation Details]
4. [Performance and Scability]
5. [How to run the program]
6. [Testing] 

##1. Summary
This java program, 'Digital Wallet', is designed to process the payment transactions between PayMo users and to detect fraudulent payment requests from untrusted users based on several implemented features. 

##2. Three Features
###Feature 1 - 1st degree friends: 
Payment between two users who have made transaction before is regarded as "trusted", otherwise, "unverified".

###Feature 2 - 2nd degree friends: 
Payment between two users who have mutual friends (transaction made between them), is regarded as "trusted", otherwise, "unverified". 

###Feature 3 - 4th degree friends: 
"Unverified" warning is given only when they are outside the 4th degree friends network, otherwise, the payment is "trusted". 

##3. Implementation Details
###Data Structure 
Graph representation is used to solve the program. Every user is a vertex and edge between two users is a "trusted" payment transaction. All the users and payments within PayMo comprise a big graph. In this program, all payment transaction is not weighted. To check validity of the payment transaction between two users, the shortest distance between these two vertex is calculated and used for the evaluation according to three different features. Once a payment transaction between two users is examined, the graph will be updated correspondingly. 
Therefore, a directed unweighted graph is designed to model the payment network, though undirected graph is implemented for this problem; Vertex is designed to model user and edge is designed to model payment transaction.     

###Algorithm
To solve the single source shortest path problem for unweighted graph, the current fastest algorithm is Breadth first search (BFS) with O(V+E) complexity. If the graph is weighted, Dijkstra algorithm with Fibonacci heap may be a better choice with O(VlogV+E) complexity.   
The pseudocode for BFS algorithm can be found online (https://en.wikipedia.org/wiki/Breadth-first_search), and it is used to solve this problem with slight modification to best represent the payment transaction process across the network. 

###Feature determination 
Feather1 determination: every vertex in the graph has a Hashmap (adjMatrix) storing all the adjacent vertex, which are also the 1st degree friends of that vertex. By checking whether the receiver is in the sender's adjMatrix, the transaction can be determined as trusted or unverified. If feature1 is trusted, feature2 and feature3 are trusted as well.
Feature2 determination: the program searches through the 2nd degree neighbors of the sender, if the receiver is inside the 2nd degree neighbors, the transaction is trusted, otherwise, unverified. 
Feature3 determination: the program will find all the 2nd degree neighbors of the sender and the 2nd degree neighbors of the receiver as well; if there is any mutual vertex in these two sets of neighbors, the transaction can be trusted, otherwise, the receiver is not the 4th degree neighbor of the sender, therefore the transaction is unverified.  

###Data cleaning and importing 
Some entries in the 'batch_payment' and 'stream_payment' files are not in the format of 
* `time`: Timestamp for the payment 
* `id1`: ID of the user making the payment 
* `id2`: ID of the user receiving the payment 
* `amount`: Amount of the payment 
* `message`: Any message the payer wants to associate with the transaction
therefore, these entries are considered as invalid records. A method called 'isValidEntry' in 'impl.antifraud' is used to evaluate each entry, whether any field of 'id1', 'id2' and 'amount' is missing or non-numeric - if so, the record is considered as invalid and will be discarded.  
There are 52 invalid records and 3938360 valid records in 'batch_payment', and 33 invalid records and 3000000 valid records in 'stream_payment'. 

###Graph construction and updating 
Inputs in 'batch_payment' are used to construct the initial graph of payment transaction network via the method 'processPaymentRecords' in 'impl.antifraud'. The records in 'stream_payment' are also read line by line -  these records are not only used for evaluation of three features, but also may potentially alter the existing graph. The method 'processOnePaymentRecord' is used to update the graph when necessary. 

##4. Performance and scability 
The program is run on a single i7 core without parallel computation. 
As the program is designed to solve real-time payment transactions, the computation process should be as fast as possible. For feature1 determination, it is about 1millisecond per payment transaction. For feature2 determination, it is around 1 to 50millisecond per payment transaction. For feature3 determination, by doing Breadth-first search to all the neighbors within two degrees of sender and receiver, it is about the same runtime as that for feature2 determination. 
With regard to scability, the computation time is linearly increasing with the growth of size (vertex and edge) of the graph due to BFS O(V+E) time complexity. 

##5. How to run the program
To run the program at project root directory, java file needs to be compiled first with the following command: 
	javac -cp src/ src/impl/antifraud.java
	
then the compiled java class file can be run with the following command:
	java -cp src/ impl/antifraud paymo_input/batch_payment.txt paymo_input/stream_payment.txt paymo_output/output1.txt paymo_output/output2.txt paymo_output/output3.txt
	
Both commands are included in the shell script named 'run.sh'.   

##6. Testing
###Testing Cases 
Under the 'insight_testsuite/tests' folder, there are two test cases: one is named 'test-1-paymo-trans', which contains only one transaction in the 'batch_payment' and 'stream_payment' files; the other one is named 'test-4-paymo-trans', which contains 5 transactions in 'batch_payment' as: 1 - 2, 2 - 3, 3 - 4, 4 - 5, 5 - 6, and 4 transactions in 'stream_payment' as: 1 - 2, 1 - 3, 1 - 6, 1 - 7. 

The first text case with only one transaction is used to test whether the program runs correctly. 
The second text case is slightly more complicated to test these scenarios: 
	[1] first degree neighbors: 1-2 where 2 is the 1st degree neighbors of 1, so the result will be 'trusted' in all three output files. 
	[2] second degree neighbors: 1-3 where 3 is the 2nd degree neighbors of 1, so the result will be 'unverified' in output1, and 'trusted' in output2 and output3. 
	[3] out of fourth degree neighbors: 1-6 where 6 is the 5th degree neighbor of 1 at the beginning, so the result will be 'unverified' in output1 and output2. 
	[4] changes of degrees when graph updating: after 1-3 transaction is verified in output3, 6 becomes the 4th degree neighbors of 1, so 1-6 will be 'trusted' in output3.  
	[5] new customers: 1-7 where 7 is a new customer to the network, thus the results will be 'unverified' in all three outputs. 

###Testing Code Directory 
The code directory is structured as required, both directory and output format have been tested with the shell script as shown below:
	insight_testsuite$ ./run_tests.sh
	
The program runs successfully and the results are displayed below: 
	
	[PASS]: test-1-paymo-trans (output1.txt)
	[PASS]: test-1-paymo-trans (output2.txt)
	[PASS]: test-1-paymo-trans (output3.txt)
	
	[PASS]: test-4-paymo-trans (output1.txt)
	[PASS]: test-4-paymo-trans (output2.txt)
	[PASS]: test-4-paymo-trans (output3.txt)
	
	
	
