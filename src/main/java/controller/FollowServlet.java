package controller;
import dao.FollowDAO;
import model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/follow")
public class FollowServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int followedUserId = Integer.parseInt(request.getParameter("userId"));
        
        if (user != null) {
            new FollowDAO().follow(user.getId(), followedUserId);
        }

        String htmxRequest = request.getHeader("HX-Request");
        if ("true".equals(htmxRequest)) {
            response.setContentType("text/html");
            response.getWriter().write("<span class='followed-badge'><i class='fas fa-check'></i> Đã theo dõi</span>");
        } else {
            response.sendRedirect("users");
        }
    }
}