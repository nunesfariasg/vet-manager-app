// LoginFrame.java
package br.com.sistema.view;

import br.com.sistema.dao.UserDAO;
import br.com.sistema.model.User;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField txtUsername = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);

    public LoginFrame() {
        setTitle("VETMANAGER - Login");
        setSize(380, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(new JLabel("VETMANAGER", SwingConstants.CENTER), c);

        c.gridwidth = 1;
        c.gridy++;
        c.gridx = 0;
        panel.add(new JLabel("Usuário:"), c);
        c.gridx = 1;
        panel.add(txtUsername, c);

        c.gridy++;
        c.gridx = 0;
        panel.add(new JLabel("Senha:"), c);
        c.gridx = 1;
        panel.add(txtPassword, c);

        JButton btnLogin = new JButton("Entrar");
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        panel.add(btnLogin, c);

        btnLogin.addActionListener(e -> login());
        txtPassword.addActionListener(e -> login());

        add(panel);
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Informe o usuário e a senha.");
            return;
        }

        try {
            User user = new UserDAO().findByCredentials(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(this,
                        "Usuário ou senha inválidos.");
                return;
            }

            new MainFrame().setVisible(true);
            dispose();

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao realizar login: " + ex.getMessage());
        }
    }
}