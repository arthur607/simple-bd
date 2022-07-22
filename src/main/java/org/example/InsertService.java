package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

public class InsertService {

    private static Map<String, List<User>> database = new HashMap<>();

    private static final Scanner sc = new Scanner(System.in);
    private static String comando;

    public static void inserir() throws IOException {
        var fake = "insert users { name: 'Arthur', age: 35 }";


        String[] split = fake.split("\\{");
        final var table = split[0].replace("insert", "");
        database.put(table, new ArrayList<>());

        final String[] values = split[1].replace("}", "").split(",");


        var id = from(table).size();

        from(table).add(id, new User("ds", 1));
        writeTxt(table);
    }

    private static void writeTxt(String table) throws IOException {
        var file = new File("saida/sql.txt");

        if (file.createNewFile()) {

            System.out.println("pastas criadas");
            try (FileWriter fw = new FileWriter(file, true)) {
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(database.get(table).toString());

                bw.close();
            }

        }
        else {
            System.out.println("nao criado");
        }
    }


    public static <T> List<?> from(T table, Supplier function) {
        return new ArrayList<>();
    }

    public static List<User> from(String table) {

        return database.get(table);
    }

    public static void main(String[] args) throws IOException {
        inserir();
    }
}
