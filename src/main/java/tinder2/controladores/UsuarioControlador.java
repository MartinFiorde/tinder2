package tinder2.controladores;

import java.util.List;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import sun.util.logging.PlatformLogger;
import tinder2.entidades.Foto;
import tinder2.errores.ErrorServicio;
import tinder2.entidades.Usuario;
import tinder2.entidades.Zona;
import tinder2.repositorios.UsuarioRepositorio;
import tinder2.repositorios.ZonaRepositorio;
import tinder2.servicios.FotoServicio;
import tinder2.servicios.UsuarioServicio;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    private UsuarioRepositorio usuarioRepositorio;
    private UsuarioServicio usuarioServicio;
    private ZonaRepositorio zonaRepositorio;
    
    @Autowired
    public UsuarioControlador(UsuarioRepositorio usuariorepositorio, UsuarioServicio usuarioServicio, ZonaRepositorio zonaRepositorio) {
        this.usuarioRepositorio = usuariorepositorio;
        this.usuarioServicio = usuarioServicio;
        this.zonaRepositorio = zonaRepositorio;
    }

    @GetMapping
    public String listarUsuarios(ModelMap model) {
        List<Usuario> usuarios = usuarioRepositorio.findAll();
        model.addAttribute("usuarios", usuarios);
        return "usuario_t/lista_t.html";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(ModelMap model) {
        model.addAttribute("usuario", new Usuario());
        List<Zona> zonas = zonaRepositorio.findAll();
        model.addAttribute("zonas", zonas);
        return "usuario_t/registro_t.html";
    }

    @PostMapping("/registro")
    public String guardarUsuario(@ModelAttribute Usuario usuario, ModelMap model, MultipartFile foto2, @RequestParam String clave2, String idZona2) {
        try {
            System.out.println("\n"+"nombre: " + usuario.getNombre());
            System.out.println("apellido: " + usuario.getApellido());
            System.out.println("mail: " + usuario.getMail());
            System.out.println("clave: " + usuario.getClave());
            System.out.println("clave2: "+ clave2);
            usuarioServicio.registrar(usuario.getNombre(), usuario.getApellido(), usuario.getMail(), usuario.getClave(), clave2, idZona2, foto2);
            model.put("titulo","Bienvenido a Tinder de mascotas!");
            model.put("descripcion","Tu usuario fue registrado de manera satisfactoria");
            return "exito_t.html";
        } catch (Exception ex) {
            model.put("error", ex.getMessage().toString());
            model.put("nombre", usuario.getNombre());
            model.put("apellido", usuario.getApellido());
            model.put("mail", usuario.getMail());
            model.put("clave", usuario.getClave());
            model.addAttribute("error", ex.getMessage());
            List<Zona> zonas = zonaRepositorio.findAll();
            model.addAttribute("zonas", zonas);
            System.out.println("\n" + ex + "\n");
//            Logger.getLogger(UsuarioControlador.class.getName()).log(Level.FATAL, null, ex);
            return "/usuario_t/registro_t.html";
        }
    }

    @GetMapping("/error")
    public String error(ModelMap model) {
        return "usuario_t/error_t.html";
    }
    
    
    
    
    
    
}
