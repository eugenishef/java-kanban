import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        System.setProperty("jdk.permissions.allowIllegalAccess", "true");
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks", new TasksHandler());

        httpServer.start();
        System.out.println("HTTP server start on port: " + PORT);
    }
}
