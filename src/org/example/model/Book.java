package org.example.model;

public class Book {
    private int bookId;
    private String bookName;
    private String authorName;
    private String publisher;
    private double price;
    private int quantity;

    public Book() {}

    public Book(int bookId, String bookName, String authorName, String publisher, double price, int quantity) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.publisher = publisher;
        this.price = price;
        this.quantity = quantity;
    }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return String.format("%-6d %-28s %-22s %-18s %10.2f %8d",
                bookId, bookName, authorName, publisher == null ? "" : publisher, price, quantity);
    }
}
