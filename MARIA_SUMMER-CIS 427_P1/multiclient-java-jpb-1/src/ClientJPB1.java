
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.net.*;
import java.io.*;
import java.util.*;

public class ClientJPB1 {
    
    private static final int SERVER_PORT = 4999;
    private static String user;
    
public static int dummy = 1;

    public static void main(String[] args) {
        
        DataOutputStream toServer;
        DataInputStream fromServer;
        Scanner input = 
                new Scanner(System.in);
        String message;
        
        //attempt to connect to the server
        try {
            
            Socket s = new Socket("localhost", 4999); //connecting to port
            DataInputStream din = new DataInputStream(s.getInputStream()); //creating input variable
            DataOutputStream dout = new DataOutputStream(s.getOutputStream()); //creating output variable
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            //while loop to check connection
            String command = "", response = "";
                            response = din.readUTF();
                System.out.println(response);
                
               

            while (true) {
                command = br.readLine();
                dout.writeUTF(command);
                dout.flush();
                
                if(user == null)//get the username
                {                          
                response = din.readUTF();
                user = response;
                }
                
                response = din.readUTF();
                
                 
                
                if (command.equalsIgnoreCase("shutdown") || command.equalsIgnoreCase("logout")) {
                System.out.println("200 ok");

                    dout.close();//close the client
                    s.close();
                    System.exit(0);
                  
                }
                System.out.println(response);
                
                //String[] thedata = MultiServerJPB1.talkgetter(user);
                
                 if (dummy ==1)//skip the first message check for loggin in 
                    {
                        dummy = 0;
                        continue;
                    }
                response = din.readUTF();//get the input for the messages, will either be a message or no messages
                System.out.println(response);
                  
                
        
      
        
            }
             
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }//end try-catch
        
        
    }//end main
}
