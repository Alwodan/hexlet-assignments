package exercise.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

import java.util.*;

import org.apache.commons.lang3.ArrayUtils;

import exercise.TemplateEngineUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;



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

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

        String action = getAction(request);

        switch (action) {
            case "list":
                showArticles(request, response);
                break;
            default:
                showArticle(request, response);
                break;
        }
    }

    private void showArticles(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException, ServletException {

        ServletContext context = request.getServletContext();
        Connection connection = (Connection) context.getAttribute("dbConnection");
        // BEGIN

        int pageNumber;
        if (request.getParameter("page") == null
                || Integer.parseInt(request.getParameter("page")) == 0 ) {
            pageNumber = 1;
        } else {
            pageNumber = Integer.parseInt(request.getParameter("page"));
        }

        List<Map<String, String>> articles = new ArrayList<>();

        String sqlQuery = "SELECT * FROM articles ORDER BY id LIMIT 10 OFFSET ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, (pageNumber - 1) * 10);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                articles.add(Map.of("id", rs.getString(1), "title", rs.getString(2)));
            }

            if (articles.size() < 10) {
                request.setAttribute("pageNumber", pageNumber - 1);
            } else {
                request.setAttribute("pageNumber", pageNumber);
            }
            request.setAttribute("articles", articles);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // END
        TemplateEngineUtil.render("articles/index.html", request, response);
    }

    private void showArticle(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {

        ServletContext context = request.getServletContext();
        Connection connection = (Connection) context.getAttribute("dbConnection");
        Map<String, String> article = new HashMap<>();
        // BEGIN
        String sqlQuery = "SELECT * FROM articles WHERE id=?";
        try {
            PreparedStatement pr = connection.prepareStatement(sqlQuery);
            pr.setInt(1, Integer.parseInt(Objects.requireNonNull(getId(request))));

            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                article.put("title", rs.getString(2));
                article.put("body", rs.getString(3));
            }

            request.setAttribute("article", article);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // END
        TemplateEngineUtil.render("articles/show.html", request, response);
    }
}
