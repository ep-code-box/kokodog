<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>KOKODOG[Proposal Analysis]</title>
    <link rel="shortcut icon" type="image/x-icon" href="/FileDown?file_key=tId6m0peoFkF2GYcAfAJG48kOE5Djo5ky49MFtnX" />
    <link rel="stylesheet" href="/css/skd/ppa/main.css"/>
    <link rel="stylesheet" href="/js/jqwidgets/styles/jqx.base.css" type="text/css"/>
    <link rel="stylesheet" href="/js/jqwidgets/styles/jqx.energyblue.css" type="text/css"/>
    <link rel="stylesheet" href="/js/jqwidgets/styles/jqx.orange.css" type="text/css"/>
    <script type="text/javascript" src="/js/jquery/jquery-3.2.1.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxcore.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxloader.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxmenu.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxsplitter.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxfileupload.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxdropdownlist.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxbuttons.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxgrid.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxlistbox.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxscrollbar.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxdata.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxgrid.filter.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxgrid.columnsresize.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxgrid.sort.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxgrid.selection.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxresponsivepanel.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxinput.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxnotification.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxwindow.js"></script>
    <script src="/js/cmn.js"></script>
    <script type="text/javascript">
      var fileKey = null;
      var morphemeData = null;
      $(document).ready(function() {
        contentInitLoad();
        contentEventLoad();
        setComoboCodeMapping();
        GetPastFileUploadList();
      });
      
      /* jqWidget을 사용하는 각종 함수 들 첫 오픈 처리 */
      function contentInitLoad() {
        $("div#top_menu_bar_component").jqxMenu({
          width: "100%",
          height: "100%"
        });
        $("div#left_right_splitter_component").jqxSplitter({
          width: "100%",
          height: "100%",
          panels: [{
            size: "40%",
            min: "20%"
          }, {
            min: "40%"
          }]
        });
        $("div#file_upload_component").jqxFileUpload({
          width: "100%",
          autoUpload: true,
          multipleFilesUpload: false,
          uploadUrl: "/skd/ppa/main/FileUpload",
          fileInputName: "fileToUpload"
        });
        $("div#past_upload_file_list_component").jqxDropDownList({
          width: "100%",
          height: "100%"
        });
        $("div#past_upload_file_del_but_component").jqxButton({
          width: "100%",
          height: "100%",
          value: "삭제",
          disabled: true
        });
        $("div#past_upload_file_process_but_component").jqxButton({
          width: "100%",
          height: "100%",
          value: "조회",
          disabled: true
        });
        $("div#view_data_type_combo_component").jqxDropDownList({
          width: "100%",
          height: "100%"
        });
        $("div#view_data_list_component").jqxGrid({
          width: "100%",
          height: "100%",
          columnsheight: 25,
          columnsresize: true,
          selectionmode: "none",
          enabletooltips: true,
          showfilterrow: true,
          filterable: true,
          sortable: true,
          filtermode: "default",
          columns: [
            {text: "No", datafield: "no", width: 50, cellsalign: "right", cellsrenderer: cellsrenderer},
            {text: "형태소", datafield: "vocabulary", width: 100, cellsalign: "left", cellsrenderer: cellsrenderer},
            {text: "품사태그", datafield: "part_of_speach_tag", cellsalign: "left", width: 70, cellsrenderer: cellsrenderer},
            {text: "의미부류", datafield: "meaning", width: 100, cellsalign: "left", cellsrenderer: cellsrenderer},
            {text: "종성유무", datafield: "final_consonant_yn", width: 70, cellsalign: "left", cellsrenderer: cellsrenderer},
            {text: "발음", datafield: "pronounce", width:100, cellsalign: "left", cellsrenderer: cellsrenderer},
            {text: "타입", datafield: "type", width: 100, cellsalign: "left", cellsrenderer: cellsrenderer},
            {text: "첫번째품사", datafield: "first_part_of_speach_tag", width: 100, cellsalign: "left", cellsrenderer: cellsrenderer},
            {text: "마지막품사", datafield: "last_part_of_speach_tag", width: 100, cellsalign: "left", cellsrenderer: cellsrenderer},
            {text: "표현", datafield: "expression", cellsalign: "left", cellsrenderer: cellsrenderer}
          ]
        });
        $("div#top_chat_toggle_button_component").jqxButton({
          width: "100%",
          height: "100%",
          imgSrc: "/FileDown?file_key=sP2eZeZJ2WaKM3fWXyKgXkERfBkA8JWvty0TJK13"
        });
        $("div#top_chat_refresh_button_component").jqxButton({
          width: "100%",
          height: "100%",
          disabled: true,
          imgSrc: "/FileDown?file_key=fuZm4Ylwru55YVRQdID77h7OTyJjZurfOlzEBu9N"          
        });
        $("div#chat_component").jqxResponsivePanel({
          width: "100%",
          height: "100%",
          collapseBreakpoint: 500,
          animationType: "slide",
          animationDirection: "right",
          autoClose: false
        });
        $("input#chatbot_input_component").jqxInput({
          placeHolder: "질문을 입력하세요.",
          height: "100%",
          width: "100%"
        });
        $("div#chatbot_del_confirm_window_component").jqxWindow({
          autoOpen: false,
          resizable: false,
          okButton: $("input#chatbot_del_confirm_window_component_ok"),
          cancelButton: $("input#chatbot_del_confirm_window_component_ok"),
          isModal: true,
          initContent: function () {
            $("input#chatbot_del_confirm_window_component_ok").jqxButton({
              width: "80px",
              height: "30px"
            });
            $("input#chatbot_del_confirm_window_component_cancel").jqxButton({
              width: "80px",
              height: "30px"
            });
            $("input#chatbot_del_confirm_window_component_ok").focus();
          },
          width: "270px",
          height: "130px"
        });
      }

      /* jqWidget을 사용하는 각종 이벤트들 맵핑 처리 */
      function contentEventLoad() {
        $("a#menu_scen_manage").click(event_a_menu_scen_manage_click);
        $("a#menu_exit").click(event_a_menu_exit_click);
        $("a#help_help").click(event_a_help_help_click);
        $("a#help_about").click(event_a_menu_help_about_click);
        $("div#file_upload_component").on("uploadEnd", event_div_file_upload_component_upload_end);
        $("div#past_upload_file_list_component").on("change", event_div_past_upload_file_list_component);
        $("div#past_upload_file_del_but_component").click(event_div_past_upload_file_del_but_component);
        $("div#past_upload_file_process_but_component").click(event_div_past_upload_file_process_but_component);
        $("div#view_data_type_combo_component").on("change", event_div_view_dat_type_combo_component_change);
        $("div#top_chat_toggle_button_component").on("click", event_div_top_chat_toggle_button_component_click);
        $("input#chatbot_input_component").keydown(event_input_chatbot_input_component_onkeypress);
        $("div#top_chat_refresh_button_component").on("click", event_div_top_chat_refresh_button_component_click);
        $("div#chat_component").on("open", event_div_chat_component_open);
        $("div#chat_component").on("close", event_div_chat_component_close);
        $("input#chatbot_del_confirm_window_component_ok").on("click", event_chatbot_del_confirm_window_component_ok_click);
        $("input#chatbot_del_confirm_window_component_cancel").on("click", event_chatbot_del_confirm_window_component_cancel_click);
      }
      
      function setComoboCodeMapping() {
        setCmnComboCodeMapping($("div#view_data_type_combo_component"), 31);
      }
      
      function event_a_menu_scen_manage_click() {
        
      }
      
      function event_a_menu_exit_click() {
        
      }
      
      function event_a_help_help_click() {
        
      }
      
      function event_a_menu_help_about_click() {
        
      }
      
      function event_div_view_dat_type_combo_component_change() {
        setMorphemeList();
      }
      
      function event_div_file_upload_component_upload_end(event) {
        var response = JSON.parse($(event.args.response).html());
        fileKey = response.file_key;
        cmnSyncCall("ConvToHtml", {file_key : fileKey}, callback, null);
      }
      
      function event_div_past_upload_file_list_component(event) {
        if (event.args.index >= 0) {
          $("div#past_upload_file_process_but_component").jqxButton("disabled", false);
          $("div#past_upload_file_del_but_component").jqxButton("disabled", false);
        } else {
          $("div#past_upload_file_process_but_component").jqxButton("disabled", true);
          $("div#past_upload_file_del_but_component").jqxButton("disabled", true);          
        }
      }
      
      function event_div_past_upload_file_del_but_component() {
        cmnSyncCall("DeletePastFileUpload", {file_key: $("div#past_upload_file_list_component").jqxDropDownList("val")}, callback, $("div#past_upload_file_list_component").jqxDropDownList("val"));
      }
      
      function event_div_past_upload_file_process_but_component() {
        fileKey = $("div#past_upload_file_list_component").jqxDropDownList("val");
        cmnSyncCall("ConvToHtml", {file_key : fileKey}, callback, null);
      }
      
      function event_div_top_chat_toggle_button_component_click() {
        if ($("div#chat_component").jqxResponsivePanel("isOpened") == true) {
          $("div#top_chat_refresh_button_component").jqxButton("disabled", true);
          $("div#chat_component").jqxResponsivePanel("close");
        } else {
          cmnSyncCall("ProdChatInit", {}, callback, null);
        }
      }
      
      function event_div_top_chat_refresh_button_component_click() {
        if ($("div#top_chat_refresh_button_component").jqxButton("disabled") == false) {
          $("div#chatbot_del_confirm_window_component").jqxWindow("open");
        }
      }
      
      function event_input_chatbot_input_component_onkeypress(event) {
        if(event.keyCode == 13 && $("input#chatbot_input_component").val().length > 0) {
          addChatList($("input#chatbot_input_component").val(), 1);
          cmnSyncCall("ProdChat", {text:  $("input#chatbot_input_component").val()}, callback, null);
          $("input#chatbot_input_component").val("");
        }
      }
      
      function event_div_chat_component_open() {
        $("div#top_chat_refresh_button_component").jqxButton("disabled", false);
        $("input#chatbot_input_component").jqxInput("focus");
      }
      
      function event_div_chat_component_close() {
        $("div.chat").css("z-index", -1);
      }
      
      function GetPastFileUploadList() {
        cmnSyncCall("GetPastFileUploadList", {}, callback, null);
      }
      
      function event_chatbot_del_confirm_window_component_ok_click() {
        cmnSyncCall("RefreshChatBot", {}, callback, null);        
      }
      
      function event_chatbot_del_confirm_window_component_cancel_click() {
        $("div#chatbot_del_confirm_window_component").jqxWindow("close");
      }
      
      function callback(data, act, input_param, callbackVar) {
        if (act == "ConvToHtml") {
          $("div#doc_component").children("iframe#iframe_doc_component").remove();
          $("div#doc_component").append($("<iframe>", {id: "iframe_doc_component", name: "iframe_doc_component", class: "iframe_doc_component"}));
          $("body").children("form#doc_load_form").remove();
          var doc_load_form = $("<form>", {id: "doc_load_form", method: "post", target: "iframe_doc_component", action: "/skd/ppa/main/GetConvHtmlPage"});
          doc_load_form.append($("<input>", {type: "hidden", name: "file_key", value: fileKey}));
          $("body").append(doc_load_form);
          $("form#doc_load_form").submit();
          cmnSyncCall("GetMorphemeDetailList", {file_key: fileKey}, callback, null);
        } else if (act == "GetPastFileUploadList") {
          var i = 0;
          for (i = 0; i < data.length; i++) {
            var date = new Date(data[i].eff_sta_dtm);
            var nowDateStr = pad(date.getFullYear(), 4) + "-" + pad(date.getMonth() + 1, 2) + "-" + pad(date.getDate(), 2) + " " + pad(date.getHours(), 2) + ":" + pad(date.getMinutes(), 2) + ":" + pad(date.getSeconds(), 2);
            $("div#past_upload_file_list_component").jqxDropDownList("addItem", {label: "[" + nowDateStr + "]" + data[i].file_nm, value: data[i].file_key});
          }
        } else if (act == "DeletePastFileUpload") {
          var i = 0;
          for (i = 0; i < $("div#past_upload_file_list_component").jqxDropDownList("getItems").length; i++) {
            if ($("div#past_upload_file_list_component").jqxDropDownList("getItem", i).value == callbackVar) {
              if ($("div#past_upload_file_list_component").jqxDropDownList("getSelectedItem").index == i) {
                $("div#past_upload_file_list_component").jqxDropDownList("selectIndex", -1);
              }
              $("div#past_upload_file_list_component").jqxDropDownList("removeAt", i);
            }
          }
        } else if (act == "GetMorphemeDetailList") {
          morphemeData = data;
          setMorphemeList();
        } else if (act == "ProdChat") {
          addChatList(data.text, 2);
        } else if (act == "ProdChatInit") {
          $("div.chat").css("z-index", 298);
          var i = 0;
          $("div.chat_list").html("");
          for (i = 0; i < data.length; i++) {
            addChatList(data[data.length - i - 1].text, data[data.length - i - 1].conv_main_cd);
          }
          $("div#chat_component").jqxResponsivePanel("open");          
        } else if (act == "RefreshChatBot") {
          $("div.chat_list").html("");
          addChatList(data.text, 2);
          $("div#chatbot_del_confirm_window_component").jqxWindow("close");
        }
      }
      
      function addChatList(text, type) {
        var new_component = $("<div>", {class: "chatbot_message"}).addClass("chatbot_message_pos_" + type);
        new_component.appendTo("div.chat_list");
        var new_component_sub = $("<div>");
        new_component_sub.html(text);
        new_component_sub.jqxNotification({
          width: "250px",
          opacity: 0.9,
          autoOpen: true,
          autoClose: false,
          showCloseButton: false,
          closeOnClick: false,
          template: null,
          appendContainer: "div.chatbot_message:last-child",
          theme: (type == 1 ? "energyblue" : "orange")
        });
        new_component_sub.appendTo(new_component);
        $("div.chat_list").append($("<p>").css("clear", "both").html(" "));
        $("div.chat_list").scrollTop($("div.chat_list")[0].scrollHeight);
      }

      var cellsrenderer = function (row, columnfield, value, defaulthtml, columnproperties) {
        if ($("div#view_data_list_component").jqxGrid("getrowdata", row)["meaning"] == "상품요건서분석") {
          return "<span style=\"margin:4px 4px;float:" + columnproperties.cellsalign + ";color:#FF0000;\">" + value + "</span>";
        } else {
          return "<span style=\"margin:4px 4px;float:" + columnproperties.cellsalign + ";color:#000000;\">" + value + "</span>";
        }
      }
      
      function setMorphemeList() {
        var i = 0;
        var sourceDataList = [];
        if (morphemeData == null) {
          return;
        }
        for (i = 0; i < morphemeData.length; i++) {
          var strFontsStart = "<div class=\"voca_selected\">";
          var strFontEnd = "</div>";
          if ($("div#view_data_type_combo_component").jqxDropDownList().val() == "1"
             || ($("div#view_data_type_combo_component").jqxDropDownList().val() == "2" && morphemeData[i].type[0].substring(0, 1) == "N")
             || ($("div#view_data_type_combo_component").jqxDropDownList().val() == "3" && morphemeData[i].type[1] == "상품요건서분석")) {
            sourceDataList.push({no: i + 1, vocabulary: morphemeData[i].voca.replace(/</gi, "&lt;").replace(/>/gi, "&gt;"), part_of_speach_tag: morphemeData[i].type[0], meaning: morphemeData[i].type[1]
                                 , final_consonant_yn: morphemeData[i].type[2], pronounce: morphemeData[i].type[3], type: morphemeData[i].type[4]
                                 , first_part_of_speach_tag: morphemeData[i].type[5], last_part_of_speach_tag: morphemeData[i].type[6], expression: morphemeData[i].type[7]});
          }
        }
        $("div#view_data_list_component").jqxGrid("clear");
        $("div#view_data_list_component").jqxGrid("addrow", null, sourceDataList);        
      }
      
      function pad(number, size) {
        var str = "" + number;
        if (str.length > size) {
          throw "Number size is bigger than request size";
        }
        var i = 0;
        for (i = str.length; i < size; i++) {
          str = "0" + str;
        }
        return str;
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
                <li id="li_menu_scen_manage">
                  <a id="menu_scen_manage">Scenario Management</a>
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
      <div class="top_chat_toggle_button">
        <div id="top_chat_toggle_button_component">
        </div>
      </div>
      <div class="top_chat_refresh_button">
        <div id="top_chat_refresh_button_component">
        </div>
      </div>
      <div class="content">
        <div id="left_right_splitter_component">
          <div class="left_splitter">
            <div class="past_upload_file">
              <div class="past_upload_file_list">
                <div id="past_upload_file_list_component">
                </div>
              </div>
              <div class="past_upload_file_process_but">
                <div id="past_upload_file_process_but_component">
                </div>
              </div>
              <div class="past_upload_file_del_but">
                <div id="past_upload_file_del_but_component">
                </div>
              </div>
            </div>
            <div class="file_upload">
              <div id="file_upload_component">
              </div>
            </div>
            <div class="doc">
              <div id="doc_component">
              </div>
            </div>
            <div class="data_list">
              <div id="data_list_component"></div>
            </div>
          </div>
          <div class="right_splitter">
            <div class="view_data_type_combo">
              <div id="view_data_type_combo_component">
              </div>
            </div>
            <div class="view_data_list">
              <div id="view_data_list_component">
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div id="chatbot_del_confirm_window_component">      
      <div>확인</div>
      <div>
        <div style="margin-top:10px;margin-left:10px;">
          정말로 모든 대화를 삭제하시겠습니까?
        </div>
        <div>
          <input type="button" id="chatbot_del_confirm_window_component_ok" value="OK" style="margin-right:20px;margin-top:20px;margin-left:30px;" />
          <input type="button" id="chatbot_del_confirm_window_component_cancel" value="Cancel" />
        </div>
      </div>
    </div>
    <div class="chat">
      <div id="chat_component" style="padding:5px;">
        <div class="chat_list">
        </div>
        <div class="chatbot_input">
          <input type="text" id="chatbot_input_component"/>
        </div>
      </div>
    </div>
  </body>
</html>