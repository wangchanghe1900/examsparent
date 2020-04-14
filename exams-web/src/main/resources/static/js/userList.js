layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;
    //显示页面按钮
    $.get("sysmenu/btnAuthroInfo",{"btn":"user"},
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
        elem: '#userList',
        url :'getUserList',
        cellMinWidth : 95,
        page : true,
        height : "full-100",
        limits : [10,15,20,25],
        limit : 20,
        id : "userListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'username', title: '用户名', minWidth:100, align:"center"},
            {field: 'realname', title: '真实姓名', minWidth:100, align:"center"},
            {field: 'mobile', title: '移动电话', minWidth:100, align:"center"},
            {field: 'email', title: '用户邮箱', minWidth:200, align:'center',templet:function(d){
                return '<a class="layui-blue" href="mailto:'+d.email+'">'+d.email+'</a>';
            }},
            {field: 'deptname', title: '所属部门', align:'center',templet:function(d){
                return d.sysDept.deptname;
            }},
            {field: 'rolesname', title: '权限名称',  minWidth:150 ,align:'center',templet:function(d){
                var rolename="";
                for(var i=0;i<d.roles.length;i++){
                    rolename+=d.roles[i].name+",";
                }
                rolename=rolename.substring(0,rolename.length-1);
                return rolename;
            }},
            {field: 'status', title: '用户状态',  align:'center',templet:function(d){
                return d.status == "1" ? "正常使用" : "禁止使用";
            }},
            {field: 'lastlogintime', title: '最后登录时间', align:'center',minWidth:150},
            {title: '操作', minWidth:220, templet:'#userListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        //console.log($(".searchVal").val());
        if($(".searchVal").val() != ''){
            table.reload("userListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    realname: $(".searchVal").val()  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

    //编辑用户
    function editUser(edit){
        var index = layui.layer.open({
            title : "编辑用户",
            type : 2,
            content : "editUserList",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                    //console.log(edit);
                    body.find(".id").val(edit.id);
                    body.find(".username").val(edit.username);  //登录名
                    body.find(".realname").val(edit.realname);  //真实姓名
                    body.find(".email").val(edit.email);  //邮箱
                    //body.find(".userSex input[value="+edit.userSex+"]").prop("checked","checked");  //性别
                    body.find(".mobile").val(edit.mobile);  //电话号码
                    body.find(".status").val(edit.status);    //用户状态
                    body.find(".deptname").val(edit.sysDept.id);    //所属部门
                    //body.find(".roles").val(edit.roles);
                    form.render();
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回用户列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            },
            cancel: function(index, layero){
                location.reload();
                return false;
            }
        })
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    };

    //添加用户
    function addUser(){
        var index = layui.layer.open({
            title : "新增用户",
            type : 2,
            content : "addUserList",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                setTimeout(function(){
                    layui.layer.tips('点击此处返回用户列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            },
            cancel: function(index, layero){
                location.reload();
                return false;
            }
        })
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    };
    $(".addNews_btn").click(function(){
        window.sessionStorage.setItem("roles","");
        window.sessionStorage.setItem("deptId","");
        addUser();
    });

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('userListTable'),
            data = checkStatus.data,
            userId ="" ;//[];
        if(data.length > 0) {
            for (var i in data) {
                //userId.push(data[i].id);
                userId +=data[i].id+",";
            }
            userId=userId.substring(0,userId.length-1);
            layer.confirm('确定删除选中用户？',{icon:3, title:'提示信息'},function(index){
                $.post("delUserByIds",{
                    userids : userId  //将需要删除的newsId作为参数传入
                },function(data){
                    tableIns.reload();
                    layer.close(index);
                })
            });
        }else{
            layer.msg("请选择需要删除的用户");
        }
    });

    //列表操作
    table.on('tool(userList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            window.sessionStorage.setItem("deptId",data.sysDept.id);
            ids=[];
            for(var i=0;i<data.roles.length;i++){
                ids.push(data.roles[i].id);
            };
            window.sessionStorage.setItem("roles",JSON.stringify(ids));
            editUser(data);
        }else if(layEvent === 'usable'){ //重置密码

            layer.confirm("确定重置密码？",{
                icon: 3,
                title:'系统提示',
                cancel : function(index){
                    layer.close(index);
                }
            },function(index){
                $.post("resetpwd/"+data.id,function(data,status){
                    if(status=='success'){
                        layer.msg("密码重置成功，初始密码为：Abcd#123!");
                    }
                });
                layer.close(index);
            });
        }else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此用户？',{icon:3, title:'提示信息'},function(index){
                 $.get("delUserById",{
                     id : data.id  //将需要删除的newsId作为参数传入
                 },function(data){
                    tableIns.reload();
                    layer.close(index);
                })
            });
        }
    });

})
