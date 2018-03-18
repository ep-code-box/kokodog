<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" import="com.cmn.err.KokodogException"%>
<%@ page isErrorPage="true"%>
<%
String errMsg = null;
int responseStatus = 404;
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
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Untitled Document</title>
    <link href="/css/cmn/err/err_404.css" rel="stylesheet" type="text/css"/>
  </head>
  <body>
    <div class="Wrap">
	    <div class="errorBox">
		    <div class="noneImg">
			    <img src="/img/err_img01.gif" alt="Error Image"/>
		    </div>
		    <ul class="errorTxt">
			    <li>
			      <img src="/ciespp/images/error_img/err_img02.gif" alt="이용에 불편을 드려 죄송합니다."/>
			    </li>			
			    <li>
			      <div class="block">
			        <span class="txtBold">
			          404
			        </span>
			        : 웹 서버가 요청한 URL 혹은 자원을 찾지 못하였습니다.
			      </div>
            NOT FOUND, 문서를 찾을 수 없습니다.
        </li>
			  <li>
			    <a href="javascript:goHome();">
			      <img src="/img/err_btn02.gif" alt="홈으로가기"/>
			    </a>
			  </li>
		  </ul>
	  </div>
  </div>
</body>
</html>
<%
}
%>