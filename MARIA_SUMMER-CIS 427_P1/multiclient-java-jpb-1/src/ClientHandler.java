
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.*;
import java.io.*;
import java.util.*;



//Runnable class allows us to create a task
//to be run on a thread
public class ClientHandler implements Runnable {
    private Socket socket;  //connected socket
    private ServerSocket serverSocket;  //server's socket
    private int clientNumber;
    
   
    public static String who = " ";
        public static String mess;
                public static String from;
                public static String whoisloggedin = "";
    
    //create an instance
    public ClientHandler(int clientNumber, Socket socket, ServerSocket serverSocket) {
        this.socket = socket;
        this.serverSocket = serverSocket;
        this.clientNumber = clientNumber;
    }//end ctor
    
    
    private static ArrayList<String> logins = new ArrayList<>();
    boolean loggedIn = false;
    String user = "";
    BufferedWriter bw = null;
    //run() method is required by all
    //Runnable implementers
    @Override
    public void run() {
        
        readData();
        
        
        
        //run the thread in here
        try {
            ServerSocket ss = this.serverSocket;
            Socket s = this.socket;
            DataInputStream din = new DataInputStream(s.getInputStream()); //input variable
            DataOutputStream dout = new DataOutputStream(s.getOutputStream()); //output variable
            String str = "";
            dout.writeUTF("Please type 'LOGIN' followed by your username, space, and then your password. Then hit enter."); //message to client before commands code for login
            
            //while loop, checks if "LOGIN" is correctly done (based on usernames/password in "logins.txt" folder)
            while (true) {
                
                
                
                str = din.readUTF();
                String l = str.split(" ")[0];
                if (loggedIn == false && l.equalsIgnoreCase("login"))//str.startsWith("LOGIN"))
                {
                    
                    String u = str.split(" ")[1];
                    String p = str.split(" ")[2];
                    
                    if (logins.contains(u + " " + p)) {
                        loggedIn = true;
                        user = u;
                        dout.writeUTF(user);
                        dout.writeUTF("SUCCESS\nPlease type 'SOLVE' followed by -c or -r (for circle or rectangle), and then the number and hit enter!"); //output message after sucessful login, instructing user on next steps
                        dout.flush();
                        whoisloggedin = whoisloggedin + " " + user;
                    } else {
                        dout.writeUTF(" ");
                        dout.writeUTF("FAILURE: Please provide correct username and password. Try again"); //if login attempt with an account not in logins.txt
                        dout.flush();
                    }
                    continue;
                }
                
                if(who.contains(user))//if the users name is in the address for message return to the client the message
                {
                dout.writeUTF("message from :" + from + "\n" + mess);
                        dout.flush();
                        String msg[] = who.split(" ");
        String new_str = "";
 
        // Iterating the string using for each loop
        for (String words : msg) {// take your name out of the list sine you viewed it already
 
            // If desired word is found
            if (!words.equals(user)) {
 
                // Concat the word not equal to the given
                // word
                new_str += words + " ";
            }
        }
        who = new_str;
                }
                else // if youre not in the address list there are no messages for you
                {
                    dout.writeUTF("no messages\n");
                        dout.flush();
                }
                // command, system closes if "SHUTDOWN" is entered
                if (loggedIn) {
                    if (str.equalsIgnoreCase("SHUTDOWN")) {
                        dout.writeUTF("200 ok");
                        
                        dout.flush();
                        String new_str = "";
 String msg[] = whoisloggedin.split(" ");//if youre leaving the server you need to take your name off the list of who is logged in 
        // Iterating the string using for each loop
        for (String words : msg) {
 
            // If desired word is found
            if (!words.equals(user)) {
 
                // Concat the word not equal to the given
                // word
                new_str += words + " ";
            }
        }
        whoisloggedin = new_str;
                        din.close();
                        s.close();
                        ss.close();
                        System.exit(0);
                    }
                    
                    //LOGOUT command
                    else if (str.equalsIgnoreCase("LOGOUT")) {
                        dout.writeUTF("200 ok");
                        dout.flush();
                        
                        String new_str = "";
 String msg[] = whoisloggedin.split(" ");//taking name off
        // Iterating the string using for each loop
        for (String words : msg) {
 
            // If desired word is found
            if (!words.equals(user)) {
 
                // Concat the word not equal to the given
                // word
                new_str += words + " ";
            }
        }
        whoisloggedin = new_str;
                                                
                    }
                    //SOLVE command
                    else if (l.equalsIgnoreCase("solve")) {//str.startsWith("SOLVE")) {
                        bw = new BufferedWriter(new FileWriter(user + "_solutions.txt", true)); //creats a text file with username where every input is appended
                        String[] shape = str.split(" ");
                        //code for -c, circle, includes circumference + area formula and output messages
                        if (shape[1].equals("-c")) {  
                            if (shape.length == 3) {
                                int radius = Integer.parseInt(shape[2]);
                                double circum = 2 * Math.PI * radius;
                                double area = Math.PI * radius * radius;
                               
                                dout.writeUTF("Circle???s circumference is " + String.format("%.2f", circum)
                                        + " and area is " + String.format("%.2f", area));
                              //  dout.flush();
                                bw.write("\nradius " + radius + ": Circle???s circumference is " + String.format("%.2f", circum)
                                        + " and area is " + String.format("%.2f", area));
                                //bw.newLine();
                                bw.close();
                            } else {
                                dout.writeUTF("Error: No radius found");
                                dout.flush();
                                bw.write("Error: No radius found");
                                bw.newLine();
                                bw.close();
                            }
                        } 
                        //code for -r, rectangle, includes what to do if only one number is entered, how to find area, and perimetet + messages to client
                        else if (shape[1].equals("-r")) {    
                            if (shape.length == 3) {
                                int length = Integer.parseInt(shape[2]);
                                double perimeter = 2 * (length + length);
                                double area = length * length;
                                dout.writeUTF("Rectangle???s perimeter is " + String.format("%.2f", perimeter)
                                        + " and area is " + String.format("%.2f", area));
                                dout.flush();
                                bw.write("\nsides " + length + " " + length + ": Rectangle???s perimeter is " + String.format("%.2f", perimeter)
                                        + " and area is " + String.format("%.2f", area));
                                bw.newLine();
                                bw.close();
                            } else if (shape.length == 4) {
                                int length = Integer.parseInt(shape[2]);
                                int width = Integer.parseInt(shape[3]);
                                double perimeter = 2 * (length + width);
                                double area = length * width;

                                dout.writeUTF("Rectangle???s perimeter is " + String.format("%.2f", perimeter)
                                        + " and area is " + String.format("%.2f", area));
                                dout.flush();
                                bw.write("\nsides " + length + " " + width + ": Rectangle???s perimeter is " + String.format("%.2f", perimeter)
                                        + " and area is " + String.format("%.2f", area));
                                bw.newLine();
                                bw.close();
                            } else {
                                dout.writeUTF("Error: No sides found");
                                dout.flush();
                                bw.write("\nError: No sides found");
                                bw.newLine();
                                bw.close();
                            }
                        }

                    } 
                    //LIST command, outputs everything client logged in has ever done if they write "LIST"
                    else if (l.equalsIgnoreCase("list")) {//str.startsWith("LIST")) {
                        if (str.equalsIgnoreCase("LIST")) {
                            Scanner sc = new Scanner(new File(user + "_solutions.txt"));
                            String result = user + "\n";
                            while (sc.hasNextLine()) {
                                String line = sc.nextLine();
                                result += "\t" + line + "\n";
                            }
                            dout.writeUTF(result);
                            dout.flush();
                        } 
                        //else if statement to show what ever user has done, but only to root user
                        else if (str.split(" ").length == 2) {
                            if (user.equals("root")) {
                                String result = "";
                                for (int a = 0; a < logins.size(); a++) {
                                    String user = logins.get(a).split(" ")[0];
                                    result += user + "\n";
                                    File f = new File(user + "_solutions.txt");
                                    if(f.exists()){
                                        Scanner sc = new Scanner(f);
                                        while (sc.hasNextLine()) {
                                            String line = sc.nextLine();
                                            result += "\t" + line + "\n";
                                        }
                                    }
                                    else{
                                        result += "\tNo interactions yet\n";
                                    }
                                }
                                dout.writeUTF(result);
                                dout.flush();
                            }
                            //output if another client tries to list all 
                            else {
                                dout.writeUTF("Error: you are not the root user");
                                dout.flush();
                            }
                        }
                    }
                    else if (l.equalsIgnoreCase("MESSAGE")) {//when the client wants to message
                                 

                        if(str.split(" ")[1].equalsIgnoreCase("-all")  && user.equalsIgnoreCase("root"))//check if the root is sending a message to all
                        {
                             
                        
                        String message = str.toLowerCase().split("message", 2)[1];//split up the line into the parts you ened

                        String tag = message.split(" ")[1];
                        String message2 = message.toLowerCase().split(tag, 2)[1];
                        
                        

                        who = "john sally qiang";//put the message and addresses into variables
                        from = user;

                        mess = message2;
                        
                        String sending =" ";//combine the names into a variable of who were sending to
if(whoisloggedin.contains("john"))
    sending = sending + "john ";
if(whoisloggedin.contains("sally"))
    sending = sending + "sally ";
if(whoisloggedin.contains("qiang"))
    sending = sending + "qiang ";
                        dout.writeUTF("sending to" + sending);//notify the sender it worked
                        dout.flush();
                        }
                        else if(str.split(" ")[1] == "-all"){//youre not root
                            dout.writeUTF("not the root user");
                        dout.flush();
                        }
                        else{// no all but message one person 
                            
                        
                        
                        
                        String message = str.toLowerCase().split("message", 2)[1];
                        String tag = message.split(" ")[1];
                        String message2 = message.toLowerCase().split(tag, 2)[1];
                        
                        

                        who = tag;
                        from = user;

                        mess = message2;
                        
                        if(whoisloggedin.contains(who))//the person is logged in
                        {
                        dout.writeUTF("sending to " + who);
                        dout.flush();
                        }
                        else
                        {
                            dout.writeUTF( who + " is not logged in");//not logged in
                        dout.flush();
                        }
                        }
                    }
                    
                   
                    //invalid command statement
                    else {
                       dout.writeUTF("300 invalid command!");
                        dout.flush();
                    }
                    
                    
                }
                
                
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }//end try-catch
        
    }//end run
    public static void readData() {
        try {
            //directory to computer, directory has to be changed 
            //if being ran on another computer to the directory of that logins.txt file
            Scanner in = new Scanner(new File("logins.txt")); 
            while (in.hasNextLine()) {
                String line = in.nextLine();
                logins.add(line);
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }
    }
    
    
    
}//end ClientHandler
