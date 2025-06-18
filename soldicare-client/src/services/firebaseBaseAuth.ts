// src/firebase/firebaseBaseAuth.ts

import { getAuth, signInAnonymously, onAuthStateChanged } from "firebase/auth";
import type { User } from "firebase/auth";
import { app } from "../firebase";

const auth = getAuth(app);

/**
 * Ensures the user is authenticated anonymously (only once).
 * @returns Promise that resolves with the Firebase User
 */
export const ensureAnonymousAuth = (): Promise<User> => {
  return new Promise((resolve, reject) => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      unsubscribe(); // Avoid multiple calls

      if (user) {
        resolve(user);
      } else {
        signInAnonymously(auth)
          .then((cred) => resolve(cred.user))
          .catch((err) => reject(err));
      }
    });
  });
};
