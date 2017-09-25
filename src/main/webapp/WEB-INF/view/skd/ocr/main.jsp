<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>KOKODOG[OCR Analysis]</title>
    <link rel="shortcut icon" type="image/x-icon" href="/FileDown?file_key=tId6m0peoFkF2GYcAfAJG48kOE5Djo5ky49MFtnX" />
    <link rel="stylesheet" href="/css/skd/ocr/main.css"/>
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
      var onLoadImg = false;
      $(document).ready(function() {
        contentInitLoad();
        contentEventLoad();
        init();
        GetPastFileUploadList();
      });
      var checkImgRenderer = function (row, column, value) {
        var chkYn = $("div#view_prod_chk_list_component").jqxGrid("getrowdata", row).chk_yn;
        var imgUrl = null;
        if (chkYn == "Y") {
          imgUrl = "/FileDown?file_key=1XA2nZGruEcuxsFDIxpSxttofROJMNF53XCRX4Ew";
        } else {
          imgUrl = "/FileDown?file_key=rWxcM1PT9ElCFHwTOSuByQ09LfOg1VPtRoUkrv4K";
        }
        return "<div style=\"width:100%;height:100%;text-align:center;\"><img style=\"width:20px;height:20px;margin-top:2px\" src=\"" + imgUrl + "\"></div>";
      }        
      
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
          uploadUrl: "/skd/ocr/main/FileUpload",
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
        $("div#view_data_list_component").jqxGrid({
          width: "100%",
          height: "100%",
          columnsheight: 25,
          columnsresize: true,
          selectionmode: "singlerow",
          editable: false,
          enabletooltips: true,
          filterable: false,
          sortable: true,
          filtermode: "default",
          columns: [
            {text: "No", datafield: "no", width: 50, cellsalign: "right"},
            {text: "hier", datafield: "hier", width: 200, cellsalign: "left"},
            {text: "좌표", datafield: "cord", cellsalign: "left", width: 200},
            {text: "문구", datafield: "text", cellsalign: "left"}
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
        $("div#past_upload_file_del_but_component").click(event_div_past_upload_file_del_but_component);
        $("div#past_upload_file_process_but_component").click(event_div_past_upload_file_process_but_component);
        $("div#view_data_list_component").on("rowclick", event_div_view_data_list_component_rowclick);
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
        updateImgInfo(response.file_key);
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
        if ($("div#past_upload_file_del_but_component").jqxButton("disabled") == false) {
          cmnSyncCall("DeletePastFileUpload", {file_key: $("div#past_upload_file_list_component").jqxDropDownList("val")}, callback, $("div#past_upload_file_list_component").jqxDropDownList("val"));
        }
      }
      
      function event_div_past_upload_file_process_but_component() {
        if ($("div#past_upload_file_process_but_component").jqxButton("disabled") == false) {
          fileKey = $("div#past_upload_file_list_component").jqxDropDownList("val");
          updateImgInfo(fileKey);
        }
      }
      
      function event_div_view_data_list_component_rowclick(event) {
        if (onLoadImg == false) {
          $("div#img_component").children("div").remove();
          var cord = $("div#view_data_list_component").jqxGrid("getrowdata", event.args.rowindex).cord_nat;
          var leftCover = $("<div>", {css: {opacity: 0.7, backgroundColor: "#000000", position: "absolute", zIndex: 1, left: "0px", top: "0px", bottom: "0px"
                                            , width: (cord[0] / $("img#upload_img")[0].naturalWidth * 100).toString() + "%"}});
          var rightCover = $("<div>", {css: {opacity: 0.7, backgroundColor: "#000000", position: "absolute", zIndex: 1, right: "0px", top: "0px", bottom: "0px"
                                            , left: ((cord[0] + cord[2]) / $("img#upload_img")[0].naturalWidth * 100).toString() + "%"}});
          var topCover = $("<div>", {css: {opacity: 0.7, backgroundColor: "#000000", position: "absolute", zIndex: 1, left: (cord[0] / $("img#upload_img")[0].naturalWidth * 100).toString() + "%"
                                           , top: "0px", height: (cord[1] / $("img#upload_img")[0].naturalHeight * 100).toString() + "%"
                                           , width: (cord[2] / $("img#upload_img")[0].naturalWidth * 100).toString() + "%"}});
          var bottomCover = $("<div>", {css: {opacity: 0.7, backgroundColor: "#000000", position: "absolute", zIndex: 1, left: (cord[0] / $("img#upload_img")[0].naturalWidth * 100).toString() + "%"
                                           , top: ((cord[1] + cord[3]) / $("img#upload_img")[0].naturalHeight * 100).toString() + "%"
                                           , bottom: "0px", width: (cord[2] / $("img#upload_img")[0].naturalWidth * 100).toString() + "%"}});
          $("div#img_component").append(leftCover);
          $("div#img_component").append(rightCover);
          $("div#img_component").append(topCover);
          $("div#img_component").append(bottomCover);
          $("div#img_component").children("div").click(img_upload_click);
        }
      }
      
      function callback(data, act, input_param, callbackVar) {
        if (act == "GetOcr") {
          addOcrListToGrid(data);
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
      
      function GetPastFileUploadList() {
        cmnSyncCall("GetPastFileUploadList", {}, callback, null);
      }
      
      function init() {
      }
      
      function updateImgInfo(file_key) {
        fileKey = file_key;
        onLoadImg = false;
        if ($("div#img_component").children("img").length >= 1) {
          $("div#img_component").children("img").remove();
          $("div#img_component").children("div").remove();
        }
        var tmpImg = $("<img>", {css: {maxWidth: "100%", height: "auto"}}).attr("src", "/FileDown?file_key=" + fileKey).attr("id", "upload_img");  
        $("div#img_component").append(tmpImg);
        $("img#upload_img").on("load", img_upload_onload);
        $("img#upload_img").click(img_upload_click);
        cmnSyncCall("GetOcr", {file_key: fileKey}, callback, null);
      }
      
      function img_upload_onload() {
        onLoadImg = false;
      }
      
      function img_upload_click() {
        $("div#img_component").children("div").remove();
        $("div#view_data_list_component").jqxGrid("unselectrow", $("div#view_data_list_component").jqxGrid("selectedrowindex"));
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
      
      function addOcrListToGrid(data) {
        var i = 0;
        var sourceDataList = [];
        for (i = 0; i < data.length; i++) {
          sourceDataList.push({no: i + 1, hier: data[i].hier, text: data[i].text, cord: "[" + data[i].cord[0] + ", " + data[i].cord[1] + ", "
                               + data[i].cord[2] + ", " + data[i].cord[3] + "]", cord_nat: data[i].cord});
        }
        $("div#view_data_list_component").jqxGrid("clear");
        $("div#view_data_list_component").jqxGrid("addrow", null, sourceDataList);   
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
            <div class="img">
              <div id="img_component">
              </div>
            </div>
            <div class="data_list">
              <div id="data_list_component"></div>
            </div>
          </div>
          <div class="right_splitter">
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