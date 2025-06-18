import React, { useEffect, useState } from "react";
import {
  Backdrop,
  Box,
  Modal,
  Fade,
  Typography,
  IconButton,
  Stack,
  Button,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import EqualizerOutlinedIcon from "@mui/icons-material/EqualizerOutlined";
import UnitGrid from "../UnitGrid/UnitGrid";
import type { UnitData } from "../../interface/UnitData.interface";

const style = {
  position: "absolute" as const,
  top: "40%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: "90%",
  maxHeight: "90vh",
  bgcolor: "background.paper",
  boxShadow: 24,
  p: 3,
  overflowY: "auto",
};

interface ModalUnitProps {
  open: boolean;
  handleOpen: () => void;
  handleClose: () => void;
  handleShowAllUnits: () => void;
  displaySpecificUnit: (unitId: string) => void;
  allUnits: UnitData[];
}

const ModalUnit: React.FC<ModalUnitProps> = ({
  open,
  handleClose,
  displaySpecificUnit,
  handleShowAllUnits,
  allUnits,
}) => {
  const [expandedIndices, setExpandedIndices] = useState<number[]>([]);

  useEffect(() => {
    // Reset expansion when unit list changes
    setExpandedIndices([]);
  }, [allUnits]);

  const toggleExpandAll = () => {
    if (expandedIndices.length === allUnits.length) {
      setExpandedIndices([]);
    } else {
      setExpandedIndices(allUnits.map((_, i) => i));
    }
  };

  const toggleSingle = (index: number) => {
    setExpandedIndices((prev) =>
      prev.includes(index) ? prev.filter((i) => i !== index) : [...prev, index]
    );
  };

  return (
    <Modal
      aria-labelledby="modal-title"
      aria-describedby="modal-description"
      open={open}
      onClose={handleClose}
      closeAfterTransition
      slots={{ backdrop: Backdrop }}
      slotProps={{
        backdrop: {
          timeout: 500,
          sx: { backgroundColor: "rgba(0,0,0,0.2)" },
        },
      }}
    >
      <Fade in={open}>
        <Box sx={style}>
          {/* Header */}
          <Stack
            direction="row"
            justifyContent="space-between"
            alignItems="center"
            mb={2}
          >
            <Stack direction="row" alignItems="center" spacing={1}>
              <EqualizerOutlinedIcon />
              <Typography variant="h6">Statistics per Army Unit</Typography>
            </Stack>
            <Stack direction="row" spacing={2}>
              <Button onClick={toggleExpandAll} sx={{ textTransform: "none" }}>
                {expandedIndices.length === allUnits.length
                  ? "Collapse all"
                  : "Expand all"}
              </Button>
              <Button
                onClick={handleShowAllUnits}
                sx={{ textTransform: "none" }}
              >
                Display Units in map
              </Button>
              <IconButton onClick={handleClose}>
                <CloseIcon />
              </IconButton>
            </Stack>
          </Stack>

          {/* Accordion grid */}
          <UnitGrid
            units={allUnits}
            expandedIndices={expandedIndices}
            onToggle={toggleSingle}
            displaySpecificUnit={displaySpecificUnit}
          />
        </Box>
      </Fade>
    </Modal>
  );
};

export default ModalUnit;
