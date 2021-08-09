package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    public static void main(String[] args) {
        ArrayList<String> file_lines = new ArrayList<>();
        ArrayList<Pattern> pattern = new ArrayList<>();
        Matcher matcher;
        ArrayList<String> replace_val = new ArrayList<>();
        Boolean match_found = false;

        // Check args
        if (!args[0].equals("-input") || !args[2].equals("-rules"))
            throw new IllegalArgumentException("Not a valid argument");

        // Read rules file
        try (BufferedReader rules_file = new BufferedReader(new FileReader(args[3]))) {
            String line;
            while ((line = rules_file.readLine()) != null) {
                pattern.add(Pattern.compile(line.substring(0, line.indexOf("->"))));
                replace_val.add(line.substring(line.indexOf("->") + 2));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        // Read input file and replace all matches according rules
        try (BufferedReader br = new BufferedReader(new FileReader(args[1]))) {
            String line;

            while ((line = br.readLine()) != null) {
                for (int i = 0; i < pattern.size(); i++) {
                    matcher = pattern.get(i).matcher(line);
                    // If some match is found replace it
                    if (!line.equals(matcher.replaceAll(replace_val.get(i)))) {
                        match_found = true;
                        line = matcher.replaceAll(replace_val.get(i));
                    }
                }
                file_lines.add(line);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        // If at least one match is found replace it in the file
        if (match_found)
        {
            try(FileWriter writer = new FileWriter(args[1], false))
            {
                for (int i = 0; i < file_lines.size(); i++) {
                    writer.append(file_lines.get(i) + '\n');
                }
                writer.flush();
            }
            catch(IOException ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}

