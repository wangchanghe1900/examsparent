var curnum=0,totalNum=0,curPagenum=0,outExamTimes=0,showNum=7,examDuration=0 , answerNum=0; 
var myInfo={};
var examId="",materialID="",materialimg="",materialURL="",materialType="",examName="";
var answerlist={};
var examST=null;
$(function(){
	//来源检查
	var referrer = document.referrer;
    if (referrer==null||referrer==""||(referrer.indexOf("http://localhost:10086/examsweb/")<0 && referrer.indexOf("http://20.1.139.26:10086/examsweb/")<0)) {
    	window.location.href='/examsweb/portal/login.html';
    }
	$("#content_view").width(document.documentElement.clientWidth);
	
	myInfo=getMyInfo();
	if(myInfo.userid==null||myInfo.userid==""){
		window.location.href='/examsweb/portal/login.html';
	}
	
	startLoading();
	loadcookie();
	$("#examtitle").html(examName);
	$("#examDuration").html(examDuration);
	loadData(myInfo,examId,showNum,curPagenum);	
	
	$("#train-btn").click(
		function(){
			outExamTimes++;
			setCookie("outExamTimes",outExamTimes,null);
			setCookie("orderNum",curnum,null);
			//setCookie("materialID",materialID,null);
			//setCookie("materialst",new Date().toUTCString(),null);
			//（0：PPT，1：视频，2：音频)
			if(materialType=="0"){
				window.location.href='/examsweb/portal/trainppt.html?file='+materialURL;
			}else{
				window.location.href='/examsweb/portal/trainvideo.html?file='+materialURL;
			}
			
		}
	);
	
	$("#return-btn").click(
		function(){
			$("#exitTXT").html("您正在考试，如果退出将不计算本次考试，确定退出吗？"); 
			$("#exitModalBtn").attr("data-toggle","");
     		$("#exitModalBtn").attr("data-target","");
			$("#exitModal").show();
		}
	);
	$("#exitModalBtn").click(
		function(){
			if(answerNum>0){
				//提交
				submitAnswer(myInfo,examId,totalNum);	
			}else{
				clearData();
				window.location.href="/examsweb/portal/unStudy.html";
			}
			
		 }
	);
	$("#before-btn").click(
		function(){
			curnum--;
			getquestion(curnum);
			
		}
	);
	$("#next-btn").click(
		function(){
			//存储答案
			saveAnswer();
			//更新下一题
			curnum++;
			if((curnum<=(curPagenum * showNum)) && (curnum<=totalNum)){
				getquestion(curnum);
			}else{
				if(curnum<=totalNum){
					curPagenum++;
					loadData(myInfo,examId,showNum,curPagenum);
				}
			}
		}
	);
	$("#submit-btn").click(
		function(){
			//保存答案
			saveAnswer();
			//提交确认
			var myAkey=examId+"A";
			var adata={};
			adata=getLocalData(myAkey);
			answerNum=getAnswerNum(adata);
			$("#exitTXT").html("本次考试题目共： "+totalNum+" ，您答了 "+answerNum+" 道题，确认提交考试吗？"); 
			$("#exitModalBtn").attr("data-toggle","modal");
     		$("#exitModalBtn").attr("data-target","#scoresModal");
     		$("#exitModal").show();
			
		}
	);
	
	$("#scoresModalBtn").click(
			function(){
			//window.history.back(-1);
			clearData();
			window.location.href="/examsweb/portal/unStudy.html";
		 }
	);
	setTimeout(function(){
 		completeLoading();
    },1000);
	
	//设置时间刷新
	setTimeout(function timeRefresh() {
		examDuration =calculationDuration(examST,new Date().toUTCString());
		$("#examDuration").html(examDuration);
		setTimeout(timeRefresh, 60000);
	}, 60000);
});



function loadcookie(){
	var decodedCookie = decodeURIComponent(document.cookie);
	
	var cookievalue=parseInt(getCookie("orderNum"));
	if((cookievalue==null) || isNaN(cookievalue)){
		curnum=1;
	}else{
		curnum=cookievalue;
	}
	
	curPagenum=parseInt(getCookie("examPageNum"));
	if((curPagenum==null) || isNaN(curPagenum)){
		curPagenum=1;
	}
	outExamTimes=parseInt(getCookie("outExamTimes")); 
	if((outExamTimes==null) || isNaN(outExamTimes)){
		outExamTimes=1;
	}
	examId=getCookie("examID");
	materialID=getCookie("materialID");
	materialimg=getCookie("materialimg");
	materialURL=getCookie("materialURL");
	materialType=getCookie("materialType");
	examName=getCookie("examName");
	
	examST = getCookie("examST");
	examDuration=calculationDuration(examST,new Date().toUTCString());
	
}

