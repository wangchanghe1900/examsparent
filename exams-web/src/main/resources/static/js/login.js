layui.use(['form','layer','jquery'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer
        $ = layui.jquery;


    //登录按钮
    form.on("submit()",function(data){
        let btn=$(this);
        $(this).text("登录中...").attr("disabled","disabled").addClass("layui-disabled");
        let curWwwPath=window.document.location.href;
        //let userpageurl="http://127.0.0.1:9001/v1/getUserByName"
        let userInfo = data.field;
        let url = curWwwPath+"login";
        setTimeout(function(){
            $.ajax({
                url:url,
                type:'post',
                data:userInfo,
                success:function(res){
                    if(res.code==200){
                        window.sessionStorage.setItem("username",data.field.username);
                        //console.log(res.data("id"));
                        let redirectUrl=curWwwPath+"index?userName="+data.field.username+"&nowTime="+$.now();
                        window.location.href =redirectUrl;//curWwwPath+"index?userName="+data.field.userName+"&nowTime="+$.now();
                    }else{
                        console.log(res);
                        layer.msg(res.msg);
                        btn.text("登录").removeAttr("disabled").removeClass("layui-disabled").addClass("layui-btn");
                    }
                },
                error:function (err) {
                    //alert(err);
                    window.location.href =curWwwPath;
                }
            });
        },1000);

        return false;
    });

    //表单输入效果
    $(".loginBody .input-item").click(function(e){
        e.stopPropagation();
        $(this).addClass("layui-input-focus").find(".layui-input").focus();
    })
    $(".loginBody .layui-form-item .layui-input").focus(function(){
        $(this).parent().addClass("layui-input-focus");
    })
    $(".loginBody .layui-form-item .layui-input").blur(function(){
        $(this).parent().removeClass("layui-input-focus");
        if($(this).val() != ''){
            $(this).parent().addClass("layui-input-active");
        }else{
            $(this).parent().removeClass("layui-input-active");
        }
    })
})
