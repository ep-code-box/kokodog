<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%
int b = 0;
if ("true".equals(request.getParameter("error")) == false) {
  b = 0;
} else {
  b = 3 / 0;
}

%>
<html>
<head>
<meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
<title>test</title>
<script>
  $(document).ready(function() {
	});

</script>
</head>
<body>
test
</body>
</html>