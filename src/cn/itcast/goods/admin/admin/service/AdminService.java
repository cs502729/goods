package cn.itcast.goods.admin.admin.service;

import java.sql.SQLException;

import cn.itcast.goods.admin.admin.dao.AdminDao;
import cn.itcast.goods.admin.admin.domain.Admin;

public class AdminService {
	private AdminDao adminDao=new AdminDao();
	/*
	 * 登录共功能
	 */
	public Admin login(Admin admin){
		try {
			return adminDao.find(admin.getAdminname(), admin.getAdminpwd());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
