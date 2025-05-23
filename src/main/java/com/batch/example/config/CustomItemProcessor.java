package com.batch.example.config;

import com.batch.example.model.Product;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CustomItemProcessor implements ItemProcessor<Product, Product> {
    @Override
    public Product process(Product item) {
        try {
            System.out.println("Processing product: " + item.getProductId());
            System.out.println("Raw discount: '" + item.getDiscount() + "'");
            System.out.println("Raw price: '" + item.getPrice() + "'");

            int discountPer = 0;
            if (item.getDiscount() != null && !item.getDiscount().trim().isEmpty()) {
                discountPer = Integer.parseInt(item.getDiscount().trim());
            }

            BigDecimal originalPrice = new BigDecimal(item.getPrice().trim());
            BigDecimal discountPercent = new BigDecimal(discountPer).divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            BigDecimal discountAmount = originalPrice.multiply(discountPercent);
            BigDecimal finalPrice = originalPrice.subtract(discountAmount);

            item.setDiscountedPrice(finalPrice.setScale(2, RoundingMode.HALF_UP).toString());

            System.out.println("Discount amount: " + discountAmount);
            System.out.println("Final discounted price: " + item.getDiscountedPrice());

        } catch (NumberFormatException ex) {
            System.err.println("Error parsing price or discount for productId: " + item.getProductId());
            ex.printStackTrace();
        }

        return item;
    }

}