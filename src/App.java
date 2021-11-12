import com.beachbumtask.server.MathServer;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        MathServer server = MathServer.getInstance();
        int port = Integer.parseInt(System.getenv("PORT"));
        server.start(port);
    }
}
