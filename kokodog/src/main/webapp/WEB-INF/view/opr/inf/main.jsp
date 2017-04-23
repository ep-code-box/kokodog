<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>KOKODOG[MyInfraOperation]</title>
    <link rel="shortcut icon" type="image/x-icon" href="/FileDown?file_key=tId6m0peoFkF2GYcAfAJG48kOE5Djo5ky49MFtnX" />
    <link rel="stylesheet" href="/css/opr/inf/main.css"/>
    <link rel="stylesheet" href="https://jqwidgets.com/public/jqwidgets/styles/jqx.base.css" type="text/css"/>
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
    <script type="text/javascript" src="https://jqwidgets.com/public/jqwidgets/jqx-all.js"></script>
    <script src="/js/cmn.js"></script>
    <script type="text/javascript">
      var graphColor = {};
      var refreshTimer;
      var initTimeGap = 60 * 2;
      var initGetCnt = 300;
      var isRefresh = true;
      var xAxisTickMarkCount = 5;
      var prevInfraUsedCheckTime = 0;
      var currentTime = 0;
      var maxTimeDiffMinute = 2 * 24 * 60;
      var timerFreqSec = 10;
      /* Document 로드 시에 발생하는 시작 함수 */
      $(document).ready(function() {
        contentInitLoad();
        contentEventLoad();
        loadAllInfraInfo();
        refreshTimer = setInterval(refreshTimerFunction, 1000 * timerFreqSec);
      });
      
      /* jqWidget을 사용하는 각종 함수 들 첫 오픈 처리 */
      function contentInitLoad() {
        $("div#top_menu_bar_component").jqxMenu({
          width: "100%",
          height: "100%"
        });
        $("div#infra_info_sub").jqxPanel({
          width: "100%",
          height: "100%",
          sizeMode: "fixed"
        });
        $("div#set_search_input_time_start_sub").jqxDateTimeInput({
          width: "100%",
          height:"100%",
          formatString: "yyyy-MM-dd HH:mm",
          showFooter: true,
          showTimeButton: true
        });
        $("div#set_search_input_time_end_sub").jqxDateTimeInput({
          width: "100%",
          height:"100%",
          formatString: "yyyy-MM-dd HH:mm",
          showFooter: true,
          showTimeButton: true,
          disabled: true
        });
        $("div#set_search_input_time_is_now_sub").jqxCheckBox({
          width: "100%",
          height:"100%",
          checked: true
        });
        $("div#infra_info_content_memory_chart_sub").jqxChart({
          title: "Memory Usage",
          padding: {left: 5, top: 5, right: 5, bottom: 5},
          description: "",
          source: {},
          xAxis: {
            dataField: "mid_time",
            formatFunction: function (value) {
              return "";
            },
            type: "linear",
            tickMarks: {
              visible: true,
              step: 10,
              color: "#888888"
            },                    
            valuesOnTicks: true,
          },
          valueAxis: {
            minValue: 0,
            maxValue: 100,
            visible: true,
            title: {text: "Memory Usage(%)"},
            tickMarks: {color: "#BCBCBC"}
          },
          colorScheme: "scheme01",
          seriesGroups: [{
            type: "line",
            series: []
          }]
        });
        $("div#infra_info_content_cpu_chart_sub").jqxChart({
          title: "CPU Usage",
          padding: {left: 5, top: 5, right: 5, bottom: 5},
          source: {},
          description: "",
          xAxis: {
            dataField: "mid_time",
            formatFunction: function (value) {
              var date = new Date(value);
              return intToString(date.getFullYear(), 4) + "-" + intToString((date.getMonth() + 1), 2)
                + "-" + intToString(date.getDate(), 2) + " " + intToString(date.getHours(), 2) + ":"
                + intToString(date.getMinutes(), 2);
            },
            type: "linear",
            tickMarks: {
              visible: true,
              step: 10,
              color: "#888888"
            },                    
            valuesOnTicks: true,
          },
          valueAxis: {
            minValue: 0,
            maxValue: 100,
            visible: true,
            title: {text: "CPU Usage(%)"},
            tickMarks: {color: "#BCBCBC"}
          },
          colorScheme: "scheme02",
          seriesGroups: [{
            type: "line",
            series: []
          }]
        });
      }

      /* jqWidget을 사용하는 각종 이벤트들 맵핑 처리 */
      function contentEventLoad() {
        $("a#menu_to_mobile").click(event_a_menu_to_mobile_click);
        $("a#menu_exit").click(event_a_menu_exit_click);
        $("a#help_help").click(event_a_help_help_click);
        $("a#help_about").click(event_a_menu_help_about_click);
        $("div#set_search_input_time_start_sub").on("change", event_set_search_input_time_start_change);
        $("div#set_search_input_time_end_sub").on("change", event_set_search_input_time_end_change);
        $("div#set_search_input_time_is_now_sub").on("change", event_set_search_input_time_is_now_change);
      }
      
      function event_a_menu_to_mobile_click() {
        cmnSyncCall("SetChangeToMobile", {}, callback, null);
      }
      
      function event_a_menu_exit_click() {
        
      }
      
      function event_a_help_help_click() {
        
      }
      
      function event_a_menu_help_about_click() {
        
      }
      
      function event_set_search_input_time_start_change() {
        getInfo();
      }
      
      function event_set_search_input_time_end_change() {
        getInfo();
      }
      
      function loadAllInfraInfo() {
        cmnSyncCall("GetServerTime", {}, callback, 1);
      }
      
      function event_set_search_input_time_is_now_change(event) {
        if (event.args.checked == true) {
          $("div#set_search_input_time_end_sub").jqxDateTimeInput("disabled", true);
        } else {
          $("div#set_search_input_time_end_sub").jqxDateTimeInput("disabled", false);
        }
      }

      function callback(data, act, input_param, callbackVar) {
        if (act == "GetServerTime" && callbackVar == 1) {
          currentTime = data.datetime;
          prevInfraUsedCheckTime = data.datetime;
          isRefresh = false;
          var date = new Date(data.datetime - (initTimeGap * 60 * 1000));
          $("div#set_search_input_time_start_sub").jqxDateTimeInput("setDate", date);
          date = new Date(data.datetime);
          isRefresh = true;
          $("div#set_search_input_time_end_sub").jqxDateTimeInput("setDate", date);
        } else if (act == "GetServerTime" && callbackVar == 2) {
          currentTime = data.datetime;
          if (data.datetime - prevInfraUsedCheckTime >= 60 * 1000) {
            prevInfraUsedCheckTime = data.datetime;
            var prevFromDate = $("div#set_search_input_time_start_sub").jqxDateTimeInput("value");
            var prevEndDate = $("div#set_search_input_time_end_sub").jqxDateTimeInput("value");
            var nextFromDate = new Date(data.datetime - prevEndDate.getTime() + prevFromDate.getTime());
            isRefresh = false;
            $("div#set_search_input_time_start_sub").jqxDateTimeInput("setDate", nextFromDate);
            var date = new Date(data.datetime);
            isRefresh = true;
            $("div#set_search_input_time_end_sub").jqxDateTimeInput("setDate", date);
          }
        } else if (act == "GetSystemInfoData") {
          var i = 0;
          var j = 0;
          var k = 0;
          var l = 0;
          var m = 0;
          var n = 0;
          var maxAPNum = 0;
          var maxCoreNum = new Array();
          var maxContainerNum = new Array();
          var xAxis = {
            dataField: "mid_time",
            formatFunction: function (value) {
              var startDate = $("div#set_search_input_time_start_sub").jqxDateTimeInput("value").getTime();
              var endDate = $("div#set_search_input_time_end_sub").jqxDateTimeInput("value").getTime();
              if (value < startDate + (endDate - startDate) / initGetCnt) {
                var date = new Date(value);
                return intToString(date.getFullYear(), 4) + "-" + intToString((date.getMonth() + 1), 2)
                  + "-" + intToString(date.getDate(), 2) + "<br>" + intToString(date.getHours(), 2) + ":"
                  + intToString(date.getMinutes(), 2);
              } else if (new Date(value).getDate() != new Date(value - (endDate - startDate) / xAxisTickMarkCount).getDate()) {
                var date = new Date(value);
                return intToString(date.getFullYear(), 4) + "-" + intToString((date.getMonth() + 1), 2)
                  + "-" + intToString(date.getDate(), 2) + "<br>" + intToString(date.getHours(), 2) + ":"
                  + intToString(date.getMinutes(), 2);                
              } else {
                var date = new Date(value);
                return intToString(date.getHours(), 2) + ":" + intToString(date.getMinutes(), 2);
              }
            },
            unitInterval: ($("div#set_search_input_time_end_sub").jqxDateTimeInput("value").getTime() - $("div#set_search_input_time_start_sub").jqxDateTimeInput("value").getTime()) / xAxisTickMarkCount,
            type: "linear",
            tickMarks: {
              visible: true,
              step: ($("div#set_search_input_time_end_sub").jqxDateTimeInput("value").getTime() - $("div#set_search_input_time_start_sub").jqxDateTimeInput("value").getTime()) / xAxisTickMarkCount,
              color: "#888888"
            },                    
            valuesOnTicks: true,
          };
          for (i = 0; i < data.length; i++) {
            maxAPNum = 0;
            if (data[i].info_type == "memory_used") {
              for (j = 0; j < data[i].info.length; j++) {
                for (k = 0; k < data[i].info[j].info.length; k++) {
                  if (data[i].info[j].info[k].ap_num > maxAPNum) {
                    for (l = 0; l < data[i].info[j].info[k].ap_num - maxAPNum; l++) {
                      maxContainerNum[l + maxAPNum] = 0;
                    }
                    maxAPNum = data[i].info[j].info[k].ap_num;
                  }
                  for (l = 0; l < data[i].info[j].info[k].info.length; l++) {
                    if (data[i].info[j].info[k].info[l].container_num > maxContainerNum[data[i].info[j].info[k].ap_num - 1]) {
                      maxContainerNum[data[i].info[j].info[k].ap_num - 1] = data[i].info[j].info[k].info[l].container_num;
                    }
                  }
                }
              }
              var memoryUsed = new Array();
              for (j = 0; j < data[i].info.length; j++) {
                memoryUsed[j] = {from_time: data[i].info[j].from_time, to_time: data[i].info[j].to_time,
                                 mid_time: data[i].info[j].mid_time};
                for (k = 0; k < maxAPNum; k++) {
                  for (l = 0; l < maxContainerNum[k]; l++) {
                    var dataFind = false;
                    for (m = 0; m < data[i].info[j].info.length; m++) {
                      for (n = 0; n < data[i].info[j].info[m].info.length; n++) {
                        if (data[i].info[j].info[m].ap_num == k + 1 && data[i].info[j].info[m].info[n].container_num == l + 1) {
                          memoryUsed[j]["data_" + (k + 1) + "_" + (l + 1)] = data[i].info[j].info[m].info[n].memory_used;
                          dataFind = true;
                          break;
                        }
                      }
                      if (dataFind == true) {
                        break;
                      }
                    }
                  }
                }
              }
              $("div#infra_info_content_memory_chart_sub").jqxChart("source", memoryUsed);
              var series = new Array();
              var tempPos = 0;
              for (j = 0; j < maxAPNum; j++) {
                for (k = 0; k < maxContainerNum[j]; k++) {
                  series[tempPos] = {dataField: "data_" + (j + 1) + "_" + (k + 1), displayText: "AP : " + (j + 1) + ", Container : " + (k + 1)};
                  tempPos++;
                }
              }
              $("div#infra_info_content_memory_chart_sub").jqxChart("seriesGroups", [{
                      type: "line",
                      opacity: 0.8,
                      lineWidth: 2,
                      toolTipFormatFunction: function (value, itemIndex, serie, group, xAxisValue, xAxis) {
                        return "메모리 사용량 : " + Math.round(value * 100) / 100 + "%";
                      },
                      series: series
                    }]);
              $("div#infra_info_content_memory_chart_sub").jqxChart("xAxis", xAxis);
              $("div#infra_info_content_memory_chart_sub").jqxChart("refresh");
            } else if (data[i].info_type == "cpu_used") {
              for (j = 0; j < data[i].info.length; j++) {
                for (k = 0; k < data[i].info[j].info.length; k++) {
                  if (data[i].info[j].info[k].ap_num > maxAPNum) {
                    for (l = 0; l < data[i].info[j].info[k].ap_num - maxAPNum; l++) {
                      maxCoreNum[l + maxAPNum] = 0;
                    }
                    maxAPNum = data[i].info[j].info[k].ap_num;
                  }
                  for (l = 0; l < data[i].info[j].info[k].info.length; l++) {
                    if (data[i].info[j].info[k].info[l].core_num > maxCoreNum[data[i].info[j].info[k].ap_num - 1]) {
                      maxCoreNum[data[i].info[j].info[k].ap_num - 1] = data[i].info[j].info[k].info[l].core_num;
                    }
                  }
                }
              }
              var cpuUsed = new Array();
              for (j = 0; j < data[i].info.length; j++) {
                cpuUsed[j] = {from_time: data[i].info[j].from_time, to_time: data[i].info[j].to_time,
                                 mid_time: data[i].info[j].mid_time};
                for (k = 0; k < maxAPNum; k++) {
                  for (l = 0; l < maxCoreNum[k]; l++) {
                    var dataFind = false;
                    for (m = 0; m < data[i].info[j].info.length; m++) {
                      for (n = 0; n < data[i].info[j].info[m].info.length; n++) {
                        if (data[i].info[j].info[m].ap_num == k + 1 && data[i].info[j].info[m].info[n].core_num == l + 1) {
                          cpuUsed[j]["data_" + (k + 1) + "_" + (l + 1)] = data[i].info[j].info[m].info[n].cpu_used;
                          dataFind = true;
                          break;
                        }
                      }
                      if (dataFind == true) {
                        break;
                      }
                    }
                  }
                }
              }
              $("div#infra_info_content_cpu_chart_sub").jqxChart("source", cpuUsed);
              var series = new Array();
              var tempPos = 0;
              for (j = 0; j < maxAPNum; j++) {
                for (k = 0; k < maxCoreNum[j]; k++) {
                  series[tempPos] = {dataField: "data_" + (j + 1) + "_" + (k + 1), displayText: "AP : " + (j + 1) + ", Core : " + (k + 1)};
                  tempPos++;
                }
              }
              $("div#infra_info_content_cpu_chart_sub").jqxChart("seriesGroups", [{
                      type: "line",
                      opacity: 0.8,
                      lineWidth: 2,
                      toolTipFormatFunction: function (value, itemIndex, serie, group, xAxisValue, xAxis) {
                        return "CPU 점유율 : " + Math.round(value * 100) / 100 + "%";
                      },
                      series: series
                    }]);
              $("div#infra_info_content_cpu_chart_sub").jqxChart("xAxis", xAxis);
              $("div#infra_info_content_cpu_chart_sub").jqxChart("refresh");
            }
          }
        } else if (act == "SetChangeToMobile") {
          window.location.href=window.location.href;
        }
      }
      
      function getInfo() {
        if (isRefresh == true) {
          var startDate = $("div#set_search_input_time_start_sub").jqxDateTimeInput("getDate");
          var endDate = $("div#set_search_input_time_end_sub").jqxDateTimeInput("getDate");
          if (startDate.getTime() >= currentTime) {
            $("div#set_search_input_time_start_sub").jqxDateTimeInput("template", "warning");
          }
          if (endDate.getTime() > currentTime) {
            $("div#set_search_input_time_end_sub").jqxDateTimeInput("template", "warning");
          }
          if (endDate.getTime() - startDate.getTime() <= 0) {
            $("div#set_search_input_time_start_sub").jqxDateTimeInput("template", "warning");            
            $("div#set_search_input_time_end_sub").jqxDateTimeInput("template", "warning");
          }
          if (endDate.getTime() - startDate.getTime() >= maxTimeDiffMinute * 60 * 1000) {
            $("div#set_search_input_time_start_sub").jqxDateTimeInput("template", "warning");            
            $("div#set_search_input_time_end_sub").jqxDateTimeInput("template", "warning");
          }
          if (startDate.getTime() < currentTime && endDate.getTime() <= currentTime && endDate.getTime() - startDate.getTime() > 0
             && endDate.getTime() - startDate.getTime() < maxTimeDiffMinute * 60 * 1000) {
            $("div#set_search_input_time_start_sub").jqxDateTimeInput("template", "default");            
            $("div#set_search_input_time_end_sub").jqxDateTimeInput("template", "default");
            cmnSyncCall("GetSystemInfoData", {from_datetime: startDate.getTime(), to_datetime: endDate.getTime(), cnt: initGetCnt}, callback, null);
          }
        }
      }
      
      function intToString(number, length) {
        var i = 0;
        var returnStr;
        if (number == 0) {
          returnStr = "";
          for (i = 0; i < length; i++) {
            returnStr = returnStr + "0";
          }
          return returnStr;
        }       
        returnStr = String(abs(number));
        for (i = 0; i < length - (Math.log(abs(number)) / Math.log(10)) - 1; i++) {
          returnStr = "0" + returnStr;
        }
        if (number < 0) {
          returnStr = "-" + returnStr;
        }
        return returnStr;
      }
      
      function refreshTimerFunction() {
        if (document.hasFocus() == true) {
          if ($("div#set_search_input_time_is_now_sub").jqxCheckBox("checked") == true) {
            cmnASyncCall("GetServerTime", {}, callback, 2);
          }
        }
      }
      
      function abs(number) {
        if (number < 0) {
          return number * -1;
        } else {
          return number;
        }
      }
    </script>
  </head>
  <body>
    <div class="body_sub">
      <div class="top_main_grid"></div>
      <div class="top_menu_bar">
        <div id="top_menu_bar_component">
          <ul>
            <li>File
              <ul>
                <li id="li_menu_to_mobile">
                  <a id="menu_to_mobile">Mobile Page...</a>
                </li>
                <li type="separator"></li>
                <li id="li_menu_exit">
                  <a id="menu_exit">Exit</a>
                </li>
              </ul>
            </li>
            <li>Help
              <ul>
                <li>
                  <a id="help_help">Help</a>
                </li>
                <li type='separator'></li>
                <li>
                  <a id="help_about">About...</a>
                </li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
      <div class="content">
        <div class="infra_info">
          <div id="infra_info_sub">
            <div class="set_search_input_time">
              <div class="set_search_input_time_time">
                <div class="set_search_input_time_start">
                  <div id="set_search_input_time_start_sub">
                  </div>
                </div>
                <div class="set_search_input_time_end">
                  <div id="set_search_input_time_end_sub">
                  </div>
                </div>
              </div>
              <div class="set_search_input_time_is_now">
                <div id="set_search_input_time_is_now_sub">Now</div>
              </div>
            </div>
            <div class="infra_info_content">
              <div class="infra_info_content_memory_chart">
                <div id="infra_info_content_memory_chart_sub">
                </div>
              </div>
              <div class="infra_info_content_cpu_chart">
                <div id="infra_info_content_cpu_chart_sub">
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>