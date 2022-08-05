
package chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
//import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Premierhub_IT
 */
public class Client {
    private String username;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter bufferedWriter;
    //private HttpServletRequest request;
    
    public Client(Socket socket, String username){
        try{
            this.socket=socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username=username;
        }catch(IOException e){
            close(socket, bufferedWriter, reader);
        }
    }
    
    public void sendMessage(){
        Date d= new Date();
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username +": "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds()+"\n" +messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch(IOException e){
            close(socket, bufferedWriter, reader);
        }
    }
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String groupMessage;
                while(socket.isConnected()){
                    try{
                      groupMessage=reader.readLine();
                        System.out.println(groupMessage);
                    }catch(IOException e){
                        close(socket, bufferedWriter, reader);
                    }
                }
            }
        }).start();
    }
    private void close(Socket socket, BufferedWriter bufferedWriter, BufferedReader reader) {
        try{
            if(socket !=null){
                socket.close();
            }
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
            if(reader != null){
                reader.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static void main(String []args) throws IOException{
       // InetAddress addr;
      
       try{
            Scanner sc= new Scanner(System.in);
            System.out.println("Enter Your user name: ");
            String username = sc.nextLine();
            Socket socket = new Socket("localhost",8083);
            Client clients= new Client(socket, username);
            clients.listenForMessage();        
            clients.sendMessage();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
}
