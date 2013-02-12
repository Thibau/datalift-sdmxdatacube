package org.datalift.sdmxdatacube.jsontransporter;

public class MessageTransporter {

	public String projectId = null;
	public String inputSourceURI = null;
	public String outputSourceURI = null;
	public String outputSourceName = null;
	public String global = null;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getInputSourceURI() {
		return inputSourceURI;
	}

	public void setInputSourceURI(String inputSourceURI) {
		this.inputSourceURI = inputSourceURI;
	}

	public String getOutputSourceURI() {
		return outputSourceURI;
	}

	public void setOutputSourceURI(String outputSourceURI) {
		this.outputSourceURI = outputSourceURI;
	}

	public String getOutputSourceName() {
		return outputSourceName;
	}

	public void setOutputSourceName(String outputSourceName) {
		this.outputSourceName = outputSourceName;
	}

	public String getGlobal() {
		return global;
	}

	public void setGlobal(String global) {
		this.global = global;
	}

}