function clearData(){
	//清除考试数据、清除cookie
	localStorage.clear();
	setCookie("materialID",null,null);
    setCookie("materialst",null,null);
    setCookie("materialType",null,null);
    setCookie("examName",null,null);
    setCookie("materialimg",null,null);
    setCookie("materialURL",null,null);
    setCookie("outExamTimes",0,null);
    setCookie("examID",null,null);
    setCookie("examPageNum",0,null);
    setCookie("orderNum",0,null);
}

function loadData(pmyInfo,pexamId,pshowNum,pcurPagenum){
	//参数
	var code="";
	//dec({"empID"："","examID":"","showNum":10,"pageNum":1})
	code="{\"empID\":\""+pmyInfo.userid+"\",\"examID\":\""+pexamId+"\",\"showNum\":\""+pshowNum+"\",\"pageNum\":\""+pcurPagenum+"\"}";
	//alert(code);
	//加密
	encode=aesEncryptJava(code,abc,abc);
	//alert(encode);
	var curdt=dateFtt("yyyyMMddhhmmssS",new Date());
	//alert(curdt);
	var post_json={};
	post_json["code"]=encode;
	post_json["timestamp"]=curdt;
	
	//测试数据
	var url="examdata.html";
	/*
	if(pcurPagenum==1){
		url="examdata.html";
	}else{
		url="examdata"+pcurPagenum+".html";
	}
	*/
	url="/examsweb/api/test/testpaper";
	//获取数据
	$.ajax({
		"url":url,//方法请求到
         "data":post_json,	//传过去的数据，现在没有 null 或者直接删除掉这行
         "type":"post",//请求方式
         "dataType":"json",//数据类型
         "contentType":"application/x-www-form-urlencoded; charset=utf-8",
         "success":function(json) {
         	if(json.code=="200"){
         		//本地存储
         		var mykey=examId + pcurPagenum;
         		//alert("examId="+examId+"  pcurPagenum="+pcurPagenum+"  mykey="+mykey);
         		//console.log("mykey="+mykey);
         		//console.log(json.data);
         		writeLocalDate(mykey,json.data);
         		//curnum++;
         		totalNum=json.data.totalNum;
         		curPagenum=pcurPagenum;
         		//console.log("curnum="+curnum);
         		getquestion(curnum);
         		
			}else{
				//$("#curren-num").html(0);
         		//$("#total-num").html(0);
				return false;
			}
         	
         },
         
         "error":function(error){
        	console.log(error);
        	$("#subject-content").append('<p><strong class="d-block text-gray-dark text-center">没有内容</strong></p>');
			
			return false;
		}
    });
	
}

