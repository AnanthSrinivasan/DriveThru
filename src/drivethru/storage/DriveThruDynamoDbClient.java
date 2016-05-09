package drivethru.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

/**
 * Client for DynamoDB persistance layer for the Score Keeper skill.
 */
public class DriveThruDynamoDbClient implements IDriveThruDao{
    
    private DynamoDBMapper dynamoDBMapper;

    public DriveThruDynamoDbClient(final AmazonDynamoDBClient dynamoDBClient) {
        dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
    }

    /**
     * Loads an item from DynamoDB by primary Hash Key. Callers of this method should pass in an
     * object which represents an item in the DynamoDB table item with the primary key populated.
     * 
     * @param tableItem
     * @return
     */
    public ScoreKeeperUserDataItem loadItem(final ScoreKeeperUserDataItem tableItem) {
        ScoreKeeperUserDataItem item = dynamoDBMapper.load(tableItem);
        return item;
    }

    /**
     * Stores an item to DynamoDB.
     * 
     * @param tableItem
     */
    public void saveItem(final ScoreKeeperUserDataItem tableItem) {
        dynamoDBMapper.save(tableItem);
    }

	@Override
	public DriveThruSessionDataItem getSessionInformation(String sessionId) {
		DriveThruSessionDataItem driveThruSessionDataItem = new DriveThruSessionDataItem();
		driveThruSessionDataItem.setSessionId(sessionId);
		return dynamoDBMapper.load(driveThruSessionDataItem);
	}

	@Override
	public void storeSessionInformation(DriveThruSessionDataItem sessionData) {
		dynamoDBMapper.save(sessionData);
	}

	@Override
	public void storeOrder(DriveThruOrderDataItem orderData) {
		dynamoDBMapper.save(orderData);
	}

	@Override
	public List<DriveThruOrderDataItem> getOrdersBySessionId(String sessionId) {
		DynamoDBQueryExpression<DriveThruOrderDataItem> dynamoDBQueryExpression = 
				new DynamoDBQueryExpression<DriveThruOrderDataItem>();
		DriveThruOrderDataItem driveThruOrderDataItem = new DriveThruOrderDataItem();
		driveThruOrderDataItem.setSessionId(sessionId);
		dynamoDBQueryExpression.withHashKeyValues(driveThruOrderDataItem).withConsistentRead(true);
		return dynamoDBMapper.query(DriveThruOrderDataItem.class, dynamoDBQueryExpression);
	}

	@Override
	public void cancelOrderById(DriveThruOrderDataItem orderData) {
		dynamoDBMapper.delete(orderData);
	}

	@Override
	public void confirmsOrdersbySessionIds(List<DriveThruOrderDataItem> orders) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DriveThruMenuItemDataItem> getSpecialMenus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DriveThruMenuItemDataItem> getMenuByPriceRange(String low, String high) {
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withN(high));
        eav.put(":val2", new AttributeValue().withN(low));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
            .withFilterExpression("price <= :val1 and price >= :val2")
            .withExpressionAttributeValues(eav);
        
        List<DriveThruMenuItemDataItem> scanResult = dynamoDBMapper.scan(DriveThruMenuItemDataItem.class, scanExpression);
        return scanResult;
	}

	@Override
	public List<DriveThruMenuItemDataItem> getMenuByCategories(String category) {
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(category));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
            .withFilterExpression("category_name = :val2")
            .withExpressionAttributeValues(eav);
        
        List<DriveThruMenuItemDataItem> scanResult = dynamoDBMapper.scan(DriveThruMenuItemDataItem.class, scanExpression);
        return scanResult;
	}

	@Override
	public List<DriveThruCategoryDataItem> getCategories() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		return dynamoDBMapper.scan(DriveThruCategoryDataItem.class, scanExpression);
	}
}
