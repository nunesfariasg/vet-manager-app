// AnimalFrame.java
package br.com.sistema.view;

import br.com.sistema.dao.AnimalDAO;
import br.com.sistema.dao.ClientDAO;
import br.com.sistema.model.Animal;
import br.com.sistema.model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AnimalFrame extends JFrame {

    private final JTextField txtId = new JTextField();
    private final JTextField txtName = new JTextField();
    private final JTextField txtSpecies = new JTextField();
    private final JTextField txtBreed = new JTextField();
    private final JTextField txtAge = new JTextField();
    private final JComboBox<Client> cbClient = new JComboBox<>();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Nome", "Espécie", "Raça", "Idade", "Cliente"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);
    private final AnimalDAO dao = new AnimalDAO();
    private final ClientDAO clientDAO = new ClientDAO();

    public AnimalFrame() {
        setTitle("VETMANAGER - Animais");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        txtId.setEditable(false);

        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Dados do animal"));

        form.add(new JLabel("ID:"));
        form.add(txtId);
        form.add(new JLabel("Nome:"));
        form.add(txtName);
        form.add(new JLabel("Espécie:"));
        form.add(txtSpecies);
        form.add(new JLabel("Raça:"));
        form.add(txtBreed);
        form.add(new JLabel("Idade:"));
        form.add(txtAge);
        form.add(new JLabel("Cliente responsável:"));
        form.add(cbClient);

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
        txtSpecies.setText(model.getValueAt(row, 2).toString());
        txtBreed.setText(model.getValueAt(row, 3).toString());
        txtAge.setText(model.getValueAt(row, 4).toString());

        String clientName = model.getValueAt(row, 5).toString();
        selectClientByName(clientName);
    }
   });

        loadClients();
        loadTable();
    }
    
    private void selectClientByName(String clientName) {
        for (int i = 0; i < cbClient.getItemCount(); i++) {
            Client client = cbClient.getItemAt(i);

            if (client.getName().equals(clientName)) {
                cbClient.setSelectedIndex(i);
                return;
            }
        }

        cbClient.setSelectedIndex(-1);
    }

    private void loadClients() {
        cbClient.removeAllItems();

        for (Client client : clientDAO.listAll()) {
            cbClient.addItem(client);
        }

        cbClient.setRenderer((list, value, index, selected, focus) -> {
            JLabel label = new JLabel();

            if (value != null) {
                label.setText(value.getName());
            }

            if (selected) {
                label.setOpaque(true);
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            }

            return label;
        });
    }

    private void selectClient(int clientId) {
        for (int i = 0; i < cbClient.getItemCount(); i++) {
            Client client = cbClient.getItemAt(i);

            if (client.getId() == clientId) {
                cbClient.setSelectedIndex(i);
                return;
            }
        }
    }

    private Animal getFormData() {
        Animal animal = new Animal();

        if (!txtId.getText().isEmpty()) {
            animal.setId(Integer.parseInt(txtId.getText()));
        }

        animal.setName(txtName.getText().trim());
        animal.setSpecies(txtSpecies.getText().trim());
        animal.setBreed(txtBreed.getText().trim());
        animal.setAge(Integer.parseInt(txtAge.getText().trim()));
        animal.setClient((Client) cbClient.getSelectedItem());

        return animal;
    }

    private boolean validateForm() {
        if (txtName.getText().trim().isEmpty()
                || txtSpecies.getText().trim().isEmpty()
                || txtAge.getText().trim().isEmpty()
                || cbClient.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Preencha nome, espécie, idade e cliente responsável.");
            return false;
        }

        try {
            if (Integer.parseInt(txtAge.getText().trim()) < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe uma idade válida.");
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
            JOptionPane.showMessageDialog(this, "Animal cadastrado.");
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void update() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um animal.");
            return;
        }

        if (!validateForm()) return;

        try {
            dao.update(getFormData());
            loadTable();
            clear();
            JOptionPane.showMessageDialog(this, "Animal atualizado.");
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void delete() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um animal.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this, "Deseja excluir este animal?",
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

        List<Animal> animals = dao.listAll();

        for (Animal animal : animals) {
            Client client = animal.getClient();

            model.addRow(new Object[]{
                    animal.getId(),
                    animal.getName(),
                    animal.getSpecies(),
                    animal.getBreed(),
                    animal.getAge(),
                    client == null ? 0 : client.getId()
            });

            int row = model.getRowCount() - 1;
            model.setValueAt(
                    client == null ? "Sem cliente" : client.getName(),
                    row, 5
            );
        }
    }

    private void clear() {
        txtId.setText("");
        txtName.setText("");
        txtSpecies.setText("");
        txtBreed.setText("");
        txtAge.setText("");
        cbClient.setSelectedIndex(-1);
        table.clearSelection();
    }
}