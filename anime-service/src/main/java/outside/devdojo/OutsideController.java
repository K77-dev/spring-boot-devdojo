package outside.devdojo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OutsideController {

    @GetMapping("outside")
    public String outside() {
        return "Outside Controller DevDojo!";
    }
}
