// ClientFrame.java
package br.com.sistema.view;

import br.com.sistema.dao.ClientDAO;
import br.com.sistema.model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientFrame extends JFrame {

    private final JTextField txtId = new JTextField(8);
    private final JTextField txtName = new JTextField(20);
    private final JTextField txtCpf = new JTextField(15);
    private final JTextField txtPhone = new JTextField(15);
    private final JTextField txtEmail = new JTextField(20);

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Nome", "CPF", "Telefone", "E-mail"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);
    private final ClientDAO dao = new ClientDAO();

    public ClientFrame() {
        setTitle("VETMANAGER - Clientes");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        txtId.setEditable(false);

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Dados do cliente"));

        form.add(new JLabel("ID:"));
        form.add(txtId);
        form.add(new JLabel("Nome:"));
        form.add(txtName);
        form.add(new JLabel("CPF:"));
        form.add(txtCpf);
        form.add(new JLabel("Telefone:"));
        form.add(txtPhone);
        form.add(new JLabel("E-mail:"));
        form.add(txtEmail);

        JButton btnSave = new JButton("Salvar");
        JButton btnUpdate = new JButton("Editar");
        JButton btnDelete = new JButton("Excluir");
        JButton btnClear = new JButton("Limpar");

        JPanel buttons = new JPanel();
        buttons.add(btnSave);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(btnClear);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> save());
        btnUpdate.addActionListener(e -> update());
        btnDelete.addActionListener(e -> delete());
        btnClear.addActionListener(e -> clear());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();
                txtId.setText(model.getValueAt(row, 0).toString());
                txtName.setText(model.getValueAt(row, 1).toString());
                txtCpf.setText(model.getValueAt(row, 2).toString());
                txtPhone.setText(model.getValueAt(row, 3).toString());
                txtEmail.setText(model.getValueAt(row, 4).toString());
            }
        });

        loadTable();
    }

    private Client getFormData() {
        Client client = new Client();

        if (!txtId.getText().isEmpty()) {
            client.setId(Integer.parseInt(txtId.getText()));
        }

        client.setName(txtName.getText().trim());
        client.setCpf(txtCpf.getText().trim());
        client.setPhone(txtPhone.getText().trim());
        client.setEmail(txtEmail.getText().trim());

        return client;
    }

    private boolean validateForm() {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome.");
            return false;
        }
        return true;
    }

    private void save() {
        if (!validateForm()) return;

        try {
            dao.save(getFormData());
            loadTable();
            clear();
            JOptionPane.showMessageDialog(this, "Cliente cadastrado.");
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void update() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente.");
            return;
        }

        if (!validateForm()) return;

        try {
            dao.update(getFormData());
            loadTable();
            clear();
            JOptionPane.showMessageDialog(this, "Cliente atualizado.");
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void delete() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this, "Deseja excluir este cliente?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            dao.delete(Integer.parseInt(txtId.getText()));
            loadTable();
            clear();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void loadTable() {
        model.setRowCount(0);

        try {
            for (Client client : dao.listAll()) {
                model.addRow(new Object[]{
                        client.getId(),
                        client.getName(),
                        client.getCpf(),
                        client.getPhone(),
                        client.getEmail()
                });
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void clear() {
        txtId.setText("");
        txtName.setText("");
        txtCpf.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        table.clearSelection();
    }
}