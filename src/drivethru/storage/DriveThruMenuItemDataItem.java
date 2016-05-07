package drivethru.storage;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "menu_items")
public class DriveThruMenuItemDataItem {
	
private String categoryName;
private List<String> ingredients;
private boolean isVegan;
private String itemName;
private int price;

@DynamoDBHashKey(attributeName = "item_name")
public String getItemName() {
	return itemName;
}
public void setItemName(String itemName) {
	this.itemName = itemName;
}

@DynamoDBRangeKey(attributeName = "category_name")
public String getCategoryName() {
	return categoryName;
}
public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
}

@DynamoDBAttribute(attributeName = "ingredients")
public List<String> getIngredients() {
	return ingredients;
}
public void setIngredients(List<String> ingredients) {
	this.ingredients = ingredients;
}

@DynamoDBAttribute(attributeName = "is_vegan")
public boolean isVegan() {
	return isVegan;
}
public void setVegan(boolean isVegan) {
	this.isVegan = isVegan;
}

@DynamoDBAttribute(attributeName = "price")
public int getPrice() {
	return price;
}
public void setPrice(int price) {
	this.price = price;
}

}
