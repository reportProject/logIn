package net.skhu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProfessorController {

	@RequestMapping("professor/index")
    public String index() {
        return "professor/index"; //로그인 professor 사용자를 위한 첫 페이지 URL
    }
}
