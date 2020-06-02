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
            table.reload("deptResourceListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    deptId: data.id  //搜索的关键字
                }
            })

            table.reload("deptTestReturnListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    deptId: data.id  //搜索的关键字
                }
            })


        }
    });
    //部门已学统计
    var tableIns = table.render({
        elem: '#deptResourceList',
        url :'getDeptResourceList',
        cellMinWidth : 95,
        page : true,
        height : "full-100",
        toolbar: 'true',
        title: '部门已学资源统计',
        defaultToolbar: ['filter', 'exports', 'print'],
        limits : [10,20,30,500],
        limit : 30,
        id : "deptResourceListTable",
        cols : [[
            {field: 'deptName', title: '部门名称', minWidth:120, align:"center"},
            {field: 'resourceName', title: '资源名称', minWidth:200, align:"center"},
            {field: 'deptEmpCount', title: '部门人数', minWidth:100,align:'center'},
            {field: 'learnCount', title: '学习总时长', minWidth:100,align:'center'},
            {field: 'avgCount', title: '人均时长', minWidth:100,align:'center'},

        ]]
    });
    //部门考试统计
    var tableIns = table.render({
        elem: '#deptTestList',
        url :'getDeptTestReturnList',
        cellMinWidth : 95,
        page : true,
        height : "full-100",
        toolbar: 'true',
        title: '部门考试返回次数统计',
        defaultToolbar: ['filter', 'exports', 'print'],
        limits : [10,20,30,500],
        limit : 30,
        id : "deptTestReturnListTable",
        cols : [[
            {field: 'deptName', title: '部门名称', minWidth:100, align:"center"},
            {field: 'testName', title: '试卷名称', minWidth:200, align:"center"},
            {field: 'deptEmpCount', title: '部门人数', minWidth:100,align:'center'},
            {field: 'returnCount', title: '返回总次数', minWidth:100, align:"center"},
            {field: 'avgCount', title: '人均次数', minWidth:100, align:"center"}
        ]]
    });



})
