layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;
    var username=window.sessionStorage.getItem("username");
    let url=window.document.location.href;
    if(username==undefined){
        window.location.href=url.substring(0,url.lastIndexOf("/"));
    }else{
        $(".userName").val(username);
    }
    //添加验证规则
    form.verify({
        oldPwd : function(value, item){
/*            $.post("verifyPwd",{
                    username : $(".userName").val(),  //登录名
                    password : $("#oldPwd").val()
          },function (data) {
              if(data!='success'){
                return "密码错误，请重新输入！";
              }
            });*/
        },
        newPwd : function(value, item){
            if(value.length < 8){
                return "密码长度不能小于8位";
            }
        },
        confirmPwd : function(value, item){
            var pass=$("#newPwd").val();
            if(pass!=value){
                return "两次输入密码不一致，请重新输入！";
            }
/*            if(!new RegExp($("#newPwd").val()).test(value)){
                return "两次输入密码不一致，请重新输入！";
            }*/
        }
    });

    //控制表格编辑时文本的位置【跟随渲染时的位置】
    $("body").on("click",".layui-table-body.layui-table-main tbody tr td",function(){
        $(this).find(".layui-table-edit").addClass("layui-"+$(this).attr("align"));
    });
    form.on("submit(changePwd)",function(data){
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        $.post("updatePwd",{
            username :  $(".userName").val(),  //登录名
            oldPwd: $("#oldPwd").val(),
            newPass: $("#newPwd").val()
        },function(res){
            if(res.code!=200){
                top.layer.close(index);
                layer.msg(res.msg);
            }else{
                setTimeout(function(){
                    top.layer.close(index);
                    top.layer.msg("密码修改成功！");

                },1000);
            }
        });

        return false;
    });
})