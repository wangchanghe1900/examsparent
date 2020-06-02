layui.use(['form','layer','upload'],function(){
    var form = layui.form,
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
    upload.render({
        elem: '#fileupload'
        ,url: 'saveCommResource' //改成您自己的上传接口
        ,auto: false
        ,accept:'file'
        ,exts: 'PDF|pdf'
        ,acceptMime:'application/pdf'
        ,field:'fileinfo'
        ,size: 1024*100
        ,data: {
            id:function(){
                return $(".id").val();
            },
            resourceName: function(){
                return $(".resourcename").val();
            },
            filePath:function(){
                return $(".url").val();
            },
            remark : function(){
                return $(".remark").val();
            }

        }
        ,bindAction: '#btnupdate'
        ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
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
            layer.closeAll('loading'); //关闭loading
            layer.msg("上传文件失败");
        }
    });
})