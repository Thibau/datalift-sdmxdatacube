package org.datalift.sdmxdatacube.jsontransporter;

/**
 * Storage class. It will be parsed to json and send to the client.
 * 
 * @author thibaut
 * 
 */
public class MessageTransporter {

	private String project = null;
	private String source = null;
	private String dest_graph_uri = null;
	private String dest_title = null;
	private String global = null;
	private boolean valid = true;

	public void setError(String paramName, String value) {
		valid = false;

		this.global = paramName + ": " + value;

		if (paramName == "project")
			this.project = value;
		else if (paramName == "source")
			this.source = value;
		else if (paramName == "dest_graph_uri")
			this.dest_graph_uri = value;
		else if (paramName == "dest_title")
			this.dest_title = value;
		else if (paramName == "global")
			this.global = value;
	}

	public boolean isValid() {
		return valid;
	}

	public String getProjectId() {
		return project;
	}

	public String getInputSourceUri() {
		return source;
	}

	public String getOutputSourceUri() {
		return dest_graph_uri;
	}

	public String getOutputSourceName() {
		return dest_title;
	}

	public String getGlobal() {
		return global;
	}
}
