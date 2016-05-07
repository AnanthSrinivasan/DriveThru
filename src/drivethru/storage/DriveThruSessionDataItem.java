package drivethru.storage;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Session")
public class DriveThruSessionDataItem {
	
private String sessionId;
private String name;

@DynamoDBHashKey(attributeName = "SessionId")
public String getSessionId() {
	return sessionId;
}
public void setSessionId(String sessionId) {
	this.sessionId = sessionId;
}

@DynamoDBAttribute(attributeName = "Name")
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}

}
