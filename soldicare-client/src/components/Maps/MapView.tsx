import { useEffect } from "react";
import {
  MapContainer,
  TileLayer,
  Polygon,
  Tooltip,
  Marker,
  Popup,
  useMapEvent,
  CircleMarker,
} from "react-leaflet";
import * as React from "react";
import "leaflet/dist/leaflet.css";
import { Box } from "@mui/material";
import type { LatLngTuple } from "leaflet";
import { useState, Fragment } from "react";
import ModalUnit from "../Modals/ModalUnit";
import type { UnitData } from "../../interface/UnitData.interface";
import CircularProgress from "@mui/material/CircularProgress";
import { getUnitsWithSoldiers } from "../../services/UnitService";
import { useAppContext } from "../../context/AppContext";

const center: LatLngTuple = [31.53454, 34.49364];

function ClickMarker({ onClick }: { onClick: (pos: LatLngTuple) => void }) {
  useMapEvent("click", (e) => {
    const coords: LatLngTuple = [e.latlng.lat, e.latlng.lng];
    onClick(coords);
  });
  return null;
}

export default function MapView() {
  const { systemID, email } = useAppContext();
  const [clickedPos, setClickedPos] = useState<LatLngTuple | null>(null);
  const [open, setOpen] = React.useState<boolean>(true);
  const [allUnits, setAllUnits] = useState<UnitData[]>([]);
  const [unitsToDisplay, setUnitsToDisplay] = useState<UnitData[]>([]);
  const [isLoading, setLoading] = useState<boolean>(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const fetchUnits = async () => {
    try {
      setLoading(true);

      const rawUnits = await getUnitsWithSoldiers(systemID, email);

      const formattedUnits: UnitData[] = rawUnits.map((unit: any) => ({
        id: unit.id,
        name: unit.objectDetails?.name ?? unit.name,
        color: unit.objectDetails?.color ?? unit.color,
        polygon: unit.objectDetails?.polygon ?? unit.polygon,
        hotspots: [],
        status: unit.status,
        severelyWounded:
          unit.objectDetails?.severelyWounded ?? unit.severelyWounded,
        lightlyWounded:
          unit.objectDetails?.lightlyWounded ?? unit.lightlyWounded,
        immobileUnconscious:
          unit.objectDetails?.immobileUnconscious ?? unit.immobileUnconscious,
        dead: unit.objectDetails?.dead ?? unit.dead,
        combatReadyTroops:
          unit.objectDetails?.combatReadyTroops ?? unit.combatReadyTroops,
        soldiers: unit.soldiers ?? [],
      }));

      console.log("formattedUnits: ", formattedUnits);

      setAllUnits(formattedUnits);
      setUnitsToDisplay(formattedUnits);
    } catch (err) {
      console.error("Error fetching units:", err);
    } finally {
      setLoading(false);
    }
  };

  //simulate fetching data
  useEffect(() => {
    fetchUnits();
  }, []);

  const displaySpecificUnit = (unitId: string) => {
    const selectedUnit = allUnits.find((unit) => unit.id.objectId === unitId);
    if (selectedUnit) {
      setUnitsToDisplay([selectedUnit]);
    }
    handleClose();
  };

  const handlePolygonClick = () => {
    handleOpen();
  };

  const handleMapClick = (pos: LatLngTuple) => {
    setClickedPos(pos);
  };

  const handleShowAllUnits = () => {
    setUnitsToDisplay(allUnits);
  };

  return (
    <>
      {isLoading ? (
        <Box
          height="100vh"
          width="100%"
          display="flex"
          justifyContent="center"
          alignItems="center"
        >
          <CircularProgress size={80} thickness={5} />
        </Box>
      ) : (
        <>
          <ModalUnit
            handleOpen={handleOpen}
            open={open}
            handleClose={handleClose}
            handleShowAllUnits={handleShowAllUnits}
            displaySpecificUnit={displaySpecificUnit}
            allUnits={allUnits}
          />
          <Box height="100%" width="100%">
            <MapContainer
              center={center}
              zoom={14.3}
              style={{ height: "100%", width: "100%", zIndex: 1 }}
            >
              <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                attribution="&copy; OpenStreetMap contributors"
              />

              <ClickMarker onClick={handleMapClick} />

              {unitsToDisplay?.map((unit) => (
                <Fragment key={unit.id.objectId}>
                  <Polygon
                    positions={unit.polygon}
                    pathOptions={{ color: unit.color }}
                    eventHandlers={{
                      click: () => handlePolygonClick(),
                    }}
                  >
                    <Tooltip sticky>{unit.name}</Tooltip>
                  </Polygon>

                  {unit.hotspots?.map((pos, i) => (
                    <CircleMarker
                      key={`hotspot-${unit.id}-${i}`}
                      center={pos}
                      radius={10}
                      pathOptions={{
                        color: "black",
                        fillColor: "black",
                        fillOpacity: 0.4,
                      }}
                    />
                  ))}
                </Fragment>
              ))}

              {clickedPos && (
                <Marker position={clickedPos}>
                  <Popup>
                    Lat: {clickedPos[0].toFixed(5)}, Lng:{" "}
                    {clickedPos[1].toFixed(5)}
                  </Popup>
                </Marker>
              )}
            </MapContainer>
          </Box>
        </>
      )}
    </>
  );
}
