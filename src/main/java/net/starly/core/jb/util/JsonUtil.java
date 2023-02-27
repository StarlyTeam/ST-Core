package net.starly.core.jb.util;

import com.google.gson.Gson;
import java.io.*;

public class JsonUtil {

    private static Gson gson = new Gson();

    public static <T> String toJson(T data) { return gson.toJson(data); }
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> File toJsonFile(File file, T data) {
        return $toJsonFile(file, data);
    }

    public static <T> File toJsonFile(File folder, String fileName, T data) {
        if(!fileName.endsWith(".json")) fileName += ".json";
        if(!folder.exists()) folder.mkdirs();
        File file = new File(folder, fileName);
        return $toJsonFile(file, data);
    }

    private static <T> File $toJsonFile(File file, T data) {
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) { e.printStackTrace(); }
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(toJson(data));
            bw.flush();
            return file;
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public static <T> T fromJsonFile(File file, Class<T> clazz) {
        if(!file.exists()) return null;
        FileReader fr = null;
        BufferedReader br = null;
        T result = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            result = JsonUtil.<T>fromJson(br.readLine(), clazz);
        } catch (Exception e) { e.printStackTrace(); }
        finally {
            try { br.close(); } catch (Exception ignored) { }
            try { fr.close(); } catch (Exception ignored) { }
        }
        return result;
    }

}