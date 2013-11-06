package simple.jira;

import java.net.URI;

import org.json.simple.JSONObject;


public class Priority extends JiraSimpleObject {
	private String description;
	
	public Priority(int ID, String name,String BaseURL,String description) {
		super(ID,name,BaseURL);
		this.setDescription(description);
	}
	public Priority(JSONObject obj){
		super(obj);
		setDescription(obj.get("description").toString());
	}
	
	public Priority(URI self, int ID, String name,String description) {
		super(self,ID,name);
		this.setDescription(description);
	}
	public String getDescription() {
		return description;
	}
	private void setDescription(String description) {
		this.description = description;
	}
	
}
