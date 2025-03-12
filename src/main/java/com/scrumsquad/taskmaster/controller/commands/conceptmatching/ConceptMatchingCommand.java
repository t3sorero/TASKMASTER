package com.scrumsquad.taskmaster.controller.commands.conceptmatching;

import com.scrumsquad.taskmaster.controller.commands.Command;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptMatchingService;

import java.util.Map;
import java.util.Set;

public class ConceptMatchingCommand implements Command {
    private final ConceptMatchingService conceptMatchingService;

    public ConceptMatchingCommand(ConceptMatchingService service) {
        this.conceptMatchingService = service;
    }

    @Override
    public Context execute(Context context) {
        try {
            // Verificar si el contexto tiene respuestas del usuario
            if (context.getArguments() != null && !context.getArguments().containsKey("userAnswers")) {
                throw new IllegalArgumentException("No se han recibido respuestas del usuario.");
            }

            // Obtener respuestas enviadas por el usuario
            Map<Integer, Integer> userAnswers = (Map<Integer, Integer>) context.getArguments().get("userAnswers");
            Set<Integer> conceptsInd = (Set<Integer>) context.getArguments().get("conceptosId");

            // Validar respuestas con el servicio
            Map<Integer, Boolean> results = conceptMatchingService.checkAnswers(userAnswers, conceptsInd);
            // Crear un nuevo contexto con los resultados
            Context responseContext = new Context(CommandName.conceptMatchingOk);
            responseContext.setArgument("correctAnswers", results);

            return responseContext;
        } catch (Exception e) {
            Context responseContext = new Context(CommandName.conceptMatchingKo);
            responseContext.setArgument("error", true);

            return responseContext;
        }
    }
}
