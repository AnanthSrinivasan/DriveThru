package drivethru.storage;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "menu_categories")
public class DriveThruCategoryDataItem {
	
private String categoryName;

@DynamoDBHashKey(attributeName = "category_name")
public String getCategoryName() {
	return categoryName;
}
public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
}

}
