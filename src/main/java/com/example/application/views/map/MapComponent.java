package com.example.application.views.map;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.internal.JsonUtils;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonArray;
import org.yaml.snakeyaml.error.Mark;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jcgueriaud
 */
@NpmPackage(value = "@googlemaps/js-api-loader", version = "^1.14.3")
@NpmPackage(value = "@googlemaps/markerclusterer", version = "^2.0.3")
@NpmPackage(value = "@types/google.maps", version = "^3.49.2")
// @NpmPackage(value = "@types/supercluster", version = "^7.1.0")
@JsModule("./map-loader.ts")
public class MapComponent<MARKER extends MapMarker> extends Div {

    private final List<MARKER> markers = new ArrayList<>();

    public MapComponent(String apiKey) {
        initConnector(apiKey);
    }

    private void initConnector(String apiKey) {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.initGoogleMapLazy($0, $1)",
                getElement(), apiKey));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void setCenter(LatLon center) {
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.setCenter", JsonUtils.beanToJson(center)));
    }

    public void setZoom(int zoom) {
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.setZoom", zoom));
    }

    public void addMarker(MARKER marker) {
        markers.add(marker);
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.addMarker", JsonUtils.beanToJson(marker)));
    }

    public void addMarkers(List<MARKER> markers) {
        markers.addAll(markers);
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.addMarkers", JsonUtils.listToJson(markers)));
    }

    public void removeAllMarkers() {
        markers.clear();
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.removeAllMarkers"));
    }

    @ClientCallable
    private void onClickListener(int id) {
        markers.stream().filter(m -> m.getId() == id).findFirst().ifPresentOrElse(marker -> {
            fireEvent(new MarkerClickEvent(this, true, marker));
        }, () -> {

        });
    }

    public Registration addMarkerClickListener(ComponentEventListener<MarkerClickEvent> listener) {
        return addListener(MarkerClickEvent.class, listener);
    }

    public static class MarkerClickEvent extends ComponentEvent<MapComponent> {

        private final MapMarker marker;

        public MarkerClickEvent(MapComponent source, boolean fromClient, MapMarker marker) {
            super(source, fromClient);
            this.marker = marker;
        }

        public MapMarker getMarker() {
            return marker;
        }
    }
}
