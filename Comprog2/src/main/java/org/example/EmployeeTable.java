package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;

import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class EmployeeTable extends JFrame {
    private JTextField empNumField, lastNameField, firstNameField, sssField, philHealthField, tinField, pagibigField;
    private JTable table;
    private DefaultTableModel model;
    private int selectedRow = -1;
    private static final String CSV_FILE = "Main-Employee-Info.csv";

    public EmployeeTable() {
        // Frame settings
        setTitle("Employee Table");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create column names
        String[] columns = {"Employee Number", "Last Name", "First Name", "SSS No.", "PhilHealth No.", "TIN", "Pagibig No."};

        // Create table model and table
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        // Ensure CSV file exists
        initializeCSV();

        loadCSV();

        // Add mouse listener to the table for row selection
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    empNumField.setText(model.getValueAt(selectedRow, 0).toString());
                    lastNameField.setText(model.getValueAt(selectedRow, 1).toString());
                    firstNameField.setText(model.getValueAt(selectedRow, 2).toString());
                    sssField.setText(model.getValueAt(selectedRow, 3).toString());
                    philHealthField.setText(model.getValueAt(selectedRow, 4).toString());
                    tinField.setText(model.getValueAt(selectedRow, 5).toString());
                    pagibigField.setText(model.getValueAt(selectedRow, 6).toString());
                }
            }
        });

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel for input fields and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 1));

        // Sub-panel for input fields
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(2, 7));

        empNumField = new JTextField();
        lastNameField = new JTextField();
        firstNameField = new JTextField();
        sssField = new JTextField();
        philHealthField = new JTextField();
        tinField = new JTextField();
        pagibigField = new JTextField();

        fieldsPanel.add(new JLabel("Employee No.:"));
        fieldsPanel.add(empNumField);
        fieldsPanel.add(new JLabel("Last Name:"));
        fieldsPanel.add(lastNameField);
        fieldsPanel.add(new JLabel("First Name:"));
        fieldsPanel.add(firstNameField);
        fieldsPanel.add(new JLabel("SSS No.:"));
        fieldsPanel.add(sssField);
        fieldsPanel.add(new JLabel("PhilHealth No.:"));
        fieldsPanel.add(philHealthField);
        fieldsPanel.add(new JLabel("TIN:"));
        fieldsPanel.add(tinField);
        fieldsPanel.add(new JLabel("Pagibig No.:"));
        fieldsPanel.add(pagibigField);

        inputPanel.add(fieldsPanel);

        // Sub-panel for buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 4));

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton updateButton = new JButton("Update");
        JButton viewButton = new JButton("View Employee");

        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(viewButton);

        inputPanel.add(buttonsPanel);

        // Add action listeners for buttons
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRow();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteRow();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateRow();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewEmployee();
            }
        });

        add(inputPanel, BorderLayout.SOUTH);
    }

    private void addRow() {
        String empNum = empNumField.getText();
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        String sss = sssField.getText();
        String philHealth = philHealthField.getText();
        String tin = tinField.getText();
        String pagibig = pagibigField.getText();

        if (!empNum.isEmpty() && !lastName.isEmpty() && !firstName.isEmpty() && !sss.isEmpty() && !philHealth.isEmpty() && !tin.isEmpty() && !pagibig.isEmpty()) {
            model.addRow(new Object[]{empNum, lastName, firstName, sss, philHealth, tin, pagibig});
            saveCSV();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRow() {
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
            saveCSV();
            clearFields();
            selectedRow = -1;
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRow() {
        if (selectedRow != -1) {
            String empNum = empNumField.getText();
            String lastName = lastNameField.getText();
            String firstName = firstNameField.getText();
            String sss = sssField.getText();
            String philHealth = philHealthField.getText();
            String tin = tinField.getText();
            String pagibig = pagibigField.getText();

            if (!empNum.isEmpty() && !lastName.isEmpty() && !firstName.isEmpty() && !sss.isEmpty() && !philHealth.isEmpty() && !tin.isEmpty() && !pagibig.isEmpty()) {
                model.setValueAt(empNum, selectedRow, 0);
                model.setValueAt(lastName, selectedRow, 1);
                model.setValueAt(firstName, selectedRow, 2);
                model.setValueAt(sss, selectedRow, 3);
                model.setValueAt(philHealth, selectedRow, 4);
                model.setValueAt(tin, selectedRow, 5);
                model.setValueAt(pagibig, selectedRow, 6);
                saveCSV();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to update", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewEmployee() {
        if (selectedRow != -1) {
            String empNum = model.getValueAt(selectedRow, 0).toString();
            String lastName = model.getValueAt(selectedRow, 1).toString();
            String firstName = model.getValueAt(selectedRow, 2).toString();
            String sss = model.getValueAt(selectedRow, 3).toString();
            String philHealth = model.getValueAt(selectedRow, 4).toString();
            String tin = model.getValueAt(selectedRow, 5).toString();
            String pagibig = model.getValueAt(selectedRow, 6).toString();

            JFrame detailsFrame = new JFrame("Employee Details");
            detailsFrame.setSize(600, 600);
            detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            detailsFrame.setLayout(new GridLayout(16, 2));

            detailsFrame.add(new JLabel("Employee Number:"));
            JTextField empNumField = new JTextField(empNum);
            empNumField.setEditable(false);
            detailsFrame.add(empNumField);

            detailsFrame.add(new JLabel("Last Name:"));
            JTextField lastNameField = new JTextField(lastName);
            lastNameField.setEditable(false);
            detailsFrame.add(lastNameField);

            detailsFrame.add(new JLabel("First Name:"));
            JTextField firstNameField = new JTextField(firstName);
            firstNameField.setEditable(false);
            detailsFrame.add(firstNameField);

            detailsFrame.add(new JLabel("SSS No.:"));
            JTextField sssField = new JTextField(sss);
            sssField.setEditable(false);
            detailsFrame.add(sssField);

            detailsFrame.add(new JLabel("PhilHealth No.:"));
            JTextField philHealthField = new JTextField(philHealth);
            philHealthField.setEditable(false);
            detailsFrame.add(philHealthField);

            detailsFrame.add(new JLabel("TIN:"));
            JTextField tinField = new JTextField(tin);
            tinField.setEditable(false);
            detailsFrame.add(tinField);

            detailsFrame.add(new JLabel("Pagibig No.:"));
            JTextField pagibigField = new JTextField(pagibig);
            pagibigField.setEditable(false);
            detailsFrame.add(pagibigField);

            detailsFrame.add(new JLabel("Basic Salary:"));
            JTextField basicSalaryField = new JTextField();
            detailsFrame.add(basicSalaryField);

            detailsFrame.add(new JLabel("Month:"));
            String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            JComboBox<String> monthComboBox = new JComboBox<>(months);
            detailsFrame.add(monthComboBox);

            JButton computeButton = new JButton("Compute");
            detailsFrame.add(computeButton);

            JTextField totalSalaryField = new JTextField();
            totalSalaryField.setEditable(false);
            detailsFrame.add(totalSalaryField);

            detailsFrame.add(new JLabel("PagIbig Deduction:"));
            JTextField pagibigDeductionField = new JTextField();
            pagibigDeductionField.setEditable(false);
            detailsFrame.add(pagibigDeductionField);

            detailsFrame.add(new JLabel("SSS Deduction:"));
            JTextField sssDeductionField = new JTextField();
            sssDeductionField.setEditable(false);
            detailsFrame.add(sssDeductionField);

            detailsFrame.add(new JLabel("PhilHealth Deduction:"));
            JTextField philHealthDeductionField = new JTextField();
            philHealthDeductionField.setEditable(false);
            detailsFrame.add(philHealthDeductionField);

            detailsFrame.add(new JLabel("Withholding Tax Deduction:"));
            JTextField withholdingTaxField = new JTextField();
            withholdingTaxField.setEditable(false);
            detailsFrame.add(withholdingTaxField);

            computeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        double basicSalary = Double.parseDouble(basicSalaryField.getText());

                        // Calculating deductions
                        double pagibigDeduction = basicSalary * 0.02;
                        double sssDeduction = 1125;
                        double philHealthDeduction = (basicSalary * 0.03) / 50;
                        double totalDeductions = pagibigDeduction + sssDeduction + philHealthDeduction;

                        // Calculating withholding tax
                        double taxableIncome = basicSalary - totalDeductions;
                        double withholdingTax = 0;
                        if (taxableIncome > 20833) {
                            withholdingTax = (taxableIncome - 20833) * 0.20;
                        }

                        // Total deductions including withholding tax
                        totalDeductions += withholdingTax;

                        // Calculating final salary
                        double finalSalary = basicSalary - totalDeductions;

                        // Displaying results
                        pagibigDeductionField.setText(String.format("%.2f", pagibigDeduction));
                        sssDeductionField.setText(String.format("%.2f", sssDeduction));
                        philHealthDeductionField.setText(String.format("%.2f", philHealthDeduction));
                        withholdingTaxField.setText(String.format("%.2f", withholdingTax));
                        totalSalaryField.setText(String.format("%.2f", finalSalary));

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(detailsFrame, "Please enter a valid basic salary.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            detailsFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to view", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        empNumField.setText("");
        lastNameField.setText("");
        firstNameField.setText("");
        sssField.setText("");
        philHealthField.setText("");
        tinField.setText("");
        pagibigField.setText("");
    }

    private void saveCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE))) {
            Vector<Vector> data = model.getDataVector();
            for (Vector row : data) {
                String[] rowData = new String[row.size()];
                for (int i = 0; i < row.size(); i++) {
                    rowData[i] = row.get(i).toString();
                }
                writer.writeNext(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCSV() {
        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                model.addRow(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeCSV() {
        File file = new File(CSV_FILE);
        if (!file.exists()) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE))) {
                String[] header = {"Employee Number", "Last Name", "First Name", "SSS No.", "PhilHealth No.", "TIN", "Pagibig No."};
                writer.writeNext(header);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EmployeeTable().setVisible(true);
            }
        });
    }
}