package link.dayang.tjutname.datasource.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//KeBiao是所有Slot的集合，读取的顺序是从左到右，从上到下
public class KeBiao {
    public List<Slot> list;

    public KeBiao() {
        list = new ArrayList<>();
    }

    public void add(Slot slot) {
        list.add(slot);
    }

    public int size() {
        return list.size();
    }

    @Override
    public String toString() {
        return "KeBiao{" +
                "list=" + list +
                '}';
    }
}
