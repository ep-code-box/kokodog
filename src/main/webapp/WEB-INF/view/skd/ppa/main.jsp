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
        $("div#view_data_type_combo_component").on("change", event_div_view_dat_type_combo_component_change);
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
        }
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
            sourceDataList.push({no: i + 1, vocabulary: morphemeData[i].voca, part_of_speach_tag: morphemeData[i].type[0], meaning: morphemeData[i].type[1]
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
  </body>
</html>