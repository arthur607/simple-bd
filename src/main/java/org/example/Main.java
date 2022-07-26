package org.example;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main {

    private static final HashMap<String, TreeSet<Users>> database = new HashMap<>(1, 1.0f);
    public static final String USERS = "users";

    private static final Comparator<Users> usersComparator = Comparator.comparingInt(users -> users.id);

    public static void main(String[] args) throws NoSuchFieldException {
        createDatabase();
        put();
        printList(database.get("users"));
        help();
        from(USERS, where(user -> Objects.equals(user.nome, "Arthur")));
        select("age", from("users"));
    }

    private static void createDatabase() {

        database.put(USERS, new TreeSet<>(usersComparator));
    }

    private static void put() {
        //carga
        var list = database.get("users");
        list.add(new Users(1, "Arthur", 19));
        list.add(new Users(2, "Caio", 30));
        list.add(new Users(3, "Fabricio", 15));
        list.add(new Users(5, "Layla", 24));
        list.add(new Users(4, "Alex", 38));
        list.add(new Users(7, "Rodrigo", 31));
        list.add(new Users(6, "Jeff", 27));
        list.add(new Users(9, "Aaron", 30));
        list.add(new Users(8, "Renata", 20));
        list.add(new Users(11, "Marcela", 17));
        list.add(new Users(10, "Victoria", 19));
        list.add(new Users(14, "Natalia", 20));
    }

    private static void printList(TreeSet<? extends Users> list) {
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%5s %12s %7s", "USER ID", "NAME", "AGE");
        headers();

        for (Users student : list) {
            System.out.format("%5s %14s, %6s", student.id, student.nome, student.age);
            System.out.println();
        }
        System.out.println("-----------------------------------------------------------------------------");
    }

    static TreeSet<Users> from(String table, Predicate<Users> conditions) {
        var data = database.get(table);
        if (conditions == null) {
            printList(data);
            return data;
        } else {

            //Collections.binarySearch(Arrays.asList(database.get(table).toArray()), new Users(0,"Arthur",0),u)
            var filterData = data.stream().filter(conditions).collect(Collectors.toCollection(() -> new TreeSet<>(usersComparator)));  //table scan
            printList(filterData);
            return filterData;
        }
    }

    /**
     * not 100% implemented
     */
    public static TreeSet<?> select(String column, TreeSet<Users> from) {
        switch (column) {
            case "*":
                return database.get("users");
            case "count":
                return new TreeSet<>(Set.of(database.get("users").size()));

            default:
                String[] split = column.split(",");
                final var fields = new ArrayList<>(Arrays.asList(split));
                TreeSet<Users> collect = from.stream().map(row -> new Users(fields, row)).collect(Collectors.toCollection(() -> new TreeSet<>(usersComparator)));
                final var finalData = new LinkedHashMap<String, Object>();
                collect.forEach(e -> {
                    if (e.nome != null) {
                        finalData.put("NAME", e.nome);
                    }
                    if (e.age != 0) {
                        finalData.put("AGE", e.age);
                    }
                    if (e.id != 0) {
                        finalData.put("ID", e.id);
                    }
                });
                printElementsBySelect(finalData);
                final var finalValues = new TreeSet<Object>();
                finalData.forEach((k, v) -> finalValues.add(v));
                return finalValues;

        }
    }

    private static void printElementsBySelect(LinkedHashMap<String, Object> finalData) {
      //  String reduce = finalData.keySet().stream().reduce((s, s2) -> s + "," + s2).get();
        System.out.println("-----------------------------------------------------------------------------");
        switch (finalData.size()) {
            case 1: {
                if (finalData.containsKey("ID")) {
                    System.out.printf("%5s", "USER ID");
                    headers();
                    System.out.format("%5s", finalData.get("ID"));
                }
                if (finalData.containsKey("AGE")) {
                    System.out.printf("%5s", "AGE");
                    headers();
                    System.out.format("%5s", finalData.get("AGE"));
                }
                if (finalData.containsKey("NAME")) {
                    System.out.printf("%5s", "NAME");
                    headers();
                    System.out.format("%5s", finalData.get("NAME"));
                }
                break;
            }
            case 2: {
                System.out.printf("%5s %12s", "USER ID", "NAME");
                headers();
                System.out.format("%5s %14s", finalData.get("id"), finalData.get("NAME"));
                break;
            }
            case 3: {
                System.out.printf("%5s %12s %7s", "USER ID", "NAME", "AGE");
                headers();
                System.out.format("%5s %14s %6s", finalData.get("ID"), finalData.get("NAME"), finalData.get("AGE"));
                break;
            }
        }
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");

//        for (Users student : list) {
//            System.out.format("%5s %14s %6s", student.id, student.nome, student.age);
//            System.out.println();
//        }

    }

    private static void headers() {
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");
    }

    static TreeSet<Users> from(final String table) {
        var data = database.get(table);
        printList(data);
        return database.get(table);
    }

    static Predicate<Users> where(Predicate<Users> conditions) {
        return conditions;
    }

    public static void help() {
        System.out.println();
        System.out.format("%5s", "Helper");
        headers();

        System.out.println("FOR EXAMPLE: Try run 'from(\"users\",where(e -> e.nome == \"Alex\"))'\n");
        System.out.println("or: 'from(users)'\n");
        System.out.println("or: 'select(\"*\", from(\"users\")'");
    }

    public static class Users {
        public int id;
        public String nome;
        public int age;

        public Users(int id, String nome, int age) {
            this.id = id;
            this.nome = nome;
            this.age = age;
        }

        public Users(ArrayList<String> filds, Users row) {
            for (String element : filds) {
                if (element.equals("age")) {
                    this.age = row.age;
                }
                if (element.equals("nome")) {
                    this.nome = row.nome;
                }
                if (element.equals("id")) {
                    this.id = row.id;
                }
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Users users = (Users) o;

            return id == users.id;
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public String toString() {
            return "\n Columns: { id : " + id + " | name : " + nome + " | age : " + age + " }\n";
        }
    }
}