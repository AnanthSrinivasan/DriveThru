package drivethru.storage;

import lombok.Data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@Data
@DynamoDBTable(tableName = "menu_categories")
public class DriveThruCategoryDataItem {
	
    @DynamoDBHashKey(attributeName="category_name") private String categoryName;

}
