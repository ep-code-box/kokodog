<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>KOKODOG[Selenium 테스트 시나리오/케이스/소스 관리 시스템]</title>
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

      function callback(data, act, input_param, callbackVar) {
        if (act == "GetDBIOList") {
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