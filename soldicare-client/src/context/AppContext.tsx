import { createContext, useContext, useEffect, useState } from "react";
import type { ReactNode } from "react";

interface AppContextType {
  email: string;
  systemID: string;
  username: string;
  isLoggedIn: boolean;
  setEmail: (email: string) => void;
  setSystemID: (id: string) => void;
  setUsername: (id: string) => void;
  logout: () => void;
}

const AppContext = createContext<AppContextType | undefined>(undefined);

export const AppProvider = ({ children }: { children: ReactNode }) => {
  const [email, setEmailState] = useState("");
  const [systemID, setSystemIDState] = useState("");
  const [username, setUsernameState] = useState("");

  const isLoggedIn = !!email && !!systemID && !!username;

  const setEmail = (email: string) => {
    setEmailState(email);
    localStorage.setItem("email", email);
  };

  const setSystemID = (id: string) => {
    setSystemIDState(id);
    localStorage.setItem("systemID", id);
  };

  const setUsername = (username: string) => {
    setUsernameState(username);
    localStorage.setItem("username", username);
  };

  const logout = () => {
    setEmail("");
    setSystemID("");
    setUsername("");
    localStorage.removeItem("email");
    localStorage.removeItem("systemID");
    localStorage.removeItem("username");
  };

  useEffect(() => {
    const savedEmail = localStorage.getItem("email");
    const savedSystemID = localStorage.getItem("systemID");
    const savedUsername = localStorage.getItem("username");

    if (savedEmail) setEmailState(savedEmail);
    if (savedSystemID) setSystemIDState(savedSystemID);
    if (savedUsername) setUsernameState(savedUsername);
  }, []);

  return (
    <AppContext.Provider
      value={{
        email,
        systemID,
        username,
        isLoggedIn,
        setEmail,
        setSystemID,
        setUsername,
        logout,
      }}
    >
      {children}
    </AppContext.Provider>
  );
};

export const useAppContext = (): AppContextType => {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error("useAppContext must be used within AppProvider");
  }
  return context;
};
