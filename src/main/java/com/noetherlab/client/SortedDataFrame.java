package com.noetherlab.client;

import com.google.common.collect.RowSortedTable;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class SortedDataFrame<R, C, V> {

    final Class<R> rowClass;
    final Class<C> colClass;
    final Class<V> valClass;

    final TreeBasedTable<R, C, V> data;
    final TreeBasedTable<C, String,  Object> metaData;

    public Class<R> getRowClass() {
        return rowClass;
    }
    public Class<C> getColClass() {
        return colClass;
    }
    public Class<V> getValueClass() {
        return valClass;
    }

    public SortedDataFrame<R, C, V> merge(SortedDataFrame<R, C, V> df) {
        this.getData().putAll(df.getData());
        this.getMetaData().putAll(df.getMetaData());
        return this;
    }

    public SortedDataFrame<R, C, V> select(Set<C> columns) {

        SortedDataFrame<R, C, V> res = new SortedDataFrame<>(getRowClass(), getColClass(), getValueClass());

        for(Table.Cell<R, C, V> cell : this.getData().cellSet()) {
            if(columns.contains(cell.getColumnKey())) {
                res.getData().put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
            }
        }

        for(Table.Cell<C, String, Object> cell : this.getMetaData().cellSet()) {
            if(columns.contains(cell.getRowKey())) {
                res.getMetaData().put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
            }
        }

        return res;
    }

    public SortedDataFrame<R, C, V> tail(R fromRow) {

        SortedDataFrame<R, C, V> res = new SortedDataFrame<>(getRowClass(), getColClass(), getValueClass());

        for(Table.Cell<C, String, Object> cell : this.getMetaData().cellSet()) {
            res.getMetaData().put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }

        Comparator<R> comparator = getComparator(getRowClass());
        for(Map.Entry<R, Map<C, V>> row : this.getData().rowMap().entrySet()) {
            if(comparator.compare(row.getKey(), fromRow) > 0) {
                for(Map.Entry<C, V> c : row.getValue().entrySet()) {
                    res.getData().put(row.getKey(), c.getKey(), c.getValue());
                }
            }
        }

        return res;
    }

    public void renameColumn(C fromName, C toName) {

        if(fromName.equals(toName)) {
            return;
        }

        for(Map.Entry<R, V> c : data.columnMap().get(fromName).entrySet()) {
            data.put(c.getKey(), toName, c.getValue());
        }
        data.columnMap().remove(fromName);


        for(Map.Entry<String, Object> r : metaData.rowMap().get(fromName).entrySet()) {
            metaData.put(toName, r.getKey(), r.getValue());
        }
        metaData.rowMap().remove(fromName);

    }

    public R lastDataRow(R lastKey) {
        SortedSet<R> rs = data.rowKeySet();
        if (rs.contains(lastKey)) {
            return lastKey;
        }
        return rs.headSet(lastKey).last();
    }

    public SortedDataFrame<R, C, V> removeColumn(C colName) {
        for(R rowName : this.getData().rowKeySet()) {
            this.getData().remove(rowName, colName);
        }
        return this;
    }


    public static class LocalDateComparator implements Comparator<LocalDate> {
        @Override
        public int compare(LocalDate o1, LocalDate o2) {
            return o1.compareTo(o2);
        }
    }

    public static class LocalDateTimeComparator implements Comparator<LocalDateTime> {
        @Override
        public int compare(LocalDateTime o1, LocalDateTime o2) {
            return o1.compareTo(o2);
        }
    }

    public static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    public static class IntegerComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    }

    public static <C> Comparator<C> getComparator(Class<C> klass) {
        Comparator<C> comparator = null;
        if(klass.equals(LocalDateTime.class)) {
            comparator = (Comparator<C>) new LocalDateTimeComparator();
        } else if(klass.equals(LocalDate.class)) {
            comparator = (Comparator<C>) new LocalDateComparator();
        }

        if(klass.equals(String.class)) {
            comparator = (Comparator<C>) new StringComparator();
        } else if(klass.equals(Integer.class)) {
            comparator = (Comparator<C>) new IntegerComparator();
        }
        return comparator;
    }

    public SortedDataFrame(Class<R> rowClass, Class<C> colClass, Class<V> valClass) {

        this.rowClass = rowClass;
        this.colClass = colClass;
        this.valClass = valClass;

        Comparator<R> rowComparator = getComparator(rowClass);
        Comparator<C> columnComparator = getComparator(colClass);

        this.data = TreeBasedTable.create(rowComparator, columnComparator);
        this.metaData = TreeBasedTable.create(columnComparator, new StringComparator());
    }



    public RowSortedTable<R, C, V> getData() {
        return data;
    }

    public Table<C, String, Object> getMetaData() {
        return metaData;
    }


    public Optional<R> getFirstRow() {
        return getData().rowKeySet().stream().min(getComparator(getRowClass()));
    }

    public Optional<R> getLastRow() {
        return getData().rowKeySet().stream().max(getComparator(getRowClass()));
    }


}
