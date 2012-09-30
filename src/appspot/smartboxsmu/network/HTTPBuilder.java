package appspot.smartboxsmu.network;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * This class is used to set the standard HTTPParams for GET and POST AsyncTask requests
 * @author Ronny
 */
public class HTTPBuilder {
	// Set the timeout in milliseconds until a connection is established.
	// The default value is zero, that means the timeout is not used. 
	private static final int CONNECTION_TIMEOUT = 30000; //set to 10s for now
	// Set the default socket timeout (SO_TIMEOUT) 
	// in milliseconds which is the timeout for waiting for data after connection has been established
	// and the socket is still  waiting for data
	private static final int SOCKET_TIMEOUT = 30000; //set to 10s for now

	public static void setParameter(HttpGet get) {
		get.setParams(HTTPBuilder.getHttpParams());
	}
	
	public static void setParameter(HttpPut put) {
		put.setParams(HTTPBuilder.getHttpParams());
	}
	
	public static void setParameter(HttpDelete delete) {
		delete.setParams(HTTPBuilder.getHttpParams());
	}

	public static void setParameter(HttpPost post) {
		post.setParams(HTTPBuilder.getHttpParams());
	}

	private static HttpParams getHttpParams() {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
		return httpParameters;
	}
}
