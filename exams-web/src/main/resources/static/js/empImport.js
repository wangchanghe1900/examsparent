layui.use(['form','layer','upload','jquery'],function(){
    var form = layui.form,
        layer =  layui.layer,
        $ = layui.$;
    var upload = layui.upload;

    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var webpath=localhostPaht + projectName;

    upload.render({
        elem: '#excelfiles'
        ,url: 'importEmpExcel' //改成您自己的上传接口
        ,auto: false
        ,accept:'file'
        ,exts: 'xls|xlsx'
        ,acceptMime:'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        ,field:'excelinfo'
        ,size: 1024*50
        ,data: {}
        ,bindAction: '#empUpload'
        ,before:function (obj) {
            layer.load(); //上传loading
        }
        ,done: function(res, index, upload){
            if(res.code==0){
                layer.closeAll('loading'); //关闭loading
                setTimeout(function(){
                    top.layer.msg(res.msg);
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                },1000);
            }else{
                layer.closeAll('loading');
                layer.msg(res.msg);

            };

        }
        ,error: function(index, upload){
            //console.log(upload);
            layer.closeAll('loading'); //关闭loading
            layer.msg("信息导入失败!");
        }
    });


})