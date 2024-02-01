
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alyaf
 */
public class Server {
    
    public static void main(String []args)
    {
        try 
        {
            //Server socket to establish connection of the server
            ServerSocket serverSocket = new ServerSocket(5678);

            //Socket listen to the serverSocket and accept the data
            Socket socket = serverSocket.accept();
            
            //Read data from console
            Scanner fromConsole = new Scanner(System.in);

            //read data from the socket
            Scanner fromClient = new Scanner(socket.getInputStream());
            //Send data sent by client to out throw socket
            PrintWriter fromServer = new PrintWriter(socket.getOutputStream());

           // BufferedReader reader = new BufferedReader(new InputStramReader(socket.getInputStream()));
            // DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            
            String inputMsg, outputMsg;
            while(true)
            {
                
                inputMsg = fromClient.nextLine();
                System.out.println("Client : " + inputMsg);
                if(inputMsg.equals("*\"close\""))
                {
                    break;
                }

                //Input data from server
                System.out.print("Server : ");
                inputMsg = fromConsole.nextLine();
                fromServer.println(inputMsg);
                
                //convert the client message to uppercase 
                //outputMsg = inputMsg.toUpperCase();
                //fromServer.println("Server: " + outputMsg);
                fromServer.flush();
                if(inputMsg.equals("*\"close\""))
                {
                    break;
                }
                
                
            }
            socket.close();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "The server is out", ex);
        }
    }
}
