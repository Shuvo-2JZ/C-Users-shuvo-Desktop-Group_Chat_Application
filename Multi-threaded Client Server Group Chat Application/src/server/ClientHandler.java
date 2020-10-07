package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static server.Server.*;

/**
 *
 * @author Anika Salsabil
 */
public class ClientHandler extends Thread {

    final DataInputStream dis;
    final DataOutputStream dos;
    DataOutputStream dos1;
    int no_client;
    int clientId;
    Socket sc;
   
    //   ArrayList<Socket> Socketarr;

    ClientHandler(DataInputStream dis_main, DataOutputStream dos_main, Socket sc, int id) {

        dis = dis_main;
        dos = dos_main;
        this.clientId = id;
        this.sc = sc;
        this.no_client = Socket_arr.size();
        
    }

    @Override
    public void run() {
          
        BufferedReader br = new BufferedReader(new InputStreamReader(dis));
      String sentenceFromClient="";
        while (true) {

            try {
                 sentenceFromClient = br.readLine();
                 
                 
                if (sentenceFromClient.startsWith("##left_msg")||dis==null) {

                    Socket_arr.remove(sc);
                    System.out.println("One client left.");
                    System.out.println("Total number of Clients : " + Socket_arr.size());

                }

                for (Socket sc : Socket_arr) {

                    if (!sc.isClosed()) {
                        dos1 = new DataOutputStream(sc.getOutputStream());

                        {

                            dos1.writeBytes(sentenceFromClient + '\n');
                        }

                    }

                }
                if (sentenceFromClient.startsWith("##left_msg")) {
                    this.sc.close();
                    this.stop();

                }

            } catch (IOException ex) {
                int pos=Socket_arr.indexOf(sc);
                 Socket_arr.remove(sc);
                    System.out.println("One client left.");
                    System.out.println("Total number of Clients : " + Socket_arr.size());  
                    
                  for (Socket sc : Socket_arr) {

                    
                    {
                        try {
                            
                            dos1 = new DataOutputStream(sc.getOutputStream());
                            
                        } catch (IOException ex1) {
                            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
                        }

                        {

                            try {
                                dos1.writeBytes(name_arr[pos] + " has left the group chat.\n");
                            } catch (IOException ex1) {
                                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
                            }
                        }

                    }

                }  
                  this.stop();
               // Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
