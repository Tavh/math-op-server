import com.beachbumtask.server.MathServer;

import java.io.IOException;

/**
 * A math server designed to handle multiple math clients with a unique protocol
 * @author Tav Herzlich
 * @version 1.0
 * @since 11/12/2021
 */
public class App {
    public static void main(String[] args) {
        MathServer server = MathServer.getInstance();
        int port = Integer.parseInt(System.getenv("PORT"));
        server.start(port);
    }
}
