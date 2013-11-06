package simple.jira;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class SimpleRestClient2 {
	private URI BaseURI;
	private CloseableHttpClient client;
	private String simpleAuthString;
	
	public SimpleRestClient2(URI BaseURI,String User,String Password) {
		this.BaseURI = BaseURI;
		client = HttpClients.createDefault();
		simpleAuthString = Base64.encodeBase64String((User+":"+Password).getBytes());
	}
	public SimpleRestClient2(String url,String User,String Password) {
		this.BaseURI = URI.create(url);
		client = HttpClients.createDefault();
		simpleAuthString = Base64.encodeBase64String((User+":"+Password).getBytes());
	}
	
	public BasicIssue getIssue(String issueID) throws Exception{
		HttpGet httpGet = new HttpGet(BaseURI.toASCIIString() + "/issue/" + issueID);
		httpGet.addHeader("Authorization", "Basic "+ simpleAuthString);
		CloseableHttpResponse response1 = null;
		try {
			response1 = client.execute(httpGet);
			JSONObject jIssue = null;
		    if(response1.getStatusLine().getStatusCode() == 404){
		    	jIssue = (JSONObject)JSONValue.parse("{error: 'The requested issue doesn't exist'}");
		    }else{
		    HttpEntity entity1 = response1.getEntity();
		    jIssue = (JSONObject) JSONValue.parse(EntityUtils.toString(entity1));
		    return new BasicIssue(jIssue);
		    }
		} catch (ClientProtocolException e) {
		e.printStackTrace();
		} finally {
		    response1.close();
		}
		return null;
	}
	public BasicIssue getIssue(Integer issueID) throws Exception{
		return getIssue(issueID.toString());	
	}
	public BasicIssue createIssue(BasicIssue issue){
		HttpPost httppost = new HttpPost(BaseURI.toASCIIString() + "issue/");
		httppost.addHeader("Authorization", "Basic "+ simpleAuthString);
		StringEntity se;
		HttpResponse response = null;
		try {
			se = new StringEntity(issue.toJSONString());
			se.setContentType(ContentType.APPLICATION_JSON.toString());
	        httppost.setEntity(se);
	        response = client.execute(httppost);
	        HttpEntity ent = response.getEntity();
		    JSONObject result = (JSONObject) JSONValue.parse(EntityUtils.toString(ent));
        	System.out.println(result);
	        if(response.getStatusLine().getStatusCode() == 400 ){
			    return null;
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
		try {
			return getIssue(issue.ID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public Boolean addAttachment(Integer issueID, String FilePath){
		HttpPost httppost = new HttpPost(BaseURI.toASCIIString() + "issue/" + issueID +"/attachments");
		httppost.addHeader("Authorization", "Basic "+ simpleAuthString);
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
	public Boolean addAttachment(BasicIssue issue, String FilePath){
		return addAttachment(issue.ID, FilePath);
	}
	public ArrayList<BasicIssue> getIssuesInProject(Project project) {
		JSONObject query = new JSONObject();
		query.put("jql","project = "+ project.name);
		query.put("startAt", "0");
		query.put("maxResults", "15");
		
		JSONObject result = search(query);
		ArrayList<BasicIssue> ret = new ArrayList<>();
		if(result != null){
			JSONArray issues = (JSONArray) result.get("issues");
			for (Object i : issues) {
				ret.add(new BasicIssue((JSONObject)i));
			}
		}
		return ret;
	
	}
	private JSONObject search(JSONObject query){
		JSONObject results = null;
		HttpPost httppost = new HttpPost(BaseURI.toASCIIString() + "search/");
		httppost.addHeader("Authorization", "Basic "+ simpleAuthString);
		HttpResponse response = null;
		try {
			StringEntity se = new StringEntity(query.toJSONString());
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
}
