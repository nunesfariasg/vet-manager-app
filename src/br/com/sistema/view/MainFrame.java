// MainFrame.java
package br.com.sistema.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("VETMANAGER - Sistema de Clínica Veterinária");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();

        JMenu menuCadastros = new JMenu("Cadastros");
        JMenuItem itemClientes = new JMenuItem("Clientes");
        JMenuItem itemAnimais = new JMenuItem("Animais");

        itemClientes.addActionListener(e ->
                new ClientFrame().setVisible(true));

        itemAnimais.addActionListener(e ->
                new AnimalFrame().setVisible(true));

        menuCadastros.add(itemClientes);
        menuCadastros.add(itemAnimais);

        JMenu menuConsultas = new JMenu("Consultas");
        JMenuItem itemConsultas = new JMenuItem("Gerenciar consultas");

        itemConsultas.addActionListener(e ->
                new AppointmentFrame().setVisible(true));

        menuConsultas.add(itemConsultas);

        JMenu menuSistema = new JMenu("Sistema");
        JMenuItem itemSair = new JMenuItem("Sair");

        itemSair.addActionListener(e -> {
            int resposta = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente sair do sistema?",
                    "Confirmar saída",
                    JOptionPane.YES_NO_OPTION
            );

            if (resposta == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        menuSistema.add(itemSair);

        menuBar.add(menuCadastros);
        menuBar.add(menuConsultas);
        menuBar.add(menuSistema);

        setJMenuBar(menuBar);

        JLabel title = new JLabel(
                "VETMANAGER",
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel subtitle = new JLabel(
                "Sistema de Gerenciamento de Clínica Veterinária",
                SwingConstants.CENTER
        );

        JPanel center = new JPanel(new GridLayout(2, 1, 5, 5));
        center.add(title);
        center.add(subtitle);

        add(center, BorderLayout.CENTER);
    }
}