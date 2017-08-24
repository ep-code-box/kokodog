var g_pop_status;
var g_interval = null;
var g_callArg;
var g_status_call;
var g_is_loader_exist = false;
var g_is_loader_open = false;
var g_body_obj = [];

function cmnSyncCall(tpNm, param, callbackFunc, callbackVar, callbackErr) {
  if (g_is_loader_exist === false) {
    var body_obj = $("<div>").children();
    for (var i = 0; i < body_obj.length; i++) {
      if (body_obj[i].css("zIndex") > 0) {
        g_body_obj.push({body_obj: body_obj[i], zIndex: body_obj[i].css("zIndex")});
//        body_obj[i].css("zIndex", 0);
      }
    }
    $("<div>", {id: "_cmn_loader"}).appendTo("body");
    $("div#_cmn_loader").jqxLoader({width: 100, height: 60, imagePosition: "top"});
    g_is_loader_exist = true;
  }
  openLoader();
  cmnCall(tpNm, param, callbackFunc, callbackVar, false, callbackErr);
}

function cmnASyncCall(tpNm, param, callbackFunc, callbackVar, callbackErr) {
  cmnCall(tpNm, param, callbackFunc, callbackVar, true, callbackErr);
}

function cmnCall(tpNm, param, callbackFunc, callbackVar, isASync, callbackErr) {
  var urlLengthPos = location.href.indexOf("?");
  if (urlLengthPos < 0 || (location.href.indexOf("#") > 0 && urlLengthPos > location.href.indexOf("#"))) {
    urlLengthPos = location.href.indexOf("#");
  }
  if (urlLengthPos < 0) {
    urlLengthPos = location.href.length;
  }
  var uriSplit =  location.href.substring(location.href.indexOf("://") + 3, urlLengthPos).split("/");
  var pgm = null;
  var task = null;
  var page = null;
  var url = null;
  if (tpNm.substring(0, 1) != "/") {
    if (uriSplit.length >= 2 && uriSplit[1] !== "") {
      pgm = uriSplit[1];
    } else {
      pgm = "cmn";
    }
    if (uriSplit.length >= 3 && uriSplit[2] !== "") {
      task = uriSplit[2];
    } else {
      task = "cmn";
    }
    if (uriSplit.length >= 4 && uriSplit[3] !== "") {
      page = uriSplit[3];
    } else {
      page = "main";
    }
    url = "/" + pgm + "/" + task + "/" + page + "/" + tpNm;
  } else {
    url = tpNm;
  }
  $.ajax({
    url: url,
    type: "post",
    data: param,
    dataType: "JSON",
    async: true,
    success: function(data) {
      closeLoader();
      if (callbackFunc != null && typeof callbackFunc != "undefined" && callbackFunc !== "") {
        callbackFunc(data, tpNm, param, callbackVar);
      }
    },
    error: function(request, status, error) {
      var jsonMsg;
      try {
        jsonMsg = JSON.parse(request.responseText);
      } catch (e) {
        closeLoader();
      }
      if (jsonMsg.error_num == 4) {
        g_pop_status = window.open("/cmn/cmn/login?redirect_url=" + encodeURIComponent(getCloseUrl()), "Auto Login",
                                    "width=500px,height=840px,left=0,top=0,scrollbars=no,toolbar=no,resizable=no");
        if (typeof g_pop_status != "undefined" && g_pop_status != null) {
          g_status_call = 0;
          g_pop_status.focus();
          g_callArg = {tpNm: tpNm, param: param, callbackFunc: callbackFunc, callbackVar: callbackVar, isASync: isASync};
          if (typeof g_interval != "undefined" && g_interval != null) {
            clearInterval(g_interval);
          }
          g_interval = setInterval(checkWindowPopup, 1000);
        }
      } else {
        alert(jsonMsg.error_nm);
        closeLoader();
        if (callbackErr != null && typeof callbackErr != "undefined" && callbackErr !== "") {
          callbackErr(jsonMsg.error_num, tpNm, param, callbackVar);
        }
      }
    }
  });
}

function checkWindowPopup() {
  if (g_status_call >= 10) {
    clearInterval(g_interval);
    closeLoader();
  }
  if (g_pop_status == null || g_pop_status.document.location == null) {
    clearInterval(g_interval);
    closeLoader();
  }
  if (g_pop_status.document.location.href.split("://")[1].split("/")[3] === "close") {
    g_pop_status.close();
    clearInterval(g_interval);
    cmnCall(g_callArg.tpNm, g_callArg.param, g_callArg.callbackFunc, g_callArg.callbackVar, g_callArg.isASync);
  }
  g_status_call++;
}

function closeLoader() {
  if (g_is_loader_open === true) {
    $("body").css("cursor", "default");
    $("div#_cmn_loader").jqxLoader("close");
    g_is_loader_open = false;
  }
}

function openLoader() {
  g_is_loader_open = true;
  $("body").css("cursor", "wait");
  $("div#_cmn_loader").jqxLoader("open");
}

function getCloseUrl() {
  return location.href.split("://")[0] + "://" + location.href.split("://")[1].split("?")[0].split("#")[0].split("/")[0] + "/cmn/cmn/close";
}

function getParameter(name){
  var hashes = window.location.href.slice(location.href.indexOf("?") + 1).split("&");
  var i = 0;
  var hash = null;
  for (i = 0; i < hashes.length; i = i + 1) {
    hash = hashes[i].split("=");
    if (decodeURIComponent(hash[0]) == name) {
      return decodeURIComponent(hash[1]);
    }
  }
  return null;
}

function set_cookie(name, value, exp_y, exp_m, exp_d, path, domain, secure) {
  var cookie_string = name + "=" + escape(value);
  var expires;
  if (typeof exp_y != "undefined") {
    expires = new Date(exp_y, exp_m, exp_d);
    cookie_string += "; expires=" + expires.toGMTString();
  } else {
    var today = new Date();
    expires = new Date(today.getFullYear() + 100, today.getMonth(), today.getDate());
    cookie_string += "; expires=" + expires.toGMTString();
  }
  if (typeof path != "undefined") {
    cookie_string += "; path=" + escape(path);
  }
  if (typeof domain != "undefined") {
    cookie_string += "; domain=" + escape(domain);
  }
  if (typeof secure != "undefined" && secure === true) {
    cookie_string += "; secure";
  }
  document.cookie = cookie_string;
}

function delete_cookie(cookie_name) {
  var cookie_date = new Date();
  cookie_date.setTime(cookie_date.getTime() - 1);
  document.cookie = cookie_name += "=; expires=" + cookie_date.toGMTString();
}

function get_cookie(cookie_name) {
  var results = document.cookie.match("(^|;) ?" + cookie_name + "=([^;]*)(;|$)");
  if (typeof results != "undefined" && results != null) {
    return unescape(results[2]);
  }
  else {
    return null;
  }
}

function setCmnComboCodeMapping(obj, code, init) {
  cmnSyncCall("/cmn/cmn/main/GetCommonCode", {code: code}, cmnCallbackFunc, {obj: obj, init: init});
}

function cmnCallbackFunc(data, act, input_param, callbackVar) {
  var i = 0;
  var dict = Object.keys(data);
  for (i = 0; i < dict.length; i++) {
    callbackVar.obj.jqxDropDownList("addItem", {label: data[dict[i]], value: dict[i]});
  }
  if (typeof callbackVar.init == "undefined" || callbackVar.init == null) {
    callbackVar.obj.jqxDropDownList("selectIndex", 0);
  } else {
    callbackVar.obj.jqxDropDownList("selectIndex", callbackVar.init);    
  }
}