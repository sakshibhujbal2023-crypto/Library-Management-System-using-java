package org.example.operations;

import org.example.connection.MySQLConnection;
import java.sql.*;
import java.util.Scanner;

public class ReturnBookOperation {
    private final Scanner sc;
    public ReturnBookOperation(Scanner sc) { this.sc = sc; }

    public void returnBook() {
        int issueId;
        while (true) {
            System.out.print("Issue ID to return: ");
            try { issueId = Integer.parseInt(sc.nextLine().trim()); break; }
            catch (NumberFormatException e) { System.out.println("Enter a valid whole number."); }
        }
        try (Connection con = MySQLConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                int bookId;
                String find = "SELECT book_id FROM issue_book WHERE issue_id=? AND status='ISSUED' FOR UPDATE";
                try (PreparedStatement ps = con.prepareStatement(find)) {
                    ps.setInt(1, issueId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalArgumentException("Active issue not found; it may already be returned.");
                        bookId = rs.getInt("book_id");
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE issue_book SET status='RETURNED', return_date=CURRENT_DATE WHERE issue_id=? AND status='ISSUED'")) {
                    ps.setInt(1, issueId);
                    if (ps.executeUpdate() != 1) throw new IllegalArgumentException("Issue could not be updated.");
                }
                try (PreparedStatement ps = con.prepareStatement("UPDATE books SET quantity=quantity+1 WHERE book_id=?")) {
                    ps.setInt(1, bookId); ps.executeUpdate();
                }
                con.commit();
                System.out.println("Book returned successfully.");
            } catch (Exception e) {
                con.rollback();
                System.out.println("Return failed: " + e.getMessage());
            } finally { con.setAutoCommit(true); }
        } catch (SQLException e) { System.out.println("Database error: " + e.getMessage()); }
    }
}
