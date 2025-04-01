package com.scrumsquad.taskmaster.controller.commands.login;

import com.scrumsquad.taskmaster.controller.commands.Command;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.services.login.LoginService;

public class StudentLoginCommand implements Command {
    @Override
    public Context execute(Context ctx) {
        try {
            var userAnswer = ctx.getArguments();
            if (!userAnswer.containsKey("email")) {
                throw new Exception("Falta el campo email");
            }
            if (!userAnswer.containsKey("password")) {
                throw new Exception("Falta el campo contraseña");
            }

            boolean valid = LoginService.getInstance().loginValidation(
                    (String) userAnswer.get("email"),
                    (String) userAnswer.get("password")
            );

            if (valid) {
                return new Context(CommandName.loginOk);
            } else {
                Context errorCtx = new Context(CommandName.loginKo);
                errorCtx.setArgument("credentials", true);  // <== Aquí se indica que el fallo fue por credenciales incorrectas
                return errorCtx;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Context(CommandName.loginKo); // No se añade "credentials" => la vista mostrará "Ocurrió un error"
        }
    }
}
