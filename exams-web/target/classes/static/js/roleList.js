layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;
    //提取网页根目录
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var webpath=localhostPaht + projectName;
    //显示页面按钮
    $.get(webpath+"/sysmenu/btnAuthroInfo",{"btn":"role"},
        function (data) {
            if(data){
                if(data.isFind){
                    $('div:first').removeClass("disp");
                }
                if(data.isAdd){
                    $('div:nth-child(2)').removeClass("disp");
                }
                if(data.isBatchDel){
                    $('div:nth-child(3)').removeClass("disp");
                }
            }
        });
    //用户列表
    var tableIns = table.render({
        elem: '#roleList',
        url :'getRoleList',
        cellMinWidth : 95,
        page : true,
        height : "full-100",
        limits : [10,15,20,25],
        limit : 20,
        id : "roleListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'name', title: '角色名称', minWidth:100, align:"center"},
            {field: 'remark', title: '角色描述', minWidth:200, align:"center"},
            {field: 'isenable', title: '是否启用', minWidth:100, align:"center",templet:'#roleStatus'},
            {field: 'createtime', title: '创建日期', minWidth:100, align:"center"},
            {title: '操作', minWidth:220, templet:'#roleListBar',fixed:"right",align:"center"}
        ]]
    });
/*    form.on('switch(roleEnable)', function(data){
        var index = layer.msg('修改中，请稍候',{icon: 16,time:false,shade:0.8});
        setTimeout(function(){
            layer.close(index);
            if(data.elem.checked){
                layer.msg("已启用成功！");
            }else{
                layer.msg("取消启用！");
            }
        },500);
    })*/
    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        //console.log($(".searchVal").val());
        if($(".searchVal").val() != ''){
            table.reload("roleListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    name: $(".searchVal").val()  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

    //添加用户
    function addRole(edit){
        var index = layui.layer.open({
            title : "添加角色",
            type : 2,
            content : "addRoleList",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                setTimeout(function(){
                    layui.layer.tips('点击此处返回角色列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    }
    $(".addRole_btn").click(function(){
        addRole();
    });
    //编辑角色
    function editRole(edit){
        var index = layui.layer.open({
            title : "编辑角色",
            type : 2,
            content : "editRoleList",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                    //console.log(edit);
                    body.find(".id").val(edit.id);
                    body.find(".rolename").val(edit.name);  //角色名称
                    body.find(".remark").val(edit.remark);  //角色描述
                    body.find(".roleStatus input[value='"+edit.isenable+"']").prop("checked",true);  //角色状态
                    /*body.find(".status").val(edit.isenable);*/
                    form.render();
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回角色列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    };
    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('roleListTable'),
            data = checkStatus.data,
            roleId ="" ;//[];
        if(data.length > 0) {
            for (var i in data) {
               roleId +=data[i].id+",";
            }
            roleId=roleId.substring(0,roleId.length-1);
            layer.confirm('确定删除选中角色？',{icon:3, title:'提示信息'},function(index){
                $.post("delRoleByIds",{
                    roleIds : roleId  //将需要删除的roleId作为参数传入
                },function(data){
                    if(data==true){
                        tableIns.reload();
                        layer.msg("删除成功！");
                        layer.close(index);
                    }

                })
            });
        }else{
            layer.msg("请选择需要删除的角色");
        }
    });
    //设置权限
    function setPermiss(data){
        var index = layui.layer.open({
            title : "设置权限",
            type: 2,
            area: ['700px', '450px'],
            fixed: false, //不固定
            maxmin: true,
            content : "setPermissList",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(data){
                    body.find(".id").val(data.id);
                    body.find(".rolename").val(data.name);  //角色名称
                    form.render();
                }
            }
        });
    };
    //列表操作
    table.on('tool(roleList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;
        if(layEvent === 'edit'){ //编辑
            editRole(data);
        }else if(layEvent === 'setPower'){ //重置密码
            sessionStorage.setItem("roleId",data.id);
            setPermiss(data);
        }else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此角色？',{icon:3, title:'提示信息'},function(index){
                 $.get("delRoleById",{
                     id : data.id  //将需要删除的newsId作为参数传入
                 },function(data){
                    tableIns.reload();
                     layer.msg("删除成功！");
                    layer.close(index);
                })
            });
        }
    });

})
