package simple.jira;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import simple.jira.client;

public class executor {

	public static void main(String[] args) throws Exception {
		client theClient = new client();
		NotRealTestGetIssue(theClient);
		NotRealTestAddAttachment(theClient);
		//NotRealTestAddIssue(theClient);
		//NotRealTestGetIssues(theClient);
	}
	private static void NotRealTestGetIssue(client myClient) throws Exception{
		JSONObject issue = myClient.getIssue("10000");
		if(issue.containsKey("self")){
			System.out.println(issue.get("self"));
			JSONObject fields = (JSONObject) issue.get("fields");
			if(fields.containsKey("attachment")){
				System.out.println(fields.get("attachment"));
			}
		}
	}
	private static void NotRealTestGetIssues(client myClient) throws Exception{
		JSONObject searchString = new JSONObject();
		searchString.put("jql", "project = TEST");
		searchString.put("startAt", "0");
		searchString.put("maxResults", "15");
		JSONObject results = myClient.getIssues(searchString);
		System.out.println(results.toString());
	}
	private static void NotRealTestAddIssue(client myClient) {
		JSONObject issue = new JSONObject();
		JSONObject fields = new JSONObject();
		// Here should be gettign types from system 
		JSONObject issueType = new JSONObject();
		issueType.put("id", "1");
		// here should be getting projects from system
		JSONObject project = new JSONObject();
		project.put("id","10000");
				
		// here should be getting priorities from system maybe hash map instead?
		JSONObject priority = new JSONObject();
		priority.put("id", "4");
		
		fields.put("project", project);
		fields.put("description", "Test1");
		fields.put("issuetype", issueType);
		fields.put("priority", priority);
		fields.put("summary", "some summary");
		issue.put("fields", fields);
		
		if(myClient.createIssuee(issue)){
			System.out.println("Issue added");
		}
	}
	private static void NotRealTestAddAttachment(client myClient) {
		String issueID = "10000";
		if(myClient.addAttachment(issueID, "C:\\install.res.1028.dll")){
			System.out.println("Add attachment to issue");
		}
	}

}
