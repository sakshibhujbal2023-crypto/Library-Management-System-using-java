package org.example.operations;

import org.example.connection.MySQLConnection;
import java.sql.*;
import java.util.Scanner;

public class IssueBookOperation {
    private final Scanner sc;
    public IssueBookOperation(Scanner sc) { this.sc = sc; }

    public void issueBook() {
        int bookId = readInt("Book ID: ");
        int memberId = readInt("Member ID: ");
        String checkBook = "SELECT quantity FROM books WHERE book_id=? FOR UPDATE";
        String checkMember = "SELECT member_id FROM members WHERE member_id=?";
        String updateBook = "UPDATE books SET quantity=quantity-1 WHERE book_id=? AND quantity>0";
        String insertIssue = "INSERT INTO issue_book(book_id,member_id,issue_date,status) VALUES(?,?,CURRENT_DATE,'ISSUED')";
        try (Connection con = MySQLConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(checkBook)) {
                    ps.setInt(1, bookId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalArgumentException("Book ID does not exist.");
                        if (rs.getInt("quantity") < 1) throw new IllegalArgumentException("No copies are available.");
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(checkMember)) {
                    ps.setInt(1, memberId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalArgumentException("Member ID does not exist.");
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(updateBook)) {
                    ps.setInt(1, bookId);
                    if (ps.executeUpdate() != 1) throw new IllegalArgumentException("No copies are available.");
                }
                try (PreparedStatement ps = con.prepareStatement(insertIssue)) {
                    ps.setInt(1, bookId); ps.setInt(2, memberId); ps.executeUpdate();
                }
                con.commit();
                System.out.println("Book issued successfully.");
            } catch (Exception e) {
                con.rollback();
                System.out.println("Issue failed: " + e.getMessage());
            } finally { con.setAutoCommit(true); }
        } catch (SQLException e) { System.out.println("Database error: " + e.getMessage()); }
    }

    private int readInt(String prompt) {
        while (true) { System.out.print(prompt); try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid whole number."); } }
    }
}
