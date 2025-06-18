import { useState } from "react";
import { TextField, MenuItem, Button, Alert, Typography } from "@mui/material";
import { Link, useNavigate } from "react-router-dom";
import { signup } from "../../services/authService";
import { Role } from "../../enums/role.enum";
import { useAppContext } from "../../context/AppContext";
import { inputStyle, buttonStyle } from "../../style/formStyle";

export default function SignUpPage() {
  const { setEmail, setSystemID, setUsername } = useAppContext();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    email: "",
    username: "",
    role: Role.END_USER,
    avatar: "",
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async () => {
    setLoading(true);
    setError("");

    try {
      const user = await signup({
        email: formData.email,
        username: formData.username,
        role: formData.role,
        avatar: formData.avatar,
      });

      setEmail(user.userId.email);
      setSystemID(user.userId.systemID);
      setUsername(user.username);

      navigate("/map-view");
    } catch (err) {
      setError("Signup failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <TextField
        label="Email"
        name="email"
        value={formData.email}
        onChange={handleChange}
        fullWidth
        sx={inputStyle}
      />

      <TextField
        label="Username"
        name="username"
        value={formData.username}
        onChange={handleChange}
        fullWidth
        sx={inputStyle}
      />

      <TextField
        select
        label="Role"
        name="role"
        value={formData.role}
        onChange={handleChange}
        fullWidth
        sx={inputStyle}
      >
        {Object.values(Role).map((role) => (
          <MenuItem key={role} value={role}>
            {role}
          </MenuItem>
        ))}
      </TextField>

      <TextField
        label="Avatar"
        name="avatar"
        value={formData.avatar}
        onChange={handleChange}
        fullWidth
        sx={inputStyle}
      />

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <Button
        variant="contained"
        onClick={handleSubmit}
        disabled={loading}
        fullWidth
        sx={buttonStyle}
      >
        {loading ? "Signing up..." : "Sign Up"}
      </Button>

      <Typography variant="body2" textAlign="center">
        Already have an account?{" "}
        <Link
          to="/login"
          style={{ textDecoration: "underline", color: "#3D5943" }}
        >
          Log in
        </Link>
      </Typography>
    </>
  );
}
