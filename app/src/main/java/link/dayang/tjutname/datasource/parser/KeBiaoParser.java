package link.dayang.tjutname.datasource.parser;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import link.dayang.tjutname.datasource.entity.Course;
import link.dayang.tjutname.datasource.entity.KeBiao;
import link.dayang.tjutname.datasource.entity.Slot;


public class KeBiaoParser {
    public static KeBiao parse(String raw) {
        KeBiao keBiao = new KeBiao();
        Document document = Jsoup.parse(raw);
        Elements trs = document.select("tr");
        int i = 0;
        for (Element tr : trs) {
            if (i % 2 == 0) {
                i++;
                continue;
            }

            Log.v("dydy", "tr count: " + i);
            Elements tds = tr.select("td");
            int skipCount = 2;
            for (Element td : tds) {
                if (keBiao.size() > 35) return keBiao;
                //跳过前两个个td（左表头）
                if (skipCount > 0) {
                    skipCount--;
                    continue;
                }

                String tdHtml = td.html();

                //无课程
                Slot slot = new Slot();
                if (tdHtml.equals("&nbsp;") || tdHtml.contains("&amp;nbs p;")) {
                    Log.v("dydy", "空空");
                    Log.v("dydy", "====================");
                    keBiao.add(slot);

                    continue;
                }

                // 判断多个课同一槽位
                String[] courses = tdHtml.split("<hr>");
                int coursesCount = courses.length;

                Log.v("dydy", coursesCount + "门课同一槽位");

                int ci = 0;
                //一个槽位只有一个无意义div需要跳过
                boolean isFirstItem = true;
                for (String rawCourse : courses) {
                    Course course = new Course();
                    Log.v("dydy", "第" + ++ci + "门");
                    String[] items = rawCourse.split("&nbsp;");

                    if (items.length == 0) continue;

                    ArrayList<Integer> weeks = new ArrayList<>();
                    ArrayList<String> cur = new ArrayList<>(List.of(items));
                    if (cur.get(0).contains("<div")) {
                        cur.remove(0);
                    }
                    String last = cur.remove(cur.size() - 1);
                    cur.addAll(List.of(last.split("<br>")));
                    for (int itemIndex = 0; itemIndex < cur.size(); itemIndex++) {

                        //跳过第一个(无意义div)
//                        if (isFirstItem) {
//                            isFirstItem = false;
//                            cur = cur.subList(1, items.length);
//                            itemIndex--;
//                            continue;
//                        }
                        String item = cur.get(itemIndex);

                        switch (itemIndex) {
                            case 0:
                                course.setTitle(item.trim());
                                break;
                            case 1:
                                String[] weekStrs = item.split(",");
                                for (String weekStr : weekStrs) {
                                    int weekFrom = Integer.parseInt(weekStr.split("-")[0]);
                                    String weekToStr = weekStr.split("-")[1].split("周")[0];
                                    int weekTo = Integer.parseInt(weekToStr);
                                    boolean isNormal = true;
                                    boolean isDanZhou = false;
                                    if (weekStr.contains("单")) {
                                        isDanZhou = true;
                                        isNormal = false;
                                    } else if (weekStr.contains("双")) {
                                        isDanZhou = false;
                                        isNormal = false;
                                    }
                                    for (int wi = weekFrom; wi <= weekTo; wi++) {
                                        if (isNormal) {
                                            weeks.add(wi);
                                        } else {
                                            if (isDanZhou) {
                                                if (wi % 2 == 1) weeks.add(wi);
                                            } else {
                                                if (wi % 2 == 0) weeks.add(wi);
                                            }
                                        }

                                    }
                                }
                                break;
                            case 2:
                                break;
                            case 3:
                                course.setTeacher(item.trim());
                                break;
                            case 4:
                                course.setLocation(item.trim());
                                break;
                        }
                    }
                    slot.addCourse(course, weeks);

                    Log.v("dydy", "============");
                }
                Log.v("dydy", "====================");

                keBiao.add(slot);

            }
            i++;
            Log.v("dydy", "=========================");
        }

        return keBiao;
    }

}
