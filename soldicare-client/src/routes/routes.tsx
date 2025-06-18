import { Route, Routes } from "react-router-dom";
import LoginPage from "../pages/login/LoginPage";
import AboutPage from "../pages/AboutPage/AboutPage";
import HomePage from "../pages/Home/HomePage";
import SignUpPage from "../pages/SignUp/SignUpPage";
import MapViewPage from "../pages/MapPage/MapViewPage";
import MainLayout from "../layouts/MainLayout";
import SoldierAlert from "../pages/SoldierAlert/SoldierAlert";
import AuthLayout from "../layouts/AuthLayout";
import SoliderDashboard from "../pages/SoldierPage/SoliderDashboard";

export default function AppRoutes() {
  return (
    <Routes>
      <Route
        path="/"
        element={
          <MainLayout>
            <HomePage />
          </MainLayout>
        }
      />
      <Route
        path="/login"
        element={
          <AuthLayout title="Welcome To SoldiCare">
            <LoginPage />
          </AuthLayout>
        }
      />
      <Route path="/map-view" element={<MapViewPage />} />
      <Route
        path="/sign-up"
        element={
          <AuthLayout title="Welcome To SoldiCare">
            <SignUpPage />
          </AuthLayout>
        }
      />
      <Route
        path="/about"
        element={
          <MainLayout>
            <AboutPage />
          </MainLayout>
        }
      />
      <Route
        path="soldier-dashboard"
        element={
          <MainLayout>
            <SoliderDashboard />
          </MainLayout>
        }
      />
      <Route
        path="soldier-alert"
        element={
          <MainLayout>
            <SoldierAlert />
          </MainLayout>
        }
      />
    </Routes>
  );
}
