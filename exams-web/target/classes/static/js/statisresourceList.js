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
            //deptid=data.id;
            //console.log(deptid);
            table.reload("unlearnResListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    deptId: data.id  //搜索的关键字
                }
            })

            table.reload("learnResListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    deptId: data.id  //搜索的关键字
                }
            })

            table.reload("empTestListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    deptId: data.id  //搜索的关键字
                }
            })

        }
    });
    //未学列表
    var tableIns = table.render({
        elem: '#unlearnResList',
        url :'getUnlearnResourceList',
        cellMinWidth : 95,
        page : true,
        height : "full-100",
        toolbar: 'true',
        title: '未学统计',
        defaultToolbar: ['filter', 'exports', 'print'],
        limits : [10,20,30],
        limit : 30,
        id : "unlearnResListTable",
        cols : [[
            {field: 'resourceName', title: '未学资源名称', minWidth:250, align:"center"},
            {field: 'employeeName', title: '员工姓名', align:'center'},
            {field: 'deptName', title: '部门名称', minWidth:100, align:"center"}

        ]]
    });
    //已学资源
    var tableIns = table.render({
        elem: '#learnResList',
        url :'getLearnResourceList',
        cellMinWidth : 95,
        page : true,
        height : "full-100",
        toolbar: 'true',
        title: '已学统计',
        defaultToolbar: ['filter', 'exports', 'print'],
        limits : [10,20,30],
        limit : 30,
        id : "learnResListTable",
        cols : [[
            {field: 'resourceName', title: '已学资源名称', minWidth:200, align:"center"},
            {field: 'employeeName', title: '员工姓名', align:'center'},
            {field: 'deptName', title: '部门名称', minWidth:100, align:"center"},
            {field: 'learnTimes', title: '学习次数', minWidth:100, align:"center"},
            {field: 'learnLong', title: '学习总时长', minWidth:100, align:"center"}
        ]]
    });

    //考试信息
    var tableIns = table.render({
        elem: '#empTestList',
        url :'getEmpTestInfoList',
        cellMinWidth : 95,
        page : true,
        height : "full-100",
        toolbar: 'true',
        title: '考试统计',
        defaultToolbar: ['filter', 'exports', 'print'],
        limits : [10,20,30],
        limit : 30,
        id : "empTestListTable",
        cols : [[
            {field: 'testName', title: '已考试卷', minWidth:200, align:"center"},
            {field: 'employeeName', title: '员工姓名', align:'center'},
            {field: 'deptName', title: '部门名称', minWidth:120, align:"center"},
            {field: 'score', title: '分数', minWidth:60, align:"center"},
            {field: 'testDuration', title: '考试时长', minWidth:100, align:"center"},
            {field: 'returnCount', title: '返回次数', minWidth:120, align:"center"},
            {field: 'testTime', title: '考试日期', minWidth:120, align:"center"}

        ]]
    });

})
