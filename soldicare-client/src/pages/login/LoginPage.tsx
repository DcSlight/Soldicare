import { useState } from "react";
import { Link } from "react-router-dom";
import { Typography, Button, TextField, Alert } from "@mui/material";
import { useAppContext } from "../../context/AppContext";
import { useNavigate } from "react-router-dom";
import { login } from "../../services/authService";
import { buttonStyle, inputStyle } from "../../style/formStyle";

export default function LoginPage() {
  const { setEmail, setSystemID, setUsername } = useAppContext();
  const [formData, setFormData] = useState({ email: "", systemID: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleLogin = async () => {
    setLoading(true);
    setError("");
    try {
      const user = await login({
        email: formData.email,
        systemID: formData.systemID,
      });
      setEmail(user.userId.email);
      setSystemID(user.userId.systemID);
      setUsername(user.username);
      navigate("/map-view");
    } catch (err) {
      setError("Login failed. Please check your credentials.");
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
        label="System ID"
        name="systemID"
        value={formData.systemID}
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
        onClick={handleLogin}
        disabled={loading}
        fullWidth
        sx={buttonStyle}
      >
        {loading ? "Logging in..." : "Log in"}
      </Button>
      <Typography variant="body2" textAlign="center">
        Don’t have an account?{" "}
        <Link
          to="/sign-up"
          style={{ textDecoration: "underline", color: "#3D5943" }}
        >
          Sign up
        </Link>
      </Typography>
    </>
  );
}
