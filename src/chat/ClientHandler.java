
package chat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;


class ClientHandler implements Runnable{
    
    public static ArrayList<ClientHandler> clients= new ArrayList<>();
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String clientUsername;
    public String getUsername(){
        return clientUsername;
    }
    ClientHandler(Socket socket){
        try{
            this.socket=socket;
           this.writer= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
           this.reader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
           this.clientUsername = reader.readLine();
           clients.add(this);
           broadCastMessage("SERVER: client " + clientUsername +" has entered the chat!");
           
        }catch(IOException e){
            closeEverything(socket, reader, writer);
           
        }
    }
    @Override
    public void run() {
        String clientMessages;
        while(socket.isConnected()){
            try{
               clientMessages = reader.readLine();
               broadCastMessage(clientMessages);
            }catch(IOException e){
                closeEverything(socket, reader, writer);
                break;
            }
        } 
    }

    private void broadCastMessage(String messageSend) {
        for(ClientHandler clientHandler: clients){
            try{
               if (!clientHandler.clientUsername.equals(clientUsername)){
                   clientHandler.writer.write(messageSend);
                   clientHandler.writer.newLine();
                   clientHandler.writer.flush();
               } 
            }catch(IOException e){
                closeEverything(socket, reader, writer);
            }
        }
    }

    private void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer) {
        removeClient();
        try{
            if(reader !=null ){
                reader.close();
            }
            if(socket !=null){
                socket.close();
            }
            if(writer !=null){
                writer.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void  removeClient(){
        clients.remove(this);
        broadCastMessage("SERVER: "+clientUsername +" has left the chat");
    }
}
