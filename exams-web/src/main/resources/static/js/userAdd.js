layui.config({
    base : "js/"
}).extend({
    treeSelect: 'treeSelect'
})
layui.use(['form','layer','treeSelect'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var formSelects = layui.formSelects;
    var treeSelect= layui.treeSelect;

/*    $.ajax({
        url: 'dept/deptnamelist',
        dataType: 'json',
        data:{'delFlag': 0},	//查询状态为正常的所有机构类型
        type: 'post',
        success: function (data) {
            var deptId=window.sessionStorage.getItem("deptId");
            $.each(data, function (index, item) {
                $('#deptname').append(new Option(item.deptname, item.id));// 下拉菜单里添加元素
                if(item.id==deptId){
                    $('#deptname').val(deptId);
                }

            });
            layui.form.render("select");
        }
    });*/
    //添加验证规则

    var deptId;
    treeSelect.render({
        // 选择器
        elem: '#depttree',
        // 数据
        data: 'dept/getDeptAllInfo',
        // 异步加载方式：get/post，默认get
        type: 'get',
        // 占位符
        placeholder: '请选择所属部门',
        // 是否开启搜索功能：true/false，默认false
        search: true,
        style: {
            folder: {
                enable: false
            },
            line: {
                enable: true
            }
        },
        // 点击回调
        click: function(d){
            deptId= d.current.id;
        },
        // 加载完成后的回调函数
        success: function (d) {
            var deptId=window.sessionStorage.getItem("deptId");
            treeSelect.checkNode('depttree', deptId);
            //                选中节点，根据id筛选
            //                treeSelect.checkNode('tree', 3);

            //                获取zTree对象，可以调用zTree方法
            //                var treeObj = treeSelect.zTree('tree');
            //                console.log(treeObj);

            //                刷新树结构
            //                treeSelect.refresh();
        }
    });
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