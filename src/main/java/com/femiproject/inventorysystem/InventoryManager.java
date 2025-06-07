package com.femiproject.inventorysystem;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {

    private List<Product> products;
    private final InventoryInterface<Product> productInterface;

    public InventoryManager(InventoryInterface<Product> product) throws Exception {
        this.productInterface = product;
        this.products = productInterface.load();
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
    }

    public Product findProductById(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addProducts(Product product) throws Exception {
        products.add(product);
        productInterface.save(products);
    }

    public boolean deleteProduct(String id) throws Exception {
        Product product = findProductById(id);

        if (product != null) {
            products.remove(product);
            productInterface.save(products);
            return true;
        }
        return false;
    }

    public boolean updateProduct(Product p) {
        Product product = findProductById(p.getId());

        if (product == null) {
            return false;
        }

        if (p.getName() != null) {
            product.setName(p.getName());
        }
        product.setPrice(p.getPrice());
        product.setQuantity(p.getQuantity());

        try {
            productInterface.save(products);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Product> listAllProducts() {
        return new ArrayList<>(products);
    }

    public double getTotalInventoryValue() {
        return products.stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();
    }

    public boolean stockCheck(Product p) {
        Product product = findProductById(p.getId());

        if (product == null) {
            return false;
        }

        return product.getQuantity() < product.getMinThreshold();
    }

    public List<Product> getProductsUnderPrice(double price) {
        return products.stream()
                .filter(p -> p.getPrice() < price)
                .toList();
    }

    public List<Product> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String searchQuery = query.toLowerCase().trim();
        return products.stream()
                .filter(product -> (product.getId() != null && product.getId().toLowerCase().contains(searchQuery)) ||
                        (product.getName() != null && product.getName().toLowerCase().contains(searchQuery)) ||
                        (String.valueOf(product.getPrice()).contains(searchQuery)) ||
                        (String.valueOf(product.getQuantity()).contains(searchQuery)))
                .toList();
    }

    public boolean restockProduct(String id, int amount) throws Exception {
        if (amount <= 0)
            return false;
        Product p = findProductById(id);
        if (p == null)
            return false;
        p.setQuantity(p.getQuantity() + amount);
        productInterface.save(products);
        return true;
    }

    public void addMultipleProducts(List<Product> newProducts) throws Exception {
        products.addAll(newProducts);
        productInterface.save(products);
    }

    public List<Product> getLowStockProducts() {
        return products.stream()
                .filter(p -> p.getQuantity() < p.getMinThreshold())
                .toList();
    }

}
