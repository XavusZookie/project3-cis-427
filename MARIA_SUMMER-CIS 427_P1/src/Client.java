import java.net.*;
import java.io.*;

//Client class, created to connect with the server class, can't run without it

public class Client {

    public static void main(String args[]) {
        try {
            Socket s = new Socket("localhost", 9034); //connecting to port
            DataInputStream din = new DataInputStream(s.getInputStream()); //creating input variable
            DataOutputStream dout = new DataOutputStream(s.getOutputStream()); //creating output variable
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            //while loop to check connection
            String command = "", response = "";
            while (true) {
                command = br.readLine();
                dout.writeUTF(command);
                dout.flush();
                response = din.readUTF();
                if (response.equalsIgnoreCase("200 ok")) {
                    dout.close();
                    s.close();
                    System.exit(0);
                }
                System.out.println(response);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }
    }
}

