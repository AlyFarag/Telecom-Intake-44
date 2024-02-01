
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Client {
    public static void main(String[] args) {
        
        try 
        {

            //Create socket to connect to the serverSocket
            //can replace 127.0.0.1 by "localhost"   loopback Address and the same port at server
            Socket socket = new Socket("127.0.0.1", 5678);
            
            Scanner fromConsole = new Scanner(System.in);
            Scanner fromServer = new Scanner(socket.getInputStream());
            PrintWriter fromClient = new PrintWriter(socket.getOutputStream(), true);
            
            String inputMsg, outputMsg;
            while(true){
                System.out.print("Client : ");
                inputMsg = fromConsole.nextLine();

                fromClient.println(inputMsg);
                if(inputMsg.equals("*\"close\""))
                {
                    break;
                }

                outputMsg = fromServer.nextLine();
                System.out.println("Server : " + outputMsg);
                //System.out.println("You sent : " + outputMsg);
                if(outputMsg.equals("*\"close\""))
                {
                    break;
                }
                
            }
            socket.close();
            
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
