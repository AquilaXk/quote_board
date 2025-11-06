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
                try (BufferedReader br = new BufferedReader(new FileReader(lastIdFile))) {
                    String lastId = br.readLine();
                    currentId = Integer.parseInt(lastId);
                    currentId++;
                }

            }
            else {currentId = 1;}

            Quote quote = new Quote(currentId, content, author);

            try (FileWriter fw = new FileWriter(folderDir + "/" + currentId + ".json")) {
                fw.write(makeJson(quote));
            }
            try (FileWriter fw = new FileWriter(folderDir + "/lastId.txt")){
                fw.write(Integer.toString(currentId));
            }

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

        else if (input.startsWith("삭제?id")) {
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

        else if (input.startsWith("수정?id")) {
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
        String line;
        String id = "";
        String content = "";
        String author = "";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while ((line = br.readLine()) != null)) {
                line = line.trim();
                if(line.startsWith("\"content\":")) {
                    content = line.split(":")[1].trim();
                    content = content.substring(1, content.length() - 2);
                }
                else if (line.startsWith("\"author\":")) {
                    author = line.split(":")[1].trim();
                    author = author.substring(1, author.length() - 1);
                }
                else if (line.startsWith("\"id\":")) {
                    id = line.split(":")[1].trim();
                    id = id.substring(1, id.length() - 1);
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Quote(Integer.parseInt(id), content, author);
    }
    public static String makeJson(Quote quote) {
        return "{\n" +
                " \"id\": " + quote.id + ",\n" +
                "  \"content\": \"" + quote.content + "\",\n" +
                "  \"author\": \"" + quote.author + "\"\n" +
                "}";
    }

}
