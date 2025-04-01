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

public class RegisterView extends View {
    private static final String backgroundPath = "/images/login-background.jpg";

    private FormInput emailField;
    private FormInput passwordField;
    private FormInput confirmPasswordField;
    private FormInput nameField;
    private FormInput surnameField;
    private JButton registerButton;
    private JButton loginButton;
    private JLabel errorResponseLabel;

    public RegisterView() {
    }

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

        // Register Form Panel
        final JPanel loginPanel = new RoundedPanel(48);
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(AppColors.secondary65);
        loginPanel.setBorder(SwingUtils.emptyBorder(64, 40));
        panel.add(loginPanel, gbc);
        final GridBagConstraints loginGbc = new GridBagConstraints();
        loginGbc.gridx = 0;
        loginGbc.gridy = GridBagConstraints.RELATIVE;
        loginGbc.fill = GridBagConstraints.HORIZONTAL;
        loginGbc.insets = new Insets(8, 0, 8, 0);

        emailField = new FormInput("Email:");
        emailField.setColumns(30);
        emailField.onSubmit(this::sendRegister);
        emailField.setValidation(this::validateEmail);

        passwordField = FormInput.password("Contraseña:");
        passwordField.setColumns(30);
        passwordField.onSubmit(this::sendRegister);
        passwordField.setValidation(this::validatePassword);

        confirmPasswordField = FormInput.password("Repetir contraseña:");
        confirmPasswordField.setColumns(30);
        confirmPasswordField.onSubmit(this::sendRegister);
        confirmPasswordField.setValidation((confirmPassword) ->
                validateConfirmPassword(passwordField.getInputText(), confirmPassword));

        final JPanel separatorPanel = new JPanel(new GridLayout(1, 1));
        separatorPanel.setOpaque(false);
        final JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBackground(AppColors.transparent);
        separator.setForeground(AppColors.text);
        separatorPanel.add(separator);

        nameField = new FormInput("Nombre:");
        nameField.setColumns(30);
        nameField.onSubmit(this::sendRegister);
        nameField.setValidation(this::validateName);

        surnameField = new FormInput("Apellido:");
        surnameField.setColumns(30);
        surnameField.setBorder(SwingUtils.emptyBorderBottom(8));
        surnameField.onSubmit(this::sendRegister);
        surnameField.setValidation(this::validateSurname);

        // Register button
        registerButton = new RoundedButton("REGISTRARSE");
        registerButton.setFont(FontUtils.lato14);
        registerButton.setBorder(SwingUtils.emptyBorder(0, 12));
        registerButton.setBackground(AppColors.primary);
        registerButton.setForeground(AppColors.primaryText);
        registerButton.addActionListener((e) -> sendRegister());

        loginPanel.add(emailField, loginGbc);
        loginPanel.add(passwordField, loginGbc);
        loginPanel.add(confirmPasswordField, loginGbc);
        loginPanel.add(separatorPanel, loginGbc);
        loginPanel.add(nameField, loginGbc);
        loginPanel.add(surnameField, loginGbc);
        loginPanel.add(registerButton, loginGbc);

        errorResponseLabel = new JLabel();
        errorResponseLabel.setBorder(SwingUtils.emptyBorderTop(16));
        errorResponseLabel.setFont(FontUtils.lato14);
        errorResponseLabel.setForeground(AppColors.error);
        errorResponseLabel.setVisible(false);
        errorResponseLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginPanel.add(errorResponseLabel, loginGbc);

        // Botón de iniciar sesión
        loginButton = new RoundedButton("INICIAR SESIÓN");
        loginButton.setFont(FontUtils.lato14);
        loginButton.setBorder(SwingUtils.emptyBorder(0, 12));
        loginButton.setBackground(AppColors.secondary);
        loginButton.setForeground(AppColors.secondaryText);
        loginButton.addActionListener((e) -> {
            Navigator.getNavigator().offTo(ViewRoutes.login);
        });

        panel.add(loginButton, gbc);

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

    private boolean validateConfirmPassword(String password, String confirmPassword) {
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            confirmPasswordField.setErrorLabelText("* Campo obligatorio");
            return false;
        }
        if (password == null || !password.equals(confirmPassword)) {
            confirmPasswordField.setErrorLabelText("* Las contraseñas no coinciden");
            return false;
        }
        return true;
    }

    private boolean validateName(String name) {
        if (name == null || name.isEmpty()) {
            nameField.setErrorLabelText("* Campo obligatorio");
            return false;
        }
        return true;
    }

    private boolean validateSurname(String surname) {
        if (surname == null || surname.isEmpty()) {
            surnameField.setErrorLabelText("* Campo obligatorio");
            return false;
        }
        return true;
    }

    public boolean validateRegister(String email, String password, String confirmPassword, String name, String surname) {
        boolean r = true;
        if (!validateEmail(email)) {
            emailField.requestFocus();
            r = false;
        }
        if (!validatePassword(password)) {
            if (r) passwordField.requestFocus();
            r = false;
        }
        if (!validateConfirmPassword(password, confirmPassword)) {
            if (r) passwordField.requestFocus();
            r = false;
        }
        if (!validateName(name)) {
            if (r) nameField.requestFocus();
            r = false;
        }
        if (!validateSurname(surname)) {
            if (r) surnameField.requestFocus();
            r = false;
        }
        return r;
    }

    private boolean isLoginEnabled() {
        return registerButton.isEnabled();
    }

    private void setLoginEnabled(boolean enabled) {
        registerButton.setEnabled(enabled);
        loginButton.setEnabled(enabled);
    }

    private void sendRegister() {
        if (!isLoginEnabled()) return;
        setLoginEnabled(false);
        errorResponseLabel.setText(null);
        errorResponseLabel.setVisible(false);
        String email = emailField.getInputText();
        String password = passwordField.getInputText();
        String confirmPassword = confirmPasswordField.getInputText();
        String name = nameField.getInputText();
        String surname = surnameField.getInputText();
        final String emailSanitized = email != null ? email.trim() : "";
        // No se hace trim() a password, hay que respetar cada caracter
        // introducido por el usuario en la contraseña
        final String passwordSanitized = password != null ? password : "";
        final String confirmPasswordSanitized = confirmPassword != null ? confirmPassword : "";
        final String nameSanitized = name != null ? name.trim() : "";
        final String surnameSanitized = surname != null ? surname.trim() : "";
        boolean isValid = validateRegister(
                emailSanitized,
                passwordSanitized,
                confirmPasswordSanitized,
                nameSanitized,
                surnameSanitized);
        if (!isValid) {
            setLoginEnabled(true);
            return;
        }
        final Map<String, Object> args = new HashMap<>();
        args.put("email", emailSanitized);
        args.put("password", passwordSanitized);
        args.put("name", nameSanitized);
        args.put("surname", surnameSanitized);
        AppController.getInstance().action(new Context(CommandName.register, args));
    }

    @Override
    public void update(Context ctx) {
        if (ctx == null) return;
        switch (ctx.getCommandName()) {
            case CommandName.registerOk -> {
                setLoginEnabled(true);
                // TODO lógica para enviar a la vista de alumno
                Navigator.getNavigator().offTo(ViewRoutes.student);
            }
            case CommandName.registerKo -> {
                setLoginEnabled(true);
                String msg = "Ocurrió un error al iniciar sesión";
                if (ctx.getArguments() != null) {
                    if (ctx.getArguments().containsKey("emailTaken")) {
                        msg = "El correo electrónico ya está en uso";
                    }
                }
                errorResponseLabel.setText(msg);
                errorResponseLabel.setVisible(true);
                emailField.requestFocus();
            }
        }
    }
}