function getquestion(datanum){
////data={"subject":" 下列方法中，能让网页中第5个div隐藏的是（  ）","sj-num":"2","sj-type":"1","solution":[{"order":"A.","desc":"$('div:5th-child').hide()"},{"order":"B.","desc":"$('div:5th-child').hidden()"},{"order":"C.","desc":"$('div:nth-child(5)').hide()"},{"order":"D.","desc":"$('div:nth-child(5)').hidden()"}]};	
	//计算本题在第几页
	var pagenum=Math.ceil(datanum/showNum);
	var pdatanum=datanum -(pagenum -1)*showNum -1;
	//alert("pagenum="+pagenum);
	var data={};
	var mykey=examId+pagenum;
	var ldata={};
	ldata=getLocalData(mykey);
	//console.log("examId="+examId+"  ;pagenum="+pagenum+"  ;mykey="+mykey);
	//console.log("datanum="+(datanum -1));
	//console.log(ldata);
	if(ldata==null||ldata==""){
		$("#subject-content").append('<p><strong class="d-block text-gray-dark text-center">没有内容</strong></p>');
		
		$("#before-btn").attr("disabled", true); 
		$("#before-btn").removeClass("btn-primary");
		$("#before-btn").addClass("btn-secondary");
		$("#next-btn").attr("disabled", true); 
		$("#next-btn").removeClass("btn-primary");
		$("#next-btn").addClass("btn-secondary");
		$("#submit-btn").attr("disabled", true); 
		$("#submit-btn").removeClass("btn-primary");
		$("#submit-btn").addClass("btn-secondary");
		
		return ;
	}else{
		data["subject"]=ldata.questionsList[(pdatanum)].question;
		data["sj-id"]=ldata.questionsList[(pdatanum)].questionID;
		data["sj-num"]=ldata.questionsList[(pdatanum)].orderNum;
		data["sj-type"]=ldata.questionsList[(pdatanum)].questionType;
		data["solution"]=ldata.questionsList[(pdatanum)].option;
	}
	
	var myAkey=examId+"A";
	var adata={};
	adata=getLocalData(myAkey);
	//console.log("examId="+examId+"  ;myAkey="+myAkey);
	//console.log(adata);
	if(adata==null){
		data["answer"]=null;
	}else{
		if(adata.optionList[(datanum -1)]==null || adata.optionList[(datanum -1)].answer==null){
			data["answer"]=null;
		}else{
			data["answer"]=adata.optionList[(datanum -1)].answer;
		}
	}
	
	
	$("#curren-num").html(datanum);
	$("#total-num").html(totalNum);
	
	//动态显示内容
 	$("#subject-content").empty();
 	
	if(data["sj-type"]==0){
		//单选
		var listr = '<li data-type='+data["sj-type"]+' data-id='+data["sj-id"]+' data-ordernum='+data["sj-num"]+'>'+
			'<p class="subject">'+data["sj-num"]+'. [单选] '+data["subject"]+'</p>'+
			'<div class="d-block my-3">'
		for(var i=0;i<data["solution"].length;i++){
			if(data["answer"]!=null && data["answer"].indexOf(data["solution"][i].order)!= -1){
				listr=listr +
				'<div class="custom-control custom-radio">'+
				'<input id ="'+data["solution"][i].order+'"  name="Method" type="radio" class="custom-control-input" checked required >'+
				'<label class="custom-control-label" for="'+data["solution"][i].order+'">'+data["solution"][i].order+data["solution"][i].desc+'</label>'+
				'</div>'
			}else{
				listr=listr +
				'<div class="custom-control custom-radio">'+
				'<input id ="'+data["solution"][i].order+'"  name="Method" type="radio" class="custom-control-input" required >'+
				'<label class="custom-control-label" for="'+data["solution"][i].order+'">'+data["solution"][i].order+data["solution"][i].desc+'</label>'+
				'</div>'
			}
			
		}
		listr=listr +' </div> </li>	';
		
	}else{
		//多选
		var listr = '<li data-type='+data["sj-type"]+' data-id='+data["sj-id"]+' data-ordernum='+data["sj-num"]+'>'+
			'<p class="subject">'+data["sj-num"]+'. [多选] '+data["subject"]+'</p>'+
			'<div class="d-block my-3">'
		for(var i=0;i<data["solution"].length;i++){
			if(data["answer"]!=null && data["answer"].indexOf(data["solution"][i].order)!= -1){
				listr=listr +
				'<div class="custom-control custom-checkbox">'+
				'<input type="checkbox" class="custom-control-input" id="'+data["solution"][i].order+'" checked>'+
				'<label class="custom-control-label" for="'+data["solution"][i].order+'" >'+data["solution"][i].order+data["solution"][i].desc+'</label>'+
				'</div>'
			}else{
				listr=listr +
				'<div class="custom-control custom-checkbox">'+
				'<input type="checkbox" class="custom-control-input"  id="'+data["solution"][i].order+'" >'+
				'<label class="custom-control-label" for="'+data["solution"][i].order+'" >'+data["solution"][i].order+data["solution"][i].desc+'</label>'+
				'</div>'
			}
				
		}
		listr=listr +' </div> </li>	';
	}
	$("#subject-content").append(listr);
	
	if(data["sj-num"]=="1"){
		$("#before-btn").attr("disabled", true); 
		$("#before-btn").removeClass("btn-primary");
		$("#before-btn").addClass("btn-secondary");
		
	}else{
		$("#before-btn").attr("disabled", false); 
		$("#before-btn").removeClass("btn-secondary");
		$("#before-btn").addClass("btn-primary");
	}
	if(data["sj-num"]==$("#total-num").html()){
		$("#next-btn").attr("disabled", true); 
		$("#next-btn").removeClass("btn-primary");
		$("#next-btn").addClass("btn-secondary");
	}else{
		$("#next-btn").attr("disabled", false); 
		$("#next-btn").removeClass("btn-secondary");
		$("#next-btn").addClass("btn-primary");
	}
}

