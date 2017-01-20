package ch.heigvd.gamification.config;

import ch.heigvd.gamification.util.URIs;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(URIs.DOCUMENTATION)
public class SwaggerUIController {

    @RequestMapping(method = RequestMethod.GET)
	public String documentation() {
		System.out.println("swagger-ui.html");
		return "redirect:swagger-ui.html";
	}
}
