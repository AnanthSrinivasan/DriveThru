package drivethru.storage;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

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
		//TODO Set sessiondata.
		
		DriveThruSessionDataItem item = dynamoDBMapper.load(driveThruSessionDataItem);
		return item;
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
		DynamoDBQueryExpression dynamoDBQueryExpression = new DynamoDBQueryExpression<>();
		DriveThruSessionDataItem driveThruSessionDataItem = new DriveThruSessionDataItem();
		//TODO set sessionId
		
		dynamoDBQueryExpression.withHashKeyValues(driveThruSessionDataItem).withConsistentRead(true);
		List<DriveThruOrderDataItem> driveThruOrderDataItems = dynamoDBMapper.query(DriveThruOrderDataItem.class, dynamoDBQueryExpression);
		return driveThruOrderDataItems;
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
	public List<DriveThruMenuItemDataItem> getMenuByPriceRange(double low, double high) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DriveThruMenuItemDataItem> getMenuByCategories(String category) {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		//
		return null;
	}

	@Override
	public List<DriveThruCategoryDataItem> getCategories() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		return dynamoDBMapper.scan(DriveThruCategoryDataItem.class, scanExpression);
	}
}
