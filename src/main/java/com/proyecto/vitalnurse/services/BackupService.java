package com.proyecto.vitalnurse.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RestController
public class BackupService {

    @Scheduled(cron = "0 0 2 * * ?")
    public void tareaAutomaticaBackup() {
        System.out.println(">>> Iniciando copia de seguridad automática de madrugada...");
        ejecutarBackupMySQL();
    }

    @GetMapping("/forzar-backup")
    public String forzarBackupManual() {
        boolean exito = ejecutarBackupMySQL();
        return exito ? "¡Copia de seguridad RNF8 completada con éxito! Revisa la carpeta del proyecto."
                     : "Error al crear la copia. Revisa la consola de IntelliJ.";
    }

    private boolean ejecutarBackupMySQL() {
        try {
            File carpeta = new File("backups_seguridad");
            if (!carpeta.exists()) {
                carpeta.mkdir();
            }

            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String nombreArchivo = "backups_seguridad/vitalnurse_backup_" + fecha + ".sql";
            String rutaMysqldump = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump";
            String comando = "cmd.exe /c \"\"" + rutaMysqldump + "\" -u root -padmin vitalnurse_db > " + nombreArchivo + "\"";

            Process proceso = Runtime.getRuntime().exec(comando);
            int resultado = proceso.waitFor();

            if (resultado == 0) {
                System.out.println(">>> ÉXITO: Respaldo creado en: " + nombreArchivo);
                return true;
            } else {
                System.out.println(">>> ERROR: Falló el comando de mysqldump.");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
