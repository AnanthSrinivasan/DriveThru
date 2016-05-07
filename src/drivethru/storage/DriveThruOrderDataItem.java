package drivethru.storage;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "orders")
public class DriveThruOrderDataItem {

private String sessionId;
private String orderId;
private String status;
private int quantity;
private int price;

@DynamoDBHashKey(attributeName = "session_id")
public String getSessionId() {
	return sessionId;
}
public void setSessionId(String sessionId) {
	this.sessionId = sessionId;
}

@DynamoDBRangeKey(attributeName = "order_id")
public String getOrderId() {
	return orderId;
}
public void setOrderId(String orderId) {
	this.orderId = orderId;
}

@DynamoDBAttribute(attributeName = "status")
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}

@DynamoDBAttribute(attributeName = "quantity")
public int getQuantity() {
	return quantity;
}
public void setQuantity(int quantity) {
	this.quantity = quantity;
}

@DynamoDBAttribute(attributeName = "price")
public int getPrice() {
	return price;
}
public void setPrice(int price) {
	this.price = price;
}

}
