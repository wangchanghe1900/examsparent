layui.use(['form','layer',"jquery",'table'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        table = layui.table,
        $ = layui.jquery ;


    var tableIns = table.render({
        elem: '#messageList',
        url :'getMessageList',
        cellMinWidth : 95,
        page : true,
        height : "full-100",
        limits : [10,15,20,25],
        limit : 20,
        id : "messageListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'title', title: '消息标题', minWidth:200, align:"center",templet:function(d){
                return d.notice.title;
            }},
            {field: 'createDate', title: '接收日期', minWidth:150, align:"center",sort: true},
            {field: 'sendUser', title: '发送人', minWidth:100, align:"center"},
            {field: 'isRead', title: '状态', minWidth:180, align:'center',templet:'#messageStatus'},
            {field: 'realName', title: '接收人', minWidth:120, align:'center'},
            {title: '操作', minWidth:120, templet:'#messageListBar',fixed:"right",align:"center"}
        ]]
    });
    //搜索
    $(".search_btn").on("click",function(){
        var data = form.val('querycondition');
        table.reload("messageListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                data: JSON.stringify(data)
            }
        })
    });

    //浏览详情
    function showDetail(edit){
        top.layui.layer.open({
            title : "消息详情",
            type : 2,
            anim: 5,
            area: ['700px', '550px'],
            content : "message/showDetailList",
            success : function(layero, index){
/*                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                    body.find(".title").html(edit.notice.title);
                }*/
            },
            cancel: function(index, layero){
                $.get("updateMessageStatus",{
                    id : edit.id,
                    isRead:'是'
                },function(data){
                    layer.close(index);
                    tableIns.reload();
                    //layer.msg(data.msg);
                });
                //layer.close(index);
                return false;
            }
        })
    }
    //列表操作
    table.on('tool(messageList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;
        if(layEvent === 'detail'){
            sessionStorage.setItem("messageInfo",JSON.stringify(data));
            showDetail(data);
        }
    });
})