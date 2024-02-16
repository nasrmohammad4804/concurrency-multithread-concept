package synchronize;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Product product = new Product("shirt",new Product.InventoryCounter());

        Thread deliveryProductThread = new Thread(new ProductDelivery(product));
        Thread purchaseProductThread = new Thread(new ProductPurchasing(product));

        deliveryProductThread.start();
        purchaseProductThread.start();

        deliveryProductThread.join();


        purchaseProductThread.join();

        System.out.println(product.getInventoryCounter().getItem());
    }

}
