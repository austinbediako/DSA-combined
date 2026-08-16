package gh.edu.ug.dsaoptimizer.algorithms;

import java.util.Comparator;

public class MergeSort {

    public static <T> void sort(T[] array, Comparator<? super T> comparator) {
        if (array == null || array.length <= 1 || comparator == null) {
            return;
        }
        mergeSort(array, 0, array.length - 1, comparator);
    }

    private static <T> void mergeSort(T[] array, int left, int right, Comparator<? super T> comparator) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(array, left, mid, comparator);
            mergeSort(array, mid + 1, right, comparator);
            merge(array, left, mid, right, comparator);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void merge(T[] array, int left, int mid, int right, Comparator<? super T> comparator) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Object[] L = new Object[n1];
        Object[] R = new Object[n2];

        for (int i = 0; i < n1; ++i) L[i] = array[left + i];
        for (int j = 0; j < n2; ++j) R[j] = array[mid + 1 + j];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (comparator.compare((T) L[i], (T) R[j]) <= 0) {
                array[k] = (T) L[i];
                i++;
            } else {
                array[k] = (T) R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = (T) L[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = (T) R[j];
            j++;
            k++;
        }
    }
}