package org.example;

import org.example.operations.*;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            BookOperation books = new BookOperation(sc);
            MemberOperation members = new MemberOperation(sc);
            IssueBookOperation issue = new IssueBookOperation(sc);
            ReturnBookOperation returns = new ReturnBookOperation(sc);
            DisplayOperation display = new DisplayOperation();

            boolean running = true;
            while (running) {
                System.out.println("\n========== LIBRARY MANAGEMENT SYSTEM ==========");
                System.out.println("1. add Book");
                System.out.println("2. Member Operations");
                System.out.println("3. Issue Book");
                System.out.println("4. Return Book");
                System.out.println("5. Display Issued Books");
                System.out.println("6. Display Available Books");
                System.out.println("7. Display Returned Books");
                System.out.println("8. Exit");
                int choice = readChoice(sc, "Enter choice: ");
                switch (choice) {
                    case 1 -> bookMenu(sc, books);
                    case 2 -> memberMenu(sc, members);
                    case 3 -> issue.issueBook();
                    case 4 -> returns.returnBook();
                    case 5 -> display.displayIssuedBooks();
                    case 6 -> display.displayAvailableBooks();
                    case 7 -> display.displayReturnedBooks();
                    case 8 -> { running = false; System.out.println("Thank you for using the Library Management System."); }
                    default -> System.out.println("Invalid choice. Select 1-8.");
                }
            }
        }
    }

    private static void bookMenu(Scanner sc, BookOperation books) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- BOOK OPERATIONS ---");
            System.out.println("1. Add Book\n2. Display Books\n3. Search Book\n4. Update Book\n5. Delete Book\n6. Back");
            switch (readChoice(sc, "Enter choice: ")) {
                case 1 -> books.addBook();
                case 2 -> books.displayBooks();
                case 3 -> books.searchBook();
                case 4 -> books.updateBook();
                case 5 -> books.deleteBook();
                case 6 -> back = true;
                default -> System.out.println("Invalid choice. Select 1-6.");
            }
        }
    }

    private static void memberMenu(Scanner sc, MemberOperation members) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- MEMBER OPERATIONS ---");
            System.out.println("1. Add Member\n2. Display Members\n3. Search Member\n4. Update Member\n5. Delete Member\n6. Back");
            switch (readChoice(sc, "Enter choice: ")) {
                case 1 -> members.addMember();
                case 2 -> members.displayMembers();
                case 3 -> members.searchMember();
                case 4 -> members.updateMember();
                case 5 -> members.deleteMember();
                case 6 -> back = true;
                default -> System.out.println("Invalid choice. Select 1-6.");
            }
        }
    }

    private static int readChoice(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid number."); }
        }
    }
}
