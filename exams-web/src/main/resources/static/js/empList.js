layui.config({
    base : "../js/"
}).extend({
    treeSelect: 'treeSelect'
})
layui.use(['form','layer','table','laytpl','treeSelect','laydate'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table,
        laydate=layui.laydate;
    var formSelects = layui.formSelects;
    var treeSelect= layui.treeSelect;
    //提取网页根目录
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var webpath=localhostPaht + projectName;
    //显示页面按钮
    $.get(webpath+"/sysmenu/btnAuthroInfo",{"btn":"emp"},
        function (data) {
            if(data){
                if(data.isFind){
                    $('div:nth-child(2)').removeClass("disp");
                }
                if(data.isAdd){
                    $('div:nth-child(3)').removeClass("disp");
                }
                if(data.isBatchDel){
                    $('div:nth-child(4)').removeClass("disp");
                }
                if(data.isDetail){
                    $('div:nth-child(5)').removeClass("disp");
                }
                if(data.isResetPwd){
                    $('div:nth-child(6)').removeClass("disp");
                }
            }
    });
    //用户列表
    var tableIns = table.render({
        elem: '#empList',
        url :'getEmpList',
        cellMinWidth : 95,
        page : true,
        height : "full-100",
        limits : [10,15,20,25],
        limit : 20,
        id : "empListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'employeeCode', title: '员工编码', minWidth:120, align:"center"},
            {field: 'employeeName', title: '员工姓名', minWidth:100, align:"center"},
            {field: 'mobile', title: '移动电话', minWidth:100, align:"center"},
            {field: 'employeeStatus', title: '员工状态', minWidth:100, align:"center",templet:'#empStatus'},
            {field: 'capacity', title: '职位', minWidth:100, align:"center"},
            {field: 'deptname', title: '所属部门', align:'center',templet:function(d){
                return d.sysDept.deptname;
            }},
            {field: 'identitys', title: '身份', align:'center',minWidth:80},
            {field: 'entryTime', title: '入职时间', align:'center',minWidth:120},
            {field: 'workType', title: '工时核算', align:'center',minWidth:120},
            {field: 'lastLoginTime', title: '最后登录日期', align:'center',minWidth:120},
            {title: '操作', minWidth:200, templet:'#empListBar',fixed:"right",align:"center"}
        ]]
    });
    var deptId;
    treeSelect.render({
        // 选择器
        elem: '#depttree',
        // 数据
        data: webpath+'/dept/getDeptAllInfo',
        // 异步加载方式：get/post，默认get
        type: 'get',
        // 占位符
        placeholder: '请选择员工部门',
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
      }
    });
    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    layui.$('#btnreload').on('click', function(){
        var data = form.val('querycondition');
        data.deptId=deptId;
        table.reload("empListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                data: JSON.stringify(data)
            }
        })
    });

    laydate.render({
        elem: '#entryStartTime'
        ,format:'yyyy-MM-dd'
        ,trigger: 'click'
        ,theme: 'molv'
    });
    laydate.render({
        elem: '#entryEndTime'
        ,format:'yyyy-MM-dd'
        ,trigger: 'click'
        ,theme: 'molv'
    });
    //编辑用户
    function editEmployee(edit){
        var index = layui.layer.open({
            title : "编辑员工信息",
            skin: 'layui-layer-lan',
            type : 2,
            content : "editempList",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                    body.find(".id").val(edit.id);
                    form.render();
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回员工列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            },
            cancel: function(index, layero){
                location.reload();
                return false;
            }
        });
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        });
    };

    //添加用户
    function addEmployee(){
        var index = layui.layer.open({
            title : "新增员工信息",
            type : 2,
            skin: 'layui-layer-lan',
            content : "addEmpList",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                setTimeout(function(){
                    layui.layer.tips('点击此处返回员工列表', '.layui-layer-setwin .layui-layer-close', {
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
    $(".addEmp_btn").click(function(){
        addEmployee();
    });
    //浏览详情
    function showDetail(edit){
        top.layui.layer.open({
            title : "员工详情",
            type : 2,
            anim: 5,
            skin: 'layui-layer-lan',
            area: ['700px', '550px'],
            content : "emp/showDetailList",
            success : function(layero, index){
                //var body = layui.layer.getChildFrame('body', index);

            }
        })
    }
    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('empListTable'),
            data = checkStatus.data,
            empIds ="" ;//[];
        if(data.length > 0) {
            for (var i in data) {
                //userId.push(data[i].id);
                empIds +=data[i].id+",";
            }
            empIds=empIds.substring(0,empIds.length-1);
            layer.confirm('确定删除选中员工信息？',{icon:3, title:'提示信息'},function(index){
                $.post("delEmployeeByIds",{
                    ids : empIds  //将需要删除的newsId作为参数传入
                },function(data){
                    tableIns.reload();
                    layer.close(index);
                })
            });
        }else{
            layer.msg("请选择需要删除的员工");
        }
    });
    //导入信息
    $(".import_btn").click(function () {
        layui.layer.open({
            title : "导入员工信息",
            type : 2,
            anim: 5,
            skin: 'layui-layer-rim',
            area: ['500px', '300px'],
            content : "importEmpList",
            success : function(layero, index){
                //var body = layui.layer.getChildFrame('body', index);


            }
        })
    });

    $(".resetPwd_btn").click(function(){
        var checkStatus = table.checkStatus('empListTable'),
            data = checkStatus.data,
            empIds ="" ;//[];
        if(data.length > 0) {
            for (var i in data) {
                //userId.push(data[i].id);
                empIds +=data[i].id+",";
            }
            empIds=empIds.substring(0,empIds.length-1);
            layer.confirm('确定初始化密码？',{icon:3, title:'提示信息'},function(index){
                $.post("resetPwdEmpByIds",{
                    ids : empIds  //将需要删除的newsId作为参数传入
                },function(data){
                   layer.msg(data.msg,{icon: 0});
                })
            });
        }else{
            layer.msg("请选择需要删除的员工");
        }
    });
    //列表操作
    table.on('tool(empList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            sessionStorage.setItem("empinfo",JSON.stringify(data));
            editEmployee(data);
        }else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此员工信息？',{icon:3, title:'提示信息'},function(index){
                 $.get("delEmployeeById",{
                     id : data.id  //将需要删除的newsId作为参数传入
                 },function(data){
                    tableIns.reload();
                    layer.close(index);
                })
            });
        }else if(layEvent === 'detail'){
            sessionStorage.setItem("empinfo",JSON.stringify(data));
           showDetail(data);
        }
    });

})
