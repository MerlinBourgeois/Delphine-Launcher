package fr.caemur.delphinelauncher.file.savers;

import com.google.gson.Gson;
import fr.caemur.delphinelauncher.Main;
import fr.caemur.delphinelauncher.auth.DelphineAuthInfo;

import java.io.*;
import java.util.Base64;

public class AuthSaver {
    private static final Gson gson = new Gson();

    public static DelphineAuthInfo load(File authDataFile) {
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(authDataFile));
            String json = new String(Base64.getDecoder().decode(bufferedReader.readLine()));

            if (!json.isEmpty()) return gson.fromJson(json, DelphineAuthInfo.class);

        } catch (IOException e) {
            Main.getLogger().err("Error : can't read auth data : " + e);
            Main.getLogger().printStackTrace(e);

        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();

                } catch (IOException e) {
                    Main.getLogger().err("Error : can't close auth data file : " + e);
                    Main.getLogger().printStackTrace(e);
                }
            }
        }

        return null;
    }

    public static void save(File authDataFile, DelphineAuthInfo data) {
        String stringData = gson.toJson(data);

        BufferedWriter bufferedWriter = null;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(authDataFile));
            bufferedWriter.write(Base64.getEncoder().encodeToString(stringData.getBytes()));

        } catch (IOException e) {
            Main.getLogger().err("Error : can't save auth data : " + e);
            Main.getLogger().printStackTrace(e);

        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();

                } catch (IOException e) {
                    Main.getLogger().err("Error : can't close auth data file : " + e);
                    Main.getLogger().printStackTrace(e);
                }
            }
        }
    }
}
