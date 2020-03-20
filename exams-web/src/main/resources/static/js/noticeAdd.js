layui.config({
    base : "js/"
}).extend({
    treeSelect: 'treeSelect'
})
layui.use(['form','layer','layedit','treeSelect'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
            layedit = layui.layedit;
    var formSelects = layui.formSelects;
    var treeSelect= layui.treeSelect;

    var editIndex = layedit.build('content');

    form.verify({
        notNull : function(value, item){
            if(deptId==null){
                return "请选择所属部门";
            }
        }
    });
    layui.formSelects.config('role', {
        searchUrl: 'role/getRoleInfo',
        success: function(id, url, searchVal, result){
            var roles=window.sessionStorage.getItem("roles");
            var s=JSON.parse(roles);
            formSelects.value('role', s);
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
            deptId : deptId,
            status : data.field.status,    //用户状态
            roles : layui.formSelects.value('role', 'valStr')
            //userDesc : $(".userDesc").text(),    //用户简介
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

})