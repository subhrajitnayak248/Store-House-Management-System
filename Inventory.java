// import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
// import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Inventory {
    private CopyOnWriteArrayList<Product> products = new CopyOnWriteArrayList<>();
    private static final String FILE_NAME = "products.dat";
    
    public Inventory() {
        loadProducts();
    }

    // Add product with ACID properties
    public synchronized void addProduct(Product product) {
        products.add(product);
        products.sort(Comparator.comparingInt(Product::getId));
        saveProducts();
    }

    // Remove product with ACID properties
    public synchronized void removeProduct(int productId) {
        products.removeIf(product -> product.getId() == productId);
        saveProducts();
    }

    // Search product by ID using binary search
    public Product searchProduct(int productId) {
        int left = 0, right = products.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            Product midProduct = products.get(mid);
            if (midProduct.getId() == productId) return midProduct;
            if (midProduct.getId() < productId) left = mid + 1;
            else right = mid - 1;
        }
        return null;
    }

    // Update product with ACID properties
    public synchronized void updateProduct(int productId, int newQuantity, double newPrice) {
        Product product = searchProduct(productId);
        if (product != null) {
            product.setQuantity(newQuantity);
            product.setPrice(newPrice);
            saveProducts();
        }
    }

    // Low stock alert
    public void lowStockAlert(int threshold) {
        for (Product product : products) {
            if (product.getQuantity() < threshold) {
                System.out.println("Low stock alert for: " + product);
            }
        }
    }

    // Save products to file with exclusive lock
    private void saveProducts() {
        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw");
             FileChannel fileChannel = file.getChannel()) {
            try (FileLock lock = fileChannel.lock()) {
                ObjectOutputStream oos = new ObjectOutputStream(Channels.newOutputStream(fileChannel));
                oos.writeObject(new ArrayList<>(products));
            }
        } catch (IOException e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
    }

    // Load products from file with shared lock
    private void loadProducts() {
        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "r");
             FileChannel fileChannel = file.getChannel()) {
            try (FileLock lock = fileChannel.lock(0L, Long.MAX_VALUE, true)) {
                ObjectInputStream ois = new ObjectInputStream(Channels.newInputStream(fileChannel));
                products = new CopyOnWriteArrayList<>((List<Product>) ois.readObject());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Product file not found, starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
    }

    public void generateReport() {
        System.out.println("\n--- Inventory Report ---");
        System.out.printf("%-10s %-20s %-10s %-10s\n", "Product ID", "Product Name", "Quantity", "Price");
        System.out.println("---------------------------------------------------------------");
        for (Product product : products) {
            System.out.printf("%-10d %-20s %-10d %-10.2f\n", 
                              product.getId(), product.getName(), product.getQuantity(), product.getPrice());
        }
        System.out.println("---------------------------------------------------------------");
    }
    
}
