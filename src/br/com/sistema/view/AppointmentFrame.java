package br.com.sistema.view;

import br.com.sistema.dao.AnimalDAO;
import br.com.sistema.dao.AppointmentDAO;
import br.com.sistema.model.Animal;
import br.com.sistema.model.Appointment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AppointmentFrame extends JFrame {

    private final JTextField txtId = new JTextField();
    private final JComboBox<Animal> cbAnimal = new JComboBox<>();
    private final JTextField txtDate = new JTextField("2026-10-03");
    private final JTextField txtTime = new JTextField("14:00");
    private final JTextField txtReason = new JTextField();
    private final JTextArea txtNotes = new JTextArea(3, 20);

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{
                "ID", "Animal ID", "Animal", "Data",
                "Horário", "Motivo", "Observações"
            }, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);
    private final AppointmentDAO dao = new AppointmentDAO();
    private final AnimalDAO animalDAO = new AnimalDAO();

    public AppointmentFrame() {
        setTitle("VETMANAGER - Consultas");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        txtId.setEditable(false);

        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));
        form.setBorder(
                BorderFactory.createTitledBorder("Dados da consulta")
        );

        form.add(new JLabel("ID:"));
        form.add(txtId);

        form.add(new JLabel("Animal:"));
        form.add(cbAnimal);

        form.add(new JLabel("Data (AAAA-MM-DD):"));
        form.add(txtDate);

        form.add(new JLabel("Horário (HH:MM):"));
        form.add(txtTime);

        form.add(new JLabel("Motivo:"));
        form.add(txtReason);

        form.add(new JLabel("Observações:"));
        form.add(new JScrollPane(txtNotes));

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
            if (!e.getValueIsAdjusting()
                    && table.getSelectedRow() >= 0) {

                int row = table.convertRowIndexToModel(
                        table.getSelectedRow()
                );

                txtId.setText(
                        model.getValueAt(row, 0).toString()
                );

                Object animalIdValue = model.getValueAt(row, 1);

                if (animalIdValue != null) {
                    selectAnimal(
                            Integer.parseInt(animalIdValue.toString())
                    );
                } else {
                    cbAnimal.setSelectedIndex(-1);
                }

                txtDate.setText(
                        model.getValueAt(row, 3).toString()
                );

                txtTime.setText(
                        model.getValueAt(row, 4).toString()
                );

                txtReason.setText(
                        model.getValueAt(row, 5).toString()
                );

                Object notes = model.getValueAt(row, 6);
                txtNotes.setText(
                        notes == null ? "" : notes.toString()
                );
            }
        });

        loadAnimals();
        loadTable();
    }

    private void loadAnimals() {
        cbAnimal.removeAllItems();

        for (Animal animal : animalDAO.listAll()) {
            cbAnimal.addItem(animal);
        }

        cbAnimal.setRenderer((list, value, index, selected, focus) -> {
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

    private void selectAnimal(int animalId) {
        for (int i = 0; i < cbAnimal.getItemCount(); i++) {
            Animal animal = cbAnimal.getItemAt(i);

            if (animal != null && animal.getId() == animalId) {
                cbAnimal.setSelectedIndex(i);
                return;
            }
        }

        cbAnimal.setSelectedIndex(-1);
    }

    private Appointment getFormData() {
        Appointment appointment = new Appointment();

        if (!txtId.getText().trim().isEmpty()) {
            appointment.setId(
                    Integer.parseInt(txtId.getText().trim())
            );
        }

        appointment.setAnimal(
                (Animal) cbAnimal.getSelectedItem()
        );

        appointment.setDate(
                LocalDate.parse(txtDate.getText().trim())
        );

        appointment.setTime(
                LocalTime.parse(txtTime.getText().trim())
        );

        appointment.setReason(txtReason.getText().trim());
        appointment.setNotes(txtNotes.getText().trim());

        return appointment;
    }

    private boolean validateForm() {
        if (cbAnimal.getSelectedItem() == null
                || txtDate.getText().trim().isEmpty()
                || txtTime.getText().trim().isEmpty()
                || txtReason.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preencha animal, data, horário e motivo."
            );

            return false;
        }

        try {
            LocalDate.parse(txtDate.getText().trim());
            LocalTime.parse(txtTime.getText().trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Data ou horário inválido. Use AAAA-MM-DD e HH:MM."
            );

            return false;
        }

        return true;
    }

    private void save() {
        if (!validateForm()) {
            return;
        }

        try {
            dao.save(getFormData());
            loadTable();
            clear();

            JOptionPane.showMessageDialog(
                    this,
                    "Consulta cadastrada."
            );

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro: " + ex.getMessage()
            );
        }
    }

    private void update() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma consulta."
            );

            return;
        }

        if (!validateForm()) {
            return;
        }

        try {
            dao.update(getFormData());
            loadTable();
            clear();

            JOptionPane.showMessageDialog(
                    this,
                    "Consulta atualizada."
            );

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro: " + ex.getMessage()
            );
        }
    }

    private void delete() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma consulta."
            );

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deseja excluir esta consulta?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            dao.delete(
                    Integer.parseInt(txtId.getText().trim())
            );

            loadTable();
            clear();

            JOptionPane.showMessageDialog(
                    this,
                    "Consulta excluída."
            );

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro: " + ex.getMessage()
            );
        }
    }

    private void loadTable() {
        model.setRowCount(0);

        for (Appointment appointment : dao.listAll()) {
            Animal animal = appointment.getAnimal();

            model.addRow(new Object[]{
                appointment.getId(),
                animal == null ? null : animal.getId(),
                animal == null ? "Sem animal" : animal.getName(),
                appointment.getDate(),
                appointment.getTime(),
                appointment.getReason(),
                appointment.getNotes()
            });
        }

        // Mantém o ID do animal na tabela, mas não o exibe.
        if (table.getColumnModel().getColumnCount() > 1) {
            table.getColumnModel().getColumn(1).setMinWidth(0);
            table.getColumnModel().getColumn(1).setMaxWidth(0);
            table.getColumnModel().getColumn(1).setPreferredWidth(0);
        }
    }

    private void clear() {
        txtId.setText("");
        cbAnimal.setSelectedIndex(-1);
        txtDate.setText("");
        txtTime.setText("");
        txtReason.setText("");
        txtNotes.setText("");
        table.clearSelection();
    }
}
