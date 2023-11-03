package crm_project_02.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm_project_02.config.MysqlConfig;
import crm_project_02.entity.Role;

@WebServlet(name = "userController", urlPatterns = {"/user-add"})
public class UserController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Role> list = new ArrayList<Role>();
		try {
			list = getAllRole();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Lỗi get all role " + e.getLocalizedMessage());
		}
		req.setAttribute("listRole", list);
		req.getRequestDispatcher("user-add.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		Lấy tham số từ thẻ form truyền qua khi người dùng click button submit
		String fullname = req.getParameter("fullname");
		String password = req.getParameter("password");
		String phone = req.getParameter("phone");
		String email = req.getParameter("email");
		int idRole = Integer.parseInt(req.getParameter("role"));
		
//		Tạo câu query và truyền giá trị vào câu query
		String query = "INSERT INTO Users(fullname,email,pwd,phone,id_role) \n"
				+ "VALUES(?,?,?,?,?)";
		
//		Mở kết nối tới CSDL
		Connection connection = MysqlConfig.getConection();
		try {
//			Truyền câu query vào database đã được kết nối
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, fullname);
			statement.setString(2, email);
			statement.setString(3, password);
			statement.setString(4, phone);
			statement.setInt(5, idRole);
			
			int count = statement.executeUpdate();
			
			if(count > 0) {
				//insert dữ liệu thành công
				System.out.println("Thêm Thành Công");
			}else {
				//insert dữ liệu thất bại
				System.out.println("Thêm Thất Bại");
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Lỗi thêm dữ liệu User " + e.getLocalizedMessage());
		}finally {
			try {
				connection.close();
			} catch (SQLException e) {
				System.out.println("Lỗi đóng kết nối " + e.getLocalizedMessage());
			}
		}
		
		List<Role> list = new ArrayList<Role>();
		try {
			list = getAllRole();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Lỗi get all role " + e.getLocalizedMessage());
		}
		req.setAttribute("listRole", list);
		
		req.getRequestDispatcher("user-add.jsp").forward(req, resp);

	}
	
	private List<Role> getAllRole() throws SQLException {
		//Chuẩn bị câu query
		String query = "SELECT * FROM Role";
		
		//Mở kết nối CSDL
		Connection connection = MysqlConfig.getConection();
		
		//Truyền câu query vào Connection
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		
		//Thực thi câu truy vấn và được một danh sách dữ liệu
		ResultSet resultSet = preparedStatement.executeQuery();
		List<Role> listRole = new ArrayList<Role>();
		
		//Duyệt qua từng dòng dữ liệu
		while(resultSet.next()) {
			Role role = new Role();
			role.setId(resultSet.getInt("id"));
			role.setName(resultSet.getString("name"));
			role.setDescription(resultSet.getString("description"));
			
			listRole.add(role);
		}
		
		return listRole;
	}
	
}
