
package chat;

import java.io.IOException;
import java.lang.reflect.Array;
//import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Premierhub_IT
 */
public class Server {
    private final  ServerSocket serverSocket;
    private Socket socket;
    public String message ="";
    //private DatagramSocket datasocket;
    public Server (ServerSocket serverSocket){
        this.serverSocket =serverSocket;
    }
    public void startServer(final int port){
        
        try{
            if(!serverSocket.isClosed()){
               message="Server stated on port " + port;
            }
            int count = 0;
            List<String> cl= new ArrayList<>();
            
            while(!serverSocket.isClosed()){
               System.out.println("Waiting for connections......");
               socket=serverSocket.accept();
               count++;
               System.out.println("A new client has been connected!" + serverSocket.getLocalSocketAddress());
               ClientHandler client = new ClientHandler(socket);
               Thread thread = new Thread(client);
               cl.add(client.getUsername());
                numberOfUser(cl);
               //Iterator<ClientHandler> i =cl1.iterator();
               thread.start();
           } 
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public void closeServer(){
        try{
            if(serverSocket !=null){
                serverSocket.close();
                socket.close();
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
     public static void main(String [] args){
         try{
         ServerSocket ss = new ServerSocket(8083);
         Server s = new Server(ss);
         s.startServer(8083);
         
        //System.out.print(ss.getLocalPort()+" "+ss.getLocalSocketAddress()+" "+ss.getInetAddress());

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
     }
     
     private void numberOfUser(List<String> arr){
         
         new Thread(new Runnable() {
             int count;
             @Override
             public void run() {
                for(String cliens: arr){
                   count=(int)arr.size();
                   System.out.println(cliens);
               }
                System.out.println("Connected clients: " +count);
             }
             
         }).start();
     }
}
