package servlets;

import database.PostgreSQLManager;
import entities.Post;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/main")
public class MainServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        try {
            PostgreSQLManager pm = new PostgreSQLManager();
            String username = request.getParameter("username");
            String text = request.getParameter("text");
            if ((username != null && !username.isEmpty())
                    && (text != null && !text.isEmpty())) {
                String usernameString = new String(
                        request.getParameter("username")
                                .getBytes("ISO-8859-1"), "cp1251");
                String textString = new String(
                        request.getParameter("text")
                                .getBytes("ISO-8859-1"), "cp1251");
                pm.insertIntoPosts(usernameString, textString);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("cp1251");
        response.setContentType("text/html; charset=cp1251");

        List<Post> posts = null;
        int page = request.getParameter("page") == null ? 1 :
                Integer.parseInt(request.getParameter("page"));
        int postNumber = 0;
        int postsPerPage = 5;
        try {
            PostgreSQLManager pm = new PostgreSQLManager();
            postNumber = pm.countPosts();
            posts = pm.selectPosts(page, postsPerPage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.setAttribute("posts", posts);
        request.setAttribute("postNumber", postNumber);
        request.setAttribute("postsPerPage", postsPerPage);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp?page=" + page);
        dispatcher.forward(request, response);
    }
}