<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String name = request.getParameter("name");
%>

<html>
<head>
<title><%= name %></title>
</head>
<body>
<div style="text-align: center;">
<h2><%= name %></h2>
<%= name %>
の入力が完了しました<br/>
</div>
</body>
</html>
