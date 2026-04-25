<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, model.PostDTO" %>

<%
    List<PostDTO> posts = (List<PostDTO>) request.getAttribute("posts");
    Integer nextOffset = (Integer) request.getAttribute("nextOffset");
    Boolean hasMore = (Boolean) request.getAttribute("hasMore");
    if (posts != null) {
        for (int i = 0; i < posts.size(); i++) {
            PostDTO post = posts.get(i);
            boolean isLast = (i == posts.size() - 1);
%>
<div class="post" 
     <% if (isLast && hasMore != null && hasMore) { %>
     hx-get="posts?offset=<%= nextOffset %>"
     hx-trigger="revealed"
     hx-swap="afterend"
     <% } %>
>
    <div class="content">
        <h4><%= post.getTitle() %></h4>

        <div class="meta">
            <span><i class="fas fa-user"></i> <%= post.getAuthorName() %></span>
            <span><i class="far fa-clock"></i> <%= post.getCreatedAt() %></span>

            <% if ("published".equals(post.getStatus())) { %>
                <span class="status-badge status-published-badge">
                    <i class="fas fa-globe"></i> Công khai
                </span>
            <% } else { %>
                <span class="status-badge status-draft-badge">
                    <i class="fas fa-lock"></i> Bản nháp
                </span>
            <% } %>
        </div>

        <div class="post-body">
            <%= post.getBody() %>
        </div>

        <div class="post-actions">
            <button class="action-btn like-btn" 
                    hx-post="like?postId=<%= post.getId() %>" 
                    hx-swap="outerHTML"
                    <%= post.isLikedByMe() ? "style='color: #ef4444;'" : "" %>>
                <i class="<%= post.isLikedByMe() ? "fas" : "far" %> fa-heart"></i>
                <span><%= post.getLikeCount() %></span> Thích
            </button>
        </div>
    </div>
</div>
<%
        }
    }
%>
