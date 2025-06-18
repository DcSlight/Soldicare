import { Box, Paper, Typography } from "@mui/material";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import { Icon } from "leaflet";
import type { LatLngTuple } from "leaflet";

interface Location {
  lat: string;
  lng: string;
}

interface MapSectionProps {
  location: Location;
}

const MapSection = ({ location }: MapSectionProps) => {
  const center: LatLngTuple = [
    parseFloat(location.lat),
    parseFloat(location.lng),
  ];

  const defaultIcon = new Icon({
    iconUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",
    iconSize: [25, 41],
    iconAnchor: [12, 41],
  });

  return (
    <Box sx={{ height: "100%" }}>
      <Paper
        sx={{
          p: 2,
          height: "100%",
          display: "flex",
          flexDirection: "column",
          transition: "0.2s",
          "&:hover": {
            backgroundColor: "#f0f0f0",
          },
        }}
      >
        <Typography variant="h6" gutterBottom>
          Location
        </Typography>

        <Box sx={{ flexGrow: 1 }}>
          <MapContainer
            center={center}
            zoom={14}
            style={{ height: "100%", width: "100%", borderRadius: 8 }}
            scrollWheelZoom={false}
          >
            <TileLayer
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
              attribution='&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors'
            />
            <Marker position={center} icon={defaultIcon}>
              <Popup>This is the soldier's last known location.</Popup>
            </Marker>
          </MapContainer>
        </Box>
      </Paper>
    </Box>
  );
};

export default MapSection;
