package synchronize;

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

    public static class InventoryCounter {
        private int items = 0;

        private final Object lock = new Object();

        public void increment() {

            synchronized (lock) {
                items++;
            }

        }

        public void decrement() {
            synchronized (lock) {
                items--;
            }
        }

        public int getItem() {
            return items;
        }

    }

}
