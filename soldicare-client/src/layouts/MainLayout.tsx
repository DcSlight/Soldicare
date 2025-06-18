import { useState } from "react";
import { Box } from "@mui/material";
import TopNav from "../components/TopNav";
import SideDrawer from "../components/SideDrawer";
import Footer from "../components/Footer";

interface Props {
  children: React.ReactNode;
}

export default function MainLayout({ children }: Props) {
  const [drawerOpen, setDrawerOpen] = useState(false);

  return (
    <Box display="flex" flexDirection="column" minHeight="100vh">
      <TopNav onMenuClick={() => setDrawerOpen(true)} />
      <SideDrawer open={drawerOpen} onClose={() => setDrawerOpen(false)} />

      {/* Main content grows to fill vertical space */}
      <Box sx={{ flexGrow: 1 }}>{children}</Box>

      <Footer />
    </Box>
  );
}
