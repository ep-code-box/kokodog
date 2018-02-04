<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" import="com.cmn.err.KokodogException"%>
<%@ page isErrorPage="true"%>
<%
String errMsg = null;
int responseStatus = 401;
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
    <title>401 Unauthorized</title>
    <link rel="stylesheet" href="/js/jqwidgets/styles/jqx.base.css" type="text/css"/>
    <script type="text/javascript" src="/js/jquery/jquery-3.2.1.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxcore.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxloader.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxdropdownlist.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxbuttons.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxlistbox.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxlistbox.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxscrollbar.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxdata.js"></script>
    <script src="/js/cmn.js"></script>
    <script>
      $(document).ready(function() {
        contentInitLoad();
        contentEventLoad();
        init();
      });
      
      function contentInitLoad() {
        $("div#audit_lst_component").jqxDropDownList({
          width: "100%",
          height: "25px"
        });
        $("div#audit_lst_request_component").jqxButton({
          width: "100%",
          height: "25px",
          value: "요청",
          disabled: true
        });        
      }
      
      function contentEventLoad() {
        $("div#audit_lst_request_component").on("click", event_audit_lst_request_component_click);
        $("div#audit_lst_component").on("change", event_audit_lst_component_change);
      }
      
      function event_audit_lst_request_component_click() {
        if ($("div#audit_lst_request_component").jqxButton("disabled") == false) {
          cmnSyncCall("/cmn/err/err_401/RequestAuth", {auth_num: $("div#audit_lst_component").val()}, callback, null);
        }
      }
      
      function event_audit_lst_component_change(event) {
        if (event.args.index >= 0) {
          $("div#audit_lst_request_component").jqxButton("disabled", false);
        } else {
          $("div#audit_lst_request_component").jqxButton("disabled", true);          
        }
      }
      
      function init() {
        cmnSyncCall("/cmn/err/err_401/GetPagetAuth", {path: window.location.pathname}, callback, null);
      }
      
      function callback(data, act, input_param, callbackVar) {
        if (act == "/cmn/err/err_401/GetPagetAuth") {
          var i = 0;
          for (i = 0; i < data.length; i++) {
            $("div#audit_lst_component").jqxDropDownList("addItem", {label: data[i].auth_nm, value: data[i].auth_num});
          }
        } else if (act == "/cmn/err/err_401/RequestAuth") {
          alert("정상 요청되었습니다.");
          $("div#audit_lst_component").jqxDropDownList("removeAt", $("div#audit_lst_component").jqxDropDownList("getItemByValue", input_param.auth_num).index);
          $("div#audit_lst_component").jqxDropDownList("uncheckAll");
          $("div#audit_lst_request_component").jqxButton("disabled", true);
        }
      }
    </script>
  </head>
  <body>
    <table id="401_err" align="center">
      <tr>
        <th colspan="4">
          <img align="absmiddle" style="width:800px;height:600px;" src="http://2slick.com/web/wp-content/themes/smartit/admin/extensions/timthumb.php?src=http%3A%2F%2F2slick.com%2Fweb%2Fwp-content%2Fuploads%2F2012%2F11%2F401.jpg&w=845"/>
        </th>
      </tr>
      <tr height="25">
        <td width="250">
        </td>
        <td width="200" height="25">
          <div id="audit_lst" style="width:100%;height:100%;">
            <div id="audit_lst_component">
            </div>
          </div>
        </td>
        <td width="100" height="25">
          <div id="audit_lst_request" style="width:100%;height:100%;">
            <div id="audit_lst_request_component">
            </div>
          </div>
        </td>
        <td>
        </td>
      </tr>
    </table>
  </body>
</html>
<%
}
%>