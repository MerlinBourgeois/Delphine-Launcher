package fr.caemur.delphinelauncher.file.savers;

import com.google.gson.Gson;
import fr.caemur.delphinelauncher.utils.LauncherOptions;

import java.io.*;

public class OptionSaver {
    private static final Gson gson = new Gson();

    public static LauncherOptions readOptions(File file) {
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String optionsText = bufferedReader.readLine();
            if (optionsText != null) {
                return gson.fromJson(optionsText, LauncherOptions.class);
            }
        } catch (IOException exception) {
            System.err.println("Error : can't read launcher options : " + exception);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException exception) {
                    System.err.println("Error : can't close launcher options file : " + exception);
                }
            }
        }

        return null;
    }

    public static void save(File file, LauncherOptions options) {
        BufferedWriter bufferedWriter = null;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(gson.toJson(options));
        } catch (IOException exception) {
            System.err.println("Error : can't save launcher options : " + exception);
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException exception) {
                    System.err.println("Error : can't close launcher options file : " + exception);
                }
            }
        }
    }

    public static void initFile(File file) {
        try {
            if (file.createNewFile()) {
                OptionSaver.save(file, LauncherOptions.DEFAULT_OPTIONS);
            }
        } catch (IOException exception) {
            System.err.println("Error : can't create launcher options file : " + exception);
        }
    }
}
