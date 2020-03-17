layui.use(['form','layer','tree', 'util'],function(){
    var form = layui.form,
        layer =  layui.layer,
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
        $.ajax({
            url: webpath+"/resource/getDeptResourceInfo",    //后台数据请求地址
            type: "get",
            async:false,
            success: function(resut){
                data = resut;
            }
        });
        return data;
       };

    tree.render({
        elem: '#resource'
        ,data: getData()
        ,showCheckbox: false  //是否显示复选框
        ,id: 'treeId2'
        ,accordion:true
        ,isJump: false //是否允许点击节点时弹出新窗口跳转
        ,click: function(obj){
            var data = obj.data;  //获取当前点击的节点数据
            if(data.id!=null){
                layer.msg("已选："+data.title);
                //parent.layui.$('.questionName').val('我被改变了');
                window.sessionStorage.setItem("resourceName",data.title);
                window.sessionStorage.setItem("resId",data.id);
            }else{
                layer.msg("请选择正确的学习资源",{icon: 6});
            }

        }
    });
    //tree.setChecked('treeId1', getRolePremiss());

/*
    form.on("submit(resselect)",function(data){
        //弹出loading
        //var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
        //console.log(checkStr);
        //top.layer.close(index);
        var index = parent.layer.getFrameIndex(window.name);
        parent.location.reload();
        //layer.closeAll("iframe");
        parent.layer.close(index);
        //return false;
    });*/
/*  $("#btn_resource").on('click', function(){
      var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
      parent.layer.close(index);
      //return false;
  });*/

})