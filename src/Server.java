public class Server {

    public static void main(String[] args) {
        int port = 8531;
        ServerHandler serverHandler = new ServerHandler(port);
        serverHandler.start();
    }
}
