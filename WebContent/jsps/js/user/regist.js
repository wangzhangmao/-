$(function() {
	
	/*
	 * 3. 输入框得到焦点隐藏错误信息
	 */
	$(".inputClass").focus(function(){
		var labelId = $(this).attr("id") + "Error";
		$("#"+labelId).text("");
		showError($("#" + labelId));
	});
	
	/*
	 * 4. 输入框失去焦点进行校验
	 */
	$(".inputClass").blur(function(){
		var id=$(this).attr("id");
		var funName = "validate" + id.substring(0,1).toUpperCase() + id.substring(1) + "()";
		eval(funName);// 映射多个验证方法
	});
	/*
	 * 登录名校验方法
	 */
	function validateLoginname(){
		var id="loginname";
		var value=$("#"+id).val();
		// 非空验证
		if(!value){
			$("#"+id+"Error").text("用户名不能为空");
			showError($("#"+id+"Error"));
			return false;
		}
		// 长度验证
		if(value.length<3||value.length>20){
			$("#"+id+"Error").text("用户名长度必须在3 ~ 20之间");
			showError($("#"+id+"Error"));
			return false;
		}
		
		var url="/goods/UserServlet";
		var ages={"method":"ajaxValidateLoginname","loginname":value};
		$.post(url,ages,function(data){
			if(data=="false") {// 如果校验失败
				$("#" + id + "Error").text("用户名已被注册！");
				showError($("#" + id + "Error"));
				return false;
			}
		});
		return true;
	}
	
	/*
	 * 登录密码校验方法
	 */
	function validateLoginpass() {
		var id = "loginpass";
		var value = $("#" + id).val();// 获取输入框内容
		/*
		 * 1. 非空校验
		 */
		if(!value) {
			$("#" + id + "Error").text("密码不能为空！");
			showError($("#" + id + "Error"));
			return false;
		}
		/*
		 * 2. 长度校验
		 */
		if(value.length < 3 || value.length > 20) {
			$("#" + id + "Error").text("密码长度必须在3 ~ 20之间！");
			showError($("#" + id + "Error"));
			false;
		}
		return true;	
	}
	

	/*
	 * 确认密码校验方法
	 */
	function validateReloginpass() {
		var id = "reloginpass";
		var value = $("#" + id).val();// 获取输入框内容
		/*
		 * 1. 非空校验
		 */
		if(!value) {
			$("#" + id + "Error").text("确认密码不能为空！");
			showError($("#" + id + "Error"));
			return false;
		}
		if(value != $("#loginpass").val()) {
			$("#" + id + "Error").text("两次输入不一致！");
			showError($("#" + id + "Error"));
			false;
		}
		return true;	
	}
	
	function validateEmail() {
		var id="email";
		var value=$("#"+id).val();
		if(!value){
			$("#"+id+"Error").text("Email不能为空");
			showError($("#"+id+"Error"));
			return false;
		}
		
		if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(value)) {
			$("#" + id + "Error").text("错误的Email格式！");
			showError($("#" + id + "Error"));
			false;
		}
		
		var url="/goods/UserServlet";
		var ages={"method":"ajaxValidateEmail","email":value};
		$.post(url,ages,function(data){
			if(data=="false") {// 如果校验失败
				$("#" + id + "Error").text("Email已被注册！");
				showError($("#" + id + "Error"));
				return false;
			}
		});
	}
	
	function validateVerifyCode() {
		var id = "verifyCode";
		var value = $("#" + id).val();// 获取输入框内容
		/*
		 * 1. 非空校验
		 */
		if(!value) {
			$("#" + id + "Error").text("验证码不能为空！");
			showError($("#" + id + "Error"));
			return false;
		}
		if(value.length != 4) {
			$("#" + id + "Error").text("错误的验证码！");
			showError($("#" + id + "Error"));
			false;
		}
		var url="/goods/UserServlet";
		var ages={"method":"ajaxValidateVerifyCode","verifyCode":value};
		$.post(url,ages,function(data){
			if(data=="false") {// 如果校验失败
				$("#" + id + "Error").text("验证码错误！");
				showError($("#" + id + "Error"));
				return false;
			}
		});
	}
	
	

	$("#registForm").submit(function() {		
		return true;
	});
	
	/*
	 * 1. 得到所有的错误信息，循环遍历之。调用一个方法来确定是否显示错误信息！
	 */
	$(".errorClass").each(function() {
		showError($(this));// 遍历每个元素，使用每个元素来调用showError方法
	});
	
	/*
	 * 2. 切换注册按钮的图片
	 */
	$("#submitBtn").hover(
		function() {
			$("#submitBtn").attr("src", "/goods/images/regist2.jpg");
		},
		function() {
			$("#submitBtn").attr("src", "/goods/images/regist1.jpg");
		}
	);
});

/*
 * 判断当前元素是否存在内容，如果存在显示，不页面不显示！
 */
function showError(ele) {
	var text = ele.text();// 获取元素的内容
	if(!text) {// 如果没有内容
		ele.css("display", "none");// 隐藏元素
	} else {// 如果有内容
		ele.css("display", "");// 显示元素
	}
}

/*
 * 换一张验证码
 */
function _hyz() {
	/*
	 * 1. 获取<img>元素 2. 重新设置它的src 3. 使用毫秒来添加参数
	 */
	$("#imgVerifyCode").attr("src", "/goods/VerifyCodeServlet?a=" + new Date().getTime());
}
