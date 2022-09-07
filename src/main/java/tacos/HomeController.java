package tacos;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller            // <1>
public class HomeController {

  @GetMapping("/")     // <2>
  public String home() {
	  System.out.println("in the mapping controller");
    return "home";     // <3>
  }

}
