package myproject.icarus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *  Class to send API Request
 */
class SendApiRequest
{
    /**
     * Takes a URL makes a GET request and returns its response in String format.
     * @param url URL to send GET request to.
     * @return Response the URL provides.
     */
    static int calls = 0;
    static String sendGetRequest(String url) throws IOException, URISyntaxException     //Basic Function to send a URL Request.
    {
        URI u = new URI(url);
        System.out.println("API Call #" + ++calls + " Made : " + u);
        HttpURLConnection connection = (HttpURLConnection) (u.toURL()).openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() > 399)
        {
            return ("Error : " + connection.getResponseCode());
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
            response.append(line);
        return response.toString();
    }
}
