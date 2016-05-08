package drivethru.storage;

import lombok.Data;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@Data
@DynamoDBTable(tableName = "menu_items")
public class DriveThruMenuItemDataItem {
	
    @DynamoDBHashKey(attributeName="item_name") private String itemName;
    @DynamoDBRangeKey(attributeName="category_name") private String categoryName;
    @DynamoDBAttribute(attributeName="ingredients") private List<String> ingredients;
    @DynamoDBAttribute(attributeName="is_vegan") private boolean isVegan;
    @DynamoDBAttribute(attributeName="price") private int price;

}
