import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerHandler extends Thread{
    private final int port;
    private final ArrayList<ClientThread> user_clientthreads = new ArrayList<>();
    public ServerHandler(int port){
        this.port = port;
    }

    public ArrayList<ClientThread> getUser_clientthreads() {
        return user_clientthreads;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port); // A server socket to transfer data over networks
            while (true) { // While loop runs until the connection is closed
                Socket acceptedSocket = serverSocket.accept(); // Accepting the user connection
                System.out.println(acceptedSocket + "'s connection has been accepted!");
                ClientThread clientThread = new ClientThread(this, acceptedSocket); // Running multiple threads with user made class
                user_clientthreads.add(clientThread);
                clientThread.start();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
 }

