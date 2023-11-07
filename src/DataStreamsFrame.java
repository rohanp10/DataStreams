import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JFileChooser;
import javax.swing.border.EmptyBorder;

public class DataStreamsFrame extends JFrame {

    JPanel mainPnl;
    JPanel topPnl;
    JPanel middlePnl;

    JPanel bottomPnl;

    JTextArea displayTAOriginal;
    JScrollPane scrollerOriginal;

    JTextArea displayTAFiltered;
    JScrollPane scrollerFiltered;

    JTextField searchInput;

    JLabel titleLbl;

    ImageIcon icon;

    JButton load;
    JButton search;

    JButton quit;

    public String[] userText;

    public String searchText;

    public Map<String, Integer> frequencies = new TreeMap<>();

    Random rnd = new Random();


    public DataStreamsFrame()
    {
        mainPnl = new JPanel();

        mainPnl.setLayout(new BorderLayout());

        createTopPnl();
        mainPnl.add(topPnl, BorderLayout.NORTH);

        createMiddlePnl();
        mainPnl.add(middlePnl, BorderLayout.CENTER);

        createBottomPnl();
        mainPnl.add(bottomPnl, BorderLayout.SOUTH);

        add(mainPnl);

    }

    private void createTopPnl()
    {
        topPnl = new JPanel();

        topPnl.setLayout(new GridLayout(2, 1));

        topPnl.setBorder(new EmptyBorder(10, 10, 10, 10));


        titleLbl = new JLabel("Data Streams", JLabel.CENTER);

        searchInput = new JTextField("Search String: ");

        searchInput.setPreferredSize(new Dimension(100, 100));

        titleLbl.setFont(new Font("Roboto", Font.PLAIN, 36));
        searchInput.setFont(new Font("Roboto", Font.PLAIN, 24));

        titleLbl.setVerticalTextPosition(JLabel.TOP);
        titleLbl.setHorizontalTextPosition(JLabel.CENTER);

        topPnl.setBackground(new Color(198,226,255));

        topPnl.add(titleLbl);
        topPnl.add(searchInput);
    }

    private void createMiddlePnl()
    {
        middlePnl = new JPanel();

        middlePnl.setLayout(new BorderLayout());

        displayTAOriginal = new JTextArea(10, 40);
        displayTAFiltered = new JTextArea(10, 40);

        displayTAOriginal.setFont(new Font("Verdana", Font.PLAIN, 20));
        displayTAFiltered.setFont(new Font("Verdana", Font.PLAIN, 20));

        displayTAOriginal.setEditable(false);
        displayTAFiltered.setEditable(false);

        scrollerOriginal = new JScrollPane(displayTAOriginal);
        scrollerFiltered = new JScrollPane(displayTAFiltered);

        middlePnl.add(scrollerOriginal, BorderLayout.WEST);
        middlePnl.add(scrollerFiltered, BorderLayout.EAST);

        middlePnl.setBackground(new Color(198,226,255));

    }

    private void createBottomPnl()
    {

        bottomPnl = new JPanel();
        bottomPnl.setLayout(new GridLayout(1, 3));

        load = new JButton("Load");
        load.addActionListener((ActionEvent ae) ->
        {
            userText = loadFile();

            for (String line: userText) {
                displayTAOriginal.append(line + "\n");
            }
        });

        search = new JButton("Search");
        search.addActionListener((ActionEvent ae) ->
        {

            searchText = searchInput.getText();

            String[] userFiltered;

            for (String line: userText) {
                if (line.contains(searchText)) {
                   displayTAFiltered.append(line + "\n");
                }
            }

        });

        quit = new JButton("Quit");

        quit.addActionListener((ActionEvent ae) ->
                {
                    System.exit(0);
                });

        load.setPreferredSize(new Dimension(40, 40));
        search.setPreferredSize(new Dimension(40, 40));
        quit.setPreferredSize(new Dimension(40, 40));


        load.setFont(new Font("Sans Serif", Font.PLAIN, 15));
        search.setFont(new Font("Sans Serif", Font.PLAIN, 15));
        quit.setFont(new Font("Sans Serif", Font.PLAIN, 15));

        bottomPnl.add(load);
        bottomPnl.add(search);
        bottomPnl.add(quit);


        bottomPnl.setBackground(new Color(198,226,255));

    }

    public String[] loadFile() {

        JFileChooser chooser = new JFileChooser();
        File selectedFile;
        String rec = "";


        try
        {

            File workingDirectory = new File(System.getProperty("user.dir"));

            chooser.setCurrentDirectory(workingDirectory);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                selectedFile = chooser.getSelectedFile();
                Path file = selectedFile.toPath();

             try (Stream<String> lines = Files.lines(file))
             {

                 String[] result = lines
                         .toArray(String[]::new);

                 return result;
             }

            }

            else {
                System.out.println("Failed to choose a file to process");
                System.out.println("Run the program again!");
                System.exit(0);
            }
        }

        catch (FileNotFoundException e)
        {
            System.out.println("File not found!");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        String[] lines = {""};
        return lines;

    }

    public List<String> searchFile() {
        JFileChooser chooser = new JFileChooser();
        File selectedFile;
        String rec = "";

        List<String> result = new ArrayList<>();

        try
        {

            File workingDirectory = new File(System.getProperty("user.dir"));

            chooser.setCurrentDirectory(workingDirectory);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                selectedFile = chooser.getSelectedFile();
                Path file = selectedFile.toPath();

                try (Stream<String> lines = Files.lines(file))
                {

                    result = lines.filter(line -> line.contains(searchText))
                            .collect(Collectors.toList());

                    return result;
                }

            }

            else {
                System.out.println("Failed to choose a file to process");
                System.out.println("Run the program again!");
                System.exit(0);
            }
        }

        catch (FileNotFoundException e)
        {
            System.out.println("File not found!");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return result;
    }

    }

