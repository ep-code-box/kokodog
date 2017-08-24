<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <link rel="stylesheet" href="/js/jquery-widgets-demo/jqwidgets/styles/jqx.base.css" type="text/css"/>
    <script type="text/javascript" src="/js/jquery/jquery-3.2.1.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxcore.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxloader.js"></script>
    <script type="text/javascript" src="/js/cmn.js"></script>
    <meta charset="UTF-8"/>
    <link rel="stylesheet" type="text/css" href="/css/cmn/cmn/login.css">
    <title>KOKODOG Login</title>
    <script>
      $(document).ready(function() {
        cmnSyncCall("SetSystemLogin", {code: getParameter("code")}, callbackFunc, null);
      });
      
      function callbackFunc(data, tpName, parameter, callbackVar) {
        if (tpName == "SetSystemLogin") {
          window.location.href = getParameter("state");
        }
      }
    </script>
  </head>
  <body>
  </body>
</html>