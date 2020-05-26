layui.config({
    base : "../js/"
}).extend({
    treeSelect: 'treeSelect'
})
layui.use(['form','layer','treeSelect', 'util'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var treeSelect= layui.treeSelect;
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var webpath = localhostPaht + projectName;

    var parentId;
    treeSelect.render({
        // 选择器
        elem: '#tree2',
        // 数据
        data: 'getDeptAllInfo',
        // 异步加载方式：get/post，默认get
        type: 'get',
        // 占位符
        placeholder: '选择上级部门',
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
           parentId= d.current.id;
        },
        // 加载完成后的回调函数
        success: function (d) {
            //console.log(d);
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
            if(parentId==null){
                return "请选择上级部门";
            }
        }
    });
    form.on("submit(saveDept)",function(data){
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
        $.post(webpath+"/dept/saveDept", {
                'title': data.field.title,
                 'parentId' : parentId,
                  'content' :data.field.content,
                  'ordernum': data.field.ordernum
            }
            ,function(res){
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