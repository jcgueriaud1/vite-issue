import {Loader, LoaderStatus} from "@googlemaps/js-api-loader"
import {MarkerClusterer} from "@googlemaps/markerclusterer";

interface Window {
    initGoogleMapLazy: (c: HTMLElement) => void;
}

interface ServerMapInterface {
    onClickListener: (id: number) => void;
}

interface MapMarker {
    id: number;
    position: google.maps.LatLng;
    title?: string;
    map?: google.maps.Map;
}

interface GoogleMapContainer extends HTMLElement {
    $connector: GoogleMapConnector;
    $server: ServerMapInterface;
}

class GoogleMapConnector {
    constructor(loader: Loader, c : GoogleMapContainer) {
        this.loader = loader;
        this.$server = c.$server;
        loader.load().then(() => {
            this.setMap(new google.maps.Map(c, {
                zoom: 8,
            }));
            this.markerClusterer = new MarkerClusterer({ map: this.map! });
            this.loaded = true;
            if (this.center) {
                this.map!.setCenter(this.center);
            }
            if (this.zoom) {
                this.map!.setZoom(this.zoom);
            }
            this.markers = [...this.markers, ...this.temporaryMarkers.map(serverMarker => {
                let marker = new google.maps.Marker({
                    position: serverMarker.position,
                    title: serverMarker.title,
                });

                // markers can only be keyboard focusable when they have click listeners
                // open info window when marker is clicked
                marker.addListener("click", () => {
                    this.$server.onClickListener(serverMarker.id);
                });

                return marker;
            })];
            if (this.markers.length > 0) {
                this.setMapOnAll(this.map!);
            }
        });
    }

    loaded: boolean = false;
    map: google.maps.Map | null = null;
    center?: google.maps.LatLng;
    zoom?: number;
    markers: google.maps.Marker[] = [];
    loader: Loader;
    temporaryMarkers: MapMarker[] = [];
    markerClusterer?: MarkerClusterer;
    $server: ServerMapInterface;


    public setCenter(latLong: google.maps.LatLng) : void {
        this.center = latLong;
        if (this.map) {
            this.map.setCenter(latLong);
        }
    }

    public setZoom(zoom: number) : void {
        this.zoom = zoom;
        if (this.map) {
            this.map.setZoom(zoom);
        }
    }

    public setMapOnAll(map: google.maps.Map | null) {
        if (map) {
            this.markerClusterer!.addMarkers(this.markers);
        } else {
            this.markerClusterer!.clearMarkers();
        }
    }

    public setMap(map : google.maps.Map) {
        this.map = map;
    }

    public addMarker(serverMarker : MapMarker) {
        if (this.loaded) {
            const marker = new google.maps.Marker({
                position: serverMarker.position,
                map: this.map,
                title: serverMarker.title,
            });
            this.markers.push(marker);
            marker.setMap(this.map);
        } else {
            this.temporaryMarkers = [...this.temporaryMarkers, serverMarker];
        }
    }

    public addMarkers(serverMarkers: MapMarker[]) {
        if (this.loaded) {
            this.markers = [...this.markers, ...serverMarkers.map(serverMarker => {
                return new google.maps.Marker({
                    position: serverMarker.position,
                    title: serverMarker.title,
                });
            })];
            this.setMapOnAll(this.map);
        } else {
            this.temporaryMarkers = [...this.temporaryMarkers, ...serverMarkers];
        }
    }
    public removeAllMarkers() : void {
        this.setMapOnAll(null);
        this.markers = [];
        this.temporaryMarkers = [];
    }

}
function initGoogleMapLazy(c : GoogleMapContainer, apiKey: string ): void {
    // Check whether the connector was already initialized
    if (c.$connector) {
        return;
    }

    const loader = new Loader({
        apiKey: apiKey,
        version: "weekly",
    });
    c.$connector = new  GoogleMapConnector(loader, c);

}

(window as any).initGoogleMapLazy = initGoogleMapLazy;
