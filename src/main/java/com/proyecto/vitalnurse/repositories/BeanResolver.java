package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.entity.clinical.EvaluacionResultado;
import com.proyecto.vitalnurse.entity.seguridad.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class BeanResolver {

    @Autowired
    private EvaluacionResultadoRepository injectedEvalResultRepo;

    @Autowired
    private UsuarioRepository injectedUsuarioRepo;

    public static EvaluacionResultadoRepository evaluacionResultadoRepo;
    public static UsuarioRepository usuarioRepo;

    @PostConstruct
    private void init() {
        evaluacionResultadoRepo = injectedEvalResultRepo;
        usuarioRepo = injectedUsuarioRepo;
    }
}
