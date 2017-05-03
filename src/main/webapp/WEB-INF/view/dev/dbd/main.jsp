<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>KOKODOG[MyDBIO]</title>
    <link rel="shortcut icon" type="image/x-icon" href="/FileDown?file_key=tId6m0peoFkF2GYcAfAJG48kOE5Djo5ky49MFtnX" />
    <link rel="stylesheet" href="/css/dev/dbd/main.css"/>
    <link rel="stylesheet" href="https://jqwidgets.com/public/jqwidgets/styles/jqx.base.css" type="text/css"/>
    <link href="https://fonts.googleapis.com/css?family=Oxygen+Mono" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
    <script type="text/javascript" src="https://jqwidgets.com/public/jqwidgets/jqx-all.js"></script>
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
      var g_rep_ver_drop_down_system_change = false;
      var g_cmEditorTheme = "eclipse";

      /* Document 로드 시에 발생하는 시작 함수 */
      $(document).ready(function() {
        contentInitLoad();
        contentEventLoad();
        $("div#source_data_tab_component").jqxTabs("removeFirst");
        GetDBIOList(1);
      });
      
      /* jqWidget을 사용하는 각종 함수 들 첫 오픈 처리 */
      function contentInitLoad() {
        $("div#top_menu_bar_component").jqxMenu({
          width: "100%",
          height: "100%"
        });
        $("div#top_menu_icon_component").jqxToolBar({
          tools: "custom | custom custom | custom | custom custom | dropdownlist",
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
        $("input#save_config_dbio_name_txt_component").jqxInput({
          placeHolder: "DBIO Name...",
          height: "100%",
          width: "100%"          
        });
        $("input#save_config_dbio_ok_but_component").jqxButton({
          height: "100%",
          width: "100%"          
        });
        $("input#save_config_dbio_cancel_but_component").jqxButton({
          height: "100%",
          width: "100%"          
        });
        $("div#test_window").jqxWindow({
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
        $("div#test_input_component").jqxGrid({
          width: "100%",
          height: "100%",
          sortable: true,
          scrollmode: "logical",
          editable: true,
          editmode: "programmatic",
          columnsresize: true,
          showtoolbar: false,
          selectionmode: "singlerow",
          columns: [
            {text: "Parameter Name", columntype: "textbox", width: "30%", datafield: "parameter_name"},
            {text: "Parameter Value", columntype: "textbox", width: "70%", datafield: "parameter_value"}
          ]
        });
        $("div#test_output_component").jqxGrid({
          width: "100%",
          height: "100%",
          sortable: false,
          scrollmode: "logical",
          columnsresize: true,
          editable: false,
          selectionmode: "singlecell",
          columns: []
        });
        $("div#test_input_component").on("rowclick", function(event) {
          $("div#test_input_component").jqxGrid("endcelledit", event.args.rowindex, "parameter_value");
        });
        $("div#test_input_component").on("rowdoubleclick", function(event) {
          $("div#test_input_component").jqxGrid("begincelledit", event.args.rowindex, "parameter_value");
        });
        $("input#test_ok_but_component").jqxButton({
          height: "100%",
          width: "100%"          
        });
        $("input#test_cancel_but_component").jqxButton({
          height: "100%",
          width: "100%"          
        });
      }

      /* jqWidget을 사용하는 각종 이벤트들 맵핑 처리 */
      function contentEventLoad() {
        $("a#menu_new").click(event_a_menu_new_click);
        $("a#menu_save").click(event_a_menu_save_click);
        $("a#menu_save_as").click(event_a_menu_save_as_click);
        $("a#menu_deploy").click(event_a_menu_distributequery);
        $("a#menu_delete").click(event_a_menu_deletequery);
        $("a#menu_test").click(event_a_menu_test);
        $("div#source_data_tab_component").on("removed", event_source_data_tab_component_removed);
        $("div#source_data_tab_component").on("selected", event_source_data_tab_component_selected);
        $("div#data_list_component").on("select", event_data_list_component_select);
        $("input#search_text_component").keyup(event_search_text_component_keyup);
        $("input#save_config_dbio_ok_but_component").on("click", event_save_config_dbio_ok_but_component_click);
        $("input#save_config_dbio_cancel_but_component").on("click", event_save_config_dbio_cancel_but_component_click);
        $("input#test_ok_but_component").on("click", event_test_ok_but_component_click);
        $("input#test_cancel_but_component").on("click", event_test_cancel_but_component_click);
      }
      
      /* jsource_data_tab_component(메뉴 상단 아이콘 버튼) 신규 맵핑 처리 */
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
            distrubteButton.on("click", event_a_menu_distributequery);
            break;
          case 4:
            var deleteButton = $("<div>" + "<img src='/FileDown?file_key=YwN3sUbnW2f7T2YrLy5lbLUztLD9EWDIyP3v6g4A' title='Ver. Delete' style='width:20px;height:20px;'/>" + "</div>");
            tool.append(deleteButton);
            deleteButton.jqxButton({
              width: "20px",
              height: "20px"
            });
            deleteButton.on("click", event_a_menu_deletequery);
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
          default:
            break;
        }
      }

      /* New Icon을 클릭하거나 혹은 File - New 클릭 시 새 창 오픈해주는 함수 */
      function event_a_menu_new_click(e) {
        addNewQueryTab(0, "", "", 0, "", 0, "Y", "N");
      }

      /* Save Icon을 클릭하거나 혹은 File - Save 클릭 시 새 창 오픈해주는 함수 */
      function event_a_menu_save_click(e) {
        if (getTabContent().find("input.is_changed").val() == "false" && 
           getTabContent().find("input.query_num").val() != "0") {
          return;
        }
        var queryNum = parseInt(getTabContent().find("input.query_num").val());
        if (typeof queryNum == "undefined" || queryNum == null || queryNum == "" || queryNum == 0) {
          $("input#save_config_dbio_name_txt_component").val("");
          $("div#save_config_window").jqxWindow("open");
        } else {
          saveDBIOWithPrevQueryName();
        }
      }
      
      function event_a_menu_save_as_click(e) {
        if (getTabContent().find("input.query_num").val() == "0") {
          return;
        }
        var queryNum = parseInt(getTabContent().find("input.query_num").val());
        var queryName = "";
        if (typeof queryNum != "undefined" && queryNum != null && queryNum != "" && queryNum != 0) {
          queryName = getTabContent().find("input.query_name").val();
        }
        $("input#save_config_dbio_name_txt_component").val(queryName);
        $("div#save_config_window").jqxWindow("open");
        $("input#save_config_dbio_name_txt_component").jqxInput("focus");
        $("input#save_config_dbio_name_txt_component").jqxInput("selectAll");
      }

      function event_a_menu_distributequery(e) {
        if (getTabContent().find("input.is_changed").val() != "false" ||
           getTabContent().find("input.query_num").val() == "0") {
          return;
        }
        distributeQuery();
      }
      
      function event_a_menu_deletequery(e) {
        if (getTabContent().find("input.is_exist_no_dist").val() != "Y") {
          return;
        }
        cmnSyncCall("DeleteLastRepVer", {query_num: getTabContent().find("input.query_num").val()}, callback, null);
      }

      function event_a_menu_test(e) {
        $("div#test_input_component").css("display", "block");
        $("div#test_output_component").css("display", "none");
        $("div.test_output").css("display", "none");
        $("div.test_input").css("display", "block");
        $("div#test_input_component").jqxGrid("clear");
        var inputList = getAllValuList(editorArray[getTabContent().find("input.editor_num").val()].
                             getDoc().getValue());
        for (var i = 0; i < inputList.length; i++) {
          $("div#test_input_component").jqxGrid("addrow", null, {parameter_name: inputList[i], parameter_value: ""}, "last");
        }
        $("div#test_window").jqxWindow("open");
      }

      function event_source_data_tab_component_removed() {
        if ($("div#source_data_tab_component").jqxTabs("length") == 0) {
          addNewQueryTab(0, "", "", 0, "", 0, "Y", "N");
        }
      }
      
      function event_source_data_tab_component_selected() {
        $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 1].tool.jqxDropDownList("disabled", false);
        setButtonEnable();
        setRepVerCombo();
      }
      
      function event_data_list_component_select(event) {
        var queryTabNum = dbioTabFindByQueryNum(event.args.item.value);
        if (queryTabNum <= -1) {
          cmnFindQueryInfo(event.args.item.value);          
        } else {
          $("div#source_data_tab_component").jqxTabs("select", queryTabNum);
        }
      }
      
      function event_search_text_component_keyup() {
        GetDBIOList(1);
      }
      
      function event_save_config_dbio_ok_but_component_click() {
        saveDBIOWithNewQueryName();
      }
      
      function event_save_config_dbio_cancel_but_component_click() {
        $("div#save_config_window").jqxWindow("close");
        $("input#save_config_dbio_name_txt_component").val("");
      }
      
      function event_rep_ver_change(event) {
        if (g_rep_ver_drop_down_system_change == false) {
          if (parseInt(getTabContent().find("input.rep_ver").val())
              != $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 1].tool.val()) {
            cmnSyncCall("GetQueryInfo", {query_num: getTabContent().find("input.query_num").val(),
                                        rep_ver: $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 1].tool.val()}, callback, null);
          }
        }
      }
      
      function event_test_ok_but_component_click(event) {
        var param = $("div#test_input_component").jqxGrid("getrows");
        var inputdata = new Array();
        for (var i = 0; i < param.length; i++) {
          inputdata[i] = {parameter_name: param[i].parameter_name, parameter_value: param[i].parameter_value};
        }
        cmnSyncCall("TestQuery", {query_param: JSON.stringify(inputdata), query: editorArray[getTabContent().find("input.editor_num").val()].getDoc().getValue()},
                    callback, null);
      }
      
      function event_test_cancel_but_component_click(event) {
        $("div#test_window").jqxWindow("close");
      }

      function callback(data, act, input_param, callbackVar) {
        if (act == "GetDBIOList") {
          var selectedIndex = $("div#data_list_component").jqxListBox("clearSelection");
          $("div#data_list_component").jqxListBox("clear");
          for (var i = 0; i < data.length; i++) {
            $("div#data_list_component").jqxListBox("addItem", {label: data[i].query_name, value: data[i].query_num});
          }
        } else if (act == "GetQueryInfo" && (typeof input_param.rep_ver == "undefined" || input_param.rep_ver == null)) {
          addNewQueryTab($("div#data_list_component").jqxListBox("getSelectedItems")[0].value,
                         $("div#data_list_component").jqxListBox("getSelectedItems")[0].label, data.query,
                         data.max_rep_ver, data.query_rep_info, data.rep_ver, data.is_auth, data.is_exist_no_dist);
          setButtonEnable();
          setRepVerCombo();
        } else if (act == "GetQueryInfo" && typeof input_param.rep_ver != "undefined" && input_param.rep_ver != null) {
          editorArray[getTabContent().find("input.editor_num").val()].getDoc().setValue(data.query);
          getTabContent().find("input.rep_ver").val(data.rep_ver);
          $("div#source_data_tab_component").jqxTabs("setTitleAt", $("div#source_data_tab_component").val(), getTabContent().find("input.query_name").val());
          editorArray[getTabContent().find("input.editor_num").val()].getDoc().clearHistory();
          setButtonEnable();
        } else if (act == "InsertQuery") {
          alert("정상적으로 저장되었습니다.");
          var queryNum = getTabContent().find("input.query_num").val();
          if (typeof queryNum == "undefined" || queryNum == null || queryNum == "" || queryNum == "0" || queryNum == 0) {
            getTabContent().find("input.query_num").val(data.query_num);
          }
          if ($("input#save_config_dbio_name_txt_component").val() != "") {
            getTabContent().find("input.query_name").val($("input#save_config_dbio_name_txt_component").val());           
          }
          getTabContent().find("input.is_changed").val("false");
          $("div#source_data_tab_component").jqxTabs("setTitleAt", $("div#source_data_tab_component").val(), getTabContent().find("input.query_name").val());
          if (data.is_update == false && typeof queryNum != "undefined" && queryNum != null && queryNum != "" && queryNum != "0" && queryNum != 0) {
            var repVerInfo = JSON.parse(getTabContent().find("input.rep_ver_info").val());
            var maxRepVer = parseInt(getTabContent().find("input.max_rep_ver").val());
            getTabContent().find("input.max_rep_ver").val("" + (maxRepVer + 1));
            getTabContent().find("input.rep_ver").val("" + (maxRepVer + 1));
          } else if (data.is_update == false) {
            getTabContent().find("input.max_rep_ver").val("1");
            getTabContent().find("input.rep_ver_info").val("[]");
            getTabContent().find("input.rep_ver").val("1");
            getTabContent().find("input.is_exist_no_dist").val("Y");
          }
          $("div#save_config_window").jqxWindow("close");
          $("input#save_config_dbio_name_txt_component").val("");
          GetDBIOList(1);
          setButtonEnable();
          setRepVerCombo();
        } else if (act == "DistributeQuery") {
          alert("정상적으로 처리되었습니다.");
          var repVerInfo = JSON.parse(getTabContent().find("input.rep_ver_info").val());
          var i = 0;
          for (i = 0; i < repVerInfo.length; i++) {
            if (repVerInfo[i].dist_instance == 1) {
              repVerInfo[i].rep_ver = input_param.rep_ver;
              break;
            }
          }
          if (i == repVerInfo.length) {
            repVerInfo.push({rep_ver: input_param.rep_ver, dist_instance: 1, dist_instance_nm: "개발기"});
          }
          getTabContent().find("input.rep_ver_info").val(JSON.stringify(repVerInfo));
          getTabContent().find("input.is_exist_no_dist").val("N");
          setButtonEnable();
          setRepVerCombo();
        } else if (act == "TestQuery") {
          var MAX_VALUE = 100;
          if (typeof data.output != "undefined") {
            $("div#test_output_component").css("display", "block");
            $("div.test_output").css("display", "block");
            var columnLength = new Array();
            $("div#test_output_component").jqxGrid("clear");
            var columns = new Array();
            for (var i = 0; i < data.output.column.length; i++) {
              columnLength[i] = data.output.column[i].length;
            }
            for (var i = 0; i < data.output.data.length; i++) {
              for (var j = 0; j < data.output.column.length; j++) {
                if (columnLength[j] == MAX_VALUE) {
                  continue;
                }
                if (data.output.data[i][data.output.column[j]] != null) {
                  var splitStrArr = ("" + data.output.data[i][data.output.column[j]]).split("\n");
                  for (var k = 0; k < splitStrArr.length; k++) {
                    if (splitStrArr[k].length > columnLength[j]) {
                      if (MAX_VALUE < data.output.data[i][data.output.column[j]].length) {
                        columnLength[j] = MAX_VALUE;
                      } else {
                        columnLength[j] = data.output.data[i][data.output.column[j]].length;
                      }
                    }
                  }
                }
              }
            }
            for (var i = 0; i < data.output.column.length; i++) {
              columns[i] = {text: data.output.column[i], columntype: "textbox", datafield: data.output.column[i], width: "" + (columnLength[i] * 10) + "px"};
            }
            $("div#test_output_component").jqxGrid("columns", columns);
            for (var i = 0; i < data.output.data.length; i++) {
              $("div#test_output_component").jqxGrid("addrow", null, data.output.data[i], "last");
            }
            $("div#test_input_component").css("display", "none");
            $("div.test_input").css("display", "none");
          }
        }
      }
      
      function cmnFindQueryInfo(queryNum) {
        cmnSyncCall("GetQueryInfo", {query_num: queryNum}, callback, null);
      }

      function addNewQueryTab(queryNum, queryName, query, maxRepVer, repVerInfo, repVer, isAuth, isExistNoDist) {
        var title = "";
        if (typeof queryNum == "undefined" || queryNum == null || queryNum == 0) {
          title = "New...";
        } else {
          title = queryName;
        }
        var content = $("<div>", {});
        content.append($("<input>", {
          type: "hidden",
          class: "query_num",
          value: "" + queryNum
        }));
        content.append($("<input>", {
          type: "hidden",
          class: "query_name",
          value: queryName
        }));
        content.append($("<input>", {
          type: "hidden",
          class: "max_rep_ver",
          value: "" + maxRepVer
        }));
        content.append($("<input>", {
          type: "hidden",
          class: "rep_ver_info",
          value: repVerInfo == "" ? JSON.stringify(new Array()) : JSON.stringify(repVerInfo)
        }));
        content.append($("<input>", {
          type: "hidden",
          class: "rep_ver",
          value: "" + repVer
        }));
        content.append($("<input>", {
          type: "hidden",
          class: "is_changed",
          value: "false"
        }));
        content.append($("<input>", {
          type: "hidden",
          class: "editor_num",
          value: "" + editorNum
        }));
        content.append($("<input>", {
          type: "hidden",
          class: "is_exist_no_dist",
          value: "" + isExistNoDist
        }));
        var editor = $("<textarea>", {class: "query_editor", css: {width: "100%", height: "100%"}});
        content.append(editor);
        $("div#source_data_tab_component").jqxTabs("addLast", title, content.html());
        var isReadOnly;
        if (typeof isAuth != "undefined" && (isAuth == false || isAuth == "N")) {
          isReadOnly = true;
        } else {
          isReadOnly = false;
        }
        var codeMirrorEditor = CodeMirror.fromTextArea(getTabContent($("div#source_data_tab_component").jqxTabs("length") - 1).find("textarea.query_editor")[0], {
          mode: "text/x-mysql",
          lineNumbers: false,
          lineWrapping: false,
          readOnly: isReadOnly,
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
        codeMirrorEditor.getDoc().setValue(query);
        codeMirrorEditor.getDoc().clearHistory();
        codeMirrorEditor.on("change", function(cm, changeObj) {
          var queryNum = getTabContent().find("input.query_num").val();
          if (typeof queryNum == "undefined" || queryNum == null || queryNum == "" || queryNum == "0") {
            if (cm.getDoc().historySize().undo == 0 && getTabContent().find("input.is_changed").val() == "true") {
              $("div#source_data_tab_component").jqxTabs("setTitleAt", $("div#source_data_tab_component").val(), "New...");
            } else if (getTabContent().find("input.is_changed").val() == "false") {
              $("div#source_data_tab_component").jqxTabs("setTitleAt", $("div#source_data_tab_component").val(), "* New...");
            }
          } else {
            if (cm.getDoc().historySize().undo == 0 && getTabContent().find("input.is_changed").val() == "true") {
              $("div#source_data_tab_component").jqxTabs("setTitleAt", $("div#source_data_tab_component").val(), getTabContent().find("input.query_name").val());
            } else if (getTabContent().find("input.is_changed").val() == "false") {
              $("div#source_data_tab_component").jqxTabs("setTitleAt", $("div#source_data_tab_component").val(), "*" + getTabContent().find("input.query_name").val());
            }
          }
          if (cm.getDoc().historySize().undo == 0 && getTabContent().find("input.is_changed").val() == "true") {
            getTabContent().find("input.is_changed").val("false");
            cm.refresh();
            cm.getDoc().setCursor(cm.getDoc().getCursor());
          } else if (getTabContent().find("input.is_changed").val() == "false") {
            getTabContent().find("input.is_changed").val("true");
            cm.refresh();
            cm.getDoc().setCursor(cm.getDoc().getCursor());
          }
          setButtonEnable();
        });
        $("div#source_data_tab_component").jqxTabs("select", $("div#source_data_tab_component").jqxTabs("length") - 1);
        if ($("div#source_data_tab_component").jqxTabs("length") == 2 && getTabContent(0).find("input.query_num").val() == "0" && getTabContent(0).find("input.is_changed").val() == "false") {
          $("div#source_data_tab_component").jqxTabs("removeAt", 0);
        }
      }
      
      function setRepVerCombo() {
        var repVerInfoTool = $("div#top_menu_icon_component").jqxToolBar("getTools")[$("div#top_menu_icon_component").jqxToolBar("getTools").length - 1].tool;
        var repVer = parseInt(getTabContent().find("input.rep_ver").val());
        var maxRepVer = parseInt(getTabContent().find("input.max_rep_ver").val());
        var repVerInfo = JSON.parse(getTabContent().find("input.rep_ver_info").val());
        repVerInfoTool.jqxDropDownList("clear");
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
      
      function saveDBIOWithNewQueryName() {
        if (saveDBIOValidationCheckWithQueryName() == false) {
          return;
        }
        var queryNum = getTabContent().find("input.query_num").val();
        var query = editorArray[getTabContent().find("input.editor_num").val()].getDoc().getValue();
        if (typeof queryNum == "undefined" || queryNum == null || queryNum == "" || queryNum == "0") {
          cmnSyncCall("InsertQuery", {query_name: $("input#save_config_dbio_name_txt_component").val(),
                                     query: query},
                     callback, null);
        } else {
          cmnSyncCall("InsertQuery", {query_num: queryNum, query_name: $("input#save_config_dbio_name_txt_component").val(),
                                     query: query},
                     callback, null);
        }
      }
      
      function saveDBIOValidationCheckWithQueryName() {
        var queryName = $("input#save_config_dbio_name_txt_component").val();
        if (typeof queryName == "undefined" || queryName == null || queryName == "") {
          alert("정확한 쿼리 명을 입력하세요");
          return false;
        }
        return true;
      }
      
      function saveDBIOWithPrevQueryName() {
        if (saveDBIOValidationCheckWithPrevQueryName() == false) {
          return;
        }
        var query = editorArray[getTabContent().find("input.editor_num").val()].getDoc().getValue();
        cmnSyncCall("InsertQuery", {query_num: getTabContent().find("input.query_num").val(),
                                   query: query},
                   callback, null);
      }
      
      function saveDBIOValidationCheckWithPrevQueryName() {
        return true;
      }
      
      function distributeQuery() {
        cmnSyncCall("DistributeQuery", {query_num: getTabContent().find("input.query_num").val(),
                                       rep_ver: getTabContent().find("input.rep_ver").val(),
                                       dist_instance: 1},
                   callback, null);        
      }
      
      function GetDBIOList(page) {
        cmnASyncCall("GetDBIOList", {page: page, search_txt: $("input#search_text_component").val()}, callback, null);
      }
            
      function dbioTabFindByQueryNum(queryNum) {
        for (var i = 0; i < $("div#source_data_tab_component").jqxTabs("length"); i++) {
          if (parseInt(getTabContent(i).find("input.query_num").val()) == queryNum) {
            return i;
          }
        }
        return -1;
      }
      
      function setButtonEnable() {
        if (getTabContent().find("input.is_changed").val() == "false" && 
           getTabContent().find("input.query_num").val() != "0") {
          $("div#top_menu_icon_component").jqxToolBar("getTools")[1].tool.children("div").jqxButton("disabled", true);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_save", true);
        } else {
          $("div#top_menu_icon_component").jqxToolBar("getTools")[1].tool.children("div").jqxButton("disabled", false);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_save", false);
        }
        if (getTabContent().find("input.query_num").val() == "0") {
          $("div#top_menu_icon_component").jqxToolBar("getTools")[2].tool.children("div").jqxButton("disabled", true);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_save_as", true);
        } else {
          $("div#top_menu_icon_component").jqxToolBar("getTools")[2].tool.children("div").jqxButton("disabled", false);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_save_as", false);
        }
        if (getTabContent().find("input.is_changed").val() == "false" && 
           getTabContent().find("input.query_num").val() != "0") {
          $("div#top_menu_icon_component").jqxToolBar("getTools")[3].tool.children("div").jqxButton("disabled", false);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_deploy", false);
        } else {
          $("div#top_menu_icon_component").jqxToolBar("getTools")[3].tool.children("div").jqxButton("disabled", true);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_deploy", true);
        }
        if (getTabContent().find("input.is_exist_no_dist").val() == "Y") {
          $("div#top_menu_icon_component").jqxToolBar("getTools")[4].tool.children("div").jqxButton("disabled", false);          
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_delete", false);
        } else {
          $("div#top_menu_icon_component").jqxToolBar("getTools")[4].tool.children("div").jqxButton("disabled", true);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_delete", true);
        }
        if (getTabContent().find("input.query_num").val() != "0") {
          g_rep_ver_drop_down_system_change = true;
          setTimeout(function() {g_rep_ver_drop_down_system_change = false;}, 500);
          $("div#top_menu_icon_component").jqxToolBar("getTools")[6].tool.jqxDropDownList("disabled", false);   
        } else {
          g_rep_ver_drop_down_system_change = true;
          setTimeout(function() {g_rep_ver_drop_down_system_change = false;}, 500);
          $("div#top_menu_icon_component").jqxToolBar("getTools")[6].tool.jqxDropDownList("disabled", true);
        }
      }
      
      function getAllValuList(query) {
        var returnArr = new Array();
        var i = 0;
        var index = 0;
        while ((index = query.indexOf("#", i)) != -1 && query.charAt(index + 1) == "{") {
          var indexLast = query.indexOf("}", index);
          for (var j = 0; j < returnArr.length; j++) {
            if (returnArr[j] == query.substring(index + 2, indexLast)) {
              break;
            }
          }
          if (j == returnArr.length) {
            returnArr.push(query.substring(index + 2, indexLast));
          }
          i = indexLast;
        }
        return returnArr;
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
        DBIO 저장
      </div>
      <div style="overflow: hidden;" id="windowContent">
        <div class="save_config_dbio_name_txt">
          <input id="save_config_dbio_name_txt_component"/>
        </div>
        <div class="save_config_dbio_ok_but">
          <input id="save_config_dbio_ok_but_component" value="Save"/>
        </div>
        <div class="save_config_dbio_cancel_but">
          <input id="save_config_dbio_cancel_but_component" value="Cancel"/>
        </div>
      </div>
    </div>
    <div id="test_window" class="test_window" style="display:none;">
      <div class="test_window_header">
        SQL Test
      </div>
      <div style="overflow:hidden;" id="testWindowContent">
        <div class="test_input">
          <div id="test_input_component">
          </div>
        </div>
        <div class="test_output">
          <div id="test_output_component">
          </div>
        </div>
        <div class="test_ok_but">
          <input id="test_ok_but_component" value="Test"/>
        </div>
        <div class="test_cancel_but">
          <input id="test_cancel_but_component" value="Cancel"/>
        </div>
      </div>
    </div>
  </body>
</html>