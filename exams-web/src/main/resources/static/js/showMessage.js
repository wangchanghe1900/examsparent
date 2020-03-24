layui.use(['form','layer','tree'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery ;
    var tree = layui.tree;
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var webpath=localhostPaht + projectName;
    function initInfo() {
       var messageInfo= sessionStorage.getItem("messageInfo");
       if(messageInfo!=null){
           sessionStorage.removeItem("messageInfo");
           messageInfo=JSON.parse(messageInfo);
           $(".title").html(messageInfo.notice.title);
           var info="发布人:"+messageInfo.sendUser;
           var date=" 发布日期:"+messageInfo.createDate;
           $("#box .authorinfo").html(info);
           $("#box .createTime").html(date);
           $(".content .detail").html(messageInfo.notice.content);
       }
    }
    initInfo();

})