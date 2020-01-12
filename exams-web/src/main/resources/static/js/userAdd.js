layui.use(['form','layer'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var deptId=window.sessionStorage.getItem("deptId");
    //console.log(deptname);
    $.ajax({
        url: 'dept/deptnamelist',
        dataType: 'json',
        data:{'delFlag': 0},	//查询状态为正常的所有机构类型
        type: 'post',
        success: function (data) {
            $.each(data, function (index, item) {
                $('#deptname').append(new Option(item.deptname, item.id));// 下拉菜单里添加元素
                $('#deptname').val(deptId);
            });
            layui.form.render("select");
        }
    });
    form.on("submit(addUser)",function(data){
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
        $.post("addUser",{
            id :  $(".id").val(),
            username : $(".username").val(),  //登录名
            realname : $(".realname").val(),  //登录名
            email : $(".email").val(),  //邮箱
            mobile : $(".mobile").val(),  //电话
            //userGrade : data.field.userGrade,  //会员等级
            deptId : data.field.deptname,
            status : data.field.status   //用户状态
            //newsTime : submitTime,    //添加时间
            //userDesc : $(".userDesc").text(),    //用户简介
        },function(res){
            if(res.code!=200){
                layer.msg(res.msg);

            }else{
                setTimeout(function(){
                    top.layer.close(index);
                    top.layer.msg("用户添加(更新)成功！");
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                },1000);
            }
        });

        return false;
    });

    //格式化时间
    function filterTime(val){
        if(val < 10){
            return "0" + val;
        }else{
            return val;
        }
    }
    //定时发布
    var time = new Date();
    var submitTime = time.getFullYear()+'-'+filterTime(time.getMonth()+1)+'-'+filterTime(time.getDate())+' '+filterTime(time.getHours())+':'+filterTime(time.getMinutes())+':'+filterTime(time.getSeconds());

})