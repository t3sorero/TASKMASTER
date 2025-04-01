package com.scrumsquad.taskmaster.views.auth;

import com.scrumsquad.taskmaster.controller.AppController;
import com.scrumsquad.taskmaster.controller.Navigator;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.lib.*;
import com.scrumsquad.taskmaster.lib.swing.FormInput;
import com.scrumsquad.taskmaster.lib.swing.ImagePanel;
import com.scrumsquad.taskmaster.lib.swing.RoundedButton;
import com.scrumsquad.taskmaster.lib.swing.RoundedPanel;
import com.scrumsquad.taskmaster.views.AppColors;
import com.scrumsquad.taskmaster.views.ViewRoutes;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LoginView extends View {
    private static final String backgroundPath = "/images/login-background.jpg";

    private FormInput emailField;
    private FormInput passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel errorResponseLabel;

    @Override
    public JPanel build(BuildOptions options) {
        final JPanel panel = new ImagePanel(backgroundPath);
        panel.setLayout(new GridBagLayout());
        panel.setBackground(AppColors.background);
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0); // Padding

        // Login Form Panel
        final JPanel loginPanel = new RoundedPanel(48);
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(AppColors.secondary65);
        loginPanel.setBorder(SwingUtils.emptyBorder(64, 40));
        panel.add(loginPanel, gbc);
        final GridBagConstraints loginGbc = new GridBagConstraints();
        loginGbc.gridx = 0;
        loginGbc.gridy = GridBagConstraints.RELATIVE;
        loginGbc.fill = GridBagConstraints.HORIZONTAL;

        emailField = new FormInput("Email:");
        emailField.setColumns(30);
        emailField.setBorder(SwingUtils.emptyBorderBottom(16));
        passwordField = FormInput.password("Contraseña:");
        passwordField.setColumns(30);
        passwordField.setBorder(SwingUtils.emptyBorderBottom(24));

        emailField.onSubmit(this::sendLogin);
        emailField.setValidation(this::validateEmail);
        passwordField.onSubmit(this::sendLogin);
        passwordField.setValidation(this::validatePassword);

        // Login button
        loginButton = new RoundedButton("INICIAR SESIÓN");
        loginButton.setFont(FontUtils.lato14);
        loginButton.setMargin(new Insets(16, 0, 0, 0));
        loginButton.setBorder(SwingUtils.emptyBorder(0, 12));
        loginButton.setBackground(AppColors.primary);
        loginButton.setForeground(AppColors.primaryText);
        loginButton.addActionListener((e) -> {
            sendLogin();
        });
        errorResponseLabel = new JLabel();
        errorResponseLabel.setBorder(SwingUtils.emptyBorderTop(16));
        errorResponseLabel.setFont(FontUtils.lato14);
        errorResponseLabel.setForeground(AppColors.error);
        errorResponseLabel.setVisible(false);
        errorResponseLabel.setHorizontalAlignment(SwingConstants.CENTER);

        loginPanel.add(emailField, loginGbc);
        loginPanel.add(passwordField, loginGbc);
        loginPanel.add(loginButton, loginGbc);
        loginPanel.add(errorResponseLabel, loginGbc);

        // Botón de registro
        registerButton = new RoundedButton("REGISTRARSE");
        registerButton.setFont(FontUtils.lato14);
        registerButton.setBorder(SwingUtils.emptyBorder(0, 12));
        registerButton.setBackground(AppColors.secondary);
        registerButton.setForeground(AppColors.secondaryText);
        registerButton.addActionListener((e) -> {
            Navigator.getNavigator().offTo(ViewRoutes.register);
        });

        panel.add(registerButton, gbc);

        emailField.requestFocus();

        return panel;
    }

    private boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            emailField.setErrorLabelText("* Campo obligatorio");
            return false;
        }
        if (!CommonUtils.isEmail(email.trim())) {
            emailField.setErrorLabelText("* Se ha introducido un email inválido");
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            passwordField.setErrorLabelText("* Campo obligatorio");
            return false;
        }
        return true;
    }

    public boolean validateLogin(String email, String password) {
        boolean r = true;
        if (!validateEmail(email)) {
            emailField.requestFocus();
            r = false;
        }
        if (!validatePassword(password)) {
            if (r) passwordField.requestFocus();
            r = false;
        }
        return r;
    }

    private boolean isLoginEnabled() {
        return loginButton.isEnabled();
    }

    private void setLoginEnabled(boolean enabled) {
        loginButton.setEnabled(enabled);
        registerButton.setEnabled(enabled);
    }

    private void sendLogin() {
        if (!isLoginEnabled()) return;
        setLoginEnabled(false);
        errorResponseLabel.setText(null);
        errorResponseLabel.setVisible(false);
        String email = emailField.getInputText();
        String password = passwordField.getInputText();
        final String emailSanitized = email != null ? email.trim() : "";
        // No se hace trim() a password, hay que respetar cada caracter
        // introducido por el usuario en la contraseña
        final String passwordSanitized = password != null ? password : "";
        boolean isValid = validateLogin(emailSanitized, passwordSanitized);
        if (!isValid) {
            setLoginEnabled(true);
            return;
        }
        final Map<String, Object> args = new HashMap<>();
        args.put("email", emailSanitized);
        args.put("password", passwordSanitized);
        AppController.getInstance().action(new Context(CommandName.login, args));
    }

    @Override
    public void update(Context ctx) {
        if (ctx == null) return;
        switch (ctx.getCommandName()) {
            case CommandName.loginOk -> {
                setLoginEnabled(true);
                // TODO lógica para enviar a la vista de profesor o alumno
                Navigator.getNavigator().to(ViewRoutes.student);
            }
            case CommandName.loginKo -> {
                setLoginEnabled(true);
                String msg = "Ocurrió un error al iniciar sesión";
                if (ctx.getArguments() != null) {
                    if (ctx.getArguments().containsKey("credentials")) {
                        msg = "Usuario o contraseña incorrectos";
                    }
                }
                errorResponseLabel.setText(msg);
                errorResponseLabel.setVisible(true);
                emailField.requestFocus();
            }
        }
    }
}
