package jp.ne.sakura.vopaldragon.bib.item.estm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class HashCounter<K> {


    public void print() {
        for (K key : getKeySet()) {
            System.out.println(key + "\t" + getCount(key));
        }
    }

    public static interface Matcher<K> {

        boolean matches(K key);
    }

    public int matchCount(Matcher<K> matcher) {
        int count = 0;
        for (Count c : countMap.values()) {
            if (matcher.matches(c.key)) {
                count += c.count;
            }
        }
        return count;
    }

    public int totalCount() {
        int count = 0;
        for (Count c : countMap.values()) {
            count += c.count;
        }
        return count;
    }

    public void sort(boolean accend) {
        ArrayList<Count> counts = new ArrayList<Count>(countMap.values());
        if (accend) {
            Collections.sort(counts);
        } else {
            Collections.sort(counts, Collections.reverseOrder());
        }
        countMap.clear();
        for (Count c : counts) {
            countMap.put(c.key, c);
        }
    }

    public Set<K> getKeySet() {
        return countMap.keySet();
    }

    public List<K> getKeyList() {
        return new ArrayList<K>(getKeySet());
    }

    private class Count implements Comparable<Count> {

        K key;

        public Count(K key) {
            this.key = key;
        }
        int count = 0;

        @Override
        public String toString() {
            return Integer.toString(count);
        }

        @Override
        public int compareTo(Count o) {
            return count - o.count;
        }
    }
    private LinkedHashMap<K, Count> countMap = new LinkedHashMap<K, Count>();

    public void countPlus(K key) {
        if (key == null) {
            return;
        }
        get(key).count++;
    }

    public void countMinus(K key) {
        if (key == null) {
            return;
        }
        get(key).count--;
    }

    private Count get(K key){
        Count c = countMap.get(key);
        if (c == null) {
            c = new Count(key);
            countMap.put(key, c);
        }
        return c;
    }

    public void setCount(K key, int count) {
        if (key == null) {
            return;
        }
        get(key).count = count;
    }

    public void countValue(K key, int count) {
        if (key == null) {
            return;
        }
        get(key).count += count;
    }

    public int getCount(K key) {
        if (key == null) {
            return 0;
        }
        Count c = countMap.get(key);
        if (c == null) {
            return 0;
        } else {
            return c.count;
        }
    }

    @Override
    public String toString() {
        return countMap.toString();
    }
}
