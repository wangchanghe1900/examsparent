layui.use(['form','layer','tree', 'util'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var tree = layui.tree
        ,util = layui.util;
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var webpath = localhostPaht + projectName;
    function getData(){
        var data = [];
        var roleId=sessionStorage.getItem("roleId");
        $.ajax({
            url: webpath+"/sysmenu/getAllMenuInfo/"+roleId,    //后台数据请求地址
            type: "get",
            async:false,
            success: function(resut){
                data = resut;
            }
        });
        return data;
       };

/*    function getRolePremiss(){
        var data = [];
        var roleId= sessionStorage.getItem("roleId");
        $.ajax({
            url: webpath+"/role/getRolePermiss/"+roleId,    //后台数据请求地址
            type: "post",
            async:false,
            success: function(resut){
                if(resut!=null){
                    data = resut;
                }
            }
        });
        return data;
    };*/

    tree.render({
        elem: '#permiss'
        ,data: getData()
        ,showCheckbox: true  //是否显示复选框
        ,id: 'treeId1'
        ,isJump: false //是否允许点击节点时弹出新窗口跳转
        ,click: function(obj){
            var data = obj.data;  //获取当前点击的节点数据
            layer.msg('状态：'+ obj.state + '<br>节点数据：' + JSON.stringify(data));
        }
    });
    //tree.setChecked('treeId1', getRolePremiss());


    form.on("submit(savePermiss)",function(data){
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
        $.post("addRole",{
            id :  $(".id").val(),
            name : $(".rolename").val(),  //角色名称
            remark : $(".remark").val(),    //角色描述
            isenable : $(".roleStatus input[name='isenable']:checked").val()  //$(".roleStatus input[checked]").val()
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