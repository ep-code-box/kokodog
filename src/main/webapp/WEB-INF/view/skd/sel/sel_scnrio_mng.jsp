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
      var codeMirrorEditor;
      var editedCaseInput = new Array();
      var editedExptRslt = new Array();

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
        $("div#case_input_component").jqxGrid({
          editable: true,
          enabletooltips: true,
          columns: [{text: "순번", datafield: "seq", width: 40, editable: false, columntype: "dropdownlist", cellsalign: "right", cellclassname: cellInputClass},
                    {text: "입력명", datafield: "input_nm", width: "30%", editable: false, columntype: "textbox", cellclassname: cellInputClass},
                    {text: "입력값", datafield: "input_val", editable: true, columntype: "textbox", cellclassname: cellInputClass}
                   ],
          width: "100%",
          height: "100%",
          showtoolbar: true,
          rendertoolbar: function (statusbar) {
            var container = $("<div style='overflow: hidden; position: relative; margin: 5px;'></div>");
            var saveButton = $("<div style='float: left; margin-left: 5px;'>Save</div>");
            container.append(saveButton);
            statusbar.append(container);
            saveButton.jqxButton({width: 60, height: 20});
            saveButton.click(event_input_data_save_but_click);
          }
        });
        $("div#test_scnrio_right_click_pop").jqxMenu({
          width: "150px",
          autoOpenPopup: false,
          mode: "popup"
        });
        $("div#test_case_right_click_pop").jqxMenu({
          width: "120px",
          autoOpenPopup: false,
          mode: "popup"
        });
        cmnSyncCall("/cmn/cmn/main/GetCommonCode", {code: 34}, callback, null);
        $("div#new_rgst_window").jqxWindow({
          position: "center",
          showCloseButton: true,
          resizable: false,
          isModal: true,
          modalOpacity: 0.3,
          draggable: true,
          autoOpen: false,
          width: "520px",
          height: "220px"
        });
        $("input#new_rgst_window_nm_txt_component").jqxInput({
          height: "100%",
          width: "100%"
        });
        $("#new_rgst_window_desc_txt_component").jqxTextArea({
          height: "100%",
          width: "100%"
        })
        $("input#new_rgst_window_ok_but_component").jqxButton({
          height: "100%",
          width: "100%"          
        });
        $("input#new_rgst_window_cancel_but_component").jqxButton({
          height: "100%",
          width: "100%"          
        });
      }

      /* jqWidget을 사용하는 각종 이벤트들 맵핑 처리 */
      function contentEventLoad() {
        $("div#data_tree_component").on("itemClick", event_div_data_tree_component_item_click);
        $("div#data_tree_component").on("expand", event_div_data_tree_component_expand);
        $("div#case_input_component").on("cellendedit", event_div_case_input_component_cellendedit);
        $("div#rslt_expt_component").on("cellendedit", event_div_rslt_expt_component_cellendedit);
        $(document).bind("contextmenu", function (event) {
          if ($(event.target).parents(".jqx-tree").length > 0 || $(event.target).attr("id") == "_cmn_loaderModal") {
            return false;
          }
          return true;
        });
        $("div#data_tree_component ul").on("mousedown", function (event) {
          var target = $(event.target).parents("li:first")[0];
          var rightClick = isRightClick(event);
          if (rightClick && target != null) {
            $("div#test_scnrio_right_click_pop").jqxMenu("close");
            $("div#test_case_right_click_pop").jqxMenu("close");
            $("div#data_tree_component").jqxTree("selectItem", target);
            var scrollTop = $(window).scrollTop();
            var scrollLeft = $(window).scrollLeft();
            if ($("div#data_tree_component").jqxTree("getSelectedItem").parentElement == null) {
              $("div#test_scnrio_right_click_pop").jqxMenu("open", parseInt(event.clientX) + 5 + scrollLeft, parseInt(event.clientY) + 5 + scrollTop);
            } else {
              $("div#test_case_right_click_pop").jqxMenu("open", parseInt(event.clientX) + 5 + scrollLeft, parseInt(event.clientY) + 5 + scrollTop);              
            }
            loadTestScnrioCaseInfo(target);
            return false;
          }
        });
        $("div#test_scnrio_right_click_pop").on("itemclick", event_div_test_scnrio_right_click_pop_click);
        $("input#new_rgst_window_ok_but_component").on("click", event_input_new_rgst_window_ok_but_component_click);
        $("input#new_rgst_window_cancel_but_component").on("click", event_input_new_rgst_window_cancel_but_component_click);
        $("a#menu_scnrio_new").click(event_a_menu_scnrio_new_click);
        $("a#menu_import").click(event_a_menu_import_click);
      }
      
      function event_div_data_tree_component_item_click(event) {
        loadTestScnrioCaseInfo(event.args.element);
      }
      
      function loadTestScnrioCaseInfo(target) {
        if ($("div#data_tree_component").jqxTree("getItem", target).parentElement == null) {
          $("div#div_case_input_rslt").css("display", "none");
          $("div#div_src_cd").css("display", "block");
          cmnSyncCall("GetSrcCdByScnrioNum", {scnrio_num: $("div#data_tree_component").jqxTree("getItem", target).value}, callback, null);
        } else {
          $("div#div_src_cd").css("display", "none");
          $("div#div_case_input_rslt").css("display", "block");
          cmnSyncCall("GetTestInputAndExptRsltByCaseNum", {
            scnrio_num: $("div#data_tree_component").jqxTree("getItem", $("div#data_tree_component").jqxTree("getItem", target).parentElement).value
            , case_num: $("div#data_tree_component").jqxTree("getItem", target).value
          }, callback, null);
        }
      }
      
      function event_div_data_tree_component_expand(event) {
        if ($("div#data_tree_component").jqxTree("getItem", event.args.element).parentElement == null) {
          for (var i = 0; i < $("div#data_tree_component").jqxTree("getItems").length; i++) {
            if ($("div#data_tree_component").jqxTree("getItems")[i].parentElement == event.args.element) {
              $("div#data_tree_component").jqxTree("removeItem", $("div#data_tree_component").jqxTree("getItems")[i]);
            }
          }
          cmnASyncCall("GetTestCaseInfoByScnrioNum", {scnrio_num: $("div#data_tree_component").jqxTree("getItem", event.args.element).value}, callback, null);
        }
      }
      
      function event_div_case_input_component_cellendedit(event) {
        if (event.args.oldvalue != event.args.value) {
          editedCaseInput.push({rowindex: event.args.rowindex});
          $("div#case_input_component").jqxGrid("updaterow", event.args.rowindex);
        }
      }
      
      function event_div_rslt_expt_component_cellendedit(event) {
        if (event.args.oldvalue != event.args.value) {
          editedExptRslt.push({rowindex: event.args.rowindex});
          $("div#rslt_expt_component").jqxGrid("updaterow", event.args.rowindex);
        }        
      }
      
      function event_div_test_scnrio_right_click_pop_click(event) {
        if ($(event.target).text() == "시나리오 추가") {
          scnrio_add_window_pop();
        } else if ($(event.target).text() == "삭제") {
          scnrio_del_window_pop();
        } else if ($(event.target).text() == "수정") {
          scnrio_update_window_pop();
        } else if ($(event.target).text() == "시나리오파일 업로드") {
          scnrio_file_upload();
        } else if ($(event.target).text() == "시나리오 테스트") {
          scnrio_test();
        } else if ($(event.target).text() == "시나리오 정보") {
          scnrio_inform();
        } else if ($(event.target).text() == "케이스 추가") {
          case_add_window_pop();
        }
      }
      
      function event_input_new_rgst_window_ok_but_component_click() {
        var validCheckMsg = event_input_new_rgst_window_ok_but_component_click_validation();
        if (validCheckMsg != null) {
          cmnAlert(validCheckMsg);
        } else {
          cmnSyncCall("InsertNewScnrio", {scnrio_nm: $.trim($("input#new_rgst_window_nm_txt_component").val()), scnrio_desc: $("#new_rgst_window_desc_txt_component").val()}, callback, null);
        }
      }
      
      function event_input_new_rgst_window_cancel_but_component_click() {
        $("div#new_rgst_window").jqxWindow("close");
      }

      function event_input_data_save_but_click(event) {
        cmnAlert("구현중");
      }
      
      function event_expt_rslt_add_but_click(event) {
        cmnAlert("구현중");
      }
      
      function event_expt_rslt_del_but_click(event) {
        cmnAlert("구현중");
      }

      function event_expt_rslt_save_but_click(event) {
        cmnAlert("구현중");
      }
      
      function event_a_menu_scnrio_new_click(event) {
        scnrio_add_window_pop();
      }
      
      function event_a_menu_import_click(event) {
        scnrio_file_upload();
      }

      function setCodeMirrorEditor() {
       codeMirrorEditor = CodeMirror.fromTextArea($("#text_src_cd")[0], {
          mode: "python",
          lineNumbers: true,
          lineWrapping: false,
          readOnly: "nocursor",
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
            $("div#data_tree_component").jqxTree("addTo", {label: "", value: 0}, $("div#data_tree_component").jqxTree("getItems")[$("div#data_tree_component").jqxTree("getItems").length - 1]);
          }
        } else if (act == "GetSrcCdByScnrioNum") {
          codeMirrorEditor.getDoc().setValue(data.src_cd);
          codeMirrorEditor.setOption("readOnly", false);
        } else if (act == "GetTestCaseInfoByScnrioNum") {
          var i = 0;
          for (i = 0; i < $("div#data_tree_component").jqxTree("getItems").length; i++) {
            if ($("div#data_tree_component").jqxTree("getItems")[i].parentElement == null && $("div#data_tree_component").jqxTree("getItems")[i].value == input_param.scnrio_num) {
              break;
            }
          }
          for (var j = 0; j < data.length; j++) {
            $("div#data_tree_component").jqxTree("addTo", {label: data[j].case_nm, value: data[j].case_num}, $("div#data_tree_component").jqxTree("getItems")[i]);
          }
        } else if (act == "GetTestInputAndExptRsltByCaseNum") {
          $("div#case_input_component").jqxGrid("clear");
          for (var i = 0; i < data.input.length; i++) {
            $("div#case_input_component").jqxGrid("addrow", null, {seq: (i + 1), input_nm: data.input[i].input_nm, input_val: data.input[i].input_val});
          }
          $("div#rslt_expt_component").jqxGrid("clear");
          for (var i = 0; i < data.expt_rslt.length; i++) {
            $("div#rslt_expt_component").jqxGrid("addrow", null, {test_step_num: data.expt_rslt[i].test_step_num
                                                                   , judg_typ_nm: data.expt_rslt[i].judg_typ_nm
                                                                   , rslt_strd: data.expt_rslt[i].rslt_strd
                                                                  });
          }
        } else if (act == "/cmn/cmn/main/GetCommonCode") {
          var judgTypNms = [];
          for (var property in data) {
            judgTypNms.push({value: property, label: data[property]});
          }
          var judgTypNmsSource = {
            datatype: "array",
            datafields: [
              {name: "label", type: "string"},
              {name: "value", type: "int"}
            ],
            localdata: judgTypNms
          };
          var judgTypNmsAdapter = new $.jqx.dataAdapter(judgTypNmsSource, {autoBind: true});
          $("div#rslt_expt_component").jqxGrid({
            editable: true,
            enabletooltips: true,
            columns: [{text: "테스트스텝번호", datafield: "test_step_num", width: 100, editable: true, columntype: "textbox", cellsalign: "right", cellclassname: cellExptRsltClass},
                      {text: "판정여부구분", datafield: "judg_typ_nm", width: "30%", editable: true, columntype: "dropdownlist"
                      , createeditor: function (row, value, editor) {
                        editor.jqxDropDownList({ source: judgTypNmsAdapter, displayMember: "label", valueMember: "value"});
                      }, cellclassname: cellExptRsltClass},
                      {text: "기준문구", datafield: "rslt_strd", editable: true, columntype: "textbox", cellclassname: cellExptRsltClass}
                     ],
            width: "100%",
            height: "100%",
            showtoolbar: true,
            rendertoolbar: function (statusbar) {
              var container = $("<div style='overflow: hidden; position: relative; margin: 5px;'></div>");
              var addBut = $("<div style='float: left; margin-left: 5px;'>Add</div>");
              var delBut = $("<div style='float: left; margin-left: 5px;'>Delete</div>");
              var saveBut = $("<div style='float: left; margin-left: 5px;'>Save</div>");
              container.append(addBut);
              container.append(delBut);
              container.append(saveBut);
              statusbar.append(container);
              addBut.jqxButton({width: 60, height: 20});
              delBut.jqxButton({width: 65, height: 20});
              saveBut.jqxButton({width: 65, height: 20});
              addBut.click(event_expt_rslt_add_but_click);
              delBut.click(event_expt_rslt_del_but_click);
              saveBut.click(event_expt_rslt_save_but_click);
            }
          });
        } else if (act == "InsertNewScnrio") {
          $("div#data_tree_component").jqxTree("addTo", {label: $("input#new_rgst_window_nm_txt_component").val(), value: data.scnrio_num});
          $("div#data_tree_component").jqxTree("addTo", {label: "샘플 케이스", value: 1}, $("div#data_tree_component").jqxTree("getItems")[$("div#data_tree_component").jqxTree("getItems").length - 1]);
          $("div#new_rgst_window").jqxWindow("close");
        }
      }
      
      function getSncrioLst() {
        $("div#data_tree_component").jqxTree("clear");
        getScnrioLstNext(1);
      }
      
      function getScnrioLstNext(page_num) {
        cmnSyncCall("GetScnrioLst", {sch_txt: $("input#search_text_component").val(), page_num: page_num}, callback, null);
      }
      
      function isRightClick(event) {
        var rightclick;
        if (event != true) {
          var event = window.event;
        }
        if (event.which == true) {
          rightclick = (event.which == 3);
        } else if (event.button) {
          rightclick = (event.button == 2);
        }
        return rightclick;
      }
      
      function loadTestScnrioCaseInfo(target) {
        editedCaseInput = [];
        editedExptRslt = [];
        if (typeof target == "undefined") {
          target = $("div#data_tree_component").jqxTree("getSelectedItem").element;
        }
        if ($("div#data_tree_component").jqxTree("getItem", target).parentElement == null) {
          $("div#div_case_input_rslt").css("display", "none");
          $("div#div_src_cd").css("display", "block");
          cmnSyncCall("GetSrcCdByScnrioNum", {scnrio_num: $("div#data_tree_component").jqxTree("getItem", target).value}, callback, null);
        } else {
          $("div#div_src_cd").css("display", "none");
          $("div#div_case_input_rslt").css("display", "block");
          cmnSyncCall("GetTestInputAndExptRsltByCaseNum", {
            scnrio_num: $("div#data_tree_component").jqxTree("getItem", $("div#data_tree_component").jqxTree("getItem", target).parentElement).value
            , case_num: $("div#data_tree_component").jqxTree("getItem", target).value
          }, callback, null);
        }
      }
      
      function scnrio_add_window_pop() {
        $("div#new_rgst_window_header").html("시나리오 신규 등록");
        $("div#new_rgst_window_nm_label").html("시나리오명 :");
        $("div#new_rgst_window_desc_label").html("시나리오 설명 :");
        $("input#new_rgst_window_ok_but_component").val("등록");
        $("input#new_rgst_window_nm_txt_component").jqxInput({placeHolder: "시나리오명..."});
        $("#new_rgst_window_desc_txt_component").jqxTextArea({placeHolder: "시나리오설명..."});
        $("div#new_rgst_window").jqxWindow("open");
      }
      
      function event_input_new_rgst_window_ok_but_component_click_validation() {
        if ($.trim($("input#new_rgst_window_nm_txt_component").val()) == "") {
          return "시나리오 명을 넣어주세요";
        } else {
          var RegExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\\(\=]/gi;
          if($("input#new_rgst_window_nm_txt_component").val().match(RegExp) != null) {
            return "정상적인 시나리오 명을 넣어주세요[특수문자 금지]";
          }
        }
        return null;
      }
      
      function scnrio_update_window_pop() {
        cmnAlert("구현중");
      }
      
      function scnrio_del_window_pop() {
        cmnConfirm(callbackConfirm, "웹페이지 메시지", "정말로 삭제하시겠습니까?", 1);
      }
      
      function callbackConfirm(ret, callbackVar) {
        if (callbackVar == 1) {
          if (ret == true) {
            cmnAlert("구현중");
          } else {
            cmnAlert("삭제가 취소되었습니다.");
          }
        } else if (callbackVar == 2) {
          if (ret == true) {
          cmnSyncCall("GetTestList", {scnrio_num: $("div#data_tree_component").jqxTree("getSelectedItem").value}, callback, null);
          } else {
            cmnAlert("테스트가 취소되었습니다.");
          }
        }
      }
      
      function scnrio_test() {
        cmnConfirm(callbackConfirm, "췝페이지 메시지", "테스트를 수행하시겠습니까?", 2);
      }
      
      function scnrio_file_upload() {
        var form = $("<form>");
        form.append($("<input>").attr({type: "file", id: "scnrio_file_upload"}).css("display", "none"));
        $("input#scnrio_file_upload").on("change", function(event) {
          cmnAlert("구현중");
        });
        $("input#scnrio_file_upload").click();
      }
      
      function file_upload_callback() {
        cmnAlert("구현중");
      }
      
      function scnrio_inform() {
        cmnAlert("구현중");
      }
      
      function case_add_window_pop() {
        $("div#new_rgst_window_header").html("케이스 신규 등록");
        $("div#new_rgst_window_nm_label").html("케이스명 :");
        $("div#new_rgst_window_desc_label").html("케이스 설명 :");
        $("input#new_rgst_window_ok_but_component").val("등록");
        $("input#new_rgst_window_nm_txt_component").jqxInput({placeHolder: "케이스명..."});
        $("#new_rgst_window_desc_txt_component").jqxTextArea({placeHolder: "케이스설명..."});
        $("div#new_rgst_window").jqxWindow("open");        
      }
      
      var cellInputClass = function(row, datafield, value, rowdata) {
        for (var i = 0; i < editedCaseInput.length; i++) {
          if (editedCaseInput[i].rowindex == row) {
            return "cell_edited";
          }
        }
        return "cell_not_edited";
      }

      var cellExptRsltClass = function(row, datafield, value, rowdata) {
        for (var i = 0; i < editedExptRslt.length; i++) {
          if (editedExptRslt[i].rowindex == row) {
            return "cell_edited";
          }
        }
        return "cell_not_edited";
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
                <li id="li_menu_scnrio_new">
                  <a id="menu_scnrio_new">시나리오추가</a>
                </li>
                <li id="li_menu_case_new">
                  <a id="menu_case_new">케이스추가</a>
                </li>
                <li id="li_menu_update">
                  <a id="menu_update">수정</a>
                </li>
                <li id="li_menu_delete">
                  <a id="menu_delete">삭제</a>
                </li>
                <li type="separator"></li>
                <li id="li_menu_scnrio_test">
                  <a id="menu_scnrio_test">시나리오테스트</a>
                </li>
                <li id="li_menu_case_test">
                  <a id="menu_case_test">케이스테스트</a>
                </li>
                <li type="separator"></li>
                <li id="li_menu_import">
                  <a id="menu_import">시나리오파일 업로드</a>
                </li>
                <li type="separator"></li>
                <li id="li_menu_inform">
                  <a id="menu_inform">정보</a>
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
            <div class="div_src_cd" id="div_src_cd" style="display:none;">
              <textarea class="text_src_cd" id="text_src_cd"></textarea>            
            </div>
            <div class="div_case_input_rslt" id="div_case_input_rslt" style="display:none;">
              <div class="case_input">
                <div id="case_input_component">
                </div>
              </div>
              <div class="rslt_expt">
                <div id="rslt_expt_component">
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div id="new_rgst_window" class="new_rgst_window">
      <div class="new_rgst_window_header" id="new_rgst_window_header">
      </div>
      <div style="overflow: hidden;" class="window_rgst_content">
        <div class="new_rgst_window_nm_label" id="new_rgst_window_nm_label">
        </div>
        <div class="new_rgst_window_nm_txt">
          <input id="new_rgst_window_nm_txt_component"/>
        </div>
        <div class="new_rgst_window_desc_label" id="new_rgst_window_desc_label">
        </div>
        <div class="new_rgst_window_desc_txt">
          <textarea id="new_rgst_window_desc_txt_component"/></textarea>
        </div>
        <div class="new_rgst_window_ok_but">
          <input id="new_rgst_window_ok_but_component" type="button" value=""/>
        </div>
        <div class="new_rgst_window_cancel_but">
          <input id="new_rgst_window_cancel_but_component" type="button" value="취소"/>
        </div>
      </div>
    </div>
    <div id="test_scnrio_right_click_pop">
      <ul>
        <li>시나리오 추가</li>
        <li>삭제</li>
        <li>수정</li>
        <li type="separator"></li>
        <li>시나리오파일 업로드</li>
        <li>시나리오 테스트</li>
        <li type="separator"></li>
        <li>시나리오 정보</li>
        <li type="separator"></li>
        <li>케이스 추가</li>
      </ul>
    </div>
    <div id="test_case_right_click_pop">
      <ul>
        <li>추가</li>
        <li>삭제</li>
        <li>수정</li>
        <li type="separator"></li>
        <li>케이스 테스트</li>
        <li type="separator"></li>
        <li>케이스 정보</li>
      </ul>
    </div>
    <input type="file" id="scnrio_file_upload" style="display:none;"/>
  </body>
</html>