package pl.pzagawa.gae.auth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

public class CookieRequest
{
	private static final String AUTH_COOKIE_NAME_S = "SACSID";
	private static final String AUTH_COOKIE_NAME = "ACSID";

	private String authCookie;
	private String errorMessage;

	public CookieRequest()
	{
	}

	public void process(final String authToken)
	{
		authCookie = null;
		
		if (authToken != null)
        	authCookie = getAuthCookie(authToken);
	}

	private URI getURI(String authToken)
		throws UnsupportedEncodingException, URISyntaxException
	{
        return new URI(Consts.APP_URL + "/_ah/login?continue=" + URLEncoder.encode(Consts.APP_URL, "UTF-8") + "&auth=" + authToken);		
	}
	
	private String getAuthCookie(String authToken)
	{
		errorMessage = null;
		
        try
        {
            //get S/ACSID cookie
            DefaultHttpClient client = new DefaultHttpClient();

            URI uri = getURI(authToken);

            HttpGet httpGet = new HttpGet(uri);
            
            final HttpParams params = new BasicHttpParams();
            
            HttpClientParams.setRedirecting(params, false);
            
            httpGet.setParams(params);

            HttpResponse response = client.execute(httpGet);
            
            Header[] headers = response.getHeaders("Set-Cookie");
            
            final int statusCode = response.getStatusLine().getStatusCode();
                        
            if (statusCode != HttpStatus.SC_MOVED_TEMPORARILY)
                return null;
            
            if (headers.length == 0)
            	return null;

            for (Cookie cookie : client.getCookieStore().getCookies())
            {
                if (AUTH_COOKIE_NAME.equals(cookie.getName()))
                    return AUTH_COOKIE_NAME + "=" + cookie.getValue();                
                if (AUTH_COOKIE_NAME_S.equals(cookie.getName()))
                    return AUTH_COOKIE_NAME_S + "=" + cookie.getValue();
            }

        } catch (URISyntaxException e)
        {
        	errorMessage = e.getMessage();
        	e.printStackTrace();
	    } catch (IOException e)
	    {
	    	errorMessage = e.getMessage();
	    	e.printStackTrace();
	    }

        return null;
    }

	public boolean isSuccess()
	{
		return (authCookie != null);
	}
		
	public String getAuthCookie()
	{
		return authCookie;
	}
	
	public String getErrorMessage()
	{
		return errorMessage;
	}
	
}
