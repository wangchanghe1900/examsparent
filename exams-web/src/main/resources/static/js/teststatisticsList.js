layui.use(['form','layer','table','laytpl','laydate'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table,
        laydate=layui.laydate;
    //提取网页根目录
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var webpath=localhostPaht + projectName;

    //用户列表
    var tableIns = table.render({
        elem: '#teststatisList',
        url :'getTestStatisList',
        cellMinWidth : 95,
        page : true,
        height : "full-100",
        limits : [10,15,20,25],
        limit : 20,
        id : "teststatisTable",
        toolbar: 'true',
        title: '试卷统计',
        defaultToolbar: ['filter', 'exports', 'print'],
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'testName', title: '试卷名称', minWidth:120, align:"center",templet:function(d){
                return d.testpaper.testName;
            }},
            {field: 'deptName', title: '考试部门', minWidth:100, align:"center"},
            {field: 'testCount', title: '考试人数', minWidth:100, align:"center"},
            {field: 'untestCount', title: '未试人数', minWidth:100, align:"center"},
            {field: 'passrate', title: '及格率(%)', minWidth:100, align:"center"},
            {field: 'avgscore', title: '平均分', minWidth:100, align:"center"},
            {field: 'finerate', title: '优秀率(%)', minWidth:100, align:"center"},

        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $('.search_btn').on('click', function(){
        var data = $(".searchVal").val();
        table.reload("teststatisTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                testName: data
            }
        })
    });


})
