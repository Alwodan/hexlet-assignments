<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- BEGIN -->
<html>
  <head>
    <meta charset="UTF-8">
    <title>Users</title>
  </head>
  <body>
   <table>
    <c:forEach var="user" items="${users}">
      <tr>
      <td>${user.get("id")}</td>
      <td><a href='/users/show?id=${user.get("id")}'>${user.get("firstName")} ${user.get("lastName")}</a></td>
      </tr>
    </c:forEach>
   </table>
  </body>
</html>
<!-- END -->
