package drivethru.storage;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "MenuCategories")
public class DriveThruCategoryDataItem {
	
private String categoryName;

@DynamoDBHashKey(attributeName = "CategoryName")
public String getCategoryName() {
	return categoryName;
}
public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
}

}
