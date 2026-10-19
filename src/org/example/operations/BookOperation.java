package org.example.operations;

import org.example.connection.MySQLConnection;
import org.example.model.Book;

import java.sql.*;
import java.util.Scanner;

public class BookOperation {
    private final Scanner sc;

    public BookOperation(Scanner sc) { this.sc = sc; }

    public void addBook() {
        String name = readText("Book name: ");
        String author = readText("Author name: ");
        String publisher = readText("Publisher (optional): ");
        double price = readDouble("Price: ");
        int quantity = readInt("Quantity: ");
        if (price < 0 || quantity < 0) {
            System.out.println("Price and quantity cannot be negative.");
            return;
        }
        String sql = "INSERT INTO books(book_name, author_name, publisher, price, quantity) VALUES(?,?,?,?,?)";
        try (Connection con = MySQLConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, author);
            ps.setString(3, publisher.isBlank() ? null : publisher);
            ps.setDouble(4, price);
            ps.setInt(5, quantity);
            ps.executeUpdate();
            System.out.println("Book added successfully.");
        } catch (SQLException e) { showError(e); }
    }

    public void displayBooks() {
        String sql = "SELECT book_id, book_name, author_name, publisher, price, quantity FROM books ORDER BY book_id";
        try (Connection con = MySQLConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.printf("%-6s %-28s %-22s %-18s %10s %8s%n", "ID", "Book", "Author", "Publisher", "Price", "Qty");
            while (rs.next()) {
                System.out.println(new Book(rs.getInt("book_id"), rs.getString("book_name"),
                        rs.getString("author_name"), rs.getString("publisher"),
                        rs.getDouble("price"), rs.getInt("quantity")));
            }
        } catch (SQLException e) { showError(e); }
    }

    public void searchBook() {
        System.out.print("Enter book ID or part of book name: ");
        String term = sc.nextLine().trim();
        String sql = "SELECT * FROM books WHERE CAST(book_id AS CHAR)=? OR book_name LIKE ?";
        try (Connection con = MySQLConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, term);
            ps.setString(2, "%" + term + "%");
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.println(new Book(rs.getInt("book_id"), rs.getString("book_name"),
                            rs.getString("author_name"), rs.getString("publisher"),
                            rs.getDouble("price"), rs.getInt("quantity")));
                }
                if (!found) System.out.println("No matching book found.");
            }
        } catch (SQLException e) { showError(e); }
    }

    public void updateBook() {
        int id = readInt("Book ID to update: ");
        String name = readText("New book name: ");
        String author = readText("New author name: ");
        String publisher = readText("New publisher (optional): ");
        double price = readDouble("New price: ");
        int quantity = readInt("New quantity: ");
        if (price < 0 || quantity < 0) { System.out.println("Price and quantity cannot be negative."); return; }
        String sql = "UPDATE books SET book_name=?, author_name=?, publisher=?, price=?, quantity=? WHERE book_id=?";
        try (Connection con = MySQLConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name); ps.setString(2, author);
            ps.setString(3, publisher.isBlank() ? null : publisher);
            ps.setDouble(4, price); ps.setInt(5, quantity); ps.setInt(6, id);
            int count = ps.executeUpdate();
            System.out.println(count > 0 ? "Book updated successfully." : "Book ID not found.");
        } catch (SQLException e) { showError(e); }
    }

    public void deleteBook() {
        int id = readInt("Book ID to delete: ");
        String sql = "DELETE FROM books WHERE book_id=? AND NOT EXISTS " +
                "(SELECT 1 FROM issue_book WHERE book_id=? AND status='ISSUED')";
        try (Connection con = MySQLConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id); ps.setInt(2, id);
            int count = ps.executeUpdate();
            System.out.println(count > 0 ? "Book deleted successfully." :
                    "Book not deleted. It may not exist or currently be issued.");
        } catch (SQLException e) { showError(e); }
    }

    private String readText(String prompt) { System.out.print(prompt); return sc.nextLine().trim(); }
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid whole number."); }
        }
    }
    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Double.parseDouble(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid number."); }
        }
    }
    private void showError(SQLException e) {
        System.out.println("Database error: " + e.getMessage());
    }
}
