import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.regex.Pattern;


public class StudentManager extends JFrame {


    static final String URL = "jdbc:mysql://localhost:3306/studentdb";
    static final String USER = "root";
    static final String PASS = "02042011@Cwc";


    JTextField idField, nameField, emailField, ageField;
    JTable table;
    DefaultTableModel model;
    JButton insertBtn, updateBtn, deleteBtn, searchBtn, clearBtn;


    public StudentManager() {
        setTitle("Student Manager - Swing + JDBC");
        setSize(700, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel form = new JPanel(new GridLayout(2, 4, 5, 5));
        idField = new JTextField();
        nameField = new JTextField();
        emailField = new JTextField();
        ageField = new JTextField();
        form.add(new JLabel("ID:"));      form.add(idField);
        form.add(new JLabel("Name:"));    form.add(nameField);
        form.add(new JLabel("Email:"));   form.add(emailField);
        form.add(new JLabel("Age:"));     form.add(ageField);


        JPanel btnPanel = new JPanel();
        insertBtn = new JButton("Insert");
        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");
        searchBtn = new JButton("Search");
        clearBtn  = new JButton("Clear");
        btnPanel.add(insertBtn); btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn); btnPanel.add(searchBtn);
        btnPanel.add(clearBtn);


        model = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Age"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);


        setLayout(new BorderLayout(5, 5));
        add(form, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);


        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                idField.setText(model.getValueAt(row, 0).toString());
                nameField.setText(model.getValueAt(row, 1).toString());
                emailField.setText(model.getValueAt(row, 2).toString());
                ageField.setText(model.getValueAt(row, 3).toString());
            }
        });


        insertBtn.addActionListener(e -> { if (validateInput()) runTask(this::insertStudent, "Inserted"); });
        updateBtn.addActionListener(e -> { if (validateInput()) runTask(this::updateStudent, "Updated"); });
        deleteBtn.addActionListener(e -> { if (!idField.getText().trim().isEmpty()) runTask(this::deleteStudent, "Deleted"); else warn("Enter ID to delete"); });
        searchBtn.addActionListener(e -> runTask(this::searchStudent, "Search complete"));
        clearBtn.addActionListener(e -> clearFields());


        loadAll();
    }


    boolean validateInput() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String age = ageField.getText().trim();


        if (name.isEmpty() || email.isEmpty() || age.isEmpty()) {
            warn("All fields except ID (for insert) are required");
            return false;
        }
        if (!Pattern.matches("^[A-Za-z ]+$", name)) {
            warn("Name must contain letters only");
            return false;
        }
        if (!Pattern.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$", email)) {
            warn("Invalid email format");
            return false;
        }
        try {
            int a = Integer.parseInt(age);
            if (a <= 0 || a > 120) { warn("Age must be between 1 and 120"); return false; }
        } catch (NumberFormatException ex) {
            warn("Age must be numeric");
            return false;
        }
        return true;
    }


    // Runs a DB task off the EDT so the UI never freezes during insert/update/delete/search
    void runTask(DbTask task, String successMsg) {
        setButtonsEnabled(false);
        new SwingWorker<String, Void>() {
            protected String doInBackground() {
                try {
                    task.run();
                    return null;
                } catch (SQLException ex) {
                    return ex.getMessage();
                }
            }
            protected void done() {
                try {
                    String error = get();
                    if (error != null) warn("DB error: " + error);
                    else { JOptionPane.showMessageDialog(StudentManager.this, successMsg); loadAll(); clearFields(); }
                } catch (Exception ex) {
                    warn("Unexpected error: " + ex.getMessage());
                } finally {
                    setButtonsEnabled(true);
                }
            }
        }.execute();
    }


    interface DbTask { void run() throws SQLException; }


    void insertStudent() throws SQLException {
        String sql = "INSERT INTO students (name, email, age) VALUES (?, ?, ?)";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nameField.getText().trim());
            ps.setString(2, emailField.getText().trim());
            ps.setInt(3, Integer.parseInt(ageField.getText().trim()));
            ps.executeUpdate();
        }
    }


    void updateStudent() throws SQLException {
        String sql = "UPDATE students SET name=?, email=?, age=? WHERE id=?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nameField.getText().trim());
            ps.setString(2, emailField.getText().trim());
            ps.setInt(3, Integer.parseInt(ageField.getText().trim()));
            ps.setInt(4, Integer.parseInt(idField.getText().trim()));
            if (ps.executeUpdate() == 0) throw new SQLException("No record found with that ID");
        }
    }


    void deleteStudent() throws SQLException {
        String sql = "DELETE FROM students WHERE id=?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idField.getText().trim()));
            if (ps.executeUpdate() == 0) throw new SQLException("No record found with that ID");
        }
    }


    void searchStudent() throws SQLException {
        String sql = "SELECT * FROM students WHERE id=?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idField.getText().trim()));
            ResultSet rs = ps.executeQuery();
            SwingUtilities.invokeLater(() -> model.setRowCount(0));
            while (rs.next()) {
                Object[] row = {rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getInt("age")};
                SwingUtilities.invokeLater(() -> model.addRow(row));
            }
        }
    }


    void loadAll() {
        setButtonsEnabled(false);
        new SwingWorker<Void, Void>() {
            java.util.List<Object[]> rows = new java.util.ArrayList<>();
            protected Void doInBackground() {
                String sql = "SELECT * FROM students";
                try (Connection con = getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                    while (rs.next())
                        rows.add(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getInt("age")});
                } catch (SQLException ex) {
                    warn("Load failed: " + ex.getMessage());
                }
                return null;
            }
            protected void done() {
                model.setRowCount(0);
                for (Object[] row : rows) model.addRow(row);
                setButtonsEnabled(true);
            }
        }.execute();
    }


    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }


    void setButtonsEnabled(boolean b) {
        insertBtn.setEnabled(b); updateBtn.setEnabled(b); deleteBtn.setEnabled(b); searchBtn.setEnabled(b);
    }


    void clearFields() {
        idField.setText(""); nameField.setText(""); emailField.setText(""); ageField.setText("");
        table.clearSelection();
    }


    void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Notice", JOptionPane.WARNING_MESSAGE);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManager().setVisible(true));
    }
}

