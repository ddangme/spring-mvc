package hello.itemservice.web.controller.converter;

import hello.itemservice.web.converter.type.IpPort;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/converter")
public class ConverterController {

    @GetMapping("/converter-view")
    public String converterView(Model model) {
        model.addAttribute("number", 10_000);
        model.addAttribute("ipPort", new IpPort("127.0.0.1", 8080));

        return "converter/converter-view";
    }

    @GetMapping("/edit")
    public String converterForm(Model model) {
        IpPort ipPort = new IpPort("127.0.0.1", 8080);
        Form form = new Form(ipPort);

        model.addAttribute("form", form);
        return "converter/converter-form";
    }

    @PostMapping("/edit")
    public String converterEdit(@ModelAttribute Form form, Model model) {
        IpPort ipPort = form.getIpPort();

        model.addAttribute("ipPort", ipPort);
        return "converter/converter-view";
    }

    @Data
    @AllArgsConstructor
    static class Form {
        private IpPort ipPort;
    }
}
