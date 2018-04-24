package com.example.triprecycler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by hend on 02/04/18.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private List<Trip> tripList;
    private Context mContext;
    private OnItemClickListener mListener;
    DatabaseReference reference;

    public interface OnItemClickListener{
        void onItemClick(int postition);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public MyAdapter(List<Trip> trips, Context mContext) {
        this.tripList = trips;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Trip trip = tripList.get(position);

        //Toast.makeText(mContext, trip.getTripName(), Toast.LENGTH_SHORT).show();
        Log.i("tagggg",trip.getTripId());

        holder.txtTripTitle.setText(trip.getTripName());
        holder.txtTripDate.setText(trip.getStartDate());
//
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onItemClick(position);
                }

            }
        });


        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.txtOptionDigit);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.mnu_item_edit:
                                Toast.makeText(mContext, "edit", Toast.LENGTH_SHORT).show();
                                Intent toEditIntent = new Intent(mContext,EditActivity.class);
                                toEditIntent.putExtra("id",trip.getTripId());
                                Log.i("IDDDD",trip.getTripId());
                                mContext.startActivity(toEditIntent);
                                break;
                            case R.id.mnu_item_delete:
                                tripList.remove(position);
                                reference = FirebaseDatabase.getInstance().getReference("DataBase").child("Trips");
                                Query query = reference.orderByChild("tripId").equalTo(trip.getTripId());

                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // dataSnapshot is the "issue" node with all children with id 0
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                // do something with the individual "issues"
                                                snapshot.getRef().removeValue();


                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                notifyDataSetChanged();
                                Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        public TextView txtTripTitle;
        public TextView txtTripDate;
        public TextView txtOptionDigit;

        // view holder constructor
        public ViewHolder(final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            txtTripTitle = itemView.findViewById(R.id.tripName);
            txtTripDate = itemView.findViewById(R.id.tripDate);
            txtOptionDigit = itemView.findViewById(R.id.txtOptionDigit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
