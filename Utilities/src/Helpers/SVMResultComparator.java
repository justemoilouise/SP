package Helpers;

import java.util.Comparator;

import Data.SVMResult;

public class SVMResultComparator implements Comparator<SVMResult> {
    @Override
    public int compare(SVMResult o1, SVMResult o2) {
        return (Double.toString(o2.getProbability())).compareTo(Double.toString(o1.getProbability()));
    }
}
