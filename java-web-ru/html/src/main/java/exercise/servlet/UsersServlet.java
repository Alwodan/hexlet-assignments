package exercise.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;

import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;

public class UsersServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null) {
            showUsers(request, response);
            return;
        }

        String[] pathParts = pathInfo.split("/");
        String id = ArrayUtils.get(pathParts, 1, "");

        showUser(request, response, id);
    }

    private List<HashMap<String, String>> getUsers() throws IOException {
        // BEGIN
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File("src/main/resources/users.json"), new TypeReference<>() {});
        // END
    }

    private void showUsers(HttpServletRequest request,
                          HttpServletResponse response)
                throws IOException {

        // BEGIN
        StringBuilder result = new StringBuilder();
        result.append("""
                <!DOCTYPE html>
                <html lang="ru">
                    <head>
                        <meta charset="UTF-8">
                        <title>Users</title>
                    </head>
                    <body>
                        <table>
                """);

        for (HashMap<String, String> user : getUsers()) {
            result.append("<tr>");
            result.append("<td>").append(user.get("id")).append("</td>");
            result.append("<td><a href=\"/users/").append(user.get("id")).append("\">");
            result.append(user.get("firstName")).append(" ").append(user.get("lastName"));
            result.append("</a></td></tr>");
        }
        result.append("</table></body></html>");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(result);
        // END
    }

    private void showUser(HttpServletRequest request,
                         HttpServletResponse response,
                         String id)
                 throws IOException {

        // BEGIN
        List<HashMap<String, String>> users = getUsers();
        Optional<HashMap<String, String>> optional = users.stream()
                .filter(user -> user.get("id").equals(id))
                .findAny();

        StringBuilder result = new StringBuilder();
        result.append("""
                <!DOCTYPE html>
                <html lang="ru">
                    <head>
                        <meta charset="UTF-8">
                        <title>Users</title>
                    </head>
                    <body>
                        <table>
                """);

        if (optional.isPresent()) {
            result.append("<tr>");
            result.append("<td>").append(optional.get().get("id")).append("</td>");
            result.append("<td><a href=\"/users/").append(optional.get().get("id")).append("\">");
            result.append(optional.get().get("firstName")).append(" ").append(optional.get().get("lastName"));
            result.append("</a></td></tr>");
        } else {
            response.sendError(404);
        }
        result.append("</table></body></html>");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(result);
        // END
    }
}
