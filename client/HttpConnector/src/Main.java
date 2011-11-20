import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

/**
 * A example that demonstrates how HttpClient APIs can be used to perform
 * form-based logon.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        
        HttpPost httpost = new HttpPost("http://192.168.2.6:8000/api/login/");
        
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair("username", "roka"));
        nvps.add(new BasicNameValuePair("password", "roka"));

        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        HttpResponse response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();

        System.out.println("Login form get: " + response.getStatusLine());
        if (entity != null) {
            entity.consumeContent();
        }
        System.out.println("Initial set of cookies:");
        List<Cookie> cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }
        
        
        httpclient.getCookieStore().addCookie(cookies.get(0));
        HttpGet httpget = new HttpGet("http://192.168.2.6:8000/api/check_in/");
        
        
        HttpResponse response2 = httpclient.execute(httpget);
        HttpEntity entity2 = response2.getEntity();
        
        System.out.println("Checkin get: " + response2.getStatusLine());
        if (entity2 != null) {
            entity2.consumeContent();
        }
        
        httpclient.getConnectionManager().shutdown();   
        
       
             
    }
}

