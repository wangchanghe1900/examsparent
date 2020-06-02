$(function(){
	$(".div-btn-blue").hover(
		function(){
			$(this).addClass("div-btn-blue-over");
		},
		function(){
			$(this).removeClass("div-btn-blue-over");
		}
	);
});

//调用：dateFtt("yyyy-MM-dd hh:mm:ss",crtTime);
function dateFtt(fmt,date){
	var o = { 
		"M+" : date.getMonth()+1,     //月份 
		"d+" : date.getDate(),     //日 
		"h+" : date.getHours(),     //小时 
		"m+" : date.getMinutes(),     //分 
		"s+" : date.getSeconds(),     //秒 
		"q+" : Math.floor((date.getMonth()+3)/3), //季度 
		"S" : date.getMilliseconds()    //毫秒 
	}; 
	if(/(y+)/.test(fmt)) 
		fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	for(var k in o) 
		if(new RegExp("("+ k +")").test(fmt)) 
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
	
	return fmt; 
}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
         }
         if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
         }
     }
    return "";
}


function setCookie(cname, cvalue, exdays) {
	if(exdays==null){
		exdays=0.25;
	}
	
	if(exdays>0){
	    var d = new Date();
	    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
	    var expires = "expires="+d.toUTCString();
	    document.cookie = cname + "=" + encodeURIComponent(cvalue) + ";" + expires + ";path=/";
	}else{
	 	//alert(cname + "=" +encodeURIComponent(cvalue));
	   	document.cookie = cname + "=" + encodeURIComponent(cvalue) + ";expires="+new Date(0).toUTCString();
	}
	
    
	
}

function getMyInfo(){ 
	var decodedCookie = decodeURIComponent(document.cookie);
	var myInfo={};
	myInfo["department"]=getCookie("deptname");
	myInfo["username"]=getCookie("username");
	myInfo["realname"]=getCookie("realname");
	myInfo["userid"]=getCookie("userid");
	myInfo["deptId"]=getCookie("deptId");
	myInfo["userimg"]=getCookie("userimg");
	return myInfo; 
  　　 
　} 

function duration(st, et,ctype){
	var date1 = new Date(et).getTime() - new Date(st).getTime();  
	//alert("date1:"+date1);
	
	if(ctype=="days"){
		//计算出相差天数
		//var days=Math.floor(date1/(24*3600*1000));
		var days=(date1/(24*3600*1000)).toFixed(2);
		return days;
	}else{
		if(ctype=="hours"){
			//计算出小时数
			//var leave1=date1%(24*3600*1000);    
			//计算天数后剩余的毫秒数
			//var hours=Math.floor(leave1/(3600*1000));
			var hours=(date1/(3600*1000)).toFixed(2);
			return hours;
		}else{
			if(ctype=="minus"){
				 //var leave1=date1%(24*3600*1000);
				 //计算相差分钟数
				 //var leave2=leave1%(3600*1000);        
				 //计算小时数后剩余的毫秒数
				 //var minutes=Math.floor(leave2/(60*1000));
				var minutes=(date1/(60*1000)).toFixed(2);
				return minutes;
			}else{
				//var leave1=date1%(24*3600*1000);
				 //计算相差分钟数
				 //var leave2=leave1%(3600*1000);
				//计算相差秒数
				 //var leave3=leave2%(60*1000);
				 //计算分钟数后剩余的毫秒数
				 //var seconds=Math.round(leave3/1000);
				 var seconds=(date1/1000).toFixed(2);
				 return seconds;
			}
		}
	}
	
	return null;
}

//将数据写入本地
function writeLocalDate(key,jsondata){
    
    localStorage.setItem(key,JSON.stringify(jsondata));
}

//从本地获取数据
function getLocalData(key){
    var jsondata=localStorage.getItem(key);
    //返回值为数组 (JSON.parse： 将字符串转换为对象 数组) 
    return JSON.parse(jsondata);
}

function startLoading(){
	//获取浏览器页面可见高度和宽度
	var _PageHeight = document.documentElement.clientHeight,
	    _PageWidth = document.documentElement.clientWidth;
	//计算loading框距离顶部和左部的距离（loading框的宽度为215px，高度为61px）
	var _LoadingTop = _PageHeight > 61 ? (_PageHeight - 61) / 2 : 0,
	    _LoadingLeft = _PageWidth > 215 ? (_PageWidth - 215) / 2 : 0;
	//在页面未加载完毕之前显示的loading Html自定义内容
	var _LoadingHtml = '<div id="loadingDiv" style="position:absolute;left:0;width:100%;height:' + _PageHeight + 'px;top:0;background:#f3f8ff;opacity:1;filter:alpha(opacity=80);z-index:10000;"><div style="position: absolute; cursor1: wait; left: ' + _LoadingLeft + 'px; top:' + _LoadingTop + 'px; width: auto; height: 57px; line-height: 57px; padding-left: 50px; padding-right: 5px; background: #fff url(images/comm/loading.gif) no-repeat scroll 5px 10px; border: 2px solid #95B8E7; color: #696969; font-family:\'Microsoft YaHei\';">页面加载中，请等待...</div></div>';
	//呈现loading效果
	//document.write(_LoadingHtml);
	$("body").children().first().prepend(_LoadingHtml) ;
}

//加载状态为complete时移除loading效果
function completeLoading() {
    var loadingMask = document.getElementById('loadingDiv');
    loadingMask.parentNode.removeChild(loadingMask);
}
