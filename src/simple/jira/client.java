package simple.jira;
import java.beans.FeatureDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.Header;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MinimalField;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class client {
	static String BASEURL = "http://localhost:8080/rest/api/2/";
	public CloseableHttpClient client;
	String encoded = Base64.encodeBase64String("admin:admin".getBytes());
	
	public client() {
		client = HttpClients.createDefault();
	}
	public JSONObject getIssue(String issueID) throws Exception{
		JSONObject issue = null;
		HttpGet httpGet = new HttpGet(BASEURL + "issue/" + issueID);
		httpGet.addHeader("Authorization", "Basic "+ encoded);
		CloseableHttpResponse response1 = null;
		try {
			response1 = client.execute(httpGet);

		    if(response1.getStatusLine().getStatusCode() == 404){
		    	issue = (JSONObject)JSONValue.parse("{error: 'The requested issue doesn't exist'}");
		    }else{
		    HttpEntity entity1 = response1.getEntity();
		    issue = (JSONObject) JSONValue.parse(EntityUtils.toString(entity1));
		    }
		} catch (ClientProtocolException e) {
		e.printStackTrace();
		} finally {
		    response1.close();
		}
		return issue;
	}
	public Boolean updateIssue(String issueID, JSONObject update){
		HttpPut httpput = new HttpPut(BASEURL + "issue/");
		
		return true;
	}
	public Boolean createIssuee(JSONObject details){
		HttpPost httppost = new HttpPost(BASEURL + "issue/");
		httppost.addHeader("Authorization", "Basic "+ encoded);
		StringEntity se;
		HttpResponse response = null;
		try {
			se = new StringEntity(details.toJSONString());
			se.setContentType(ContentType.APPLICATION_JSON.toString());
	        httppost.setEntity(se);
	        response = client.execute(httppost);
	        HttpEntity ent = response.getEntity();
		    JSONObject result = (JSONObject) JSONValue.parse(EntityUtils.toString(ent));
        	System.out.println(result);
	        if(response.getStatusLine().getStatusCode() == 400 ){
			    return false;
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
		return true;
	}
	public Boolean addAttachment(String issueID, String FilePath){
		HttpPost httppost = new HttpPost(BASEURL + "issue/" + issueID +"/attachments");
		httppost.addHeader("Authorization", "Basic "+ encoded);
		MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
		HttpResponse response = null;
		try {
			
			File file = new File(FilePath);
			String fileName = file.getName();
			multipartEntity.addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, fileName);
			httppost.setEntity(multipartEntity.build());
			httppost.addHeader("X-Atlassian-Token", "nocheck");
			response = client.execute(httppost);
			System.out.println(response.getStatusLine());
			if(response.getStatusLine().getStatusCode() != 200)
			{
				return false;
			}
		} catch (IOException e) {
		e.printStackTrace();
		}
		return true;
	}
	public JSONObject getIssues(JSONObject params){
		
		JSONObject results = null;
		HttpPost httppost = new HttpPost(BASEURL + "search/");
		httppost.addHeader("Authorization", "Basic "+ encoded);
		HttpResponse response = null;
		try {
			StringEntity se = new StringEntity(params.toJSONString());
			se.setContentType(ContentType.APPLICATION_JSON.toString());
			httppost.setEntity(se);
			response = client.execute(httppost);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity ent = response.getEntity();
			    results = (JSONObject) JSONValue.parse(EntityUtils.toString(ent));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
	private List<String> GetAvailablePriorities() throws Exception{
		HttpGet httpGet = new HttpGet(BASEURL + "priority/");
		httpGet.addHeader("Authorization", "Basic "+ encoded);
		CloseableHttpResponse response1 = null;
		List<String> priorityNames = new ArrayList<String>();
		try {
			response1 = client.execute(httpGet);
		    HttpEntity entity1 = response1.getEntity();
		    JSONArray arr = (JSONArray) JSONValue.parse(EntityUtils.toString(entity1));
		    for (Object object : arr) {
				priorityNames.add(((JSONObject)object).get("name").toString());
			}
		} catch (ClientProtocolException e) {
		e.printStackTrace();
		} finally {
		    response1.close();
		}
		return priorityNames;
	}
	
    

}
