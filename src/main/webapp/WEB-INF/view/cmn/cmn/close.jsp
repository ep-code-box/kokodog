<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <link rel="stylesheet" href="/js/jqwidgets/styles/jqx.base.css" type="text/css"/>
    <script type="text/javascript" src="/js/jquery/jquery-3.2.1.js"></script>
    <script type="text/javascript" src="/js/cmn.js"></script>
    <meta charset="UTF-8"/>
    <link rel="stylesheet" type="text/css" href="/css/cmn/cmn/close.css">
    <title>KOKODOG Login</title>
    <script>
      var g_interval = null;
      
      $(document).ready(function() {
        if (typeof window.opener != "undefined" && window.opener != null) {
          window.opener.send();
        }
        g_interval = setInterval(checkWindowPopup, 2000);
      });
      
      function setInterval() {
        clearInterval(g_interval);
        alert("정상 로그인 되었습니다. 서비스 재호출해주세요.");
        window.close();
      }
    </script>
  </head>
  <body>
  </body>
</html>