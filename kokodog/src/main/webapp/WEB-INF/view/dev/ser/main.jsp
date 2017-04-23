<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>KOKODOG[MyService]</title>
    <link rel="shortcut icon" type="image/x-icon" href="/FileDown?file_key=tId6m0peoFkF2GYcAfAJG48kOE5Djo5ky49MFtnX" />
    <link rel="stylesheet" href="/css/dev/ser/main.css"/>
    <link rel="stylesheet" href="http://www.jqwidgets.com/jquery-widgets-demo/jqwidgets/styles/jqx.base.css" type="text/css"/>
    <link href="https://fonts.googleapis.com/css?family=Oxygen+Mono" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
    <script type="text/javascript" src="http://www.jqwidgets.com/jquery-widgets-demo/jqwidgets/jqx-all.js"></script>
    <script type="text/javascript" src="http://www.jqwidgets.com/jquery-widgets-demo/jqwidgets/jqxwindow.js"></script>
    <link rel="stylesheet" href="http://codemirror.net/lib/codemirror.css"/>
    <link rel="stylesheet" href="http://codemirror.net/addon/hint/show-hint.css"/>
    <link rel="stylesheet" href="http://codemirror.net/addon/fold/foldgutter.css"/>
    <link rel="stylesheet" href="https://codemirror.net/theme/eclipse.css"/>
    <script src="http://codemirror.net/lib/codemirror.js"></script>
    <script src="http://codemirror.net/addon/edit/matchbrackets.js"></script>
    <script src="http://codemirror.net/addon/hint/show-hint.js"></script>
    <script src="http://codemirror.net/addon/hint/javascript-hint.js"></script>
    <script src="http://codemirror.net/mode/sql/sql.js"></script>
    <script src="http://codemirror.net/addon/fold/foldcode.js"></script>
    <script src="http://codemirror.net/addon/fold/foldgutter.js"></script>
    <script src="http://codemirror.net/addon/fold/brace-fold.js"></script>
    <script src="http://codemirror.net/mode/clike/clike.js"></script>
    <script src="http://codemirror.net/addon/fold/xml-fold.js"></script>
    <script src="http://codemirror.net/addon/fold/comment-fold.js"></script>
    <script src="http://codemirror.net/mode/xml/xml.js"></script>
    <script src="/js/cmn.js"></script>
    <script type="text/javascript">
      var editorArray = new Array();
      var editorNum = 0;
      var checkUnload = false;
      var g_isNew = false;
      var g_cmEditorTheme = "eclipse";
      var g_rep_ver_drop_down_system_change = false;

      $(document).ready(function() {
        contentInitLoad();
        contentEventLoad();
        cmnSyncCall("GetPgmList", {}, callback, null);
        $("div#source_data_tab_component").jqxTabs("removeFirst");
      });
      
      function contentInitLoad() {
        $("div#top_menu_bar_component").jqxMenu({
          width: "100%",
          height: "100%"
        });
        $("div#top_menu_icon_component").jqxToolBar({
          tools: "custom | custom custom | custom | custom custom | dropdownlist | dropdownlist dropdownlist dropdownlist",
          width: "100%",
          height: "100%",
          initTools: init_top_menu_icon_component_init_tools
        });
        $("div#left_right_splitter_component").jqxSplitter({
          width: "100%",
          height: "100%",
          panels: [{
            size: 300,
            min: 100
          }, {
            min: 200
          }]
        });
        $("input#search_text_component").jqxInput({
          placeHolder: "Search text...",
          height: "100%",
          width: "100%"
        });
        $("div#data_list_component").jqxListBox({
          width: "100%",
          height: "100%"
        });
        $("div#source_data_tab_component").jqxTabs({
          width: "100%",
          height: "100%",
          showCloseButtons: true
        });
        $("div#save_config_window").jqxWindow({
          position: "center",
          showCloseButton: true,
          resizable: false,
          isModal: true,
          modalOpacity: 0.3,
          draggable: true,
          autoOpen: false,
          width: "500px",
          height: "150px"
        });
        $("input#save_config_service_name_txt_component").jqxInput({
          placeHolder: "Service Name...",
          height: "100%",
          width: "100%"          
        });
        $("input#save_config_service_ok_but_component").jqxButton({
          height: "100%",
          width: "100%"          
        });
        $("input#save_config_service_cancel_but_component").jqxButton({
          height: "100%",
          width: "100%"          
        });
        $("div#test_input_window").jqxWindow({
          position: "center",
          showCloseButton: true,
          resizable: true,
          isModal: true,
          modalOpacity: 0.3,
          draggable: true,
          autoOpen: false,
          width: "800px",
          height: "600px"
        });
        $("div#test_window_content_panel_component").jqxPanel({
          height: "100%",
          width: "100%"
        });
        $("input#test_window_content_test_button_component").jqxButton({
          height: "100%",
          width: "100%"
        });
        $("input#test_window_content_cancel_button_component").jqxButton({
          height: "100%",
          width: "100%"
        });
        $("div#test_output_window").jqxWindow({
          position: "center",
          showCloseButton: true,
          resizable: true,
          isModal: true,
          modalOpacity: 0.3,
          draggable: true,
          autoOpen: false,
          width: "800px",
          height: "600px"          
        });
        $("div#test_window_content_output_compile_console_component").jqxPanel({
          height: "100%",
          width: "100%"
        });
        $("div#test_window_content_output_run_console_component").jqxPanel({
          height: "100%",
          width: "100%"
        });
        $("div#test_window_content_output_log_console_component").jqxPanel({
          height: "100%",
          width: "100%"
        });
        $("div#test_window_content_output_result_console_component").jqxPanel({
          height: "100%",
          width: "100%"
        });
      }
      
      function contentEventLoad() {
        $("div#data_list_component").on("select", event_data_list_component_select);
        $("input#save_config_service_ok_but_component").on("click", event_save_config_service_ok_but_component_select);
        $("input#test_window_content_test_button_component").on("click", event_test_window_content_test_buton_component_click);
        $("div#source_data_tab_component").on("removed", event_source_data_tab_component_removed);
        $("div#source_data_tab_component").on("selected", event_source_data_tab_component_selected);
      }
      
      function init_top_menu_icon_component_init_tools(type, index, tool, menuToolIninitialization) {
        switch (index) {
          case 0:
            var newButton = $("<div>" + "<img src='/FileDown?file_key=0xAoKZ11FQB3HyZH0bIpCaaphbKcyS3uXYn0G5rn' title='New...' style='width:20px;height:20px;'/>" + "</div>");
            tool.append(newButton);
            newButton.jqxButton({
              width: "20px",
              height: "20px"
            });
            newButton.on("click", event_a_menu_new_click);
            break;
          case 1:
            var saveButton = $("<div>" + "<img src='/FileDown?file_key=GgnS17SPV1IuXIfUZGWXfykXCbOMyeB2S2AGKtWD' title='Save...' style='width:20px;height:20px;'/>" + "</div>");
            tool.append(saveButton);
            saveButton.jqxButton({
              width: "20px",
              height: "20px"
            });
            saveButton.on("click", event_a_menu_save_click);
            break;
          case 2:
            var saveAsButton = $("<div>" + "<img src='/FileDown?file_key=6Tyvf2KhwkyKKug6IZZmJOvzLcZT4mYWoBK3D5Ke' title='Save As...' style='width:20px;height:20px;'/>" + "</div>");
            tool.append(saveAsButton);
            saveAsButton.jqxButton({
              width: "20px",
              height: "20px"
            });
            saveAsButton.on("click", event_a_menu_save_as_click);
            break;
          case 3:
            var distrubteButton = $("<div>" + "<img src='/FileDown?file_key=zc15PA0zUXEPfRQVPYNOTbcbdUEoJScZZLO8TBYG' title='Deploy' style='width:20px;height:20px;'/>" + "</div>");
            tool.append(distrubteButton);
            distrubteButton.jqxButton({
              width: "20px",
              height: "20px"
            });
            distrubteButton.on("click", event_a_menu_distribute);
            break;
          case 4:
            var deleteButton = $("<div>" + "<img src='/FileDown?file_key=YwN3sUbnW2f7T2YrLy5lbLUztLD9EWDIyP3v6g4A' title='Ver. Delete' style='width:20px;height:20px;'/>" + "</div>");
            tool.append(deleteButton);
            deleteButton.jqxButton({
              width: "20px",
              height: "20px"
            });
            deleteButton.on("click", event_a_menu_delete);
            break;
          case 5:
            var testButton = $("<div>" + "<img src='/FileDown?file_key=YwN3sUbnW2f7T2YrLy5lbLUztLD9EWDIyP3v6g4A' title='Test' style='width:20px;height:20px;'/>" + "</div>");
            tool.append(testButton);
            testButton.jqxButton({
              width: "20px",
              height: "20px"
            });
            testButton.on("click", event_a_menu_test);
            break;
          case 6:
            tool.jqxDropDownList({width: "150px", height: "28px", dropDownHeight: "200px"});
            tool.on("change", event_rep_ver_change);
            break;
          case 7:
            tool.jqxDropDownList({width: "150px", height: "28px", dropDownHeight: "200px"});
            tool.on("change", event_pgm_change);
            break;
          case 8:
            tool.jqxDropDownList({width: "150px", height: "28px", dropDownHeight: "200px"});
            tool.on("change", event_task_change);
            break;
          case 9:
            tool.jqxDropDownList({width: "150px", height: "28px", dropDownHeight: "200px"});
            tool.on("change", event_page_change);
            break;
          default:
            break;
        }
      }
      
      function event_a_menu_new_click(event) {
        g_isNew = true;
        $("div#save_config_window").jqxWindow({resizable: false, height: 120, width: 500,
          initContent: function () {
            $("div#save_config_window").jqxWindow("focus");
          },
          isModal: true
        });
        $("div#save_config_window").jqxWindow("open");
        $("input#save_config_service_name_txt_component").val("");
        $("input#save_config_service_name_txt_component").select();
      }
      
      function event_a_menu_save_click(event) {
        if ($("div#source_data_tab_component").jqxTabs("length") != 0 &&
            getTabContent().find("input.is_changed").val() != "false") {
          $("input#save_config_service_name_txt_component").val("");
          cmnSyncCall("SaveService", {service_num: getTabContent().find("input.service_num").val(),
                                      source: editorArray[Number(getTabContent().find("input.editor_num").val())].getDoc().getValue()},
                       callback, null);
        }
      }
      
      function event_a_menu_save_as_click(event) {
        if ($("div#source_data_tab_component").jqxTabs("length") != 0) {
          saveMode = 1;
          $("div#save_window").jqxWindow({resizable: false, height: 100, width: 500,
            initContent: function () {
              $("div#save_window").jqxWindow("focus");
            },
            isModal: true
          });
          $("input#save_name_txt").val(getTabContent().find("input.service_name").val());
          $("div#save_window").jqxWindow("open");
          $("input#save_name_txt").select();
        }
      }
      
      function event_a_menu_distribute(e) {
        if ($("div#source_data_tab_component").jqxTabs("length") != 0 &&
            getTabContent().find("input.is_changed").val() == "false") {
          cmnSyncCall("Distribute", {service_num: getTabContent().find("input.service_num").val()},
                    callback, null);
        }
      }
      
      function event_a_menu_delete(e) {
        if ($("div#source_data_tab_component").jqxTabs("length") != 0 &&
            getTabContent().find("input.is_exist_no_dist").val() == "Y") {
          cmnSyncCall("DeleteLastRepVer", {query_num: getTabContent().find("input.query_num").val()}, callback, null);
        }
      }

      function event_a_menu_test(e) {
        $("div#test_window_content_panel_component").jqxPanel("clearcontent");
        addTestInputParameterComponent();
        $("div#test_input_window").jqxWindow("open");
      }

      function event_rep_ver_change(event) {
        if (g_rep_ver_drop_down_system_change == false) {
          if (parseInt(getTabContent().find("input.rep_ver").val())
              != $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 4].tool.val()) {
            cmnSyncCall("GetServiceInfo", {service_num: getTabContent().find("input.service_num").val(),
                                           rep_ver: $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 4].tool.val()}, callback, null);
          }
        }
      }
      
      function event_pgm_change(event) {
        var pgmInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 3].tool;
        cmnSyncCall("GetTaskList", {pgm_num: pgmInfoTool.val()}, callback, null);
      }
      
      function event_task_change(event) {
        var pgmInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 3].tool;
        var taskInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 2].tool;
        cmnSyncCall("GetPageList", {pgm_num: pgmInfoTool.val(), task_num: taskInfoTool.val()}, callback, null);        
      }
      
      function event_page_change(event) {
        var pgmInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 3].tool;
        var taskInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 2].tool;
        var pageInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 1].tool;
        $("input#search_text_component").val("");
        cmnSyncCall("GetServiceList", {pgm_num: pgmInfoTool.val(), task_num: taskInfoTool.val(), page_num: pageInfoTool.val()}, callback, null);
      }
      
      function event_data_list_component_select(event) {
        var sourceTabNum = isExistSourceTab($("div#data_list_component").val());
        if (sourceTabNum >= 0) {
          $("div#source_data_tab_component").jqxTabs("select", sourceTabNum);
        } else {
          cmnSyncCall("GetServiceInfo", {service_num: $("div#data_list_component").val()}, callback, null);
        }
      }
      
      function event_save_config_service_ok_but_component_select(event) {
        if (g_isNew == true) {
          var pgmInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 3].tool;
          var taskInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 2].tool;
          var pageInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 1].tool;
          cmnSyncCall("InsertService", {pgm_num: pgmInfoTool.val(), task_num: taskInfoTool.val(), page_num: pageInfoTool.val(),
                                        service_name: $("input#save_config_service_name_txt_component").val()}, callback, null);
        } else {
          cmnSyncCall("SaveService", {service_num: getTabContent().find("input.service_num").val(),
                                     service_name: $("input#save_config_service_name_txt_component").val()},
                     callback, null);          
        }
      }
      
      function event_test_window_content_test_buton_component_click(event) {
        var keyParam = new Array();
        for (var i = 0; i < $("div.test_parameter").length; i++) {
          if (typeof $("div.test_parameter").eq(i).children("div.test_parameter_key").children("input").val() != "undefined" && $("div.test_parameter").eq(i).children("div.test_parameter_key").children("input").val() != ""
             && $("div.test_parameter").eq(i).children("div.test_parameter_key").children("input").val() != null) {
            keyParam.push({key: $("div.test_parameter").eq(i).children("div.test_parameter_key").children("input").val(), value: $("div.test_parameter").eq(i).children("div.test_parameter_value").children("input").val()});
          }
        }
        cmnSyncCall("TestService", {service_num: getTabContent().find("input.service_num").val(),
                                    param: JSON.stringify(keyParam),
                                    rep_ver: getTabContent().find("input.rep_ver").val()},
                     callback, null);
      }
      
      function event_source_data_tab_component_removed(event) {
        setRepVerCombo();
        setEnableButton();        
      }

      function event_source_data_tab_component_selected(event) {
        setRepVerCombo();
        setEnableButton();        
      }

      function callback(data, act, input_param, callbackVar) {
        if (act == "GetPgmList") {
          var pgmInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 3].tool;
          pgmInfoTool.jqxDropDownList("clear");
          for (var i = 0; i < data.length; i++) {
            pgmInfoTool.jqxDropDownList("addItem", {label: data[i].pgm_name, value: data[i].pgm_num});
          }
          if (data.length != 0) {
            pgmInfoTool.jqxDropDownList("selectIndex", 0);
          }
        } else if (act == "GetTaskList") {
          var taskInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 2].tool;
          taskInfoTool.jqxDropDownList("clear");
          for (var i = 0; i < data.length; i++) {
            taskInfoTool.jqxDropDownList("addItem", {label: data[i].task_nm, value: data[i].task_num});
          }
          if (data.length != 0) {
            taskInfoTool.jqxDropDownList("selectIndex", 0);
          }
        } else if (act == "GetPageList") {
          var pageInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 1].tool;
          pageInfoTool.jqxDropDownList("clear");
          for (var i = 0; i < data.length; i++) {
            pageInfoTool.jqxDropDownList("addItem", {label: data[i].page_nm, value: data[i].page_num});
          }
          if (data.length != 0) {
            pageInfoTool.jqxDropDownList("selectIndex", 0);
          }
        } else if (act == "GetServiceList") {
          $("div#data_list_component").jqxListBox("clear");
          for (var i = 0; i < data.length; i++) {
            $("div#data_list_component").jqxListBox("addItem", {label: data[i].service_name, value: data[i].service_num});
          }
        } else if (act == "GetServiceInfo" && typeof input_param.rep_ver == "undefined") {
          addNewServiceTabWithService($("div#data_list_component").jqxListBox("getSelectedItems")[0].label,
                                      $("div#data_list_component").jqxListBox("getSelectedItems")[0].value, data.source,
                                      data.rep_ver, data.max_rep_ver, data.is_auth, data.is_exist_no_dist,
                                      JSON.stringify(data.service_rep_info));
        } else if (act == "GetServiceInfo" && typeof input_param.rep_ver != "undefined") {
          var editorNum = parseInt(getTabContent().find("input.editor_num").val());
          var codeMirrorEditor = editorArray[editorNum];
          getTabContent().find("input.rep_ver").val(data.rep_ver);
          getTabContent().find("input.max_rep_ver").val(data.max_rep_ver);
          getTabContent().find("input.is_auth").val(data.is_auth);
          getTabContent().find("input.is_exist_no_dist").val(data.is_exist_no_dist);
          getTabContent().find("input.rep_ver_info").val(JSON.stringify(data.service_rep_info));
          getTabContent().find("input.is_changed").val("false");
          codeMirrorEditor.getDoc().setValue(data.source);
          codeMirrorEditor.getDoc().clearHistory();
          $("div#source_data_tab_component").jqxTabs("setTitleAt", $("div#source_data_tab_component").val(), getTabContent().find("input.service_name").val());
          setRepVerCombo();
          setEnableButton();
        } else if (act == "InsertService") {
          alert("정상적으로 저장되었습니다.");
          addNewServiceTabWithService($("input#save_config_service_name_txt_component").val(), data.service_num, data.source, 1, 1, "Y", "Y", "{}");
          var pgmInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 3].tool;
          var taskInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 2].tool;
          var pageInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 1].tool;
          cmnSyncCall("GetServiceList", {pgm_num: pgmInfoTool.val(), task_num: taskInfoTool.val(), page_num: pageInfoTool.val(), search_text: $("input#search_text_component").val()}
                      , callback, null);
          $("div#save_config_window").jqxWindow("close");
        } else if (act == "SaveService") {
          alert("정상적으로 처리되었습니다.");
          if ($("input#save_config_service_name_txt_component").val() != "") {
            getTabContent().find("input.service_name").val($("input#save_config_service_name_txt_component").val());
            $("input#save_config_service_name_txt_component").val("");
          }
          getTabContent().find("input.is_changed").val("false");
          $("div#source_data_tab_component").jqxTabs("setTitleAt", $("div#source_data_tab_component").val(), getTabContent().find("input.service_name").val());
          var maxRepVer = parseInt(getTabContent().find("input.max_rep_ver").val()) + 1;
          getTabContent().find("input.max_rep_ver").val("" + maxRepVer);
          setRepVerCombo();
          setEnableButton();
        } else if (act == "TestService") {
          $("div#test_input_window").jqxWindow("close");
          $("div#test_window_content_output_compile_console_component").jqxPanel("clearcontent");
          $("div#test_window_content_output_run_console_component").jqxPanel("clearcontent");
          $("div#test_window_content_output_result_console_component").jqxPanel("clearcontent");
          $("div#test_window_content_output_log_console_component").jqxPanel("clearcontent");
          $("div#test_window_content_output_compile_console_component").jqxPanel("append", $("<div>", {css: {whiteSpace: "nowrap"}}).html());
          $("div#test_window_content_output_run_console_component").jqxPanel("append", $("<div>", {css: {whiteSpace: "nowrap"}}).html());
          $("div#test_window_content_output_result_console_component").jqxPanel("append", $("<div>", {css: {whiteSpace: "nowrap"}}).html());
          cmnSyncCall("GetTestLog", {test_key: data.test_key}, callback, null);
          $("div#test_output_window").jqxWindow("open");
        } else if (act == "GetTestLog") {
          getTestLog(input_param.test_key, data.min_log_seq, data.log_list, data.output_data, data.test_success_yn, data.test_process_nm, data.test_err_nm);
        }
      }
      
      function getTestLog(test_key, min_log_seq, log_list, output_data, test_success_yn, test_process_nm, test_err_nm) {
        var processState = test_process_nm;
        if (typeof test_err_nm != "undefined" && test_err_nm != null && test_err_nm != "") {
          processState = processState + "[" + test_err_nm + "]";
        }
        $("div#test_window_content_state").html(test_process_nm);
        for (var i = 0; i < log_list.length; i++) {
          if (log_list[i].log_typ == 1) {
            var strList = log_list[i].log_msg.split("\n");
            for (var j = 0; j < strList.length; j++) {
              strList[j] = strList[j].replace(/ /gi, "&nbsp");
              if (j == 0) {
                var lastContent = $("div#test_window_content_output_compile_console_component").children("div").eq(0).children("div").eq(0).children("div").last();
                lastContent.append($("<font>", {class: "general"}).html(strList[j]));
              } else {
                $("div#test_window_content_output_compile_console_component").jqxPanel("append", $("<div>", {css: {whiteSpace: "nowrap"}}).append($("<font>", {class: "general"}).html(strList[j])));
              }
            }
          } else if (log_list[i].log_typ == 2) {
            var strList = log_list[i].log_msg.split("\n");
            for (var j = 0; j < strList.length; j++) {
              strList[j] = strList[j].replace(/ /gi, "&nbsp");
              if (j == 0) {
                var lastContent = $("div#test_window_content_output_compile_console_component").children("div").eq(0).children("div").eq(0).children("div").last();
                lastContent.append($("<font>", {class: "error"}).html(strList[j]));
              } else {
                $("div#test_window_content_output_compile_console_component").jqxPanel("append", $("<div>", {css: {whiteSpace: "nowrap"}}).append($("<font>", {class: "error"}).html(strList[j])));
              }
            }
          } else if (log_list[i].log_typ == 3) {
            var strList = log_list[i].log_msg.split("\n");
            for (var j = 0; j < strList.length; j++) {
              strList[j] = strList[j].replace(/ /gi, "&nbsp");
              if (j == 0) {
                var lastContent = $("div#test_window_content_output_run_console_component").children("div").eq(0).children("div").eq(0).children("div").last();
                lastContent.append($("<font>", {class: "general"}).html(strList[j]));
              } else {
                $("div#test_window_content_output_run_console_component").jqxPanel("append", $("<div>", {css: {whiteSpace: "nowrap"}}).append($("<font>", {class: "general"}).html(strList[j])));
              }
            }
          } else if (log_list[i].log_typ == 4) {
            var strList = log_list[i].log_msg.split("\n");
            for (var j = 0; j < strList.length; j++) {
              strList[j] = strList[j].replace(/ /gi, "&nbsp");
              if (j == 0) {
                var lastContent = $("div#test_window_content_output_run_console_component").children("div").eq(0).children("div").eq(0).children("div").last();
                lastContent.append($("<font>", {class: "error"}).html(strList[j]));
              } else {
                $("div#test_window_content_output_run_console_component").jqxPanel("append", $("<div>", {css: {whiteSpace: "nowrap"}}).append($("<font>", {class: "error"}).html(strList[j])));
              }
            }
          } else if (log_list[i].log_typ == 5) {
            var strList = log_list[i].log_msg.split("\n");
            var log_type = 0;
            for (var j = 0; j < strList.length; j++) {
              strList[j] = strList[j].replace(/ /gi, "&nbsp");
              if (log_list[i].log_msg.length >= 8 && (log_list[i].log_msg.substring(0, 7) == "[DEBUG]"
                                                      || log_list[i].log_msg.substring(0, 8) == "[TRACE]"
                                                      || log_list[i].log_msg.substring(0, 6) == "[INFO]")) {
                $("div#test_window_content_output_log_console_component").jqxPanel("append", $("<div>", {css: {whiteSpace: "nowrap"}}).append($("<font>", {class: "general"}).html(strList[j])));
              } else if (log_list[i].log_msg.length >= 6 && log_list[i].log_msg.substring(0, 6) == "[WARN]") {
                $("div#test_window_content_output_log_console_component").jqxPanel("append", $("<div>", {css: {whiteSpace: "nowrap"}}).append($("<font>", {class: "warn"}).html(strList[j])));
              } else if (log_list[i].log_msg.length >= 7 && log_list[i].log_msg.substring(0, 7) == "[ERROR]") {
                $("div#test_window_content_output_log_console_component").jqxPanel("append", $("<div>", {css: {whiteSpace: "nowrap"}}).append($("<font>", {class: "error"}).html(strList[j])));
              } else {
                if ($("div#test_window_content_output_run_console_component").children("div").eq(0).children("div").eq(0).length == 0) {
                  $("div#test_window_content_output_log_console_component").jqxPanel("append", $("<div>", {css: {whiteSpace: "nowrap"}}).append($("<font>", {class: "general"}).html(strList[j])));
                } else {
                  var lastContent = $("div#test_window_content_output_run_console_component").children("div").eq(0).children("div").eq(0).children("div").last();
                  var className = lastContent.children("font").attr("class");
                  $("div#test_window_content_output_log_console_component").jqxPanel("append", $("<div>", {css: {whiteSpace: "nowrap"}}).append($("<font>", {class: className}).html(strList[j])));
                }
              }
            }
          }
        }
        $("div#test_window_content_output_result_console_component").jqxPanel("append", output_data);
        if (typeof test_success_yn == "undefined" || test_success_yn == null || (test_success_yn != "N" && test_success_yn != "Y")) {
          setTimeout("getTestLogTimer(\"" + test_key + "\", " + min_log_seq + ")", 1000);
        }
      }
      
      function getTestLogTimer(test_key, min_log_seq) {
        cmnSyncCall("GetTestLog", {test_key: test_key, min_log_seq: min_log_seq}, callback, null);
      }
      
      function isExistSourceTab(serviceNum) {
        for (var i = 0; i < $("div#source_data_tab_component").jqxTabs("length"); i++) {
          if (getTabContent(i).find("input.service_num").val() == ("" + serviceNum)) {
            return i;
          }
        }
        return -1;
      }
    
      function addNewServiceTabWithService(serviceName, serviceNum, source, repVer, maxRepVer, isAuth, isExistNoDist, repVerInfo) {
        var contents = $("<div>", {});
        contents.append($("<input>", {type: "hidden", class: "service_num", value: "" + serviceNum}));
        contents.append($("<input>", {type: "hidden", class: "service_name", value: serviceName}));
        contents.append($("<input>", {type: "hidden", class: "is_changed", value: "false"}));
        contents.append($("<input>", {type: "hidden", class: "rep_ver", value: "" + repVer}));
        contents.append($("<input>", {type: "hidden", class: "source", value: source}));
        contents.append($("<input>", {type: "hidden", class: "max_rep_ver", value: maxRepVer}));
        contents.append($("<input>", {type: "hidden", class: "is_auth", value: isAuth}));
        contents.append($("<input>", {type: "hidden", class: "is_exist_no_dist", value: isExistNoDist}));
        contents.append($("<input>", {type: "hidden", class: "rep_ver_info", value: repVerInfo}));
        contents.append($("<input>", {type: "hidden", class: "editor_num", value: "" + editorNum}));
        var editor = $("<textarea>", {class: "service_editor", css: {width: "100%", height: "100%"}});
        contents.append(editor);
        $("div#source_data_tab_component").jqxTabs("addLast", serviceName, contents.html());
        var codeMirrorEditor = CodeMirror.fromTextArea(getTabContent($("div#source_data_tab_component").jqxTabs("length") - 1).find("textarea.service_editor")[0], {
          mode: "text/x-java",
          lineNumbers: true,
          lineWrapping: false,
          indentWithTabs: true,
          indentUnit: 4,
          foldGutter: {
            rangeFinder: new CodeMirror.fold.combine(CodeMirror.fold.brace, CodeMirror.fold.comment)
          },
          gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"]
        });
        editorArray[editorNum] = codeMirrorEditor;
        editorNum++;
        codeMirrorEditor.setOption("theme", g_cmEditorTheme);
        codeMirrorEditor.getWrapperElement().style["font-family"] = "Oxygen Mono";
        codeMirrorEditor.refresh();
        codeMirrorEditor.setSize("100%", "100%");
        codeMirrorEditor.getDoc().setValue(source);
        codeMirrorEditor.getDoc().clearHistory();
        codeMirrorEditor.on("change", function(cm, changeObj) {
          if (cm.getDoc().historySize().undo == 0 && getTabContent().find("input.is_changed").val() == "true") {
            $("div#source_data_tab_component").jqxTabs("setTitleAt", $("div#source_data_tab_component").val(), getTabContent().find("input.service_name").val());
            getTabContent().find("input.is_changed").val("false");
            cm.refresh();
            cm.getDoc().setCursor(cm.getDoc().getCursor());
          } else if (getTabContent().find("input.is_changed").val() == "false") {
            $("div#source_data_tab_component").jqxTabs("setTitleAt", $("div#source_data_tab_component").val(), "*" + getTabContent().find("input.service_name").val());
            getTabContent().find("input.is_changed").val("true");
            cm.refresh();
            cm.getDoc().setCursor(cm.getDoc().getCursor());
          }
          setRepVerCombo();
          setEnableButton();
        });
        $("div#source_data_tab_component").jqxTabs("select", $("div#source_data_tab_component").jqxTabs("length") - 1);
        setRepVerCombo();
        setEnableButton();
      }
      
      function setRepVerCombo() {
        g_rep_ver_drop_down_system_change = true;
        var repVerInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 4].tool;
        repVerInfoTool.jqxDropDownList("clear");
        if ($("div#source_data_tab_component").jqxTabs("length") == 0) {
          return;
        } else {
          var repVer = parseInt(getTabContent().find("input.rep_ver").val());
          var maxRepVer = parseInt(getTabContent().find("input.max_rep_ver").val());
          var repVerInfo = JSON.parse(getTabContent().find("input.rep_ver_info").val());
          for (var i = 0; i < maxRepVer; i++) {
            var label = "" + (maxRepVer - i);
            var distNum = 0;
            for (var j = 0; j < repVerInfo.length; j++) {
              if (repVerInfo[j].rep_ver == (maxRepVer - i)) {
                if (distNum == 0) {
                  label = label + "(";
                } else {
                  label = label + ", ";
                }
                label = label + repVerInfo[j].dist_instance_nm
                distNum++;
              }
            }
            if (distNum != 0) {
              label = label + ")";
            }
            repVerInfoTool.jqxDropDownList("addItem", {label: label, value: (maxRepVer - i)});
            if (maxRepVer - i == repVer) {
              repVerInfoTool.jqxDropDownList("selectIndex", i);
            }
          }
        }
        g_rep_ver_drop_down_system_change = false;
      }
      
      function setEnableButton() {
        if ($("div#source_data_tab_component").jqxTabs("length") == 0 ||
            getTabContent().find("input.is_changed").val() == "false") {
          $("div#top_menu_icon_component").jqxToolBar("getTools")[1].tool.children("div").jqxButton("disabled", true);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_save", true);
        } else {
          $("div#top_menu_icon_component").jqxToolBar("getTools")[1].tool.children("div").jqxButton("disabled", false);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_save", false);
        }
        if ($("div#source_data_tab_component").jqxTabs("length") == 0) {
          $("div#top_menu_icon_component").jqxToolBar("getTools")[2].tool.children("div").jqxButton("disabled", true);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_save_as", true);
        } else {
          $("div#top_menu_icon_component").jqxToolBar("getTools")[2].tool.children("div").jqxButton("disabled", false);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_save_as", false);
        }
        if ($("div#source_data_tab_component").jqxTabs("length") != 0 &&
            getTabContent().find("input.is_changed").val() == "false") {
          $("div#top_menu_icon_component").jqxToolBar("getTools")[3].tool.children("div").jqxButton("disabled", false);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_deploy", false);
        } else {
          $("div#top_menu_icon_component").jqxToolBar("getTools")[3].tool.children("div").jqxButton("disabled", true);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_deploy", true);
        }
        if ($("div#source_data_tab_component").jqxTabs("length") != 0 &&
            getTabContent().find("input.is_exist_no_dist").val() == "Y") {
          $("div#top_menu_icon_component").jqxToolBar("getTools")[4].tool.children("div").jqxButton("disabled", false);          
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_delete", false);
        } else {
          $("div#top_menu_icon_component").jqxToolBar("getTools")[4].tool.children("div").jqxButton("disabled", true);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_delete", true);
        }
        if ($("div#source_data_tab_component").jqxTabs("length") != 0) {
          g_rep_ver_drop_down_system_change = true;
          setTimeout(function() {g_rep_ver_drop_down_system_change = false;}, 500);
          $("div#top_menu_icon_component").jqxToolBar("getTools")[6].tool.jqxDropDownList("disabled", false);   
        } else {
          g_rep_ver_drop_down_system_change = true;
          setTimeout(function() {g_rep_ver_drop_down_system_change = false;}, 500);
          $("div#top_menu_icon_component").jqxToolBar("getTools")[6].tool.jqxDropDownList("disabled", true);
        }        
      }
        
      function queryValidation() {
        var editor_num = getTabContent().find("input.editor_aiv").val();
        if (editor[editor_num].getDoc().getValue().trim() == "") {
          alert("정상적인 쿼리를 입력하세요.");
          return false;
        } else {
          for (var i = 0; i < $("div#input_param").jqxGrid("getdatainformation").rowscount; i++) {
            if ($("div#input_param").jqxGrid("getrenderedrowdata", i).name.trim() == "") {
              alert("파라미터에 값을 넣으세요.");
              return false;
            }
          }
          for (var i = 0; i < $("div#output_param").jqxGrid("getdatainformation").rowscount; i++) {
            if ($("div#output_param").jqxGrid("getrenderedrowdata", i).name.trim() == "") {
              alert("파라미터에 값을 넣으세요.");
              return false;
            }
          }
        }
        return true;
      }
      
      function addTestInputParameterComponent() {
        var input_parameter = $("<div>", {class: "test_parameter", css: {position: "relative", left: "0px", width: "100%", height: "30px"}});
        var input_parameter_key = $("<div>", {class: "test_parameter_key", css: {position: "absolute", left: "10px", width: "200px", top: "2px", height: "20px"}});
        var input_parameter_key_component = $("<input>", {type: "text", css: {position: "absolute", left: "0px", width: "100%", top: "0px", height: "100%"}});
        input_parameter_key_component.jqxInput({
          placeHolder: "key",
          width: "100%",
          height: "100%"
        });
        var input_parameter_value = $("<div>", {class: "test_parameter_value", css: {position: "absolute", left: "225px", right: "70px", top: "2px", height: "20px"}});
        var input_parameter_value_component = $("<input>", {type: "text", css: {position: "absolute", left: "0px", right: "0px", top: "0px", height: "100%"}});
        input_parameter_value_component.jqxInput({
          placeHolder: "value",
          width: "100%",
          height: "100%"
        });
        var input_parameter_plus_but = $("<a>", {class: "test_parameter_plus_but", onClick: "test_parameter_plus_but_click(this);", css: {position: "absolute", width: "20px", right: "35px", top: "2px", height: "20px"}});
        var input_parameter_del_but = $("<a>", {class: "test_parameter_del_but", css: {position: "absolute", width: "20px", right: "10px", top: "2px", height: "20px"}});
        input_parameter_del_but.on("click", test_parameter_del_but_click);
        var input_parameter_plus_but_img = $("<img>", {src: "/FileDown?file_key=UtZ5mzcUPNjnNWjCwMG7xe6ecvrg3oxoCobS1rVD", css: {position:"absolute", width: "100%", height: "100%", left: "0px", top: "0px"}});
        var input_parameter_pdel_but_img = $("<img>", {src: "/FileDown?file_key=1YctFgMgRu9r1XkxJXeo2ZJrowJ4LXBCuParLx4L", css: {position:"absolute", width: "100%", height: "100%", left: "0px", top: "0px"}});
        input_parameter_plus_but.append(input_parameter_plus_but_img);
        input_parameter_del_but.append(input_parameter_pdel_but_img);
        input_parameter.append(input_parameter_plus_but);
        input_parameter.append(input_parameter_del_but);
        input_parameter_key.append(input_parameter_key_component);
        input_parameter_value.append(input_parameter_value_component);
        input_parameter.append(input_parameter_key).append(input_parameter_value);
        $("div#test_window_content_panel_component").jqxPanel("append", input_parameter);
      }
      
      function test_parameter_plus_but_click(obj) {
        addTestInputParameterComponent();
      }
      
      function test_parameter_del_but_click() {
        var parentObj = $(this).parent().parent();
        $(this).parent().remove();
        if (parentObj.children("div").length == 0) {
          addTestInputParameterComponent();
        }
      }
      
      function getTabContent(tabNumber) {
        if (typeof tabNumber == "undefined" || tabNumber == null) {
          return $($("div#source_data_tab_component").jqxTabs("getContentAt", $("div#source_data_tab_component").val()));
        } else {
          return $($("div#source_data_tab_component").jqxTabs("getContentAt", tabNumber));
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
                <li id="li_menu_new">
                  <a id="menu_new">New</a>
                </li>
                <li type="separator"></li>
                <li id="li_menu_save">
                  <a id="menu_save">Save</a>
                </li>
                <li id="li_menu_save_as">
                  <a id="menu_save_as">Save As...</a>
                </li>
              </ul>
            </li>
            <li>Develop
              <ul>
                <li id="li_menu_deploy">
                  <a id="menu_deploy">Deploy</a>
                </li>
                <li id="li_menu_delete">
                  <a id="menu_delete">Ver. Delete</a>
                </li>
                <li type='separator'></li>
                <li id="li_menu_test">
                  <a id="menu_test">Test</a>
                </li>
              </ul>
            </li>
            <li>Help
              <ul>
                <li>
                  <a id="help_about">Help</a>
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
      <div class="top_menu_icon">
        <div id="top_menu_icon_component">
        </div>
      </div>
      <div class="content">
        <div id="left_right_splitter_component">
          <div class="left_splitter">
            <div class="search_text">
              <input id="search_text_component"/>
            </div>
            <div class="data_list">
              <div id="data_list_component"></div>
            </div>
          </div>
          <div class="right_splitter">
            <div class="source_data_tab">
              <div id="source_data_tab_component">
                <ul>
                  <li></li>
                </ul>
                <div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div id="save_config_window" class="save_config_window">
      <div class="save_config_window_header">
        다른이름으로 저장
      </div>
      <div style="overflow: hidden;" id="windowContent">
        <div class="save_config_service_name_txt">
          <input id="save_config_service_name_txt_component"/>
        </div>
        <div class="save_config_service_ok_but">
          <input id="save_config_service_ok_but_component" value="Save"/>
        </div>
        <div class="save_config_service_cancel_but">
          <input id="save_config_service_cancel_but_component" value="Cancel"/>
        </div>
      </div>
    </div>
    <div id="test_input_window" class="test_input_window">
      <div class="test_input_window_header">
        서비스 테스트
      </div>
      <div style="overflow: hidden;" id="test_window_content">
        <div class="test_window_content_panel">
          <div id="test_window_content_panel_component">
          </div>
        </div>
        <div class="test_window_content_test_button">
          <input type="button" id="test_window_content_test_button_component" value="Test"/>
        </div>
        <div class="test_window_content_cancel_button">
          <input type="button" id="test_window_content_cancel_button_component" value="Cancel"/>
        </div>
      </div>
    </div>
    <div id="test_output_window" class="test_output_window">
      <div class="test_output_window_header">
        서비스 테스트
      </div>
      <div style="overflow: hidden;" id="test_window_content">
        <div class="test_window_content_state">
          <div id="test_window_content_state">
          </div>
        </div>
        <div class="test_window_content_output_compile_console">
          <div id="test_window_content_output_compile_console_component">
          </div>
        </div>
        <div class="test_window_content_output_run_console">
          <div id="test_window_content_output_run_console_component">
          </div>
        </div>
        <div class="test_window_content_output_log_console">
          <div id="test_window_content_output_log_console_component">
          </div>
        </div>
        <div class="test_window_content_output_result_console">
          <div id="test_window_content_output_result_console_component">
          </div>
        </div>
      </div>
    </div>
  </body>
</html>