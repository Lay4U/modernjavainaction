package com.example.modernjavainaction.chapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class ch18 {
    static List<List<Integer>> subsets(List<Integer> list) {
        if (list.isEmpty()) {
            List<List<Integer>> ans = new ArrayList<>();
            ans.add(Collections.emptyList());
            return ans;
        }
        Integer first = list.get(0);
        List<Integer> rest = list.subList(1, list.size());

        List<List<Integer>> subans = subsets(rest);
        List<List<Integer>> subans2 = insertAll(first, subans);
        return concat(subans, subans2);
    }

    static List<List<Integer>> insertAll(Integer first, List<List<Integer>> lists) {
        List<List<Integer>> result = new ArrayList<>();
        for (List<Integer> list : lists) {
            List<Integer> copyList = new ArrayList<>();
            copyList.add(first);
            copyList.addAll(list);
            result.add(copyList);
        }

        return result;
    }

    static List<List<Integer>> concat(List<List<Integer>> a,
                                      List<List<Integer>> b){
        List<List<Integer>> r = new ArrayList<>(a);
        r.addAll(b);
        return r;
    }

    static List<List<Integer>> subsetsToFunctional(List<Integer> list) {
        return list.stream()
                .collect(() -> new ArrayList<>(List.of(Collections.emptyList())),
                        (ans, first) -> ans.addAll(ans.stream()
                                .map(ArrayList::new)
                                .toList()),
                        ArrayList::addAll);
    }

    static int factorialIterative(int n){
        int r = 1;
        for (int i = 1; i <= n; i++) {
            r *= i;
        }
        return r;
    }

    static long factorialRecursive(long n) {
        return n == 1 ? 1 : n * factorialRecursive(n - 1);
    }

    static long factorialStreams(long n) {
        return LongStream.rangeClosed(1, n)
                .reduce(1, (a, b) -> a * b);
    }

    static long factorialTailRecursive(long n) {
        return factorialHelper(1, n);
    }

    static long factorialHelper(long acc, long n) {
        return n == 1 ? acc : factorialHelper(acc * n, n-1);
    }
}
