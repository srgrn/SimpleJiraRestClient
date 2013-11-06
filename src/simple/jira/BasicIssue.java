package simple.jira;

import java.net.URI;

import org.json.simple.JSONObject;

public class BasicIssue extends JiraSimpleObject {
	private Project project;
	private Priority priority;
	private IssueType issueType;
	private String description;
	public BasicIssue(int ID, String BaseURL) {
		super(ID, "BasicIssue", BaseURL);
		//@TODO Fix it to get all required details
	}

	public BasicIssue(JSONObject obj) {
		super(obj);
		JSONObject fields = (JSONObject)obj.get("fields");
		project = new Project((JSONObject)fields.get("project"));
		priority = new Priority((JSONObject)fields.get("priority"));
		issueType = new IssueType((JSONObject)fields.get("issuetype"));
		description = fields.get("description").toString();
		
	}

	public BasicIssue(URI self, int ID) {
		super(self, ID, "BasicIssue");
		//@TODO Fix it to get all required details 
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		ret.put("self", self.toASCIIString());
		ret.put("id",ID);
		
		JSONObject fields = new JSONObject();
		fields.put("project", project.toJSON());
		fields.put("priority", priority.toJSON());
		fields.put("issuetype", issueType.toJSON());
		ret.put("fields", fields);
		return ret;
	}
	public String toJSONString() {
		return this.toJSON().toJSONString();
	}
}
