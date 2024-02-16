package synchronize;

import java.util.stream.IntStream;

public class ProductDelivery implements Runnable{

    private Product product;

    public ProductDelivery(Product product) {
        this.product = product;
    }

    @Override
    public void run() {

        IntStream
                .range(0,10000)
                .forEach(index -> product.getInventoryCounter().increment());

    }
}
