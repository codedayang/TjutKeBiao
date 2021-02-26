package link.dayang.tjutname.datasource;


import link.dayang.tjutname.datasource.entity.KeBiao;

public interface GetRawKeBiaoCallback {
    void onFinish(boolean success, KeBiao keBiao, int enrollDate);
}
