package simple.jira;

import java.net.URI;

import org.json.simple.JSONObject;

public class JiraSimpleObject {

	protected URI self;
	protected int ID;
	protected String name;

	public JiraSimpleObject(int ID, String name,String BaseURL) {
		this.ID = ID;
		this.name = name;
		this.self = URI.create(BaseURL + "/project/" + ID);
	}
	public JiraSimpleObject(JSONObject obj){
		self = URI.create(obj.get("self").toString());
		ID = Integer.parseInt(obj.get("id").toString());
		name = obj.get("name").toString();
	}
	
	public JiraSimpleObject(URI self, int ID, String name) {
		this.self = self;
		this.ID = ID;
		this.name = name;
	}

	public URI getSelf() {
		return self;
	}

	public String getSelfString() {
		return self.toASCIIString();
	}

	public int getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		ret.put("self", self.toASCIIString());
		ret.put("name", name);
		ret.put("id",ID);
		return ret;
	}

	public String toJSONString() {
		return this.toJSON().toJSONString();
	}

}