function saveAnswer(){
	//读取
	var myAkey=examId+"A";
	var adata={};
	adata=getLocalData(myAkey);
	if(adata==null){
		//更新变量
		//alert($("#subject-content li").attr("data-id"));
		//alert($("#subject-content li").attr("data-ordernum"));
		adata={};
		adata["optionList"]=[];
	}
	var curordernum=parseInt($("#subject-content li").attr("data-ordernum")) -1;
	adata["optionList"][curordernum]={};
	adata["optionList"][curordernum]["questionID"]=$("#subject-content li").attr("data-id");
	var myanswer = "";
	//alert($("input:checked").val());
	//alert($("input:checked").attr("id"));
	var checkedinput=$("input:checked");
	//console.log(checkedinput);
	for (var i = 0; i < checkedinput.length; i++) {
		if (checkedinput[i].checked) {
			myanswer=myanswer+(checkedinput[i].id);
		}
	}
	//alert(myanswer);
	adata["optionList"][curordernum]["answer"]=myanswer;
	
	answerlist=adata;
	//本地存储
	writeLocalDate(myAkey,answerlist);
}

function submitAnswer(pmyInfo,pexamId,ptotalNum){
	//参数判断
	if(pmyInfo.userid==null||pmyInfo.userid==""||pexamId==null||pexamId==""){
		alert("长时间没有操作，数据已失效！"); 
		return false;
	}
	
	//参数
	//console.log("myAkey="+myAkey);
	//console.log(adata);
	var code="";
	var myAkey=pexamId+"A";
	var adata={};
	adata=getLocalData(myAkey);
	//dec({"empID":"18610810006","examID":"21","totalNum":5,"answerNum":5,
    //"quitTimes":3,"duration":20,"optionList":[{"questionID":"1.","answer":"A."},
    //    {"questionID":"2.","answer":"B.C."},
    //    {"questionID":"3.","answer":"C、D、"}]})
	code="{\"empID\":\""+pmyInfo.userid+"\",\"examID\":\""+pexamId+"\",\"totalNum\":"+ptotalNum+
		",\"answerNum\":"+answerNum+",\"quitTimes\":"+outExamTimes+",\"duration\":"+examDuration+
		",\"optionList\":"+JSON.stringify(adata.optionList)+"}";
	console.log(code);
	//加密
	encode=aesEncryptJava(code,abc,abc);
	//alert(encode);
	var curdt=dateFtt("yyyyMMddhhmmssS",new Date());
	//alert(curdt);
	var post_json={};
	post_json["code"]=encode;
	post_json["timestamp"]=curdt;
	
	
	//提交数据
	$.ajax({
		 //"url":"testresult.html",//方法请求到
		 "url":"/examsweb/api/test/testresult",
         "data":post_json,	//传过去的数据，现在没有 null 或者直接删除掉这行
         "type":"post",//请求方式
         "dataType":"json",//数据类型
         "contentType":"application/x-www-form-urlencoded; charset=utf-8",
         "success":function(json) {
         	if(json.code=="200"){
         		
         		$("#exitModalCancle").click();
         		$("#scoresTxT").html(json.data.scores); 
         		$("#scoresModal").show();
			}else{
				alert(json.msg); 
				return false;
			}
         	
         },
         
         "error":function(error){
        	console.log(error);
        	alert("系统错误,稍后再试!"); 
			return false;
		}
    });
}

function calculationDuration(pST,pET){
	//计算时长(分钟)，
	return Math.floor(duration(pST,pET,"minus"));
	
}

function getAnswerNum(pdata){
	var examCount=0;
	console.log(pdata);
	for(var i=0;i<pdata.optionList.length;i++){
		if((pdata.optionList[i].answer!="") && (pdata.optionList[i].answer!=null)){
			examCount++;
		}
	}
	return examCount;
}
