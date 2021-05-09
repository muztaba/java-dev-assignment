Java Developer Assignment
=========================

This document outlines the specification of a TCP/IP socket server and client. Your task is to develop a simple TCP/IP
socket server based on specifications and also a client to send subsequent requests to the server.

TCP/IP Socket Server
--------------------
A socket is one endpoint of a two-way communication link between two programs running on the network. The socket is
bound to a port number so that the TCP layer can identify the application that data is destined to be sent to.

You can find an example of Java Socket Server [here](app/src/main/java/socket/server/App.java).

In server application, there is a class
called [`PrimeCalculationManager`](app/src/main/java/socket/server/manager/PrimeCalculationManager.java) which returns
the count of prime numbers less or equal to the given input n. There might be lots of other manager classes like this.

Our desired socket server should be able to handle subsequent requests from clients. So one specification of the socket
server would be, it must be able to handle incoming requests in parallel **for every socket connection**.

TCP/IP Socket Client
--------------------
A client can establish a socket connection to the server and send/receive messages. Here is an example of a TCP/IP
socket client.

Our desired client will send requests to the server with
the [`RequestObject`](app/src/main/java/socket/server/io/RequestObject.java).

For example, if I want to get the count of prime numbers less than or equal to 100, it should have request object like
following:

```
{
    "method":"findPrimes",
    "managerName":"PrimeCalculationManager",
    "args":{"n":"100"}
}
```

Tasks
-----
1. Implement the socket server which will receive incoming requests from clients. The incoming
requests object will have 3 parameters.

 - **managerName**: The server will instantiate an instance of the manager class from the managerName. Here we have a
  constraint. We have to instantiate the manager class using reflection.
 - **method**: It denotes which method we need to call for that specific manager. Here we will also have the same
  constraint. We will get the method using reflection and invoke later.
 - **args**: Denotes the arguments need to invoke the method.

 So the server will execute the requested method with provided arguments and will return the result to the client.

2. Implement the socket client which will send subsequent requests to server with appropriate request object. **Important:** we can use one socket connection to send multiple requests.  

Instruction
-----------

- **Server**: [App](app/src/main/java/socket/server/App.java)
- **Client**: [ClientRunner](app/src/main/java/socket/client/ClientRunner.java)

