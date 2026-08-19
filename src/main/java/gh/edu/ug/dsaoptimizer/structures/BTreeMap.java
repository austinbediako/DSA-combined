package gh.edu.ug.dsaoptimizer.structures;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple B-Tree (in-memory) with configurable minimum degree t >= 2. Supports insert and search. No delete.
 * Keys must be comparable.
 */
public class BTreeMap<K extends Comparable<K>, V> {
    private final int t; // minimum degree
    private BNode root;

    private class BNode {
        int n = 0; // keys count
        Object[] keys;
        Object[] vals;
        BNode[] children;
        boolean leaf = true;
        BNode() {
            keys = new Object[2 * t - 1];
            vals = new Object[2 * t - 1];
            children = new BNode[2 * t];
        }
    }

    public BTreeMap(int t) {
        if (t < 2) throw new IllegalArgumentException("t>=2");
        this.t = t;
        root = new BNode();
    }

    public V get(K key) {
        return search(root, key);
    }

    @SuppressWarnings("unchecked")
    private V search(BNode x, K k) {
        int i = 0;
        while (i < x.n && k.compareTo((K)x.keys[i]) > 0) i++;
        if (i < x.n && k.compareTo((K)x.keys[i]) == 0) return (V) x.vals[i];
        if (x.leaf) return null;
        return search(x.children[i], k);
    }

    public void put(K key, V val) {
        BNode r = root;
        if (r.n == 2 * t -1) {
            BNode s = new BNode(); s.leaf = false; s.n = 0; s.children[0] = r; root = s; splitChild(s,0); insertNonFull(s, key, val);
        } else insertNonFull(r, key, val);
    }

    @SuppressWarnings("unchecked")
    private void insertNonFull(BNode x, K k, V v) {
        int i = x.n -1;
        if (x.leaf) {
            while (i >=0 && k.compareTo((K)x.keys[i]) < 0) {
                x.keys[i+1] = x.keys[i]; x.vals[i+1] = x.vals[i]; i--;
            }
            x.keys[i+1] = k; x.vals[i+1] = v; x.n++;
        } else {
            while (i>=0 && k.compareTo((K)x.keys[i]) < 0) i--;
            i++;
            if (x.children[i].n == 2*t -1) { splitChild(x,i); if (k.compareTo((K)x.keys[i]) > 0) i++; }
            insertNonFull(x.children[i], k, v);
        }
    }

    @SuppressWarnings("unchecked")
    private void splitChild(BNode x, int i) {
        BNode y = x.children[i];
        BNode z = new BNode(); z.leaf = y.leaf; z.n = t - 1;
        for (int j = 0; j < t-1; j++) { z.keys[j] = y.keys[j+t]; z.vals[j] = y.vals[j+t]; }
        if (!y.leaf) for (int j = 0; j < t; j++) z.children[j] = y.children[j+t];
        y.n = t -1;
        for (int j = x.n; j >= i+1; j--) x.children[j+1] = x.children[j];
        x.children[i+1] = z;
        for (int j = x.n-1; j >= i; j--) { x.keys[j+1] = x.keys[j]; x.vals[j+1] = x.vals[j]; }
        x.keys[i] = y.keys[t-1]; x.vals[i] = y.vals[t-1];
        x.n = x.n + 1;
    }

    public List<K> keysInOrder() {
        ArrayList<K> out = new ArrayList<>();
        inorder(root, out);
        return out;
    }

    @SuppressWarnings("unchecked")
    private void inorder(BNode x, List<K> out) {
        int i;
        for (i = 0; i < x.n; i++) {
            if (!x.leaf) inorder(x.children[i], out);
            out.add((K)x.keys[i]);
        }
        if (!x.leaf) inorder(x.children[i], out);
    }
}
