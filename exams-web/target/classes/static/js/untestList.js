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
        elem: '#untestList',
        url :'getUntestList',
        cellMinWidth : 95,
        page : true,
        height : "full-100",
        limits : [10,15,20,25],
        limit : 20,
        id : "untestTable",
        toolbar: 'true',
        title: '未考统计',
        defaultToolbar: ['filter', 'exports', 'print'],
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'tid', title: '试卷ID', minWidth:80, align:"center"},
            {field: 'testName', title: '试卷名称', minWidth:220, align:"center"},
            {field: 'resourceName', title: '学习资料名称', minWidth:250, align:"center"},
            {field: 'publishDate', title: '考试发布日期', minWidth:200, align:"center"},
            {field: 'employeeName', title: '员工姓名', minWidth:200, align:"center"},
            {field: 'empCode', title: '员工编号', minWidth:150, align:"center"},
            {field: 'deptName', title: '所属部门', minWidth:200, align:"center"}

        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $('.search_btn').on('click', function(){
        var data = form.val('querycondition');
        table.reload("untestTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                data: JSON.stringify(data)
            }
        })
    });


})
