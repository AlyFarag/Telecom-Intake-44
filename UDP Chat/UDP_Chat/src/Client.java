import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            //create the client socket
            DatagramSocket socket = new DatagramSocket();

            Scanner scanner = new Scanner(System.in);

            while (true) 
            {
                System.out.print("Enter message to send to server (type 'exit' to quit): ");
                String message = scanner.nextLine();

                if (message.equals("exit")) {
                    break;
                }

                byte[] sendData = message.getBytes();
                
                //Create packet to send
                InetAddress serverAddress = InetAddress.getByName("localhost");
                int serverPort = 9876;

                // Using IPv4 address for localhost (127.0.0.1)
//                byte[] ipv4 = { 127, 0, 0, 1 };
//                InetAddress serverAddress_= InetAddress.getByAddress(ipv4);
                
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                //Sending packet to server
                socket.send(sendPacket);

                //Receive data from the server 
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                String responseMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println(responseMessage);
            }

            socket.close();
            scanner.close();
        }
        catch (IOException e) 
        {
        }
    }
}
