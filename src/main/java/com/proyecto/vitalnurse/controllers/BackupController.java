package com.proyecto.vitalnurse.controllers;

import com.proyecto.vitalnurse.services.BackupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class BackupController {

    private static final Logger log = LoggerFactory.getLogger(BackupController.class);

    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @PostMapping("/forzar-backup")
    public String forzarBackupManual(RedirectAttributes attrs) {
        boolean exito = backupService.ejecutarBackupMySQL();
        if (exito) {
            attrs.addFlashAttribute("mensajeExito", "Copia de seguridad completada con exito.");
        } else {
            attrs.addFlashAttribute("error", "Error al crear la copia de seguridad.");
        }
        return "redirect:/dashboard";
    }
}
