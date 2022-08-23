package com.example.application.views.about;

import com.example.application.views.MainLayout;
import com.example.application.views.map.LatLon;
import com.example.application.views.map.MapComponent;
import com.example.application.views.map.MapMarker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class AboutView extends VerticalLayout {

    public AboutView() {
        MapComponent<MapMarker> mapComponent = new MapComponent("TODO");
        mapComponent.setHeight("800px");
        mapComponent.setWidth("800px");
        mapComponent.setCenter(new LatLon(-28.024, 140.887));
        mapComponent.addMarker(new MapMarker(1, new LatLon(-25.263, 131.144)));
        mapComponent.addMarkerClickListener(event -> {
            Notification.show("Hello ");
        });
        add(mapComponent);
    }

}
