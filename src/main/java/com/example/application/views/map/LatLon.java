package com.example.application.views.map;

import java.io.Serializable;

public class LatLon implements Serializable {
  private static final long serialVersionUID = 646346543243L;

  private double lat = 0.0;
  private double lng = 0.0;

  public LatLon(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }

  @Override
  public String toString() {
    return "LatLon [lat=" + lat + ", lon=" + lng + "]";
  }
}
