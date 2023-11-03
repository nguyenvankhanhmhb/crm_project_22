package crm_project_02.entity;
//Entity : Là nơi khai báo ra các class đặt tên và thuộc tính giống với lại tên bảng trong database
public class Users {
	private int id;
	private String email;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private Role role;
	private String fullName;
	private String image;
	private String phone;
	private int idRole;
	private List<Job> hasntStartedJobs;
	private List<Job> startingJobs;
	private List<Job> startedJobs;
	private String roleName;
	
	
}
