package drivethru.storage;

import lombok.Data;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@Data
@DynamoDBTable(tableName = "session")
public class DriveThruSessionDataItem {
	
    @DynamoDBHashKey(attributeName="session_id") private String sessionId;
    @DynamoDBAttribute(attributeName="name") private String name;

}
