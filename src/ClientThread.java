import java.io.*;
import java.net.Socket;
import java.lang.String;
import java.util.ArrayList;

public class ClientThread extends Thread {
    private final Socket clientSocket; // The client socket that will accept the connection of the user
    private final ServerHandler serverHandler;
    private OutputStream output;

    public ClientThread(ServerHandler serverHandler, Socket clientSocket) { // Constructor
        this.clientSocket = clientSocket;
        this.serverHandler = serverHandler;
    }

    /**
     * Handling the thread of each user
     * @throws IOException
     * @throws InterruptedException
     */
    private void handleSocket() throws IOException, InterruptedException {

        InputStreamReader inputReader = new InputStreamReader(this.clientSocket.getInputStream()); // Reading the user input
        this.output = this.clientSocket.getOutputStream(); // The output that is read by the terminal
        UserInfo userInfo = new UserInfo(); // The user info to be stored in

        BufferedReader bufferedReader = new BufferedReader(inputReader); // Reading out each line of the user input
        String line; // Storing each buffered line into this variable

        while ((line = bufferedReader.readLine()) != null){
            String[] tokens = line.split(" ");

            String command = tokens[0]; // Storing the command
            if ("quit".equalsIgnoreCase(command)){ // If the user input reads quit, then close the socket and then break out of the loop
                output.write(("Closing chatroom").getBytes());
                this.clientSocket.close();
                break;
            }
            else if ("register".equalsIgnoreCase(command)){ // Register the user if necessary
                handleRegister(output, tokens, userInfo); // Handles the register command
            }
            else if ("login".equalsIgnoreCase(command)){ // Login command
                handleLogin(output, tokens, userInfo); // Handle logging in
            }
            else{
                output.write(("Unknown command " + command + "\n").getBytes()); // Output to user the unknown command
            }
        }
    }

    /**
     * Handles the register command
     * @param outputStream: What the console is being written to
     * @param tokens: The user input
     * @param userInfo: A HashMap of type UserInfo that stores all user information
     * @throws IOException
     */
    private void handleRegister(OutputStream outputStream, String[] tokens, UserInfo userInfo) throws IOException {
        if (tokens.length > 3){
            outputStream.write(("Too many arguments. Please try again\n").getBytes());
        }
        else if (tokens.length < 3){
            outputStream.write(("Too few arguments. Please try again\n").getBytes());
        }
        else {
            userInfo.register(tokens[1], tokens[2]);
            outputStream.write("Successfully registered!\n".getBytes());
        }
    }

    /**
     * Handles the login command
     * @param outputStream: What the console is being written to
     * @param tokens: The user input
     * @param userInfo: A HashMap of type UserInfo that stores all user information
     * @throws IOException
     */
    private void handleLogin(OutputStream outputStream, String[] tokens, UserInfo userInfo) throws IOException {
        if (tokens.length > 3){
            outputStream.write(("Too many arguments. Please try again\n").getBytes());
        }
        else if (tokens.length < 3){
            outputStream.write(("Too few arguments. Please try again\n").getBytes());
        }
        else{
            String username = tokens[1];
            String password = tokens[2];
            ArrayList<ClientThread> usersOnline = serverHandler.getUser_clientthreads();

            if (userInfo.login(username, password)){ // If successful login, send online status to everyone logged in
                outputStream.write("Successful login!\n".getBytes());
                send("Online " + userInfo.getLoginInfo().keySet() + "\n"); // Sends current user who is currently online

                for (ClientThread user: usersOnline){
                   user.send("Online " + username + "\n"); // Send to everyone else online a new user joined
                }
            }
            else{
                outputStream.write("Invalid username or password\n".getBytes() );
            }
        }
    }


    private void send(String console_output) throws IOException {
        output.write(console_output.getBytes());
    }
    @Override
    public void run(){
        try{
            handleSocket();
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
