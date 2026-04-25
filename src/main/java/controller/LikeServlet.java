package controller;

import dao.PostLikeDAO;
import dao.InteractionDAO;
import model.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/like")
public class LikeServlet extends HttpServlet {
    private PostLikeDAO postLikeDAO;
    private InteractionDAO interactionDAO;

    @Override
    public void init() {
        postLikeDAO = new PostLikeDAO();
        interactionDAO = new InteractionDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.setStatus(401);
            return;
        }

        String postIdStr = request.getParameter("postId");
        if (postIdStr != null) {
            int postId = Integer.parseInt(postIdStr);
            int userId = user.getId();

            boolean isLiked = postLikeDAO.isLiked(postId, userId);
            if (isLiked) {
                postLikeDAO.removeLike(postId, userId);
            } else {
                postLikeDAO.addLike(postId, userId);
                // Log interaction: only for 'add' like
                // We need to find authorId to log interaction properly if we want targetUserId
                // For now, let's just log targetPostId
                interactionDAO.addInteraction(userId, null, postId, "like_post");
            }

            int newLikeCount = postLikeDAO.getLikeCount(postId);
            boolean nowLiked = !isLiked;

            // Return HTML fragment for the like button
            response.setContentType("text/html;charset=UTF-8");
            String iconClass = nowLiked ? "fas" : "far";
            String colorStyle = nowLiked ? "color: #ef4444;" : "";
            
            response.getWriter().write(
                "<button class=\"action-btn like-btn\" " +
                "hx-post=\"like?postId=" + postId + "\" " +
                "hx-swap=\"outerHTML\" " +
                "style=\"" + colorStyle + "\">" +
                "<i class=\"" + iconClass + " fa-heart\"></i> " +
                "<span>" + newLikeCount + "</span> Thích" +
                "</button>"
            );
        }
    }
}
