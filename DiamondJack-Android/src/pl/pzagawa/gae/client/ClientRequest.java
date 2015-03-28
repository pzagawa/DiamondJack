package pl.pzagawa.gae.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.sql.SQLException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import pl.pzagawa.diamond.jack.MainApplication;
import pl.pzagawa.gae.auth.Consts;

public class ClientRequest
{
    private static HttpClient client;
	
    private final URI uri;
    private final String authCookie;
    
    private String result;
    private int statusCode;
    private String statusReason;
    private String serverMessage;
    
    public ClientRequest(URI uri, String authCookie)
    {
    	if (client == null)
    		client = new DefaultHttpClient();

        this.uri = uri;
        this.authCookie = authCookie;
    }

    public void post(RequestCommand requestCommand)
    	throws JSONException, ClientProtocolException, IOException, SQLException
    {
    	result = null;
    	statusCode = 0;
    	statusReason = "";
    	serverMessage = "";
    	
    	final HttpEntity requestData = new StringEntity(requestCommand.getRequestData(), "UTF-8");
    	
        HttpPost post = new HttpPost();

        post.setURI(uri);
        
        post.setHeader("Content-Type", "application/json; charset=UTF-8");        
        post.setHeader("User-Agent", Consts.USER_AGENT);
        post.setHeader("Cookie", authCookie);
        post.setHeader("RequestCommand", requestCommand.getName());
        post.setHeader("Mobile-Client-Version", Integer.toString(MainApplication.getAppVersionCode()));

    	post.setEntity(requestData);
    	
    	//execute request
        HttpResponse response = client.execute(post);

        //get results
    	statusCode = response.getStatusLine().getStatusCode();
    	statusReason = response.getStatusLine().getReasonPhrase();
    	
    	final HttpEntity responseData = response.getEntity();

    	//read response string
    	if (responseData != null)
    		result = readStreamAsString(responseData.getContent());

    	//decode json server message if exists
   		serverMessage = decodeServerMessage();
    }
    
    private String readStreamAsString(InputStream in)
    {
        try
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            
            byte[] buffer = new byte[1024];            
            int count;            
            do {
                count = in.read(buffer);
                if (count > 0)
                    out.write(buffer, 0, count);
            } while (count >= 0);
            
            return out.toString("UTF-8");
            
        } catch (UnsupportedEncodingException e)
        {
        } catch (IOException e)
        {
        } finally {
            try
            {
                in.close();
            } catch (IOException ignored)
            {
            }
        }
        
        return null;
    }

    private String decodeServerMessage()
    {
    	if (statusCode != 200 && result != null)
    	{
			try
			{
				JSONObject jMessage = new JSONObject(result);				
	    		return jMessage.optString("text", "").trim();
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}    		
    	} 
    
    	return "";
    }
    
    public String getResultString()
    {
    	return result;
    }

    public int getStatusCode()
    {
    	return statusCode;
    }
    
    public String getStatusReason()
    {
    	return statusReason;
    }
    
    public String getServerMessage()
    {
    	if (serverMessage == null)
    		return "";
    	
    	return serverMessage;
    }

}
