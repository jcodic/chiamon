package com.ddx.chiamon.utils;

import com.ddx.chiamon.common.data.NodeAuthenticator;
import com.ddx.chiamon.common.data.Setup;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author ddx
 */
public class HttpUtils {

    private static boolean initAnyCentificateCompleted = false;
    
    public static void initAnyCentificate() throws Exception {
	
        if (initAnyCentificateCompleted) return;
        
        initAnyCentificateCompleted = true;
        
	// Create a trust manager that does not validate certificate chains
	TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
	    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		return null;
	    }

	    public void checkClientTrusted(X509Certificate[] certs, String authType) {
	    }

	    public void checkServerTrusted(X509Certificate[] certs, String authType) {
	    }
	}
	};

	// Install the all-trusting trust manager
	SSLContext sc = SSLContext.getInstance("SSL");
	sc.init(null, trustAllCerts, new java.security.SecureRandom());
	HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	// Create all-trusting host name verifier
	HostnameVerifier allHostsValid = new HostnameVerifier() {
	    public boolean verify(String hostname, SSLSession session) {
		return true;
	    }
	};

	HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    public static void init(Setup setup) throws Exception {
        
        initAnyCentificate();
        Authenticator.setDefault(new NodeAuthenticator(setup.getMainNodeUser(), MessageX.decryptString(setup.getMainNodePassword())));
    }

    public static String encodeParams(Map<String, Object> params) throws Exception {
        
        StringBuilder bf = new StringBuilder();
        
        for (Map.Entry<String, Object> param : params.entrySet()) {
            
            if (bf.length() != 0) {
                bf.append('&');
            }
            bf.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            bf.append('=');
            bf.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        return bf.toString();
    }
    
    public static Map<String, Object> getSimpleParam(String name, Object value) {
        
        Map<String, Object> params = new HashMap<>();
        params.put(name, value);
        return params;
    }

    public static byte[] downloadInputStream(InputStream stream) throws Exception {

        ByteArrayOutputStream bufferedStream = new ByteArrayOutputStream();

        byte[] bf = new byte[16 * 1024];
        int bytesRead;

        while ((bytesRead = stream.read(bf)) != -1) bufferedStream.write(bf, 0, bytesRead);
        stream.close();

        bufferedStream.flush();
        byte result[] = bufferedStream.toByteArray();
        bufferedStream.close();

        return result;
    }
    
}