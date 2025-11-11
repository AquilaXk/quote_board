package domain.wiseSaying.repository;


import domain.wiseSaying.entity.WiseSaying;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WiseSayingRepository {

    private final String folderDir;
    private final File folder;

    public WiseSayingRepository(String folderdir) {
        this.folderDir = folderdir;
        this.folder = new File(folderDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private static boolean isDataFile(String fileName) {
        return !fileName.equals("lastId.txt") && !fileName.equals("data.json");
    }


    public int getLastId() {
        File lastIdFile = new File(folderDir + "/lastId.txt");
        if (lastIdFile.exists()) {
            String lastId = processFileRead(lastIdFile, (BufferedReader br) -> br.readLine());
            int currentId = Integer.parseInt(lastId);
            return currentId;
        }
        else return 0;
    }

    public void save(WiseSaying wisesaying) {
        String jsonOutput = makeJson(wisesaying);
        File wiseSayingFile = new File(folderDir + "/" + wisesaying.getId() + ".json");
        processFileWriter(wiseSayingFile, (FileWriter fw) -> fw.write(jsonOutput));
    }

    public boolean saveBuildData(String jsonData) {
        File dataFile = new File(folderDir + "/data.json");
        try {
            processFileWriter(dataFile, (FileWriter fw) -> fw.write(jsonData));
            return true;
        }
        catch (RuntimeException e) {
            return false;
        }
    }

    public void saveLastId(int currentId) {
        File lastIdFile = new File(folderDir + "/lastId.txt");
        processFileWriter(lastIdFile, (FileWriter fw) -> fw.write(Integer.toString(currentId)));
    }

    public boolean deleteData(int del_idx) {
        File file = new File(folderDir + "/" + del_idx + ".json");
        if (file.exists()) {
            file.delete();
            return true;
        }
        else return false;
    }



    public List<WiseSaying> getWiseSayingList() {
        return Optional.ofNullable(folder.listFiles())
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .filter(file -> isDataFile(file.getName()))
                .map(this::getWiseSaying)
                .collect(Collectors.toList());
    }

    public Optional<WiseSaying> getBeforeModify(int modi_idx) {
        File file = new File(folderDir + "/" + modi_idx + ".json");
        if (file.exists()) {
            WiseSaying wisesaying = getWiseSaying(file);
            return Optional.of(wisesaying);
        }
        else {
            return Optional.empty();
        }
    }

    public String toMakeJson(WiseSaying wisesaying) {
        return makeJson(wisesaying);
    }

    private WiseSaying getWiseSaying(File file) {

        return processFileRead(file, (BufferedReader br) -> {
            String line;
            String id = "";
            String content = "";
            String author = "";
            LocalDateTime createDate = null;
            LocalDateTime modifyDate = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("\"content\":")) {
                    content = line.split(":", 2)[1].trim();
                    content = content.substring(1, content.length() - 2);
                } else if (line.startsWith("\"author\":")) {
                    author = line.split(":", 2)[1].trim();
                    author = author.substring(1, author.length() - 2);
                } else if (line.startsWith("\"id\":")) {
                    id = line.split(":", 2)[1].trim();
                    id = id.substring(0, id.length() - 1);
                } else if (line.startsWith("\"createDate\":")) {
                    String stringCreateDate = line.split(":", 2)[1].trim();
                    stringCreateDate = stringCreateDate.substring(1, stringCreateDate.length() - 2);
                    createDate = LocalDateTime.parse(stringCreateDate);
                } else if (line.startsWith("\"modifyDate\":")) {
                    String stringModifyDate = line.split(":", 2)[1].trim();
                    stringModifyDate = stringModifyDate.substring(1, stringModifyDate.length() - 1);
                    modifyDate = LocalDateTime.parse(stringModifyDate);
                }
            }

            if (id.isEmpty()) {
                throw new IOException("ID를 찾을 수 없습니다");
            }
            WiseSaying wisesaying = new WiseSaying(Integer.parseInt(id), content, author);
            wisesaying.setCreateDate(createDate);
            wisesaying.setModifyDate(modifyDate);
            return wisesaying;

        });
    }


    @FunctionalInterface
    interface BufferedReaderProcessor<R> {
        R process(BufferedReader br) throws IOException;
    }

     private <R> R processFileRead(File file, BufferedReaderProcessor<R> processor) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            return processor.process(br);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FunctionalInterface
    interface FileWriterProcessor {
        void process(FileWriter fw) throws IOException;
    }

    private void processFileWriter(File file, FileWriterProcessor p) {
        try (FileWriter fw = new FileWriter(file)) {
            p.process(fw);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String makeJson(WiseSaying wisesaying) {
        return "{\n" +
                " \"id\": " + wisesaying.getId() + ",\n" +
                "  \"content\": \"" + wisesaying.getContent() + "\",\n" +
                "  \"author\": \"" + wisesaying.getAuthor() + "\",\n" +
                "  \"createDate\": \"" + wisesaying.getCreateDate() + "\",\n" +
                "  \"modifyDate\": \"" + wisesaying.getModifyDate() + "\"\n" +
                "}";
    }

}
