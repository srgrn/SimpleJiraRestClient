package simple.jira;

import java.net.URI;

import org.json.simple.JSONObject;

public class IssueType extends JiraSimpleObject {
	private String description;
	
	public IssueType(int ID, String name,String BaseURL,String description) {
		super(ID,name,BaseURL);
		this.description = description;
	}
	public IssueType(JSONObject obj){
		super(obj);
		description = obj.get("description").toString();
	}
	
	public IssueType(URI self, int ID, String name,String description) {
		super(self,ID,name);
		this.description = description;
	}
	

}
