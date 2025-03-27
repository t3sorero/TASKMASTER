package com.scrumsquad.taskmaster.controller.commands.teoria;

import com.scrumsquad.taskmaster.controller.commands.Command;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.services.teoria.GetTeoriaService;

public class GetTeoriaCommand implements Command {
    @Override
    public Context execute(Context ctx) {
        try {
            var teoria = GetTeoriaService.getInstance().getTeoria((int) ctx.getArguments().get("tema"));
            if (teoria == null) {
                return new Context(CommandName.teoriaGetDataKo, ctx.getArguments());
            }
            ctx.getArguments().put("teoria", teoria);
            return new Context(CommandName.teoriaGetDataOk, ctx.getArguments());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Context(CommandName.teoriaGetDataKo);
        }
    }
}
