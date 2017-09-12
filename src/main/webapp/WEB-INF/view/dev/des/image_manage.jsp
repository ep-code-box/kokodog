 <%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>이미지 관리</title>
    <link rel="shortcut icon" type="image/x-icon" href="/FileDown?file_key=tId6m0peoFkF2GYcAfAJG48kOE5Djo5ky49MFtnX"/>
    <link rel="stylesheet" href="/css/dev/des/image_manage.css"/>
    <link rel="stylesheet" href="/js/jqwidgets/styles/jqx.base.css" type="text/css"/>
    <link href="https://rawgithub.com/hayageek/jquery-upload-file/master/css/uploadfile.css" rel="stylesheet">
    <script type="text/javascript" src="/js/jquery/jquery-3.2.1.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqx-all.js"></script>
    <script type="text/javascript" src="/js/jqwidgets/jqxwindow.js"></script>
    <script type="text/javascript" src="https://rawgithub.com/hayageek/jquery-upload-file/master/js/jquery.uploadfile.min.js"></script>
    <script type="text/javascript" src="/js/cmn.js"></script>
    <script type="text/javascript">
      /* Document 로드 시에 발생하는 시작 함수 */
      $(document).ready(function() {
        contentInitLoad();
        contentEventLoad();
        GetImgList(1);
      });
      
      /* jqWidget을 사용하는 각종 함수 들 첫 오픈 처리 */
      function contentInitLoad() {
        $("div#top_menu_bar_component").jqxMenu({
          width: "100%",
          height: "100%"
        });
        $("div#top_menu_icon_component").jqxToolBar({
          tools: "custom custom",
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
        $("div#save_config_window").jqxWindow({
          position: "center",
          showCloseButton: true,
          resizable: false,
          isModal: true,
          modalOpacity: 0.3,
          draggable: true,
          autoOpen: false,
          width: "500px",
          height: "350px"
        });
        $("input#save_config_img_name_txt_component").jqxInput({
          placeHolder: "Image Name",
          height: "100%",
          width: "100%"          
        });
        $("input#save_config_img_memo_txt_component").jqxInput({
          placeHolder: "Image Memo",
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
        var uploaderObj = $("div#mulitplefileuploader_component").uploadFile({
          url: "/dev/des/image_manage/ImageUpload",
          returnType: "json",
          method: "POST",
          fileName: "myfile",
          dynamicFormData: function() {
            return {img_name: $("input#save_config_img_name_txt_component").val(), img_memo: $("input#save_config_img_memo_txt_component").val()};
          },
          multiple: false,
          autoSubmit: true,
          showStatusAfterSuccess: false,
          showError: false,
          maxFileCount: 1,
          onSubmit: function(files) {
            if ($("input#save_config_img_name_txt_component").val() == "") {
              alert("이름을 입력하세요.");
              uploaderObj.reset();
              return false;
            }
            if ((new RegExp("\.(bmp|gif|jpg|jpeg|png)$", "i")).test(files) == false) {
              alert("파일 확장자가 이미지 전용 확장자가 아닙니다.");
              uploaderObj.reset();
              return false;
            }
          },
          onSuccess: function(files, data, xhr, pd) {
            alert("정상적으로 저장되었습니다.");
            $("div#save_config_window").jqxWindow("close");
            $("input#save_config_img_name_txt_component").val("");
            $("input#save_config_img_memo_txt_component").val("");
            uploaderObj.reset();
          },
          onError: function(files, status, errMsg) {
            alert("오류가 발생하였습니다.");
            uploaderObj.reset();
          }
        });
      }

      /* jqWidget을 사용하는 각종 이벤트들 맵핑 처리 */
      function contentEventLoad() {
        $("a#menu_new").click(event_a_menu_new_click);
        $("a#menu_save").click(event_a_menu_modify_click);
        $("div#data_list_component").on("select", event_data_list_component_select);
        $("input#search_text_component").keyup(event_search_text_component_keyup);
        $("input#save_config_dbio_ok_but_component").on("click", event_save_config_dbio_ok_but_component_click);
        $("input#save_config_dbio_cancel_but_component").on("click", event_save_config_dbio_cancel_but_component_click);
      }
      
      /* jsource_data_tab_component(메뉴 상단 아이콘 버튼) 신규 맵핑 처리 */
      function init_top_menu_icon_component_init_tools(type, index, tool, menuToolIninitialization) {
        switch (index) {
          case 0:
            var button = $("<div>" + "<img src='/FileDown?file_key=0xAoKZ11FQB3HyZH0bIpCaaphbKcyS3uXYn0G5rn' title='New...' style='width:20px;height:20px;'/>" + "</div>");
            tool.append(button);
            button.jqxButton({
              width: "20px",
              height: "20px",
            });
            button.on("click", event_a_menu_new_click);
            break;
          case 1:
            var button = $("<div>" + "<img src='/FileDown?file_key=GgnS17SPV1IuXIfUZGWXfykXCbOMyeB2S2AGKtWD' title='Save...' style='width:20px;height:20px;'/>" + "</div>");
            tool.append(button);
            button.jqxButton({
              width: "20px",
              height: "20px",
            });
            button.on("click", event_a_menu_modify_click);
            break;
           default:
            break;
        }
      }

      /* New Icon을 클릭하거나 혹은 File - New 클릭 시 새 창 오픈해주는 함수 */
      function event_a_menu_new_click(e) {
        $("div#save_config_window").jqxWindow("open");
      }

      /* Save Icon을 클릭하거나 혹은 File - Save 클릭 시 새 창 오픈해주는 함수 */
      function event_a_menu_modify_click(e) {
      }
      
      function event_data_list_component_select(event) {
        cmnSyncCall("GetImgInfo", {img_num: event.args.item.value}, callback, null);
      }
      
      function event_search_text_component_keyup() {
      }
      
      function event_save_config_dbio_ok_but_component_click() {
      }
      
      function event_save_config_dbio_cancel_but_component_click() {
        $("div#save_config_window").jqxWindow("close");
        $("input#save_config_dbio_name_txt_component").val("");
      }
      
      function callback(data, act, input_param, callbackVar) {
        if (act == "GetImgList") {
          var selectedIndex = $("div#data_list_component").jqxListBox("clearSelection");
          $("div#data_list_component").jqxListBox("clear");
          for (var i = 0; i < data.length; i++) {
            $("div#data_list_component").jqxListBox("addItem", {label: data[i].img_name, value: data[i].img_num});
          }
        } else if (act == "GetImgInfo") {
          $("#img_source_data_component").attr("src", "/FileDown?file_key=" + data.file_key);
          setImgLocation();
        }
      }
      
      function GetImgList(page) {
        cmnSyncCall("GetImgList", {page: page, search_txt: $("input#search_text_component").val()}, callback, null);
      }
      
      function setImgLocation() {
        $("#img_source_data_component").css("position", "absolute").css("top", "50%").css("left", "50%")
          .css("margin-left", "" + ($("#img_source_data_component").width() / 2 * -1) + "px")
          .css("margin-top", "" + ($("#img_source_data_component").height() / 2 * -1) + "px")
          .css("max-width", "100%")
          .css("max-height", "100%");
      }
    </script>
  </head>
  <body>
    <div class="body_sub">
      <div class="top_main_grid">
        <div class="top_main_banner">
          <a class="img_top_main_banner" href="#"><img class="img_top_main_banner" src="/FileDown?file_key=Ahf9stzMI3vGxDP3iAi9pZHQvcnr8sHaUrWo372A"/></a>
        </div>
      </div>
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
                  <a id="menu_save">Modify</a>
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
            <div class="source_data">
              <div id="source_data_component">
                <img id="img_source_data_component"/>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div id="save_config_window" class="save_config_window">
      <div class="save_config_window_header">
        이미지 저장
      </div>
      <div style="overflow: hidden;" id="windowContent">
        <div class="save_config_img_name_txt">
          <input id="save_config_img_name_txt_component"/>
        </div>
        <div class="save_config_img_memo_txt">
          <input id="save_config_img_memo_txt_component"/>
        </div>
        <div class="save_config_dbio_ok_but">
          <input id="save_config_dbio_ok_but_component" value="Save"/>
        </div>
        <div class="save_config_dbio_cancel_but">
          <input id="save_config_dbio_cancel_but_component" value="Cancel"/>
        </div>
        <div class="mulitplefileuploader">
          <div id="mulitplefileuploader_component">
            Upload
          </div>
        </div>
      </div>
    </div>
  </body>
</html>