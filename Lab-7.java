import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Lab7 extends JFrame
{
    JTextField title, artist, duration, year;
    JComboBox<String> genre;
    JRadioButton english, hindi;
    JCheckBox fav;
    JTable table;
    DefaultTableModel model;
    JList<String> list;

    Lab7()
    {
        setTitle("Music Catalog");
        setSize(700,500);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Labels and TextFields
        add(new JLabel("Song Title"));
        title = new JTextField(10);
        add(title);

        add(new JLabel("Artist"));
        artist = new JTextField(10);
        add(artist);

        add(new JLabel("Duration"));
        duration = new JTextField(5);
        add(duration);

        add(new JLabel("Year"));
        year = new JTextField(5);
        add(year);

        // ComboBox
        add(new JLabel("Genre"));
        genre = new JComboBox<>(
            new String[]{"Pop","Rock","Jazz","Classical"}
        );
        add(genre);

        // Radio Buttons
        english = new JRadioButton("English", true);
        hindi = new JRadioButton("Hindi");

        ButtonGroup bg = new ButtonGroup();
        bg.add(english);
        bg.add(hindi);

        add(english);
        add(hindi);

        // CheckBox
        fav = new JCheckBox("Favorite");
        add(fav);

        // Buttons
        JButton addBtn = new JButton("Add Song");
        JButton clearBtn = new JButton("Clear");
        add(addBtn);
        add(clearBtn);

        // Toggle Button
        JToggleButton play = new JToggleButton("Play");
        add(play);

        // JList
        DefaultListModel<String> lm = new DefaultListModel<>();
        list = new JList<>(lm);
        add(new JScrollPane(list));

        // JTable
        String[] columns = {"Title","Artist","Duration","Year","Genre"};
        model = new DefaultTableModel(columns,0);
        table = new JTable(model);
        add(new JScrollPane(table));

        // Menu Bar
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("Music");
        JMenuItem exit = new JMenuItem("Exit");

        menu.add(exit);
        mb.add(menu);
        setJMenuBar(mb);

        // Add Song
        addBtn.addActionListener(e ->
        {
            if(title.getText().trim().isEmpty() ||
               artist.getText().trim().isEmpty())
            {
                JOptionPane.showMessageDialog(this,
                    "Title and Artist cannot be empty!");
                return;
            }

            try
            {
                double d = Double.parseDouble(duration.getText());
                int y = Integer.parseInt(year.getText());

                if(d <= 0 || y < 1900 || y > 2026)
                {
                    JOptionPane.showMessageDialog(this,
                        "Enter valid Duration and Year!");
                    return;
                }

                model.addRow(new Object[]{
                    title.getText(),
                    artist.getText(),
                    d,
                    y,
                    genre.getSelectedItem()
                });

                lm.addElement(title.getText() + " - " + artist.getText());

                JOptionPane.showMessageDialog(this,
                    "Song Added Successfully!");

            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(this,
                    "Enter valid numbers!");
            }
        });

        // Clear
        clearBtn.addActionListener(e ->
        {
            title.setText("");
            artist.setText("");
            duration.setText("");
            year.setText("");
            fav.setSelected(false);
        });

        // Play/Pause
        play.addActionListener(e ->
        {
            if(play.isSelected())
                play.setText("Pause");
            else
                play.setText("Play");
        });

        // Exit
        exit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args)
    {
        new Lab7();
    }
}
