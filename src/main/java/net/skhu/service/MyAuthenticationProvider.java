package net.skhu.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import net.skhu.domain.Professor;
import net.skhu.domain.Student;
import net.skhu.domain.Ta;

/*MyAuthenticationProvier 클래스는
 
사용자가 입력한 로그인 아이디와 비밀번호를 검사할 때 사용되는 클래스이다.*/
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	StudentService studentService;
	@Autowired
	ProfessorService professorService;
	@Autowired
	TaService taService;

	/* 사용자가 입력한 로그인 아이디와 비밀번호가 authenticate 메소드의 파라미터로 전달된다. */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String loginId = authentication.getName();
		String passwd = authentication.getCredentials().toString();
		return authenticate(loginId, passwd);
	}

	public Authentication authenticate(String loginId, String password) throws AuthenticationException {
		Student student = studentService.login(loginId, password);
		Professor professor = professorService.login(loginId, password);
		Ta ta = taService.login(loginId, password);

		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		String role = "";
		
		if (student != null) {
			role = "ROLE_STUDENT";
		}else if(professor != null) {
			role = "ROLE_PROFESSOR";
		}else if(ta != null) {
			role = "ROLE_TA";
		}else {
			return null;
		}
//		List<Student> studentList=new ArrayList<>();
//		List<Professor> professorList=new ArrayList<>();
//		List<Ta> taList=new ArrayList<>();
//		for(Object check : studentList) {
//			if(check==student) {
//				role = "ROLE_STUDENT";
//				break;
//			}
//		}
//		for(Object check : professorList) {
//			if(check==professor) {
//				role = "ROLE_PROFESSOR";
//				break;
//			}
//		}
//		for(Object check : taList) {
//			if(check==ta) {
//				role = "ROLE_TA";
//				break;
//			}
//		}

		

		// switch (user.getUserType()) {
//		case "관리자":
//			role = "ROLE_ADMIN";
//			break;
//		case "교수":
//			role = "ROLE_PROFESSOR";
//			break;
//		case "학생":
//			role = "ROLE_STUDENT";
//			break;
//		case "TA":
//			role = "ROLE_TA";
//			break;
//		}
		grantedAuthorities.add(new SimpleGrantedAuthority(role));
		return new MyAuthenticaion(loginId, password, grantedAuthorities, student, professor, ta);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	public class MyAuthenticaion extends UsernamePasswordAuthenticationToken {
		private static final long serialVersionUID = 1L;
		Student student;
		Professor professor;
		Ta ta;

		public MyAuthenticaion(String loginId, String password, List<GrantedAuthority> grantedAuthorities,
				Student student, Professor professor, Ta ta) {
			super(loginId, password, grantedAuthorities);
			this.student = student;
			this.professor = professor;
			this.ta = ta;
		}

		public Student getStudent() {
			return student;
		}

		public void setStudent(Student student) {
			this.student = student;
		}

		public Professor getProfessor() {
			return professor;
		}

		public void setProfessor(Professor professor) {
			this.professor = professor;
		}

		public Ta getTa() {
			return ta;
		}

		public void setTa(Ta ta) {
			this.ta = ta;
		}

	}
}