package com.back;

import java.io.*;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static boolean status = true;
    static String folderDir = "src/main/java/com/back/db/wiseSaying";
    static File folder = new File(folderDir);

    public static void main(String[] args) throws IOException {
        System.out.println("== 명언 앱 ==");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        while (status) {
            controller(inputCommand());
        }

        sc.close();
    }

    public static String inputCommand() {
        System.out.print("명령) ");
        String input = sc.nextLine();
        return input.trim();
    }

    public static void controller(String input) throws IOException {
        if (input.equals("등록")) {
            System.out.print("명언 : ");
            String content = sc.nextLine().trim();
            System.out.print("작가 : ");
            String author = sc.nextLine().trim();

            int currentId;
            File lastIdFile = new File(folderDir + "/lastId.txt");
            if (lastIdFile.exists()) {
                String lastId = processFileRead(lastIdFile, (BufferedReader br) -> br.readLine());
                currentId = Integer.parseInt(lastId);
                currentId++;
            }


            else {
                currentId = 1;
            }

            Quote quote = new Quote(currentId, content, author);
            String jsonOutput = makeJson(quote);

            final int finalCurrentId = currentId;

            File quoteFile = new File(folderDir + "/" + currentId + ".json");
            processFileWriter(quoteFile, (FileWriter fw) -> fw.write(jsonOutput));

            processFileWriter(lastIdFile, (FileWriter fw) -> fw.write(Integer.toString(finalCurrentId)));

            System.out.println("%d번 명언이 등록되었습니다.".formatted(currentId));
        }

        else if (input.equals("목록")) {
            System.out.println("번호 / 작가 / 명언");
            System.out.println("----------------------");
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (isDataFile(file.getName())) {
                        Quote quote = getQuote(file);
                        System.out.println("%s / %s / %s".formatted(quote.id, quote.author, quote.content));
                    }
                }
            }
        }

        else if (input.equals("종료")) {
            status = false;
        }

        else if (input.startsWith("삭제")) {
            String[] tokens = input.split("=");
            String del_idx = tokens[1];

            File file = new File(folderDir + "/" + del_idx + ".json");
            if (file.exists()) {
                file.delete();
                System.out.println("%s번 명언이 삭제되었습니다.".formatted(del_idx));
            }
            else {
                System.out.println("%s번 명언은 존재하지 않습니다.".formatted(del_idx));
            }
        }

        else if (input.startsWith("수정")) {
            String[] tokens = input.split("=");
            String modi_idx = tokens[1];

            File file = new File(folderDir + "/" + modi_idx + ".json");
            if (file.exists()) {
                Quote quote = getQuote(file);
                System.out.println("명언(기존) : %s".formatted(quote.content));
                System.out.print("명언 : ");
                String content = sc.nextLine();

                System.out.println("작가(기존) : %s".formatted(quote.author));
                System.out.print("작가 : ");
                String author = sc.nextLine();

                quote = new Quote(Integer.parseInt(modi_idx), content, author);
                try (FileWriter fw = new FileWriter(folderDir + "/" + modi_idx + ".json")) {
                    fw.write(makeJson(quote));
                }
            }
            else{
                System.out.println("%s번 명언은 존재하지 않습니다.".formatted(modi_idx));
            }
        }

        else if (input.equals("빌드")) {
            StringBuilder sb = new StringBuilder();
            sb.append("[\n");

            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (isDataFile(file.getName())) {
                        Quote quote = getQuote(file);
                        if (sb.charAt(sb.length() - 1) == '}') {
                            sb.append(",");
                        }
                        sb.append(makeJson(quote));
                    }
                }
            }
            sb.append("]");
            try (FileWriter fw = new FileWriter(folderDir + "/data.json")) {
                fw.write(sb.toString());
            }
        }
    }

    public static boolean isDataFile(String fileName) {
        return !fileName.equals("lastId.txt") && !fileName.equals("data.json");
    }

    public static Quote getQuote(File file) {

    return processFileRead(file, (BufferedReader br) -> {
        String line;
        String id = "";
        String content = "";
        String author = "";

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("\"content\":")) {
                content = line.split(":", 2)[1].trim();
                content = content.substring(1, content.length() - 2);
            } else if (line.startsWith("\"author\":")) {
                author = line.split(":", 2)[1].trim();
                author = author.substring(1, author.length() - 1);
            } else if (line.startsWith("\"id\":")) {
                id = line.split(":", 2)[1].trim();
                id = id.substring(0, id.length() - 1);
            }
        }

        if (id.isEmpty()) {
            throw new IOException("ID를 찾을 수 없습니다");
        }

        return new Quote(Integer.parseInt(id), content, author);

    });
}


    public static String makeJson(Quote quote) {
        return "{\n" +
                " \"id\": " + quote.id + ",\n" +
                "  \"content\": \"" + quote.content + "\",\n" +
                "  \"author\": \"" + quote.author + "\"\n" +
                "}";
    }
    @FunctionalInterface
    interface BufferedReaderProcessor<R> {
        R process(BufferedReader br) throws IOException;
    }

    public static <R> R processFileRead(File file, BufferedReaderProcessor<R> processor) {
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

    public static void processFileWriter(File file, FileWriterProcessor p) {
        try (FileWriter fw = new FileWriter(file)) {
            p.process(fw);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

