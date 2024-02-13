package throughput;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class WordCountHandler implements HttpHandler {

    private String text;

    public WordCountHandler(String text) {
        this.text = text;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] keyValue = exchange.getRequestURI().getQuery().split("=");

        String action = keyValue[0];
        String value = keyValue[1];

        if (!action.equals("word")){
                exchange.sendResponseHeaders(400,0);
                return;
        }
        long count = countWord(value);
        byte[] response = Long.toString(count).getBytes();
        exchange.sendResponseHeaders(200,response.length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response);
        outputStream.close();


    }

    private long countWord(String value) {

        int index = 0;
        int count = 0;

        while (index>=0){
            index = text.indexOf(value,index);

            if (index>=0){
                count++;
                index++;
            }
        }
        return  count;
    }
}
