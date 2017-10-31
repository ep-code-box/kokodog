<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" import="com.cmn.err.KokodogException"%>
<%@ page isErrorPage="true"%>
<%
String errMsg = null;
int responseStatus = 500;
String errSubMsg = null;
if (exception != null && exception instanceof KokodogException == true) {
  errMsg = ((KokodogException)exception).getMessage();
} else {
  errMsg = "내부 시스템 오류 입니다. 관리자에게 문의하세요.";
  errSubMsg = exception.getMessage();
}
request.setAttribute("_REQUEST_RESPONSE_STATUS", responseStatus);
request.setAttribute("_REQUEST_ERR_MESSGE", errMsg);
response.setStatus(responseStatus);
if (request.getHeader("Accept").indexOf("json") >= 0 && (request.getHeader("Accept").indexOf("html") < 0 || request.getHeader("Accept").indexOf("json") < request.getHeader("Accept").indexOf("html"))) {
  response.setContentType("application/json");
  if (exception != null && exception instanceof KokodogException == true) {
%>
{"error_num": <%=responseStatus%>, "error_nm": "<%=errMsg%>"}
<%
  } else {
%>
{"error_num": <%=responseStatus%>, "error_nm": "<%=errMsg%>", "error_sub_nm": "<%=errSubMsg%>"}
<%
  }
} else {
%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8"/>
    <title>500 Page Not Found</title>
    <script>
    </script>
  </head>
  <body>
    <table id="500_err" align="center">
      <tr>
        <td>
          <img align="absmiddle" style="width:800px;height:600px;" src="https://ril3y.files.wordpress.com/2014/03/exchange-2013-500-error.png"/>
        </td>
      </tr>
    </table>
  </body>
</html>
<%
}
%>