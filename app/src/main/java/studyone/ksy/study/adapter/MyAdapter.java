package studyone.ksy.study.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import studyone.ksy.study.R;
import studyone.ksy.study.model.Thing;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private ArrayList<Thing> thingList;

    public MyAdapter() {
        super();
    }

    public MyAdapter(ArrayList<Thing> thingList) {
        this.thingList = thingList;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder( holder, position, payloads );
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId( position );
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.item_list, viewGroup, false);

        return new ViewHolder( v );
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder viewHolder, int i) {
        ViewHolder vh = viewHolder;
        Thing t = thingList.get( i );

        vh.thingName.setText( t.getName() );
        vh.thingCost.setText( t.getCost() + "" );
    }

    @Override
    public int getItemCount() {
        return thingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView thingName, thingCost;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            thingName = itemView.findViewById( R.id.thingName );
            thingCost = itemView.findViewById( R.id.thingCost );
        }
    }
}
