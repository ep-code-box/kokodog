<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<html>
<head>
<meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
<title>카카오내비</title>
<script>
  var androidPackage;
  var url;
  var userAgent;
  var os;
  
  $(document).ready(function() {
    androidPackage = "com.locnall.KimGiSa";
    url = "intent://?version=11&menu=navigation&elat=37.3628573&elng=127.1178881&etitle=%EC%A0%95%EB%93%A0%EB%A7%88%EC%9D%84%EC%8B%A0%ED%99%945%EB%8B%A8%EC%A7%80%EC%95%84%ED%8C%8C%ED%8A%B8&eelat=37.363302&eelng=127.1174255#Intent;scheme=navermaps;action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;package=com.nhn.android.nmap;end";
    userAgent = navigator.userAgent.toLowerCase();
    if(userAgent.indexOf("android")>0){
      os = "android";
    } else if(userAgent.indexOf("iphone")>0 ||  userAgent.indexOf("ipad")>0) {
      os = "ios";
    } else{
      os = "android";
    }
    checkAppInstall();
	});
  
  function checkAppInstall() {
    if(os == "android") {
      location.href = url;
    } else if(os == "ios") {
      setTimeout(function() {
        location.href= getInstallURL();
      }, 1000);
      location.href = url;
    }
    return false;
  }
</script>
</head>
<body>
카카오 내비 연동 테스트
</body>
</html>