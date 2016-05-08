package drivethru.storage;

import lombok.Data;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@Data
@DynamoDBTable(tableName = "orders")
public class DriveThruOrderDataItem {

    @DynamoDBHashKey(attributeName="session_id") private String sessionId;
    @DynamoDBRangeKey(attributeName="order_id") private String orderId;
    @DynamoDBAttribute(attributeName="status") private String status;
    @DynamoDBAttribute(attributeName="quantity") private int quantity;
    @DynamoDBAttribute(attributeName="price") private int price;

}
