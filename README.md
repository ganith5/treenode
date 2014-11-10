Project Description:
--------------------

Tree Factory is a web application project developed as part of a self learning exercise with the 
following assumptive conditions.

A web UI that allows multiple users to access the application and view the current state of the tree.
Allows each user to create/delete nodes in the tree concurrently.
Provide live update to all connected users of any changes to the tree
Tree view allows for 2 levels i.e. Parent and Child.
Factory nodes are created using a function that generates a random numbered factory. 
Each parent factory has the following properties when created:
	The name of the factory
	The random number pool with range from 1 to 1000
The child nodes for each factory is created by right clicking the factory node.
	A user input pop-up is displayed expecting user to provide number of children to be created.
The updates to tree are queued as events and are polled from the server every 5 sec.
The "Refresh" button can be used to re-query for the current state of the tree with updates from all users.

Example Use Case:
------------------

User1 connects to the supplied URL and creates the first factory. Then User2 connects. 
User1 see’s John's factory. User2 then right clicks the factory and chooses to generate a random numbers 
from the pool. 
The child nodes are generated with both User1 and User2 seeing the result.
The tree update events are displayed to both the users.

Solution Overview:
-------------------

The solution is deployed on Amazon ec2 instance and is running on a Jetty server.

It can be accessed through

	http://ec2-54-187-123-139.us-west-2.compute.amazonaws.com:8080/treeapp/

	JQuery and JSTree plug-in is used to generate the tree structure on the view.
	
	MongoDB is used as database and is running on the ec2 instance.
	
	The application is running on a Jetty server which is setup as a maven plugin.
	
	Build tool - Maven
	
	To start the server locally :
		
		Compile and build artifact - mvn clean package from project base folder
		Run Jetty server - mvn jetty:run 
		The application can be accessed as http://localhost:8080/treeapp
	
	Spring MVC, Spring Data, AOP , Asynchronous task execution are some of the concepts used in the project

	
	 

