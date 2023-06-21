<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- BEGIN -->
<html>
  <head>
    <meta charset="UTF-8">
    <title>Users</title>
  </head>
  <body>
  <form action='/users/delete?id=${user.get("id")}' method="post">
    <button type="submit">Sure, delete this user</button>
  </form>
  </body>
</html>
<!-- END -->
