package com.scrumsquad.taskmaster.controller.commands.conceptmatching;

import com.scrumsquad.taskmaster.controller.commands.Command;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptMatchingService;

import java.util.HashMap;
import java.util.Map;

public class ConceptMatchingGetDataCommand implements Command {
    @Override
    public Context execute(Context context) {
        try {
            var gameData = ConceptMatchingService.getInstance().getGameData();
            Map<String, Object> args = new HashMap<>();
            args.put("toa", gameData);
            return new Context(CommandName.conceptMatchingGetDataOk, args);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Context(CommandName.conceptMatchingGetDataKo);
        }
    }
}
