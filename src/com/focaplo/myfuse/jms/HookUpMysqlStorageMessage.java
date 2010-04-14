package com.focaplo.myfuse.jms;

/**
 * this is the message the controller sends to the execution unit
 * to serve specified lab - to read and register a data-source for
 * the given lab name.
 * The listener will then reply with a Reply message to controller
 * to indicate the lab has been registered and ready to serve.
 * 
 */
public class HookUpMysqlStorageMessage extends AbstractMessage{

	String executionUnitName;
	String labName;
	String labJdbcPropertyFilePath;
	
	public String getLabName() {
		return labName;
	}


	public void setLabName(String labName) {
		this.labName = labName;
	}


	public String getLabJdbcPropertyFilePath() {
		return labJdbcPropertyFilePath;
	}


	public void setLabJdbcPropertyFilePath(String labJdbcPropertyFilePath) {
		this.labJdbcPropertyFilePath = labJdbcPropertyFilePath;
	}


	public String getExecutionUnitName() {
		return executionUnitName;
	}


	public void setExecutionUnitName(String executionUnitName) {
		this.executionUnitName = executionUnitName;
	}
	
	
}
