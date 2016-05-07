package drivethru.storage;

import java.util.List;

import com.amazon.speech.speechlet.Session;

/**
 * Contains the methods to interact with the persistence layer for ScoreKeeper in DynamoDB.
 */
public class DriveThruDao implements IDriveThruDao{
    private final DriveThruDynamoDbClient dynamoDbClient;

    public DriveThruDao(DriveThruDynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    /**
     * Reads and returns the {@link ScoreKeeperGame} using user information from the session.
     * <p>
     * Returns null if the item could not be found in the database.
     * 
     * @param session
     * @return
     */
    public ScoreKeeperGame getScoreKeeperGame(Session session) {
        ScoreKeeperUserDataItem item = new ScoreKeeperUserDataItem();
        item.setCustomerId(session.getUser().getUserId());

        item = dynamoDbClient.loadItem(item);

        if (item == null) {
            return null;
        }

        return ScoreKeeperGame.newInstance(session, item.getGameData());
    }

    /**
     * Saves the {@link ScoreKeeperGame} into the database.
     * 
     * @param game
     */
    public void saveScoreKeeperGame(ScoreKeeperGame game) {
        ScoreKeeperUserDataItem item = new ScoreKeeperUserDataItem();
        item.setCustomerId(game.getSession().getUser().getUserId());
        item.setGameData(game.getGameData());

        dynamoDbClient.saveItem(item);
    }

	@Override
	public DriveThruSessionDataItem getSessionInformation(String sessionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeSessionInformation(DriveThruSessionDataItem sessionData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void storeOrder(DriveThruOrderDataItem orderData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DriveThruOrderDataItem> getOrdersBySessionId(String sessionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelOrderById(DriveThruOrderDataItem orderData) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DriveThruCategoryDataItem> getCategories() {
		// TODO Auto-generated method stub
		return null;
	}
}
