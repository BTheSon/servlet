-- 1. Tạo Database nếu chưa tồn tại
USE employees;

-- 2. Tạo bảng users (thêm IF NOT EXISTS để tránh lỗi khi khởi động lại server)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE, -- Thêm UNIQUE để tránh trùng lặp username
    password VARCHAR(100),
    role VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
USE employees;
-- 3. Tạo bảng posts
CREATE TABLE IF NOT EXISTS posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    body TEXT,
    user_id INT,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
USE employees;
-- 4. Tạo bảng follows
CREATE TABLE IF NOT EXISTS follows (
    following_user_id INT,
    followed_user_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (following_user_id, followed_user_id), -- Khóa chính tổ hợp
    FOREIGN KEY (following_user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (followed_user_id) REFERENCES users(id) ON DELETE CASCADE
);

USE employees;

CREATE TABLE IF NOT EXISTS post_likes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_like (post_id, user_id),          -- Mỗi user chỉ like 1 lần
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

USE employees;

-- bảng tương tác giữa người dùng
CREATE TABLE IF NOT EXISTS interactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    actor_id INT NOT NULL,           -- Người thực hiện hành động
    target_user_id INT,              -- Người bị tác động (nullable nếu target là post)
    target_post_id INT,              -- Bài viết liên quan (nullable)
    type ENUM(
        'like_post',
        'view_profile'
    ) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (actor_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (target_user_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (target_post_id) REFERENCES posts(id) ON DELETE SET NULL
);

USE employees;

-- 5. Chèn dữ liệu mẫu (Sử dụng INSERT IGNORE để không bị lỗi trùng lặp khi chạy lại)
INSERT IGNORE INTO users(id, username, password, role) VALUES
(1, 'admin', '123', 'admin'),
(2, 'user1', '123', 'user');
USE employees;
INSERT IGNORE INTO posts(id, title, body, user_id, status) VALUES
(1, 'Bài viết 1', 'Nội dung demo', 1, 'public'),
(2, 'Bài viết 2', 'Hello world', 2, 'public');