// src/pages/MapViewPage.tsx

import { useState } from "react";
import { Box } from "@mui/material";
import MapView from "../../components/Maps/MapView";
import TopNav from "../../components/TopNav";
import SideDrawer from "../../components/SideDrawer";

export default function MapViewPage() {
  const [drawerOpen, setDrawerOpen] = useState(false);

  return (
    <Box sx={{ width: "100vw", height: "100vh", overflow: "hidden" }}>
      <TopNav onMenuClick={() => setDrawerOpen(true)} />
      <SideDrawer open={drawerOpen} onClose={() => setDrawerOpen(false)} />

      <Box sx={{ height: "calc(100vh - 64px)" }}>
        <MapView />
      </Box>
    </Box>
  );
}
