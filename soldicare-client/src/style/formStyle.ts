// src/styles/formStyles.ts

export const inputStyle = {
  mb: 2,
  "& .MuiOutlinedInput-root": {
    "& fieldset": { borderColor: "#3D5943" },
    "&:hover fieldset": { borderColor: "#3D5943" },
    "&.Mui-focused fieldset": { borderColor: "#3D5943" },
  },
  "& .MuiInputLabel-root": { color: "#757575" },
};

export const buttonStyle = {
  mb: 2,
  backgroundColor: "#3D5943",
  color: "#fff",
  textTransform: "uppercase",
  fontWeight: "bold",
  "&:hover": {
    backgroundColor: "#2f4634",
  },
};
