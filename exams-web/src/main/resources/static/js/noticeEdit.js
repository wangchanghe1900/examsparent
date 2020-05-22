layui.config({
    base : "../js/"
}).extend({
    treeSelect: 'treeSelect'
})
layui.use(['form','layer','layedit','treeSelect','tree'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery ;
    var layedit = layui.layedit;
    var formSelects = layui.formSelects;
    var treeSelect= layui.treeSelect;
    var tree = layui.tree;
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var webpath=localhostPaht + projectName;
    layedit.set({
        uploadImage: {
            url: 'uploadImgFile' //接口url
            ,type: 'post' //默认post
        }
    });
    var editIndex = layedit.build('noticeContent');


    form.verify({
        notNull : function(value, item){
            var treeNode = tree.getChecked('depttree');
            //console.log(treeNode);
            if(treeNode == ""){
                return "请选择接收部门";
            }
        }
        ,content: function(value){
          layedit.sync(editIndex);
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
        ,showCheckbox: true
        ,data: getData()
        ,id: 'depttree'
        ,isJump: false //是否允许点击节点时弹出新窗口跳转
        ,click: function(d){

            //deptId= d.current.id;
            //form.render('select');
        }
    });
    function initValue(){
        var noticeId=sessionStorage.getItem("noticeId");
        if(noticeId!=null){
            sessionStorage.removeItem("noticeId");
            //noticeInfo=JSON.parse(noticeInfo);
            $.ajax({
                url: webpath+"/notice/getNoticeById?id="+noticeId,    //后台数据请求地址
                type: "get",
                async:false,
                success: function(data){
                    if(data.code==200){
                        form.val("noticeinfo",{
                            "id":data.data.id
                            ,"title":data.data.title
                            ,"content":data.data.content
                            ,"status":data.data.status
                            ,"isSendSysUser": data.data.isSendSysUser==="是" ? true:false
                            ,"isSendEmp":data.data.isSendEmp==="是" ? true:false
                        });
                        //console.log(noticeInfo.deptIds);
                        tree.setChecked('depttree', data.data.deptIds);
                        form.render();
                        //layedit.sync(editIndex);
                    }

                }
            });

        }
    }
    initValue();
    form.on("submit(EditNotice)",function(data){
        //弹出loading
        var treeNode = tree.getChecked('depttree');
        data.field.deptName=JSON.stringify(treeNode);
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
        $.post("saveNotice",{
            infos : JSON.stringify(data.field)
        },function(res){
            if(res.code!=200){
                top.layer.close(index);
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