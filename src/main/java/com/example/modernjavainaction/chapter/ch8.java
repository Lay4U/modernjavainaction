package com.example.modernjavainaction.chapter;

import com.example.modernjavainaction.domain.Transaction;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.entry;

/* 컬렉션 API 개선*/
public class ch8 {
    public void 컬렉션팩토리(){
        ArrayList<String> friends = new ArrayList<>();
        friends.add("Raphael");
        friends.add("Olivia");
        friends.add("Thibaut");

        List<String> friends2 = Arrays.asList("Raphael", "Olivia", "Thibaut");
        HashSet<String> friends3 = new HashSet<>(Arrays.asList("Raphael", "Olivia", "Thibaut"));
        Set<String> friends4 = Stream.of("Raphael", "Olivia", "Thibaut").collect(Collectors.toSet());

        Map<String, Integer> ageOfFriends = Map.of("Raphael", 30, "Olivia", 25, "Thibaut", 26);
        Map<String, Integer> ageOfFriens2 = Map.ofEntries(entry("Raphael", 30), entry("Olivia", 25), entry("Thibaut", 26));

    }

    List<Transaction> transactions = new ArrayList<>();
    public void 리스트와집합처리(){
        for (Transaction transaction : transactions) {
            if(Character.isDigit(transaction.getReferenceCode().charAt(0))){
                transactions.remove(transaction);
            }
        }

        for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext(); ) {
            Transaction transaction = iterator.next();
            if(Character.isDigit(transaction.getReferenceCode().charAt(0))){
                transactions.remove(transaction);
            }
        } //반복자의 상태와 컬렉션의 상태가 서로 동기화 되지 않는다.

        for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext(); ) {
            Transaction transaction = iterator.next();
            if(Character.isDigit(transaction.getReferenceCode().charAt(0))){
                iterator.remove();
            }
        }

        transactions.removeIf(transaction ->
                Character.isDigit(transaction.getReferenceCode().charAt(0)));

        List<String> referenceCodes = transactions.stream()
                .map(Transaction::getReferenceCode)
                .toList();

        referenceCodes.stream()
                .map(code -> Character.toUpperCase(code.charAt(0)) +
                        code.substring(1))
                .toList()
                .forEach(System.out::println);

        for (ListIterator<String> iterator = referenceCodes.listIterator(); iterator.hasNext(); ) {
            String code = iterator.next();
            iterator.set(Character.toUpperCase(code.charAt(0)) + code.substring(1));
        }

        referenceCodes.replaceAll(code -> Character.toUpperCase(code.charAt(0)) + code.substring(1));
    }

    public void 맵처리() throws NoSuchAlgorithmException {
        Map<String, Integer> ageOfFriends = new HashMap<>();
        ageOfFriends.put("Raphael", 30);
        ageOfFriends.put("Olivia", 25);
        ageOfFriends.put("Thibaut", 26);

        for (Map.Entry<String, Integer> entry : ageOfFriends.entrySet()) {
            String friend = entry.getKey();
            Integer age = entry.getValue();
            System.out.println(friend + " is " + age + " years old");
        }

        ageOfFriends.forEach((friend, age) -> System.out.println(friend + " is " + age + " years old"));

        Map<String, String> favouriteMovies = Map.ofEntries(
                entry("Raphael", "Star Wars"),
                entry("Cristina", "Matrix"),
                entry("Olivia", "James Bond"));

        favouriteMovies
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(System.out::println);


        Map<String, String> favouriteMovies2 = Map.ofEntries(
                entry("Raphael", "Star Wars"),
                entry("Olivia", "James Bond"));

        System.out.println(favouriteMovies2.getOrDefault("Olivia", "Matrix"));
        System.out.println(favouriteMovies2.getOrDefault("Thibaut", "Matrix"));

        Map<String, byte[]> dataToHash = new HashMap<>();

        List<String> lines = new ArrayList<>();
        lines.forEach(line -> {
            dataToHash.computeIfAbsent(line, this::calculateDigest);
        });

        String friend = "Raphael";
        List<String> movies = new ArrayList<>();
        Map<String, List<String>> friendsToMovies = new HashMap<>();
        if(movies == null) {
            movies = new ArrayList<>();
            friendsToMovies.put(friend, movies);
        }
        movies.add("Star Wars");

        friendsToMovies.computeIfAbsent("Rapheal", name -> new ArrayList<>())
                .add("Star Wars");


        String key = "Raphael";
        String value = "Jack Reacher 2";
        if (favouriteMovies.containsKey(key) &&
        Objects.equals(favouriteMovies.get(key), value)) {
            favouriteMovies.remove(key);
//            return true;
        }
        else {
//            return false;
        }

        favouriteMovies.remove(key, value);

        Map<String, String> favouriteMovies3 = new HashMap<>();
        favouriteMovies3.put("Raphael", "Star Wars");
        favouriteMovies3.put("Cristina", "Matrix");
        favouriteMovies3.put("Olivia", "James Bond");
        favouriteMovies3.replaceAll((Afriend, movie) -> movie.toUpperCase());


        Map<String, String> family = Map.ofEntries(
                entry("Teo", "Star Wars"),
                entry("Cristina", "James Bond"));
        Map<String, String> everyone = new HashMap<>(family);
        everyone.putAll(favouriteMovies3);

        Map<String, String> family2 = Map.ofEntries(
                entry("Teo", "Star Wars"),
                entry("Cristina", "James Bond"));
        Map<String, String> friends = Map.ofEntries(
                entry("Raphael", "Star Wars"),
                entry("Cristina", "Matrix"));

        HashMap<String, String> everyone2 = new HashMap<>(family2);
        friends.forEach((k, v) ->
                everyone2.merge(k, v, (movie1, movie2) -> movie1 + " & " + movie2));


        Map<String, Long> moviesToCount = new HashMap<>();
        String movieName = "JamesBond";
        Long count = moviesToCount.get(movieName);
        if(count == null){
            moviesToCount.put(movieName, 1L);
        }else{
            moviesToCount.put(movieName, count + 1);
        }

        moviesToCount.merge(movieName, 1L, (Akey, Acount) -> count + 1L);

        Map<String, Integer> movies2 = new HashMap<>();
        movies2.put("Star Wars", 1);
        movies2.put("Matrix", 2);
        movies2.put("James Bond", 3);
        Iterator<Map.Entry<String, Integer>> iterator = movies2.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, Integer> entry = iterator.next();
            if(entry.getValue() < 10){
                iterator.remove();
            }
        }

        movies2.entrySet().removeIf(entry -> entry.getValue() < 10);
    }
    private byte[] calculateDigest(String key) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return messageDigest.digest(key.getBytes(StandardCharsets.UTF_8));
    }


    public void 개선된ConcurrentHashMap(){

        ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
        long parallelismThreshold = 1;
        Optional<Long> maxValue
                = Optional.ofNullable(map.reduceValues(parallelismThreshold, Long::max));
        long l = map.mappingCount();

    }
}
