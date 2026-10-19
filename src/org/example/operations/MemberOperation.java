package org.example.operations;

import org.example.connection.MySQLConnection;
import org.example.model.Member;

import java.sql.*;
import java.util.Scanner;

public class MemberOperation {
    private final Scanner sc;
    public MemberOperation(Scanner sc) { this.sc = sc; }

    public void addMember() {
        String name = readText("Member name: ");
        String email = readText("Email (optional): ");
        String mobile = readText("Mobile (optional): ");
        String address = readText("Address (optional): ");
        if (!email.isBlank() && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            System.out.println("Invalid email format."); return;
        }
        String sql = "INSERT INTO members(member_name,email,mobile,address) VALUES(?,?,?,?)";
        try (Connection con = MySQLConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name); ps.setString(2, email.isBlank() ? null : email);
            ps.setString(3, mobile.isBlank() ? null : mobile); ps.setString(4, address.isBlank() ? null : address);
            ps.executeUpdate(); System.out.println("Member added successfully.");
        } catch (SQLException e) { showError(e); }
    }

    public void displayMembers() {
        String sql = "SELECT * FROM members ORDER BY member_id";
        try (Connection con = MySQLConnection.getConnection(); Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.printf("%-6s %-24s %-30s %-16s %s%n", "ID", "Name", "Email", "Mobile", "Address");
            while (rs.next()) System.out.println(new Member(rs.getInt("member_id"), rs.getString("member_name"),
                    rs.getString("email"), rs.getString("mobile"), rs.getString("address")));
        } catch (SQLException e) { showError(e); }
    }

    public void searchMember() {
        System.out.print("Enter member ID or part of member name: ");
        String term = sc.nextLine().trim();
        String sql = "SELECT * FROM members WHERE CAST(member_id AS CHAR)=? OR member_name LIKE ?";
        try (Connection con = MySQLConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, term); ps.setString(2, "%" + term + "%");
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.println(new Member(rs.getInt("member_id"), rs.getString("member_name"),
                            rs.getString("email"), rs.getString("mobile"), rs.getString("address")));
                }
                if (!found) System.out.println("No matching member found.");
            }
        } catch (SQLException e) { showError(e); }
    }

    public void updateMember() {
        int id = readInt("Member ID to update: ");
        String name = readText("New member name: ");
        String email = readText("New email (optional): ");
        String mobile = readText("New mobile (optional): ");
        String address = readText("New address (optional): ");
        if (!email.isBlank() && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            System.out.println("Invalid email format."); return;
        }
        String sql = "UPDATE members SET member_name=?,email=?,mobile=?,address=? WHERE member_id=?";
        try (Connection con = MySQLConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name); ps.setString(2, email.isBlank() ? null : email);
            ps.setString(3, mobile.isBlank() ? null : mobile); ps.setString(4, address.isBlank() ? null : address);
            ps.setInt(5, id);
            System.out.println(ps.executeUpdate() > 0 ? "Member updated successfully." : "Member ID not found.");
        } catch (SQLException e) { showError(e); }
    }

    public void deleteMember() {
        int id = readInt("Member ID to delete: ");
        String sql = "DELETE FROM members WHERE member_id=? AND NOT EXISTS " +
                "(SELECT 1 FROM issue_book WHERE member_id=? AND status='ISSUED')";
        try (Connection con = MySQLConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id); ps.setInt(2, id);
            System.out.println(ps.executeUpdate() > 0 ? "Member deleted successfully." :
                    "Member not deleted. It may not exist or have an active issue.");
        } catch (SQLException e) { showError(e); }
    }

    private String readText(String prompt) { System.out.print(prompt); return sc.nextLine().trim(); }
    private int readInt(String prompt) {
        while (true) { System.out.print(prompt); try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid whole number."); } }
    }
    private void showError(SQLException e) {
        if (e.getErrorCode() == 1062) System.out.println("That email already exists.");
        else System.out.println("Database error: " + e.getMessage());
    }
}
