package tinder2.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainControlador {
    
    @GetMapping
    public String index(){
        return "index_t.html";
    }
    
    @GetMapping("/exito")
    public String exito(ModelMap model){
        return "exito_t.html";
    }
}
