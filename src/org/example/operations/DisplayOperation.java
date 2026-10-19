package org.example.operations;

import org.example.connection.MySQLConnection;
import java.sql.*;

public class DisplayOperation {
    public void displayIssuedBooks() { displayByStatus("ISSUED", "Issued Books"); }
    public void displayReturnedBooks() { displayByStatus("RETURNED", "Returned Books"); }

    public void displayAvailableBooks() {
        String sql = "SELECT book_id,book_name,author_name,quantity FROM books WHERE quantity>0 ORDER BY book_name";
        try (Connection con = MySQLConnection.getConnection(); Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nID | Book | Author | Available Quantity");
            while (rs.next()) System.out.printf("%d | %s | %s | %d%n", rs.getInt("book_id"),
                    rs.getString("book_name"), rs.getString("author_name"), rs.getInt("quantity"));
        } catch (SQLException e) { System.out.println("Database error: " + e.getMessage()); }
    }

    private void displayByStatus(String status, String heading) {
        String sql = "SELECT i.issue_id,b.book_name,m.member_name,i.issue_date,i.return_date,i.status " +
                "FROM issue_book i JOIN books b ON i.book_id=b.book_id " +
                "JOIN members m ON i.member_id=m.member_id WHERE i.status=? ORDER BY i.issue_id";
        try (Connection con = MySQLConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("\n" + heading);
                System.out.printf("%-8s %-28s %-24s %-14s %-14s %-10s%n",
                        "Issue ID", "Book", "Member", "Issue Date", "Return Date", "Status");
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf("%-8d %-28s %-24s %-14s %-14s %-10s%n",
                            rs.getInt("issue_id"), rs.getString("book_name"), rs.getString("member_name"),
                            rs.getDate("issue_date"), rs.getDate("return_date"), rs.getString("status"));
                }
                if (!found) System.out.println("No records found.");
            }
        } catch (SQLException e) { System.out.println("Database error: " + e.getMessage()); }
    }
}
