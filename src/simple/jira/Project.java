package simple.jira;

import java.net.URI;

import org.json.simple.JSONObject;

public class Project extends JiraSimpleObject {
	public Project(int ID, String name,String BaseURL) {
		super(ID,name,BaseURL);
	}
	public Project(JSONObject obj){
		super(obj);
	}
	
	public Project(URI self, int ID, String name) {
		super(self,ID,name);
	}
	
}
