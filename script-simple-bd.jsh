    public class Users {
	//default entity ex

        public int id;
        public String nome;
        public int age;

        public Users(int id, String nome, int age) {
            this.id = id;
            this.nome = nome;
            this.age = age;
        }
        
            public Users(ArrayList<String> filds, Users row) {
            for (String element: filds) {
                if (element.equals("age")){
                    this.age = row.age;
                }
                if (element.equals("nome")){
                    this.nome = row.nome;
                }
                if (element.equals("id")){
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
            return "\n Columns: { id : "+id+ " | name : "+nome+ " | age : "+age+" }\n";
        }
    }

    private HashMap<String, TreeSet<Users>> database = new HashMap<>();
    
    private Comparator<Users> usersComparator = Comparator.comparingInt(users -> users.id);
    
        public void help(){
        System.out.println("");
        System.out.format("%5s", "Helper");
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");

        System.out.println("FOR EXAMPLE: Try run 'from(\"users\",where(e -> e.nome == \"Alex\"))'\n");
        System.out.println("or: 'from(users)'\n");
        System.out.println("or: 'select(\"*\", from(\"users\")'");
    }
        public TreeSet<?> select(String column, TreeSet<Users> from) {
        switch (column){
            case "*": return database.get("users");
            case "count": return new TreeSet<>(Collections.singleton(database.get("users").size()));

            default:
                final String[] split = column.split(",");
                final var fields = new ArrayList<>(Arrays.asList(split));
               return from.stream().map(row -> new Users(fields, row)).collect(Collectors.toCollection(() -> new TreeSet<>(usersComparator)));

        }
    }


     TreeSet<Users> from(final String table, Predicate<Users> conditions) {
        final var data = database.get(table);
        if (conditions == null) {
            printList(data);
            return data;
        } else {
           final var filterData = data.stream().filter(conditions).collect(Collectors.toCollection(() -> new TreeSet<>(usersComparator)));
            printList(filterData);
            return filterData;
        }
    }
    
        TreeSet<Users> from(final String table) {
       final var data = database.get(table);
        printList(data);
        return database.get(table);
    }
    
     Predicate<Users> where(Predicate<Users> conditions) {
        return conditions;
    }
    
    private void createDatabase() {
        database.put("users", new TreeSet<>(usersComparator));
    }
    
    
    private void put() {
    //carga...
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
    
        private void printList(TreeSet<Users> list) {
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%5s %12s %7s", "USER ID", "NAME", "AGE");
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");

        for (Users student : list) {
            System.out.format("%5s %14s, %6s", student.id, student.nome, student.age);
            System.out.println();
        }    
        System.out.println("-----------------------------------------------------------------------------");
    System.out.println();
    }
    System.out.println("|  Bem vindo ao Simple-BD!!!");
    System.out.println("|  Digite 'help()' para mais informações");
    createDatabase();
    put();




