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
      var googleLinkUrl = "";
      $(document).ready(function() {
        if (getParameter("redirect_url") == null) {
          alert("정상적인 접근이 아닙니다.");
          window.close();
        }
        cmnSyncCall("GetGoogleLoginLinkURL", {redirect_url: getParameter("redirect_url")}, callbackFunc, null);
      });
      
      function callbackFunc(data, tpName, param, callbackVar) {
        if (tpName == "GetGoogleLoginLinkURL") {
          googleLinkUrl = data.url;
          if (get_cookie("login_type") == "google") {
            goGoogleLogin();
          }
        }
      }
        
      function goGoogleLogin() {
        set_cookie("login_type", "google");
        location.href = googleLinkUrl;
      }
    </script>
  </head>
  <body>
    <table id="google_login_img" align="center">
      <tr>
        <td>
          <a id="google_login_path" onClick="javascript:goGoogleLogin();">
            <img src="https://developers.google.com/accounts/images/sign-in-with-google.png?hl=ko"/>
          </a>
        </td>
      </tr>
    </table>
  </body>
</html>