<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>KOKODOG[Proposal Analysis]</title>
    <link rel="shortcut icon" type="image/x-icon" href="/FileDown?file_key=tId6m0peoFkF2GYcAfAJG48kOE5Djo5ky49MFtnX" />
    <link rel="stylesheet" href="/css/skd/ppa/main.css"/>
    <link rel="stylesheet" href="https://jqwidgets.com/public/jqwidgets/styles/jqx.base.css" type="text/css"/>
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
    <script type="text/javascript" src="https://jqwidgets.com/public/jqwidgets/jqx-all.js"></script>
    <script src="/js/cmn.js"></script>
    <script type="text/javascript">
      var fileKey = null;
      $(document).ready(function() {
        contentInitLoad();
        contentEventLoad();
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
        $("input#past_upload_file_del_but_component").jqxButton({
          width: "100%",
          height: "100%",
          value: "삭제",
          disabled: true
        });
        $("input#past_upload_file_process_but_component").jqxButton({
          width: "100%",
          height: "100%",
          value: "조회",
          disabled: true
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
        $("div#past_upload_file_del_but_component").click(event_div_past_upload_file_del_but_omponent);
        $("div#past_upload_file_process_but_component").click(event_div_past_upload_file_process_but_component);
      }
      
      function event_a_menu_scen_manage_click() {
        
      }
      
      function event_a_menu_exit_click() {
        
      }
      
      function event_a_help_help_click() {
        
      }
      
      function event_a_menu_help_about_click() {
        
      }
      
      function event_div_file_upload_component_upload_end(event) {
        var response = JSON.parse($(event.args.response).html());
        fileKey = response.file_key;
        cmnSyncCall("ConvToHtml", {file_key : fileKey}, callback, null);
      }
      
      function event_div_past_upload_file_list_component(event) {
        if (event.args.index >= 0) {
          $("#past_upload_file_process_but_component").jqxButton("disabled", false);
          $("#past_upload_file_del_but_component").jqxButton("disabled", false);
        } else {
          $("#past_upload_file_process_but_component").jqxButton("disabled", true);
          $("#past_upload_file_del_but_component").jqxButton("disabled", true);          
        }
      }
      
      function event_div_past_upload_file_del_but_omponent() {
        cmnSyncCall("DeletePastFileUpload", {file_key: $("div#past_upload_file_list_component").jqxDropDownList("val")}, callback, $("div#past_upload_file_list_component").jqxDropDownList("val"));
      }
      
      function event_div_past_upload_file_process_but_component() {
        fileKey = $("div#past_upload_file_list_component").jqxDropDownList("val");
        cmnSyncCall("ConvToHtml", {file_key : fileKey}, callback, null);
      }
      
      function GetPastFileUploadList() {
        cmnSyncCall("GetPastFileUploadList", {}, callback, null);
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
        }
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
      <div class="content">
        <div id="left_right_splitter_component">
          <div class="left_splitter">
            <div class="past_upload_file">
              <div class="past_upload_file_list">
                <div id="past_upload_file_list_component">
                </div>
              </div>
              <div class="past_upload_file_process_but">
                <input type="button "id="past_upload_file_process_but_component"/>
              </div>
              <div class="past_upload_file_del_but">
                <input type="button" id="past_upload_file_del_but_component"/>
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
  </body>
</html>