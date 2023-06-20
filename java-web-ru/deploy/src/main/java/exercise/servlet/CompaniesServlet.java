package exercise.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.stream.Collectors;
import static exercise.Data.getCompanies;

public class CompaniesServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

        // BEGIN
        PrintWriter out = response.getWriter();
        List<String> companies = getCompanies();

        if (request.getQueryString() != null && request.getQueryString().contains("search")) {
            String result = companies.stream()
                    .filter(company -> company.contains(request.getParameter("search")))
                    .collect(Collectors.joining("\n"));
            if (result.isEmpty()) {
                out.println("Companies not found");
            } else {
                out.println(result);
            }
        } else {
            String result = String.join("\n", companies);
            out.println(result);
        }
        // END
    }
}
