import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        UserAuthentication auth = new UserAuthentication();
        
        if (!auth.authenticate()) {
            System.out.println("Authentication failed. Exiting...");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\nInventory Management System");
            System.out.println("1. Add Product");
            System.out.println("2. Remove Product");
            System.out.println("3. Search Product");
            System.out.println("4. Update Product");
            System.out.println("5. Low Stock Alert");
            System.out.println("6. View all products");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Product ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Product Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Quantity: ");
                    int quantity = scanner.nextInt();
                    System.out.print("Enter Price: ");
                    double price = scanner.nextDouble();
                    inventory.addProduct(new Product(id, name, quantity, price));
                    System.out.println("Product added successfully.");
                    break;
                case 2:
                    System.out.print("Enter Product ID to remove: ");
                    int removeId = scanner.nextInt();
                    inventory.removeProduct(removeId);
                    System.out.println("Product removed successfully.");
                    break;
                case 3:
                    System.out.print("Enter Product ID to search: ");
                    int searchId = scanner.nextInt();
                    Product product = inventory.searchProduct(searchId);
                    System.out.println(product != null ? product : "Product not found.");
                    break;
                case 4:
                    System.out.print("Enter Product ID to update: ");
                    int updateId = scanner.nextInt();
                    System.out.print("Enter new Quantity: ");
                    int newQuantity = scanner.nextInt();
                    System.out.print("Enter new Price: ");
                    double newPrice = scanner.nextDouble();
                    inventory.updateProduct(updateId, newQuantity, newPrice);
                    System.out.println("Product updated successfully.");
                    break;
                case 5:
                    System.out.print("Enter low stock threshold: ");
                    int threshold = scanner.nextInt();
                    inventory.lowStockAlert(threshold);
                    break;

                case 6:
                    inventory.generateReport();
                    break;
                case 7:
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}
