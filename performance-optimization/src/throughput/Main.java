package throughput;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final String INPUT_FILE = "./resources/throughput/war_and_peace.txt";
    private static final int NUMBER_OF_THREADS =5;

    public static void main(String[] args) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
        serverStart(text);

    }

    private static void serverStart(String text) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/search", new WordCountHandler(text));

        ExecutorService threadPool = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        httpServer.setExecutor(threadPool);
        httpServer.start();
    }
}
