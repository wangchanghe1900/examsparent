layui.use(['form','layer','upload'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var upload = layui.upload;
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var webpath=localhostPaht + projectName;
    var url=window.sessionStorage.getItem("pdfUrl");
    //var resourceWeb="http://192.168.1.5:8789";
    //var resourceWeb="http://20.1.198.11:8786";
    PDFObject.embed(webpath+"/upload"+url, "#dpflist",{height: "450px"});
    //PDFObject.embed(resourceWeb+url, "#dpflist",{height: "450px"});



})