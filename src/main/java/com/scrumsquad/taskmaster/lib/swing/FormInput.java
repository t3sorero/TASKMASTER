package com.scrumsquad.taskmaster.lib.swing;

import com.scrumsquad.taskmaster.lib.FontUtils;
import com.scrumsquad.taskmaster.views.AppColors;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class FormInput extends JPanel {

    private final JLabel errorLabel;
    private final JTextField input;

    public FormInput(String labelText) {
        this(new JTextField(), labelText);
    }

    private FormInput(JTextField input, String labelText) {
        if (input == null) throw new IllegalArgumentException();
        setLayout(new BorderLayout(0, 2));
        setOpaque(false);
        this.input = input;
        JLabel inputLabel = new JLabel(labelText);
        inputLabel.setLabelFor(input);
        inputLabel.setFont(FontUtils.lato14);
        input.setFont(FontUtils.default14);
        this.errorLabel = new JLabel();
        this.errorLabel.setFont(FontUtils.lato12);
        this.errorLabel.setForeground(AppColors.error);
        JPanel topPanel = new JPanel(new BorderLayout(8, 0));
        topPanel.setOpaque(false);
        topPanel.add(inputLabel, BorderLayout.WEST);
        topPanel.add(errorLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        add(input, BorderLayout.SOUTH);
    }

    public static FormInput password(String labelText) {
        return new FormInput(new JPasswordField(), labelText);
    }

    public void setColumns(int columns) {
        input.setColumns(columns);
    }

    public String getInputText() {
        if (input instanceof JPasswordField) {
            return new String(((JPasswordField) input).getPassword());
        }
        return input.getText();
    }

    public void onSubmit(OnSubmitListener listener) {
        input.addActionListener((e) -> {
            listener.onSubmit();
        });
    }

    public void setErrorLabelText(String text) {
        errorLabel.setText(text);
    }

    private FocusAdapter focusListener;
    private DocumentListener inputChangeListener;

    public void setValidation(Validation validation) {
        if (focusListener != null && inputChangeListener != null) {
            input.removeFocusListener(focusListener);
            input.getDocument().removeDocumentListener(inputChangeListener);
        }
        if (validation == null) return;
        focusListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                final boolean isValid = validation.isValid(getInputText());
                if (isValid) {
                    errorLabel.setText(null);
                }
                errorLabel.setVisible(!isValid);
            }
        };
        input.addFocusListener(focusListener);
        inputChangeListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                final boolean isValid = validation.isValid(getInputText());
                if (isValid) {
                    errorLabel.setText(null);
                }
                errorLabel.setVisible(!isValid);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                final boolean isValid = validation.isValid(getInputText());
                if (isValid) {
                    errorLabel.setText(null);
                }
                errorLabel.setVisible(!isValid);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                final boolean isValid = validation.isValid(getInputText());
                if (isValid) {
                    errorLabel.setText(null);
                }
                errorLabel.setVisible(!isValid);
            }
        };
        input.getDocument().addDocumentListener(inputChangeListener);
    }

    public interface Validation {
        boolean isValid(String text);
    }

    public interface OnSubmitListener {
        void onSubmit();
    }

    @Override
    public void requestFocus() {
        input.requestFocus();
    }
}
