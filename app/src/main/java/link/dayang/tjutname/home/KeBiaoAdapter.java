package link.dayang.tjutname.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import link.dayang.tjutname.R;
import link.dayang.tjutname.datasource.entity.Course;
import link.dayang.tjutname.datasource.entity.KeBiao;

public class KeBiaoAdapter extends RecyclerView.Adapter<KeBiaoAdapter.KeBiaoViewHolder> {

    private LayoutInflater inflater;
    private KeBiao keBiao;
    private int week = 1;


    public KeBiaoAdapter(HomeActivity activity) {
        inflater = activity.getLayoutInflater();
    }


    public void setKeBiao(KeBiao keBiao) {
        this.keBiao = keBiao;
        notifyDataSetChanged();
    }

    public void setWeek(int week) {
        this.week = week;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KeBiaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_course, parent, false);
        return new KeBiaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeBiaoViewHolder holder, int position) {
        Course course = keBiao.list.get(position).getCourseByWeek(week);
        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        if (keBiao == null) {
            return 0;
        }
        return 5*7;
    }

    public class KeBiaoViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView textView;
        public KeBiaoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            textView = this.itemView.findViewById(R.id.course_title);
        }

        public void bind(Course course) {
            if (course == null) {
                itemView.setVisibility(View.INVISIBLE);
                return;
            }
            itemView.setVisibility(View.VISIBLE);
            textView.setText(course.getTitle() + course.getLocation());
        }
    }
}
