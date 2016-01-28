package cn.itcast.goods.user.domain;

/**
 * 用户实体
 * @author Administrator
 *
 */

public class User {
	//数据库字段
	private String uid;//主键
	private String loginname;
	private String loginpass;
	private String email; 
	private boolean status;//状态，激活或未激活
	private String activiationCode;//激活码
	//注册表单字段

	private String verifyCode;//验证码
	
	//修改密码表单
	private String newpass;//新密码
	private String reloginpass;//确认密码
	
	public String getReloginpass() {
		return reloginpass;
	}
	public void setReloginpass(String reloginpass) {
		this.reloginpass = reloginpass;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public String getNewpass() {
		return newpass;
	}
	public void setNewpass(String newpass) {
		this.newpass = newpass;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getLoginpass() {
		return loginpass;
	}
	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getActiviationCode() {
		return activiationCode;
	}
	public void setActiviationCode(String activiationCode) {
		this.activiationCode = activiationCode;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String exmail) {
		this.email = exmail;
	}
	@Override
	public String toString() {
		return "User [uid=" + uid + ", loginname=" + loginname + ", loginpass="
				+ loginpass + ", status=" + status + ", activiationCode="
				+ activiationCode + "]";
	}
	
}
