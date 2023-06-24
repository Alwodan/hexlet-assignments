package exercise.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import exercise.TemplateEngineUtil;

public class ArticlesServlet extends HttpServlet {

    private String getId(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            return null;
        }
        String[] pathParts = pathInfo.split("/");
        return ArrayUtils.get(pathParts, 1, null);
    }

    private String getAction(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            return "list";
        }
        String[] pathParts = pathInfo.split("/");
        return ArrayUtils.get(pathParts, 2, getId(request));
    }

    private Map<String, String> getArticleById(String id, Connection connection) throws SQLException {
        String query = "SELECT * FROM articles WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, id);
        ResultSet rs = statement.executeQuery();

        if (!rs.first()) {
            return null;
        }

        return Map.of(
            "id", id,
            "title", rs.getString("title"),
            "body", rs.getString("body")
        );
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

        String action = getAction(request);

        switch (action) {
            case "list":
                showArticles(request, response);
                break;
            case "new":
                newArticle(request, response);
                break;
            case "edit":
                editArticle(request, response);
                break;
            case "delete":
                deleteArticle(request, response);
                break;
            default:
                showArticle(request, response);
                break;
        }
    }

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

        String action = getAction(request);

        switch (action) {
            case "list":
                createArticle(request, response);
                break;
            case "edit":
                updateArticle(request, response);
                break;
            case "delete":
                destroyArticle(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showArticles(HttpServletRequest request,
                          HttpServletResponse response)
                throws IOException, ServletException {

        ServletContext context = request.getServletContext();
        Connection connection = (Connection) context.getAttribute("dbConnection");

        List<Map<String, String>> articles = new ArrayList<>();

        int articlesPerPage = 10;
        String page = request.getParameter("page");
        int normalizedPage = page == null ? 1 : Integer.parseInt(page);
        int offset = (normalizedPage - 1) * articlesPerPage;

        String query = "SELECT * FROM articles ORDER BY id LIMIT ? OFFSET ?";

        try {
            // Методы для работы с базой данных могут выбросить исключение,
            // которое нужно обработать
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, articlesPerPage);
            statement.setInt(2, offset);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                articles.add(Map.of(
                    "id", rs.getString("id"),
                    "title", rs.getString("title"),
                    "body", rs.getString("body")
                    )
                );
            }

        } catch (SQLException e) {
            // Если в процессе работы с базой было выброшено исключение SQLException,
            // нужно отправить в ответе код ошибки 500
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        request.setAttribute("articles", articles);
        request.setAttribute("page", normalizedPage);
        TemplateEngineUtil.render("articles/index.html", request, response);
    }

    private void showArticle(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {

        ServletContext context = request.getServletContext();
        Connection connection = (Connection) context.getAttribute("dbConnection");

        String id = getId(request);

        Map<String, String> article;

        try {
            article = getArticleById(id, connection);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        request.setAttribute("article", article);
        TemplateEngineUtil.render("articles/show.html", request, response);
    }

    private void newArticle(HttpServletRequest request,
                            HttpServletResponse response)
                    throws IOException, ServletException {

        TemplateEngineUtil.render("articles/new.html", request, response);
    }

    private void createArticle(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {

        HttpSession session = request.getSession();
        String title = request.getParameter("title");
        String body = request.getParameter("body");

        ServletContext context = request.getServletContext();
        Connection connection = (Connection) context.getAttribute("dbConnection");

        // BEGIN
        String sqlQuery = "INSERT INTO articles (title, body) VALUES (?, ?);";
        try {
            PreparedStatement pr = connection.prepareStatement(sqlQuery);
            pr.setString(1, title);
            pr.setString(2, body);
            pr.execute();
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        // END

        session.setAttribute("flash", "Статья успешно создана");
        response.sendRedirect("/articles");
    }

    private void editArticle(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {

        ServletContext context = request.getServletContext();
        Connection connection = (Connection) context.getAttribute("dbConnection");

        String id = getId(request);

        // BEGIN
        if (id == null || Integer.parseInt(id) > getMaxId(request, response)) {
            response.sendError(404);
            return;
        }
        Map<String, String> article = new HashMap<>();
        String sqlQuery = "SELECT * FROM articles WHERE id = ?;";
        try {
            PreparedStatement pr = connection.prepareStatement(sqlQuery);
            pr.setInt(1, Integer.parseInt(id));
            ResultSet rs = pr.executeQuery();
            if (!rs.next()) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            article.put("id", rs.getString(1));
            article.put("title", rs.getString(2));
            article.put("body", rs.getString(3));
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        // END

        request.setAttribute("article", article);
        TemplateEngineUtil.render("articles/edit.html", request, response);
    }

    private void updateArticle(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {

        HttpSession session = request.getSession();
        ServletContext context = request.getServletContext();
        Connection connection = (Connection) context.getAttribute("dbConnection");

        String id = getId(request);
        String title = request.getParameter("title");
        String body = request.getParameter("body");

        // BEGIN
        if (id == null || Integer.parseInt(id) > getMaxId(request, response)) {
            response.sendError(404);
            return;
        }
        String sqlQuery = "UPDATE articles SET title=?, body=? WHERE id=?;";
        try {
            PreparedStatement pr = connection.prepareStatement(sqlQuery);
            pr.setString(1, title);
            pr.setString(2, body);
            pr.setString(3, id);
            pr.execute();
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        // END

        session.setAttribute("flash", "Статья успешно изменена");
        response.sendRedirect("/articles");
    }

    private void deleteArticle(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {

        ServletContext context = request.getServletContext();
        Connection connection = (Connection) context.getAttribute("dbConnection");

        String id = getId(request);

        // BEGIN
        if (id == null || Integer.parseInt(id) > getMaxId(request, response)) {
            response.sendError(404);
            return;
        }
        Map<String, String> article = new HashMap<>();
        String sqlQuery = "SELECT * FROM articles WHERE id = ?;";
        try {
            PreparedStatement pr = connection.prepareStatement(sqlQuery);
            pr.setInt(1, Integer.parseInt(id));
            ResultSet rs = pr.executeQuery();
            if (!rs.next()) {
                response.sendError(404);
                return;
            }
            article.put("id", rs.getString(1));
            article.put("title", rs.getString(2));
            article.put("body", rs.getString(3));
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        // END

        request.setAttribute("article", article);
        TemplateEngineUtil.render("articles/delete.html", request, response);
    }

    private void destroyArticle(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {

        HttpSession session = request.getSession();
        ServletContext context = request.getServletContext();
        Connection connection = (Connection) context.getAttribute("dbConnection");

        String id = getId(request);

        // BEGIN
        if (id == null || Integer.parseInt(id) > getMaxId(request, response)) {
            response.sendError(404);
            return;
        }
        String sqlQuery = "DELETE FROM articles WHERE id=?;";
        try {
            PreparedStatement pr = connection.prepareStatement(sqlQuery);
            pr.setInt(1, Integer.parseInt(id));
            pr.execute();
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        // END

        session.setAttribute("flash", "Статья успешно удалена");
        response.sendRedirect("/articles");
    }

    private int getMaxId(HttpServletRequest request,
                         HttpServletResponse response) {
        ServletContext context = request.getServletContext();
        Connection connection = (Connection) context.getAttribute("dbConnection");

        String sqlQuery = "SELECT MAX(id) FROM articles;";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sqlQuery);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
