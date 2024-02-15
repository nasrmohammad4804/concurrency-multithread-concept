package memory.management.resourcesharing;

public class Product {

    private String name;
    private InventoryCounter inventoryCounter;

    public Product(String name, InventoryCounter inventoryCounter) {
        this.name = name;
        this.inventoryCounter = inventoryCounter;
    }

    public String getName() {
        return name;
    }

    public InventoryCounter getInventoryCounter() {
        return inventoryCounter;
    }

    public static class InventoryCounter{
        private int items=0;

        public void increment(){
            items++;
        }
        public void decrement(){
            items--;
        }
        public int getItem(){
            return items;
        }

    }

}
