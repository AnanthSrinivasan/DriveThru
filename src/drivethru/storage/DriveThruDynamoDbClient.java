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
    private final AmazonDynamoDBClient dynamoDBClient;
    
    private DynamoDBMapper mapper = createDynamoDBMapper();

    public DriveThruDynamoDbClient(final AmazonDynamoDBClient dynamoDBClient) {
        this.dynamoDBClient = dynamoDBClient;
    }

    /**
     * Loads an item from DynamoDB by primary Hash Key. Callers of this method should pass in an
     * object which represents an item in the DynamoDB table item with the primary key populated.
     * 
     * @param tableItem
     * @return
     */
    public ScoreKeeperUserDataItem loadItem(final ScoreKeeperUserDataItem tableItem) {
        DynamoDBMapper mapper = createDynamoDBMapper();
        ScoreKeeperUserDataItem item = mapper.load(tableItem);
        return item;
    }

    /**
     * Stores an item to DynamoDB.
     * 
     * @param tableItem
     */
    public void saveItem(final ScoreKeeperUserDataItem tableItem) {
        DynamoDBMapper mapper = createDynamoDBMapper();
        mapper.save(tableItem);
    }

    /**
     * Creates a {@link DynamoDBMapper} using the default configurations.
     * 
     * @return
     */
    private DynamoDBMapper createDynamoDBMapper() {
        return new DynamoDBMapper(dynamoDBClient);
    }

	@Override
	public DriveThruSessionDataItem getSessionInformation(String sessionId) {
		DriveThruSessionDataItem driveThruSessionDataItem = new DriveThruSessionDataItem();
		//TODO Set sessiondata.
		
		DriveThruSessionDataItem item = mapper.load(driveThruSessionDataItem);
		return item;
	}

	@Override
	public void storeSessionInformation(DriveThruSessionDataItem sessionData) {
		mapper.save(sessionData);
	}

	@Override
	public void storeOrder(DriveThruOrderDataItem orderData) {
		mapper.save(orderData);
	}

	@Override
	public List<DriveThruOrderDataItem> getOrdersBySessionId(String sessionId) {
		DynamoDBQueryExpression dynamoDBQueryExpression = new DynamoDBQueryExpression<>();
		DriveThruSessionDataItem driveThruSessionDataItem = new DriveThruSessionDataItem();
		//TODO set sessionId
		
		dynamoDBQueryExpression.withHashKeyValues(driveThruSessionDataItem).withConsistentRead(true);
		List<DriveThruOrderDataItem> driveThruOrderDataItems = mapper.query(DriveThruOrderDataItem.class, dynamoDBQueryExpression);
		return driveThruOrderDataItems;
	}

	@Override
	public void cancelOrderById(DriveThruOrderDataItem orderData) {
		mapper.delete(orderData);
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
//		List<DriveThruCategoryDataItem> driveThruCategoryDataItems = new ArrayList<>();
//		DriveThruCategoryDataItem driveThruCategoryDataItem = new DriveThruCategoryDataItem();
//		driveThruCategoryDataItem.setCategoryName("burger");
//		driveThruCategoryDataItems.add(driveThruCategoryDataItem);
//		
//		driveThruCategoryDataItem = new DriveThruCategoryDataItem();
//		driveThruCategoryDataItem.setCategoryName("sandwhich");
//		driveThruCategoryDataItems.add(driveThruCategoryDataItem);
//		//return driveThruCategoryDataItems;
		DynamoDBMapper mapper = createDynamoDBMapper();
		return mapper.scan(DriveThruCategoryDataItem.class, scanExpression);
	}
}
