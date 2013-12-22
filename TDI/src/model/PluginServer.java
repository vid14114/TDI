package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ServiceLoader;

import javax.swing.JOptionPane;

public class PluginServer implements Runnable{

	 private ServerSocket server;
	 private Socket client=null;
     private ObjectOutputStream send;

     public PluginServer(){                        
             try {
                     //Created a new PluginServer instance, which is bound to the port 1234
                     server = new ServerSocket(2345);
                     
                    
             } catch (IOException e) {
                     JOptionPane.showMessageDialog(null, "Error! Couldn't initialize the server", "Error at Server initialization", JOptionPane.ERROR_MESSAGE);
             }
     }
     
     @Override
     public void run() {
             //Waits for a client connection, a new TUIO instance, which is started in a new Thread started here
             //This method also blocks this thread until a client connects to the server
             while(true){
            	 try{
                     while(client == null || client.isClosed()){
                             client = server.accept();
                             if(JOptionPane.showConfirmDialog(null, "Client: "+client.getInetAddress().getHostName()+" would like to connect to this PC, Allow?","Confirm",JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION ){
                                     client.close();
                             }
                     }
                     client.setKeepAlive(true);
                     ObjectInputStream read = new ObjectInputStream(client.getInputStream());
                     send = new ObjectOutputStream(client.getOutputStream());
            		 while(client.isConnected()){
            			 
            		 }
            		 
            	 }
            	 catch(IOException e){
                     e.printStackTrace();
                     System.out.println(e.getCause().getMessage());
                 }
                          
             }
        
     }
     
     public void sendMessage(/*parameter*/){
         try {
                 send.writeObject(/*parameter*/null);
         } catch (IOException e) {
                 e.printStackTrace();
         }
     }

     @Override
     protected void finalize() throws Throwable {
             send.close();
             server.close();
     }
     
     
}
