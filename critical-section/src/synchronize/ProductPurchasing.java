package synchronize;

import java.util.stream.IntStream;

public class ProductPurchasing implements Runnable{

    private Product product;


    public ProductPurchasing(Product product) {
        this.product = product;
    }

    @Override
    public void run() {

        IntStream
                .range(0,10000)
                .forEach(index -> product.getInventoryCounter().decrement());

    }
}
