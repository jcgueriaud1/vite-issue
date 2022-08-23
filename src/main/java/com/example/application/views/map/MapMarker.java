package com.example.application.views.map;

import com.example.application.views.map.LatLon;

import java.io.Serializable;

/**
 * @author jcgueriaud
 */
public class MapMarker implements Serializable {

    private final long id;

    private final LatLon position;

    private String title;

    public MapMarker(long id, LatLon position) {
        this.id = id;
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public LatLon getPosition() {
        return position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
