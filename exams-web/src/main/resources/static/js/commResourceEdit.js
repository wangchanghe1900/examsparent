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
    var webpath = localhostPaht + projectName;
    form.on("submit(updateResource)",function(data){
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
        $.post(webpath+"/resource/saveCommResource", {
                'id': data.field.id,
                'resourceName' : data.field.resourceName,
                'remark' :data.field.remark
            },function(res){
                if(res.code!=200){
                    layer.msg(res.msg);
                }else{
                    setTimeout(function(){
                        top.layer.close(index);
                        top.layer.msg(res.msg);
                        layer.closeAll("iframe");
                        //刷新父页面
                        parent.location.reload();
                    },1000);
                }
            });
        return false;
    });
    $(".download").click(function () {
        $.post(webpath+"/resource/downloadResource", {
            'filePath': $(".filepath").html(),
            'fileName': $(".resourcename").val()
        },function(res){
            layer.msg("下载成功");
        });
    });
/*    form.on("submit(downloadResource)",function(data){
        $.post(webpath+"/resource/downloadResource", {
            'filePath': $(".filepath").html(),
            'fileName': $(".resourcename").val()
        },function(res){
           layer.msg("下载成功");
        });
        //return false;
    });*/
})