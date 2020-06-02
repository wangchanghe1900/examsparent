layui.use(['form','layer','upload'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var upload = layui.upload;

    upload.render({
        elem: '#fileupload'
        ,url: 'videoResourceFileUpload' //改成您自己的上传接口
        ,auto: false
        ,accept:'video'
/*        ,exts: 'PDF|pdf'*/
        ,acceptMime:'video/*'
        ,field:'fileinfo'
        ,size: 1024*200
        ,data: {
            resourceName: function(){
                return $(".resourcename").val();
            },
            remark : function(){
               return $(".remark").val();
            }
        }
        ,bindAction: '#btnupload'
        ,choose: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
           //layer.load(); //上传loading
            obj.preview(function(index, file, result){
                //console.log(result);
                $('#videotmp').attr('src', result); //图片链接（base64）
            });

        }
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
            console.log(upload);
            layer.closeAll('loading'); //关闭loading
            layer.msg("视频上传失败");
        }
    });
    form.verify({
        notNull : function(value, item){
           var res=$(".resourcename").val();
           if(res==undefined){
               return "资源名称不能为空";
           }
        }
    });


})