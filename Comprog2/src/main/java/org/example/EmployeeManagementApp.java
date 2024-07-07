package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;

public class EmployeeManagementApp extends JFrame {
    private DefaultTableModel model;
    private JTable table;
    private int selectedRow = -1;

    public EmployeeManagementApp() {
        // Setup the main frame
        setTitle("Employee Management Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Welcome panel
        JPanel welcomePanel = new JPanel(new GridLayout(3, 1));
        welcomePanel.add(new JLabel("Welcome to the Employee Management System", JLabel.CENTER));

        JButton manageEmployeeButton = new JButton("Manage Employee Information");
        JButton leaveRequestButton = new JButton("Submit Leave Request");

        welcomePanel.add(manageEmployeeButton);
        welcomePanel.add(leaveRequestButton);

        add(welcomePanel, BorderLayout.CENTER);

        // Manage Employee Panel
        JPanel manageEmployeePanel = createManageEmployeePanel();

        // Leave Request Panel
        JPanel leaveRequestPanel = createLeaveRequestPanel();

        // Action listeners for the buttons
        manageEmployeeButton.addActionListener(e -> {
            remove(welcomePanel);
            add(manageEmployeePanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        leaveRequestButton.addActionListener(e -> {
            remove(welcomePanel);
            add(leaveRequestPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        // Make the main frame visible
        setVisible(true);
    }

    private JPanel createManageEmployeePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Table model and JTable
        model = new DefaultTableModel(new String[]{"Employee Number", "Last Name", "First Name", "SSS No.", "PhilHealth No.", "TIN", "Pagibig No."}, 0);
        table = new JTable(model);
        loadCSV();

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Input fields and buttons
        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        JTextField empNumField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField sssField = new JTextField();
        JTextField philHealthField = new JTextField();
        JTextField tinField = new JTextField();
        JTextField pagibigField = new JTextField();

        inputPanel.add(new JLabel("Employee Number:"));
        inputPanel.add(empNumField);
        inputPanel.add(new JLabel("Last Name:"));
        inputPanel.add(lastNameField);
        inputPanel.add(new JLabel("First Name:"));
        inputPanel.add(firstNameField);
        inputPanel.add(new JLabel("SSS No.:"));
        inputPanel.add(sssField);
        inputPanel.add(new JLabel("PhilHealth No.:"));
        inputPanel.add(philHealthField);
        inputPanel.add(new JLabel("TIN:"));
        inputPanel.add(tinField);
        inputPanel.add(new JLabel("Pagibig No.:"));
        inputPanel.add(pagibigField);

        panel.add(inputPanel, BorderLayout.NORTH);

        JButton addButton = new JButton("Add Employee");
        JButton deleteButton = new JButton("Delete Employee");
        JButton editButton = new JButton("Edit Employee");
        JButton viewButton = new JButton("View Employee");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(viewButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners for the buttons
        addButton.addActionListener(e -> {
            String empNum = empNumField.getText();
            String lastName = lastNameField.getText();
            String firstName = firstNameField.getText();
            String sss = sssField.getText();
            String philHealth = philHealthField.getText();
            String tin = tinField.getText();
            String pagibig = pagibigField.getText();

            model.addRow(new Object[]{empNum, lastName, firstName, sss, philHealth, tin, pagibig});
            saveCSV();
        });

        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                model.removeRow(row);
                saveCSV();
            }
        });

        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                model.setValueAt(empNumField.getText(), row, 0);
                model.setValueAt(lastNameField.getText(), row, 1);
                model.setValueAt(firstNameField.getText(), row, 2);
                model.setValueAt(sssField.getText(), row, 3);
                model.setValueAt(philHealthField.getText(), row, 4);
                model.setValueAt(tinField.getText(), row, 5);
                model.setValueAt(pagibigField.getText(), row, 6);
                saveCSV();
            }
        });

        viewButton.addActionListener(e -> {
            selectedRow = table.getSelectedRow();
            viewEmployee();
        });

        return panel;
    }

    private JPanel createLeaveRequestPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2));

        JTextField nameField = new JTextField();
        JTextField empNumField = new JTextField();
        JTextField daysField = new JTextField();
        JTextField reasonField = new JTextField();

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Employee Number:"));
        panel.add(empNumField);
        panel.add(new JLabel("Days of Leave:"));
        panel.add(daysField);
        panel.add(new JLabel("Reason for Leave:"));
        panel.add(reasonField);

        JButton submitButton = new JButton("Submit Leave Request");
        panel.add(submitButton);

        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String empNum = empNumField.getText();
            String days = daysField.getText();
            String reason = reasonField.getText();

            saveLeaveRequest(name, empNum, days, reason);
            JOptionPane.showMessageDialog(panel, "Leave request submitted successfully!");

            nameField.setText("");
            empNumField.setText("");
            daysField.setText("");
            reasonField.setText("");
        });

        return panel;
    }

    private void saveCSV() {
        try (PrintWriter writer = new PrintWriter(new File("Main-Employee-Info.csv"))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                Vector<?> row = model.getDataVector().elementAt(i);
                for (Object cell : row) {
                    writer.print(cell + ",");
                }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Main-Employee-Info.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                model.addRow(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveLeaveRequest(String name, String empNum, String days, String reason) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("Leave-Requests.csv", true))) {
            writer.println(name + "," + empNum + "," + days + "," + reason);
        } catch (IOException e) {
            e.printStackTrace();
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

            detailsFrame.add(new JLabel("Total Salary:"));
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

            JButton computeButton = new JButton("Compute");
            detailsFrame.add(computeButton);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeManagementApp());
    }
}
