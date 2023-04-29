package net.starly.core.jb.util;

import com.google.gson.Gson;
import java.io.*;

public class JsonUtil {

    private static final Gson gson = new Gson();

    /**
     * 객체를 Json-String 형식으로 변경해줍니다.
     * @param data 객체
     * @return Json-String
     * @param <T> 해당 객체의 타입
     */
    public static <T> String toJson(T data) { return gson.toJson(data); }

    /**
     * Json-String 을 객체로 변경해줍니다.
     * @param json Json-String
     * @param clazz 객체의 클래스
     * @return 객체
     * @param <T> 해당 객체의 타입
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    /**
     * 객체를 Json-String 으로 변경하여 File 로 생성해줍니다.
     * @param file 저장 할 파일
     * @param data 객체
     * @return 저장 된 파일
     * @param <T> 해당 객체의 타입
     */
    public static <T> File toJsonFile(File file, T data) {
        return $toJsonFile(file, data);
    }

    /**
     * 객체를 Json-String 으로 변경하여 File 로 생성해줍니다.
     * @param folder 파일이 저장 될 폴더
     * @param fileName 저장 될 파일의 이름 (확장자 '.json' 을 붙이지 않으면 자동으로 붙습니다.)
     * @param data 객체
     * @return 저장 된 파일
     * @param <T> 해당 객체의 타입
     */
    public static <T> File toJsonFile(File folder, String fileName, T data) {
        if (!fileName.endsWith(".json")) fileName += ".json";
        if (!folder.exists()) folder.mkdirs();
        File file = new File(folder, fileName);
        return $toJsonFile(file, data);
    }

    private static <T> File $toJsonFile(File file, T data) {
        if (!file.exists()) {
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

    /**
     * Json-String 이 저장 된 파일을 읽어, 객체로 변경해줍니다.
     * @param file Json-String 이 저장 된 파일
     * @param clazz 객체의 클래스
     * @return 객체
     * @param <T> 해당 객체의 타입
     */
    public static <T> T fromJsonFile(File file, Class<T> clazz) {
        if (!file.exists()) return null;
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