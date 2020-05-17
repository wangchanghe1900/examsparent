var myInfo={};
var materialID="";
var materialst=null;
var outExamTimes=0,orderNum=0;
$(function(){
	//来源检查
	var referrer = document.referrer;
	/*
	 if (referrer==null||referrer==""||(referrer.indexOf("http://localhost:10086/examsweb/")<0 && referrer.indexOf("http://20.1.139.26:10086/examsweb/")<0)) {
    	window.location.href='/examsweb/portal/login.html';
    }*/
	//var viewheight= $("#mybody").height() - $("#btn-bottom").height();
	//调整屏幕高度
	var viewheight= document.documentElement.clientHeight - $("#btn-bottom").height();
	$("#mybody").height(viewheight);
	
	myInfo = getMyInfo();
	if(myInfo.userid==null||myInfo.userid==""){
		window.location.href='/examsweb/portal/login.html';
	}
	
	startLoading();
	materialID = getCookie("materialID");
	materialst = getCookie("materialst");
	
	//记录考试次数
	outExamTimes= parseInt(getCookie("outExamTimes"));
	//alert(outExamTimes);
	if(isNaN(outExamTimes)){
		outExamTimes=0;
	}
	
	//考试顺序号
	//orderNum='<%=session.getAttribute("orderNum")%>';
	/*
	orderNum=parseInt(getCookie("orderNum"));
	if(isNaN(orderNum)){
		orderNum=1;
	}*/
	if(outExamTimes==null||outExamTimes==0||isNaN(outExamTimes)){
		$("#exam-btn").html("进入考试");
	}else{
		//alert(outExamTimes);
		$("#exam-btn").html("返回考试");
	}
	
	$("#return-btn").click(
		function(){
			//window.history.back(-1);
			if(outExamTimes==null||outExamTimes==0||isNaN(outExamTimes)){
				studyresult("/examsweb/portal/unStudy.html");
			}else{
				//考试跳出的学习，不上报学习时长
				$("#exitModal").show();
				//window.location.href="unStudy.html";
			}
		 }
	);
	$("#exitModalBtn").click(
		function(){
			
			window.location.href="/examsweb/portal/unStudy.html";
		 }
	);
	
	
	$("#exam-btn").click(
		function(){
			//var url='exam.html?outExamTimes='+outExamTimes+"&orderNum="+orderNum;
			var url='/examsweb/portal/exam.html';
			//outExamTimes++;
			//setCookie("outExamTimes",outExamTimes,null);
			
			if(outExamTimes==null||outExamTimes==0||isNaN(outExamTimes)){
				setCookie("examST",new Date().toUTCString(),null);
				studyresult(url);
			}else{
				//考试跳出的学习，不上报学习时长
				
				window.location.href=url;
			}
		}
	);
	
	setTimeout(function(){
 		completeLoading();
    },1000);
	
});

function GetQueryString(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r!=null) return (r[2]); return null; 
} 

//提交学习结果反馈
function studyresult(url){
	//计算时长(分钟)，提交
	var studyduration=Math.floor(duration(materialst,new Date().toUTCString(),"minus"));
	//alert("materialst:"+materialst+";materialet:"+new Date().toUTCString()+" ;duration:"+studyduration);
	//参数
	var code="";
	//dec({"empID"："","materalID":"","studyDuration":""})
	code="{\"empID\":\""+myInfo.userid+"\",\"materalID\":\""+materialID+"\",\"studyDuration\":\""+studyduration+"\"}";
	//alert(code);
	//加密
	encode=aesEncryptJava(code,abc,abc);
	//alert(encode);
	var curdt=dateFtt("yyyyMMddhhmmssS",new Date());
	//alert(curdt);
	var post_json={};
	post_json["code"]=encode;
	post_json["timestamp"]=curdt;
	//获取数据
	$.ajax({
		 //"url":"unlearnedresource.html",//方法请求到/emp/logint路径
		 "url":"/examsweb/api/resource/learnedResult",
         "data":post_json,	//传过去的数据，现在没有 null 或者直接删除掉这行
         "type":"post",//请求方式
         "dataType":"json",//数据类型
         "contentType":"application/x-www-form-urlencoded; charset=utf-8",
         "success":function(json) {
        	//console.log(json);
         	var data=json;
         	//console.log(data);
         	if(data.code=="200"){
         		//清空学习数据
         		setCookie("materialID","",null);
         		setCookie("materialst",null,null);
         		window.location.href=url;
			}else{
				
			}
         },
         
         "error":function(error){
        	console.log(error);
			
			return false;
		}
    });
	
}
	 


