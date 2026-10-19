CREATE DATABASE IF NOT EXISTS library_management
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE library_management;

CREATE TABLE IF NOT EXISTS books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    book_name VARCHAR(150) NOT NULL,
    author_name VARCHAR(100) NOT NULL,
    publisher VARCHAR(100),
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    quantity INT NOT NULL DEFAULT 0,
    CONSTRAINT chk_book_price CHECK (price >= 0),
    CONSTRAINT chk_book_quantity CHECK (quantity >= 0),
    INDEX idx_book_name (book_name)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS members (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    member_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    mobile VARCHAR(15),
    address VARCHAR(255),
    INDEX idx_member_name (member_name)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS issue_book (
    issue_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    member_id INT NOT NULL,
    issue_date DATE NOT NULL,
    return_date DATE NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ISSUED',
    CONSTRAINT fk_issue_book FOREIGN KEY (book_id) REFERENCES books(book_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_issue_member FOREIGN KEY (member_id) REFERENCES members(member_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT chk_issue_status CHECK (status IN ('ISSUED','RETURNED')),
    INDEX idx_issue_status (status),
    INDEX idx_issue_book (book_id),
    INDEX idx_issue_member (member_id)
) ENGINE=InnoDB;

-- Optional sample data
INSERT INTO books(book_name, author_name, publisher, price, quantity)
VALUES ('Java Programming', 'Herbert Schildt', 'McGraw Hill', 650.00, 5),
       ('Database System Concepts', 'Abraham Silberschatz', 'McGraw Hill', 750.00, 3);

INSERT INTO members(member_name, email, mobile, address)
VALUES ('Sample Student', 'student@example.com', '9876543210', 'Pune');
