package com.scrumsquad.taskmaster.controller.commands.login;

import com.scrumsquad.taskmaster.controller.commands.Command;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import services.login.LoginService;

public class StudentLoginCommand implements Command {

    @Override
    public Context execute(Context ctx) {
        try{
            var userAnswer = ctx.getArguments();
            if (!userAnswer.containsKey("email")) {
                throw new Exception();
            }
            if (!userAnswer.containsKey("password")) {
                throw new Exception();
            }

            var valid = LoginService.getInstance().loginValidation((String) userAnswer.get("email"),(String) userAnswer.get("password"));
            if(valid)
                return new Context(CommandName.loginOk);
            else
                return new Context(CommandName.loginKo);
        } catch (Exception e) {
            e.printStackTrace();
            return new Context(CommandName.loginKo);
        }
    }
}
