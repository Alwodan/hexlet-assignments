<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- BEGIN -->
<html>
  <head>
    <meta charset="UTF-8">
    <title>Users</title>
  </head>
  <body>
   <table>
      <tr><td>${user.get("id")}</td></tr>
      <tr><td>${user.get("firstName")}</td></tr>
      <tr><td>${user.get("lastName")}</td></tr>
      <tr><td>${user.get("email")}</td></tr>
      <tr><td><a href='/users/delete?id=${user.get("id")}'>Delete User</a></td></tr>
   </table>
  </body>
</html>
<!-- END -->