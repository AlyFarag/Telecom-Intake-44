import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {
    public static void main(String[] args) {
        try {
            
            //Create the server socket 
            // UDP server socket listening on port 9876
            DatagramSocket socket = new DatagramSocket(9876); // UDP server socket listening on port 9876

            while (true) 
            {
                //Receive Data 
                byte[] receiveData = new byte[1024];

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                System.out.println("Waiting for a packet : ");
                socket.receive(receivePacket);

                 //Read thepacket
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Client: " + message);

                // Get information about the client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                String responseMessage = "Server: Message received - " + message;
                byte[] sendData = responseMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                socket.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


