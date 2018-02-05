<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>KOKODOG[Selenium 테스트 시나리오/케이스/소스 관리 시스템]</title>
    <link rel="shortcut icon" type="image/x-icon" href="/FileDown?file_key=tId6m0peoFkF2GYcAfAJG48kOE5Djo5ky49MFtnX" />
    <link rel="stylesheet" href="/css/skd/sel/sel_scnrio_mng.css"/>
    <link rel="stylesheet" href="/js/jqwidgets/styles/jqx.base.css" type="text/css"/>
    <link href="https://fonts.googleapis.com/css?family=Oxygen+Mono" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="/js/jquery/jquery-3.2.1.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqx-all.js"></script>
    <link rel="stylesheet" href="http://codemirror.net/lib/codemirror.css"/>
    <link rel="stylesheet" href="http://codemirror.net/addon/hint/show-hint.css"/>
    <link rel="stylesheet" href="http://codemirror.net/addon/fold/foldgutter.css"/>
    <link rel="stylesheet" href="https://codemirror.net/theme/eclipse.css"/>
    <script src="http://codemirror.net/lib/codemirror.js"></script>
    <script src="http://codemirror.net/mode/python/python.js"></script>
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
      var gCmEditorTheme = "eclipse";

      /* Document 로드 시에 발생하는 시작 함수 */
      $(document).ready(function() {
        contentInitLoad();
        contentEventLoad();
        setCodeMirrorEditor();
        getSncrioLst();
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
        $("div#data_tree_component").jqxTree({
          width: "100%",
          height: "100%",
          allowDrag: false,
          allowDrop: false,
          checkboxes: false,
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
      }

      /* jqWidget을 사용하는 각종 이벤트들 맵핑 처리 */
      function contentEventLoad() {
        $("div#data_tree_component").on("itemClick", event_div_data_tree_component_click);
      }
      
      function event_div_data_tree_component_click(event) {
        cmnSyncCall("GetSrcCdByScnrioNum", {scnrio_num: $("div#data_tree_component").jqxTree("getItem", event.args.element).value}, callback, null);
      }
      
      function setCodeMirrorEditor() {
       var codeMirrorEditor = CodeMirror.fromTextArea($("#text_src_cd")[0], {
          mode: "python",
          lineNumbers: true,
          lineWrapping: true,
          readOnly: false,
          foldGutter: {
            rangeFinder: new CodeMirror.fold.combine(CodeMirror.fold.brace, CodeMirror.fold.comment)
          },
          gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"]
        });
        codeMirrorEditor.setOption("theme", gCmEditorTheme);
        codeMirrorEditor.getWrapperElement().style["font-family"] = "Oxygen Mono";
        codeMirrorEditor.refresh();
        codeMirrorEditor.setSize("100%", "100%");
        codeMirrorEditor.getDoc().setValue("");
        codeMirrorEditor.getDoc().clearHistory();
        codeMirrorEditor.on("change", function(cm, changeObj) {
        });
      }
      
      /* source_data_tab_component(메뉴 상단 아이콘 버튼) 신규 맵핑 처리 */
      function init_top_menu_icon_component_init_tools(type, index, tool, menuToolIninitialization) {
        switch (index) {
          case 0:
            var newButton = $("<div>" + "<img src='/FileDown?file_key=0xAoKZ11FQB3HyZH0bIpCaaphbKcyS3uXYn0G5rn' title='New...' style='width:20px;height:20px;'/>" + "</div>");
            tool.append(newButton);
            newButton.jqxButton({
              width: "20px",
              height: "20px"
            });
            break;
          case 1:
            var saveButton = $("<div>" + "<img src='/FileDown?file_key=GgnS17SPV1IuXIfUZGWXfykXCbOMyeB2S2AGKtWD' title='Save...' style='width:20px;height:20px;'/>" + "</div>");
            tool.append(saveButton);
            saveButton.jqxButton({
              width: "20px",
              height: "20px"
            });
            break;
          case 2:
            var saveAsButton = $("<div>" + "<img src='/FileDown?file_key=6Tyvf2KhwkyKKug6IZZmJOvzLcZT4mYWoBK3D5Ke' title='Save As...' style='width:20px;height:20px;'/>" + "</div>");
            tool.append(saveAsButton);
            saveAsButton.jqxButton({
              width: "20px",
              height: "20px"
            });
            break;
          case 3:
            var distrubteButton = $("<div>" + "<img src='/FileDown?file_key=zc15PA0zUXEPfRQVPYNOTbcbdUEoJScZZLO8TBYG' title='Deploy' style='width:20px;height:20px;'/>" + "</div>");
            tool.append(distrubteButton);
            distrubteButton.jqxButton({
              width: "20px",
              height: "20px"
            });
            break;
          case 4:
            var deleteButton = $("<div>" + "<img src='/FileDown?file_key=YwN3sUbnW2f7T2YrLy5lbLUztLD9EWDIyP3v6g4A' title='Ver. Delete' style='width:20px;height:20px;'/>" + "</div>");
            tool.append(deleteButton);
            deleteButton.jqxButton({
              width: "20px",
              height: "20px"
            });
            break;
          case 5:
            var testButton = $("<div>" + "<img src='/FileDown?file_key=YwN3sUbnW2f7T2YrLy5lbLUztLD9EWDIyP3v6g4A' title='Test' style='width:20px;height:20px;'/>" + "</div>");
            tool.append(testButton);
            testButton.jqxButton({
              width: "20px",
              height: "20px"
            });
            break;
          case 6:
            tool.jqxDropDownList({width: "150px", height: "28px", dropDownHeight: "200px"});
            break;
          default:
            break;
        }
      }

      function callback(data, act, input_param, callbackVar) {
        if (act == "GetScnrioLst") {
          var i = 0;
          for (i = 0; i < data.length; i++) {
            $("div#data_tree_component").jqxTree("addTo", {label: data[i].scnrio_nm, value: data[i].scnrio_num});
            $("div#data_tree_component").jqxTree("addTo", {label: "temp", value: 0}, $("div#data_tree_component").jqxTree("getItems")[$("div#data_tree_component").jqxTree("getItems").length - 1]);
          }
        }
      }
      
      function getSncrioLst() {
        $("div#data_tree_component").jqxTree("clear");
        getScnrioLstNext(1);
      }
      
      function getScnrioLstNext(page_num) {
        cmnSyncCall("GetScnrioLst", {sch_txt: $("input#search_text_component").val(), page_num: page_num}, callback, null);
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
                <li type="separator"></li>
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
            <div class="data_tree">
              <div id="data_tree_component"></div>
            </div>
          </div>
          <div class="right_splitter">
            <textarea class="text_src_cd" id="text_src_cd"></textarea>
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
  </body>
</html>