package crm_project_02.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm_project_02.config.MysqlConfig;
import crm_project_02.entity.Users;

/**
 *  package :
 *  - Controller : Là nơi chứa toàn bộ file liên quan tới khai báo đường dẫn
 *  và xử lý đường dẫn
 *
 * 	Tính năng login :
 * 		Bước 1 : Lấy dữ liệu người dùng nhập vào ô email và password
 * 		Bước 2 : Viết một câu query kiểm tra email và password người dùng nhập có tồn tại trong database hay không
 * 		Bước 3 : Dùng JDBC kết nối tới CSDL và truyền câu query ở bước 2 cho CSDL thực thi
 * 		Bước 4 : Kiểm tra dữ liệu. nếu có dữ liệu thì là đăng nhập thành công và ngược lại đăng nhập thất bại
 * 
 */



@WebServlet(name = "loginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int maxAge = 8 * 60 * 60;
		Cookie cookie = new Cookie("hoten", URLEncoder.encode("Nguyễn Văn A", "UTF-8"));
		cookie.setMaxAge(maxAge);
		
		//yêu cầu client tạo cookie
		resp.addCookie(cookie);
		
		req.getRequestDispatcher("login.html").forward(req, resp);
		
	}
	//user-add
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		Bước 1
		String email = req.getParameter("email");
		String password = req.getParameter("password");
//		Bước 2
//		? : đại diện cho tham số sẽ được truyền vào khi sử dụng JDBC
		String query = "select *\n"
						+ "from Users u\n"
						+ "where u.email = ? and u.pwd = ?";
		
		//Mở kết nối tới CSDL
		Connection conn = MysqlConfig.getConection();
		try {
			//Chuẩn bị câu query cho truyền xuống CSDL thông qua PrepareStatement
			PreparedStatement statement = conn.prepareStatement(query);
			//Gán giá trị cho tham số trong câu query có dấu (?)
			statement.setString(1, email);
			statement.setString(2, password);
			
//			"select * from users u where u.email = " + email + " and u.password = " + password + "";
		
			//Thực thi câu query và lấy kết quả
			/**
			 * executeUpdate : Nếu câu query khác SELECT => INSERT, UPDATE, DELETE...
			 * executeQuery : Nếu như câu query là câu SELECT
			 */
			ResultSet resultSet = statement.executeQuery();
			List<Users> listUser = new ArrayList<Users>();
			
//			Khi nào resultSet mà còn qua dòng tiếp theo được thì làm
			while(resultSet.next()) {
				//Duyệt qua từng dòng dữ liệu query được trong database
				Users users = new Users();
//				Lấy dữ liệu từ cột duyệt được và lưu vào thuộc tính của đối tượng users
				users.setId(resultSet.getInt("id"));
				users.setEmail(resultSet.getString("email"));
				
				listUser.add(users);
			}
			
			if(listUser.size() > 0) {
				//User tồn tại , đăng nhập thành công
				System.out.println("Đăng nhập thành công");
			}else {
				//User không tồn tại, đăng nhập thất bại
				System.out.println("Đăng nhập thất bại");
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Lỗi thực thi truy vấn " + e.getLocalizedMessage());
		}
		
	}
	
}
