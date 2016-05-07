package drivethru.storage;

import java.util.List;

public interface IDriveThruDao {

	//RetrieveSession information
	DriveThruSessionDataItem getSessionInformation(String sessionId);

	//Store session information
	void storeSessionInformation(DriveThruSessionDataItem sessionData);

	//Store order
	void storeOrder(DriveThruOrderDataItem orderData);
	
	//Get orders by session id
	List<DriveThruOrderDataItem> getOrdersBySessionId(String sessionId);
	
	//Cancel order by order id
	void cancelOrderById(DriveThruOrderDataItem orderData);
	
	//Confirm orders by session id
	void confirmsOrdersbySessionIds(List<DriveThruOrderDataItem> orders);
	
	//Get Special menu
	List<DriveThruMenuItemDataItem> getSpecialMenus();
	
	//Get Menu below a price range
	List<DriveThruMenuItemDataItem> getMenuByPriceRange(double low, double high);
	
	//get MenuByCategories
	List<DriveThruMenuItemDataItem> getMenuByCategories(String category);
	
	//Get categories
	List<DriveThruCategoryDataItem> getCategories();
	
}
