In Run packet there is the RunServer.java file that has the main function that start and stop the server.
When creating the server object we decide also on how many clients the server is going to manage at parallel.

In server packet there is the Server.java file that implements the parallel clients managment by thread pool with a queue by client with smallest board to solve first.