$(function(){
	
	$("#submit").click(
		function(){
			var username=$("#username").val();
			var password=$("#password").val();
			var validcode=$("#validcode").val();
			//检查参数是否为空
			var aa=validcheck();
			if(aa!=null){
				$('#tipmsg').html(aa);
				$('#tipdiv').css("display","");
				$("#tipdiv").show();
				return false;
			}
			//检查特殊字符
			if(checkChar(username)!=username){
				$('#tipmsg').html("用户名中有特殊字符！");
				$('#tipdiv').css("display","");
				$("#tipdiv").show();
				return false;
			};
			if(checkChar(validcode)!=validcode){
				$('#tipmsg').html("验证码中有特殊字符！");
				$('#tipdiv').css("display","");
				$("#tipdiv").show();
				return false;
			};
			//参数
			var code="";
			//dec({"userName"：""，""password":"","validcode":""})
			if(validcode==null||validcode==""){
				code="{\"userName\":\""+username+"\",\"password\":\""+password+"\",\"validcode\":\"\"}";
			}else{
				code="{\"userName\":\""+username+"\",\"password\":\""+password+"\",\"validcode\":\""+validcode+"\"}";
			}
			//alert(code);
//console.log(code);
			//加密
			encode=aesEncryptJava(code,abc,abc);
			//alert(encode);
//console.log(encode);
			var curdt=dateFtt("yyyyMMddhhmmssS",new Date());
			//alert(curdt);
			var post_json={};
			post_json["code"]=encode;
			post_json["timestamp"]=curdt;
			
			//提交
			$.ajax({
	    		//"url":"login-suc.html",//方法请求到/emp/logint路径
				//"url":"login-err.html",
				"url":"/examsweb/api/emp/login",
	             "data":post_json,	//传过去的数据，现在没有 null 或者直接删除掉这行
	             "type":"post",//请求方式
	             "dataType":"json",//数据类型
	             "contentType":"application/x-www-form-urlencoded; charset=utf-8",
	             "success":function(json) {
	            	//console.log(json);
	             	var data=json;
	             	//console.log(data);
	             	if(data.code=="200"){
	             		//写cookies
	             		//alert("userid="+data.data.empID+";"+"username="+data.data.userName+";"+"empName="+data.data.empName+";"+"deptID="+data.data.deptID+";"+"deptname="+data.data.deptName+";"+"empImg="+data.data.empImg+";");
	             		
	             		setCookie("userid",data.data.empID,null);
	             		setCookie("username",data.data.userName,null);
	             		setCookie("realname",data.data.empName,null);
	             		setCookie("deptId",data.data.deptID,null);
	             		setCookie("deptname",data.data.deptName,null);
	             		setCookie("userimg",data.data.empImg,null);
	             		setCookie("logintimes",0,null);
	             		//alert("go index.html");
	             		//window.location.href='index.html';
	             		window.location.href='/examsweb/portal/unStudy.html';
	    			}else{
	    				//console.log(data);
    					var logintimes=getCookie("logintimes");
    					if(logintimes==null||logintimes==""){
    						logintimes=1;
    					}else{
    						logintimes=parseInt(logintimes) +1;
    					}
    					document.cookie="logintimes="+logintimes+";";
	    				if(logintimes>3){
	    					//获取验证码
	    					$("#validimg").attr("src","/examsweb/captcha.jpg"+'?'+Math.random());
	    					$("#validimg").click();
	    					$("#validdiv").css("display","");
	    				}
	    				//显示错误提示
	    				$('#tipmsg').html(data.msg);
    					$('#tipdiv').css("display","");
    					$("#tipdiv").show();
	    				return false;
	    			}
	             },
	             
	             "error":function(error){
	            	//console.log(error);
	            	 if(error.status=="200"){
	            		 $('#tipmsg').html("返回数据格式不符合json格式.");
	            	 }else{
	            		 $('#tipmsg').html("错误代码:"+error.status+"  错误描述:"+error.statusText);
	            	 }
	    			
	    			$('#tipdiv').css("display","");
	    			$("#tipdiv").show();
	    			
	    			return false;
	    			
	             }
	            });
		
		}
	);
	
	$("#validimg").click(
		function(){
			//alert("sssss");
			//获取验证码
			$("#validimg").attr("src","/examsweb/captcha.jpg"+'?sn='+Math.random());
		}
	);
});

function checkChar(data){
	var pattern = new RegExp("[`~!@#$^%_+&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）—|{}【】‘；：”“'。，、？]");
	var rs = "";
    for (var i = 0; i < data.length; i++) {
        rs = rs + data.substr(i, 1).replace(pattern, '');
    }
    rs = rs.replace(/script/g, '');
    return rs;
    
}
function validcheck(){
	if($("#username").val()==null||$("#username").val()=="") return "用户名不能为空!";
	if($("#password").val()==null||$("#password").val()=="") return "密码不能为空!";
	if(($("#validdiv").css("display")=="block" ||$("#validdiv").css("display")=="") && ($("#validcode").val()==null||$("#validcode").val()=="")) return "验证码不能为空!";
	return null;
}

