package com.example.miniproject_2a;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private String[] localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView timeSlot;
        private final TextView docCount;
        private final TextView pageCount;

        private ListView queue;
        private Button toggleButton;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            timeSlot = (TextView) view.findViewById(R.id.timeSlot);
            docCount = (TextView) view.findViewById(R.id.docCount);
            pageCount = (TextView) view.findViewById(R.id.pageCount);

            queue = view.findViewById(R.id.queue);
            toggleButton = view.findViewById(R.id.showPrintQueue);
            toggleButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(queue.getVisibility()==View.GONE){

                    }else{

                    }
                }
            });
        }

        public TextView getTimeSlot() {
            return timeSlot;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CustomAdapter(String[] dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.session_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTimeSlot().setText(localDataSet[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
