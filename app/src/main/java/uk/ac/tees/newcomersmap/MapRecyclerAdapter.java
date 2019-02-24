package uk.ac.tees.newcomersmap;

import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MapRecyclerAdapter extends RecyclerView.Adapter<MapRecyclerAdapter.MapHolder> {

    private List<NewcomerMap> maps = new ArrayList<>();
    private Geocoder geocoder;
    private OnItemClickListener onItemClickListener;

    public MapRecyclerAdapter(Geocoder geocoder) {
        super();
        this.geocoder = geocoder;
    }

    @NonNull
    @Override
    public MapHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_newcomer_map, parent, false);
        return new MapHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MapHolder holder, int position) {
        NewcomerMap currentMap = maps.get(position);
        holder.textViewTitle.setText(currentMap.getTitle());
        int markerCount = 0;
        for (UserMarker marker : maps.get(position).getMarkers()) {
            markerCount++;
        }
        holder.textViewMarkers.setText(markerCount);

       // Use Geocoder and reverse-geolocate nearest address
        double latitude = currentMap.getLocation().getLatitude();
        double longitude = currentMap.getLocation().getLongitude();
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(latitude,longitude,1);
            Address address = addressList.get(0);
            String location = address.getAddressLine(0) + ", "
                    + address.getCountryCode();
            holder.textViewLocation.setText(location);
        } catch (IOException e) {
            e.printStackTrace();
            holder.textViewLocation.setText("geocoding error");
        }
    }

    @Override
    public int getItemCount() {
        return maps.size();
    }

    public void setMaps(List<NewcomerMap> maps) {
        this.maps = maps;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    class MapHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewLocation;
        private TextView textViewMarkers;

        public MapHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textView_title);
            textViewLocation = itemView.findViewById(R.id.textView_location);
            textViewMarkers = itemView.findViewById(R.id.textView_markers);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(maps.get(position));
                    }
                }
            });
        }

    }

    protected interface OnItemClickListener {
        void onItemClick(NewcomerMap map);
    }
}
