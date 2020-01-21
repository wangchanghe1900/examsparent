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
            $(".parentName").val(data.parentId);
            $(".name").val(data.title);
            $(".content").val(data.content);
            $(".ordernum").val(data.orderNum);
        }
    });

})
