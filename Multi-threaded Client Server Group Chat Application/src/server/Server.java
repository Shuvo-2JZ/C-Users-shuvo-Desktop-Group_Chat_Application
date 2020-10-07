
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Anika Salsabil
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    static ArrayList<Socket> Socket_arr;
    static String [] name_arr;
    
    public static void main(String[] args) throws IOException {
        System.out.println("Server Started.");

        ServerSocket welcomeSocket = new ServerSocket(6789);
        int i = 0;
        Socket_arr = new ArrayList<Socket>();
         name_arr=new String[1024];
        while (true) {

            Socket connectionSocket = null;

            connectionSocket = welcomeSocket.accept();
            String name = new DataInputStream(connectionSocket.getInputStream()).readLine();
            name_arr[i]=name;
            for (Socket sc : Socket_arr) {
                if (sc.isConnected()) {
                    new DataOutputStream(sc.getOutputStream()).writeBytes("##join_msg" + name + " has join the group chat.\n");
                }
            }

            Socket_arr.add(connectionSocket);

            System.out.println("A new client is connected : " + connectionSocket);
            System.out.println("Total number of Client : " + Socket_arr.size());

            DataInputStream dis = new DataInputStream(connectionSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(connectionSocket.getOutputStream());

            Thread t = new ClientHandler(dis, dos, connectionSocket, i++);

            t.start();

        }

    }

}
