package link.dayang.tjutname.datasource;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import link.dayang.tjutname.datasource.parser.EnrollDateParser;
import link.dayang.tjutname.datasource.parser.KeBiaoParser;

public class KeBiaoDataSource {
    private static KeBiaoDataSource INSTANCE;
    private final Executor mainThread = new MainThreadExecutor();
    private final Executor networkThread = Executors.newFixedThreadPool(3);

    private KeBiaoDataSource() {
    }

    public static KeBiaoDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new KeBiaoDataSource();
        }
        return INSTANCE;
    }

    public void getKeBiao(String id, String password, int grade, int term, GetRawKeBiaoCallback callback) {
        networkThread.execute(() -> {
            try {
                KeBiaoFetcher.getRawKeBiao(id, password, "2019-2020-1");
                // 2019 + grade 2  - 1 = 2020
                String jbxx = KeBiaoFetcher.fetchJbxx();
//                Log.v("dydy", jbxx);
                int date = Integer.parseInt(EnrollDateParser.parse(jbxx));
                int start = date + grade - 1;
                int end = start + 1;
                String semester = start + "-" + end + "-" + term;
                Log.v("dydy", semester);
                String raw = KeBiaoFetcher.getRawKeBiao(id, password, semester);
                mainThread.execute(() -> callback.onFinish(true, KeBiaoParser.parse(raw), date));
            } catch (Exception e) {
                e.printStackTrace();

                mainThread.execute(() -> callback.onFinish(false, null, 0));
            }

        });
    }

    private static class MainThreadExecutor implements Executor {
        private Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }

}
