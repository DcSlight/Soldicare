import {
  Box,
  Typography,
  Paper,
  Chip,
  Fab,
  Menu,
  MenuItem,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import { useState } from "react";

const initialCategories = [
  {
    title: "Allergies",
    items: ["Peanuts", "Penicillin"],
    options: ["Dust", "Lactose", "Shellfish", "Pollen"],
  },
  {
    title: "Medical Conditions",
    items: ["Diabetes Type 1", "Asthma", "Hypertension"],
    options: ["Cancer", "COPD", "Celiac"],
  },
  {
    title: "Medications",
    items: ["Ventolin", "Metformin"],
    options: ["Paracetamol", "Ibuprofen", "Aspirin"],
  },
];

const MedicalInfoCards = () => {
  const [categories, setCategories] = useState(initialCategories);
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [activeCategoryIndex, setActiveCategoryIndex] = useState<number | null>(
    null
  );

  const handleAddClick = (
    event: React.MouseEvent<HTMLElement>,
    index: number
  ) => {
    setAnchorEl(event.currentTarget);
    setActiveCategoryIndex(index);
  };

  const handleOptionClick = (option: string) => {
    if (activeCategoryIndex === null) return;

    const updated = [...categories];
    const current = updated[activeCategoryIndex];

    if (!current.items.includes(option)) {
      current.items.push(option);
    }

    setCategories(updated);
    setAnchorEl(null);
    setActiveCategoryIndex(null);
  };

  return (
    <Box
      mt={10}
      display="grid"
      gridTemplateColumns={{ xs: "1fr", md: "repeat(3, 1fr)" }}
      gap={2}
    >
      {categories.map((cat, index) => (
        <Paper
          key={cat.title}
          elevation={1}
          sx={{
            p: 2,
            height: "100%",
            position: "relative",
            display: "flex",
            flexDirection: "column",
            gap: 2,
            minHeight: 250,
          }}
        >
          <Typography variant="h6">{cat.title}</Typography>

          <Box display="flex" gap={1} flexWrap="wrap">
            {cat.items.map((item) => (
              <Chip
                key={item}
                label={item}
                variant="outlined"
                sx={{ borderRadius: "8px" }}
              />
            ))}
          </Box>

          <Fab
            size="small"
            sx={{
              position: "absolute",
              bottom: 16,
              right: 16,
              backgroundColor: "#c39696",
            }}
            onClick={(e) => handleAddClick(e, index)}
          >
            <AddIcon />
          </Fab>
        </Paper>
      ))}
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={() => {
          setAnchorEl(null);
          setActiveCategoryIndex(null);
        }}
        PaperProps={{
          elevation: 4,
          sx: {
            borderRadius: 2,
            minWidth: 180,
            mt: 1,
            boxShadow: "0px 4px 12px rgba(0, 0, 0, 0.1)",
            bgcolor: "#fff",
            "& .MuiMenuItem-root": {
              borderRadius: 1,
              px: 2,
              py: 1,
              typography: "body2",
              transition: "0.2s",
              "&:hover": {
                backgroundColor: "#f5f5f5",
              },
              "&.Mui-disabled": {
                opacity: 0.5,
              },
            },
          },
        }}
      >
        {activeCategoryIndex !== null &&
          categories[activeCategoryIndex].options.map((option) => (
            <MenuItem
              key={option}
              onClick={() => handleOptionClick(option)}
              disabled={categories[activeCategoryIndex].items.includes(option)}
            >
              {option}
            </MenuItem>
          ))}
      </Menu>
    </Box>
  );
};

export default MedicalInfoCards;
