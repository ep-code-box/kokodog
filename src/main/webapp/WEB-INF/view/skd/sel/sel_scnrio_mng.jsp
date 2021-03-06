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
      var judgTypNm = [];
      var inetrvalFunc;
      var getTestRsltFailNo = 0;

      $(document).ready(function() {
        contentInitLoad();
        contentEventLoad();
        setCodeMirrorEditor();
        getSncrioLst();
        setButEnableChk();
      });
      
      function contentInitLoad() {
        $("div#top_menu_bar_component").jqxMenu({
          width: "100%",
          height: "100%"
        });
        $("div#top_menu_icon_component").jqxToolBar({
          tools: "custom custom custom custom | custom custom | custom",
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
            var saveButton = $("<div style='float: left; margin-left: 5px;'><img src='/FileDown?file_key=GgnS17SPV1IuXIfUZGWXfykXCbOMyeB2S2AGKtWD' style='width:18px;height:18px;'/></div>").attr("id", "case_input_component_save_button");
            container.append(saveButton);
            statusbar.append(container);
            saveButton.jqxButton({width: 18, height: 18, disabled: true});
            saveButton.click(event_input_data_save_but_click);
            saveButton.jqxTooltip({position: "mouse", content: "저장"});
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
        $("div#test_blank_right_click_pop").jqxMenu({
          width: "150px",
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
        $("div#agent_down_pop_window").jqxWindow({
          position: "center",
          showCloseButton: true,
          resizable: false,
          isModal: true,
          modalOpacity: 0.3,
          draggable: true,
          autoOpen: false,
          width: "500px",
          height: "200px"          
        });
        $("input#agent_down_pop_window_down_but_component").jqxButton({
          height: "100%",
          width: "100%"
        });
        $("div#test_step_pop_window").jqxWindow({
          position: "center",
          showCloseButton: true,
          resizable: false,
          isModal: true,
          modalOpacity: 0.3,
          draggable: true,
          autoOpen: false,
          width: "1200px",
          height: "800px",
          maxWidth: "1200px",
          maxHeight: "800px"
        });
        $("div#test_step_pop_window_grid_component").jqxGrid({
          width: "100%",
          height: "100%",
          editable: false,
          columns: [{text: "시나리오명", datafield: "scnrio_nm", width: 160, editable: false, columntype: "textbox", cellsalign: "center"},
                    {text: "케이스명", datafield: "case_nm", width: 200, editable: false, columntype: "textbox", cellsalign: "center"},
                    {text: "테스트번호", datafield: "test_step_num", width: 80, editable: false, columntype: "textbox", cellsalign: "right", align: "center"},
                    {text: "로그", datafield: "log", editable: false, columntype: "textbox", cellsalign: "center"},
                    {text: "진행상태", datafield: "test_st_nm", width: 80, editable: false, columntype: "textbox", cellsalign: "center"},
                    {text: "시나리오번호", datafield: "scnrio_num", hidden: true},
                    {text: "케이스번호", datafield: "case_num", hidden: true},
                    {text: "시각", datafield: "dtm", hidden: true},
                    {text: "테스트상태코드", datafield: "test_st_cd", hidden: true},
                   ],
          width: "100%",
          height: "100%",
          autoheight: true,
          autorowheight: true,
          columnsheight: 20,
          selectionmode: "none"
        });
        $("#toolbar_scnrio_new").jqxTooltip({position: "mouse", content: "새 시나리오..."});
        $("#toolbar_case_new").jqxTooltip({position: "mouse", content: "새 테스트케이스..."});
        $("#toolbar_update").jqxTooltip({position: "mouse", content: "수정"});
        $("#toolbar_del").jqxTooltip({position: "mouse", content: "삭제"});
        $("#toolbar_scnrio_test").jqxTooltip({position: "mouse", content: "시나리오 테스트"});
        $("#toolbar_case_test").jqxTooltip({position: "mouse", content: "케이스 테스트"});
        $("#toolbar_upload").jqxTooltip({position: "mouse", content: "시나리오 업로드"});
      }

      function contentEventLoad() {
        $("div#data_tree_component").on("expand", event_div_data_tree_component_expand);
        $("div#data_tree_component").on("select", event_div_data_tree_component_select);
        $("div#case_input_component").on("cellendedit", event_div_case_input_component_cellendedit);
        $("div#rslt_expt_component").on("cellendedit", event_div_rslt_expt_component_cellendedit);
        $("div#rslt_expt_component").on("rowselect", event_div_rslt_expt_component_rowselect);
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
            $("div#test_blank_right_click_pop").jqxMenu("close");            
            $("div#data_tree_component").jqxTree("selectItem", target);
            var scrollTop = $(window).scrollTop();
            var scrollLeft = $(window).scrollLeft();
            if ($("div#data_tree_component").jqxTree("getSelectedItem").parentElement == null) {
              $("div#test_scnrio_right_click_pop").jqxMenu("open", parseInt(event.clientX) + 5 + scrollLeft, parseInt(event.clientY) + 5 + scrollTop);
            } else {
              $("div#test_case_right_click_pop").jqxMenu("open", parseInt(event.clientX) + 5 + scrollLeft, parseInt(event.clientY) + 5 + scrollTop);              
            }
            return false;
          } else if (rightClick && target == null) {
            $("div#test_scnrio_right_click_pop").jqxMenu("close");
            $("div#test_case_right_click_pop").jqxMenu("close");
            $("div#test_blank_right_click_pop").jqxMenu("close");            
            $("div#test_blank_right_click_pop").jqxMenu("open", parseInt(event.clientX) + 5 + scrollLeft, parseInt(event.clientY) + 5 + scrollTop);
          }
        });
        $("div#test_scnrio_right_click_pop").on("itemclick", event_div_test_scnrio_right_click_pop_click);
        $("div#test_case_right_click_pop").on("itemclick", event_div_test_case_right_click_pop_click);
        $("div#test_blank_right_click_pop").on("itemclick", event_div_test_blank_right_click_pop_click);
        $("input#new_rgst_window_ok_but_component").on("click", event_input_new_rgst_window_ok_but_component_click);
        $("input#new_rgst_window_cancel_but_component").on("click", event_input_new_rgst_window_cancel_but_component_click);
        $("a#menu_scnrio_new").click(event_a_menu_scnrio_new_click);
        $("a#menu_import").click(event_a_menu_import_click);
        $("input#agent_down_pop_window_down_but_component").click(event_input_agent_down_pop_window_down_but_component_click);
        $("div#test_step_pop_window").on("close", event_div_test_step_pop_window_close);
      }
      
      function event_div_data_tree_component_expand(event) {
        if ($("div#data_tree_component").jqxTree("getItem", event.args.element).parentElement == null) {
          for (var i = 0; i < $("div#data_tree_component").jqxTree("getItems").length; i++) {
            if ($("div#data_tree_component").jqxTree("getItems")[i].parentElement == event.args.element) {
              $("div#data_tree_component").jqxTree("removeItem", $("div#data_tree_component").jqxTree("getItems")[i]);
              i--;
            }
          }
          cmnASyncCall("GetTestCaseInfoByScnrioNum", {scnrio_num: $("div#data_tree_component").jqxTree("getItem", event.args.element).value}, callback, null);
        }
      }
      
      function event_div_data_tree_component_select(event) {
        loadTestScnrioCaseInfo(event.args.element);
        setButEnableChk();
      }
      
      function event_div_case_input_component_cellendedit(event) {
        if (event.args.oldvalue != event.args.value) {
          editedCaseInput.push({rowindex: event.args.rowindex});
          $("div#case_input_component").jqxGrid("updaterow", event.args.rowindex);
          $("#case_input_component_save_button").jqxButton("disabled", false);
        }
      }
      
      function event_div_rslt_expt_component_cellendedit(event) {
        if (event.args.oldvalue != event.args.value) {
          var i = 0;
          for (i = 0; i < editedExptRslt.length; i++) {
            if (editedExptRslt[i].rowindex == event.args.rowindex) {
              if (event.args.datafield == "test_step_num" && typeof editedExptRslt[i].beforeRowNum == "undefined") {
                editedExptRslt[i].beforeRowNum = event.args.oldvalue;
              } else if (event.args.datafield == "test_step_num" && editedExptRslt[i].beforeRowNum == event.args.value) {
                delete editedExptRslt[i].beforeRowNum;
              }
              break;
            }
          }
          if (i == editedExptRslt.length) {
            var pushVal;
            if (event.args.datafield == "test_step_num") {
              pushVal = {rowindex: event.args.rowindex, rowtype: "edited", beforeRowNum: event.args.oldvalue};
            } else {
              pushVal = {rowindex: event.args.rowindex, rowtype: "edited"};
            }
            editedExptRslt.push(pushVal);
          }
          $("div#rslt_expt_component").jqxGrid("updaterow", event.args.rowindex);
          $("#rslt_expt_component_save_button").jqxButton("disabled", false);
        }
      }
      
      function event_div_rslt_expt_component_rowselect(event) {
        $("#rslt_expt_component_del_button").jqxButton("disabled", false);        
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
      
      function event_div_test_case_right_click_pop_click(event) {
        if ($(event.target).text() == "추가") {
          case_add_window_pop();
        } else if ($(event.target).text() == "삭제") {
          case_del_window_pop();
        } else if ($(event.target).text() == "수정") {
          case_update_window_pop();
        } else if ($(event.target).text() == "케이스 테스트") {
          case_test();
        } else if ($(event.target).text() == "케이스 정보") {
          case_inform();
        }        
      }
      
      function event_div_test_blank_right_click_pop_click(event) {
        if ($(event.target).text() == "시나리오 추가") {
          scnrio_add_window_pop();
        }        
      }
      
      function event_input_new_rgst_window_ok_but_component_click() {
        if ($("div#new_rgst_window_header").html() == "시나리오 신규 등록") {
          var validCheckMsg = event_input_new_rgst_window_ok_but_component_click_validation(0);
          if (validCheckMsg != null) {
            cmnAlert(validCheckMsg);
          } else {
            cmnSyncCall("InsertNewScnrio", {scnrio_nm: $.trim($("input#new_rgst_window_nm_txt_component").val()), scnrio_desc: $("#new_rgst_window_desc_txt_component").val()}, callback, null);
          }
        } else if ($("div#new_rgst_window_header").html() == "케이스 신규 등록") {
          var validCheckMsg = event_input_new_rgst_window_ok_but_component_click_validation(1);
          if (validCheckMsg != null) {
            cmnAlert(validCheckMsg);
          } else {
            var scnrioNum = 0;
            if ($("div#data_tree_component").jqxTree("getSelectedItem").parentElement == null) {
              scnrioNum = $("div#data_tree_component").jqxTree("getSelectedItem").value;
            } else {
              scnrioNum = $("div#data_tree_component").jqxTree("getItem", $("div#data_tree_component").jqxTree("getSelectedItem").parentElement).value;
            }
            cmnSyncCall("InsertNewCase", {scnrio_num: scnrioNum, case_nm: $.trim($("input#new_rgst_window_nm_txt_component").val()), case_desc: $("#new_rgst_window_desc_txt_component").val()}, callback, null);
          }
        } else {
          cmnAlert("구현중");
        }
      }
      
      function event_input_new_rgst_window_cancel_but_component_click() {
        $("div#new_rgst_window").jqxWindow("close");
      }

      function event_input_data_save_but_click(event) {
        if ($("#case_input_component_save_button").jqxButton("disabled") == false) {
          var inputData = new Array();
          for (var i = 0; i < editedCaseInput.length; i++) {
            inputData.push({input_num: editedCaseInput[i].rowindex, input_nm: $("div#case_input_component").jqxGrid("getrowdata", editedCaseInput[i].rowindex).input_nm
                          , input_val: $("div#case_input_component").jqxGrid("getrowdata", editedCaseInput[i].rowindex).input_val});
          }
          cmnSyncCall("SaveCaseInput", {data: JSON.stringify({scnrio_num: $("div#data_tree_component").jqxTree("getItem", $("div#data_tree_component").jqxTree("getSelectedItem").parentElement).value
                                                              , case_num: $("div#data_tree_component").jqxTree("getSelectedItem").value
                                                              , input: inputData})}, callback, null);
        }
      }
      
      function event_expt_rslt_add_but_click(event) {
        var maxTestStepNum = 0;
        for (var i = 0; i < $("div#rslt_expt_component").jqxGrid("getrows").length; i++) {
          if (maxTestStepNum < $("div#rslt_expt_component").jqxGrid("getrowdata", i).test_step_num) {
            maxTestStepNum = $("div#rslt_expt_component").jqxGrid("getrowdata", i).test_step_num;
          }
        }
        var label = "";
        if (judgTypNm != null && judgTypNm.length > 0) {
          label = judgTypNm[0].label;
        }
        $("div#rslt_expt_component").jqxGrid("addrow", null, {test_step_num: maxTestStepNum + 1, judg_typ_nm: label, rslt_strd: ""});
        editedExptRslt.push({rowindex: $("div#rslt_expt_component").jqxGrid("getrows").length - 1, rowtype: "add"});
        $("#rslt_expt_component_save_button").jqxButton("disabled", false);
      }
      
      function event_expt_rslt_del_but_click(event) {
        if ($("#rslt_expt_component_del_button").jqxButton("disabled") == false) {
          for (var i = 0; i < editedExptRslt.length; i++) {
            if (editedExptRslt[i].rowindex == $("div#rslt_expt_component").jqxGrid("getselectedrowindex")) {
              if (editedExptRslt[i].rowtype == "add") {
                $("div#rslt_expt_component").jqxGrid("deleterow", $("div#rslt_expt_component").jqxGrid("getrowid", editedExptRslt[i].rowindex));
                $("#rslt_expt_component_del_button").jqxButton("disabled", true);
                var delRowIndex = editedExptRslt[i].rowindex;
                editedExptRslt.splice(i, 1);
                for (var j = 0; j < editedExptRslt.length; j++) {
                  if (editedExptRslt[j].rowindex > delRowIndex) {
                    editedExptRslt[j].rowindex = editedExptRslt[j].rowindex - 1;
                  }
                }
                if (editedExptRslt.length == 0) {
                  $("#rslt_expt_component_save_button").jqxButton("disabled", true);
                }
                return;
              } else if (editedExptRslt[i].rowtype == "delete") {
                if (typeof editedExptRslt[i].beforerowtype != "undefined" && editedExptRslt[i].beforerowtype == "edited") {
                  editedExptRslt[i].rowtype = "edited";
                  delete editedExptRslt[i].beforerowtype;
                } else {
                  editedExptRslt.splice(i, 1);
                }
                $("div#rslt_expt_component").jqxGrid("render");
                if (editedExptRslt.length == 0) {
                  $("#rslt_expt_component_save_button").jqxButton("disabled", true);
                }
                return;
              } else if (editedExptRslt[i].rowtype == "edited") {
                editedExptRslt[i].beforerowtype = "edited";
                editedExptRslt[i].rowtype = "delete";
                $("div#rslt_expt_component").jqxGrid("render");
                $("#rslt_expt_component_save_button").jqxButton("disabled", false);
                return;
              }
            }
          }
          editedExptRslt.push({rowindex: $("div#rslt_expt_component").jqxGrid("getselectedrowindex"), rowtype: "delete"});
          $("#rslt_expt_component_save_button").jqxButton("disabled", false);
          $("div#rslt_expt_component").jqxGrid("render");
        }
      }

      function event_expt_rslt_save_but_click(event) {
        if ($("#rslt_expt_component_save_button").jqxButton("disabled") == false) {
          var i = 0;
          var j = 0;
          var dataList = new Array();
          for (i = 0; i < editedExptRslt.length; i++) {
            if (editedExptRslt[i].rowtype == "add") {
              for (j = 0; j < judgTypNm.length; j++) {
                if (judgTypNm[j].label == $("div#rslt_expt_component").jqxGrid("getrowdata", editedExptRslt[i].rowindex).judg_typ_nm) {
                  break;
                }
              }
              dataList.push({modify_typ: 1, test_step_num: $("div#rslt_expt_component").jqxGrid("getrowdata", editedExptRslt[i].rowindex).test_step_num
                             , rslt_strd: $("div#rslt_expt_component").jqxGrid("getrowdata", editedExptRslt[i].rowindex).rslt_strd
                             , judg_typ_cd: judgTypNm[j].value});
            } else if (editedExptRslt[i].rowtype == "edited") {
              for (j = 0; j < judgTypNm.length; j++) {
                if (judgTypNm[j].label == $("div#rslt_expt_component").jqxGrid("getrowdata", editedExptRslt[i].rowindex).judg_typ_nm) {
                  break;
                }
              }
              var old_test_step_num;
              if (typeof editedExptRslt[i].beforeRowNum == "undefined") {
                old_test_step_num = $("div#rslt_expt_component").jqxGrid("getrowdata", editedExptRslt[i].rowindex).test_step_num;
              } else {
                old_test_step_num = editedExptRslt[i].beforeRowNum;
              }
              dataList.push({modify_typ: 2, test_step_num: $("div#rslt_expt_component").jqxGrid("getrowdata", editedExptRslt[i].rowindex).test_step_num
                             , rslt_strd: $("div#rslt_expt_component").jqxGrid("getrowdata", editedExptRslt[i].rowindex).rslt_strd
                             , judg_typ_cd: judgTypNm[j].value, old_test_step_num: old_test_step_num});
            } else if (editedExptRslt[i].rowtype == "delete") {
              dataList.push({modify_typ: 3, test_step_num: $("div#rslt_expt_component").jqxGrid("getrowdata", editedExptRslt[i].rowindex).test_step_num});
            }
          }
          cmnSyncCall("SaveTestExptRslt", {scnrio_num: $("div#data_tree_component").jqxTree("getItem", $("div#data_tree_component").jqxTree("getSelectedItem").parentElement).value
                                           , case_num: $("div#data_tree_component").jqxTree("getSelectedItem").value, data: JSON.stringify(dataList)}, callback, null);
        }
      }
      
      function event_a_menu_scnrio_new_click(event) {
        scnrio_add_window_pop();
      }
      
      function event_a_menu_import_click(event) {
        scnrio_file_upload();
      }
      
      function event_input_agent_down_pop_window_down_but_component_click(event) {
        cmnAlert("구현중");
      }
      
      function event_scnrio_src_cd_save() {
        cmnSyncCall("SaveSncrioSrcCd", {scnrio_num: $("div#data_tree_component").jqxTree("getSelectedItem").value, src_cd: codeMirrorEditor.getDoc().getValue()}, null, null);
      }
      
      function event_div_test_step_pop_window_close() {
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
          gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"],
          extraKeys: {
             "Ctrl-S": event_scnrio_src_cd_save
          }
        });
        codeMirrorEditor.setOption("theme", gCmEditorTheme);
        codeMirrorEditor.setOption("lineSeparator", "\n");
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
            var newButton = $("<div><img src='/FileDown?file_key=4W3AYzuLwhTwCJfNrBJcgZQL1bmgIxhKJLruOczF' style='width:20px;height:20px;'/></div>").attr("id", "toolbar_scnrio_new");
            tool.append(newButton);
            newButton.jqxButton({
              width: "24px",
              height: "24px",
            });
            newButton.on("click", function(event) {
              if ($("#toolbar_scnrio_new").jqxButton("disabled") != true) {
                scnrio_add_window_pop();
              }
            });
            break;
          case 1:
            var newButton = $("<div><img src='/FileDown?file_key=0xAoKZ11FQB3HyZH0bIpCaaphbKcyS3uXYn0G5rn' style='width:20px;height:20px;'/></div>").attr("id", "toolbar_case_new");
            tool.append(newButton);
            newButton.jqxButton({
              width: "24px",
              height: "24px",
            });
            newButton.on("click", function(event) {
              if ($("#toolbar_case_new").jqxButton("disabled") != true) {
                case_add_window_pop();
              }
            });
            break;
          case 2:
            var updateButton = $("<div><img src='/FileDown?file_key=y673Rwz0Ldj3Zu3zNCL5gpALrn1pogTcxpt36gQ2' style='width:20px;height:20px;'/></div>").attr("id", "toolbar_update");
            tool.append(updateButton);
            updateButton.jqxButton({
              width: "24px",
              height: "24px",
            });
            break;cmnAlert
            updateButton.on("click", function(event) {
              if ($("#toolbar_case_new").jqxButton("disabled") != true) {
                cmnAlert("구현중");
              }
            });
          case 3:
            var deleteButton = $("<div><img src='/FileDown?file_key=YwN3sUbnW2f7T2YrLy5lbLUztLD9EWDIyP3v6g4A' style='width:20px;height:20px;'/></div>").attr("id", "toolbar_del");
            tool.append(deleteButton);
            deleteButton.jqxButton({
              width: "24px",
              height: "24px",
            });
            deleteButton.on("click", function(event) {
              if ($("#toolbar_del").jqxButton("disabled") != true) {
                if ($("div#data_tree_component").jqxTree("getSelectedItem").parentElement == null) {
                  scnrio_del_window_pop();
                } else {
                  case_del_window_pop();
                }
              }
            });
            break;
          case 4:
            var scnrioTestButton = $("<div><img src='/FileDown?file_key=nTIw04flYSlW9VDRAkHtx8wRnPfNG85hIuHUVZg1' style='width:20px;height:20px;'/></div>").attr("id", "toolbar_scnrio_test");
            tool.append(scnrioTestButton);
            scnrioTestButton.jqxButton({
              width: "24px",
              height: "24px",
            });
            scnrioTestButton.on("click", function(event) {
              if ($("#toolbar_scnrio_test").jqxButton("disabled") != true) {
                scnrio_test();
              }
            });
            break;
          case 5:
            var caseTestButton = $("<div><img src='/FileDown?file_key=J0Rr7aMrbGXl0Op8r8Rs01KOBJDJNG5AC3UBBWga' style='width:20px;height:20px;'/></div>").attr("id", "toolbar_case_test");
            tool.append(caseTestButton);
            caseTestButton.jqxButton({
              width: "24px",
              height: "24px",
            });
            caseTestButton.on("click", function(event) {
              if ($("#toolbar_case_test").jqxButton("disabled") != true) {
                case_test();
              }
            });
            break;
          case 6:
            var uploadButton = $("<div><img src='/FileDown?file_key=9co4OmyQyfCvWpYQbxb8GLA0PAwLssHxYu5c26Zf' style='width:20px;height:20px;'/></div>").attr("id", "toolbar_upload");
            tool.append(uploadButton);
            uploadButton.jqxButton({
              width: "24px",
              height: "24px",
            });
            uploadButton.on("click", function(event) {
              if ($("#toolbar_upload").jqxButton("disabled") != true) {
                scnrio_file_upload();
              }
            });
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
          $("#case_input_component_save_button").jqxButton("disabled", true);
          $("#rslt_expt_component_save_button").jqxButton("disabled", true);
          $("#rslt_expt_component_del_button").jqxButton("disabled", true);
        } else if (act == "/cmn/cmn/main/GetCommonCode") {
          judgTypNm = [];
          for (var property in data) {
            judgTypNm.push({value: property, label: data[property]});
          }
          var judgTypNmSource = {
            datatype: "array",
            datafields: [
              {name: "label", type: "string"},
              {name: "value", type: "int"}
            ],
            localdata: judgTypNm
          };
          var judgTypNmAdapter = new $.jqx.dataAdapter(judgTypNmSource, {autoBind: true});
          $("div#rslt_expt_component").jqxGrid({
            editable: true,
            enabletooltips: true,
            columns: [{text: "테스트스텝번호", datafield: "test_step_num", width: 100, editable: true, columntype: "textbox", cellsalign: "right", cellclassname: cellExptRsltClass, cellbeginedit: cellbeginedit},
                      {text: "판정여부구분", datafield: "judg_typ_nm", width: "30%", editable: true, columntype: "dropdownlist"
                      , createeditor: function (row, value, editor) {
                        editor.jqxDropDownList({source: judgTypNmAdapter, displayMember: "label", valueMember: "value"});
                      }, cellclassname: cellExptRsltClass, cellbeginedit: cellbeginedit},
                      {text: "기준문구", datafield: "rslt_strd", editable: true, columntype: "textbox", cellclassname: cellExptRsltClass, cellbeginedit: cellbeginedit}
                     ],
            width: "100%",
            height: "100%",
            showtoolbar: true,
            rendertoolbar: function (statusbar) {
              var container = $("<div style='overflow: hidden; position: relative; margin: 5px;'></div>");
              var addBut = $("<div style='float: left; margin-left: 5px;'><img src='/FileDown?file_key=UtZ5mzcUPNjnNWjCwMG7xe6ecvrg3oxoCobS1rVD' style='width:18px;height:18px;'/></div>").attr("id", "rslt_expt_component_add_button");
              var delBut = $("<div style='float: left; margin-left: 5px;'><img src='/FileDown?file_key=1YctFgMgRu9r1XkxJXeo2ZJrowJ4LXBCuParLx4L' style='width:18px;height:18px;'/></div>").attr("id", "rslt_expt_component_del_button");
              var saveBut = $("<div style='float: left; margin-left: 5px;'><img src='/FileDown?file_key=GgnS17SPV1IuXIfUZGWXfykXCbOMyeB2S2AGKtWD' style='width:18px;height:18px;'/></div>").attr("id", "rslt_expt_component_save_button");
              container.append(addBut);
              container.append(delBut);
              container.append(saveBut);
              statusbar.append(container);
              addBut.jqxButton({width: 18, height: 18});
              delBut.jqxButton({width: 18, height: 18, disabled: true});
              saveBut.jqxButton({width: 18, height: 18, disabled: true});
              addBut.click(event_expt_rslt_add_but_click);
              delBut.click(event_expt_rslt_del_but_click);
              saveBut.click(event_expt_rslt_save_but_click);
              addBut.jqxTooltip({position: "mouse", content: "추가"});
              delBut.jqxTooltip({position: "mouse", content: "삭제"});
              saveBut.jqxTooltip({position: "mouse", content: "저장"});
            }
          });
        } else if (act == "InsertNewScnrio") {
          $("div#data_tree_component").jqxTree("addTo", {label: $("input#new_rgst_window_nm_txt_component").val(), value: data.scnrio_num});
          $("div#data_tree_component").jqxTree("addTo", {label: "샘플 케이스", value: 1}, $("div#data_tree_component").jqxTree("getItems")[$("div#data_tree_component").jqxTree("getItems").length - 1]);
          $("div#new_rgst_window").jqxWindow("close");
        } else if (act == "GetImportedSrcCdByScnrioNum") {
          $.ajax({
            url: "http://localhost:30710/execute",
            type: "POST",
            data: {scnrio_num: input_param.scnrio_num, dtm: data.dtm, test_data: JSON.stringify(data.test_data)},
            async: true,
            success: function(subData) {
              if (typeof input_param.case_num == "undefined") {
                cmnSyncCall("GetTestStepInfo", {scnrio_num: input_param.scnrio_num}, callback, data.dtm);
              } else {
                cmnSyncCall("GetTestStepInfo", {scnrio_num: input_param.scnrio_num, case_num: input_param.case_num}, callback, data.dtm);                
              }
            },
            error: function(request, status, error) {
              $("div#agent_down_pop_window").jqxWindow("open");
            }
          });
        } else if (act == "InsertNewCase") {
          if ($("div#data_tree_component").jqxTree("getSelectedItem").parentElement == null) {
            $("div#data_tree_component").jqxTree("addTo", {label: $("input#new_rgst_window_nm_txt_component").val(), value: data.case_num}, $("div#data_tree_component").jqxTree("getSelectedItem"));
          } else {
            $("div#data_tree_component").jqxTree("addTo", {label: $("input#new_rgst_window_nm_txt_component").val(), value: data.case_num}
                                                 , $("div#data_tree_component").jqxTree("getItem", $("div#data_tree_component").jqxTree("getSelectedItem").parentElement));            
          }
          $("div#new_rgst_window").jqxWindow("close");
        } else if (act == "SaveCaseInput") {
          editedCaseInput = new Array();
          $("#case_input_component_save_button").jqxButton("disabled", true);
          for (var i = 0; i < $("div#case_input_component").jqxGrid("getrows").length; i++) {
            $("div#case_input_component").jqxGrid("updaterow", i, $("div#case_input_component").jqxGrid("getrowdata", i));
          }
        } else if (act == "DelTestScnrio") {
          for (var i = 0; i < $("div#data_tree_component").jqxTree("getItems").length; i++) {
            if ($("div#data_tree_component").jqxTree("getItems")[i].parentElement == null && $("div#data_tree_component").jqxTree("getItems")[i].value == input_param.scnrio_num) {
              $("div#data_tree_component").jqxTree("removeItem", $("div#data_tree_component").jqxTree("getItems")[i]);
              break;
            }
          }
        } else if (act == "DelTestCase") {
          for (var i = 0; i < $("div#data_tree_component").jqxTree("getItems").length; i++) {
            if ($("div#data_tree_component").jqxTree("getItems")[i].parentElement != null
                && $("div#data_tree_component").jqxTree("getItem", $("div#data_tree_component").jqxTree("getItems")[i].parentElement).value == input_param.scnrio_num
                && $("div#data_tree_component").jqxTree("getItems")[i].value == input_param.case_num) {
              $("div#data_tree_component").jqxTree("removeItem", $("div#data_tree_component").jqxTree("getItems")[i]);
              break;
            }
          }          
        } else if (act == "SaveTestExptRslt") {
          for (var i = 0; i < editedExptRslt.length; i++) {
            if (editedExptRslt[i].rowtype == "delete") {
              $("div#rslt_expt_component").jqxGrid("deleterow", $("div#rslt_expt_component").jqxGrid("getrowid", editedExptRslt[i].rowindex));
              for (var j = 0; j < editedExptRslt.length; j++)  {
                if (editedExptRslt[i].rowindex < editedExptRslt[j].rowindex) {
                  editedExptRslt[j].rowindex = editedExptRslt[j].rowindex - 1;
                }
              }
            }
          }
          editedExptRslt = [];
          var tmpList = new Array();
          for (var i = 0; i < $("div#rslt_expt_component").jqxGrid("getrows").length; i++) {
            tmpList.push($("div#rslt_expt_component").jqxGrid("getrows")[i]);
          }
          var retList = sort(tmpList, "test_step_num");
          $("div#rslt_expt_component").jqxGrid("clear")
          for (var i = 0; i < retList.length; i++) {
            $("div#rslt_expt_component").jqxGrid("addrow", null, retList[i]);
          }
          $("div#rslt_expt_component").jqxGrid("render");
        } else if (act == "GetTestStepInfo") {
          openTestRsltWindow();
          $("div#test_step_pop_window_grid_component").jqxGrid("clear");
          $("div#test_step_pop_window_grid_component").jqxGrid("addrow", null, {test_step_num: data[0].test_step_num
                                                                               , scnrio_nm: data[0].scnrio_nm
                                                                               , case_nm: data[0].case_nm
                                                                               , scnrio_num: data[0].scnrio_num
                                                                               , case_num: data[0].case_num
                                                                               , log: ""
                                                                               , test_st_nm: "<img width=\"24px\" height:\"24px\" src=\"http://kokodogdev.fun25.co.kr/FileDown?file_key=CG5jTwaR34Dv4CpuPhiBQtquRpRHAastPcxrvMgs\"/>"
                                                                               , dtm: callbackVar
                                                                               , test_st_cd : 1
                                                                               });
          for (var i = 1; i < data.length; i++) {
            $("div#test_step_pop_window_grid_component").jqxGrid("addrow", null, {test_step_num: data[i].test_step_num
                                                                                 , scnrio_nm: data[i].scnrio_nm
                                                                                 , case_nm: data[i].case_nm
                                                                                 , scnrio_num: data[i].scnrio_num
                                                                                 , case_num: data[i].case_num
                                                                                 , log: ""
                                                                                 , test_st_nm: "<div style=\"color:#ffffff\">진행중</div>"
                                                                                 , dtm: callbackVar
                                                                                 , test_st_cd : 3
                                                                                 });
          }
          setTimeout(periodicTestResultCall, 1000);
        }
      }
      
      function periodicTestResultCall() {
        for (var i = 0; i < $("div#test_step_pop_window_grid_component").jqxGrid("getrows").length; i++) {
          if ($("div#test_step_pop_window_grid_component").jqxGrid("getrows")[i].test_st_cd == 1) {
            nextTestResultCall(i);
            return;
          }
        }
      }
      
      function nextTestResultCall(row) {
        $.ajax({
          url: "http://localhost:30710/return",
          type: "POST",
          dataType : "json",
          data: {scnrio_num: $("div#test_step_pop_window_grid_component").jqxGrid("getrows")[row].scnrio_num
                 , dtm: $("div#test_step_pop_window_grid_component").jqxGrid("getrows")[row].dtm
                 , case_num: $("div#test_step_pop_window_grid_component").jqxGrid("getrows")[row].case_num
                 , test_step_num: $("div#test_step_pop_window_grid_component").jqxGrid("getrows")[row].test_step_num
                },
          async: true,
          success: function(data) {
            getTestRsltFailNo = 0;
            if (data.test_st_cd == 2) {
              $("div#test_step_pop_window_grid_component").jqxGrid("setcellvalue", row, "test_st_cd", 2);
              if (data.test_rslt_cd == 1) {
                $("div#test_step_pop_window_grid_component").jqxGrid("setcellvalue", row, "test_st_nm", "<div style=\"color:#0f0fb9\">정상</div>");
              } else if (data.test_rslt_cd == 3) {
                $("div#test_step_pop_window_grid_component").jqxGrid("setcellvalue", row, "test_st_nm", "<div style=\"color:#b90f0f\">오류</div>");                
              } else {
                $("div#test_step_pop_window_grid_component").jqxGrid("setcellvalue", row, "test_st_nm", "<div style=\"color:#b90f0f\">비정상</div>");
              }
              $("div#test_step_pop_window_grid_component").jqxGrid("setcellvalue", row, "log", data.log);
              if ($("div#test_step_pop_window_grid_component").jqxGrid("getrows").length > row + 1) {
                nextTestResultCall(row + 1);
              }
            } else {
              $("div#test_step_pop_window_grid_component").jqxGrid("setcellvalue", row, "test_st_cd", 1);
              $("div#test_step_pop_window_grid_component").jqxGrid("setcellvalue", row, "test_st_nm", "<img width=\"24px\" height:\"24px\" src=\"http://kokodogdev.fun25.co.kr/FileDown?file_key=CG5jTwaR34Dv4CpuPhiBQtquRpRHAastPcxrvMgs\"/>");
              if ($("div#test_step_pop_window").jqxWindow("isOpen") == true) {
                setTimeout(periodicTestResultCall, 1000);
              }
            }
          },
          error: function(request, status, error) {
            if (getTestRsltFailNo >= 3) {
              cmnAlert("테스트 가져오기 오류가 발생했습니다.");
            } else {
              getTestRsltFailNo++;
              setTimeout(periodicTestResultCall, 1000);
            }
          }
        });
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
          codeMirrorEditor.getDoc().setValue("");
          $("div#div_case_input_rslt").css("display", "none");
          $("div#div_src_cd").css("display", "block");
          cmnSyncCall("GetSrcCdByScnrioNum", {scnrio_num: $("div#data_tree_component").jqxTree("getItem", target).value}, callback, null);
        } else {
          $("div#case_input_component").jqxGrid("clear");
          $("div#rslt_expt_component").jqxGrid("clear");
          $("div#div_src_cd").css("display", "none");
          $("div#div_case_input_rslt").css("display", "block");
          $("div#case_input_component").jqxGrid("unselectrow", $("div#case_input_component").jqxGrid("getselectedrowindex"));
          $("div#rslt_expt_component").jqxGrid("unselectrow", $("div#rslt_expt_component").jqxGrid("getselectedrowindex"));
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
        $("input#new_rgst_window_nm_txt_component").jqxInput("val", "");
        $("#new_rgst_window_desc_txt_component").jqxTextArea("val", "");
        $("div#new_rgst_window").jqxWindow("open");
      }
      
      function event_input_new_rgst_window_ok_but_component_click_validation(typ) {
        if (typ == 0) {
          if ($.trim($("input#new_rgst_window_nm_txt_component").val()) == "") {
            return "시나리오 명을 넣어주세요";
          } else {
            var RegExp = /[\{\}\[\]\/?.,;:|\)*~`!^+┼<>@\#$%&\'\"\\\(\=]/gi;
            if($("input#new_rgst_window_nm_txt_component").val().match(RegExp) != null) {
              return "정상적인 시나리오 명을 넣어주세요[특수문자 금지]";
            }
          }
        } else if (typ == 1) {
          if ($.trim($("input#new_rgst_window_nm_txt_component").val()) == "") {
            return "케이스 명을 넣어주세요";
          } else {
            var RegExp = /[\{\}\[\]\/?.,;:|\)*~`!^+┼<>@\#$%&\'\"\\\(\=]/gi;
            if($("input#new_rgst_window_nm_txt_component").val().match(RegExp) != null) {
              return "정상적인 케이스 명을 넣어주세요[특수문자 금지]";
            }
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
            cmnSyncCall("DelTestScnrio", {scnrio_num: $("div#data_tree_component").jqxTree("getSelectedItem").value}, callback, null);
          } else {
            cmnAlert("삭제가 취소되었습니다.");
          }
        } else if (callbackVar == 2) {
          if (ret == true) {
            cmnSyncCall("GetImportedSrcCdByScnrioNum", {scnrio_num: $("div#data_tree_component").jqxTree("getSelectedItem").value}, callback, null);
          } else {
            cmnAlert("테스트가 취소되었습니다.");
          }
        } else if (callbackVar == 3) {
          if (ret == true) {
            cmnSyncCall("GetImportedSrcCdByScnrioNum", {scnrio_num: $("div#data_tree_component").jqxTree("getItem", $("div#data_tree_component").jqxTree("getSelectedItem").parentElement).value, case_num: $("div#data_tree_component").jqxTree("getSelectedItem").value}, callback, null);
          } else {
            cmnAlert("테스트가 취소되었습니다.");
          }
        } else if (callbackVar == 4) {
          if (ret == true) {
            cmnSyncCall("DelTestCase", {scnrio_num: $("div#data_tree_component").jqxTree("getItem", $("div#data_tree_component").jqxTree("getSelectedItem").parentElement).value
                                      , case_num: $("div#data_tree_component").jqxTree("getSelectedItem").value}, callback, null);
          } else {
            cmnAlert("삭제가 취소되었습니다.");
          }
        }
      }
      
      function scnrio_test() {
        cmnConfirm(callbackConfirm, "췝페이지 메시지", "테스트를 수행하시겠습니까?", 2);
      }
      
      function scnrio_file_upload() {
        var form = $("<form>");
        form.append($("<input>").attr({type: "file", id: "scnrio_file_upload", accept: ".side"}).css("display", "none"));
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
        $("input#new_rgst_window_nm_txt_component").jqxInput("val", "");
        $("#new_rgst_window_desc_txt_component").jqxTextArea("val", "");
        $("div#new_rgst_window").jqxWindow("open");        
      }
      
      function case_del_window_pop() {
        cmnConfirm(callbackConfirm, "췝페이지 메시지", "삭제하시겠습니까?", 4);
      }
      
      function case_update_window_pop() {
        cmnAlert("구현중");
      }
      
      function case_test() {
        cmnConfirm(callbackConfirm, "췝페이지 메시지", "테스트를 수행하시겠습니까?", 3);        
      }
      
      function setButEnableChk() {
        if ($("div#data_tree_component").jqxTree("getSelectedItem") == null) {
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_case_new", true);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_update", true);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_delete", true);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_scnrio_test", true);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_case_test", true);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_import", true);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_inform", true);
          $("#toolbar_case_new").jqxButton("disabled", true);
          $("#toolbar_update").jqxButton("disabled", true);
          $("#toolbar_del").jqxButton("disabled", true);
          $("#toolbar_scnrio_test").jqxButton("disabled", true);
          $("#toolbar_case_test").jqxButton("disabled", true);
          $("#toolbar_upload").jqxButton("disabled", true);
        } else if ($("div#data_tree_component").jqxTree("getSelectedItem").parentElement == null) {
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_case_new", false);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_update", false);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_delete", false);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_scnrio_test", false);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_case_test", true);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_import", false);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_inform", false);
          $("#toolbar_case_new").jqxButton("disabled", false);
          $("#toolbar_update").jqxButton("disabled", false);
          $("#toolbar_del").jqxButton("disabled", false);
          $("#toolbar_scnrio_test").jqxButton("disabled", false);
          $("#toolbar_case_test").jqxButton("disabled", true);
          $("#toolbar_upload").jqxButton("disabled", false);
        } else {
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_case_new", false);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_update", false);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_delete", false);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_scnrio_test", true);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_case_test", false);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_import", false);
          $("div#top_menu_bar_component").jqxMenu("disable", "li_menu_inform", false);
          $("#toolbar_case_new").jqxButton("disabled", false);
          $("#toolbar_case_new").jqxButton("disabled", false);
          $("#toolbar_update").jqxButton("disabled", false);
          $("#toolbar_del").jqxButton("disabled", false);
          $("#toolbar_scnrio_test").jqxButton("disabled", true);
          $("#toolbar_case_test").jqxButton("disabled", false);
          $("#toolbar_upload").jqxButton("disabled", false);
        }
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
            if (editedExptRslt[i].rowtype == "add") {
              return "cell_added";
            } else if (editedExptRslt[i].rowtype == "delete") {
              return "cell_deleted";
            } else {
              return "cell_edited";
            }
          }
        }
        return "cell_not_edited";
      }

      var cellbeginedit = function (row, datafield, columntype, value) {
        for (var i = 0; i < editedExptRslt.length; i++) {
          if (editedExptRslt[i].rowindex == row) {
            if (editedExptRslt[i].rowtype == "delete") {
              return false;
            } else {
              return true;
            }
          }
        }
        return true;
      }
      
      function sort(list, dict) {
        var retList = list.slice();
        for (var i = 0; i < retList.length; i++) {
          for (var j = i + 1; j < retList.length; j++) {
            if (eval("retList[" + i + "]." + dict) > eval("retList[" + j + "]." + dict)) {
              var tmpList = retList[i];
              retList[i] = retList[j];
              retList[j] = tmpList;
            }
          }
        }
        return retList;
      }
      
      function openTestRsltWindow() {
        if ($(window).width() >= 1300) {
          $("div#test_step_pop_window").jqxWindow("width", "1200px");
        } else {
          $("div#test_step_pop_window").jqxWindow("width", "" + ($(window).width() -150) + "px");
        }
        if ($(window).height() >= 860) {
          $("div#test_step_pop_window").jqxWindow("height", "800px");
        } else {
          $("div#test_step_pop_window").jqxWindow("height", "" + ($(window).height() -120) + "px");
        }
        $("div#test_step_pop_window").jqxWindow("open");
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
        <li id="pop_menu_new_scnrio">시나리오 추가</li>
        <li id="pop_menu_new_delete">삭제</li>
        <li id="pop_menu_new_update">수정</li>
        <li type="separator"></li>
        <li id="pop_menu_scnrio_file_upload">시나리오파일 업로드</li>
        <li id="pop_menu_scnrio_test">시나리오 테스트</li>
        <li type="separator"></li>
        <li id="pop_menu_scnrio_inform">시나리오 정보</li>
        <li type="separator"></li>
        <li id="pop_menu_new_case">케이스 추가</li>
      </ul>
    </div>
    <div id="test_case_right_click_pop">
      <ul>
        <li id="pop_menu_case_new_case">추가</li>
        <li id="pop_menu_case_delete">삭제</li>
        <li>수정</li>
        <li type="separator"></li>
        <li>케이스 테스트</li>
        <li type="separator"></li>
        <li>케이스 정보</li>
      </ul>
    </div>
    <div id="test_blank_right_click_pop">
      <ul>
        <li id="pop_menu_blank_new_scnrio">시나리오 추가</li>
      </ul>
    </div>
    <input type="file" id="scnrio_file_upload" style="display:none;"/>
    <div id="agent_down_pop_window">
      <div class="agent_down_pop_window_header" id="agent_down_pop_window_header">
        에이전트 설치 안내
      </div>
      <div style="overflow: hidden;" class="window_rgst_content">
        <div id="agent_down_pop_window_label" class="agent_down_pop_window_label">
          테스트를 수행하기 위해서는 에이전트 파일이 필수로 설치되어야 합니다.<br/>
          아래 다운로드 버튼을 클릭하고 다운받은 파일을 실행하면 테스트를 위한<br/>
          에이전트 파일이 설치됩니다.<br/>
          설치 후에 다시 테스트를 진행하시기 바립니다.<br/>
        </div>
        <div class="agent_down_pop_window_down_but">
          <input id="agent_down_pop_window_down_but_component" type="button" value="다운로드"/>
        </div>
      </div>      
    </div>
    <div id="test_step_pop_window">
      <div class="test_step_pop_window_header" id="test_step_pop_window_header">
        테스트 진행 경과 상태
      </div>
      <div style="overflow: hidden;" class="window_rgst_content">
        <div id="test_step_pop_window_grid" class="test_step_pop_window_grid">
          <div id="test_step_pop_window_grid_component" class="test_step_pop_window_grid_component">
          </div>
        </div>
      </div>      
    </div>
  </body>
</html>