package models;

public class ItemPriceModel {
    String itemName;
    String itemPrice;
    String itemQuantity;
    String expectedSubtotal;


    public ItemPriceModel(String itemName, String itemPrice, String itemQuantity) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.expectedSubtotal = calculateExpectedSubtotal(itemPrice, itemQuantity);
    }

    private String calculateExpectedSubtotal(String itemPrice, String itemQuantity) {
        try {
            int price = Integer.parseInt(itemPrice);
            int quantity = Integer.parseInt(itemQuantity);
            int subtotal = price * quantity;
            return String.valueOf(subtotal);
        } catch (NumberFormatException e) {
            return "Invalid price or quantity";
        }
    }

    public String getItemName() {
        return itemName;
    }

    public String getExpectedSubtotal() {
        return expectedSubtotal;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public String getItemPrice() {
        return itemPrice;
    }
}
