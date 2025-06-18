// firebase.ts
import { initializeApp } from "firebase/app";
import { getStorage } from "firebase/storage";

const firebaseConfig = {
  apiKey: "AIzaSyB7ZrDztFVO_pDhSbrAjZF2T3SLR42cufY",
  authDomain: "soldicare-3cd45.firebaseapp.com",
  projectId: "soldicare-3cd45",
  storageBucket: "soldicare-3cd45.firebasestorage.app",
  messagingSenderId: "481616200986",
  appId: "1:481616200986:web:1d6c2e0f12aaa818ffd8a3",
  measurementId: "G-LJY2T5V8ZY",
};
export const app = initializeApp(firebaseConfig);
export const storage = getStorage(app);
