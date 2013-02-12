package org.datalift.sdmxdatacube.jsontransporter;

/**
 * Storage class. It will be parsed to json and send to the client.
 * 
 * @author thibaut
 * 
 */
public class MessageTransporter {

	private String projectId = null;
	private String inputSourceUri = null;
	private String outputSourceUri = null;
	private String outputSourceName = null;
	private String global = null;
	private boolean valid = true;

	public void setError(String paramName, String value) {

		// TODO Pourquoi les varibles ne sont pas affectées ?!?!?!
		valid = false;

		if (paramName == "projectId")
			this.projectId = value;
		else if (paramName == "inputSourceUri")
			this.inputSourceUri = value;
		else if (paramName == "outputSourceUri")
			this.outputSourceUri = value;
		else if (paramName == "outputSourceName")
			this.outputSourceName = value;
		else if (paramName == "global")
			this.global = value;
	}

	public boolean isValid() {
		return valid;
	}

	public String getProjectId() {
		return projectId;
	}

	public String getInputSourceUri() {
		return inputSourceUri;
	}

	public String getOutputSourceUri() {
		return outputSourceUri;
	}

	public String getOutputSourceName() {
		return outputSourceName;
	}

	public String getGlobal() {
		return global;
	}
}
