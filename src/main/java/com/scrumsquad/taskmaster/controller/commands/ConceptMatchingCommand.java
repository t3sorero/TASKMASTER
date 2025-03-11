package com.scrumsquad.taskmaster.controller.commands;

import com.scrumsquad.taskmaster.services.ConceptMatchingService;

import java.util.Map;

public class ConceptMatchingCommand implements Command {
    private final ConceptMatchingService conceptMatchingService;

    public ConceptMatchingCommand(ConceptMatchingService service) {
        this.conceptMatchingService = service;
    }

    @Override
    public Context execute(Context context) {
        // Verificar si el contexto tiene respuestas del usuario
        if (!context.getArguments().containsKey("userAnswers")) {
            throw new IllegalArgumentException("No se han recibido respuestas del usuario.");
        }

        // Obtener respuestas enviadas por el usuario
        Map<String, String> userAnswers = (Map<String, String>) context.getArguments().get("userAnswers");

        // Validar respuestas con el servicio
        int[] results = conceptMatchingService.checkAnswers(userAnswers);
        int correct = results[0]; //NUMERO DE RESPUESTAS CORRECTAS
        int incorrect = results[1]; //NUMERO DE RESPUESTAS INCORRECTAS

        // Crear un nuevo contexto con los resultados
        Context responseContext = new Context(CommandName.conceptMatching);
        responseContext.setArgument("correctAnswers", correct);
        responseContext.setArgument("incorrectAnswers", incorrect);

        return responseContext;
    }
}
