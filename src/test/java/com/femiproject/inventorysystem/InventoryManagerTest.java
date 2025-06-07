package com.femiproject.inventorysystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventoryManagerTest {

    @Mock
    private InventoryInterface<Product> mockInterface;

    private InventoryManager inventoryManager;
    private Product testProduct;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(mockInterface.load()).thenReturn(new ArrayList<>());
        inventoryManager = new InventoryManager(mockInterface);

        testProduct = new Product();
        testProduct.setId("TEST001");
        testProduct.setName("Test Product");
        testProduct.setPrice(25.0);
        testProduct.setQuantity(10);
        testProduct.setMinThreshold(5);
    }

    @Test
    void testAddProduct() throws Exception {
        inventoryManager.addProducts(testProduct);
        verify(mockInterface).save(anyList());
        assertNotNull(inventoryManager.findProductById("TEST001"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        inventoryManager.addProducts(testProduct);
        testProduct.setPrice(30.0);
        assertTrue(inventoryManager.updateProduct(testProduct));
        Product updated = inventoryManager.findProductById("TEST001");
        assertEquals(30.0, updated.getPrice());
    }

    @Test
    void testDeleteProduct() throws Exception {
        inventoryManager.addProducts(testProduct);
        assertTrue(inventoryManager.deleteProduct("TEST001"));
        assertNull(inventoryManager.findProductById("TEST001"));
    }

    @Test
    void testGetProductsUnderPrice() throws Exception {
        inventoryManager.addProducts(testProduct);
        List<Product> cheapProducts = inventoryManager.getProductsUnderPrice(50.0);
        assertEquals(1, cheapProducts.size());
        assertEquals("TEST001", cheapProducts.get(0).getId());
    }

    @Test
    void testSearchProducts() throws Exception {
        inventoryManager.addProducts(testProduct);
        List<Product> results = inventoryManager.searchProducts("Test");
        assertEquals(1, results.size());
        assertEquals("TEST001", results.get(0).getId());
    }

    @Test
    void testRestockProduct() throws Exception {
        inventoryManager.addProducts(testProduct);
        assertTrue(inventoryManager.restockProduct("TEST001", 5));
        Product restocked = inventoryManager.findProductById("TEST001");
        assertEquals(15, restocked.getQuantity());
    }

    @Test
    void testGetLowStockProducts() throws Exception {
        testProduct.setQuantity(3);
        inventoryManager.addProducts(testProduct);
        List<Product> lowStock = inventoryManager.getLowStockProducts();
        assertEquals(1, lowStock.size());
        assertEquals("TEST001", lowStock.get(0).getId());
    }

    @Test
    void testGetTotalInventoryValue() throws Exception {
        inventoryManager.addProducts(testProduct);
        double total = inventoryManager.getTotalInventoryValue();
        assertEquals(250.0, total); // 25.0 * 10
    }
}
