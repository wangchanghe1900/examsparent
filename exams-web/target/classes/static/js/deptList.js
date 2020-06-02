layui.use(['form','layer','table','laytpl','tree', 'util'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;
    var tree = layui.tree
        ,util = layui.util;
    //提取网页根目录
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var webpath=localhostPaht + projectName;
    //显示页面按钮
    $.get(webpath+"/sysmenu/btnAuthroInfo",{"btn":"dept"},
        function (data) {
            if(data){
                if(data.isAdd){
                    $('div:nth-child(1)').removeClass("disp");
                }
                if(data.isDel){
                    $('div:nth-child(2)').removeClass("disp");
                }
                if(data.isEdit){
                    $('.edit').removeClass("disp");
                }
            }
        });
    function getData(){
        var data = [];
        $.ajax({
            url: webpath+"/dept/getAllDeptInfo",    //后台数据请求地址
            type: "get",
            async:false,
            success: function(resut){
                data = resut;
            }
        });
        return data;
    };

    tree.render({
        elem: '#dept_tree'
        ,data: getData()
        ,id: 'depttree'
        ,isJump: false //是否允许点击节点时弹出新窗口跳转
        ,click: function(obj){
            var data = obj.data;
            $(".parentId").val(data.parentId);
            $(".id").val(data.id);
            $(".parentName").val(data.parentName);
            var title=data.title;
            title.indexOf("(")==-1?$(".name").val(title):$(".name").val(title.substring(0,title.indexOf("(")));

            $(".content").val(data.content);
            $(".ordernum").val(data.orderNum);
            form.render('select');
        }
    });
    form.on("submit(editDept)",function(data){
        //弹出loading
        if(data.field.id==""){
            layer.msg("请选择要编辑的部门");
        }else{
            var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
            $.post("editDept", data.field
                ,function (result) {
                    if(result.code==200){
                        top.layer.close(index);
                        layer.msg(result.msg);
                        tree.reload('depttree', {data: getData()});
                    }else{
                        layer.msg(result.msg);
                    }
                });
        };

        return false;
    });

    form.on("submit(delDept)",function(data){
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        layer.confirm('确定删除此部门？',{icon:3, title:'提示信息'},function(index) {
            $.post("delDept", data.field
                , function (result) {
                    if (result.code == 200) {
                        top.layer.close(index);
                        layer.msg(result.msg);
                        tree.reload('depttree', {data: getData()});
                    } else {
                        layer.msg(result.msg);
                    }
                });
          });
        return false;
    });

    form.on("submit(addDept)",function(data){
        layui.layer.open({
            title: "增加部门",
            type: 2,
            area: ['700px', '450px'],
            fixed: false, //不固定
            maxmin: true,
            content: "addDeptList"
        });
    });
})
