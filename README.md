# MXH mini update thêm thuật toán hiển thị feed
Dự án này là một phiên bản nâng cấp của ứng dụng mạng xã hội cơ bản, tập trung vào việc tối ưu hóa trải nghiệm người dùng (UX) bằng các công nghệ hiện đại như **HTMX**, **Alpine.js** và thuật toán xếp hạng nội dung thông minh.

## Các Tính Năng Đã Cập Nhật

### 1. Thuật Toán Xếp Hạng Feed (Feed Ranking)
Sử dụng thuật toán tính điểm ưu tiên (`rank_score`) để đưa những nội dung phù hợp nhất lên đầu:
- **Ưu tiên theo dõi (+10đ):** Bài viết từ những người bạn đang follow.
- **Tương tác cá nhân (Tối đa +20đ):** Dựa trên lịch sử tương tác giữa bạn và tác giả.
- **Độ phổ biến (+2đ/like):** Bài viết có nhiều lượt yêu thích từ cộng đồng.
- **Độ tươi mới (-1đ/ngày):** Tự động giảm hạng các bài viết cũ theo thời gian.

### 2. Cuộn Vô Hạn (Infinite Scroll)
Sử dụng **HTMX** để tải dữ liệu theo từng đợt (Batch loading):
- Trang chủ ban đầu chỉ tải 5 bài viết để tối ưu tốc độ.
- Khi người dùng cuộn xuống cuối, HTMX sẽ tự động gọi server để lấy các bài tiếp theo và chèn vào trang mà không cần load lại.

### 3. Tương Tác Một Chạm (AJAX via HTMX)
- **Nút Thích (Like):** Cập nhật trạng thái và số lượng like tức thì, sử dụng icon trái tim (FontAwesome).
- **Theo dõi (Follow):** Cho phép theo dõi người dùng ngay tại thanh Sidebar "Gợi ý", cập nhật trạng thái "Đã theo dõi" mượt mà.

### 4. Giao Diện (UI/UX) Hiện Đại
- Thiết kế layout 3 cột lấy cảm hứng từ X (Twitter).
- Sidebar gợi ý người dùng với avatar tự động tạo từ tên.
- Sử dụng **Alpine.js** để xử lý các logic frontend nhẹ nhàng.

---

## Thay Đổi Cấu Trúc Database

Đã chủ động bổ sung 2 bảng quan trọng để phục vụ logic ranking và tương tác:

### 1. Bảng `post_likes`
Quản lý việc người dùng yêu thích các bài viết.
- `post_id`: ID bài viết.
- `user_id`: ID người thích.
- **Unique Constraint:** Đảm bảo mỗi người dùng chỉ có thể thích một bài viết một lần.

### 2. Bảng `interactions`
Lưu vết mọi hành động của người dùng để tính toán mức độ thân thiết giữa các tài khoản.
- `actor_id`: Người thực hiện (User).
- `target_user_id`: Người nhận tác động (Tác giả).
- `target_post_id`: Bài viết liên quan.
- `type`: Loại tương tác (`like_post`, `view_profile`, ...).

---

## 🛠 Cấu Trúc Mã Nguồn Mới

- **`PostDTO.java`:** Data Transfer Object mở rộng từ `Post` để chứa thêm thông tin về `likeCount`, `isLikedByMe` và `rankScore`.
- **`fragments/post_list.jsp`:** Fragment JSP chứa mã nguồn render danh sách bài viết, giúp tái sử dụng cho cả lần load đầu và lazy load.
- **`SampleDataGenerator.java`:** Công cụ tự động tạo 20 users, 100 bài viết và hàng trăm tương tác mẫu để kiểm thử hệ thống.
- **`LikeServlet.java` & `FollowServlet.java`:** Các endpoint xử lý AJAX trả về fragment HTML cho HTMX.

---
