import org.junit.Test;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SendPostRequestTest {

    @Test
    public void postTest() {

        System.setProperty("http.proxyHost", "localhost");
        System.setProperty("https.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "8889");
        System.setProperty("https.proxyPort", "8889");

        String SAUCE_USERNAME = System.getenv("SAUCE_USERNAME");
        String SAUCE_ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");


        try {
//            URL obj = new URL("https://api.us-west-1.saucelabs.com/team-management/v1/users/");
            URL obj = new URL("https://api.us-west-1.saucelabs.com/team-management/v1/users");
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            BufferedReader in;

            String encoded = Base64.getEncoder().encodeToString((SAUCE_USERNAME+":"+SAUCE_ACCESS_KEY).getBytes(StandardCharsets.UTF_8));  //Java 8
            con.setRequestProperty("Authorization", "Basic "+encoded);

            // add request header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "EnriqueSauceTest");
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("Accept", "application/json");

            //JSON String need to be constructed for the specific resource.
            //We may construct complex JSON using any third-party JSON libraries such as jackson or org.json


            // Send post request
            con.setDoOutput(true);
            String jsonInputString = "{\"email\": \"email@mycorp.com\",\"first_name\": \"test\",\"last_name\": \"case01\",\"organization\": \"ENTER_ORG_ID\",\"password\": \"stringeewdd@!\",\"role\": 3,\"username\": \"MY_USER_NAME\"}";

            try(OutputStream os = con.getOutputStream()){
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            if (responseCode >= 400)
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            else
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
